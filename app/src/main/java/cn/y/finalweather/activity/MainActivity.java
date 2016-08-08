package cn.y.finalweather.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.squareup.okhttp.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.y.finalweather.R;
import cn.y.finalweather.adapter.NowWeatherAdapter;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.City;
import cn.y.finalweather.model.CityInfo;
import cn.y.finalweather.model.Condition;
import cn.y.finalweather.model.ConditionInfo;
import cn.y.finalweather.model.HeWeather;
import cn.y.finalweather.service.FinalWeatherService;
import cn.y.finalweather.util.OkHttpClientManager;

/**
 * 和风天气api KEY：9c121f3f984f4dde86917788a38b9956
 * 国内城市：allchina、 热门城市：hotworld、 全部城市：allworld
 * 城市列表接口： https://api.heweather.com/x3/citylist?search=类型&key=你的认证key
 * 城市天气接口： https://api.heweather.com/x3/weather?cityid=城市ID&key=你的认证key
 */

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SurfaceHolder.Callback {

    public static final String FULL_CITY_URL = "https://api.heweather.com/x3/citylist?search=allworld&key=9c121f3f984f4dde86917788a38b9956";
    //    private String CONDITION_URL = "https://api.heweather.com/x3/condition?search=allcond&key=9c121f3f984f4dde86917788a38b9956";
    public static final int MSG_NET_CON = 0x00;
    public static final int MSG_PROGRESS_CHANGED = 0x01;
    public static final String TAG = "MainActivity";
    private FinalWeatherDB db;
    private MediaPlayer mediaPlayer1;
    public static Handler handler;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;
    private RecyclerView rv;
    private HeWeather weather;
    private ActionBar actionBar;
    private SurfaceView sv;
    private NowWeatherAdapter adapter;
    private SwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sv = (SurfaceView) findViewById(R.id.sv);
        //设置播放时打开屏幕
        sv.getHolder().setKeepScreenOn(true);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);

        rv = (RecyclerView) findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        rv.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        rv.setItemAnimator(new DefaultItemAnimator());

        srl.setColorSchemeResources(android.R.color.holo_orange_dark, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        srl.setOnRefreshListener(this);
        db = FinalWeatherDB.getFinalWeatherDB(MainActivity.this);
        progressDialog1 = new ProgressDialog(MainActivity.this);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setLogo(R.drawable.ic_city_gps);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
        weather = db.getWeather(sp);
        if (weather.getBasic().getCity() != null) {
            actionBar.setTitle(weather.getBasic().getCity());
            sv.getHolder().addCallback(this);
            Log.d(TAG, "103: weather.getBasic().getCity() != null");

            adapter = new NowWeatherAdapter(this, weather);
            rv.setAdapter(adapter);

        } else {
//            saveConditionInDb();
            Intent intent = new Intent(this, SearchableActivity.class);
//            saveCityInDb(intent);
            if (writeDB())
                startActivityForResult(intent, 0);
            else Log.e(TAG, "复制数据库失败");
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog1.setTitle(TAG);
                progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog1.setMessage("正在加载中。。。");
                switch (msg.what) {
                    case MSG_NET_CON:
                        MainActivity.this.progressDialog.dismiss();
                        progressDialog1.setMax(msg.arg1);
                        break;
                    case MSG_PROGRESS_CHANGED:
                        progressDialog1.show();
                        progressDialog1.setProgress(msg.arg1);//size = 53039
                        if (msg.arg1 >= msg.arg2 - 1)
                            progressDialog1.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        Intent intent = new Intent(this, FinalWeatherService.class);
        startService(intent);
        //saveInDb();


//        Gson gson = new Gson();
//        String jsonString = "{\"city_info\":[{\"city\":\"丘北\",\"cnty\":\"中国\",\"id\":\"CN101290606\",\"lat\":\"24.107000\",\"lon\":\"104.073000\",\"prov\":\"云南\"}," +
//                                            "{\"city\":\"广南\",\"cnty\":\"中国\",\"id\":\"CN101290607\",\"lat\":\"23.981000\",\"lon\":\"105.054000\",\"prov\":\"云南\"}],\"status\":\"ok\"}";
//        Log.d("MainActivity",jsonString);
//        Type type = new TypeToken<List<City>>(){}.getType();
//        CityInfo ls = gson.fromJson(jsonString,CityInfo.class);
//        List<City> cities = ls.getCity_info();
//        Log.d("MainActivity",cities.get(0).getCity());
//        tv.setText(cities.get(0).getCity()+"\n"+cities.get(1).getCity());
    }

    private void saveCityInDb(final Intent intent) {
        progressDialog = ProgressDialog.show(MainActivity.this, TAG, "网络访问中。。。。");
//        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        OkHttpClientManager.getAsyn(FULL_CITY_URL, new OkHttpClientManager.ResultCallback<CityInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(CityInfo response) {
//                progressDialog.dismiss();
                final List<City> cities = response.getCity_info();
                final List<City> cities1 = db.loadCities(null);
                if (cities != cities1) {
                    new Thread() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = MSG_NET_CON;
                            msg.arg1 = cities.size();
                            handler.sendMessage(msg);
                            for (City city : cities) {
                                Message msg1 = new Message();
                                msg1.what = MSG_PROGRESS_CHANGED;
                                msg1.arg1 = cities.indexOf(city);
                                msg1.arg2 = cities.size();
                                handler.sendMessage(msg1);
                                if (cities1.size() > cities.indexOf(city)) {
                                    Log.d(TAG, cities1.get(cities.indexOf(city)).getCity() + ",读出：" + cities1.get(cities.indexOf(city)).getId());
                                    if (!city.getId().equals(cities1.get(cities.indexOf(city)).getId())) {
                                        db.updateCityDB(city, (cities.indexOf(city) + 1) + "");
                                    }
                                } else {
                                    db.saveCity(city);
                                    Log.d(TAG, city.getCity() + ",已存入数据库：" + city.getId());
                                }
//                    progressDialog.setProgress(cities.indexOf(city));
//                    db.saveCity(city);
                            }
                            Log.d(TAG, "全部已存入数据库!");
                            if (intent != null) {
                                startActivityForResult(intent, 0);
                            }
                        }


                    }.start();
                }
//                progressDialog.dismiss();
            }
        });
    }

//    private void saveConditionInDb() {
//        OkHttpClientManager.getAsyn(CONDITION_URL, new OkHttpClientManager.ResultCallback<ConditionInfo>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(ConditionInfo response) {
//                List<Condition> conditions = response.getCond_info();
//                List<Condition> conditionList = db.getCondition(new String[]{});
//                Log.d(TAG, "conditions: " + conditions.size() + "\nconditionList: " + conditionList.size());
//                if (conditionList != conditions) {
//                    for (Condition condition : conditions) {
//                        if (conditionList.size() > conditions.indexOf(condition)) {
////                            Log.d(TAG, conditionList.get(conditions.indexOf(condition)).getCode() + ",读出：" + conditionList.get(conditions.indexOf(condition)).getIcon());
//                            if (condition.getCode() != conditionList.get(conditions.indexOf(condition)).getCode()) {
//                                db.updateConditionDB(condition, (conditions.indexOf(condition) + 1) + "");
//                            }
//                        } else {
//                            db.saveCondition(condition);
////                            Log.d(TAG, "已存入数据库!" + condition.getIcon());
//                        }
//                    }
//                }
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        rv.setAdapter(new NowWeatherAdapter(this, weather));
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    String cityId = data.getExtras().getString("cityId");
//                    Log.d("cityId", cityId);
                    if (cityId != null) {
                        refreshWeather(cityId);
                    }
                }
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
//        searchView.setBackgroundColor(0xff000000);
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, SearchableActivity.class);
                Log.d(TAG, db.getCityNum(null) + "");
                if (db.getCityNum(null) < 1) {
                    saveCityInDb(intent);
                } else {
                    startActivityForResult(intent, 0);
                }
                return false;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 从网络获取天气数据，并更新本地数据
     *
     * @param cityId :如果传入一个cityId则从网络获取这个Id对应的天气数据，如果不传入则更新本地城市数据
     */
    private void refreshWeather(String cityId) {
        weather = new HeWeather();
        srl.setRefreshing(true);
        SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
        HeWeather dbWeather = db.getWeather(sp);
        String dbCityId = dbWeather.getBasic().getId();
        cityId = cityId == null ? dbCityId : cityId;
        Log.d("CityCum", cityId + "<----->" + dbCityId);
        String WEATHER_URL = "https://api.heweather.com/x3/weather?cityid=" + cityId + "&key=9c121f3f984f4dde86917788a38b9956";
        if (cityId.endsWith("A")) {
            Snackbar.make(srl, "抱歉！由于资金不足暂时不支持景点天气，换个城市试试吧!", Snackbar.LENGTH_LONG)
                    .setAction("重选", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, SearchableActivity.class);
                            startActivityForResult(intent, 0);
                        }
                    })
                    .show();
            return;
        }
        OkHttpClientManager.getAsyn(WEATHER_URL, new OkHttpClientManager.ResultCallback<Map<String, List<HeWeather>>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, List<HeWeather>> response) {
                if (response != null) {
                    HeWeather heWeather = response.get("HeWeather data service 3.0").get(0);
                    String city = heWeather.getBasic().getCity();
                    actionBar.setTitle(city);
                    sv.getHolder().removeCallback(MainActivity.this);
                    sv.getHolder().addCallback(MainActivity.this);
                    Log.d(TAG, "sv.getHolder().addCallback(MainActivity.this);");
                    SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
                    db.saveWeather(heWeather, sp, false);
                    rv.setAdapter(new NowWeatherAdapter(MainActivity.this, heWeather));
                    srl.setRefreshing(false);
//                                    if (heWeather.getNow() != null) {
//                                        Log.d("HeWeather", heWeather.getNow().getFl() + "");
//                                    } else Log.d("HeWeather", "response.getNow()=null");
//                                    if (heWeather.getStatus() != null) {
//                                        Log.d("HeWeather", heWeather.getStatus() + "");
//                                        tv.setText(heWeather.getStatus());
//                                    } else Log.d("HeWeather", "response.getStatus()=null");
                } else Log.d("HeWeather", "response = null");
            }
        });

    }

    @Override
    public void onRefresh() {
        SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
        weather = db.getWeather(sp);
        refreshWeather(weather.getBasic().getId());

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setSurface();
    }

    private void setSurface() {

        mediaPlayer1 = new MediaPlayer();
        Log.d(TAG, "surfaceCreated");
        try {
            String uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video100;
            mediaPlayer1.setLooping(true);
            // 把视频输出到SurfaceView上
            mediaPlayer1.setDisplay(sv.getHolder());
            mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);

            if (weather.getBasic().getUpdate().getLoc() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = dateFormat.parse(weather.getBasic().getUpdate().getLoc());
                Calendar calendar = dateFormat.getCalendar();
                calendar.setTime(date);
                int nowDate = calendar.get(Calendar.HOUR_OF_DAY);//设置为HOUR得到的是12小时制的时间，使用HOUR_OF_DAY得到的是24小时制的时间
                int srDate = Integer.valueOf(weather.getDaily_forecast().get(0).getAstro().getSr().substring(0, 2));//本地存储的日出时间
                int ssDate = Integer.valueOf(weather.getDaily_forecast().get(0).getAstro().getSs().substring(0, 2));//本地存储的日落时间
                Log.d(TAG, ssDate + "：当前时间==" + weather.getNow().getCond().getCode());
                switch (Integer.valueOf(weather.getNow().getCond().getCode())) {
                    case 100:
                        if (nowDate > srDate && nowDate < ssDate) {
                        } else {
                            uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video_night100;
                        }
                        break;
                    case 101:
                    case 102:
                        if (nowDate > srDate && nowDate < ssDate) {
                            uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video101;
                        } else {
                            uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video_night101;
                        }
                        break;
                    case 103:
                    case 104:
                        if (nowDate > srDate && nowDate < ssDate) {
                            uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video103;
                        } else {
                            uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video_night103;
                        }
                        break;
                    case 200:
                    case 201:
                    case 202:
                    case 203:
                    case 204:
                    case 205:
                    case 206:
                    case 207:
                    case 208:
                    case 209:
                    case 210:
                    case 211:
                    case 212:
                    case 213:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video200;
                        break;
                    case 302:
                    case 303:
                    case 304:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video302;
                        break;
                    case 300:
                    case 301:
                    case 305:
                    case 306:
                    case 307:
                    case 308:
                    case 309:
                    case 310:
                    case 311:
                    case 312:
                    case 313:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video305;
                        break;
                    case 400:
                    case 401:
                    case 402:
                    case 403:
                    case 407:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video400;
                        break;
                    case 404:
                    case 405:
                    case 406:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video406;
                        break;
                    case 500:
                    case 501:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video501;
                        break;
                    case 502:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video502;
                        break;
                    case 503:
                    case 504:
                    case 506:
                    case 507:
                    case 508:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video503;
                        break;
                    case 900:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video900;
                        break;
                    case 901:
                        uriString = "android.resource://" + MainActivity.this.getPackageName() + "/" + R.raw.video901;
                        break;

                }
            } else {
                Log.d(TAG, "weather.getBasic().getUpdate().getLoc() == null");
            }
            Uri uri = Uri.parse(uriString);//Uri uri=Uri.paese("android.resource://包名/"+R.raw.xxx);
            mediaPlayer1.setDataSource(MainActivity.this, uri);//"android:resource://包名/"+R.raw.xxx
            mediaPlayer1.prepare();
            mediaPlayer1.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("surfaceChanged", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mediaPlayer1 != null) {
            if (mediaPlayer1.isPlaying())
                mediaPlayer1.stop();
            mediaPlayer1.release();
        }
    }

    public void nullClick(View view) {
    }

    public boolean writeDB() {
        Log.e(TAG, "文件系统：" + this.getDatabasePath("final_weather.db").getPath());
        String f = this.getDatabasePath("final_weather.db").getPath();//此处如果是放在应用包名的目录下,自动放入“databases目录下
        File file = new File("/data/data/" + getPackageName() + "/databases/");
        File file1 = new File(f);
        FileOutputStream fout = null;
        InputStream inputStream = null;
        try {
            inputStream = getResources().openRawResource(R.raw.weather);

            if (!file.exists())
                if (file.mkdir())
                    Log.d(TAG, file.getAbsolutePath() + "创建成功");
            if (!file1.exists())
                if (file1.createNewFile())
                    Log.d(TAG, file1.getAbsolutePath() + "创建成功");
            fout = new FileOutputStream(new File(f));
            byte[] buffer = new byte[128];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fout.write(buffer, 0, len);
            }
            buffer = null;

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

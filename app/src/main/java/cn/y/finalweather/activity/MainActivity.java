package cn.y.finalweather.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import java.util.ArrayList;
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
import cn.y.finalweather.util.OkHttpClientManager;

/**
 * 和风天气api KEY：9c121f3f984f4dde86917788a38b9956
 * 国内城市：allchina、 热门城市：hotworld、 全部城市：allworld
 * 城市列表接口： https://api.heweather.com/x3/citylist?search=类型&key=你的认证key
 * 城市天气接口： https://api.heweather.com/x3/weather?cityid=城市ID&key=你的认证key
 */

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String FULL_CITY_URL = "https://api.heweather.com/x3/citylist?search=allworld&key=9c121f3f984f4dde86917788a38b9956";
    private String CONDITION_URL = "https://api.heweather.com/x3/condition?search=allcond&key=9c121f3f984f4dde86917788a38b9956";
    public static final int MSG_NET_CON = 0x00;
    public static final int MSG_PROGRESS_CHANGED = 0x01;
    public static final String TAG = "MainActivity";
    private FinalWeatherDB db;
    public static Handler handler;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;
    private RecyclerView rv;
    private HeWeather weather;
    private ActionBar actionBar;
    private SwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        srl= (SwipeRefreshLayout) findViewById(R.id.srl);

        rv = (RecyclerView) findViewById(R.id.rv_main);
        List<HeWeather> weathers = new ArrayList<>();
        weathers.add(weather);
        rv.setAdapter(new NowWeatherAdapter(this,weathers));

        srl.setColorSchemeResources(android.R.color.holo_orange_dark, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        srl.setOnRefreshListener(this);
        db = FinalWeatherDB.getFinalWeatherDB(MainActivity.this);
        progressDialog1 = new ProgressDialog(MainActivity.this);
        actionBar = getSupportActionBar();
        SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
        HeWeather weather = db.getWeather(sp);
        if (weather.getBasic().getCity()!=null) {
            actionBar.setTitle(weather.getBasic().getCity());
        }else {
            saveConditionInDb();
            Intent intent = new Intent(this,SearchableActivity.class);
            saveCityInDb(intent);
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
                            if (intent != null)
                                startActivityForResult(intent, 0);
                        }


                    }.start();
                }
//                progressDialog.dismiss();
            }
        });
    }

    private void saveConditionInDb() {
        OkHttpClientManager.getAsyn(CONDITION_URL, new OkHttpClientManager.ResultCallback<ConditionInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ConditionInfo response) {
                List<Condition> conditions = response.getCond_info();
                List<Condition> conditionList = db.getCondition(new String[]{});
                Log.d(TAG, "conditions: " + conditions.size() + "\nconditionList: " + conditionList.size());
                if (conditionList != conditions) {
                    for (Condition condition : conditions) {
                        if (conditionList.size() > conditions.indexOf(condition)) {
//                            Log.d(TAG, conditionList.get(conditions.indexOf(condition)).getCode() + ",读出：" + conditionList.get(conditions.indexOf(condition)).getIcon());
                            if (condition.getCode() != conditionList.get(conditions.indexOf(condition)).getCode()) {
                                db.updateConditionDB(condition, (conditions.indexOf(condition) + 1) + "");
                            }
                        } else {
                            db.saveCondition(condition);
//                            Log.d(TAG, "已存入数据库!" + condition.getIcon());
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    /**
     * 从网络获取天气数据，并更新本地数据
     * @param cityId :如果传入一个cityId则从网络获取这个Id对应的天气数据，如果不传入则更新本地城市数据
     */
    private void refreshWeather(String cityId) {
        weather = new HeWeather();
        SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
        HeWeather dbWeather = db.getWeather(sp);
        String dbCityId = dbWeather.getBasic().getId();
        cityId = cityId == null ? dbCityId : cityId;
        Log.d("CityCum", cityId + "<----->" + dbCityId);
        String WEATHER_URL = "https://api.heweather.com/x3/weather?cityid=" + cityId + "&key=9c121f3f984f4dde86917788a38b9956";
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
                    SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
                    db.saveWeather(heWeather, sp, false);
                    //TODO:将Weather对象加到UI中
//                                    if (heWeather.getNow() != null) {
//                                        Log.d("HeWeather", heWeather.getNow().getFl() + "");
//                                    } else Log.d("HeWeather", "response.getNow()=null");
//                                    if (heWeather.getStatus() != null) {
//                                        Log.d("HeWeather", heWeather.getStatus() + "");
//                                        tv.setText(heWeather.getStatus());
//                                    } else Log.d("HeWeather", "response.getStatus()=null");
                } else Log.d("HeWeather", "response=null");
            }
        });

    }
    @Override
    public void onRefresh() {
        new Handler() .postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                srl.setRefreshing(false);
            }
        }, 5000); // 5秒后发送消息，停止刷新
    }
}

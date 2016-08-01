package cn.y.finalweather.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import java.util.List;

import cn.y.finalweather.R;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.City;
import cn.y.finalweather.model.CityInfo;
import cn.y.finalweather.model.Condition;
import cn.y.finalweather.model.ConditionInfo;
import cn.y.finalweather.util.OkHttpClientManager;

/**
 * 和风天气api KEY：9c121f3f984f4dde86917788a38b9956
 * 国内城市：allchina、 热门城市：hotworld、 全部城市：allworld
 * 城市列表接口： https://api.heweather.com/x3/citylist?search=类型&key=你的认证key
 * 城市天气接口： https://api.heweather.com/x3/weather?cityid=城市ID&key=你的认证key
 */

public class MainActivity extends AppCompatActivity {

    public static final String FULL_CITY_URL = "https://api.heweather.com/x3/citylist?search=allworld&key=9c121f3f984f4dde86917788a38b9956";
    private String CONDITION_URL = "https://api.heweather.com/x3/condition?search=allcond&key=9c121f3f984f4dde86917788a38b9956";
    public static final int MSG_NET_CON = 0x00;
    public static final int MSG_PROGRESS_CHANGED = 0x01;
    public static final String TAG = "MainActivity";
    private FinalWeatherDB db;
    private Handler handler;
    private TextView tv;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        db = FinalWeatherDB.getFinalWeatherDB(MainActivity.this);
        progressDialog1 = new ProgressDialog(MainActivity.this);
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
        saveConditionInDb();
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
                                startActivityForResult(intent,0);
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
        switch (requestCode){
            case 0:
                if (resultCode==RESULT_OK)
                {
                    String cityId = data.getExtras().getString("cityId");
                    Log.d("cityId",cityId);
                    String WEATHER_URL = "https://api.heweather.com/x3/weather?cityid="+cityId+"&key=9c121f3f984f4dde86917788a38b9956";
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
                    startActivityForResult(intent,0);
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

}

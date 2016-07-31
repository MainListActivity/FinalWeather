package cn.y.finalweather.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import java.lang.reflect.Type;
import java.util.List;

import cn.y.finalweather.R;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.City;
import cn.y.finalweather.model.CityInfo;
import cn.y.finalweather.util.OkHttpClientManager;

/**
 * 和风天气api KEY：9c121f3f984f4dde86917788a38b9956
 * 国内城市：allchina、 热门城市：hotworld、 全部城市：allworld
 * 城市列表接口： https://api.heweather.com/x3/citylist?search=类型&key=你的认证key
 * 城市天气接口： https://api.heweather.com/x3/weather?cityid=城市ID&key=你的认证key
 */

public class MainActivity extends AppCompatActivity {

    public static String FULL_CITY_URL = "https://api.heweather.com/x3/citylist?search=allworld&key=9c121f3f984f4dde86917788a38b9956";
    public static String WEATHER_URL = "https://api.heweather.com/x3/weather?cityid=CN101290606&key=9c121f3f984f4dde86917788a38b9956";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        OkHttpClientManager.getAsyn(FULL_CITY_URL, new OkHttpClientManager.ResultCallback<CityInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                tv.setText(request.toString());
            }

            @Override
            public void onResponse(CityInfo response) {
                FinalWeatherDB db = FinalWeatherDB.getFinalWeatherDB(MainActivity.this);
                List<City> cities = response.getCity_info();
                for (City city : cities)
                    db.saveCity(city);
                tv.setText(response.getCity_info().get(0).getCnty());
            }
        });
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


}

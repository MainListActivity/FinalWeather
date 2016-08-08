package cn.y.finalweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.squareup.okhttp.Request;

import java.util.List;
import java.util.Map;

import cn.y.finalweather.receiver.AutoUpDateReceiver;
import cn.y.finalweather.application.FinalApplication;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.HeWeather;
import cn.y.finalweather.util.OkHttpClientManager;

/**
 * =============================================
 * 版权：版权所有(C) 2016
 * 作者： y
 * 版本：1.0
 * 创建日期： 2016/8/7
 * 描述：
 * 修订历史：
 * =============================================
 */
public class FinalWeatherService extends Service {

    private FinalWeatherDB db;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                refreshWeather(null);
            }
        }.start();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        SharedPreferences sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        int hour = sp.getInt("hour", 6) * 60 * 60 * 1000;
//        int hour = 5000;
        long triggerAtTime = SystemClock.elapsedRealtime() + hour;
        Intent i = new Intent(this, AutoUpDateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 从网络获取天气数据，并更新本地数据
     *
     * @param cityId :如果传入一个cityId则从网络获取这个Id对应的天气数据，如果不传入则更新本地城市数据
     */
    private void refreshWeather(String cityId) {
        Context context = FinalApplication.getFinalApplicationContext();
        db = FinalWeatherDB.getFinalWeatherDB(context);
        SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
        HeWeather dbWeather = db.getWeather(sp);
        String dbCityId = dbWeather.getBasic().getId();
        cityId = cityId == null ? dbCityId : cityId;
        Log.d("CityCum", cityId + "<----->" + dbCityId);
        String WEATHER_URL = "https://api.heweather.com/x3/weather?cityid=" + cityId + "&key=9c121f3f984f4dde86917788a38b9956";
        if (cityId == null || cityId.endsWith("A")) {
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
                    SharedPreferences[] sp = new SharedPreferences[]{getSharedPreferences("basic", MODE_PRIVATE), getSharedPreferences("now", MODE_PRIVATE), getSharedPreferences("suggestion", MODE_PRIVATE)};
                    db.saveWeather(heWeather, sp, false);
                } else Log.d("HeWeather", "response = null");
            }
        });

    }

}

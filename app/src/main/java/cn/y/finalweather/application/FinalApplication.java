package cn.y.finalweather.application;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.List;

import cn.y.finalweather.R;
import cn.y.finalweather.activity.MainActivity;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.Condition;
import cn.y.finalweather.model.HeWeather;

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
public class FinalApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getFinalApplicationContext() {
        return context;
    }

    public static void buildNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.notification_content);
        FinalWeatherDB db = FinalWeatherDB.getFinalWeatherDB(context);
        SharedPreferences[] sp = new SharedPreferences[]{context.getSharedPreferences("basic", Context.MODE_PRIVATE), context.getSharedPreferences("now", Context.MODE_PRIVATE), context.getSharedPreferences("suggestion", Context.MODE_PRIVATE)};
        HeWeather dbWeather = db.getWeather(sp);
        views.setTextViewText(R.id.tv_notify_cond_txt, dbWeather.getNow().getCond().getTxt());
        List<Condition> conds = db.getCondition(new String[]{});
        for (Condition cond : conds) {
            if (dbWeather.getNow().getCond().getCode().equals(cond.getCode())) {
                views.setImageViewResource(R.id.iv_notify_cond_code, conds.indexOf(cond) + R.drawable.weather_100);
            }
        }
        views.setTextViewText(R.id.tv_notify_city, dbWeather.getBasic().getCity());
        views.setTextViewText(R.id.tv_notify_refresh_date, dbWeather.getBasic().getUpdate().getLoc());
        views.setTextViewText(R.id.tv_notify_wind_dir, dbWeather.getNow().getWind().getDir());
        views.setTextViewText(R.id.tv_notify_tmp, dbWeather.getNow().getTmp());
        views.setTextViewText(R.id.tv_notify_tmp_max, dbWeather.getDaily_forecast().get(0).getTmp().getMax());
        views.setTextViewText(R.id.tv_notify_tmp_min, dbWeather.getDaily_forecast().get(0).getTmp().getMin());

        builder.setContent(views);
        builder.setOngoing(true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.main_icon_notify);
        notificationManager.notify(122, builder.build());
    }

}

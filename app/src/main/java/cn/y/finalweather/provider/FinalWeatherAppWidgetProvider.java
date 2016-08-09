package cn.y.finalweather.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cn.y.finalweather.R;
import cn.y.finalweather.activity.MainActivity;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.Condition;
import cn.y.finalweather.model.HeWeather;
import cn.y.finalweather.service.FinalWeatherService;

/**
 * =============================================
 * 版权：版权所有(C) 2016
 * 作者： y
 * 版本：1.0
 * 创建日期： 2016/8/8
 * 描述：
 * 修订历史：
 * =============================================
 */
public class FinalWeatherAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        ComponentName thisWidget = new ComponentName(context,
                FinalWeatherAppWidgetProvider.class);
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.appwidget_provider);
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.TIME_TICK")) {
            setWidget(context, views);
        }
        AppWidgetManager appmanager = AppWidgetManager.getInstance(context);
//        Log.d("UPUP", intent.getAction());
        appmanager.updateAppWidget(thisWidget, views);
    }

    private void setWidget(Context context, RemoteViews views) {
        String[] numStr = new String[]{"一", "二", "三", "四", "五", "六", "日"};

        FinalWeatherDB db = FinalWeatherDB.getFinalWeatherDB(context);
        SharedPreferences[] sp = new SharedPreferences[]{context.getSharedPreferences("basic", Context.MODE_PRIVATE), context.getSharedPreferences("now", Context.MODE_PRIVATE), context.getSharedPreferences("suggestion", Context.MODE_PRIVATE)};
        HeWeather dbWeather = db.getWeather(sp);
        views.setTextViewText(R.id.tv_weather_text, dbWeather.getNow().getCond().getTxt());
        List<Condition> conds = db.getCondition(new String[]{});
        for (Condition cond : conds) {
            if (dbWeather.getNow().getCond().getCode().equals(cond.getCode())) {
                views.setImageViewResource(R.id.iv_widget_weather, conds.indexOf(cond) + R.drawable.weather_100);
            }
        }

        Calendar c = Calendar.getInstance();

        String yd = new SimpleDateFormat("yyyy.MM.dd").format(c.getTime());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int week = c.getFirstDayOfWeek();

        int minuteShi = minute / 10;
        int minuteGe = minute % 10;
        int hourShi = hour / 10;
        int hourGe = hour % 10;
        views.setImageViewResource(R.id.iv_widget_time_minute_shi, R.mipmap.org4_widget_nw0 + minuteShi);
        views.setImageViewResource(R.id.iv_widget_time_minute_ge, R.mipmap.org4_widget_nw0 + minuteGe);
        views.setImageViewResource(R.id.iv_widget_time_hour_shi, R.mipmap.org4_widget_nw0 + hourShi);
        views.setImageViewResource(R.id.iv_widget_time_hour_ge, R.mipmap.org4_widget_nw0 + hourGe);
        views.setTextViewText(R.id.tv_widget_yd, yd);
        if (week < 8 && week > 0) {
            views.setTextViewText(R.id.tv_widget_week, numStr[week]);
        }


        views.setTextViewText(R.id.tv_widget_fl, dbWeather.getNow().getTmp());
        views.setTextViewText(R.id.tv_widget_city, dbWeather.getBasic().getCity());
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("FinalWeatherApp", "onUpdate");
        int appWidgetId = appWidgetIds[0];
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.appwidget_provider);
//        为按钮绑定点击事件处理器
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("appWidgetId", appWidgetId);
//        Log.i(TAG,"ID:"+(intent.getExtras()).getInt("appWidgetId"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.ll_widget_content, pendingIntent);
        setWidget(context, views);
        context.startService((new Intent(context, FinalWeatherService.class)));
        // 给View 上的两个按钮绑定事件，这里是广播消息的发送
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

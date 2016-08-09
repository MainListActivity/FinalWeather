package cn.y.finalweather.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import cn.y.finalweather.service.FinalWeatherService;
import cn.y.finalweather.service.TimeTickService;

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
public class AutoUpDateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        Log.d("AutoUpDateReceiver", intent.getAction() + "\nbootStart" + sp.getBoolean("bootStart", true) + "\nautoUpdate" + sp.getBoolean("autoUpdate", true));
        if (intent.getAction() == null) {
            Intent i = new Intent(context, FinalWeatherService.class);
            context.startService(i);
        } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (sp.getBoolean("bootStart", true)) {
                Intent i = new Intent(context, FinalWeatherService.class);
                context.startService(i);
                context.startService(new Intent(context, TimeTickService.class));
            }
        } else if (intent.getAction().equals("android.intent.action.TIME_TICK")) {

        }
    }
}

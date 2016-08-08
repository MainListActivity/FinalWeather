package cn.y.finalweather.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.y.finalweather.application.FinalApplication;
import cn.y.finalweather.provider.FinalWeatherAppWidgetProvider;
import cn.y.finalweather.receiver.AutoUpDateReceiver;

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
public class TimeTickService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        FinalApplication.getFinalApplicationContext().registerReceiver(new FinalWeatherAppWidgetProvider(), filter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

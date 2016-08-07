package cn.y.finalweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.y.finalweather.service.FinalWeatherService;

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
        Intent i = new Intent(context, FinalWeatherService.class);
        context.startService(i);
    }
}
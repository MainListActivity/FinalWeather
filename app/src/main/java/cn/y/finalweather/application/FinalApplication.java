package cn.y.finalweather.application;

import android.app.Application;
import android.content.Context;

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
}

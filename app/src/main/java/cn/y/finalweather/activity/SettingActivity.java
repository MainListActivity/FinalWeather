package cn.y.finalweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import cn.y.finalweather.R;

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
public class SettingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_setting);
    }
}

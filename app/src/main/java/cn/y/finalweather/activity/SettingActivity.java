package cn.y.finalweather.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class SettingActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextView tv_setting_hour;
    private SwitchCompat switch_setting_auto;
    private SwitchCompat switch_setting_notify;
    private SwitchCompat switch_setting_boot_start;

    private LinearLayout ll_setting_update_hour;
    private LinearLayout ll_setting_no_auto;

    private AlertDialog alertDialog;
    private EditText et_setting_hour;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv_setting_hour = (TextView) findViewById(R.id.tv_setting_hour);
        switch_setting_auto = (SwitchCompat) findViewById(R.id.switch_setting_auto);
        switch_setting_notify = (SwitchCompat) findViewById(R.id.switch_setting_notify);
        switch_setting_boot_start = (SwitchCompat) findViewById(R.id.switch_setting_boot_start);

        switch_setting_auto.setOnClickListener(this);
        switch_setting_notify.setOnClickListener(this);
        switch_setting_boot_start.setOnClickListener(this);
        findViewById(R.id.ll_setting_tmp).setOnClickListener(this);
        findViewById(R.id.ll_setting_push_notify).setOnClickListener(this);
        findViewById(R.id.ll_setting_auto_update).setOnClickListener(this);
        findViewById(R.id.ll_setting_boot_start).setOnClickListener(this);
        ll_setting_no_auto = (LinearLayout) findViewById(R.id.ll_setting_no_auto);
        ll_setting_update_hour = (LinearLayout) findViewById(R.id.ll_setting_update_hour);
        ll_setting_update_hour.setOnClickListener(this);

        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        tv_setting_hour.setText(String.valueOf(sp.getInt("hour", 6)));
        switch_setting_auto.setChecked(sp.getBoolean("autoUpdate", true));
        switch_setting_boot_start.setChecked(sp.getBoolean("bootStart", true));
        switch_setting_notify.setChecked(sp.getBoolean("notify", true));
        if (!switch_setting_auto.isChecked()) {
            ll_setting_update_hour.setClickable(false);
            ll_setting_no_auto.setVisibility(View.VISIBLE);
        } else {
            ll_setting_update_hour.setClickable(true);
            ll_setting_no_auto.setVisibility(View.GONE);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onClick(View v) {

        SharedPreferences.Editor editor = sp.edit();
        switch (v.getId()) {
            case R.id.ll_setting_tmp://设置温度显示的单位
                break;
            case R.id.ll_setting_push_notify://设置是否显示通知
                switch_setting_notify.setChecked(!switch_setting_notify.isChecked());
            case R.id.switch_setting_notify://设置是否显示通知
                editor.putBoolean("notify", switch_setting_notify.isChecked());
                break;
            case R.id.ll_setting_auto_update://设置是否自动更新
                switch_setting_auto.setChecked(!switch_setting_auto.isChecked());
            case R.id.switch_setting_auto://设置是否自动更新
                editor.putBoolean("autoUpdate", switch_setting_auto.isChecked());
                if (!switch_setting_auto.isChecked()) {
                    ll_setting_update_hour.setClickable(false);
                    ll_setting_no_auto.setVisibility(View.VISIBLE);
                } else {
                    ll_setting_update_hour.setClickable(true);
                    ll_setting_no_auto.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_setting_update_hour://设置自动更新的时间间隔
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.alert_dialog_content);
                et_setting_hour = (EditText) window.findViewById(R.id.et_setting_hour);
                window.findViewById(R.id.bt_dialog_false).setOnClickListener(this);
                window.findViewById(R.id.bt_dialog_true).setOnClickListener(this);
                et_setting_hour.setText(tv_setting_hour.getText());
                et_setting_hour.setSelection(et_setting_hour.getText().length());
                et_setting_hour.addTextChangedListener(this);
                break;
            case R.id.ll_setting_boot_start://设置开机自动启动
                switch_setting_boot_start.setChecked(!switch_setting_boot_start.isChecked());
            case R.id.switch_setting_boot_start:
                editor.putBoolean("bootStart", switch_setting_boot_start.isChecked());
                break;
            case R.id.bt_dialog_true:
                editor.putInt("hour", Integer.valueOf(et_setting_hour.getText().toString().trim()));
                tv_setting_hour.setText(et_setting_hour.getText().toString().trim());
                alertDialog.dismiss();
                break;
            case R.id.bt_dialog_false:
                alertDialog.dismiss();
                break;

        }
        editor.apply();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().trim().equals("-") || s.toString().trim().equals("+") || s.toString().trim().equals("0"))
            s.clear();
        else if (s.length() > 0) {
            if (Integer.parseInt(s.toString().trim()) > 100)
                s.delete(s.length() - 1, s.length());
        }
    }
}

package cn.y.finalweather.adapter;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.y.finalweather.R;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.Condition;
import cn.y.finalweather.model.HeWeather;

/**
 * =============================================
 * 版权：版权所有(C) 2016
 * 作者： y
 * 版本：1.0
 * 创建日期： 2016/8/3
 * 描述：
 * 修订历史：
 * =============================================
 */
public class NowWeatherAdapter extends RecyclerView.Adapter<NowWeatherAdapter.MyViewHolder> {
    private HeWeather mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    public static final String TAG = "NowWeatherAdapter";

    public NowWeatherAdapter(Context context, HeWeather datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.item_now_weather, parent, false);
                break;
            case 1:
                view = inflater.inflate(R.layout.item_daily_weather, parent, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.item_hourly_weather, parent, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.item_now_suggestion, parent, false);
                break;
        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FinalWeatherDB db = FinalWeatherDB.getFinalWeatherDB(mContext);
        List<Condition> conds = db.getCondition(new String[]{mDatas.getNow().getCond().getCode(), mDatas.getNow().getCond().getTxt()});
        String upDateLocString = mDatas.getBasic().getUpdate().getLoc();
        switch (position) {
            case 0:
                holder.textView.setText("上次更新：" + upDateLocString);
                break;
            case 1:
                holder.textViewDaily.setText("textViewDaily");
                break;
            case 2:
                holder.textViewHourly.setText("textViewHourly");
                break;
            case 3:
                holder.textViewSuggestion.setText("textViewSuggestion");
                break;
        }

    }

    /**
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源的下标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 4;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textViewDaily;
        TextView textViewHourly;
        TextView textViewSuggestion;
        ImageView iv_weather_icon;
        ImageView iv_wind_rotation;

        public MyViewHolder(View view) {
            super(view);
            iv_weather_icon = (ImageView) view.findViewById(R.id.iv_weather_icon);
            iv_wind_rotation = (ImageView) view.findViewById(R.id.iv_wind_rotation);
            //通过一个动画资源加载器去加载一个动画xml文件
            ObjectAnimator oa = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.image_animaor);
            TimeInterpolator interpolator = new LinearInterpolator();
            oa.setInterpolator(interpolator);
            oa.setTarget(iv_wind_rotation);
            oa.start();
            textView = (TextView) view.findViewById(R.id.tv_refresh_date);
            textViewDaily = (TextView) view.findViewById(R.id.tv_daily);
            textViewHourly = (TextView) view.findViewById(R.id.tv_hourly);
            textViewSuggestion = (TextView) view.findViewById(R.id.tv_now_suggestion);

        }

    }
}

package cn.y.finalweather.adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.y.finalweather.R;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.Condition;
import cn.y.finalweather.model.HeWeather;
import cn.y.finalweather.util.DividerItemDecoration;

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
    private int indexSuggestion = 0;
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
                view = inflater.inflate(R.layout.item_hourly_weather, parent, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.item_daily_weather, parent, false);
                break;
        }

        return new MyViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        String upDateLocString = mDatas.getBasic().getUpdate().getLoc();
        switch (position) {
            case 0:
                adapterNowWeather(holder);
                break;
            case 1:
//                holder.textViewDaily.setText("textViewDaily");
                holder.rv_hourly.setAdapter(new ItemHourlyAdapter(mContext, mDatas.getHourly_forecast()));
                holder.rv_hourly.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL, 1));
                break;
            case 2:
//                holder.textViewHourly.setText("textViewHourly");
                holder.rv_daily.setAdapter(new ItemDailyAdapter(mContext, mDatas.getDaily_forecast()));
                holder.rv_daily.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL, 1));
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
        return 3;
    }

    /**
     * 只有允许在NowWeatherAdapter中的onBindViewHolder里面调用
     *
     * @param holder 传入一个MyViewHolder
     */
    private void adapterNowWeather(final MyViewHolder holder) {
        FinalWeatherDB db = FinalWeatherDB.getFinalWeatherDB(mContext);
//        List<Condition> conds = db.getCondition(new String[]{mDatas.getNow().getCond().getCode(), mDatas.getNow().getCond().getTxt()});
        List<Condition> conds = db.getCondition(new String[]{});
        List<String> suggestion = new ArrayList<>();
        suggestion.add(mDatas.getSuggestion().getCw().getTxt());
        suggestion.add(mDatas.getSuggestion().getDrsg().getTxt());
        suggestion.add(mDatas.getSuggestion().getFlu().getTxt());
        suggestion.add(mDatas.getSuggestion().getSport().getTxt());
        suggestion.add(mDatas.getSuggestion().getTrav().getTxt());
        suggestion.add(mDatas.getSuggestion().getUv().getTxt());
        final List<String> suggestionList = suggestion;
        String upDateLocString = mDatas.getBasic().getUpdate().getLoc();
        holder.textView.setText(upDateLocString);
        HeWeather.Now now = mDatas.getNow();
        String deg = mDatas.getNow().getWind().getDeg();
        deg = deg == null ? "0" : deg;
        float degF = Float.valueOf(deg);
        ObjectAnimator//
                .ofFloat(holder.iv_now_wind_deg, "rotation", 0.0F, degF)//
                .setDuration(1500)//
                .start();
        holder.tv_now_wind_dir.setText(now.getWind().getDir());
        holder.tv_now_wind_sc.setText(now.getWind().getSc());
        holder.tv_now_wind_spd.setText(now.getWind().getSpd());
        holder.tv_weather_text.setText(now.getCond().getTxt());
        holder.tv_now_fl.setText(now.getFl());
        holder.tv_now_hum.setText(now.getHum());
        holder.tv_now_pcpn.setText(now.getPcpn());
        holder.tv_now_pres.setText(now.getPres());
        holder.tv_now_vis.setText(now.getVis());
        String tmp = now.getTmp();
        int shi = Integer.valueOf(tmp.substring(0, 1)) + R.mipmap.org4_widget_nw0;
        int ge = Integer.valueOf(tmp.substring(1, 2)) + R.mipmap.org4_widget_nw0;

        holder.iv_weather_du_ge.setImageResource(ge);
        holder.iv_weather_du_shi.setImageResource(shi);
        // Log.d(TAG, "shi: " + Integer.valueOf(tmp.substring(0, 1)) + "\nge: " + Integer.valueOf(tmp.substring(1, 2)));
//Log.d("conds.size()",""+conds.size());
        for (Condition cond : conds) {
//            Log.d(TAG, "now.getCond().getCode(): " + now.getCond().getCode() + "\ncond.getCode(): " + cond.getCode());
            if (now.getCond().getCode().equals(cond.getCode())) {
//                Log.d(TAG,""+conds.indexOf(cond));
                holder.iv_weather_icon.setImageResource(conds.indexOf(cond) + R.drawable.weather_100);
                break;
            }
        }

//        final ViewPropertyAnimator viewAnimatorOut = holder.tv_suggestion_txt.animate();
        final Animator oa = AnimatorInflater.loadAnimator(mContext, R.animator.suggestion_linearlayout_in);
        oa.setTarget(holder.tv_suggestion_txt);
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (indexSuggestion < suggestionList.size())
                    if (suggestionList.get(indexSuggestion) == null) {
                        holder.ll_suggestion.setVisibility(View.GONE);
                        holder.tv_suggestion_txt.setVisibility(View.GONE);
                        holder.iv_suggestion_notify.setVisibility(View.GONE);
                    } else {
                        holder.ll_suggestion.setVisibility(View.VISIBLE);
                        holder.tv_suggestion_txt.setVisibility(View.VISIBLE);
                        holder.iv_suggestion_notify.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (indexSuggestion < suggestionList.size() && suggestionList.get(indexSuggestion) != null) {
                    holder.tv_suggestion_txt.setText(suggestionList.get(indexSuggestion));
                    indexSuggestion++;
                } else indexSuggestion = 0;

                oa.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        oa.start();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView tv_weather_text;
        TextView tv_now_fl;
        TextView tv_now_wind_dir;
        TextView tv_now_wind_sc;
        TextView tv_now_wind_spd;
        TextView tv_now_hum;
        TextView tv_now_pcpn;
        TextView tv_now_pres;
        TextView tv_now_vis;
        TextView tv_suggestion_txt;

        ImageView iv_weather_icon;
        ImageView iv_wind_rotation;
        ImageView iv_weather_du_shi;
        ImageView iv_weather_du_ge;
        ImageView iv_now_wind_deg;
        ImageView iv_suggestion_notify;

        LinearLayout ll_suggestion;
        RecyclerView rv_daily;
        RecyclerView rv_hourly;


        public MyViewHolder(View view, int viewType) {
            super(view);
            switch (viewType) {
                case 0:
                    ll_suggestion = (LinearLayout) view.findViewById(R.id.ll_suggestion);

                    iv_weather_icon = (ImageView) view.findViewById(R.id.iv_weather_icon);
                    iv_wind_rotation = (ImageView) view.findViewById(R.id.iv_wind_rotation);
                    //通过一个动画资源加载器去加载一个动画xml文件
                    ObjectAnimator oa = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.image_animaor);
                    TimeInterpolator interpolator = new LinearInterpolator();
                    oa.setInterpolator(interpolator);
                    oa.setTarget(iv_wind_rotation);
                    oa.start();

                    iv_weather_du_shi = (ImageView) view.findViewById(R.id.iv_weather_du_shi);
                    iv_weather_du_ge = (ImageView) view.findViewById(R.id.iv_weather_du_ge);
                    iv_now_wind_deg = (ImageView) view.findViewById(R.id.iv_now_wind_deg);
                    iv_suggestion_notify = (ImageView) view.findViewById(R.id.iv_suggestion_notify);
                    //通过一个动画资源加载器去加载一个动画xml文件
                    AnimatorSet oa_iv_suggestion_notify = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.suggestion_notify);
                    oa_iv_suggestion_notify.setTarget(iv_suggestion_notify);
                    oa_iv_suggestion_notify.start();

                    textView = (TextView) view.findViewById(R.id.tv_refresh_date);
                    tv_weather_text = (TextView) view.findViewById(R.id.tv_weather_text);
                    tv_now_fl = (TextView) view.findViewById(R.id.tv_now_fl);
                    tv_now_wind_dir = (TextView) view.findViewById(R.id.tv_now_wind_dir);
                    tv_now_wind_sc = (TextView) view.findViewById(R.id.tv_now_wind_sc);
                    tv_now_wind_spd = (TextView) view.findViewById(R.id.tv_now_wind_spd);
                    tv_now_hum = (TextView) view.findViewById(R.id.tv_now_hum);
                    tv_now_pcpn = (TextView) view.findViewById(R.id.tv_now_pcpn);
                    tv_now_pres = (TextView) view.findViewById(R.id.tv_now_pres);
                    tv_now_vis = (TextView) view.findViewById(R.id.tv_now_vis);
                    tv_suggestion_txt = (TextView) view.findViewById(R.id.tv_suggestion_txt);
                    break;
                case 1:
                    rv_hourly = (RecyclerView) view.findViewById(R.id.rv_hourly);
                    LinearLayoutManager layoutManagerHourly = new LinearLayoutManager(mContext);
                    //设置布局管理器
                    rv_hourly.setLayoutManager(layoutManagerHourly);
                    //设置为垂直布局，这也是默认的
                    layoutManagerHourly.setOrientation(OrientationHelper.HORIZONTAL);
                    //设置增加或删除条目的动画
                    rv_hourly.setItemAnimator(new DefaultItemAnimator());
                    break;
                case 2:
                    rv_daily = (RecyclerView) view.findViewById(R.id.rv_daily);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                    //设置布局管理器
                    rv_daily.setLayoutManager(layoutManager);
                    //设置为垂直布局，这也是默认的
                    layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
                    //设置增加或删除条目的动画
                    rv_daily.setItemAnimator(new DefaultItemAnimator());
                    break;
            }
        }

    }
}

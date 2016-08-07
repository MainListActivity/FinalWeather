package cn.y.finalweather.adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * 创建日期： 2016/8/6
 * 描述：
 * 修订历史：
 * =============================================
 */
public class ItemDailyAdapter extends RecyclerView.Adapter<ItemDailyAdapter.MyViewHolder> {
    private Context context;
    private List<HeWeather.DailyForecast> dailyForecasts;

    public ItemDailyAdapter(Context context, List<HeWeather.DailyForecast> dailyForecasts) {
        this.context = context;
        this.dailyForecasts = dailyForecasts;
    }

    @Override
    public ItemDailyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_daily_item, parent, false);//此处必须设置最后一个参数为false，不适用其为父View，否则报错
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemDailyAdapter.MyViewHolder holder, int position) {
        FinalWeatherDB db = FinalWeatherDB.getFinalWeatherDB(context);
        List<Condition> conds = db.getCondition(new String[]{});
        HeWeather.DailyForecast dailyForecast = dailyForecasts.get(position);
        for (Condition cond : conds) {
//            Log.d(TAG, "now.getCond().getCode(): " + now.getCond().getCode() + "\ncond.getCode(): " + cond.getCode());
            if (dailyForecast.getCond().getCode_d().equals(cond.getCode())) {
//                Log.d(TAG,""+conds.indexOf(cond));
                holder.iv_daily_code_d.setImageResource(conds.indexOf(cond) + R.drawable.weather_100);
            }
            if (dailyForecast.getCond().getCode_n().equals(cond.getCode())) {
//                Log.d(TAG,""+conds.indexOf(cond));
                holder.iv_daily_code_n.setImageResource(conds.indexOf(cond) + R.drawable.weather_100);
            }
        }
        holder.tv_daily_astro_sr.setText(dailyForecast.getAstro().getSr());
        holder.tv_daily_astro_ss.setText(dailyForecast.getAstro().getSs());
        holder.tv_daily_txt_d.setText(dailyForecast.getCond().getTxt_d());
        holder.tv_daily_txt_n.setText(dailyForecast.getCond().getTxt_n());
        holder.tv_daily_tmp_min.setText(dailyForecast.getTmp().getMin());
        holder.tv_daily_tmp_max.setText(dailyForecast.getTmp().getMax());
        holder.tv_daily_wind_sc.setText(dailyForecast.getWind().getSc());
        holder.tv_daily_wind_spd.setText(dailyForecast.getWind().getSpd());
        String deg = dailyForecast.getWind().getDeg();
        deg = deg == null ? "0" : deg;
        float degF = Float.valueOf(deg);
        ObjectAnimator//
                .ofFloat(holder.iv_daily_wind_deg, "rotation", 0.0F, degF)//
                .setDuration(1500)//
                .start();
        holder.tv_daily_hum.setText(dailyForecast.getHum());
        holder.tv_daily_pcpn.setText(dailyForecast.getPcpn());
        holder.tv_daily_pres.setText(dailyForecast.getPres());
        holder.tv_daily_vis.setText(dailyForecast.getVis());
        holder.tv_daily_date.setText(dailyForecast.getDate());
        Animator oa1 = AnimatorInflater.loadAnimator(context, R.animator.sun_up);
        oa1.setTarget(holder.iv_sunset_up_sun);
        oa1.start();
        Animator oa2 = AnimatorInflater.loadAnimator(context, R.animator.sun_down);
        oa2.setTarget(holder.iv_sunset_down_sun);
        oa2.start();

    }

    @Override
    public int getItemCount() {
        return dailyForecasts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_daily_astro_sr;
        TextView tv_daily_astro_ss;
        TextView tv_daily_txt_d;
        TextView tv_daily_txt_n;
        TextView tv_daily_tmp_min;
        TextView tv_daily_tmp_max;
        TextView tv_daily_wind_spd;
        TextView tv_daily_wind_sc;
        TextView tv_daily_hum;
        TextView tv_daily_pcpn;
        TextView tv_daily_pres;
        TextView tv_daily_vis;
        TextView tv_daily_date;

        ImageView iv_sunset_up_sun;
        ImageView iv_sunset_down_sun;
        ImageView iv_daily_code_d;
        ImageView iv_daily_code_n;
        ImageView iv_daily_wind_deg;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_daily_astro_sr = (TextView) itemView.findViewById(R.id.tv_daily_astro_sr);
            tv_daily_astro_ss = (TextView) itemView.findViewById(R.id.tv_daily_astro_ss);
            tv_daily_txt_d = (TextView) itemView.findViewById(R.id.tv_daily_txt_d);
            tv_daily_txt_n = (TextView) itemView.findViewById(R.id.tv_daily_txt_n);
            tv_daily_tmp_min = (TextView) itemView.findViewById(R.id.tv_daily_tmp_min);
            tv_daily_tmp_max = (TextView) itemView.findViewById(R.id.tv_daily_tmp_max);
            tv_daily_wind_spd = (TextView) itemView.findViewById(R.id.tv_daily_wind_spd);
            tv_daily_wind_sc = (TextView) itemView.findViewById(R.id.tv_daily_wind_sc);
            tv_daily_hum = (TextView) itemView.findViewById(R.id.tv_daily_hum);
            tv_daily_pcpn = (TextView) itemView.findViewById(R.id.tv_daily_pcpn);
            tv_daily_pres = (TextView) itemView.findViewById(R.id.tv_daily_pres);
            tv_daily_vis = (TextView) itemView.findViewById(R.id.tv_daily_vis);
            tv_daily_date = (TextView) itemView.findViewById(R.id.tv_daily_date);


            iv_sunset_up_sun = (ImageView) itemView.findViewById(R.id.iv_sunset_up_sun);
            iv_sunset_down_sun = (ImageView) itemView.findViewById(R.id.iv_sunset_down_sun);
            iv_daily_code_d = (ImageView) itemView.findViewById(R.id.iv_daily_code_d);
            iv_daily_code_n = (ImageView) itemView.findViewById(R.id.iv_daily_code_n);
            iv_daily_wind_deg = (ImageView) itemView.findViewById(R.id.iv_daily_wind_deg);

        }
    }


}

package cn.y.finalweather.adapter;

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
import cn.y.finalweather.model.HeWeather;

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
public class ItemHourlyAdapter extends RecyclerView.Adapter<ItemHourlyAdapter.MyViewHolder> {

    private Context context;
    private List<HeWeather.HourlyForecast> hourlyForecasts;

    public ItemHourlyAdapter(Context context, List<HeWeather.HourlyForecast> hourlyForecasts) {
        this.context = context;
        this.hourlyForecasts = hourlyForecasts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_hourly_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HeWeather.HourlyForecast hourlyForecast = hourlyForecasts.get(position);
        holder.tv_hourly_tmp.setText(hourlyForecast.getTmp());
        holder.tv_hourly_wind_sc.setText(hourlyForecast.getWind().getSc());
        holder.tv_hourly_wind_spd.setText(hourlyForecast.getWind().getSpd());
        String deg = hourlyForecast.getWind().getDeg();
        deg = deg == null ? "0" : deg;
        float degF = Float.valueOf(deg);
        ObjectAnimator//
                .ofFloat(holder.iv_hourly_wind_deg, "rotation", 0.0F, degF)//
                .setDuration(1500)//
                .start();
        holder.tv_hourly_hum.setText(hourlyForecast.getHum());
        holder.tv_hourly_pres.setText(hourlyForecast.getPres());
        holder.tv_hourly_date.setText(hourlyForecast.getDate());
        holder.tv_hourly_pop.setText(hourlyForecast.getPop());
    }

    @Override
    public int getItemCount() {
        return hourlyForecasts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_hourly_tmp;
        TextView tv_hourly_wind_sc;
        TextView tv_hourly_wind_spd;
        TextView tv_hourly_hum;
        TextView tv_hourly_pop;
        TextView tv_hourly_pres;
        TextView tv_hourly_date;

        ImageView iv_hourly_wind_deg;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_hourly_tmp = (TextView) itemView.findViewById(R.id.tv_hourly_tmp);
            tv_hourly_wind_sc = (TextView) itemView.findViewById(R.id.tv_hourly_wind_sc);
            tv_hourly_wind_spd = (TextView) itemView.findViewById(R.id.tv_hourly_wind_spd);
            tv_hourly_hum = (TextView) itemView.findViewById(R.id.tv_hourly_hum);
            tv_hourly_pop = (TextView) itemView.findViewById(R.id.tv_hourly_pop);
            tv_hourly_pres = (TextView) itemView.findViewById(R.id.tv_hourly_pres);
            tv_hourly_date = (TextView) itemView.findViewById(R.id.tv_hourly_date);


            iv_hourly_wind_deg = (ImageView) itemView.findViewById(R.id.iv_hourly_wind_deg);

        }
    }

}

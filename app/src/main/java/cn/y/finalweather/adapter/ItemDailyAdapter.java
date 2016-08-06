package cn.y.finalweather.adapter;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.y.finalweather.R;
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
        View view = layoutInflater.inflate(R.layout.item_daily_item, parent,false);//此处必须设置最后一个参数为false，不适用其为父View，否则报错
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemDailyAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dailyForecasts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }


}

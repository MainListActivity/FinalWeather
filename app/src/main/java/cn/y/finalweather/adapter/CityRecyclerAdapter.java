package cn.y.finalweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.y.finalweather.R;
import cn.y.finalweather.model.City;

/**
 * =============================================
 * 版权：版权所有(C) 2016
 * 作者： y
 * 版本：1.0
 * 创建日期： 2016/7/31
 * 描述：
 * 修订历史：
 * =============================================
 */
public class CityRecyclerAdapter extends RecyclerView.Adapter<CityRecyclerAdapter.MyViewHolder> {


    private List<City> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    public static final String TAG = "CityRecyclerAdapter";

    public CityRecyclerAdapter(Context context, List<City> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public CityRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_city_list,parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CityRecyclerAdapter.MyViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position).getCity());
//        Log.d(TAG,mDatas.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_recycler);
            view.findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

    }

}

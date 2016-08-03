package cn.y.finalweather.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import cn.y.finalweather.R;
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
public class NowWeatherAdapter extends RecyclerView.Adapter<NowWeatherAdapter.MyViewHolder> implements SurfaceHolder.Callback {
    private List<HeWeather> mDatas;
    private Context mContext;
    private MediaPlayer mediaPlayer1;
    private LayoutInflater inflater;
    public static final String TAG = "NowWeatherAdapter";

    public NowWeatherAdapter(Context context, List<HeWeather> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_now_weather, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SurfaceView sv = holder.sv;
        //设置播放时打开屏幕
        sv.getHolder().setKeepScreenOn(true);
        sv.getHolder().addCallback(this);
        mediaPlayer1 = new MediaPlayer();
        try {
            mediaPlayer1.setLooping(true);
            // 把视频输出到SurfaceView上
            mediaPlayer1.setDisplay(sv.getHolder());
            mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer1.setDataSource("file:///android_asset/banhusha.mp4");
            mediaPlayer1.prepare();
            mediaPlayer1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mediaPlayer1.isPlaying())
            mediaPlayer1.stop();
        mediaPlayer1.release();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SurfaceView sv;

        public MyViewHolder(View view) {
            super(view);
            sv = (SurfaceView) view.findViewById(R.id.sv);

        }

    }
}

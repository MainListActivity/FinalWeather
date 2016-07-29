package cn.y.finalweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.y.finalweather.model.City;

/**
 * 版权：版权所有(c) 2016
 * 作者：y
 * 版本：1.0
 * 创建日期：2016/7/29
 * 描述：
 * 修订历史：
 */
public class FinalWeatherDB {
    public static final String DB_NAME = "final_weather";
    public static final int VERSION = 1;
    public static FinalWeatherDB finalWeatherDB;
    private SQLiteDatabase db;

    private FinalWeatherDB(Context context) {
        FinalWeatherOpenHelper dbHelper = new FinalWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static FinalWeatherDB getFinalWeatherDB(Context context) {
        if (finalWeatherDB == null)
            return new FinalWeatherDB(context);
        return finalWeatherDB;
    }

    public void saveCity(City city) {
        if (city != null){
            ContentValues values = new ContentValues();
            values.put("cityCode",city.getCityCode());
            values.put("cityName",city.getCityName());
            values.put("cnty",city.getCnty());
            values.put("lat",city.getLat());
            values.put("lon",city.getLon());
            values.put("prov",city.getProv());
        }
    }
}

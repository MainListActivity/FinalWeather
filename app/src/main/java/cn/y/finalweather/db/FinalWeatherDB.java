package cn.y.finalweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
    public static final String DB_NAME = "final_weather.db";
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
            values.put("cityCode",city.getId());
            values.put("cityName",city.getCity());
            values.put("cnty",city.getCnty());
            values.put("lat",city.getLat());
            values.put("lon",city.getLon());
            values.put("prov",city.getProv());
            db.insert("City",null,values);
        }
    }

    public List<City> loadCities(){
        List<City> cities = new ArrayList<>();
        Cursor cursor = db.query("City",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                City city = new City();
                city.setCity(cursor.getString(cursor.getColumnIndex("cityName")));
                city.setId(cursor.getString(cursor.getColumnIndex("cityCode")));
                city.setCnty(cursor.getString(cursor.getColumnIndex("cnty")));
                city.setLat(cursor.getFloat(cursor.getColumnIndex("lat")));
                city.setLon(cursor.getFloat(cursor.getColumnIndex("lon")));
                city.setProv(cursor.getString(cursor.getColumnIndex("prov")));
                city.setCustomId(cursor.getInt(cursor.getColumnIndex("id")));
                cities.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return cities;
    }
    public void updateDB(City city,String id){
        if (city != null){
            ContentValues values = new ContentValues();
            values.put("cityCode",city.getId());
            values.put("cityName",city.getCity());
            values.put("cnty",city.getCnty());
            values.put("lat",city.getLat());
            values.put("lon",city.getLon());
            values.put("prov",city.getProv());
            db.update("City",values,"id = ?",new String[]{id});
        }
    }
}

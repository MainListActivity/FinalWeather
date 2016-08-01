package cn.y.finalweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.y.finalweather.model.City;
import cn.y.finalweather.model.Condition;

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
    public static final int VERSION = 2;
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
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("cityCode", city.getId());
            values.put("cityName", city.getCity());
            values.put("cnty", city.getCnty());
            values.put("lat", city.getLat());
            values.put("lon", city.getLon());
            values.put("prov", city.getProv());
            db.insert("City", null, values);
        }
    }

    public void saveCondition(Condition condition) {
        if (condition != null) {
            ContentValues values = new ContentValues();
            values.put("code", condition.getCode());
            values.put("icon", condition.getIcon());
            values.put("txt", condition.getTxt());
            values.put("txt_en", condition.getTxt_en());
            db.insert("Condition", null, values);
        }
    }

    public List<Condition> getCondition(String[] codeANDtxt) {
        List<Condition> conditions = new ArrayList<>();
        Cursor cursor;
        if (codeANDtxt.length == 2)
            cursor = db.query("Condition", null, "code = ? and txt = ?", codeANDtxt, null, null, null);
        else
            cursor = db.query("Condition", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Condition condition = new Condition();
                condition.setCode(cursor.getInt(cursor.getColumnIndex("code")));
                condition.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                condition.setTxt(cursor.getString(cursor.getColumnIndex("txt")));
                condition.setTxt_en(cursor.getString(cursor.getColumnIndex("txt_en")));
                condition.setId(cursor.getString(cursor.getColumnIndex("id")));
                conditions.add(condition);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return conditions;
    }

    public int getCityNum(String queryText) {
        String selection = null;
        if (queryText != null)
            selection = "cityName LIKE '%" + queryText + "%' ";
        Cursor cursor = db.query("City", null, selection, null, null, null, null);
        int count = cursor.getCount();
//        Log.d("getCityNum",cursor.getCount()+"");
        cursor.close();
        return count;
    }

    public List<City> loadCities(String queryText) {
        String selection = null;
        if (queryText != null)
            selection = "cityName LIKE '%" + queryText + "%' ";
        List<City> cities = new ArrayList<>();
        Cursor cursor = db.query("City", null, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
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
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cities;
    }

    public void updateCityDB(City city, String id) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("cityCode", city.getId());
            values.put("cityName", city.getCity());
            values.put("cnty", city.getCnty());
            values.put("lat", city.getLat());
            values.put("lon", city.getLon());
            values.put("prov", city.getProv());
            db.update("City", values, "id = ?", new String[]{id});
        }
    }
    public void updateConditionDB(Condition condition, String id) {
        if (condition != null) {
            ContentValues values = new ContentValues();
            values.put("code", condition.getCode());
            values.put("icon", condition.getIcon());
            values.put("txt", condition.getTxt());
            values.put("txt_en", condition.getTxt_en());
            db.update("Condition", values, "id = ?", new String[]{id});
        }
    }

    public void asynLoadCities(String queryText, CityLoadedCallBack cityLoadedCallBack) {
        new LoadTask(cityLoadedCallBack).execute(queryText);

    }

    public static abstract class CityLoadedCallBack {
        public abstract void onCityLoadStart();

        public abstract void onCityLoaded(List<City> cities);

        public abstract void onCityLoading(Integer now, Integer max);
    }

    public class LoadTask extends AsyncTask<String, Integer, List<City>> {
        private CityLoadedCallBack cityLoadedCallBack;

        public LoadTask(CityLoadedCallBack cityLoadedCallBack) {
            this.cityLoadedCallBack = cityLoadedCallBack;
        }

        @Override
        protected void onPreExecute() {
            cityLoadedCallBack.onCityLoadStart();
        }

        @Override
        protected List<City> doInBackground(String... strings) {
            String queryText = strings[0];
            String selection = null;
            if (queryText != null)
                selection = "cityName LIKE '%" + queryText + "%' ";
            List<City> cities = new ArrayList<>();
            Cursor cursor = db.query("City", null, selection, null, null, null, null);
            if (cursor.moveToFirst()) {
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
                    publishProgress(cursor.getPosition(), cursor.getColumnCount());
                } while (cursor.moveToNext());
            }
            cursor.close();
            return cities;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            cityLoadedCallBack.onCityLoading(values[0], values[1]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<City> cities) {
            cityLoadedCallBack.onCityLoaded(cities);
            super.onPostExecute(cities);
        }
    }
}

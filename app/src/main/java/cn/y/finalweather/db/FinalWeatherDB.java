package cn.y.finalweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.renderscript.Float4;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.y.finalweather.model.City;
import cn.y.finalweather.model.Condition;
import cn.y.finalweather.model.HeWeather;

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
    public static final int VERSION = 3;
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
            cursor = db.query("Condition", null, "code = ? or txt = ?", codeANDtxt, null, null, null);
        else
            cursor = db.query("Condition", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Condition condition = new Condition();
                condition.setCode(cursor.getString(cursor.getColumnIndex("code")));
                condition.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                condition.setTxt(cursor.getString(cursor.getColumnIndex("txt")));
                condition.setTxt_en(cursor.getString(cursor.getColumnIndex("txt_en")));
                condition.setId(cursor.getString(cursor.getColumnIndex("id")));
                conditions.add(condition);
            } while (cursor.moveToNext());
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
                city.setLat(cursor.getString(cursor.getColumnIndex("lat")));
                city.setLon(cursor.getString(cursor.getColumnIndex("lon")));
                city.setProv(cursor.getString(cursor.getColumnIndex("prov")));
                city.setCustomId(cursor.getString(cursor.getColumnIndex("id")));
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

    /**
     * @param sp sp[0]:basic; sp[1]: now; sp[2]: suggestion
     * @return 天气对象
     */
    public HeWeather getWeather(SharedPreferences[] sp) {
        HeWeather weather = new HeWeather();

        HeWeather.Basic basic = weather.getBasic();
        HeWeather.Basic.Update upDate = weather.getBasic().getUpdate();
        basic.setCity(sp[0].getString("city", null));
        basic.setCnty(sp[0].getString("cnty", null));
        basic.setId(sp[0].getString("id", null));
        basic.setLat(sp[0].getString("lat", null));
        basic.setLon(sp[0].getString("lon", null));
        upDate.setLoc(sp[0].getString("loc", null));
        upDate.setUtc(sp[0].getString("utc", null));
        basic.setUpdate(upDate);

        HeWeather.Now now = weather.getNow();
        HeWeather.Now.Cond cond = now.getCond();
        HeWeather.DailyForecast.Wind wind = now.getWind();
        cond.setCode(sp[1].getString("code", null));
        cond.setTxt(sp[1].getString("txt", null));
        now.setFl(sp[1].getString("fl", null));
        now.setHum(sp[1].getString("hum", null));
        now.setPcpn(sp[1].getString("pcpn", null));
        now.setPres(sp[1].getString("pres", null));
        now.setTmp(sp[1].getString("tmp", null));
        now.setVis(sp[1].getString("vis", null));
        wind.setDeg(sp[1].getString("deg", null));
        wind.setDir(sp[1].getString("dir", null));
        wind.setSc(sp[1].getString("sc", null));
        wind.setSpd(sp[1].getString("spd", null));
        now.setCond(cond);
        now.setWind(wind);

        HeWeather.Suggestion suggestion = weather.getSuggestion();
        HeWeather.Suggestion.Comf comf = suggestion.getComf();
        HeWeather.Suggestion.Cw cw = suggestion.getCw();
        HeWeather.Suggestion.Drsg drsg = suggestion.getDrsg();
        HeWeather.Suggestion.Flu flu = suggestion.getFlu();
        HeWeather.Suggestion.Sport sport = suggestion.getSport();
        HeWeather.Suggestion.Trav trav = suggestion.getTrav();
        HeWeather.Suggestion.Uv uv = suggestion.getUv();
        comf.setBrf(sp[2].getString("comf_brf", null));
        comf.setTxt(sp[2].getString("comf_txt", null));
        cw.setBrf(sp[2].getString("cw_brf", null));
        cw.setTxt(sp[2].getString("cw_txt", null));
        drsg.setBrf(sp[2].getString("drsg_brf", null));
        drsg.setTxt(sp[2].getString("drsg_txt", null));
        flu.setBrf(sp[2].getString("flu_brf", null));
        flu.setTxt(sp[2].getString("flu_txt", null));
        sport.setBrf(sp[2].getString("sport_brf", null));
        sport.setTxt(sp[2].getString("sport_txt", null));
        trav.setBrf(sp[2].getString("trav_brf", null));
        trav.setTxt(sp[2].getString("trav_txt", null));
        uv.setBrf(sp[2].getString("uv_brf", null));
        uv.setTxt(sp[2].getString("uv_txt", null));
        suggestion.setComf(comf);
        suggestion.setCw(cw);
        suggestion.setDrsg(drsg);
        suggestion.setFlu(flu);
        suggestion.setSport(sport);
        suggestion.setTrav(trav);
        suggestion.setUv(uv);

        List<HeWeather.DailyForecast> dailyForecasts = new ArrayList<>();
        Cursor cursorDaily = db.query("daily_forecast",null,null,null,null,null,null);
        if (cursorDaily.moveToFirst()){
            do {
                HeWeather.DailyForecast dailyForecast = weather.getDailyForecast();
                HeWeather.DailyForecast.Astro astro = dailyForecast.getAstro();
                HeWeather.DailyForecast.Cond dialyCond = dailyForecast.getCond();
                HeWeather.DailyForecast.Tmp tmp = dailyForecast.getTmp();
                HeWeather.DailyForecast.Wind win = dailyForecast.getWind();
                astro.setSr(cursorDaily.getString(cursorDaily.getColumnIndex("sr")));
                astro.setSs(cursorDaily.getString(cursorDaily.getColumnIndex("ss")));
                dialyCond.setCode_d(cursorDaily.getString(cursorDaily.getColumnIndex("code_d")));
                dialyCond.setCode_n(cursorDaily.getString(cursorDaily.getColumnIndex("code_n")));
                dialyCond.setTxt_d(cursorDaily.getString(cursorDaily.getColumnIndex("txt_d")));
                dialyCond.setTxt_n(cursorDaily.getString(cursorDaily.getColumnIndex("txt_n")));
                tmp.setMax(cursorDaily.getString(cursorDaily.getColumnIndex("max")));
                tmp.setMin(cursorDaily.getString(cursorDaily.getColumnIndex("min")));
                win.setSpd(cursorDaily.getString(cursorDaily.getColumnIndex("spd")));
                win.setDir(cursorDaily.getString(cursorDaily.getColumnIndex("dir")));
                win.setDeg(cursorDaily.getString(cursorDaily.getColumnIndex("deg")));
                win.setSc(cursorDaily.getString(cursorDaily.getColumnIndex("sc")));
                dailyForecast.setAstro(astro);
                dailyForecast.setCond(dialyCond);
                dailyForecast.setWind(win);
                dailyForecast.setTmp(tmp);
                dailyForecast.setDate(cursorDaily.getString(cursorDaily.getColumnIndex("date")));
                dailyForecast.setHum(cursorDaily.getString(cursorDaily.getColumnIndex("hum")));
                dailyForecast.setPcpn(cursorDaily.getString(cursorDaily.getColumnIndex("pcpn")));
                dailyForecast.setPop(cursorDaily.getString(cursorDaily.getColumnIndex("pop")));
                dailyForecast.setPres(cursorDaily.getString(cursorDaily.getColumnIndex("pres")));
                dailyForecast.setVis(cursorDaily.getString(cursorDaily.getColumnIndex("vis")));

                dailyForecasts.add(dailyForecast);
            }while (cursorDaily.moveToNext());
        }
        cursorDaily.close();

        List<HeWeather.HourlyForecast> hourlyForecasts = new ArrayList<>();

        Cursor hourlyCursor = db.query("hourly_forecast",null,null,null,null,null,null);
        if (hourlyCursor.moveToFirst()){
            do {
                HeWeather.HourlyForecast hourlyForecast = weather.getHourlyForecast();
                HeWeather.DailyForecast.Wind hourlyWind = hourlyForecast.getWind();
                hourlyWind.setDeg(hourlyCursor.getString(hourlyCursor.getColumnIndex("deg")));
                hourlyWind.setDir(hourlyCursor.getString(hourlyCursor.getColumnIndex("dir")));
                hourlyWind.setSc(hourlyCursor.getString(hourlyCursor.getColumnIndex("sc")));
                hourlyWind.setSpd(hourlyCursor.getString(hourlyCursor.getColumnIndex("spd")));
                hourlyForecast.setWind(hourlyWind);
                hourlyForecast.setDate(hourlyCursor.getString(hourlyCursor.getColumnIndex("date")));
                hourlyForecast.setHum(hourlyCursor.getString(hourlyCursor.getColumnIndex("hum")));
                hourlyForecast.setPop(hourlyCursor.getString(hourlyCursor.getColumnIndex("pop")));
                hourlyForecast.setPres(hourlyCursor.getString(hourlyCursor.getColumnIndex("pres")));
                hourlyForecast.setTmp(hourlyCursor.getString(hourlyCursor.getColumnIndex("tmp")));

                hourlyForecasts.add(hourlyForecast);
            }while (hourlyCursor.moveToNext());
        }
        hourlyCursor.close();
        weather.setBasic(basic);
        weather.setNow(now);
        weather.setDaily_forecast(dailyForecasts);
        weather.setHourly_forecast(hourlyForecasts);
        weather.setSuggestion(suggestion);
        return weather;
    }

    /**
     * 将天气数据保存到本地，其中天气预报数据保存到数据库，实时天气信息保存到SharedPreferences
     *
     * @param weather 天气对象
     * @param sp      sp[0]:basic; sp[1]: now; sp[2]: suggestion
     * @param upDate   第一次运行时选择不升级，以后都选择升级
     */
    public void saveWeather(HeWeather weather, SharedPreferences[] sp, boolean upDate) {
        if (weather != null) {
            if (!upDate) {
                db.delete("daily_forecast", null, null);
                db.delete("hourly_forecast", null, null);
            }
            SharedPreferences.Editor basicEditor = sp[0].edit();
            SharedPreferences.Editor nowEditor = sp[1].edit();
            SharedPreferences.Editor suggestionEditor = sp[2].edit();

            basicEditor.putString("city", weather.getBasic()==null?null:weather.getBasic().getCity());
            basicEditor.putString("cnty", weather.getBasic().getCnty());
            basicEditor.putString("id", weather.getBasic().getId());
            basicEditor.putString("lat", weather.getBasic().getLat());
            basicEditor.putString("lon", weather.getBasic().getLon());
            basicEditor.putString("loc", weather.getBasic().getUpdate().getLoc());
            basicEditor.putString("utc", weather.getBasic().getUpdate().getUtc());

            nowEditor.putString("code", weather.getNow().getCond().getCode());
            nowEditor.putString("txt", weather.getNow().getCond().getTxt());
            nowEditor.putString("fl", weather.getNow().getFl());
            nowEditor.putString("hum", weather.getNow().getHum());
            nowEditor.putString("pcpn", weather.getNow().getPcpn());
            nowEditor.putString("pres", weather.getNow().getPres());
            nowEditor.putString("tmp", weather.getNow().getTmp());
            nowEditor.putString("vis", weather.getNow().getVis());
            nowEditor.putString("deg", weather.getNow().getWind().getDeg());
            nowEditor.putString("dir", weather.getNow().getWind().getDir());
            nowEditor.putString("sc", weather.getNow().getWind().getSc());
            nowEditor.putString("spd", weather.getNow().getWind().getSpd());

            suggestionEditor.putString("comf_brf", weather.getSuggestion()==null?null:weather.getSuggestion().getComf().getBrf());
            suggestionEditor.putString("comf_txt", weather.getSuggestion()==null?null:weather.getSuggestion().getComf().getTxt());
            suggestionEditor.putString("cw_brf", weather.getSuggestion()==null?null:weather.getSuggestion().getCw().getBrf());
            suggestionEditor.putString("cw_txt", weather.getSuggestion()==null?null:weather.getSuggestion().getCw().getTxt());
            suggestionEditor.putString("drsg_brf", weather.getSuggestion()==null?null:weather.getSuggestion().getDrsg().getBrf());
            suggestionEditor.putString("drsg_txt", weather.getSuggestion()==null?null:weather.getSuggestion().getDrsg().getTxt());
            suggestionEditor.putString("flu_brf", weather.getSuggestion()==null?null:weather.getSuggestion().getFlu().getBrf());
            suggestionEditor.putString("flu_txt", weather.getSuggestion()==null?null:weather.getSuggestion().getFlu().getTxt());
            suggestionEditor.putString("sport_brf", weather.getSuggestion()==null?null:weather.getSuggestion().getSport().getBrf());
            suggestionEditor.putString("sport_txt", weather.getSuggestion()==null?null:weather.getSuggestion().getSport().getTxt());
            suggestionEditor.putString("trav_brf", weather.getSuggestion()==null?null:weather.getSuggestion().getTrav().getBrf());
            suggestionEditor.putString("trav_txt", weather.getSuggestion()==null?null:weather.getSuggestion().getTrav().getTxt());
            suggestionEditor.putString("uv_brf", weather.getSuggestion()==null?null:weather.getSuggestion().getUv().getBrf());
            suggestionEditor.putString("uv_txt", weather.getSuggestion()==null?null:weather.getSuggestion().getUv().getTxt());

            basicEditor.apply();
            nowEditor.apply();
            suggestionEditor.apply();
            for (HeWeather.DailyForecast dailyForecast : weather.getDaily_forecast()) {
                ContentValues values = new ContentValues();
                values.put("sr", dailyForecast.getAstro().getSr());
                values.put("ss", dailyForecast.getAstro().getSs());
                values.put("code_d", dailyForecast.getCond().getCode_d());
                values.put("code_n", dailyForecast.getCond().getCode_n());
                values.put("txt_d", dailyForecast.getCond().getTxt_d());
                values.put("txt_n", dailyForecast.getCond().getTxt_n());
                values.put("date", dailyForecast.getDate());
                values.put("hum", dailyForecast.getHum());
                values.put("pcpn", dailyForecast.getPcpn());
                values.put("pop", dailyForecast.getPop());
                values.put("pres", dailyForecast.getPres());
                values.put("max", dailyForecast.getTmp().getMax());
                values.put("min", dailyForecast.getTmp().getMin());
                values.put("vis", dailyForecast.getVis());
                values.put("deg", dailyForecast.getWind().getDeg());
                values.put("dir", dailyForecast.getWind().getDir());
                values.put("sc", dailyForecast.getWind().getSc());
                values.put("spd", dailyForecast.getWind().getSpd());
                if (upDate) {
                    int id = weather.getDaily_forecast().indexOf(dailyForecast);
                    db.update("daily_forecast", values, "id = ", new String[]{String.valueOf(id)});
                } else
                    db.insert("daily_forecast", null, values);
            }
            for (HeWeather.HourlyForecast hourlyForecast : weather.getHourly_forecast()) {
                ContentValues values = new ContentValues();
                values.put("date", hourlyForecast.getDate());
                values.put("hum", hourlyForecast.getHum());
                values.put("pop", hourlyForecast.getPop());
                values.put("pres", hourlyForecast.getPres());
                values.put("tmp", hourlyForecast.getTmp());
                values.put("deg", hourlyForecast.getWind().getDeg());
                values.put("dir", hourlyForecast.getWind().getDir());
                values.put("sc", hourlyForecast.getWind().getSc());
                values.put("spd", hourlyForecast.getWind().getSpd());
                if (upDate) {
                    int id = weather.getHourly_forecast().indexOf(hourlyForecast);
                    db.update("hourly_forecast", values, "id = ", new String[]{String.valueOf(id)});
                } else {
                    db.insert("hourly_forecast", null, values);
                }
            }
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
                    city.setLat(cursor.getString(cursor.getColumnIndex("lat")));
                    city.setLon(cursor.getString(cursor.getColumnIndex("lon")));
                    city.setProv(cursor.getString(cursor.getColumnIndex("prov")));
                    city.setCustomId(cursor.getString(cursor.getColumnIndex("id")));
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

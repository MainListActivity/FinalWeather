package cn.y.finalweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 版权：版权所有(c) 2016
 * 作者：y
 * 版本：1.0
 * 创建日期：2016/7/29
 * 描述：
 * 修订历史：
 */
public class FinalWeatherOpenHelper extends SQLiteOpenHelper {


    public static final String CREATE_CITY = "create table City(" +
            "id integer primary key autoincrement," +
            "cityName text," +
            "cnty text," +
            "cityCode text," +
            "lon text," +
            "prov text," +
            "lat text)";
    public static final String CREATE_CONDITION = "create table Condition(" +
            "id integer primary key autoincrement," +
            "code text," +
            "txt text," +
            "txt_en text," +
            "icon text)";
    public static final String CREATE_DAILY_FORECAST = "create table daily_forecast(" +
            "id integer primary key autoincrement," +
            "sr text," +
            "ss text," +
            "code_d text," +
            "code_n text," +
            "txt_d text," +
            "txt_n text," +
            "date text," +
            "hum text," +
            "pcpn text," +
            "pop text," +
            "pres text," +
            "max text," +
            "min text," +
            "vis text," +
            "deg text," +
            "dir text," +
            "sc text," +
            "spd text)";
    public static final String CREATE_HOURLY_FORECAST = "create table hourly_forecast(" +
            "id integer primary key autoincrement," +
            "date text," +
            "hum text," +
            "pop text," +
            "pres text," +
            "tmp text," +
            "deg text," +
            "dir text," +
            "sc text," +
            "spd text)";

    public FinalWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CITY);
        sqLiteDatabase.execSQL(CREATE_CONDITION);
        sqLiteDatabase.execSQL(CREATE_DAILY_FORECAST);
        sqLiteDatabase.execSQL(CREATE_HOURLY_FORECAST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i) {
            case 1:
                sqLiteDatabase.execSQL(CREATE_CONDITION);
            case 2:
                sqLiteDatabase.execSQL(CREATE_DAILY_FORECAST);
                sqLiteDatabase.execSQL(CREATE_HOURLY_FORECAST);
            default:
        }
    }
}

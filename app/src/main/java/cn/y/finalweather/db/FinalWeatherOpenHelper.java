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

    public FinalWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

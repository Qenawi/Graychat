package com.example.qenawi.am.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by QEnawi on 4/11/2017.
 */

public class DpHelper extends SQLiteOpenHelper
{
    public DpHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }
private final static String query =
        "CREATE TABLE IF NOT EXISTS "
        +"MYDP"+ "(" + "ID"
        + " integer primary key autoincrement, "
                + "EMAILPAIR"
        + " text not null,"
                + "USEREMAIL"
                + " text not null"
                +  ");";

    @Override
    public void onCreate(SQLiteDatabase dp)
    {
dp.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dp, int i, int i1)
    {

    }
    public void check()
    {
        SQLiteDatabase dp=getReadableDatabase();
        dp.execSQL(query);
    }
}

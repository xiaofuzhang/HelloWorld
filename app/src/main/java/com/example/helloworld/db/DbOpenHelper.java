package com.example.helloworld.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.helloworld.Constant;

public class DbOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "school";
    public static final String TABLE_NAME = "stu";
    public static final int VERSION = 1;
    private final String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ( "+
            "id integer primary key autoincrement," +
            "name string," +
            "score string," +
            "sex string default '男' )";

    private final String DELETE_TABLE_DATA = "DELETE FROM "+TABLE_NAME ;
    private final String INSERT_TABLE_DATA = "INSERT INTO "+TABLE_NAME +"(name, score) " +
            "select 'zhangsan' , 90 " +
            "union all select 'lisi' , 80 " +
            "union all select 'wangwu' , 70 " +
            "union all select 'zhaoliu', 60 " +
            "union all select 'sunqi' , 50 ";
    //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
    private final String UPDATE_TABLE_DATA = "UPDATE "+TABLE_NAME +
            " SET sex = '女' WHERE name = 'sunqi' ";

    public DbOpenHelper(@Nullable Context context){
        this(context,DB_NAME,null,VERSION);
    }
    public DbOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    //Called when the database is created for the first time.
    // This is where the creation of tables and
    // the initial population of the tables should happen.
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void initialValues(SQLiteDatabase db){
        if(db!=null){
            db.execSQL(DROP_TABLE);
            db.execSQL(CREATE_TABLE);
            db.execSQL(DELETE_TABLE_DATA);
            db.execSQL(INSERT_TABLE_DATA);
            db.execSQL(UPDATE_TABLE_DATA);
        }
    }
}

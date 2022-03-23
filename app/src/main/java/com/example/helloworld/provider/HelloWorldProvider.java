package com.example.helloworld.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helloworld.db.DbOpenHelper;

public class HelloWorldProvider extends ContentProvider {
    private static final String CONTENT = "content://";
    private static final String AUTHORITY = "com.example.helloworld";
    private static final Uri STU_URI = Uri.parse(CONTENT+AUTHORITY+"/"+ DbOpenHelper.TABLE_NAME);
    private static final String CONTENT_TYPE = "zxf/"+AUTHORITY;
    private static final int STU_INFO = 1;

    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private SQLiteDatabase dbOpenHelper;
    @Override
    public boolean onCreate() {
        matcher.addURI(AUTHORITY,DbOpenHelper.TABLE_NAME,STU_INFO);
        matcher.addURI(AUTHORITY,DbOpenHelper.TABLE_NAME+"/*",STU_INFO);
        matcher.addURI(AUTHORITY,DbOpenHelper.TABLE_NAME+"/#",STU_INFO);

        dbOpenHelper = new DbOpenHelper(getContext()).getWritableDatabase();
        Log.e("zxf","HelloWorldProvider onCreate");
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e("zxf","HelloWorldProvider getType uri:"+uri)
        ;
        switch (matcher.match(uri)){
            case STU_INFO:
                return CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (matcher.match(uri)){
            case STU_INFO:
                cursor = dbOpenHelper.query(DbOpenHelper.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long newId = 0;
        String newUri = null;
        switch (matcher.match(uri)){
            case STU_INFO:
                newId = dbOpenHelper.insert(DbOpenHelper.TABLE_NAME,null,values);
                newUri = STU_URI+"/"+newId;
                break;
        }
        if(newId>0){
            return Uri.parse(newUri);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.helloworld.view.DrawAndroid;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public static final String LOG_KEY_SMZQ="smzq";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        LocalBroadcastManager.getInstance(this);
        Log.i(LOG_KEY_SMZQ,"B onCreate");
        Log.i("zxf","detail:"+this.getIntent().toString());
        Log.i("zxf","detail getDataString:"+getIntent().getDataString());
        Uri uri = this.getIntent().getData();
        Log.i("zxf","detail uri:"+uri);
        if(uri!=null){
            String str = uri.toString();
            List s = uri.getPathSegments();
            String id = uri.getQueryParameter("id");
            Log.i("zxf","id:"+id+" uri:"+str+" PathSegments:"+s.toString());
        }

        FrameLayout frameLayout = findViewById(R.id.frame12);
        SharedPreferences sp = getSharedPreferences("name",MODE_MULTI_PROCESS);
        frameLayout.addView(new DrawAndroid(this,sp.getString(MainActivity.SHAREDPREFERENCES_KEY,null)));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_KEY_SMZQ,"B onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_KEY_SMZQ,"B onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_KEY_SMZQ,"B onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_KEY_SMZQ,"B onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_KEY_SMZQ,"B onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_KEY_SMZQ,"B onDestroy");
    }
}
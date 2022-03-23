package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.service.autofill.TextValueSanitizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.db.DbOpenHelper;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    public static final String SHAREDPREFERENCES_KEY = "key";
    private static final String LOG_TAG = "zxf";
    private DbOpenHelper dbHelper ;
    private GridLayout tbLayout;
    private GridLayout resultLayout;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu,menu);
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.one:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private ProgressBar pb;
    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            if(msg.what == MyService.HANDLER_UPDATAUI){//更新主界面UI
                pb.setProgress(msg.arg1);
                Log.i(LOG_TAG,"进度条进度："+msg.arg1);
            }else{
                pb.setProgress(msg.arg1);
                pb.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,"进度条结束！",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(DetailActivity.LOG_KEY_SMZQ,"A onCreate");

        pb = findViewById(R.id.progress1);
        //启动Myservice
        Intent i1 = new Intent();
        i1.putExtra(MyJobIntentService.HANDLER_KEY,new Messenger(handler));
        MyJobIntentService.enqueueWork(this,i1);
//        Intent intentService = new Intent(this,MyIntentService.class);
//        intentService.putExtra(MyIntentService.HANDLER_KEY, handler);
//        startService(intentService);

        Intent intent = new Intent(this,MyNotifIntentService.class);
        startService(intent);

        EditText et = findViewById(R.id.text);
        Button btn = findViewById(R.id.btn);
        Button btn2 = findViewById(R.id.btn2);
        tbLayout = findViewById(R.id.tb_stu);
        resultLayout = findViewById(R.id.result_tb);
        dbHelper = new DbOpenHelper(MainActivity.this,DbOpenHelper.DB_NAME,null,1);
        dbHelper.initialValues(dbHelper.getWritableDatabase());
        //显示原始表格
        showTable(tbLayout,false,DbOpenHelper.TABLE_NAME,null,
                null,null,
                null,null,
                null,null);

        SharedPreferences sp = getSharedPreferences("name",MODE_MULTI_PROCESS);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(Constant.LOG_KEY,s+" "+start+" "+before+" "+count);
                if(s!=null && s.length()>0){
                    btn.setEnabled(true);

                }else{
                    btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(SHAREDPREFERENCES_KEY,s.toString());
                editor.commit();
                Toast.makeText(MainActivity.this,s.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Intent intent = new Intent();
//                intent.setData(Uri.parse("app"));
//                intent.setData(Uri.parse("app://hello.world:8080/activity/detail"));
                intent.setDataAndType(
                        Uri.parse("app://hello.world:8080/activity/detail?id=999"),"abc/xyz");
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //显示查询结果表格
                showTable(resultLayout,false,DbOpenHelper.TABLE_NAME,null,
                        "score in (?,?,?)",new String[] {"70","81","90"},
                        null,null,
                        null,null);

            }
        });
    }
    private void showTable(GridLayout layout, boolean distinct, String table, String[] columns,
                           String selection, String[] selectionArgs, String groupBy,
                           String having, String orderBy, String limit){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(distinct,table,columns,selection,selectionArgs,groupBy,having,orderBy,limit);
        if(cursor!=null){
            int columnNum = cursor.getColumnCount();
            String[] backgroundColors = Utils.getBackgroundColors(columnNum);
            layout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
            layout.removeAllViews();
            layout.setColumnCount(columnNum);
            //添加表头
            for(int i=0;i<columnNum;i++){
                TextView tv = new TextView(MainActivity.this);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                tv.setTypeface(null,Typeface.BOLD);
                tv.setText(cursor.getColumnName(i));
                tv.setBackgroundColor(Color.parseColor(backgroundColors[i]));

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
//                        params.topMargin = 0;
//                        params.setGravity(Gravity.CENTER);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    params.columnSpec  = GridLayout.spec(i, 1.0f);
                }
                //把列添加进布局中
                layout.addView(tv,params);
            }
            while(cursor.moveToNext()){
                for(int i=0;i<columnNum;i++){
                    TextView tv = new TextView(MainActivity.this);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    tv.setBackgroundColor(Color.parseColor(backgroundColors[i]));

                    int type = cursor.getType(i);
                    switch (type){
                        case Cursor.FIELD_TYPE_INTEGER:
                            tv.setText(cursor.getInt(i)+"");
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            tv.setText(cursor.getString(i));
                            break;
                    }


                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
//                        params.topMargin = 0;
//                        params.setGravity(Gravity.CENTER);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        //start (int) ：起始位置，说明从第几行|列开始。
                        //              是行还是列决定权在赋值对象(params.rowSpec|params.columnSpec)
                        //              0为第1行|列
                        //size (int) ：默认为1，可以不写。如果值是1，那就说明占据1行|列，如果是2说明占据2行|列
                        //             但是这个大小不是单元格实际显示大小，逻辑上的占位单元格
                        //weight (float) ：比重，剩余空间的分配权重
                        params.columnSpec  = GridLayout.spec(i, 1, 1.0f);
                    }
                    //把列添加进布局中
                    layout.addView(tv,params);
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper!=null){
            dbHelper.close();
        }
        //停止service
        stopService(new Intent(this,MyService.class));
        //stopService(new Intent(this,MyNotifIntentService.class));//不需要
        Log.i(DetailActivity.LOG_KEY_SMZQ,"A onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(DetailActivity.LOG_KEY_SMZQ,"A onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(DetailActivity.LOG_KEY_SMZQ,"A onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(DetailActivity.LOG_KEY_SMZQ,"A onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(DetailActivity.LOG_KEY_SMZQ,"A onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(DetailActivity.LOG_KEY_SMZQ,"A onStop");
    }

}
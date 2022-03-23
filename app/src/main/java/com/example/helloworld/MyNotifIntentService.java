package com.example.helloworld;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyNotifIntentService extends IntentService {

    public MyNotifIntentService() {
        super("MyNotifIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //开启通知
        Log.e(Constant.LOG_KEY,"开启通知");
        //创建发送通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        String cId = "0x999";
        String cName = "channel_name";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(cId, cName, NotificationManager.IMPORTANCE_LOW);
            Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
            Log.i(Constant.LOG_KEY, mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(cId);
        }
        builder.setAutoCancel(true);//设置通知打开后消失
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置通知图标
        builder.setContentTitle("小机器人通知你：");//设置通知标题
        builder.setContentText("回家吃饭");//设置通知内容
        builder.setContentInfo("info");//设置通知信息
        builder.setWhen(System.currentTimeMillis());//设置通知发送时间
        builder.setDefaults(Notification.DEFAULT_SOUND|
                Notification.DEFAULT_VIBRATE| Notification.DEFAULT_LIGHTS);//设置通知默认声音，默认震动，默认亮度

        Intent mainIntent = new Intent(this,MainActivity.class);
        //跳转的页面，隐式intent
//        Intent notifintent = new Intent();
//        notifintent.setAction(Intent.ACTION_VIEW);
//        notifintent.setDataAndType(
//                Uri.parse("app://hello.world:8080/activity/detail?id=098"),"abc/xyz");
//        PendingIntent pi = PendingIntent.getActivity(this,0,notifintent,0);

        //创建任务栈，提升用户体验，当从桌面点击通知，看完跳转页面后，按返回键，会退回到app主界面
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //把跳转页面的层级附上，在AM.XML中有android:parentActivityName=".MainActivity"
        //说明detailActivity的父Activity是MainActivity
        stackBuilder.addParentStack(DetailActivity.class);
        //要访问的Activity的Intent,只能用显示的方式
        Intent notifintent = new Intent();
        String pkgName = "com.example.helloworld";
        String clsName = "com.example.helloworld.ThirdActivity";
//        String pkgName = "com.zxf.switcherdemo";
//        String clsName = "com.zxf.switcherdemo.MainActivity";
        notifintent.setComponent(new ComponentName(pkgName,clsName));
        stackBuilder.addNextIntent(notifintent);//即将跳转的页面，可以是别的应用
        PendingIntent pi = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pi);
        notificationManager.notify(0x001,builder.build());
    }
}
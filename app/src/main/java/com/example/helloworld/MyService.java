package com.example.helloworld;

import static com.example.helloworld.Constant.LOG_KEY;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyService extends Service {
    public static final int HANDLER_UPDATAUI = 0x001;
    public static final String HANDLER_KEY= "handler";
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    //创建时调用
    public void onCreate() {
        super.onCreate();
    }

    //每次启动service时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*new Thread(){
            @Override
            public void run() {
                int i = 0 ;
                while(isRunning()){
                    Log.i(LOG_TAG,"i="+i++);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i(LOG_TAG,"MYSERVICE is not Running");

            }
        }.start();*/
        Bundle bundle = intent.getExtras();
        Messenger msger = (Messenger) bundle.get(HANDLER_KEY);
        try {
            doWork(msger);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //service销毁时调用
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void doWork(Messenger msger) throws RemoteException {
        int progressNum =0;
        while(true){
            Message msg = Message.obtain();
            //不使用new关键字，obtain方法可以循环使用message对象，减小内存开销
            //Message msg=new Message();
            progressNum += (int)(Math.random()*10);
//                progressNum += 1;
            Log.i(LOG_KEY,"doWork:::"+progressNum+" "+Math.random());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(progressNum<100){
                msg.what = HANDLER_UPDATAUI;
                msg.arg1 = progressNum;
                msger.send(msg);
            }else{
                msg.what = 0x999;
                msg.arg1 = 100;
//                MainActivity.handler.sendMessage();
                msger.send(msg);
                break;
            }
        }
    }
    private boolean isRunning(){
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(10);
        Iterator it = list.iterator();
        while(it.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = (ActivityManager.RunningServiceInfo) it.next();
            Log.i(LOG_KEY,"Running service"+runningServiceInfo.service.getClassName().toString());
            if(runningServiceInfo!=null && runningServiceInfo.service.getClassName().equals("com.example.helloworld.MyService")){
                return true;
            }
        }
        Log.i(LOG_KEY,"is not Running ");
        return false;
    }
}
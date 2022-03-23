package com.example.helloworld;

import static com.example.helloworld.Constant.LOG_KEY;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

public class MyJobIntentService extends JobIntentService {
    public static final int JOB_ID = 0x111;
    public static final int HANDLER_UPDATAUI = 0x001;
    public static final String HANDLER_KEY= "handler";

    public static void enqueueWork(Context context, Intent i) {
        enqueueWork(context,MyJobIntentService.class,JOB_ID,i);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Messenger msgr = intent.getParcelableExtra(HANDLER_KEY);
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
                try {
                    msgr.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else{
                msg.what = 0x999;
                msg.arg1 = 100;
//                MainActivity.handler.sendMessage();
                try {
                    msgr.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}

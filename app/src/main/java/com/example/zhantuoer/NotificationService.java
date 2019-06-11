package com.example.zhantuoer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SignIn();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
                  SignIn();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    protected void NotificationTest(){
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle("通知").setContentText("你今天还没有签到哦")
                .setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .build();
        manager.notify(1,notification);
    }

    public void SignIn(){

        Calendar c = Calendar.getInstance();

       int hour = c.get(Calendar.HOUR_OF_DAY);
       int minute = c.get(Calendar.MINUTE);
        int second=c.get(Calendar.SECOND);
        if(hour==22&&minute==0){
           NotificationTest();
        }else{
          return;
         }


    }
}

package com.example.a190510_gcglist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import androidx.work.Worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.transform.Result;

public class MyService extends Worker {
    ListViewAdapter adapter;
    MyListView mlv;
    Context m_mainActivity;
    int cnt = 0;
    public MyService() {
//        m_mainActivity = _context;//(MainActivity)this.getApplicationContext();
       // Intent i = getIntent();
       // Service s = s.startService()
    }

    @NonNull
    @Override
    public WorkerResult doWork() {
        Log.i("Noti"," Called doWork ");
        m_mainActivity = getApplicationContext();
//        new Sender(adapter,mlv,(MainActivity)this.getApplicationContext()).sendProcessTaskForBackGround();
        new Sender(adapter,mlv,m_mainActivity).sendProcessTaskForBackGround();
        //sendNotification("kjh", "보냈습니다");






        return WorkerResult.SUCCESS;
    }

    public void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }


//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//    public int onStartCommand(Intent intent,int flags, int startId){
//        new Sender(adapter,mlv).sendProcessTask();
//        return START_NOT_STICKY;
//    }



}

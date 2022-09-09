package com.mylibrary;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CounterServiceParallel extends Service {

    List<Integer> startIds=new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            final int number=intent.getIntExtra("number",-1);
            if(number != -1){
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i=15;i<number;i++){
                            System.out.println("nikhil parallel="+i);
                            SystemClock.sleep(1000);
                        }
                    }
                });
                startIds.add(startId);
                thread.start();
            }
        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(Integer startId:startIds) {
            stopSelf(startId);
        }
    }
}

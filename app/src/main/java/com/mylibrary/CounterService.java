package com.mylibrary;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

import androidx.annotation.Nullable;

/*
* Service is one of the four main components of androind system viz activities,service,broadcast receivers, content providers
* The main purpose of service is to perform background tasks. (Alternative to AsyncTask)
* */
public class CounterService extends IntentService {

    public CounterService() {
        super("Counter service thread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent !=null){
            int number = intent.getIntExtra("number",-1);
            for (int i=0;i<number;i++){
                System.out.println("nikhil="+i);
                SystemClock.sleep(1000);
            }
        }
    }
}

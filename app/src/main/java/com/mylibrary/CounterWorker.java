package com.mylibrary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/*
* Recommended to use this instead of JobScheduler as this is backward compatible.
* */
public class CounterWorker extends Worker {
    public CounterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    //Note that this method creates a new Worker thread.
    @NonNull
    @Override
    public Result doWork() {
        Data inputData=getInputData();
        int number=inputData.getInt("number",-1);
        if(number!=-1){
            for (int i=27;i<number;i++){
                System.out.println("nikhil Work Manager="+i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return Result.failure();
                }
            }
        }
        return Result.success();
    }
}

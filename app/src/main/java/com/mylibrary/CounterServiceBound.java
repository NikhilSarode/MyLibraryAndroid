package com.mylibrary;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

/*
* Bound services are bounded to the component from which being called like activity etc.
* Thus it can interact with the UI as opposed to the started services
* The lifecycle of the bound services is same of the binding client
* */
public class CounterServiceBound extends Service {

    private IBinder binder = new LocalBinder();

    //This method is responsible for binding the service to the client
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        CounterServiceBound getService(){
            return CounterServiceBound.this;
        }
    }

    public int getCounterNumber(int i){
        return i*4;
    }
}

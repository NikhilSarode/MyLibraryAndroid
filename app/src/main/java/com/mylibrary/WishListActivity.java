package com.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class WishListActivity extends AppCompatActivity implements SendNameInterface{

    private TextView txtParentWishList;
    private MyAsyncTask myAsyncTask;

    private boolean isBound=false;
    private CounterServiceBound counterServiceBound;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            CounterServiceBound.LocalBinder binder = (CounterServiceBound.LocalBinder) iBinder;
            counterServiceBound = binder.getService();
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, CounterServiceBound.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound && counterServiceBound!=null) {
            unbindService(connection);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        txtParentWishList=findViewById(R.id.txtParentWishList);

        //2nd way of using fragment:- This creates a transaction to replace the Frame with our fragment
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        FirstWishList fragment= new FirstWishList();
        //sending data to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("message", "This message is coming from parent activity to fragment");
        fragment.setArguments(bundle);
        transaction.replace(R.id.container,fragment);
        transaction.commit();

        //Async counter
        myAsyncTask=new MyAsyncTask();
        myAsyncTask.execute(10);

        /*Async counter using IntentService
        * Limitation:- you cannot interact with UI (calling client)
        * */
        Intent intent =new Intent(WishListActivity.this,CounterService.class); //note that we can pass service to intent
        intent.putExtra("number",10);
        startService(intent);
        startService(intent);
        /*Note that if you start the service twice (means two threads). Those will get sequentially one after other
        * To execute them parallely do below approach
        * */
        Intent intent1 =new Intent(WishListActivity.this,CounterServiceParallel.class);
        intent1.putExtra("number",22);
        startService(intent1);
        startService(intent1);

        //JOB Scheduler
        initJobScheduler();

        //Work Manager
        initWorker();
    }

    @Override
    public void sendNameResult(String name) {
        txtParentWishList.setText(name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*The thread allocates some memory for its members. Now say the worker thread is running and you close the
        application. In such a case the memory will still be allocated. This wont be cleaned up by garbage collector
        because GC clears only the Main(UI) thread memory and not the worker thread. Hence you need to clear it manually
        and the good place to do so is inside activity onDestroy.
        * */
        if(myAsyncTask != null && !myAsyncTask.isCancelled()){
            myAsyncTask.cancel(true);
        }
    }

    /*To handle background task we could have created java thread but android provides support for background task as
    * below. With this we dont need to manage many things that would have been required in case of simple threads*/
    private class MyAsyncTask extends AsyncTask<Integer/*Input*/,Integer /*Progress*/, String/*Result*/>{
        private TextView asyncCounter,asyncCounterBounded;

        //Initialize things before thread starts. Note that this is executed in Main(UI) thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncCounter=findViewById(R.id.asyncCounter);
            asyncCounterBounded=findViewById(R.id.asyncCounterBounded);
        }

        //This is executed in worker thread.
        @Override
        protected String doInBackground(Integer... integers) {
            for(int i=0;i<integers[0];i++){
                int boundedCounter=-1;
                if(isBound && counterServiceBound!=null){
                    boundedCounter=counterServiceBound.getCounterNumber(i);
                }
                publishProgress(i,boundedCounter);
                SystemClock.sleep(1000);  //Instead of Thread.sleep
            }
            return "Finished";
        }

        /*As the thread progresses this gets called on every publishProgress call .
         Note that this is executed in Main(UI) thread*/
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            asyncCounter.setText(String.valueOf(values[0]));
            asyncCounterBounded.setText(String.valueOf(values[1]));
        }

        //String is the result type coming after end of doInBackground for the curent thread.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            asyncCounter.setText(s);
            asyncCounterBounded.setText(s);
        }
    }

    private void initJobScheduler(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            ComponentName componentName = new ComponentName(this, CounterJobScheduler.class);
            PersistableBundle bundle = new PersistableBundle();
            bundle.putInt("number",20);
            JobInfo.Builder builder=new JobInfo.Builder(138,componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(bundle)
                    .setPersisted(true); // Job will be persisted after device reboot. Needs a permission in manifest.

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                builder.setPeriodic(15*60*1000,30*60*1000);
            }else{
                builder.setPeriodic(15*60*1000);
            }
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());

        }
    }

    private void initWorker(){
        Data data=new Data.Builder()
                .putInt("number",34)
                .build();

        //Various conditions can be put here
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //One time request means valid only for one time execution of te job
        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(CounterWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(downloadRequest);

        //Executes the job at specified interval of time
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                CounterWorker.class,
                1,
                TimeUnit.DAYS
        ).setInputData(data)
                .setConstraints(constraints)
                .build();
        //WorkManager.getInstance(this).enqueue(periodicWorkRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadRequest.getId()).observe(this,
                new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        System.out.println("nikhil Work Manager STATUS="+workInfo.getState());
                    }
                });
        //WorkManager.getInstance(this).cancelWorkById(downloadRequest.getId());
    }
}
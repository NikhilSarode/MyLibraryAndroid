package com.mylibrary;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

/*
* Limitation:- Not backward compatible. Available only for API21.
* For lower versions use Work Manager.
* Note that JobScheduler does not create Worker thread by itself. You will have to do it using AsyncTask.
* */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CounterJobScheduler extends JobService {
    private MyAsyncTask myAsyncTask;
    private JobParameters jobParameters;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        System.out.println("nikhil Job Scheduler onStartJob called");
        this.jobParameters=jobParameters;
        PersistableBundle bundle=jobParameters.getExtras();
        int number = bundle.getInt("number",-1);
        myAsyncTask = new MyAsyncTask();
        if(number!=-1){
            myAsyncTask.execute(number);
        }
        /*
        * return false if you are sure that your job is handled(completed) successfully.
        * Here we will return true because our job is a async task and it can run much longer than UI thread and
        * we are not sure when it will be finished
        * */
        return true;
    }

    /*
    * This method will get called when android system kills your job for eg. some other thing comes up with higher
    * priority, the android system will kill your job.
    * Other situation is when your criterias are not met. For eg. you asked for executing this job over WiFi but
    * wifi is disconnected. Thus its a good place to free up the resources.
    * */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        System.out.println("nikhil Job Scheduler onStopJob called");
        if(myAsyncTask != null && !myAsyncTask.isCancelled()){
            myAsyncTask.cancel(true);
        }
        /*
        * If you want to reschedule your job again after getting killed in between then return true otherwise false.
        * */
        return true;
    }

    private class MyAsyncTask extends AsyncTask<Integer,Integer, String> {

        //This is executed in worker thread.
        @Override
        protected String doInBackground(Integer... integers) {
            for(int i=0;i<integers[0];i++){
                publishProgress(i);
                SystemClock.sleep(1000);
            }
            return "Job Finished";
        }

         @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println("nikhil Job Scheduler="+String.valueOf(values[0]));
        }

         @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("nikhil"+s);
            jobFinished(jobParameters,true);
        }
    }
}

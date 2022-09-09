package com.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class WishListActivity extends AppCompatActivity implements SendNameInterface{

    private TextView txtParentWishList;
    private MyAsyncTask myAsyncTask;

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

        //Async counter using IntentService
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
        private TextView asyncCounter;

        //Initialize things before thread starts. Note that this is executed in Main(UI) thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncCounter=findViewById(R.id.asyncCounter);
        }

        //This is executed in worker thread.
        @Override
        protected String doInBackground(Integer... integers) {
            for(int i=0;i<integers[0];i++){
                publishProgress(i);
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
        }

        //String is the result type coming after end of doInBackground for the curent thread.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            asyncCounter.setText(s);
        }
    }
}
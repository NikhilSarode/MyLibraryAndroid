package com.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class CurrentlyReadingActivity extends AppCompatActivity {

    private RecyclerView currentlyReadingRecView;
    private BookRecViewAdapter  adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currently_reading);

        currentlyReadingRecView=findViewById(R.id.currentlyReadingRecView);
        adapter=new BookRecViewAdapter(this,"currentlyReading");
        currentlyReadingRecView.setAdapter(adapter);
        currentlyReadingRecView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setBooks(Utils.getInstance().getCurrentlyReadingBooks());
    }

    /*intent.setFlags clears the backstack and closes the app. But say we want to navigate to Main activity
    so to do that we can override below method*/
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);       //To clear the backstack. On pressing back it will close the app.
        startActivity(intent);
    }
}
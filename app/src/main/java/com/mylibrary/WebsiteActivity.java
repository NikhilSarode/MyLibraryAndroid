package com.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebsiteActivity extends AppCompatActivity {

    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        myWebView=findViewById(R.id.myWebView);
        myWebView.loadUrl(url);
        myWebView.setWebViewClient(new WebViewClient()); // to load the website in our application instead of browser
        //myWebView.getSettings().setJavaScriptEnabled(true);
    }

    //to load previous web page instead of navigating to prev activity
    @Override
    public void onBackPressed() {
        if(myWebView.canGoBack()){
            myWebView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
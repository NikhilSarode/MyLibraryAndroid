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
        myWebView.setWebViewClient(new WebViewClient());    //to load previous web page instead of navigating to prev activity
        //myWebView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if(myWebView.canGoBack()){
            myWebView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
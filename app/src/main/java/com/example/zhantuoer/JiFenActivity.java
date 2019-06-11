package com.example.zhantuoer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JiFenActivity extends AppCompatActivity {
    //webView
    WebView webView;
    String phoneNumber;
    private static final String TAG = "JiFenActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ji_fen);
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("user_phone");
        Log.w(TAG, "onCreate111: "+phoneNumber);
        webView = (WebView)findViewById(R.id.web_wiew);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(JsonTools.CONSTURL+"zhantuoer/product_index.jsp?phoneNumber="+phoneNumber);
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if(keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

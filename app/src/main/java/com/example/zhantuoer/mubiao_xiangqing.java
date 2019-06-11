package com.example.zhantuoer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class mubiao_xiangqing extends AppCompatActivity implements View.OnClickListener {
    private String Url = JsonTools.CONSTURL+"zhantuoer/userGoalList";
    String tid;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mubiao_xiangqing);
        click_init();
        web();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void web(){
        String s =Url+"?tid="+tid  ;
        webView = (WebView)findViewById(R.id.web_wiew);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(s);
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    private void click_init(){
        ImageView back;
        back = (ImageView)findViewById(R.id.back);
        Intent intent = getIntent();
        tid = intent.getStringExtra("tID");
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
        }
    }

}

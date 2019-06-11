package com.example.zhantuoer;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TuiJianActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tui_jian);
        String fruitName = "厚积薄发";
        int fruitImageId =R.drawable.tuijianbj;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView = (ImageView) findViewById(R.id.fruit_image_view);
        TextView fruitContentText = (TextView) findViewById(R.id.fruit_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(fruitName);
        Glide.with(this).load(fruitImageId).into(fruitImageView);
        fruitContentText.setText("    如果你累了，请告诉自己，再坚持一下下就好；如果想放弃了，请告诉自己，冷静下来，成功就在前方；如果天黑了，请告诉自己，耐心等待，光明总会来临。这个极速匮乏的社会，太多的人浮躁，冰冷，急于求成，总在东奔西跑，半途而废，所谓天才，百分之1的灵感和百分之99的努力，成功都不是偶然的，昙花一现的绝美，源自数年的隐忍坚持，厚积薄发，才有这惊世的一瞬，我们总说成功不易，却是我们失去了追逐成功的耐心和恒心  水滴石穿，铁杵磨针 ，不都是极致的恒心才伸手碰到的天，年轻人 ，不要总把放弃说的那么云淡风轻，也不要总把新时代挂在嘴边，老祖宗的话是真理它就永远都适用。坚持就是会胜利。");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

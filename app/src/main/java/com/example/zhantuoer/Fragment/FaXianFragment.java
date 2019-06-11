package com.example.zhantuoer.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zhantuoer.AddfriendsActivity;
import com.example.zhantuoer.MapActivity;
import com.example.zhantuoer.R;
import com.example.zhantuoer.SearchActivity;
import com.example.zhantuoer.SendGoalActivity;
import com.example.zhantuoer.TuiJianActivity;

import static android.content.Context.MODE_PRIVATE;

public class FaXianFragment extends Fragment implements View.OnClickListener {
    //侧边栏按钮
    private DrawerLayout drawerLayout;
    //自己的用户名
    private String user_phone;
    private Boolean isLogin = false;
    private RelativeLayout fujin;
    private RelativeLayout MakeScan;
    private RelativeLayout tuijian;

    public FaXianFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.faxian, container, false);
        //toolbar标题栏
        Toolbar toolbar =(Toolbar)rootView.findViewById(R.id.toolbar1);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //隐藏toolbar的tittle
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        //toolbar滑动导航按钮的设置与显示
        drawerLayout = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        readFile();
    }
    private void readFile(){
        SharedPreferences pref =getActivity().getSharedPreferences("data",MODE_PRIVATE);
        user_phone =pref.getString("username","");
        if(user_phone.length()==0){
            isLogin = false;
        }else{
            isLogin = true;
        }
    }
    //toolbar菜单逻辑
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.sendinfo:
                Intent intent=new Intent(getActivity(),SendGoalActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fujin = (RelativeLayout)getActivity().findViewById(R.id.fujin);
        fujin.setOnClickListener(this);
        tuijian = (RelativeLayout)getActivity().findViewById(R.id.tuisong);
        tuijian.setOnClickListener(this);
        MakeScan=(RelativeLayout)getActivity().findViewById(R.id.makescan);
        MakeScan.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fujin:
                if(isLogin){
                    Intent intent = new Intent(getActivity(),MapActivity.class);
                    intent.putExtra("user_phone",user_phone);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"登录获取更多精彩",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tuisong:
                startActivity(new Intent(getActivity(),TuiJianActivity.class));
                break;
            case R.id.makescan:
                if(isLogin){
                Intent intent = new Intent(getActivity(),AddfriendsActivity.class);
                intent.putExtra("user_phone",user_phone);
                startActivity(intent);
                }else{
            Toast.makeText(getActivity(),"登录获取更多精彩",Toast.LENGTH_SHORT).show();
             }
             break;
        }
    }
}

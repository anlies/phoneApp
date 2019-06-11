package com.example.zhantuoer.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zhantuoer.JsonTools;
import com.example.zhantuoer.R;
import com.example.zhantuoer.SearchActivity;
import com.example.zhantuoer.SendGoalActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.zhantuoer.Fragment.FriendAdapter.friendBases;


public class FriendFragment extends Fragment {
    //侧边栏按钮
    private DrawerLayout drawerLayout;
    private String userName;
    private Boolean isLogin = false;

    private static final String JILU_JSON = JsonTools.CONSTURL+"zhantuoer/record?phoneNumber=";
    private String Uri;

    private RecyclerView recyclerView;
    FriendAsyncTask friendAsyncTask;

    public FriendFragment() {

    }
    private void readFile(){
        SharedPreferences pref =getActivity().getSharedPreferences("data",MODE_PRIVATE);
        userName =pref.getString("username","");
        if(userName.length()==0){
            isLogin = false;
            Toast.makeText(getActivity(),"登录获取更多精彩",Toast.LENGTH_SHORT).show();
        }else{
            isLogin = true;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.friend_recycler);
        //layoutManager用于指定布局方式，这里为线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend, container, false);
        //toolbar标题栏
        Toolbar toolbar =(Toolbar)rootView.findViewById(R.id.toolbar);
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
    public void onResume() {
        super.onResume();
        readFile();
        if(isLogin){
            //Toast.makeText(getActivity(),userName,Toast.LENGTH_SHORT).show();
            Uri=JsonTools.CONSTURL+"zhantuoer/ReadAttentionList?phoneNumber="+userName;
            // 启动 AsyncTask 以获取数据
            friendAsyncTask = new FriendAsyncTask();
            friendAsyncTask.executeOnExecutor(THREAD_POOL_EXECUTOR,Uri);
        }
    }

    private class FriendAsyncTask extends AsyncTask<String, Void, List<FriendBase>> {

        @Override
        protected List<FriendBase> doInBackground(String... urls) {
            // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<FriendBase> result = JsonTools.friendHelp(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<FriendBase> data) {
            Log.w(TAG, "onPostExecute: "+data );
            if (data != null && !data.isEmpty()) {
                // 创建新适配器，将空列表作为输入
                FriendAdapter mAdapter = new FriendAdapter(getActivity(),new ArrayList<FriendBase>());
                friendBases.addAll(data);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(),"网络好像有点问题",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

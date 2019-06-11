package com.example.zhantuoer.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhantuoer.JsonTools;
import com.example.zhantuoer.R;
import com.example.zhantuoer.TongJiActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static com.example.zhantuoer.Fragment.JiLuAdapter.jilus;


public class JiLuFragment extends Fragment implements View.OnClickListener{
    private String userName;
    private Boolean isLogin = false;
    private Force receiver;
    private static final String JILU_JSON = JsonTools.CONSTURL+"zhantuoer/record?phoneNumber=";
    private String Uri;
    //总天数
    TextView day_count;
    //侧边栏按钮
    private DrawerLayout drawerLayout;
    public JiLuFragment() {
    }
    private RecyclerView recyclerView;
    JiluAsyncTask task;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.jilu_recycle);
        //layoutManager用于指定布局方式，这里为线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

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
    public void onResume() {
        super.onResume();
        readFile();
        if(isLogin){
            //Toast.makeText(getActivity(),userName,Toast.LENGTH_SHORT).show();
            Uri=JILU_JSON+userName;
            // 启动 AsyncTask 以获取数据
            task = new JiluAsyncTask();
            task.executeOnExecutor(THREAD_POOL_EXECUTOR,Uri);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.zhantuoer.BROADCAST");
        receiver = new Force();
        getActivity().registerReceiver(receiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jilu, container, false);
        drawerLayout = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        ImageView bar_start = (ImageView)rootView.findViewById(R.id.bar_start);
        ImageView time_table = (ImageView)rootView.findViewById(R.id.time_table);
        ImageView paihang =(ImageView)rootView.findViewById(R.id.paihang);
        ImageView fenxiang =(ImageView)rootView.findViewById(R.id.jilu_fenxiang);
        TextView datcount = (TextView)rootView.findViewById(R.id.day_count);
        bar_start.setOnClickListener(this);
        time_table.setOnClickListener(this);
        paihang.setOnClickListener(this);
        fenxiang.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bar_start:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.time_table:
                Intent intent = new Intent(getActivity(),TongJiActivity.class);
                startActivity(intent);
                break;
            case R.id.paihang:
                Toast.makeText(getActivity(),"正在开发中",Toast.LENGTH_SHORT).show();
                break;
            case R.id.jilu_fenxiang:
                Toast.makeText(getActivity(),"正在开发中",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class JiluAsyncTask extends AsyncTask<String, Void, List<JiLu_structor>> {

        @Override
        protected List<JiLu_structor> doInBackground(String... urls) {
            // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<JiLu_structor> result = JsonTools.jiluHelp(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<JiLu_structor> data) {
            Log.w(TAG, "onPostExecute: "+data );
            if (data != null && !data.isEmpty()) {
                // 创建新适配器，将空列表作为输入
                JiLuAdapter mAdapter = new JiLuAdapter(getActivity(),new ArrayList<JiLu_structor>());
                jilus.addAll(data);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(),"网络好像有点问题",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class Force extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new JiluAsyncTask().execute(Uri);
        }
    }
}

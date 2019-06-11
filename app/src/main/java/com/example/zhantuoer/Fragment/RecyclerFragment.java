package com.example.zhantuoer.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhantuoer.JsonTools;
import com.example.zhantuoer.R;
import com.example.zhantuoer.RecyclerActivity;
import com.example.zhantuoer.SearchActivity;
import com.example.zhantuoer.SendGoalActivity;

import java.util.ArrayList;
import java.util.List;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static com.example.zhantuoer.Fragment.GoalAdapter.recyclerGoals;

public class RecyclerFragment extends Fragment implements View.OnClickListener {
    public static final String LOG_TAG = RecyclerActivity.class.getName();
    private  SwipeRefreshLayout swipeRefreshLayout;
    private static final String RECYCLER_JSON = JsonTools.CONSTURL+"zhantuoer/indexLogin";
    private  RecyclerView recyclerView;
    GoalAsyncTask task;
    //侧边栏按钮
    private DrawerLayout drawerLayout;
    //无网络时失败提示
    LinearLayout fail;
    //无网络时点击重试文本
    TextView refresh;

    public RecyclerFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onStop() {
        super.onStop();
        //为解决BUG，重新初始化
        swipeRefreshLayout =(SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh);
        //下拉刷新
        swipeRefreshLayout.setColorSchemeResources(R.color.md_blue_500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                new GoalAsyncTask().execute(RECYCLER_JSON);

            }
        });
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //下拉刷新初始化
        swipeRefreshLayout =(SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh);

        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler);
        //layoutManager用于指定布局方式，这里为线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // 启动 AsyncTask 以获取数据
        task = new GoalAsyncTask();
        task.executeOnExecutor(THREAD_POOL_EXECUTOR,RECYCLER_JSON);

        //下拉刷新
        swipeRefreshLayout.setColorSchemeResources(R.color.md_blue_500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {

                new GoalAsyncTask().execute(RECYCLER_JSON);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_recycler, container, false);
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
        fail = (LinearLayout)rootView.findViewById(R.id.fail);
        refresh = (TextView)rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);

        /*滚动列表的List
         * 最开始使用使用了自定义List，每个list对象是一个RecyclerGoal，然后
         *  List<RecyclerGoal> goalList = new ArrayList<RecyclerGoal>();
         *  后为适配json，使用extractGoals()来初始化而不是直接goalList.add
         *  extractGoals()方法是new ArrayList<RecyclerGoal>()构造方法的衍生。
         */
//        List<RecyclerGoal> goalList = extractGoals();
//        //初始化资源列表
//        for(int i = 0;i<20;i++) {
//            goalList.add(new RecyclerGoal(R.drawable.gingerbread, "用户名", "标题"));
//       }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar, menu);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                // 启动 AsyncTask 以获取数据
                task = new GoalAsyncTask();
                task.executeOnExecutor(THREAD_POOL_EXECUTOR,RECYCLER_JSON);
                break;
            default:
        }
    }

    /**
     * AsyncTask类用于在后台线程上执行网络请求，然后使用响应中的列表更新 UI。
     * AsyncTask 有三个泛型参数：输入类型、用于进度更新的类型和输出类型。
     * 我们将仅重写 AsyncTask 的两个方法：doInBackground() 和 onPostExecute()。
     * doInBackground() 方法会在后台线程上运行，因此可以运行长时间运行的代码（如网络活动），而不会干扰应用的响应性。
     * onPostExecute() 在 UI 线程上运行，系统会将 doInBackground() 方法的结果传递给它，因此该方法可使用生成的数据更新 UI。
     */
    private class GoalAsyncTask extends AsyncTask<String, Void, List<RecyclerGoal>> {
        @Override
        protected void onPreExecute() {
            fail.setVisibility(View.GONE);
            if(swipeRefreshLayout!=null) {
                swipeRefreshLayout.setRefreshing(true);
            }
        }
        @Override
        protected List<RecyclerGoal> doInBackground(String... urls) {
            // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<RecyclerGoal> result = JsonTools.fetchData(urls[0]);
            return result;
        }
        @Override
        protected void onPostExecute(List<RecyclerGoal> data) {
            // 如果存在有效列表，则将其添加到适配器的数据集。这将触发 ListView 执行更新。
            if (data != null && !data.isEmpty()) {
                // 创建新适配器，将空列表作为输入
                GoalAdapter mAdapter = new GoalAdapter(getActivity(),new ArrayList<RecyclerGoal>());
                recyclerGoals.addAll(data);
                //由于默认ArrayAdapter只能完成一条字符串的输出，所以构建自定义适配器并继承自Adapter，
                //将数据给由适配器，并由适配器完成工作。
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }else{
                fail.setVisibility(View.VISIBLE);
            }
            if(swipeRefreshLayout!=null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

}
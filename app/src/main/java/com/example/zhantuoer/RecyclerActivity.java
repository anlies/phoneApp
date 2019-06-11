package com.example.zhantuoer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhantuoer.Fragment.FaXianFragment;
import com.example.zhantuoer.Fragment.FriendFragment;
import com.example.zhantuoer.Fragment.JiLuFragment;
import com.example.zhantuoer.Fragment.RecyclerFragment;

import java.lang.reflect.Field;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static com.example.zhantuoer.LoginActivity.URL_Login;
import static com.example.zhantuoer.R.id.bottomNav;
import static com.example.zhantuoer.R.id.userNiCheng;

public class RecyclerActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerActivity";
    //这是要尝试登陆的账号
    protected static String user_name;
    private RecyclerFragment recyclerFragment;                     //定义首页fragment
    private JiLuFragment jiLuFragment;                      //定义记录fragment
    private FaXianFragment faXianFragment;
    private FriendFragment friendFragment;
    private Fragment isFragment;                         //记录当前正在使用的fragment
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start_page_Activity .instance.finish();
        //沉浸式状态栏
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        read_files();
        setContentView(R.layout.activity_category);
        //定义侧边栏列表项的对象
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        //动态修改侧边栏用户名字
        final View headerView = navigationView.getHeaderView(0);
        final TextView textview = (TextView)headerView.findViewById(userNiCheng);
        if(user_name.length()==0){
            textview.setText("请点击登录");
            textview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RecyclerActivity.this,LoginActivity.class));
                }
            });
        }else{
            textview.setText(user_name);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.update_info:
                        Intent intent=new Intent(RecyclerActivity.this,settingsActivity.class);
                        startActivity(intent);
                        RecyclerActivity.this.overridePendingTransition(0, 0);
                        break;
                    case R.id.myMoney:
                        if(user_name.length()==0){
                            Toast.makeText(RecyclerActivity.this,"登陆获取更多精彩",Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent1 = new Intent(RecyclerActivity.this, JiFenActivity.class);
                            intent1.putExtra("user_phone", user_name);
                            startActivity(intent1);
                        }
                        break;
                    case R.id.shezhi:
                        startActivity(new Intent(RecyclerActivity.this,SystemSettingsActivity.class));
                        break;
                    case R.id.quitSystem:
                        //最外层Snackbar为屏幕底部提示框
                        //内层onClick执行后表示清空用户数据文件并显示在侧边栏中
                        Snackbar.make(headerView,"你确定要退出登录吗？",Snackbar.LENGTH_LONG).setAction("我确定",new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor editor=RecyclerActivity.this.getSharedPreferences("data",MODE_PRIVATE).edit();
                                editor.putString("username","");
                                editor.putString("password","");
                                editor.putBoolean("isSuccess",false);
                                editor.apply();
                                user_name="";
                                textview.setText("请点击登录");
                                textview.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(RecyclerActivity.this,LoginActivity.class));
                                    }
                                });
                                Toast.makeText(RecyclerActivity.this,"你已退出登录",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                        break;
                }
                return true;
            }
        });
        initFragment(savedInstanceState);
        bottomNavigationView =(BottomNavigationView)findViewById(bottomNav);
        disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    public void initFragment(Bundle savedInstanceState)
    {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if(savedInstanceState==null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if(recyclerFragment ==null) {
                recyclerFragment = new RecyclerFragment();
            }
            isFragment = recyclerFragment;
            ft.replace(R.id.container, recyclerFragment).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(recyclerFragment ==null) {
                        recyclerFragment = new RecyclerFragment();
                    }
                    switchContent(isFragment, recyclerFragment);
                    return true;
                case R.id.navigation_faxian:
                    if(faXianFragment == null){
                        faXianFragment = new FaXianFragment();
                    }
                    switchContent(isFragment, faXianFragment);
                    return true;
                case R.id.navigation_message:
                    if(friendFragment == null){
                        friendFragment = new FriendFragment();
                    }
                    switchContent(isFragment,friendFragment);
                    return true;
                case R.id.navigation_geren:
                    if(jiLuFragment ==null)
                    {
                        jiLuFragment = new JiLuFragment();
                    }
                    switchContent(isFragment, jiLuFragment);
                    return true;
            }
            return false;
        }

    };
    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                ft.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        //动态修改侧边栏用户名字
        final View headerView = navigationView.getHeaderView(0);
        final TextView textview = (TextView)headerView.findViewById(userNiCheng);
        if(user_name.length()==0){
            textview.setText("请点击登录");
            textview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RecyclerActivity.this,LoginActivity.class));
                }
            });
        }else{
            textview.setText(user_name);
        }
    }

    //利用反射关闭底部导航栏默认动画效果，使多个按钮平分界面
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    //读取文件内用户名密码
    private void read_files(){
        SharedPreferences pref =RecyclerActivity.this.getSharedPreferences("data",MODE_PRIVATE);
        user_name=pref.getString("username","");
        String password=pref.getString("password","");
        Boolean isFirstSuccess = pref.getBoolean("isSuccess",false);
        Log.w(TAG, "read_files: "+user_name );
        Log.w(TAG, "read_files: "+password );
        Log.w(TAG, "read_files: "+isFirstSuccess );
        if(user_name.length()==0||password.length()==0){
            firstdialog();
        }else if(!isFirstSuccess){
            String registerUrlStr = URL_Login + "?PhoneNumber=" + user_name + "&Password=" + password;
            new MyAsyncTask().executeOnExecutor(THREAD_POOL_EXECUTOR,registerUrlStr);
        }else{
            SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
            editor.putBoolean("isSuccess",false);
            editor.apply();
        }
    }

    //未登录时弹出提示框
    public void firstdialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(RecyclerActivity.this);
        dialog.setTitle("您还未登录");
        dialog.setMessage("请问您是否登录？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("是，点击登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(RecyclerActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("否，游客模式", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    //自动登录网络连接类
    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(RecyclerActivity.this,"正在登录中,请稍后",Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            if (params.length < 1 || params[0] == null) {
                return null;
            }
            return JsonTools.loginhelp(params[0]);
        }
        //运行在UI线程中，所以可以直接操作UI元素
        @Override
        protected void onPostExecute(String s) {
            Log.w(TAG, "onPostExecute: 登陆"+s );
            if(s.length()==0){
                Toast.makeText(RecyclerActivity.this,"网络好像有点问题",Toast.LENGTH_SHORT).show();
            }else if(s.equals("true")){
                Toast.makeText(RecyclerActivity.this,"自动登录成功",Toast.LENGTH_SHORT).show();
            }else if(s.equals("false")){
                Toast.makeText(RecyclerActivity.this,"自动登录失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit(){
        if(System.currentTimeMillis()-exitTime>2000){
            Toast.makeText(getApplication(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
            exitTime=System.currentTimeMillis();
        }else {
            finish();
            System.exit(0);
        }
    }

}

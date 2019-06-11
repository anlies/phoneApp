package com.example.zhantuoer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.zhantuoer.RecyclerActivity.user_name;


public class LoginActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView mregister;
    private Button mLogin;
    private EditText user;
    private TextView Fpass;
    private EditText password;
    protected static final String URL_Login = JsonTools.CONSTURL+"zhantuoer/LoginServlet";
    private CheckBox Vlogin;
    private TextView protocol_text;
    private CheckBox protocol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.admin);
        password = (EditText) findViewById(R.id.password);
        //测试用户名密码
        user.setText("13000000000");
        password.setText("123456");
        //跳转注册页面
        mregister = (TextView) findViewById(R.id.Register0);
        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });
        //判断用户名密码
        mLogin = (Button) findViewById(R.id.mLogin);
        mLogin.getBackground().setAlpha(150);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_user = user.getText().toString();
                String text_pass = password.getText().toString();
                if (text_user.length() == 0 || text_pass.length() == 0) {
                    Toast.makeText(getApplicationContext(), "请输入用户名或密码", Toast.LENGTH_LONG).show();
                } else {
                    login(text_user, text_pass);
                }
            }
        });
        //跳转到修改密码
        Fpass = (TextView) findViewById(R.id.ForgetPass);
        Fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        //复选框选中自动登录事件
        Vlogin=(CheckBox)findViewById(R.id.vLogin);
        Vlogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }else {

                }
            }
        });
        protocol_text=(TextView)findViewById(R.id.textView5);
        protocol_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,protocol_Activity.class);
                startActivity(intent);
            }
        });
        //同意用户协议
        protocol=(CheckBox)findViewById(R.id.protocol);
        protocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mLogin.setClickable(true);
                }else {
                    mLogin.setClickable(false);
                }
            }
        });
    }
    //用户登录成功时自动保存用户名密码
    private void save_files(){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("username",user.getText().toString());
        editor.putString("password",password.getText().toString());
        //加入isFirstSuccess,这样当用户是登录界面成功进入的，就无需在主界面自动登录，以减少网络请求
        editor.putBoolean("isSuccess",true);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void login(String account, String password) {
        String registerUrlStr = URL_Login + "?PhoneNumber=" + account + "&Password=" + password;
        new MyAsyncTask().execute(registerUrlStr);
    }


    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("正在加载中，请等待");
            progressDialog.setCancelable(true);
            progressDialog.show();
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
            //关闭加载框
            progressDialog.dismiss();
            if(s.length()==0){
                Toast.makeText(LoginActivity.this,"网络好像有点问题",Toast.LENGTH_SHORT).show();
            }else if(s.equals("true")){
                save_files();
                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                user_name = user.getText().toString();
                Intent intent=new Intent(LoginActivity.this,RecyclerActivity.class);
                finish();
                startActivity(intent);
            }else if(s.equals("false")){
                Toast.makeText(LoginActivity.this,"你输入的用户名密码有误",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

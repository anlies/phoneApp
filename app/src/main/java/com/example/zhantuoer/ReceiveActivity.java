package com.example.zhantuoer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class ReceiveActivity extends AppCompatActivity {
    private static final String TAG = "ReceiveActivity";
    private static final String URL_Login = JsonTools.CONSTURL+"zhantuoer/RegistServlet";
    private String Phone_number;
    private ProgressDialog progressDialog;
    private TextView Rec_Message;
    private Button Re_recButton;
    private Button Submit_Button;
    private EditText Ident_Number;
    private EditText InputPassword;
    private EditText CheckUserPassword;
    //回调
    EventHandler eventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), new Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理成功得到验证码的结果
                            // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                            Toast.makeText(ReceiveActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO 处理错误的结果
                            Toast.makeText(ReceiveActivity.this,"连接服务器有点问题",Toast.LENGTH_SHORT).show();
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理验证码验证通过的结果
                            Toast.makeText(getApplication(),"验证成功",Toast.LENGTH_SHORT).show();
//                            Log.w(TAG, "handleMessage: "+Phone_number );
                            login(Phone_number,InputPassword.getText().toString());
                        } else {
                            // TODO 处理错误的结果
                            Toast.makeText(getApplication(),"短信验证失败",Toast.LENGTH_SHORT).show();
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    // todo其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                    return false;
                }
            }).sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MobSDK.init(this,"27e2554e83a59","ebcbe74f2a18e491e5d73e0c6ec276b1");
        SMSSDK.registerEventHandler(eventHandler);
        timer.start();

        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if(Build.VERSION.SDK_INT>=21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            setContentView(R.layout.activity_receive);
        }

        //获取上个界面EditText的值
        Rec_Message=(TextView) findViewById(R.id.Phonenumber);
        Intent intent=getIntent();
        Phone_number=intent.getStringExtra("String");
        SMSSDK.getVerificationCode("86",Phone_number);
        Rec_Message.setText(Phone_number);

        //重新发送验证码
        Re_recButton=(Button)findViewById(R.id.receiveident);
        Re_recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.start();
                SMSSDK.getVerificationCode("86",Phone_number);
            }
        });

        //获取EditText中验证码的值
        Ident_Number=(EditText)findViewById(R.id.identnumber);
        Submit_Button=(Button)findViewById(R.id.Submit_code);
        //获取密码和确认密码
        InputPassword = (EditText)findViewById(R.id.InputPassword);
        CheckUserPassword =(EditText)findViewById(R.id.CheckUserPassword);

        Submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(Ident_Number.getText().toString().length()==0){
                   Toast.makeText(getApplication(),"验证码不能为空",Toast.LENGTH_SHORT).show();
               }else if(Ident_Number.getText().toString().length()!=4){
                   Toast.makeText(getApplication(),"请输入正确的验证码",Toast.LENGTH_SHORT).show();
               }else if(!(InputPassword.getText().toString().equals(CheckUserPassword.getText().toString()))){
                   Toast.makeText(getApplication(),"两次输入密码不一致",Toast.LENGTH_SHORT).show();
               } else {
                   SMSSDK.submitVerificationCode("86",Phone_number,Ident_Number.getText().toString());
               }
            }
        });

    }

    //获取验证码后倒计时30秒
    private CountDownTimer timer=new CountDownTimer(30000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            Re_recButton.setClickable(false);
            Re_recButton.setText((millisUntilFinished/1000)+"秒后可重发");
        }
        @Override
        public void onFinish() {
            Re_recButton.setText("重新发送");
            Re_recButton.setClickable(true);
        }
    } ;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }


    private void login(String account, String password) {
        String registerUrlStr = URL_Login + "?PhoneNumber=" + account + "&Password=" + password;
        new MyAsyncTask().execute(registerUrlStr);
    }
    private void save_files(){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("username",Phone_number);
        editor.putString("password",InputPassword.getText().toString());
        //加入isFirstSuccess,这样当用户是登录界面成功进入的，就无需在主界面自动登录，以减少网络请求
        editor.putBoolean("isSuccess",true);
        editor.apply();
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ReceiveActivity.this);
            progressDialog.setTitle("正在加载中，请等待");
            progressDialog.setCancelable(false);
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
            Log.w(TAG, "onPostExecute: " + s);
            progressDialog.dismiss();
            if("".equals(s)){
                Toast.makeText(ReceiveActivity.this,"网络好像有点问题",Toast.LENGTH_SHORT).show();
            } else if(s.equals("true")){
                save_files();
                Toast.makeText(ReceiveActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ReceiveActivity.this,RecyclerActivity.class);
                startActivity(intent);
                finish();
            }else if(s.equals("false")){
                Toast.makeText(ReceiveActivity.this,"用户名和密码已经注册过了",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

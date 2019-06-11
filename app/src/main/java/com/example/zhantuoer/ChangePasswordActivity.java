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
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ChangePasswordActivity extends AppCompatActivity {
    protected static final String URL_ChangPassword = JsonTools.CONSTURL+"zhantuoer/ResetPassword";
    private Button ReceiveIdent;
    private Button Submit_Change;
    private String phone_number;
    private String checkPassword;
    private ProgressDialog progressDialog;
    private EditText Phone_number;
    private EditText New_Password;
    private EditText Check_New_Password;
    private EditText Ident_Number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobSDK.init(this,"27e2554e83a59","ebcbe74f2a18e491e5d73e0c6ec276b1");
        SMSSDK.registerEventHandler(eventHandler);
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_change_password);
        ReceiveIdent=(Button)findViewById(R.id.Receive_Ident);
        ReceiveIdent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Ident_Number();
                timer.start();
            }
        });
        Submit_Change=(Button)findViewById(R.id.CheckIdent);
        Submit_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_change();
            }
        });
    }
//提交修改信息
    protected void submit_change(){
        Phone_number=(EditText)findViewById(R.id.Phonenumber);
        phone_number=Phone_number.getText().toString();

        New_Password=(EditText)findViewById(R.id.Input_Change_Password);
        Check_New_Password=(EditText)findViewById(R.id.Check_Change_Password);
        Ident_Number=(EditText)findViewById(R.id.identnumber);
        String newpassward=New_Password.getText().toString();
        checkPassword=Check_New_Password.getText().toString();
        String identnumber=Ident_Number.getText().toString();

        if(identnumber.length()==0){
            Toast.makeText(getApplication(),"验证码不能为空",Toast.LENGTH_SHORT).show();
        }else if(identnumber.length()!=4){
            Toast.makeText(getApplication(),"请输入正确的验证码",Toast.LENGTH_SHORT).show();
        }else if(!(newpassward.equals(checkPassword))){
            Toast.makeText(getApplication(),"两次输入密码不一致",Toast.LENGTH_SHORT).show();
        } else {
            SMSSDK.submitVerificationCode("86",phone_number,identnumber);
        }
    }
    //计时器
    private CountDownTimer timer=new CountDownTimer(30000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            ReceiveIdent.setEnabled(false);
            ReceiveIdent.setText((millisUntilFinished/1000)+"秒后可重发");
        }

        @Override
        public void onFinish() {
            ReceiveIdent.setEnabled(true);
            ReceiveIdent.setText("重新发送");
        }
    };
    protected void Send_Ident_Number(){
        Phone_number=(EditText)findViewById(R.id.Phonenumber);
        String phone_number=Phone_number.getText().toString();
        if(phone_number.length()==0||phone_number.length()<11){
            Toast.makeText(getApplication(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
        }else {
            SMSSDK.getVerificationCode("86", phone_number);
        }
    }
    //回调
    EventHandler eventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理成功得到验证码的结果
                            // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                            Toast.makeText(ChangePasswordActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO 处理错误的结果
                            Toast.makeText(ChangePasswordActivity.this,"连接短信服务器有点问题",Toast.LENGTH_SHORT).show();
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理验证码验证通过的结果
                            login(phone_number,checkPassword);
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
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    private void save_files(){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("username",phone_number);
        editor.putString("password",checkPassword);
        //加入isFirstSuccess,这样当用户是登录界面成功进入的，就无需在主界面自动登录，以减少网络请求
        editor.putBoolean("isSuccess",true);
        editor.apply();
    }
    private void login(String account, String password) {
        String registerUrlStr = URL_ChangPassword + "?PhoneNumber=" + account + "&Password=" + password;
        new MyAsyncTask().execute(registerUrlStr);
    }
    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ChangePasswordActivity.this);
            progressDialog.setTitle("正在处理中，请等待");
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
            progressDialog.dismiss();
            if("".equals(s)){
                Toast.makeText(ChangePasswordActivity.this,"网络好像有点问题",Toast.LENGTH_SHORT).show();
            } else if(s.equals("true")){
                save_files();
                Toast.makeText(ChangePasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ChangePasswordActivity.this,RecyclerActivity.class);
                startActivity(intent);
                finish();
            }else if(s.equals("false")){
                Toast.makeText(ChangePasswordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

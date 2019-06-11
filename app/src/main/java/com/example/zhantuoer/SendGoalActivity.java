package com.example.zhantuoer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendGoalActivity extends AppCompatActivity {
    private static final String TAG = "SendGoalActivity";
    private ProgressDialog progressDialog;
    private EditText tittle;
    private EditText editContent;
    private EditText editTime;
    private String userName;
    private Button sendGoal;
    private Boolean isLogin = false;
    protected static final String URL_SendGoal = JsonTools.CONSTURL+"zhantuoer/publishTopicServlet";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);
        init();
        if(isLogin){
            sendMessage();
        }else{
            sendGoal.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(SendGoalActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    //做初始化工作
    private void init(){
        tittle= (EditText)findViewById(R.id.jilu_tittle);
        editContent = (EditText)findViewById(R.id.editcontent);
        SharedPreferences pref =getSharedPreferences("data",MODE_PRIVATE);
        userName =pref.getString("username","");
        sendGoal = (Button)findViewById(R.id.start_goal);
        editTime = (EditText)findViewById(R.id.editTime);
        if(userName.length()==0){
            isLogin = false;
            Toast.makeText(SendGoalActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
        }else{
            isLogin = true;
        }
    }

    //获取文本内容与用户名，准备发送信息
    private void sendMessage(){
        sendGoal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkData()) {
                    sendGoalMessage(userName, tittle.getText().toString(), editTime.getText().toString(), 12, editContent.getText().toString());
                }else{
                    Toast.makeText(SendGoalActivity.this,"请输入正确的信息",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendGoalMessage(String userName, String tittle,String goalTime,int money,String goalText) {
        String url = URL_SendGoal + "?phoneNumber=" + userName + "&goalName=" + tittle
                +"&goalTime="+goalTime+"&money="+money+"&goalText="+goalText;
        new MyAsyncTask().execute(url);
    }

    //数据检验
    private Boolean checkData(){
        Boolean isSucess = true;
        if(tittle.getText().length()==0 || editTime.getText().length()==0 ||editContent.getText().length()==0){
            isSucess = false;
        }
        return isSucess;
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SendGoalActivity.this);
            progressDialog.setTitle("正在发送中，请等待");
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
            Log.w(TAG, "onPostExecutefatie " + s);
            progressDialog.dismiss();
            if(s.length()==0){
                Toast.makeText(SendGoalActivity.this,"网络好像有点问题",Toast.LENGTH_SHORT).show();
            }
            else if(s.equals("true")){
                Toast.makeText(SendGoalActivity.this,"发送成功，请在记录页签到",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SendGoalActivity.this,RecyclerActivity.class);
                startActivity(intent);
                finish();
            }else if(s.equals("false")){
                Toast.makeText(SendGoalActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

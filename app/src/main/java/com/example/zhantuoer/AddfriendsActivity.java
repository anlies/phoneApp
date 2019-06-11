package com.example.zhantuoer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class AddfriendsActivity extends AppCompatActivity {
    private Button addfriends;
    private ImageView QRcode;
    private ImageView Back;
    private ProgressDialog progressDialog;
    private String user_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);
        SetQRcodePicture();
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (ContextCompat.checkSelfPermission(AddfriendsActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(AddfriendsActivity.this, new String[]{Manifest.permission.CAMERA},
                    1);}
        Back=(ImageView)findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddfriendsActivity.this,RecyclerActivity.class);
                startActivity(intent);
            }
        });
        addfriends=(Button)findViewById(R.id.AddFriends);
        addfriends.setOnClickListener(new View.OnClickListener() {     //跳转到扫描二维码页
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddfriendsActivity.this,CaptureActivity.class),0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {      //显示扫描二维码返回的字符串
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            String res=bundle.getString("result");  //扫描二维码返回的字符串
            guanzhu(user_phone,res);
        }
    }
    private void SetQRcodePicture(){
             Intent intent = getIntent();
             user_phone = intent.getStringExtra("user_phone");
             Bitmap bitmap= EncodingUtils.createQRCode(user_phone,190,190,null);
            QRcode = (ImageView)findViewById(R.id.QRcode);
            QRcode.setImageBitmap(bitmap);
    }

    private void guanzhu(String phoneNmuber,String fatie_user) {
        String guanzhuURL = JsonTools.CONSTURL+"zhantuoer/UserAttention?attention="+phoneNmuber+"&beAttention=" + fatie_user;
        new MyAsyncTask().execute(guanzhuURL);
    }

    //点击关注按钮后与网络通信
    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        String URIs;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AddfriendsActivity.this);
            progressDialog.setTitle("正在请求中，请等待");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            if (params.length < 1 || params[0] == null) {
                return null;
            }
            URIs = params[0];
            return JsonTools.loginhelp(URIs);
        }

        //运行在UI线程中，所以可以直接操作UI元素
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("true")){
                Toast.makeText(AddfriendsActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AddfriendsActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

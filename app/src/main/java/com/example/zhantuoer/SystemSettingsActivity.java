package com.example.zhantuoer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.zhantuoer.DownLoad.DownloadService;

public class SystemSettingsActivity extends AppCompatActivity implements View.OnClickListener{
    public static ProgressDialog proDialog;
    public static DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        proDialog = new ProgressDialog(SystemSettingsActivity.this);
        proDialog.setMessage("正在后台下载中，请稍后");
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        SwitchOpenMap();
        SwitchState();
        Switch_qiandao();
        FindLinearLayoutID();
        Intent intent_update = new Intent(this, DownloadService.class);
        startService(intent_update); // 启动服务
        bindService(intent_update, connection, BIND_AUTO_CREATE); // 绑定服务
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 2);
        }
    }
protected  void FindLinearLayoutID(){
    ImageView back;
    LinearLayout changepassword;
    LinearLayout servicephonenumber;
    LinearLayout logout;
    back = (ImageView)findViewById(R.id.back);
    servicephonenumber=(LinearLayout)findViewById(R.id.ServicePhonenumber);
    logout=(LinearLayout)findViewById(R.id.Update);
    changepassword=(LinearLayout)findViewById(R.id.ChangePassword);
    back.setOnClickListener(this);
    changepassword.setOnClickListener(this);
    servicephonenumber.setOnClickListener(this);
    logout.setOnClickListener(this);
}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.ChangePassword:
                Intent intent=new Intent(SystemSettingsActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.ServicePhonenumber:
                if(ContextCompat.checkSelfPermission(SystemSettingsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SystemSettingsActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                }else{
                    call();
                }
                break;
            case R.id.Update:
                if (downloadBinder == null) {
                    return;
                }
                PackageManager packageManager = getPackageManager();
                //getPackageName()是你当前类的包名，0代表是获取版本信息
                PackageInfo packInfo = null;
                try {
                    packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if(packInfo==null){
                    Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
                }
                else if(packInfo.versionCode==2){
                    Toast.makeText(this,"当前已是最新版本",Toast.LENGTH_SHORT).show();
                }else{
                    String url = "http://47.106.224.20:8080/zhantuoer/v2.apk";
                    downloadBinder.startDownload(url);
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    //拨打客服电话
    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:15536580470"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    //开启通知并保存开关状态
    private void SwitchState(){
        final SharedPreferences sharedPreferences;
        Switch s=(Switch)findViewById(R.id.switch2);
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            boolean name = sharedPreferences.getBoolean("flag",false);
            s.setChecked(name);
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        SharedPreferences sharedPreferences=getSharedPreferences("user",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("flag",true);
                        editor.commit();
                        //开启通知服务
                        Toast.makeText(getApplication(),"已开启通知,每天晚上10点会提醒你签到哦",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SystemSettingsActivity.this,NotificationService.class);
                        startService(intent);
                    }else {
                        SharedPreferences sharedPreferences=getSharedPreferences("user",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("flag",false);
                        editor.commit();
                        //关闭通知服务
                        Toast.makeText(getApplication(),"已关闭通知",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SystemSettingsActivity.this,NotificationService.class);
                        stopService(intent);
                    }

                }
            });
        }
    }

    //获取指纹状态
    private void Switch_qiandao(){
        final SharedPreferences mSharedPreferences;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Switch s=(Switch)findViewById(R.id.switch_qiandao);
        boolean useFingerprintPreference = mSharedPreferences
                .getBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                        true);
        if (useFingerprintPreference) {
            s.setChecked(true);
        } else {
            s.setChecked(false);
        }
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplication(),"已开启指纹签到",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplication(),"已开启密码签到",Toast.LENGTH_SHORT).show();
                }
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                            isChecked);
                    editor.apply();
            }
        });
    }
    private void SwitchOpenMap(){
        final SharedPreferences sharedPreferences;
        Switch s=(Switch)findViewById(R.id.openmap);
        sharedPreferences = getSharedPreferences("map", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            boolean name = sharedPreferences.getBoolean("flag",false);
            s.setChecked(name);
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        SharedPreferences sharedPreferences=getSharedPreferences("map",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("flag",true);
                        editor.commit();

                    }else {
                        SharedPreferences sharedPreferences=getSharedPreferences("map",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("flag",false);
                        editor.commit();
                    }

                }
            });
        }
    }
    //拨打客服电话权限申请
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "你拒绝了权限", Toast.LENGTH_SHORT).show();

                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

}

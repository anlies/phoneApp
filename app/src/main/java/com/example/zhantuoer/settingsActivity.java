package com.example.zhantuoer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class settingsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "settingsActivity";
    private static final String RECYCLER_JSON = JsonTools.CONSTURL+"zhantuoer/UserMassageRead?phoneNumber=";
    private static  final int CROP_SMALL_PICTURE=0;
    public static final int TAKE_PHOTO = 1;
    GoalAsyncTask task;
    public static final int CHOOSE_PHOTO = 2;
    private ProgressDialog progressDialog;

    //自己的用户名
    private String user_phone;

    private ImageView picture;

    private TextView nickname;

    private Uri imageUri;
    //是否已登录
    private Boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        nickname=(TextView)findViewById(R.id.true_nick_name);
        readFile();
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        picture=(ImageView)findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePhotoDialog choosePhotoDialog=new ChoosePhotoDialog(settingsActivity.this);
                choosePhotoDialog.show();
            }
        });
        LinearLayoutClick();
        getSavePicture();
    }
    //保存资料设置
    protected void saveFile(){
        TextView sex=(TextView)findViewById(R.id.true_sex);
        TextView age=(TextView)findViewById(R.id.true_age);
        TextView phonenumber=(TextView)findViewById(R.id.true_phone_number);
        TextView personsign=(TextView)findViewById(R.id.true_person_sign);
        SharedPreferences.Editor editor=getSharedPreferences("Users_Info",MODE_PRIVATE).edit();
        editor.putString("nickname",nickname.getText().toString());
        editor.putString("sex",sex.getText().toString());
        editor.putString("age",age.getText().toString());
        editor.putString("phonenumber",phonenumber.getText().toString());
        editor.putString("personsign",personsign.getText().toString());
        editor.commit();
    }
    //读取资料设置
    protected void readFile(){
        nickname=(TextView)findViewById(R.id.true_nick_name);
        TextView sex=(TextView)findViewById(R.id.true_sex);
        TextView age=(TextView)findViewById(R.id.true_age);
        TextView phonenumber=(TextView)findViewById(R.id.true_phone_number);
        TextView personsign=(TextView)findViewById(R.id.true_person_sign);
        SharedPreferences sharedPreferences=getSharedPreferences("Users_Info",MODE_PRIVATE);
        String get_nickname=sharedPreferences.getString("nickname","");
        String get_sex= sharedPreferences.getString("sex","");
        String get_age= sharedPreferences.getString("age","");
        String get_phonenumber=sharedPreferences.getString("phonenumber","");
        String get_personsign= sharedPreferences.getString("personsign","");
        nickname.setText(get_nickname);
        sex.setText(get_sex);
        age.setText(get_age);
        phonenumber.setText(get_phonenumber);
        personsign.setText(get_personsign);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(settingsActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(settingsActivity.this, new String[]{Manifest.permission.CAMERA},
                    1);}
        readUser_Phone();
    }
    private void getInfo() {
        task = new GoalAsyncTask();
        task.executeOnExecutor(THREAD_POOL_EXECUTOR,RECYCLER_JSON+user_phone);
    }
    //获取用户名
    private void readUser_Phone(){
        SharedPreferences pref =getSharedPreferences("data",MODE_PRIVATE);
        user_phone =pref.getString("username","");
        if(user_phone.length()==0){
            isLogin = false;
            Toast.makeText(settingsActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
        }else{
            isLogin = true;
            getInfo();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveFile();

    }

    private void LinearLayoutClick(){
        LinearLayout Nick_Name=(LinearLayout) findViewById(R.id.Nick_Name);
        LinearLayout Sex=(LinearLayout)findViewById(R.id.Sex);
        LinearLayout Phone_number=(LinearLayout)findViewById(R.id.Phone_number);
        LinearLayout Person_Sign=(LinearLayout)findViewById(R.id.Person_Sign);
        LinearLayout More_Info=(LinearLayout)findViewById(R.id.More_Info);
        LinearLayout Age=(LinearLayout)findViewById(R.id.Age);
        ImageView back = (ImageView)findViewById(R.id.back);
        ImageView send = (ImageView)findViewById(R.id.send);

        Nick_Name.setOnClickListener(this);
        Sex.setOnClickListener(this);
        Phone_number.setOnClickListener(this);
        Person_Sign.setOnClickListener(this);
        More_Info.setOnClickListener(this);
        Age.setOnClickListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Nick_Name:
                final EditText nickname1=new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入昵称")
                        .setView(nickname1)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView textView=(TextView)findViewById(R.id.true_nick_name);
                                textView.setText(nickname1.getText());
                            }
                        })
                        .setNegativeButton("取消",null).show();
                break;
            case R.id.Sex:

                final String [] sex=new String[]{"男","女"};
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("选择性别").setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView Sex=(TextView)findViewById(R.id.true_sex);
                        if(which==0){
                            Sex.setText(sex[0]);
                        }else{
                            Sex.setText(sex[1]);
                        }
                    }
                });
                builder.show();
                break;
            case R.id.Age:
                final EditText age=new EditText(this);
                age.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(this)
                        .setTitle("请输入年龄")
                        .setView(age)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView textView=(TextView)findViewById(R.id.true_age);
                                textView.setText(age.getText());
                            }
                        })
                        .setNegativeButton("取消",null).show();
                break;

            case R.id.Phone_number:
                final EditText phonenumber=new EditText(this);
                phonenumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(this)
                        .setTitle("请输入手机号")
                        .setView(phonenumber)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView textView=(TextView)findViewById(R.id.true_phone_number);
                                textView.setText(phonenumber.getText());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();

                break;
            case R.id.Person_Sign:
                final EditText personsign=new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("个性签名")
                        .setView(personsign)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView textView=(TextView)findViewById(R.id.true_person_sign);
                                textView.setText(personsign.getText());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.More_Info:
                Intent intent=new Intent(settingsActivity.this,More_InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.send:
                nicheng(user_phone,nickname.getText().toString());
                break;
            default:break;
        }
    }

    public class ChoosePhotoDialog extends AlertDialog {
        private ImageView imageView;
        private ImageView imageView1;
        public ChoosePhotoDialog(@NonNull Context context){
            super(context);

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_alert__dialog);
            //点击调用摄像头
            imageView=(ImageView)findViewById(R.id.take_photo);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //从相机拍照获取照片
                    // 创建File对象，用于存储拍照后的图片
                    File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                    try {
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT < 24) {
                        imageUri = Uri.fromFile(outputImage);
                    } else {
                        imageUri = FileProvider.getUriForFile(settingsActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
                    }

                    // 启动相机程序
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                }

            });
            //点击调用相册
            imageView1=(ImageView)findViewById(R.id.picture_box);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPicFromAlbm();
                }
            });
        }
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        cropPhoto(imageUri);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    cropPhoto(imageUri);
                }
                break;

            //获取裁剪到的图片并在imageview上显示
            case CROP_SMALL_PICTURE:
                Bundle extras=data.getExtras();
                if(extras!=null){
                    Bitmap photo=extras.getParcelable("data");
                    picture.setImageBitmap(photo);
                    saveImage("crop",photo);
                }
            default:
                break;
        }
    }
    //从相册中选择图片
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, CHOOSE_PHOTO);
    }
    //裁剪图片
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    //将图片保存
    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GoalAsyncTask extends AsyncTask<String, Void, List<GeRenData>> {

        @Override
        protected List<GeRenData> doInBackground(String... urls) {
            // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<GeRenData> result = JsonTools.gerendataHelp(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<GeRenData> data) {
            Log.w(TAG, "onPostExecute: "+data );
            // 如果存在数据集，则执行更新。
            if (data != null && !data.isEmpty()) {
                GeRenData gerendata = data.get(0);
                Log.w(TAG, "onPostExecute: data"+gerendata.getCity()+gerendata.getHeadPhoto()+gerendata.getQianming()+ gerendata.getJob());
//                Glide.with(settingsActivity.this)
//                        .load(gerendata.getHeadPhoto()) //加载地址
//                        .error(R.drawable.nav_icon)
//                        .into(picture);//显示的位置
                SharedPreferences.Editor editor=getSharedPreferences("Users_Info",MODE_PRIVATE).edit();
                editor.putString("nickname",gerendata.getNicheng());
                editor.putString("sex",gerendata.getSex());
                editor.putString("age",gerendata.getAge());
                editor.putString("phonenumber",gerendata.getPhonenumber());
                editor.putString("personsign",gerendata.getQianming());
                editor.putString("city",gerendata.getCity());
                editor.putString("education",gerendata.getEdu());
                editor.putString("workspace",gerendata.getJob());
                editor.commit();
                readFile();
            }else{
                Toast.makeText(settingsActivity.this,"网络好像有点问题",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //将保存的图片显示在ImageView上
   protected  void getSavePicture(){
       //获得SD卡根目录路径
        File sdDir =Environment.getExternalStorageDirectory();
       String sdpath = sdDir.getAbsolutePath();
       //获取指定图片路径
       String filepath = sdpath + File.separator + "crop.jpg";
       File file = new File(filepath);
       if (file.exists()) {
           Bitmap bm = BitmapFactory.decodeFile(filepath);
           // 将图片显示到ImageView中
           picture.setImageBitmap(bm);
       }
   }


    private void nicheng(String account, String nicheng) {
        String registerUrlStr = JsonTools.CONSTURL+"zhantuoer/UserInfo?nickName="+nicheng+"&phoneNumber="+account;
        new NiChengTask().execute(registerUrlStr);
    }


    private class NiChengTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(settingsActivity.this);
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
                Toast.makeText(settingsActivity.this,"网络好像有点问题",Toast.LENGTH_SHORT).show();
            }else if(s.equals("true")){
                Toast.makeText(settingsActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

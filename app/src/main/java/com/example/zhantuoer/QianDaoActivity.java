package com.example.zhantuoer;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zhantuoer.Fingerprint.FingerprintAuthenticationDialogFragment;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class QianDaoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String URL = JsonTools.CONSTURL+"zhantuoer/publishSign";
    EditText editcontent;
    private ProgressDialog progressDialog;
    String tid;

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    public static final String DEFAULT_KEY_NAME = "default_key";
    Cipher defaultCipher;

    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private SharedPreferences mSharedPreferences;
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
        setContentView(R.layout.activity_qiandao);
        click_init();
    }

    private void click_init(){

        ImageView back;
        ImageView send;
        ImageView xiangji;

        back = (ImageView)findViewById(R.id.back);
        send = (ImageView)findViewById(R.id.send_my_qiandao);
        xiangji = (ImageView)findViewById(R.id.xiangji);
        editcontent = (EditText)findViewById(R.id.editcontent);
        Intent intent = getIntent();
        tid = intent.getStringExtra("tID");

        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("未能获得KeyStore实例", e);
        }
        try {
            mKeyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("未能获得KeyGenerator实例", e);
        }

        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("未能获得Cipher实例", e);
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
        FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
        // 下面的这行注释阻止了Android Studio的错误检查
        // noinspection ResourceType
        if (!keyguardManager.isKeyguardSecure() || !fingerprintManager.hasEnrolledFingerprints()) {
            Toast.makeText(this,
                    "您的手机未设置指纹，只能普通签到",
                    Toast.LENGTH_LONG).show();
        }else{
            createKey(DEFAULT_KEY_NAME, true);
        }
        send.setOnClickListener(
                new PurchaseButtonClickListener(defaultCipher, DEFAULT_KEY_NAME));
        back.setOnClickListener(this);
        xiangji.setOnClickListener(this);
    }

    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Cipher初始化失败", e);
        }
    }

    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        try {
            mKeyStore.load(null);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onPurchased(boolean withFingerprint,
                            @Nullable FingerprintManager.CryptoObject cryptoObject) {
        if (withFingerprint) {
            assert cryptoObject != null;
            qiandao(tid,editcontent.getText().toString());
        } else {
            qiandao(tid,editcontent.getText().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.xiangji:
                Toast.makeText(QianDaoActivity.this,"正在紧急开发中",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    private void qiandao(String tid, String signText) {
        String qiandaoURL = URL + "?tid=" + tid + "&signText=" + signText;
        new MyAsyncTask().execute(qiandaoURL);
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(QianDaoActivity.this);
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
            progressDialog.dismiss();
            if(s.length()==0){
                Toast.makeText(QianDaoActivity.this,"网络好像有点问题",Toast.LENGTH_SHORT).show();
            }
            else if(s.equals("true")){
                Toast.makeText(QianDaoActivity.this,"签到成功",Toast.LENGTH_SHORT).show();
                //发送广播通知记录页更新
                sendBroadcast(new Intent("com.example.zhantuoer.BROADCAST"));
                //返回界面
                Intent intent=new Intent(QianDaoActivity.this,RecyclerActivity.class);
                startActivity(intent);
                finish();
            }else if(s.equals("false")){
                Toast.makeText(QianDaoActivity.this,"签到失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class PurchaseButtonClickListener implements View.OnClickListener {

        Cipher mCipher;
        String mKeyName;

        PurchaseButtonClickListener(Cipher cipher, String keyName) {
            mCipher = cipher;
            mKeyName = keyName;
        }

        @Override
        public void onClick(View view) {
            if(editcontent.getText().toString().length()==0){
                Toast.makeText(QianDaoActivity.this,"请输入内容",Toast.LENGTH_SHORT).show();
                return;
            }
            if (initCipher(mCipher, mKeyName)) {
                FingerprintAuthenticationDialogFragment fragment
                        = new FingerprintAuthenticationDialogFragment();
                fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                boolean useFingerprintPreference = mSharedPreferences
                        .getBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                                true);
                if (useFingerprintPreference) {
                    fragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                } else {
                    fragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
                }
                fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            } else {
                FingerprintAuthenticationDialogFragment fragment
                        = new FingerprintAuthenticationDialogFragment();
                fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                fragment.setStage(
                        FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
                fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        }
    }
}

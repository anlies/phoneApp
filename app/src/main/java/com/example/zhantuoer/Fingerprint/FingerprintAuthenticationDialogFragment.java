package com.example.zhantuoer.Fingerprint;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhantuoer.QianDaoActivity;
import com.example.zhantuoer.R;


/**
 * 指纹识别对话框类，部分代码参考了谷歌官方API
 */
public class FingerprintAuthenticationDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener, FingerprintUiHelper.Callback {

    public Button mCancelButton;
    public Button mSecondDialogButton;
    public View mFingerprintContent;
    public View mBackupContent;
    public EditText mPassword;
    public CheckBox mUseFingerprintFutureCheckBox;
    public TextView mPasswordDescriptionTextView;
    public TextView mNewFingerprintEnrolledTextView;

    public Stage mStage = Stage.FINGERPRINT;

    public FingerprintManager.CryptoObject mCryptoObject;
    public FingerprintUiHelper mFingerprintUiHelper;
    public QianDaoActivity mActivity;

    public InputMethodManager mInputMethodManager;
    public SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.sign_in));
        View v = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mSecondDialogButton = (Button) v.findViewById(R.id.second_dialog_button);
        mSecondDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStage == Stage.FINGERPRINT) {
                    goToBackup();
                } else {
                    verifyPassword();
                }
            }
        });
        mFingerprintContent = v.findViewById(R.id.fingerprint_container);
        mBackupContent = v.findViewById(R.id.backup_container);
        mPassword = (EditText) v.findViewById(R.id.password);
        mPassword.setOnEditorActionListener(this);
        mPasswordDescriptionTextView = (TextView) v.findViewById(R.id.password_description);
        mUseFingerprintFutureCheckBox = (CheckBox)
                v.findViewById(R.id.use_fingerprint_in_future_check);
        mNewFingerprintEnrolledTextView = (TextView)
                v.findViewById(R.id.new_fingerprint_enrolled_description);
        mFingerprintUiHelper = new FingerprintUiHelper(
                mActivity.getSystemService(FingerprintManager.class),
                (ImageView) v.findViewById(R.id.fingerprint_icon),
                (TextView) v.findViewById(R.id.fingerprint_status), this);
        updateStage();

        if (!mFingerprintUiHelper.isFingerprintAuthAvailable()) {
            goToBackup();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStage == Stage.FINGERPRINT) {
            mFingerprintUiHelper.startListening(mCryptoObject);
        }
    }

    public void setStage(Stage stage) {
        mStage = stage;
    }

    @Override
    public void onPause() {
        super.onPause();
        mFingerprintUiHelper.stopListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (QianDaoActivity) getActivity();
        mInputMethodManager = context.getSystemService(InputMethodManager.class);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
    }

    private void goToBackup() {
        mStage = Stage.PASSWORD;
        updateStage();
        mPassword.requestFocus();
        mPassword.postDelayed(mShowKeyboardRunnable, 500);
        mFingerprintUiHelper.stopListening();
    }

    private void verifyPassword() {
        if (!checkPassword(mPassword.getText().toString())) {
            return;
        }
        if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                    mUseFingerprintFutureCheckBox.isChecked());
            editor.apply();

            if (mUseFingerprintFutureCheckBox.isChecked()) {
                mActivity.createKey(QianDaoActivity.DEFAULT_KEY_NAME, true);
                mStage = Stage.FINGERPRINT;
            }
        }
        mPassword.setText("");
        mActivity.onPurchased(false , null);
        dismiss();
    }

    private boolean checkPassword(String password) {
        return password.length() > 0;
    }

    private final Runnable mShowKeyboardRunnable = new Runnable() {
        @Override
        public void run() {
            mInputMethodManager.showSoftInput(mPassword, 0);
        }
    };

    private void updateStage() {
        switch (mStage) {
            case FINGERPRINT:
                mCancelButton.setText(R.string.cancel);
                mSecondDialogButton.setText(R.string.use_password);
                mFingerprintContent.setVisibility(View.VISIBLE);
                mBackupContent.setVisibility(View.GONE);
                break;
            case NEW_FINGERPRINT_ENROLLED:
            case PASSWORD:
                mCancelButton.setText(R.string.cancel);
                mSecondDialogButton.setText(R.string.ok);
                mFingerprintContent.setVisibility(View.GONE);
                mBackupContent.setVisibility(View.VISIBLE);
                if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
                mPasswordDescriptionTextView.setVisibility(View.GONE);
                mNewFingerprintEnrolledTextView.setVisibility(View.VISIBLE);
                mUseFingerprintFutureCheckBox.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            verifyPassword();
            return true;
        }
        return false;
    }

    @Override
    public void onAuthenticated() {
        mActivity.onPurchased(true, mCryptoObject);
        dismiss();
    }

    @Override
    public void onError() {
        goToBackup();
    }

    public enum Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
        PASSWORD
    }
}

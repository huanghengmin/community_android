package com.otg.community.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.otg.community.R;
import com.otg.community.handler.ForgetHandler;
import com.otg.community.utils.TextWatcherAdapter;
import com.otg.community.utils.Toaster;
import com.otg.community.utils.ValidUtils;

/**
 * Created by Administrator on 16-1-21.
 */
public class ForgetPswActivity extends Activity implements View.OnClickListener{


    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_ERROR = 2;
    private EditText phoneEdit;
    private EditText pwdEdit;
    private EditText psw_nextEdit;
    private ImageButton header_left_small;
    private Button submitButton;


    private boolean loginEnabled(){
        return (!TextUtils.isEmpty(this.pwdEdit.getText())) && (!TextUtils.isEmpty(this.psw_nextEdit.getText()));
    }

    private TextWatcher login = new TextWatcherAdapter(){
        public void afterTextChanged(Editable paramAnonymousEditable){
            ForgetPswActivity.this.submitButton.setEnabled(ForgetPswActivity.this.loginEnabled());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_forget_pwd);

        phoneEdit = (EditText) findViewById(R.id.phone);
        pwdEdit = (EditText) findViewById(R.id.psw);
        psw_nextEdit = (EditText) findViewById(R.id.psw_next);
        header_left_small = (ImageButton) findViewById(R.id.header_left_small);
        header_left_small.setOnClickListener(this);
        submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(this);

        pwdEdit.addTextChangedListener(login);
        psw_nextEdit.addTextChangedListener(login);
    }


    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (!ValidUtils.isValidPhoneNumber(this.phoneEdit.getText().toString())){
                    Toaster.show(this, R.string.please_insert_success_phone_number);
                    return;
                }
                if (!ValidUtils.isValidPass(this.pwdEdit.getText().toString())){
                    Toaster.show(this, R.string.valid_pass);
                    return;
                }
                if (!ValidUtils.isSame(this.pwdEdit.getText().toString(), this.psw_nextEdit.getText().toString()))
                {
                    Toaster.show(this, R.string.valid_pass_next);
                    return;
                }
                ForgetHandler forgetHandler = new ForgetHandler(this,mHandler);
                forgetHandler.handler(phoneEdit.getText().toString(),psw_nextEdit.getText().toString());
                break;
            case R.id.header_left_small:
                ForgetPswActivity.this.finish();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESULT_SUCCESS:
                    Toaster.show(ForgetPswActivity.this, (String)msg.obj);
                    break;
                case RESULT_ERROR:
                    Toaster.show(ForgetPswActivity.this, (String)msg.obj);
                    break;
            }
        }
    };
}

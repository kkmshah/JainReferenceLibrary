package com.jainelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.jainelibrary.R;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

public class SecurityActivity extends AppCompatActivity {
    private PinEntryEditText edtEnterPin;
    private TextView btnCancel;
    private TextView tv_enter_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        initView();
    }

    private void initView() {
        btnCancel = findViewById(R.id.btn_cancel);
        tv_enter_code = findViewById(R.id.tv_enter_code);
        edtEnterPin = (PinEntryEditText) findViewById(R.id.edt_enter_pin);
        final boolean isLogin = SharedPrefManager.getInstance(SecurityActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            tv_enter_code.setText("Enter Passcode");
        }
        edtEnterPin.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (str != null && str.length() == 4) {
                    edtEnterPin.setText(null);
                    if (!isLogin) {
                        SharedPrefManager.getInstance(SecurityActivity.this).setBooleanPreference(SharedPrefManager.IS_LOGIN, true);
                        SharedPrefManager.getInstance(SecurityActivity.this).saveStringPref(SharedPrefManager.IS_CODE, str.toString());
                        Intent i = new Intent(SecurityActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Log.e("code--", "" + str.toString());
                        String isCode = SharedPrefManager.getInstance(SecurityActivity.this).getStringPref(SharedPrefManager.IS_CODE);
                        if (isCode.equalsIgnoreCase(str.toString())) {
                            Intent i = new Intent(SecurityActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Utils.showInfoDialog(SecurityActivity.this, "Please enter valid pin");
                        }
                    }
                } else {
                    Utils.showInfoDialog(SecurityActivity.this, "Please enter 4 digit pin");
                    edtEnterPin.setText(null);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtEnterPin.setText(null);
            }
        });
    }
    public void onClickDigit(View view) {
        String string = ((TextView) view).getText().toString();
        if (edtEnterPin.getText() == null) {
            edtEnterPin.setText(string);
        } else {
            edtEnterPin.setText(edtEnterPin.getText().append(string));
        }
    }
}

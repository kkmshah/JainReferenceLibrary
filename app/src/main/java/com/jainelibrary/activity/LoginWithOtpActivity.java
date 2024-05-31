package com.jainelibrary.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;
import com.jainelibrary.ForgotPasswordActivity;
import com.jainelibrary.R;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.SendOtpResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginWithOtpActivity extends AppCompatActivity {
    Button btnLogin;
    TextInputEditText edtMobile;
    TextView sign_up;
    private String strMobile;
    String isLoginId;
    LinearLayout llSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setHeader();
        declaration();
    }

    private void declaration() {
        edtMobile = findViewById(R.id.edtMobile);
        btnLogin = findViewById(R.id.btnLogin);
        llSignup = findViewById(R.id.llSignup);
        sign_up = findViewById(R.id.sign_up);
        sign_up.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        boolean isLogin = SharedPrefManager.getInstance(LoginWithOtpActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        //temp commented
        /*Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(i);
        finish();*/
        isLoginId = getIntent().getStringExtra("isLoginId");
        if (isLogin) {
            Intent i = new Intent(LoginWithOtpActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            Utils.showInfoDialog(LoginWithOtpActivity.this, "Already login");
        }
        llSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginWithOtpActivity.this, SignupActivity.class);
                i.putExtra("isLoginId", isLoginId);
                startActivity(i);
                finish();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginWithOtpActivity.this, SignupActivity.class);
                i.putExtra("isLoginId", isLoginId);
                startActivity(i);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strMobile = edtMobile.getText().toString();
                if (strMobile != null && strMobile.length() == 10) {
                    callSendOtpApi(strMobile);
                } else {
                    Utils.showInfoDialog(LoginWithOtpActivity.this, "Please enter 10 digit number");
                    return;
                }
            }
        });
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Login With Otp");
    }

    private void callSendOtpApi(String strMobile) {
        if (!ConnectionManager.checkInternetConnection(LoginWithOtpActivity.this)) {
            Utils.showInfoDialog(LoginWithOtpActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(LoginWithOtpActivity.this, "Please Wait...", false);
        ApiClient.sendOtp(strMobile, new Callback<SendOtpResModel>() {
            @Override
            public void onResponse(Call<SendOtpResModel> call, retrofit2.Response<SendOtpResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Intent i = new Intent(LoginWithOtpActivity.this, OtpActivity.class);
                        i.putExtra("isLoginId", isLoginId);
                        i.putExtra("mobile", strMobile);
                        startActivity(i);
                        finish();
                    } else {
                        Utils.showInfoDialog(LoginWithOtpActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SendOtpResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }
}

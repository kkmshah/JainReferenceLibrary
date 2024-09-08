package com.jainelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jainelibrary.activity.KeywordHighlightActivity;
import com.jainelibrary.activity.LoginWithOtpActivity;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.OtpActivity;
import com.jainelibrary.activity.ResetPasswordActivity;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.ApiResponseModel;
import com.jainelibrary.model.SendOtpResModel;
import com.jainelibrary.model.UserExistsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText edtMobile;
    String strMobileNo, strUserId, strUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initComponent();
        setHeader();
        declaration();
    }

    private void declaration() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strMobileNo = edtMobile.getText().toString();
                if(checkValidation()){
                    callCheckUserMobileApi();
                }
            }
        });
    }

    private void callCheckUserMobileApi() {
        if (!ConnectionManager.checkInternetConnection(ForgotPasswordActivity.this)) {
            Utils.showInfoDialog(ForgotPasswordActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(ForgotPasswordActivity.this, "Please Wait...", false);
        ApiClient.checkUserExists(strMobileNo, new Callback<UserExistsResModel>() {
            @Override
            public void onResponse(Call<UserExistsResModel> call, retrofit2.Response<UserExistsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                    if (response.body().isStatus()) {
                       strUserId = response.body().getUserId();
                       strUserName = response.body().getUserName();
                       if(strUserId != null && strUserId.length() > 0 && strUserName != null && strUserName.length() > 0){
                           callSendOtpApi(strMobileNo);
                       }
                       //Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Utils.showInfoDialog(ForgotPasswordActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserExistsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void callSendOtpApi(String strMobile) {
        if (!ConnectionManager.checkInternetConnection(ForgotPasswordActivity.this)) {
            Utils.showInfoDialog(ForgotPasswordActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(ForgotPasswordActivity.this, "Please Wait...", false);
        ApiClient.sendOtp(strMobile, new Callback<SendOtpResModel>() {
            @Override
            public void onResponse(Call<SendOtpResModel> call, retrofit2.Response<SendOtpResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Intent i = new Intent(ForgotPasswordActivity.this, OtpActivity.class);
                        i.putExtra("isLoginId", Utils.Is_Forgot_Password_Id);
                        i.putExtra("mobile", strMobile);
                        i.putExtra("UserId", strUserId);
                        i.putExtra("UserName", strUserName);
                        startActivity(i);
                        finish();
                    } else {
                        Utils.showInfoDialog(ForgotPasswordActivity.this, response.body().getMessage());
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

    private boolean checkValidation() {
        if(strMobileNo == null || strMobileNo.length() == 0){
            edtMobile.setError("Please enter mobile no.");
            return false;
        }

        return true;
    }

    private void initComponent() {
        btnSubmit = findViewById(R.id.btnSubmit);
        edtMobile = findViewById(R.id.edtMobile);
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
        tvPageName.setText("Forgot Password");
    }
}
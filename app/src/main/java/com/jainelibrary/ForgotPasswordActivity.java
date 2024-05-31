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
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.ResetPasswordActivity;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.ApiResponseModel;
import com.jainelibrary.model.UserNameExistsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText edtUserName;
    String strUserName, strUserId;

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
                strUserName = edtUserName.getText().toString();
                if(checkValidation()){
                    callCheckUserNameApi();
                }
            }
        });
    }

    private void callCheckUserNameApi() {
        if (!ConnectionManager.checkInternetConnection(ForgotPasswordActivity.this)) {
            Utils.showInfoDialog(ForgotPasswordActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(ForgotPasswordActivity.this, "Please Wait...", false);
        ApiClient.checkUserNameExists(strUserName, new Callback<UserNameExistsResModel>() {
            @Override
            public void onResponse(Call<UserNameExistsResModel> call, retrofit2.Response<UserNameExistsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                       strUserId = response.body().getUser_id();
                       if(strUserId != null && strUserId.length() > 0){
                           callSendOTPApi(strUserId);
                       }
                       //Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Utils.showInfoDialog(ForgotPasswordActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserNameExistsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void callSendOTPApi(String strUserId) {
        if (!ConnectionManager.checkInternetConnection(ForgotPasswordActivity.this)) {
            Utils.showInfoDialog(ForgotPasswordActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(ForgotPasswordActivity.this, "Please Wait...", false);
        ApiClient.sendOTP(strUserId, new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, retrofit2.Response<ApiResponseModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        //Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("UserId", strUserId);
                        startActivity(intent);
                    } else {
                        Utils.showInfoDialog(ForgotPasswordActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private boolean checkValidation() {
        if(strUserName == null || strUserName.length() == 0){
            edtUserName.setError("Please enter username");
            return false;
        }

        return true;
    }

    private void initComponent() {
        btnSubmit = findViewById(R.id.btnSubmit);
        edtUserName = findViewById(R.id.edtUserName);
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
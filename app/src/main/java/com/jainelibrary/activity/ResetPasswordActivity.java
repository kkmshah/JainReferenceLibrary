package com.jainelibrary.activity;

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

import com.jainelibrary.ForgotPasswordActivity;
import com.jainelibrary.R;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.ApiResponseModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btnReset;
    EditText edtUserName, edtPWD, edtCPWD;
    String strPWD, strCPWD, strOTP, strUserId, strUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent intent = getIntent();
        strUserId = intent.getStringExtra("UserId");
        strUserName = intent.getStringExtra("UserName");

        strOTP = SharedPrefManager.getInstance(ResetPasswordActivity.this).getStringPref(SharedPrefManager.IS_CODE);

        initComponent();
        setHeader();
        declaration();
    }

    private void declaration() {
        edtUserName.setText(strUserName);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPWD = edtPWD.getText().toString();
                strCPWD = edtCPWD.getText().toString();
                if(checkValidation()){
                    callCheckUserNameApi();
                }
            }
        });
    }

    private void callCheckUserNameApi() {
        if (!ConnectionManager.checkInternetConnection(ResetPasswordActivity.this)) {
            Utils.showInfoDialog(ResetPasswordActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(ResetPasswordActivity.this, "Please Wait...", false);
        ApiClient.resetPassword(strUserId, strOTP, strCPWD, new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, retrofit2.Response<ApiResponseModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {

                        Utils.showInfoDialog(ResetPasswordActivity.this, response.body().getMessage());
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginWithPasswordActivity.class);
                        startActivity(intent);
                    } else {
                        Utils.showInfoDialog(ResetPasswordActivity.this, response.body().getMessage());
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
        if(strPWD == null || strPWD.length() == 0){
            edtPWD.setError("Please enter password");
            return false;
        }

        if(strCPWD == null || strCPWD.length() == 0){
            edtCPWD.setError("Please enter confirm password");
            return false;
        }

        if(!strCPWD.equals(strPWD)){
            Utils.showInfoDialog(ResetPasswordActivity.this, "Confirm Password and Password are not match");
            return false;
        }

        return true;
    }

    private void initComponent() {
        btnReset = findViewById(R.id.btnReset);
        edtUserName = findViewById(R.id.edtUserName);
        edtPWD = findViewById(R.id.edtPWD);
        edtCPWD = findViewById(R.id.edtCPWD);
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
        tvPageName.setText("Reset Password");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ResetPasswordActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
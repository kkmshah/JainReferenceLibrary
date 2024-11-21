package com.jainelibrary.activity;

import android.app.Dialog;
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
import com.jainelibrary.R;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.ApiResponseModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.CheckResModel;
import com.jainelibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;

public class SignupActivity extends AppCompatActivity {
    TextInputEditText edtName, edtMobile, edtPassword, edtEmail, edtUsername, edtConFirm;
    Button btnSignup;
    LinearLayout llSignIn;
    TextView signIn;
    private String strName, strEmail, strPassword, strMobile, strUsername, strConfirmPassword;
    private boolean isReferenceLogin, isKeywordSearch, isBookSearch, isHoldSearch, isMyShelf;
    private String isLoginId;
    TextView tvLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_signup);
        setHeader();
        loadUiElements();
        declration();
    }

    public void clearAllFilds() {
        edtName.getText().clear();
        edtMobile.getText().clear();
        edtEmail.getText().clear();
        edtPassword.getText().clear();
        edtUsername.getText().clear();
        edtConFirm.getText().clear();
        strUsername = "";
        strConfirmPassword = "";
        strEmail = "";
        strMobile = "";
        strName = "";
        strPassword = "";
    }

    private void declration() {

        isLoginId = getIntent().getStringExtra("isLoginId");
        signIn.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginWithPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginWithPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginWithPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName = edtName.getText().toString();
                strEmail = edtEmail.getText().toString();
                strMobile = edtMobile.getText().toString();
                strPassword = edtPassword.getText().toString();
                strConfirmPassword = edtConFirm.getText().toString();
                strUsername = edtUsername.getText().toString();
                if (strName.equalsIgnoreCase("") || strName.length() == 0) {
                    edtName.setError("Please enter name");
                    edtName.requestFocus();
                } else if (strMobile.equalsIgnoreCase("") || strMobile.length() < 10) {
                    edtMobile.setError("Please enter 10 digit mobile no");
                    edtMobile.requestFocus();
                } else if (!Utils.isValidEmail((CharSequence) strEmail)) {
                    edtEmail.setError("Please enter valid email");
                    edtEmail.requestFocus();
                } else if (strUsername.equalsIgnoreCase("") ) {
                    edtUsername.setError("Please enter UserName");
                    edtUsername.requestFocus();
                } else if (strPassword.equalsIgnoreCase("") || strPassword.length() <6) {
                    edtPassword.setError("Please enter minimum 6 digit  password");
                    edtPassword.requestFocus();
                } else if (strConfirmPassword.equalsIgnoreCase("") || strConfirmPassword.length()<  6) {
                    edtConFirm.setError("Please enter Confirm Password As Above");
                    edtConFirm.requestFocus();
                } else if (!strConfirmPassword.equalsIgnoreCase(strPassword)) {
                    edtConFirm.setError("Please enter Confirm Password As Above");
                    edtConFirm.requestFocus();
                } else {
                    checkUserName();

                }
            }
        });
    }

    private void checkUserName() {

        if (!ConnectionManager.checkInternetConnection(SignupActivity.this)) {
            Utils.showInfoDialog(SignupActivity.this, "Please check internet connection");
            return;
        }


        ApiClient.CheckUSerName(strUsername, new Callback<CheckResModel>() {
            @Override
            public void onResponse(Call<CheckResModel> call, retrofit2.Response<CheckResModel> response) {

                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        callSignUpApi();
                    } else {
                        edtUsername.setError(response.body().getMessage());
                        edtUsername.requestFocus();
                        strUsername = "";
                        edtUsername.setText("");
                        Utils.showInfoDialog(SignupActivity.this, response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<CheckResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
            }
        });
    }

    private void callSignUpApi() {
        if (!ConnectionManager.checkInternetConnection(SignupActivity.this)) {
            Utils.showInfoDialog(SignupActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(SignupActivity.this, "Please Wait...", false);
        ApiClient.signup(strName, strMobile, strEmail, strUsername, strPassword, new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, retrofit2.Response<ApiResponseModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.showInfoDialog(SignupActivity.this, response.body().getMessage());
                        final Dialog dialogView = new Dialog(SignupActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog);
                        dialogView.setContentView(R.layout.dialog_login);
                        Button dialogYes, dialogNo;
                        //  dialogYes = (Button) dialogView.findViewById(R.id.btn_yes);
                        TextView tvDescription;

                        tvDescription = (TextView) dialogView.findViewById(R.id.tvDescription);
                        tvDescription.setText(response.body().getMessage());
                        dialogNo = (Button) dialogView.findViewById(R.id.btnOK);
                        dialogView.setCancelable(false);
                        dialogView.show();
                        dialogNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clearAllFilds();
                                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                                i.putExtra("isLoginId", isLoginId);

                                startActivity(i);
                                finish();
                            }
                        });

                    } else {
                        final Dialog dialogView = new Dialog(SignupActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog);
                        dialogView.setContentView(R.layout.dialog_login);
                        Button dialogYes, dialogNo;
                        //  dialogYes = (Button) dialogView.findViewById(R.id.btn_yes);
                        TextView tvDescription;
                        tvDescription = (TextView) dialogView.findViewById(R.id.tvDescription);
                        tvDescription.setText(response.body().getMessage());
                        dialogNo = (Button) dialogView.findViewById(R.id.btnOK);
                        dialogView.setCancelable(false);
                        dialogView.show();
                        dialogNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogView.dismiss();
                                clearAllFilds();

                            }
                        });
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
        tvPageName.setText("REGISTER");
    }


    private void loadUiElements() {

        edtName = findViewById(R.id.edtName);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignup = findViewById(R.id.btnSignup);
        llSignIn = findViewById(R.id.llSignIn);
        signIn = findViewById(R.id.signIn);
        tvLogin = findViewById(R.id.tvLogin);
        edtUsername = findViewById(R.id.edtUserName);
        edtConFirm = findViewById(R.id.edtConfirmPass);

    }
}

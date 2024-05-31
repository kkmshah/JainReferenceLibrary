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
import com.jainelibrary.ForgotPasswordActivity;
import com.jainelibrary.R;
import com.jainelibrary.fragment.KeywordsMainFragment;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginWithPasswordActivity extends AppCompatActivity {
    private String TAG = "LoginWithPasswordActivity";
    Button btnLogin;
    TextInputEditText edtUsername, edtPassword;
    TextView signWithOtp, sign_up, tvForgot;
    private String strUsername, strPassword;
    String isLoginId;
    LinearLayout llSignInWithOtp, llSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_sign_in);
        setHeader();
        declaration();

    }

    private void declaration() {
        edtUsername = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPWD);
        btnLogin = findViewById(R.id.btnLogin);
        llSignup = findViewById(R.id.llSignup);
        llSignInWithOtp = findViewById(R.id.llSignInWithOtp);
        sign_up = findViewById(R.id.sign_up);
        signWithOtp = findViewById(R.id.signWithOtp);
        tvForgot = findViewById(R.id.tvForgot);
        sign_up.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        signWithOtp.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        boolean isLogin = SharedPrefManager.getInstance(LoginWithPasswordActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        isLoginId = getIntent().getStringExtra("isLoginId");
        if (isLogin) {
            Utils.showInfoDialog(LoginWithPasswordActivity.this, "Already login");
            if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Reference_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, ReferencePageActivity.class);
                i.putExtra("isLogin", true);
                setResult(RESULT_OK, i);
                finish();

            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Keyword_Search_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, KeywordsMainFragment.class);
                i.putExtra("isLogin", true);
                setResult(RESULT_OK, i);
                finish();
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Keyword_Search_Details_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, KeywordSearchDetailsActivity.class);
                i.putExtra("isLogin", true);
                setResult(RESULT_OK, i);
                finish();
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Book_Search_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, BookDetailsActivity.class);
                i.putExtra("isLogin", true);
                setResult(RESULT_OK, i);
                finish();
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Hold_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                            /*i.putExtra("isLogin", true);
                            i.putExtra("hold", true);
                            setResult(RESULT_OK, i);
                            finish();*/
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_My_Shelf_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                           /* i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();*/
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Appendix_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, AppendixActivity.class);
                i.putExtra("isLogin", true);
                setResult(RESULT_OK, i);
                finish();
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Notes_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, NotesActivity.class);
                i.putExtra("isLogin", true);
                setResult(RESULT_OK, i);
                finish();
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Common_Login)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Pdf_Store_Login_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Higlight_Id)) {
                Intent i = new Intent(LoginWithPasswordActivity.this, KeywordHighlightActivity.class);
                setResult(RESULT_OK, i);
                finish();
            } else if (isLoginId == null || isLoginId.length() == 0) {
                Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
        llSignInWithOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginWithPasswordActivity.this, LoginWithOtpActivity.class);
                i.putExtra("isLoginId", isLoginId);
                if (isLoginId != null && isLoginId.length() > 0) {
                    startActivity(i);
                    finish();
                } else {
                    startActivity(i);
                }
            }
        });
        signWithOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginWithPasswordActivity.this, LoginWithOtpActivity.class);
                i.putExtra("isLoginId", isLoginId);
                if (isLoginId != null && isLoginId.length() > 0) {
                    startActivity(i);
                    finish();
                } else {
                    startActivity(i);
                }
            }
        });
        llSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginWithPasswordActivity.this, SignupActivity.class);
                i.putExtra("isLoginId", isLoginId);
                if (isLoginId != null && isLoginId.length() > 0) {
                    startActivity(i);
                    finish();
                } else {
                    startActivity(i);
                }
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginWithPasswordActivity.this, SignupActivity.class);
                i.putExtra("isLoginId", isLoginId);
                if (isLoginId != null && isLoginId.length() > 0) {
                    startActivity(i);
                    finish();
                } else {
                    startActivity(i);
                }
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginWithPasswordActivity.this, ForgotPasswordActivity.class);
                i.putExtra("isLoginId", isLoginId);
                if (isLoginId != null && isLoginId.length() > 0) {
                    startActivity(i);
                    finish();
                } else {
                    startActivity(i);
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUsername = edtUsername.getText().toString();
                strPassword = edtPassword.getText().toString();
                if (strUsername == null || strUsername.length() == 0) {
                    Utils.showInfoDialog(LoginWithPasswordActivity.this, "Please enter valid username");
                } else if (strPassword == null || strPassword.length() == 0) {
                    Utils.showInfoDialog(LoginWithPasswordActivity.this, "Please enter valid password");
                } else {
                    CallSigninAPI();
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
        tvPageName.setText("Login");
    }

    private void CallSigninAPI() {
        if (!ConnectionManager.checkInternetConnection(LoginWithPasswordActivity.this)) {
            Utils.showInfoDialog(LoginWithPasswordActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(LoginWithPasswordActivity.this, "Please Wait...", false);
        ApiClient.login(strUsername, strPassword, new Callback<UserDetailsResModel>() {
            @Override
            public void onResponse(Call<UserDetailsResModel> call, retrofit2.Response<UserDetailsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        //  Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        SharedPrefManager.getInstance(LoginWithPasswordActivity.this).setBooleanPreference(SharedPrefManager.IS_LOGIN, true);
                        SharedPrefManager.getInstance(LoginWithPasswordActivity.this).saveStringPref(SharedPrefManager.IS_CODE, strPassword);
                        UserDetailsResModel.UserDetailsModel mUserDetailsModel = response.body().getData();
                        Log.e(TAG, "Login Id before--" + response.body().getData().getId());
                        Log.e(TAG, "Login Id before--" + response.body().getData().getId());

                        if (mUserDetailsModel != null) {
                            SharedPrefManager.getInstance(LoginWithPasswordActivity.this).saveUserDetails(mUserDetailsModel);
                            Log.e("Login Id--" ,  ""+mUserDetailsModel.getId());
                            SharedPrefManager.getInstance(LoginWithPasswordActivity.this).saveBooleanPref(SharedPrefManager.KEY_USER_IS_ADMIN, mUserDetailsModel.getIs_admin());
                            SharedPrefManager.getInstance(LoginWithPasswordActivity.this).saveStringPref(SharedPrefManager.KEY_USER_ID, mUserDetailsModel.getId());
                        }
                        Log.e(TAG, "isLoginId Id before--" + isLoginId);
                        if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Reference_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, ReferencePageActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Keyword_Search_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, KeywordsMainFragment.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Keyword_Search_Details_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, KeywordSearchDetailsActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Book_Search_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, BookDetailsActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Hold_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            /*i.putExtra("isLogin", true);
                            i.putExtra("hold", true);
                            setResult(RESULT_OK, i);
                            finish();*/
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_My_Shelf_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                           /* i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();*/
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Appendix_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, AppendixActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Notes_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, NotesActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Common_Login)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Pdf_Store_Login_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Higlight_Id)) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, KeywordHighlightActivity.class);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId == null || isLoginId.length() == 0) {
                            Intent i = new Intent(LoginWithPasswordActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Utils.showInfoDialog(LoginWithPasswordActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }

}


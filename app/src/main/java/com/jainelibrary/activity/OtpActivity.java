package com.jainelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.jainelibrary.R;
import com.jainelibrary.fragment.KeywordsMainFragment;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.SendOtpResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

public class OtpActivity extends AppCompatActivity {
    private static final String TAG = OtpActivity.class.getSimpleName();
    TextInputEditText etOtp1, etOtp2, etOtp3, etOtp4;
    String strotp1, strotp2, strotp3, strotp4;
    Button btnLogin, btnResendCode;
    private String strFinalOtp;
    private CountDownTimer countDownTimer;
    private TextView tv_coundown;
    String strMobileNo;
    private String isLoginId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        declaration();
        btnOnClickListenerEvent();
        setHeader();
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        ivBack.setVisibility(View.INVISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Verification");
    }

    private void callVerifyOtpApi(String strFinalOtp) {
        if (!ConnectionManager.checkInternetConnection(OtpActivity.this)) {
            Utils.showInfoDialog(OtpActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(OtpActivity.this, "Please Wait...", false);
        ApiClient.verifyOtp(strMobileNo, strFinalOtp, new Callback<UserDetailsResModel>() {
            @Override
            public void onResponse(Call<UserDetailsResModel> call, retrofit2.Response<UserDetailsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        //  Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        SharedPrefManager.getInstance(OtpActivity.this).setBooleanPreference(SharedPrefManager.IS_LOGIN, true);
                        SharedPrefManager.getInstance(OtpActivity.this).saveStringPref(SharedPrefManager.IS_CODE, strFinalOtp);
                        UserDetailsResModel.UserDetailsModel mUserDetailsModel = response.body().getData();
                        Log.e(TAG,"Login Id before--"+ response.body().getData().getId());

                        if (mUserDetailsModel != null) {
                            SharedPrefManager.getInstance(OtpActivity.this).saveUserDetails(mUserDetailsModel);
                            Log.e(TAG,"Login Id--"+mUserDetailsModel.getId());
                            SharedPrefManager.getInstance(OtpActivity.this).saveBooleanPref(SharedPrefManager.KEY_USER_IS_ADMIN, mUserDetailsModel.getIs_admin());
                            SharedPrefManager.getInstance(OtpActivity.this).saveStringPref(SharedPrefManager.KEY_USER_ID, mUserDetailsModel.getId());
                        }
                        if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Reference_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, ReferencePageActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();

                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Keyword_Search_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, KeywordsMainFragment.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Keyword_Search_Details_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, KeywordSearchDetailsActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Book_Search_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, BookDetailsActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Hold_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            /*i.putExtra("isLogin", true);
                            i.putExtra("hold", true);
                            setResult(RESULT_OK, i);
                            finish();*/
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_My_Shelf_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                           /* i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();*/
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Appendix_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, AppendixActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Notes_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, NotesActivity.class);
                            i.putExtra("isLogin", true);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Common_Login)) {
                            Intent i = new Intent(OtpActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Pdf_Store_Login_Id)) {
                            Intent i = new Intent(OtpActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Higlight_Id)) {
                            Intent i = new Intent(OtpActivity.this, KeywordHighlightActivity.class);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isLoginId != null && isLoginId.equalsIgnoreCase(Utils.Is_Forgot_Password_Id)) {
                            SharedPrefManager.getInstance(OtpActivity.this).setBooleanPreference(SharedPrefManager.IS_LOGIN, false);

                            Intent intent = new Intent(OtpActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("UserId", getIntent().getStringExtra("UserId"));
                            intent.putExtra("UserName", getIntent().getStringExtra("UserName"));
                            startActivity(intent);
                        }
                        else if (isLoginId == null || isLoginId.length() == 0) {
                            Intent i = new Intent(OtpActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Utils.showInfoDialog(OtpActivity.this, response.body().getMessage());
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

    private void btnOnClickListenerEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strotp1 = etOtp1.getText().toString();
                strotp2 = etOtp2.getText().toString();
                strotp3 = etOtp3.getText().toString();
                strotp4 = etOtp4.getText().toString();
                strFinalOtp = strotp1 + "" + strotp2 + "" + strotp3 + "" + strotp4;
                if (strFinalOtp != null && strFinalOtp.length() == 4) {
                    callVerifyOtpApi(strFinalOtp);
                } else {
                    Utils.showInfoDialog(OtpActivity.this, "Please enter valid otp");
                    return;
                }
            }
        });

        btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSendOtpApi(strMobileNo);
            }
        });
    }

    private void declaration() {
        etOtp1 = findViewById(R.id.etOTPChar1);
        etOtp2 = findViewById(R.id.etOTPChar2);
        etOtp3 = findViewById(R.id.etOTPChar3);
        etOtp4 = findViewById(R.id.etOTPChar4);
        tv_coundown = findViewById(R.id.tvTimer);
        btnResendCode = findViewById(R.id.resendCode);
        btnLogin = findViewById(R.id.btnLogin);
        etOtp1.addTextChangedListener(new OtpActivity.GenericTextWatcher(etOtp1));
        etOtp2.addTextChangedListener(new OtpActivity.GenericTextWatcher(etOtp2));
        etOtp3.addTextChangedListener(new OtpActivity.GenericTextWatcher(etOtp3));
        etOtp4.addTextChangedListener(new OtpActivity.GenericTextWatcher(etOtp4));

        countDownTimer();
        isLoginId = getIntent().getStringExtra("isLoginId");
        strMobileNo = getIntent().getStringExtra("mobile");

    }

    private void countDownTimer() {
        countDownTimer = new CountDownTimer(1000 * 60 * 2, 1000) {
            @Override
            public void onTick(long l) {
                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(l) % 60);
                tv_coundown.setText(text);
            }

            @Override
            public void onFinish() {
                tv_coundown.setText("00:00");
            }
        };
        countDownTimer.start();
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            switch (view.getId()) {
                case R.id.etOTPChar1:
                    if (text.length() == 1)
                        etOtp2.requestFocus();
                    else if (text == null || text.length() == 0)
                        etOtp1.requestFocus();
                    break;
                case R.id.etOTPChar2:
                    if (text.length() == 1)
                        etOtp3.requestFocus();
                    else if (text == null || text.length() == 0)
                        etOtp1.requestFocus();
                    break;
                case R.id.etOTPChar3:
                    if (text.length() == 1)
                        etOtp4.requestFocus();
                    else if (text == null || text.length() == 0)
                        etOtp2.requestFocus();
                    break;
                case R.id.etOTPChar4:
                    if (text == null || text.length() == 0)
                        etOtp3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence text, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }

    private void callSendOtpApi(String strMobile) {
        if (!ConnectionManager.checkInternetConnection(OtpActivity.this)) {
            Utils.showInfoDialog(OtpActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(OtpActivity.this, "Please Wait...", false);
        ApiClient.sendOtp(strMobile, new Callback<SendOtpResModel>() {
            @Override
            public void onResponse(Call<SendOtpResModel> call, retrofit2.Response<SendOtpResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.showInfoDialog(OtpActivity.this, response.body().getMessage());
                       /* Intent openMainActivity = new Intent(OtpActivity.this, OtpActivity.class);
                        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivityIfNeeded(openMainActivity, 0);*/
                    } else {
                        Utils.showInfoDialog(OtpActivity.this, response.body().getMessage());
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

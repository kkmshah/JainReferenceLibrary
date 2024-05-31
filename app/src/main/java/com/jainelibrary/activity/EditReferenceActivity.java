package com.jainelibrary.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jainelibrary.R;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddDataFeedbackResModel;
import com.jainelibrary.model.FeedbackTypeResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.ReferenceResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class EditReferenceActivity extends AppCompatActivity {

    Activity activity;

    int type_id, reference_id, reference, reference_type, pdf_page_no, page_no;
    String type_value;

    LinearLayout llTypeValue;
    TextView tvKeyword, tvPDFPageNo, tvPageNo;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reference);
        activity = this;
        loadUiElements();
        declarations();
        setHeader();
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
        tvPageName.setText("Edit Reference");
    }

    private void loadUiElements() {
        llTypeValue = findViewById(R.id.llTypeValue);
        tvKeyword = findViewById(R.id.tvKeyword);
        tvPDFPageNo = findViewById(R.id.tvPDFPageNo);
        tvPageNo = findViewById(R.id.tvPageNo);
        btnSave = findViewById(R.id.btnSave);
    }

    private void declarations() {
        type_id = getIntent().getIntExtra("type_id", 0);
        reference_id = getIntent().getIntExtra("reference_id", 0);
        reference = getIntent().getIntExtra("reference", 0);
        reference_type = getIntent().getIntExtra("reference_type", 0);
        type_value = getIntent().getStringExtra("type_value");
        pdf_page_no = getIntent().getIntExtra("pdf_page_no", 0);
        page_no = getIntent().getIntExtra("page_no", 0);

        if (type_id == 0)
        {
            llTypeValue.setVisibility(View.VISIBLE);
            tvKeyword.setText(type_value);
        }
        else
        {
            llTypeValue.setVisibility(View.GONE);
        }

        tvPDFPageNo.setText("" + pdf_page_no);
        tvPageNo.setText("" + page_no);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTypeValue = tvKeyword.getText().toString();
                String strPDFPageNo = tvPDFPageNo.getText().toString();
                String strPageNo = tvPageNo.getText().toString();

                if (strPDFPageNo != null && strPDFPageNo.length() > 0 && strPageNo != null && strPageNo.length() > 0) {
                    callUpdateReferenceApi(strTypeValue, strPDFPageNo, strPageNo);
                }
            }
        });
    }

    public void callUpdateReferenceApi(String strTypeValue, String strPdfPageNo, String strPageNo) {
        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check internet connection");
            return;
        }
        String strUserId = SharedPrefManager.getInstance(EditReferenceActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        Utils.showProgressDialog(activity, "Please Wait...", false);

        ApiClient.updateReference(strUserId, "" + type_id, "" + reference_id, strTypeValue, strPdfPageNo, strPageNo, new Callback<ReferenceResModel>() {
            @Override
            public void onResponse(Call<ReferenceResModel> call, retrofit2.Response<ReferenceResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Dialog dialog = new IosDialog.Builder(activity)
                                .setMessage(response.body().getMessage())
                                .setMessageColor(Color.parseColor("#1565C0"))
                                .setMessageSize(18)
                                .setPositiveButtonColor(Color.parseColor("#981010"))
                                .setPositiveButtonSize(18)
                                .setPositiveButton("OK", new IosDialog.OnClickListener() {
                                    @Override
                                    public void onClick(IosDialog dialog, View v) {
                                        dialog.dismiss();

                                        SharedPrefManager.getInstance(EditReferenceActivity.this).setBooleanPreference("ReloadRefPageData", true);
                                        SharedPrefManager.getInstance(EditReferenceActivity.this).setBooleanPreference("ReloadBookData", true);

                                        finish();
                                        onBackPressed();
                                    }
                                }).build();
                        dialog.show();
                    } else {
                        Utils.showInfoDialog(activity, "" + response.body().getMessage());
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ReferenceResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }
}
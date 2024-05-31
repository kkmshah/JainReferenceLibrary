package com.jainelibrary.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jainelibrary.R;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.model.ReferenceTypeResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.ReferenceResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AddReferenceActivity extends AppCompatActivity {

    String[] types = {"Keyword", "Shlok", "Index", "Year"};

    Activity activity;

    int reference, reference_type, pdf_page_no, page_no;
    String book_id;

    LinearLayout llType1;
    Spinner spType, spType1;
    TextView tvTypeValue, tvPDFPageNo, tvPageNo;
    Button btnSave;

    String strType = "", strTypeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reference);
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
        tvPageName.setText("Add New Reference");
    }

    private void loadUiElements() {
        spType = findViewById(R.id.spinnerType);
        spType1 = findViewById(R.id.spinnerType1);
        llType1 = findViewById(R.id.llType1);
        tvTypeValue = findViewById(R.id.tvTypeValue);
        tvPDFPageNo = findViewById(R.id.tvPDFPageNo);
        tvPageNo = findViewById(R.id.tvPageNo);
        btnSave = findViewById(R.id.btnSave);
    }

    private void declarations() {
        book_id = getIntent().getStringExtra("book_id");
        reference = getIntent().getIntExtra("reference", 0);
        reference_type = getIntent().getIntExtra("reference_type", 0);
        pdf_page_no = getIntent().getIntExtra("pdf_page_no", 0);
        page_no = getIntent().getIntExtra("page_no", 0);

        tvPDFPageNo.setText("" + pdf_page_no);
        tvPageNo.setText("" + page_no);

        ArrayAdapter arrayAdapter = new ArrayAdapter(AddReferenceActivity.this, android.R.layout.simple_spinner_item, types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(arrayAdapter);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {
                strType = String.valueOf(position);

                if (position == 0) {
                    llType1.setVisibility(View.GONE);
                }
                else {
                    llType1.setVisibility(View.VISIBLE);
                    callReferenceTypesApi(strType);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTypeValue = tvTypeValue.getText().toString();
                String strPDFPageNo = tvPDFPageNo.getText().toString();
                String strPageNo = tvPageNo.getText().toString();

                if (strPDFPageNo != null && strPDFPageNo.length() > 0 && strPageNo != null && strPageNo.length() > 0) {
                    callAddReferenceApi(strTypeValue, strPDFPageNo, strPageNo);
                }
            }
        });
    }

    private void callReferenceTypesApi(String strType) {
        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(activity, "Please Wait...", false);

        ApiClient.getReferenceTypes(strType, new Callback<ReferenceTypeResModel>() {
            @Override
            public void onResponse(Call<ReferenceTypeResModel> call, retrofit2.Response<ReferenceTypeResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        setReferenceTypesData(response.body().getData());
                    } else {
                        Utils.showInfoDialog(activity, "Invalid Type");
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ReferenceTypeResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }

    private void setReferenceTypesData(ArrayList<ReferenceTypeResModel.TypeModel> typeList)
    {
        ArrayList<String> spinnerBList = new ArrayList<>();

        if (typeList != null && typeList.size() > 0) {
            for (int i = 0; i < typeList.size(); i++) {
                String strType = typeList.get(i).getName();
                spinnerBList.add(strType);
            }
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(AddReferenceActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerBList);
        spType1.setAdapter(adp);

        spType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                strTypeId = typeList.get(position).getId();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void callAddReferenceApi(String strTypeValue, String strPdfPageNo, String strPageNo) {
        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check internet connection");
            return;
        }
        String strUserId = SharedPrefManager.getInstance(AddReferenceActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);

        Utils.showProgressDialog(activity, "Please Wait...", false);

        ApiClient.addReference(strUserId, book_id,strType, strTypeId, strTypeValue, strPdfPageNo, strPageNo, new Callback<ReferenceResModel>() {
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

                                        SharedPrefManager.getInstance(AddReferenceActivity.this).setBooleanPreference("ReloadBookData", true);

                                        finish();

                                        Intent reference_intent = new Intent(AddReferenceActivity.this, ReferencePageDetailsActivity.class);
                                        reference_intent.putExtra("type_id", response.body().getTypeId());
                                        reference_intent.putExtra("reference_id", response.body().getReferenceId());
                                        reference_intent.putExtra("reference", reference);
                                        reference_intent.putExtra("reference_type", reference_type);
                                        startActivity(reference_intent);
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
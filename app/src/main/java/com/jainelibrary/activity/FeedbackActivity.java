package com.jainelibrary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.jainelibrary.R;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddDataFeedbackResModel;
import com.jainelibrary.model.FeedbackTypeResModel;
import com.jainelibrary.model.UpdateMyShelfNotesModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.model.YearTypeResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class FeedbackActivity extends AppCompatActivity {

    Activity activity;
    private Button btnSend;
    private TextView tvPageNo;
    private Spinner spnFeedbackType;
    private EditText edtComments;
    private String strUserId, strBookName, strBookId, strType, strTypeId, strPageNo, strPdfPageNo, spnFeedbackTypeName, strLastSpiItem;
    private int spnFeedbackTypeId;
    private final ArrayList<String> feedbackTypeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        activity = this;
        loadUiElements();
        declarations();
        setHeader();
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.INVISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Send Feedback");
    }

    private void loadUiElements() {
        tvPageNo = findViewById(R.id.tvPageNo);
        spnFeedbackType = findViewById(R.id.spnFeedbackType);
        edtComments = (EditText) findViewById(R.id.edtComments);
    }

    private void declarations() {
        strBookName = getIntent().getStringExtra("strBookName");
        strBookId = getIntent().getStringExtra("strBookId");
        strType = getIntent().getStringExtra("strType");
        strTypeId = getIntent().getStringExtra("strTypeId");
        strPageNo = getIntent().getStringExtra("strPageNo");
        strPdfPageNo = getIntent().getStringExtra("strPdfPageNo");
        btnSend = (Button) findViewById(R.id.btnSend);

        tvPageNo.setText(strBookName + " [" + "Page No. " + strPageNo + "]");

        Log.e("FBATypeId", strTypeId);

        strUserId = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_USER_ID);

        getFeedbackTypesList();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strComments = edtComments.getText().toString();
                if (strUserId != null && strUserId.length() > 0) {
                    if (strComments != null && strComments.length() > 0) {
                        callAddDataFeedbackApi(strUserId, strComments);
                    } else {
                        Utils.showInfoDialog(activity, "Please enter comments");
                    }
                }
            }
        });
    }

    private void getFeedbackTypesList() {
        if (!ConnectionManager.checkInternetConnection(FeedbackActivity.this)) {
            Utils.showInfoDialog(FeedbackActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(FeedbackActivity.this, "Please Wait...", false);
        ApiClient.getFeedbackTypes(new Callback<FeedbackTypeResModel>() {
            @Override
            public void onResponse(Call<FeedbackTypeResModel> call, retrofit2.Response<FeedbackTypeResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        Log.e("FBTypeStatus", "successful");

                        ArrayList<FeedbackTypeResModel.FeedbackType> typeList = response.body().getTypes();
                        FeedbackTypeResModel.FeedbackType feedbackType = new FeedbackTypeResModel.FeedbackType();
                        feedbackType.setId(0);
                        feedbackType.setName("Select Feedback Type");
                        typeList.add(0,feedbackType);

                        if (typeList != null && typeList.size() > 0) {
                            for (int i = 0; i < typeList.size(); i++) {
                                String strType = typeList.get(i).getName();
                                feedbackTypeList.add(strType);
                            }

                            Log.e("FeedbackTypes", "--" + feedbackTypeList.size());

                            if (feedbackTypeList.size() > 0) {
                                setTypeSpinnerData(typeList);
                            }
                        }

                    } else {
                        Utils.showInfoDialog(FeedbackActivity.this, "No Feedback Types not found");
                    }


                }
            }

            @Override
            public void onFailure(Call<FeedbackTypeResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void setTypeSpinnerData(ArrayList<FeedbackTypeResModel.FeedbackType> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(FeedbackActivity.this, android.R.layout.simple_spinner_dropdown_item, feedbackTypeList);
        spnFeedbackType.setAdapter(adp);

        Log.e("FBDataAdapter", "success");

        spnFeedbackType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                if (position == 0){
                    return;
                }else{
                    spnFeedbackTypeId = typeList.get(position).getId();
                    spnFeedbackTypeName = typeList.get(position).getName();
                    strLastSpiItem = spnFeedbackType.getSelectedItem().toString();
                    Log.e("SelectedFBType", "--id " + spnFeedbackTypeId);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void callAddDataFeedbackApi(String strUserId, String comments) {
        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(activity, "Please Wait...", false);

        ApiClient.addDataFeedback(strUserId, strBookId, strPageNo, strPdfPageNo, strType, strTypeId, spnFeedbackTypeId, comments, new Callback<AddDataFeedbackResModel>() {
            @Override
            public void onResponse(Call<AddDataFeedbackResModel> call, retrofit2.Response<AddDataFeedbackResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

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
                                        onBackPressed();
                                    }
                                }).build();
                        dialog.show();
                        Log.e("error--", "statusTrue--" + response.body().getMessage());
                    } else {
                        Utils.showInfoDialog(activity, "" + response.body().getMessage());
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<AddDataFeedbackResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }
}
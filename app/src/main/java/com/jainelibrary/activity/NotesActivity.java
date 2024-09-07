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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UpdateMyShelfNotesModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import retrofit2.Call;
import retrofit2.Callback;

public class NotesActivity extends AppCompatActivity {

    Activity activity;
    private static final int NOTES_CODE = 1;
    private TextView tvFileName, tvNotesBy;
    private Button btnSave;
    private String strNameA, strMyNotes;
    private EditText edtNotes;
    private String strKeywordId, strKeyword, strTypeRef, strType;
    boolean isMyShelf = false;
    private LinearLayout llShare, llDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
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
        if (isMyShelf) {
            tvPageName.setText("My Notes");
        } else {
            tvPageName.setText("Add Notes");
        }
    }

    private void loadUiElements() {
        tvFileName = (TextView) findViewById(R.id.tvFileName);
        tvNotesBy = (TextView) findViewById(R.id.tvNotesBy);
        edtNotes = (EditText) findViewById(R.id.edtNotes);
        llShare = findViewById(R.id.llShare);
        llDownload = findViewById(R.id.llDownload);
    }

    private void declarations() {

        strType = getIntent().getStringExtra("strType");
        strTypeRef = getIntent().getStringExtra("strTypeRef");
        strMyNotes = getIntent().getStringExtra("strNotes");
        strKeyword = getIntent().getStringExtra("strKeyword");
        strKeywordId = getIntent().getStringExtra("strKeywordId");
        isMyShelf = getIntent().getBooleanExtra("isMyShelf", false);
        Log.e("id",""+ strKeywordId);
        Log.e("id","strKeyword : "+ strKeyword);
        Gson gson = new Gson();
        String strUserId = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_USER_ID);
        String strUserDetails = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);

        if (userDetailsModel != null) {
            strNameA = userDetailsModel.getName();
        }
        
        tvNotesBy = (TextView) findViewById(R.id.tvNotesBy);

        if (strNameA != null && strNameA.length() > 0) {
            tvNotesBy.setText(": " + strNameA);

        }
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strMyNotes = edtNotes.getText().toString();
                if (strUserId != null && strUserId.length() > 0) {
                    if (strMyNotes != null && strMyNotes.length() > 0) {
                        callUpdateNotesApi(strUserId);
                    } else {
                        Utils.showInfoDialog(activity, "Please enter notes");
                    }
                } else {
                    askLogin();
                }
            }
        });


        if (strMyNotes != null && strMyNotes.length() > 0) {
            edtNotes.setText(strMyNotes);
        }

        if (strKeyword != null && strKeyword.length() > 0) {
            tvFileName.setText(": " + strKeyword);
        }

    }

    public void callUpdateNotesApi(String strUserId) {
        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(activity, "Please Wait...", false);

        ApiClient.updateMyNotes(strKeywordId, strMyNotes, new Callback<UpdateMyShelfNotesModel>() {
            @Override
            public void onResponse(Call<UpdateMyShelfNotesModel> call, retrofit2.Response<UpdateMyShelfNotesModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Log.e("error--", "statusTrue--" + response.body().getMessage());
//                        if (isMyShelf){
//                            Intent i = new Intent(NotesActivity.this, MyReferenceActivity.class);
//                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(i);
//                        }else{
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
//                        }
                    } else {
                        Utils.showInfoDialog(activity, "" + response.body().getMessage());
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<UpdateMyShelfNotesModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }


    private void askLogin() {
        Utils.showLoginDialogForResult(NotesActivity.this, NOTES_CODE);
    }


}
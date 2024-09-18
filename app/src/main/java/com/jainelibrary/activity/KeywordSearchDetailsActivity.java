package com.jainelibrary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.BuildConfig;
import com.jainelibrary.Constantss;
import com.jainelibrary.R;
import com.jainelibrary.adapter.KeywordSearchListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.CheckMyShelfFileNameResModel;
import com.jainelibrary.retrofitResModel.CreatePdfFileUrlResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jainelibrary.utils.Utils.REF_TYPE_REFERENCE_PAGE;
import static com.jainelibrary.utils.Utils.TYPE_KEYWORD_PAGE;

public class KeywordSearchDetailsActivity extends AppCompatActivity implements KeywordSearchListAdapter.SearchInterfaceListener {

    private RecyclerView rvList;
    private KeywordSearchListAdapter mSearchAdapter;
    private String strKeyName;
    private String strKeyId,strBookIds, strPageNo, strPdf;
    private LinearLayout llExport;
    private BottomSheetDialog bottomSheetDialog;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private static final int REFERENCE_CODE = 1;
    private static final String TAG = KeywordSearchDetailsActivity.class.getSimpleName();
    private ArrayList<BookListResModel.BookDetailsModel> mBookList = new ArrayList<>();
    private String PackageName;
    private TextView tvHeaderCount, tvKeywordCount;
    private ImageView ivHeaderIcon;
    private LinearLayout llAddItem, llKeywordCount;
    private String strUsername;
    private ArrayList<BookListResModel.BookDetailsModel> mReferenceBookList = new ArrayList<>();
    private ArrayList<BookListResModel.BookDetailsModel.BookPageModel> pageModels = new ArrayList<>();
    ArrayList<String> strpageno = new ArrayList<>();
    ArrayList<String> strpagenodetail = new ArrayList<>();
    private String strUID;
    int pagecount;
    CountDownTimer timer;
    String strEdtRenamefile = null, strUserId, shareText, strTypeRef;
    Activity activity;
    private String strPdfFile = null;
    private String strTotalCount = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_search_details);
        PackageName = KeywordSearchDetailsActivity.this.getPackageName();
        Log.e(TAG, "ABC");
        setHeader();
        loadUiElements();
        declaration();
        onClicKlistener();
    }

    private void onClicKlistener() {

        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(KeywordSearchDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    getShareDialog(strKeyId, strBookIds);
                  //  callBookDetailsPdfApi(strKeyId,strBookIds);
                } else {
                    askLogin();
                }

              /*  boolean isLogin = SharedPrefManager.getInstance(KeywordSearchDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    if (mBookList != null && mBookList.size() > 0) {
                        List<String> mReferenceStringList = new ArrayList<>();
                        if (mReferenceBookList != null && mReferenceBookList.size() > 0) {
                            for (int i = 0; i < mReferenceBookList.size(); i++) {
                                String strName = getReferenceName(mReferenceBookList.get(i));
                                if (strName != null && strName.length() > 0) {
                                    mReferenceStringList.add(strName);
                                }
                            }
                            if (!mediaStorageKeyWordRefDir.exists()) {
                                mediaStorageKeyWordRefDir.mkdirs();
                            }
                            if (mReferenceStringList != null && mReferenceStringList.size() > 0) {
                                getShareDialog(mReferenceStringList);
                            }
                        }
                    } else {
                        //InfoDialog("Please search any keywords");
                    }
                } else {
                    askLogin();
                }*/
            }
        });
    }

    private void declaration() {

        /*if (getIntent() != null) {
            strTotalCount = getIntent().getStringExtra("totalKeywordCount");
        }*/
        Log.e(TAG, "strTotalCount : " + strTotalCount);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(KeywordSearchDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);

        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
            strUID = userDetailsModel.getId();
        }
    }

    private void loadUiElements() {Constantss.ISTIMERFINISH = false;
        rvList = findViewById(R.id.rvList);
        llKeywordCount = findViewById(R.id.llKeywordCount);
        tvKeywordCount = findViewById(R.id.tvKeywordCount);
        llExport = (LinearLayout) findViewById(R.id.llExport);
        //loadData();
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        llAddItem = headerView.findViewById(R.id.llAddItem);
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        ivHeaderIcon.setVisibility(View.INVISIBLE);
        tvHeaderCount.setVisibility(View.INVISIBLE);
      /*  ivHeaderIcon.setVisibility(View.VISIBLE);
        tvHeaderCount.setVisibility(View.VISIBLE);*/
        ivHeaderIcon.setImageResource(R.mipmap.book);
        llAddItem.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            strKeyName = intent.getExtras().getString("keywordName");
            strKeyId = intent.getExtras().getString("keywordId");
            strBookIds = intent.getExtras().getString("bookIds");
            tvPageName.setText(strKeyName);
        }
        if (strKeyId != null && strKeyId.length() > 0) {
            callKeywordBookApi();
        }
    }

    private String getReferenceName(BookListResModel.BookDetailsModel bookDetailsModel) {
        String strReferenceName = "";
        String strBookname = bookDetailsModel.getBook_name();
        String strPageNo = bookDetailsModel.getPage_no();
        String strPdfPageNo = bookDetailsModel.getPdf_page_no();
        String strAutherName = bookDetailsModel.getAuthor_name();
        String strTranslator = bookDetailsModel.getTranslator();
        String strEditorName = bookDetailsModel.getEditor_name();
        pageModels = bookDetailsModel.getPageModels();
        if (strBookname != null && strBookname.length() > 0) {
            strReferenceName = strBookname;
        } else {
            strBookname = "";
        }
        if (strPageNo != null && strPageNo.length() > 0) {
            strPageNo = ", " + "P. " + strPageNo;
            strReferenceName = strBookname + strPageNo;
        } else {
            strPageNo = "";
            if (pageModels.size() != 0)
                for (int i = 0; i < pageModels.size(); i++) {
                    String page = strPageNo + "Page " + pageModels.get(i).getPage_no() + " , ";
                    strPageNo = strpageno + page;
                }
            strReferenceName = strBookname + strPageNo;
        }
        if (strAutherName != null && strAutherName.length() > 0) {
            strAutherName = ", " + strAutherName /*+ "[" + "ले" + "]"*/;
            strReferenceName = strBookname + strPageNo + strAutherName;
        } else {
            strAutherName = "";
        }
        if (strTranslator != null && strTranslator.length() > 0) {
            strTranslator = ", " + strTranslator/* + "[" + "अनु" + "]"*/;
            strReferenceName = strBookname + strPageNo + strAutherName + strTranslator;
        } else {
            strTranslator = "";
        }
        if (strEditorName != null && strEditorName.length() > 0) {
            strEditorName = ", " + strEditorName /*+ "[" + "संपा" + "]"*/;
            strReferenceName = strBookname + strPageNo + strAutherName + strTranslator + strEditorName;
        } else {
            strEditorName = "";
        }
        return strReferenceName;
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(KeywordSearchDetailsActivity.this, REFERENCE_CODE);
    }

    private void InfoDialog(String strMessage) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(KeywordSearchDetailsActivity.this);
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", dialogClickListener)
                .show();

    }

    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(KeywordSearchDetailsActivity.this, "Please Wait...", false);

        ApiClient.downloadMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {
                        /*strUserId = SharedPrefManager.getInstance(ReferencePageActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        if (strUserId != null && strUserId.length() > 0) {
                            callListHoldSearchKeyword(strUserId);
                        }*/
                        /*if (strPdfFile != null && strPdfFile.length() > 0) {
                            File mFile = new File(strPdfFile);
                            if (mFile.exists()) {
                                Toast.makeText(KeywordSearchDetailsActivity.this, "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("download--", "file not exits" + strPdfFile);
                            }
                        }*/
                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            Utils.downloadLocalPDF(strPdfFile, KeywordSearchDetailsActivity.this);
                        }
                        else {
                            Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "Pdf not found");
                        }
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : "+throwable.getMessage());
            }
        });
    }

    private void callShareMyShelfsApi(String strUserId, String shareText, String strPdfLink, String strPdfImage) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(KeywordSearchDetailsActivity.this, "Please Wait...", false);

        ApiClient.shareMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {
                        /*strUserId = SharedPrefManager.getInstance(ReferencePageActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        if (strUserId != null && strUserId.length() > 0) {
                            callListHoldSearchKeyword(strUserId);
                        }*/

//                        if (strPdfFile != null && strPdfFile.length() > 0) {
//                            File mFile = new File(strPdfFile);
//                            if (mFile.exists()) {
//                                share(shareText, strPdfFile);
//                            } else {
//                                Log.e("share--", "file not exits" + strPdfFile);
//                            }
//                        }

                        try {
                            if (strPdfLink != null && strPdfLink.length() > 0) {
                               // String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                                String strMessage = strPdfLink; //" " + strBookName;

                                Utils.shareContentWithImage(KeywordSearchDetailsActivity.this, "JRL Keyword Book(s) PDF", strMessage, strPdfImage);
//                                Intent intent = new Intent();
//                                intent.setAction(Intent.ACTION_SEND);
//                                intent.setType("text/plain");
//                                intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
//                                intent.putExtra(Intent.EXTRA_TEXT, strMessage);
//                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                startActivity(Intent.createChooser(intent, shareData));
                            }
                        } catch (Exception e) {
                            Log.e("Exception Error", "Error---" + e.getMessage());
                        }

                     //   Toast.makeText(KeywordSearchDetailsActivity.this, "" + response.body().isStatus(), Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : "+throwable.getMessage());
            }
        });
    }

    public void getInfoDialogs(String strNotes, String strType, String strTypeRef, String strTite, String strKeywordId) {
        Dialog dialog = new IosDialog.Builder(this)
                .setMessage(strTite)
                .setMessageColor(Color.parseColor("#1565C0"))
                .setMessageSize(18)
                .setNegativeButtonColor(Color.parseColor("#981010"))
                .setNegativeButtonSize(18)
                .setNegativeButton("No", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButtonColor(Color.parseColor("#981010"))
                .setPositiveButtonSize(18)
                .setPositiveButton("Yes", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(KeywordSearchDetailsActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strKeyName);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }

    public void getShareDialog(String strKeyId, String strBookIds) {
        List<String> mReferenceStringList = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(KeywordSearchDetailsActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetDialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_share, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        LinearLayout bottomSheetContainer = bottomSheetDialogView.findViewById(R.id.bottomSheetContainer);
        Button btnShare = bottomSheetDialogView.findViewById(R.id.btnShare);
        Button btnMyShelf = bottomSheetDialogView.findViewById(R.id.btnMyShelf);
        Button btnDownload = bottomSheetDialogView.findViewById(R.id.btnDownload);
        EditText edtRenameFile = bottomSheetDialogView.findViewById(R.id.edtRenameFile);
        ImageView ivClose = bottomSheetDialogView.findViewById(R.id.ivClose);
        LinearLayout llRename = bottomSheetDialogView.findViewById(R.id.llRename);
        LinearLayout llTotal = bottomSheetDialogView.findViewById(R.id.llTotal);
        llTotal.setVisibility(View.GONE);
        CustomKeyboardView mKeyboardView = bottomSheetDialogView.findViewById(R.id.keyboardView);
        String strLanguage = SharedPrefManager.getInstance(KeywordSearchDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        strUID = SharedPrefManager.getInstance(KeywordSearchDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        edtRenameFile.setFocusable(true);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        String strEdtRenamefile = pagecount + "_Reference_Addresses_For_Keyword " + "'" + strKeyName + "'";
        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(KeywordSearchDetailsActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(KeywordSearchDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, KeywordSearchDetailsActivity.this, strLanguage, bottomSheetDialog, null);
//                return false;
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomSheetDialog.cancel();
//                String strEdtRenamefile = edtRenameFile.getText().toString();
//                String strNewPDFile = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strEdtRenamefile + ".pdf";
//                new File(strPdfFile).renameTo(new File(strNewPDFile));
//
//                shareText = strEdtRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
//                Constantss.FILE_NAME = strEdtRenamefile;
//                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strKeyName + "_" + strPageNo + "_" + mReferenceBookList.size() + " /";
//                callShareMyShelfsApi(strUID, shareText, strNewPDFile);
                //strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);

                if ( Integer.valueOf(strTotalCount) > 0) {
                    saveKeywordDetailsFile( strKeyId, strBookIds, strUID, strEdtRenamefile, strTotalCount, true);
                }

//                if (strPdfFile != null && strPdfFile.length() > 0) {
//                    File mFile = new File(strPdfFile);
//                    if (mFile.exists()) {
//                        callAddMyShelfApi(strUID ,strEdtRenamefile, true);
//                    } else {
//                        Log.e("download--", "file not exits" + strPdfFile);
//                    }
//                }

            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strNewPDFile = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strEdtRenamefile + ".pdf";
                new File(strPdfFile).renameTo(new File(strNewPDFile));

                Constantss.FILE_NAME = strEdtRenamefile;
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strKeyName + "_" + strPageNo + "_" + mReferenceBookList.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strKeyName + "_" + strPageNo + "_" + mReferenceBookList.size() + " /";
                callDownloadMyShelfsApi(strUID, strNewPDFile);
               // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);

            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*bottomSheetDialog.cancel();
               JSONArray jsonArray = new JSONArray();
               for (int i = 0; i < mBookList.size(); i++) {
                    for (int j = 0; j < mBookList.get(i).getPageModels().size(); j++) {
                        try {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("bid", mBookList.get(i).getBook_id());

                            jsonObject.put("book_name", mBookList.get(i).getBook_name());
                            jsonObject.put("page_no", mBookList.get(i).getPageModels().get(j).getPage_no());
                            jsonObject.put("url",mReferenceBookList.get(i).getBook_url());
                            Log.e("JSON", jsonArray.toString());
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("JSON", e.getMessage());
                        }
                    }
                }
                */


                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
               // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if ( Integer.valueOf(strTotalCount) > 0) {
                    saveKeywordDetailsFile(strKeyId, strBookIds, strUID, strEdtRenamefile, strTotalCount, false);
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        bottomSheetDialog.show();
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) bottomSheetDialogView.getParent());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet,
                                       @BottomSheetBehavior.State int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss();
                } else {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        if (mKeyboardView.getVisibility() == View.VISIBLE) {
                            mKeyboardView.setVisibility(View.GONE);
                        } else {
                            bottomSheetDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void sharePDFFiles(String strEdtRenamefile) {
        try {
            List<String> mKeywordStringList = new ArrayList<>();
            if (mBookList != null && mBookList.size() > 0) {
                for (int i = 0; i < mBookList.size(); i++) {
                    String strImagePath = mBookList.get(i).getBook_name();
                    if (strImagePath != null && strImagePath.length() > 0) {
                        mKeywordStringList.add(strImagePath);
                    }
                }

                String strPdfFile = null;
                if (mKeywordStringList != null && mKeywordStringList.size() > 0) {
                    Constantss.FILE_NAME = strEdtRenamefile;
                    Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strKeyName + "_" + strPageNo + " /";
                    strPdfFile = PdfCreator.createTextPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), Constantss.FILE_NAME, mKeywordStringList);
                }
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    Uri fileUri = Uri.fromFile(new File(strPdfFile));
                    String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    intent.setType("application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, shareData));
                }
            } else {
                Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "No book data found");
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
    }

    private void setBookData(ArrayList<BookListResModel.BookDetailsModel> mBookDataList) {
        if (mBookDataList == null || mBookDataList.size() == 0) {
            return;
        }
        mReferenceBookList = mBookDataList;
      /*  if (mBookDataList != null && mBookDataList.size() > 0) {
            ivHeaderIcon.setVisibility(View.VISIBLE);
            tvHeaderCount.setVisibility(View.VISIBLE);
            tvHeaderCount.setText("" + mBookDataList.size());
        } else {
            llAddItem.setVisibility(View.GONE);
        }*/
        if (mBookDataList != null && mBookDataList.size() > 0) {
            tvKeywordCount.setVisibility(View.VISIBLE);
            llKeywordCount.setVisibility(View.VISIBLE);
            for (int i = 0; i < mBookDataList.size(); i++) {
                pageModels = new ArrayList<>();
                pageModels = mBookDataList.get(i).getPageModels();
                pagecount = pagecount + pageModels.size();
            }
            strTotalCount = String.valueOf(pagecount);
            tvKeywordCount.setText("" + pagecount + " Reference Address");
        } else {
            llKeywordCount.setVisibility(View.GONE);
        }
        mBookList = mBookDataList;
        ///      strpageno = new ArrayList<>();
        // strpagenodetail = new ArrayList<>();
        Log.e(strpageno + "", strpageno.size() + "");
        Log.e(strpagenodetail + "", strpagenodetail.size() + "");
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(KeywordSearchDetailsActivity.this));
        rvList.setVisibility(View.VISIBLE);
        mSearchAdapter = new KeywordSearchListAdapter(KeywordSearchDetailsActivity.this, mBookDataList, this);
        rvList.setAdapter(mSearchAdapter);
        if (mBookDataList.size() == 1)
            if (mBookDataList.get(0).getPageModels().size() == 1) {
                timer = new CountDownTimer(3000, 2000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if (Constantss.ISREFERENCE) {
                            if (Constantss.ISTIMERFINISH) {
                                Constantss.ISREFERENCE = false;
                                Intent i = new Intent(KeywordSearchDetailsActivity.this, ReferencePageActivity.class);
                                i.putExtra("strTotalCount", strTotalCount);
                                i.putExtra("pageno", mBookDataList.get(0).getPageModels().get(0).getPage_no());
                                i.putExtra("pdfpage", mBookDataList.get(0).getPageModels().get(0).getPdf_page_no());
                                i.putExtra("bookid", mBookDataList.get(0).getBook_id());
                                String strKeyword = mBookDataList.get(0).getKeyword();
                                i.putExtra("keywordid", mBookDataList.get(0).getKeyword());
                                Log.e("Keyword", "Keyword: " + strKeyword);
                                i.putExtra("pagemodel", mBookDataList.get(0).getPageModels());
                                i.putExtra("moduleNo", TYPE_KEYWORD_PAGE);
                                i.putExtra("model", mBookDataList.get(0));
                                // i.putExtra("page_no", strPageNo);
                                startActivity(i);
                            }
                        }

                    }
                }.start();
            }
    }

    public void callKeywordBookApi() {
        if (!ConnectionManager.checkInternetConnection(KeywordSearchDetailsActivity.this)) {
            Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "Please check internet connection");
            return;
        }
        strUID = SharedPrefManager.getInstance(KeywordSearchDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        Log.e("UserId --->>",""+strUID);
        if(strUID == null){
            strUID = "0";
        }

        /*if(strBookIds == null){
            strBookIds = "0";
        }*/
        Utils.showProgressDialog(KeywordSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.getKeywordBookDetails(strUID, strKeyId,strBookIds, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<BookListResModel.BookDetailsModel> bookDetailsModels = new ArrayList<>();
                        bookDetailsModels = response.body().getData();
                        if (bookDetailsModels != null && bookDetailsModels.size() > 0) {
                            setBookData(bookDetailsModels);
                        }
                    } else {
                        tvKeywordCount.setVisibility(View.VISIBLE);
                        llKeywordCount.setVisibility(View.VISIBLE);
                        tvKeywordCount.setText("Reference address not found");
                        Utils.showInfoDialog(KeywordSearchDetailsActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BookListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onDetailsClick(View view, BookListResModel.BookDetailsModel mBookDetails,
                               int position) {
        String s = view.getTag().toString();
        String[] str = s.split(",");
        String str1 = str[0];
        Log.e("str1", str1);
        String str2 = str[1];
        Log.e("str2", str2);
        String str3 = str[2];
        Log.e("str3", str3);
        String str4 = str[3];
        Log.e("str4", str4);
        Log.e("stringAll", str.toString());
        Log.e("strKeyId", strKeyId);
        mBookDetails.setKeywordId(strKeyId);

        if (Constantss.ISREFERENCE) {
            Constantss.ISREFERENCE = false;
            Intent i = new Intent(KeywordSearchDetailsActivity.this, ReferencePageActivity.class);
            i.putExtra("strTotalCount", strTotalCount);
            i.putExtra("pageno", str[3]);
            i.putExtra("pdfpage", str[0]);
            i.putExtra("bookid", str[1]);
            i.putExtra("keywordid", str[2]);
            i.putExtra("pagemodel", mBookDetails.getPageModels());
            i.putExtra("moduleNo", TYPE_KEYWORD_PAGE);
            i.putExtra("model", mBookDetails);
            i.putExtra("type_id", "0");
            String strKeyword = strKeyName;
            i.putExtra("keyword", strKeyName);
            Log.e("Keyword", "Keyword: " + strKeyName);
            startActivity(i);
        }
    }


    @Override
    public void onBookImageClick(View view, BookListResModel.BookDetailsModel mBookDetails,
                               int position) {
        String strImageUrl = mBookDetails.getBook_large_image();
        String fallbackImage = mBookDetails.getBook_image();

        Log.e("strImageUrl--", "index--" + strImageUrl);
        Intent i = new Intent(this, ZoomImageActivity.class);
        i.putExtra("image", strImageUrl);
        i.putExtra("fallbackImage", fallbackImage);

        i.putExtra("url", true);
        startActivity(i);
    }

    public void share(String shareData, String mFilePath) {
        File mFile = new File(mFilePath);
        Uri fileUri = Uri.fromFile(mFile);//  Uri fileUri = FileProvider.getUriForFile(KeywordSearchDetailsActivity.this, KeywordSearchDetailsActivity.this.getApplicationContext().getPackageName() + ".provider", mFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            fileUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(mFilePath));
        }
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType(URLConnection.guessContentTypeFromName(mFile.getName()));
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, shareData);
        intentShareFile.putExtra(Intent.EXTRA_TEXT, shareData);
        startActivity(Intent.createChooser(intentShareFile, shareData));
    }

    private void saveKeywordDetailsFile( String strKeyId, String strBookIds, String strUId, String strEdtRenamefile, String totalKeywordCount, boolean isShare) {
        if (!ConnectionManager.checkInternetConnection(KeywordSearchDetailsActivity.this)) {
            Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(KeywordSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.checkMyShelfFileName(strUId, strEdtRenamefile, new Callback<CheckMyShelfFileNameResModel>() {
            @Override
            public void onResponse(Call<CheckMyShelfFileNameResModel> call, retrofit2.Response<CheckMyShelfFileNameResModel> response) {
                if (!response.isSuccessful()  ) {
                    Utils.dismissProgressDialog();
                    Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "Please try again!");
                    return;
                }

                if(!response.body().isStatus()) {
                    Utils.dismissProgressDialog();

                    Dialog dialog = new IosDialog.Builder(KeywordSearchDetailsActivity.this)
                            .setMessage(response.body().getMessage())
                            .setMessageColor(Color.parseColor("#1565C0"))
                            .setMessageSize(18)
                            .setNegativeButtonColor(Color.parseColor("#981010"))
                            .setNegativeButtonSize(18)
                            .setNegativeButton("OK", new IosDialog.OnClickListener() {
                                @Override
                                public void onClick(IosDialog dialog, View v) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButtonColor(Color.parseColor("#981010"))
                            .setPositiveButtonSize(18)
                            .setPositiveButton("Save Again", new IosDialog.OnClickListener() {
                                @Override
                                public void onClick(IosDialog dialog, View v) {
                                    dialog.dismiss();
                                    callCreateKeyboardBookDetailsPdf(strKeyId, strBookIds, strUId, strEdtRenamefile, totalKeywordCount, isShare);
                                }
                            }).build();

                    dialog.show();
                }
                else
                {
                    callCreateKeyboardBookDetailsPdf(strKeyId, strBookIds, strUId, strEdtRenamefile, totalKeywordCount, isShare);
                }

                Log.e("responseData Req", strKeyId + "" +strBookIds);

            }
            @Override
            public void onFailure(Call<CheckMyShelfFileNameResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });

    }

    private void callCreateKeyboardBookDetailsPdf(String strKeyId, String strBookIds, String strUId, String strEdtRenamefile, String totalKeywordCount, boolean isShare)
    {
        Utils.showProgressDialog(KeywordSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.createKeywordBookDetailsPdf( strKeyId, strBookIds, new Callback<CreatePdfFileUrlResModel>() {
            @Override
            public void onResponse(Call<CreatePdfFileUrlResModel> call, retrofit2.Response<CreatePdfFileUrlResModel> response) {
                Utils.dismissProgressDialog();
                Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        String strTmpPdfUrl = response.body().getPdf_url();
                        if (strTmpPdfUrl != null && strTmpPdfUrl.length() > 0) {
                            callAddMyShelfApi(strTmpPdfUrl, strUId, strEdtRenamefile, totalKeywordCount, isShare);
                        } else {
                            Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "KeywordData not saved");
                        }
                    }else {
                        Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "KeywordData not saved");
                    }

                } else {
                    Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "KeywordData not saved");
                }
            }

            @Override
            public void onFailure(Call<CreatePdfFileUrlResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void callAddMyShelfApi(String fileUrl, String strUId, String strEdtRenamefile, String totalKeywordCount, boolean isShare) {
        String type =  "0";
        String typeref = REF_TYPE_REFERENCE_PAGE;
        String strFileType = "3";
        Log.e("fileType :", " "+strFileType);

        Utils.showProgressDialog(KeywordSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfsWithUrl(strUId, null, strKeyId, type, typeref, strEdtRenamefile, null, totalKeywordCount, strFileType, fileUrl, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        String strPdfLink = response.body().getPdf_url();
                        String strPdfImage = response.body().getPdf_image();
                        if (isShare) {
                            callShareMyShelfsApi(strUId, shareText, strPdfLink, strPdfImage);
                        }
                        else {
                            Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "Keywords Added In My Reference.");
                        }
                    } else {
                        Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(KeywordSearchDetailsActivity.this, "Something went wrong please try again later");
            }
        });
    }


    public String downloadFile(ResponseBody body) {
        try {
            Log.d("downloadFile", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            String strEdtRenamefile =  strKeyName;

            String strFilePath = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strEdtRenamefile + ".pdf";
            try {
                in = body.byteStream();
                out = new FileOutputStream(strFilePath);
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } catch (IOException e) {
                Log.d("DownloadPdf", e.toString());
                return "";
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            return strFilePath;

        } catch (IOException e) {
            Log.d("DownloadPdf", e.toString());
            return "";
        }
    }

}

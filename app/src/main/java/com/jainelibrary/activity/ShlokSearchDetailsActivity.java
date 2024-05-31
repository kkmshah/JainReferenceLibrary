package com.jainelibrary.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.BuildConfig;
import com.jainelibrary.Constantss;
import com.jainelibrary.R;
import com.jainelibrary.adapter.ShlokSearchDetailsListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.GranthSlokResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.retrofitResModel.ShlokGranthSutraResModel;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.paginate.Paginate;
import com.wc.widget.dialog.IosDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jainelibrary.utils.Utils.TYPE_SHLOK_PAGE;

public class ShlokSearchDetailsActivity extends AppCompatActivity implements ShlokSearchDetailsListAdapter.SearchInterfaceListener {

    private static final String TAG = ShlokSearchDetailsActivity.class.getSimpleName();
    private RecyclerView rvList;
    private ArrayList<GranthSlokResModel> mGranthDataList = new ArrayList<>();
    private ShlokSearchDetailsListAdapter mSearchAdapter;
    private int selected = 1;
    String strShlokGranthId;
    ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> bookDetailsModels = new ArrayList<>();
    private LinearLayout llExport;
    TextView tvNoRecord;
    private LinearLayout llKeywordCount;
    private TextView tvHeaderCount, tvKeywordCount;
    private ImageView ivHeaderIcon;
    NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;
    private int page = 1;
    private boolean loading = false;
    private Paginate paginate;
    private int totalPages = 200;
    protected boolean customLoadingListItem = false;
    private boolean isLoading = false;
    private boolean isFirstTimeCall = false;
    private String strUsername,strUID, strPdfLink;
    private String PackageName;
    private ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> mReferenceBookList;
    String strEdtRenamefile = null, strUserId, shareText, strPdfFile, strTypeRef;
    String strGSID, strShlokGranthName, getStrShlokGranthId;
    private EditText etSearchView;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend, ivShare;
    LinearLayout llFilter, llClose;
    private String strLanguage;
    private int total_pages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shlok_search_details);
        PackageName = ShlokSearchDetailsActivity.this.getPackageName();  Log.e(TAG,"ABC");
        loadUiElements();
        setHeader();
        declaration();
        OnclickListeners();
    }

    private void OnclickListeners() {
        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(ShlokSearchDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    /*if (bookDetailsModels != null && bookDetailsModels.size() > 0) {
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
                    }*/
                    callShlokDetailsPdfApi(strGSID);
                } else {
                    askLogin();
                }
            }
        });

        etSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strLanguage = SharedPrefManager.getInstance(ShlokSearchDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, ShlokSearchDetailsActivity.this, strLanguage, null,ivSend);
//                buttonFilter.setVisibility(View.GONE);
//                ll_buttons.setVisibility(View.GONE);
            }
        });

        etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                buttonFilter.setVisibility(View.GONE);
//                ll_buttons.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                buttonFilter.setVisibility(View.GONE);
//                ll_buttons.setVisibility(View.GONE);
                String strSearchtext = editable.toString();
                if (strSearchtext == null || strSearchtext.length() == 0) {
                    llFilter.setVisibility(View.VISIBLE);
                    llClose.setVisibility(View.GONE);
                    return;
                } else {
                    ivSend.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_voilet)));
                    llFilter.setVisibility(View.GONE);
                    llClose.setVisibility(View.VISIBLE);
                    ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            etSearchView.getText().clear();

                            page = 1;
                            isFirstTimeCall = true;

                            bookDetailsModels.clear();
                            callShlokSutraApi("");
                            /*rvList.setVisibility(View.GONE);
                            llKeywordCount.setVisibility(View.GONE);
                            llFilter.setVisibility(View.VISIBLE);
                            llClose.setVisibility(View.GONE);*/
                            /*ivHeaderIcon.setVisibility(View.INVISIBLE);
                            tvHeaderCount.setVisibility(View.INVISIBLE);*/
                        }
                    });

                    if (strSearchtext != null && strSearchtext.length() > 0) {
                        llClose.setVisibility(View.VISIBLE);
                        llFilter.setVisibility(View.GONE);
                        /* filter(strSearchtext);*/
                    } else {
                        llClose.setVisibility(View.GONE);
                        llFilter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strValue = etSearchView.getText().toString();

                if (strValue != null && strValue.length() > 0) {
                    if (mKeyboardView.getVisibility() == View.VISIBLE) {
                        mKeyboardView.setVisibility(View.GONE);
                    }

                    page = 1;
                    isFirstTimeCall = true;

                    bookDetailsModels.clear();
                    callShlokSutraApi(strValue);

                    /*buttonFilter.setVisibility(View.VISIBLE);
                    ll_buttons.setVisibility(View.VISIBLE);
                    Log.e("strUid3", strUid);
                    if (isLogin) {
                        if ((strUid != null) && (strUid.length() != 0)) {
                            callShlokKeywordSearchApi(strUid, strValue, "1");
                        }
                    } else {

                        callShlokKeywordSearchApi("0", strValue, "1");
                    }*/

                } else {
                    Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Please enter any value in searchbox");
                }
            }
        });
    }

    private void callShlokDetailsPdfApi(String strGSID) {
        if (!ConnectionManager.checkInternetConnection(ShlokSearchDetailsActivity.this)) {
            Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(ShlokSearchDetailsActivity.this, "Please Wait...", false);

        ApiClient.getShlokGranthDetailsPdf(strGSID, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    ResponseBody keywordSearchModel1 = response.body();
                    Log.e("responseData Keyword :", new GsonBuilder().setPrettyPrinting().create().toJson(keywordSearchModel1));
                    String strPdfFile = downloadFile(keywordSearchModel1);
                    if (strPdfFile != null && strPdfFile.length() > 0) {
                        // showExportDialog(view, strPdfFile);
                        getShareDialog(strPdfFile);
                    } else {
                        Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Pdf data not download");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Pdf data not download");
                Utils.dismissProgressDialog();
            }
        });
    }

    public String downloadFile(ResponseBody body) {
        try {
            Log.d("downloadFile", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            String strEdtRenamefile =  strShlokGranthName;

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

    public void getShareDialog(String strPdfFile) {
        List<String> mReferenceStringList = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(ShlokSearchDetailsActivity.this, R.style.BottomSheetDialogTheme);
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
        strUserId = SharedPrefManager.getInstance(ShlokSearchDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        String strLanguage = SharedPrefManager.getInstance(ShlokSearchDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.setFocusable(true);
        strEdtRenamefile = mReferenceBookList.size() + "_Reference_Addresses_For_Granth_" + "'" + strShlokGranthName + "'";
        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(ShlokSearchDetailsActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(ShlokSearchDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, ShlokSearchDetailsActivity.this, strLanguage, bottomSheetDialog,null);
//                return false;
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomSheetDialog.cancel();
//                String strEdtRenamefile = edtRenameFile.getText().toString();
//                String shareText = strEdtRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
//                Constantss.FILE_NAME = strEdtRenamefile;
//                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strShlokGranthName  + "_" + mReferenceBookList.size() + " /";
//                //String strPdfFile = PdfCreator.createFile(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
//                callShareMyShelfsApi(strUserId, shareText, strPdfFile);
                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strShlokGranthName + "_" +  mReferenceBookList.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strShlokGranthName + "_" + mReferenceBookList.size() + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strPdfFile = PdfCreator.createTextPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        callAddMyShelfApi(strUID,mReferenceStringList,mFile,strEdtRenamefile, true);
                    } else {
                        Log.e("download--", "file not exits" + strPdfFile);
                    }
                }

            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                Constantss.FILE_NAME = strEdtRenamefile;
                String strPdfFile = PdfCreator.createTextPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                Constantss.FILE_NAME_PDF = "JainRefLibrary /"+strShlokGranthName+"_" + mReferenceBookList.size() +" /";
                callDownloadMyShelfsApi(strUserId, strPdfFile);
            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strShlokGranthName + "_" +  mReferenceBookList.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strShlokGranthName + "_" + mReferenceBookList.size() + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strPdfFile = PdfCreator.createTextPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        callAddMyShelfApi(strUID,mReferenceStringList,mFile,strEdtRenamefile, false);
                    } else {
                        Log.e("download--", "file not exits" + strPdfFile);
                    }
                }
                //   Toast.makeText(KeywordSearchDetailsActivity.this, "work in process", Toast.LENGTH_SHORT).show();

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
    private void declaration() {
        etSearchView.setShowSoftInputOnFocus(false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(ShlokSearchDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);

        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
            strUID = userDetailsModel.getId();
        }
        if (strShlokGranthId != null && strShlokGranthId.length() > 0) {
            isFirstTimeCall = true;
            callShlokSutraApi("");
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight()) {
                        if (!isLoading) {
                            if (page < total_pages) {
                                page++;
                                isFirstTimeCall = false;

                                callShlokSutraApi(etSearchView.getText().toString());
                            }
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            Log.e(TAG, "loading");
                        }
                    }
                }
            });
        }

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            strShlokGranthName = intent.getExtras().getString("shlokGranthName");
            strShlokGranthId = intent.getExtras().getString("shlokGranthId");
            /*strBookIds = intent.getExtras().getString("bookIds");
            tvPageName.setText(strKeyName);*/
        }

        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*buttonFilter.setVisibility(View.GONE);
                strLanguage = language[2];
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, ShlokSearchActivity.this, strLanguage, null,ivSend);*/
                Utils.showDefaultKeyboardDialog(ShlokSearchDetailsActivity.this);
            }
        });
    }

    private void loadUiElements() {
        rvList = findViewById(R.id.rvList);
        tvNoRecord = findViewById(R.id.tvNoRecord);
        llKeywordCount = findViewById(R.id.llKeywordCount);
        tvKeywordCount = findViewById(R.id.tvKeywordCount);
        progressBar = findViewById(R.id.progress_bar);
        nestedScrollView = findViewById(R.id.scroll_view);
        llExport = findViewById(R.id.llExport);
        etSearchView = findViewById(R.id.etSearchView);
        mKeyboardView = findViewById(R.id.keyboardView);
        ivKeyboard = findViewById(R.id.ivKeyboard);
        llFilter = findViewById(R.id.llFilter);
        llClose = findViewById(R.id.llClose);
        ivClose = findViewById(R.id.ivClose);
        ivSend = findViewById(R.id.ivSend);

        // loadData("");
    }

    private void setHeader() {

        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        llAddItem.setVisibility(View.VISIBLE);
        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        ivHeaderIcon.setVisibility(View.INVISIBLE);
        tvHeaderCount.setVisibility(View.INVISIBLE);
        ivHeaderIcon.setImageResource(R.mipmap.shlok_search);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            strShlokGranthName = intent.getExtras().getString("shlokGranthName");
            strShlokGranthId = intent.getExtras().getString("shlokGranthId");
            tvPageName.setText(strShlokGranthName);
        }
                //   Toast.makeText(KeywordSearchDetailsActivity.this, "work in process", Toast.LENGTH_SHORT).show();
    }

    private void setShlokSutraData(ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> mGranthDataList) {

        if (mGranthDataList == null || mGranthDataList.size() == 0) {
            ivHeaderIcon.setVisibility(View.INVISIBLE);
            tvHeaderCount.setVisibility(View.INVISIBLE);
            return;
        }


        if (isFirstTimeCall) {
            if (mGranthDataList != null && mGranthDataList.size() > 0) {
                tvKeywordCount.setText("Select Sutra No.");
                llKeywordCount.setVisibility(View.VISIBLE);
                tvKeywordCount.setVisibility(View.VISIBLE);
            }
            rvList.setLayoutManager(new LinearLayoutManager(ShlokSearchDetailsActivity.this));
            mSearchAdapter = new ShlokSearchDetailsListAdapter(ShlokSearchDetailsActivity.this, mGranthDataList, this);
            rvList.setAdapter(mSearchAdapter);
        } else {
            rvList.setVisibility(View.VISIBLE);
            Log.e(TAG, "set mGranthDataList --"+mGranthDataList.size());
            mSearchAdapter.newData(mGranthDataList);
        }
    }


    public void callShlokSutraApi(String strSearchText) {
        if (!ConnectionManager.checkInternetConnection(ShlokSearchDetailsActivity.this)) {
            Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Please check internet connection");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
        String strStartPage = String.valueOf(page);
        // Utils.showProgressDialog(ShlokSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.getShlokGranthSutra(strShlokGranthId, strSearchText, strStartPage, new Callback<ShlokGranthSutraResModel>() {
            @Override
            public void onResponse(Call<ShlokGranthSutraResModel> call, retrofit2.Response<ShlokGranthSutraResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        total_pages = response.body().getTotal_pages();

                        ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> bookDetailsModelss = new ArrayList<>();
                        bookDetailsModelss = response.body().getData();
                        setPaginateData(bookDetailsModelss);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        tvNoRecord.setVisibility(View.VISIBLE);
                        rvList.setVisibility(View.GONE);
                    }
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<ShlokGranthSutraResModel> call, Throwable t) {
                String message = t.getMessage();
                progressBar.setVisibility(View.GONE);
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void setPaginateData(ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> bookDetailsModelss) {

        if (bookDetailsModelss != null && bookDetailsModelss.size() > 0) {
            bookDetailsModels.addAll(bookDetailsModelss);
            Log.e(TAG, "add data--");
        }
        mReferenceBookList = bookDetailsModelss;
        progressBar.setVisibility(View.GONE);

        if (bookDetailsModels != null && bookDetailsModels.size() > 0) {
            tvNoRecord.setVisibility(View.GONE);
            rvList.setVisibility(View.VISIBLE);
            Log.e(TAG, "set data--");
            setShlokSutraData(bookDetailsModels);
        } else {
            tvNoRecord.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetailsClick(ShlokGranthSutraResModel.ShlokGranthSutraModel granthList, int position) {
        String strName = granthList.getName();
        String strGSID = granthList.getId();
        Intent i = new Intent(ShlokSearchDetailsActivity.this, ShlokSutraDetailsActivity.class);
        i.putExtra("shlokSutraName", strName);
        i.putExtra("gsid", strGSID);
        i.putExtra("shlokGranthName", strShlokGranthName);
        i.putExtra("shlokGranthId", strShlokGranthId);
        startActivity(i);
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
                        Intent intent = new Intent(ShlokSearchDetailsActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strSutraName", strShlokGranthName);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }
    public void share(String shareData, String mFilePath) {
        File mFile = new File(mFilePath);
        Uri fileUri = Uri.fromFile(mFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            fileUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(mFilePath));
        }
        //  Uri fileUri = FileProvider.getUriForFile(KeywordSearchDetailsActivity.this, KeywordSearchDetailsActivity.this.getApplicationContext().getPackageName() + ".provider", mFile);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType(URLConnection.guessContentTypeFromName(mFile.getName()));
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, shareData);
        intentShareFile.putExtra(Intent.EXTRA_TEXT, shareData);
        startActivity(Intent.createChooser(intentShareFile, shareData));

    }
    private String getReferenceName(ShlokGranthSutraResModel.ShlokGranthSutraModel bookDetailsModel) {
        String strReferenceName = "";
        String strBookname = bookDetailsModel.getName();
        if (strBookname != null && strBookname.length() > 0) {
            strReferenceName = strBookname;
        } else {
            strBookname = "";
        }
        return strReferenceName;
    }
    private void askLogin() {
        Utils.showLoginDialogForResult(ShlokSearchDetailsActivity.this, 1);
    }

    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(ShlokSearchDetailsActivity.this, "Please Wait...", false);

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
                                Toast.makeText(ShlokSearchDetailsActivity.this, "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("download--", "file not exits" + strPdfFile);
                            }
                        }*/
                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            Utils.downloadLocalPDF(strPdfFile, ShlokSearchDetailsActivity.this);
                        }
                        else {
                            Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Pdf not found");
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

    private void callShareMyShelfsApi(String strUserId, String shareText, String strPdfFile) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(ShlokSearchDetailsActivity.this, "Please Wait...", false);

        ApiClient.shareMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
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

                        try {
                            if (strPdfLink != null && strPdfLink.length() > 0) {
                                String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                                String strMessage = strPdfLink; //" " + strBookName;
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                                intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                startActivity(Intent.createChooser(intent, shareData));
                            }
                        } catch (Exception e) {
                            Log.e("Exception Error", "Error---" + e.getMessage());
                        }

//                        if (strPdfFile != null && strPdfFile.length() > 0) {
//                            File mFile = new File(strPdfFile);
//                            if (mFile.exists()) {
//                                share(shareText, strPdfFile);
//                            } else {
//                                Log.e("share--", "file not exits" + strPdfFile);
//                            }
//                        }

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

    private void callAddMyShelfApi(String strUId, List<String> mReferenceStringList, File mFile, String strEdtRenamefile, boolean isShare)  {
        Utils.showProgressDialog(ShlokSearchDetailsActivity.this, "Please Wait...", false);
        MultipartBody.Part  filePart = null;
        if (mFile.exists())
                filePart = MultipartBody.Part.createFormData("pdf_file", mFile.getName(), RequestBody.create(MediaType.parse("*/*"), mFile));

        RequestBody bid = RequestBody.create(MediaType.parse("text/*"), "");
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUId);
        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), strEdtRenamefile);
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "0");
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), TYPE_SHLOK_PAGE);
        Utils.showProgressDialog(ShlokSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfs(uid, null, null, type, typeref, null,null,filename, null, filePart, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        strPdfLink = response.body().getPdf_url();
                        if (isShare) {
                            callShareMyShelfsApi(strUserId, shareText, strPdfFile);
                        }
                        else {
                            Utils.showInfoDialog(ShlokSearchDetailsActivity.this, "Shlok Added In My Reference.");
                        }
                    } else {
                        Utils.showInfoDialog(ShlokSearchDetailsActivity.this,"Some Error Occured..");
                    }
                }
            }


            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(ShlokSearchDetailsActivity.this,"Something went wrong please try again later");

            }
        });
    }

}
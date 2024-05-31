package com.jainelibrary.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.Constantss;
import com.jainelibrary.R;
import com.jainelibrary.adapter.YearListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UploadPDFModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ClearHoldReferenceModel;
import com.jainelibrary.retrofitResModel.ShlokGranthSutraResModel;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jainelibrary.utils.Utils.TYPE_KEYWORD_PAGE;
import static com.jainelibrary.utils.Utils.TYPE_YEAR_PAGE;

public class YearListActivity extends AppCompatActivity implements YearListAdapter.YearClickListener {
    RecyclerView rvYearBookList;
    private static final String TAG = ShlokSearchDetailsActivity.class.getSimpleName();
    ArrayList<BookListResModel.BookDetailsModel> yearListModels = new ArrayList<>();
    private YearListAdapter yearListAdapter;
    private int selected = 1;
    private String strShlokGranthName;
    String strShlokGranthId;
    ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> bookDetailsModels = new ArrayList<>();
    TextView tvNoRecord;
    private LinearLayout llKeywordCount, getLlExport;
    private TextView tvHeaderCount, tvKeywordCount;
    private ImageView ivHeaderIcon;
    NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    boolean isLoading = false, isFirstTimeCall = true;
    int page = 1;
    private String strYearId;
    private String strYearName;
    private LinearLayout llExport;
    private BottomSheetDialog bottomSheetDialog;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private String PackageName;
    private String strUsername, strUID;
    View headerView, header2;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearist);
        Log.e(TAG, "ABC");
        setHeader();
        PackageName = YearListActivity.this.getPackageName();
        loadUiElements();
        declaration();
    }

    private void declaration() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(YearListActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);
        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
            strUID = userDetailsModel.getId();
        }
        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(YearListActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    String strSearch = strYearName;
                    if (strSearch != null && strSearch.length() > 0) {
                        List<String> mKeywordStringList = new ArrayList<>();
                        if (yearListModels != null && yearListModels.size() > 0) {
                            for (int i = 0; i < yearListModels.size(); i++) {
                                String strNamr = yearListModels.get(i).getName();
                                if (strNamr != null && strNamr.length() > 0) {
                                    mKeywordStringList.add(strNamr);
                                }
                            }

                            if (mKeywordStringList != null && mKeywordStringList.size() > 0) {
                                getShareDialog(mKeywordStringList);
                            }
                        } else {
                            InfoDialog("No Year data found");
                        }
                    } else {
                        InfoDialog("Please search any other Year");
                    }
                } else {
                    askLogin();
                }
            }
        });

    }

    private void askLogin() {
        Utils.showLoginDialogForResult(YearListActivity.this, 1);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(YearListActivity.this);
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", dialogClickListener)
                .show();

    }

    /*private void onClicKlistener() {
        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(KeywordSearchDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
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
                }
            }

        });
    }*/
    public void getShareDialog(List<String> mKeywordStringList) {

        bottomSheetDialog = new BottomSheetDialog(YearListActivity.this, R.style.BottomSheetDialogTheme);

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
        String strLanguage = SharedPrefManager.getInstance(YearListActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));

        String strEdtRenamefile = yearListModels.size() + "_Search Results For_" + strYearName;
        edtRenameFile.setText(strEdtRenamefile);
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(YearListActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(YearListActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, YearListActivity.this, strLanguage, bottomSheetDialog, null);
//                return false;
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String shareText = strEdtRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
                Constantss.FILE_NAME = strEdtRenamefile;
                String strPdfFile = PdfCreator.createTextPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strShlokGranthName + "_" + mKeywordStringList.size() + " /";
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        share(shareText, strPdfFile);
                    } else {
                        Log.e("share--", "file not exits" + strPdfFile);
                    }
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strPdfFile = null;
                try {
                    Constantss.FILE_NAME = strEdtRenamefile;
                    Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strShlokGranthName + " " + mKeywordStringList.size() + " /";
                    strPdfFile = PdfCreator.createTextPdf(
                            Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mKeywordStringList);

                    /*if (strPdfFile != null && strPdfFile.length() > 0) {
                        File mFile = new File(strPdfFile);
                        if (mFile.exists()) {
                            Toast.makeText(YearListActivity.this, "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("download--", "file not exits" + strPdfFile);
                        }
                    }*/
                    if (strPdfFile != null && strPdfFile.length() > 0) {
                        Utils.downloadLocalPDF(strPdfFile, YearListActivity.this);
                    }
                    else {
                        Utils.showInfoDialog(YearListActivity.this, "Pdf not found");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }

            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = strEdtRenamefile;
                String strPdfFile = PdfCreator.createTextPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strShlokGranthName + "_" + mKeywordStringList.size() + " /";
                if (strPdfFile != null && strPdfFile.length() > 0) {


                        File mFile = new File(strPdfFile);
                        if (mFile.exists()) {
                            UploadFile(strUID, strEdtRenamefile, mFile);
                        } else {
                            Log.e("download--", "file not exits" + strPdfFile);
                        }
                    }
                }
            });

        ivClose.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){
                bottomSheetDialog.cancel();
            }
            });
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        bottomSheetDialog.show();
            BottomSheetBehavior behavior = BottomSheetBehavior.from((View) bottomSheetDialogView.getParent());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()

            {
                @Override
                public void onStateChanged (@NonNull View bottomSheet,
                @BottomSheetBehavior.State int newState){
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
                public void onSlide (@NonNull View bottomSheet,float slideOffset){
            }
            });

        }
        private void loadUiElements () {
            rvYearBookList = findViewById(R.id.rvYearbooklist);
            llKeywordCount = findViewById(R.id.llKeywordCount);
            tvKeywordCount = findViewById(R.id.tvKeywordCount);
            llExport = findViewById(R.id.llExport);
            getYearList();
            // loadData("");
        }
        private void UploadFile (String strUId, String strEdtRenamefile, File mFile){
            RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUId);
            RequestBody filename = RequestBody.create(MediaType.parse("text/*"), strEdtRenamefile);
            RequestBody type = RequestBody.create(MediaType.parse("text/*"), "1");
            RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), TYPE_YEAR_PAGE);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("keywordpdf", mFile.getName(), RequestBody.create(MediaType.parse("image/*"), mFile));
            Utils.showProgressDialog(YearListActivity.this, "Please Wait...", false);
            ApiClient.UploadFile(filePart, uid, filename, type, typeref, new Callback<UploadPDFModel>() {
                @Override
                public void onResponse(Call<UploadPDFModel> call, Response<UploadPDFModel> response) {
                    if (response.isSuccessful()) {
                         /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                        Utils.dismissProgressDialog();
                        if (response.body().isStatus()) {
                            Utils.showInfoDialog(YearListActivity.this, "Keywords Added In My Reference.");
                        } else {
                            Utils.showInfoDialog(YearListActivity.this, "Some Error Occured..");
                        }
                    }

                }

                @Override
                public void onFailure(Call<UploadPDFModel> call, Throwable t) {
                    String message = t.getMessage();
                    Log.e("error", message);
                    Utils.dismissProgressDialog();
                    Utils.showInfoDialog(YearListActivity.this, "Something went wrong please try again later");

                }
            });


        }

        private void setBookData (ArrayList < BookListResModel.BookDetailsModel > mBookDataList) {
            if (mBookDataList == null || mBookDataList.size() == 0) {
                return;
            }
            yearListModels = mBookDataList;
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
                tvKeywordCount.setText("" + mBookDataList.size() + " Reference Address");
            } else {
                llKeywordCount.setVisibility(View.GONE);
            }
            yearListModels = mBookDataList;
            ///      strpageno = new ArrayList<>();
            // strpagenodetail = new ArrayList<>();
            yearListAdapter = new YearListAdapter(YearListActivity.this, yearListModels, this);
            rvYearBookList.setLayoutManager(new LinearLayoutManager(this));
            rvYearBookList.setHasFixedSize(true);
            rvYearBookList.setAdapter(yearListAdapter);
        }
        public void share (String shareData, String mFilePath){
            File mFile = new File(mFilePath);
            Uri fileUri = Uri.fromFile(mFile);
            //  Uri fileUri = FileProvider.getUriForFile(KeywordSearchActivity.this, KeywordSearchActivity.this.getApplicationContext().getPackageName() + ".provider", mFile);
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType(URLConnection.guessContentTypeFromName(mFile.getName()));
            intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentShareFile.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, shareData);
            intentShareFile.putExtra(Intent.EXTRA_TEXT, shareData);
            startActivity(Intent.createChooser(intentShareFile, shareData));

        }
        private void getYearList () {
            if (!ConnectionManager.checkInternetConnection(YearListActivity.this)) {
                Utils.showInfoDialog(YearListActivity.this, "Please check internet connection");
                return;
            }
            Utils.showProgressDialog(YearListActivity.this, "Please Wait...", false);
            ApiClient.getYearList(strYearId, new Callback<BookListResModel>() {
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
                            Utils.showInfoDialog(YearListActivity.this, response.body().getMessage());
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

        private void setHeader () {
            headerView = findViewById(R.id.header);
            headerView.setVisibility(View.VISIBLE);
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
                strYearId = intent.getExtras().getString("YearID");
                strYearName = intent.getExtras().getString("YearName");
                tvPageName.setText(strYearName);
            }
        }

        @Override
        public void onYearClick (BookListResModel.BookDetailsModel wordModel,int position){
            Intent i = new Intent(YearListActivity.this, ReferencePageActivity.class);
            i.putExtra("model", yearListModels.get(position));
            i.putExtra("moduleNo", TYPE_YEAR_PAGE);
            i.putExtra("type_id", "3");
            startActivity(i);
        }
    }

package com.jainelibrary.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.BuildConfig;
import com.jainelibrary.Constantss;
import com.jainelibrary.R;
import com.jainelibrary.adapter.KeywordSearchListAdapter;
import com.jainelibrary.adapter.YearBookDetailsAdapter;
import com.jainelibrary.adapter.YearBookListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.model.YearBookResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wc.widget.dialog.IosDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jainelibrary.utils.Utils.REF_TYPE_REFERENCE_PAGE;
import static com.jainelibrary.utils.Utils.TYPE_KEYWORD_PAGE;

public class YearBookDetailsActivity extends AppCompatActivity implements YearBookDetailsAdapter.SearchInterfaceListener{

    private RecyclerView rvYearBookDetail;
    private YearBookDetailsAdapter mSearchAdapter;
    private LinearLayout llExport;
    private BottomSheetDialog bottomSheetDialog;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private static final int REFERENCE_CODE = 1;
    private static final String TAG = YearBookDetailsActivity.class.getSimpleName();
    private String PackageName;
    private TextView tvHeaderCount, tvKeywordCount;
    private ImageView ivHeaderIcon;
    private LinearLayout llAddItem, llKeywordCount;
    private String strUsername;
    private ArrayList<YearBookResModel.YearBookList> mYearBookList = new ArrayList<>();
    private ArrayList<YearBookResModel.YearBookList.YearBook> bookModels = new ArrayList<>();
    ArrayList<String> strpageno = new ArrayList<>();
    ArrayList<String> strpagenodetail = new ArrayList<>();
    private String strUID, strPdfLink;
    int pagecount;
    CountDownTimer timer;
    String strEdtRenamefile = null, strUserId, shareText, strTypeRef;
    Activity activity;
    private String strPdfFile = null;
    private String strTotalCount = "";
    private String strBookName, strBookId, strBookPageNo, strBookImage;
    int bookPageNo;
    TextView tvDetails;
    Button btnPageNo;
    ImageView ivBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_book_details);
        PackageName = YearBookDetailsActivity.this.getPackageName();
        Log.e(TAG, "ABC");
        loadUiElements();
        declaration();
        setHeader();
        onClicKlistener();
    }

    private void onClicKlistener() {

        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(YearBookDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    //callBookDetailsPdfApi(strKeyId,strBookIds);
                    //getYearBookPdf("1", "2078");
                } else {
                    askLogin();
                }


            }
        });

        btnPageNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(YearBookDetailsActivity.this, ReferencePageActivity.class);
                //i.putExtra("strTotalCount", strTotalCount);
                /*i.putExtra("pageno", str[0]);
                i.putExtra("pdfpage", str[3]);
                i.putExtra("bookid", str[1]);
                i.putExtra("keywordid", str[2]);*/
                /*i.putExtra("pagemodel", mYearBookList.getBooks());
                i.putExtra("moduleNo", TYPE_KEYWORD_PAGE);
                i.putExtra("model", mBookDetails);*/
                i.putExtra("type_id", "3");
                startActivity(i);

            }
        });
    }

    private void declaration() {

        /*if (getIntent() != null) {
            strTotalCount = getIntent().getStringExtra("totalKeywordCount");
        }*/
        Log.e(TAG, "strTotalCount : " + strTotalCount);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            strBookName = intent.getExtras().getString("bookName");
            strBookId = intent.getExtras().getString("bookId");
            bookPageNo = Integer.parseInt(intent.getExtras().getString("pageNo"));
            strBookImage = intent.getExtras().getString("bookImage");
            //mYearBookList = (ArrayList<YearBookResModel.YearBookList>)intent.getSerializableExtra("YearBookList");
            /*strYearType = i.getStringExtra("YearType");
            strYear = i.getStringExtra("Year");*/

            /*Log.e("BookList",  mYearBookList.get(0).getName());

            mYearBookList.get(0).getName();

            bookModels = mYearBookList.get(0).getBooks();*/
            Log.e("YearBookDetails bName",  strBookName);
            Log.e("YearBookDetails bId",  strBookId);

            Log.e("YearBookDetails bImg",  strBookImage);

            strBookPageNo = String.valueOf(bookPageNo);
            Log.e("YearBookDetails bPage",  strBookPageNo);

            if (strBookImage != null && strBookImage.length() > 0) {
                Picasso.get().load(strBookImage).into(ivBook);
            } else {
                ivBook.setImageTintList(ColorStateList.valueOf(this.getResources().getColor(R.color.light_background)));
            }

            String strFinalWord = null;

            if (strBookName != null && strBookName.length() > 0) {
                strFinalWord = strBookName;
            } else {
                strBookName = "";
            }

            if (strBookPageNo != null && strBookPageNo.length() > 0) {
                strBookPageNo = "" + "Page - " + strBookPageNo;
                btnPageNo.setText(strBookPageNo);
            }
            //tvPageName.setText(strShlokGranthName + " " + strSutraName);
        }

        setBookDetails(mYearBookList);
        //setBookData(mYearBookList);
        /*ll_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYearBookPdf(strYearType, strYear);
            }
        });*/

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(YearBookDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);

        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
            strUID = userDetailsModel.getId();
        }
    }

    private void setBookDetails(ArrayList<YearBookResModel.YearBookList> mYearBookList) {
        if (mYearBookList == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvYearBookDetail.setHasFixedSize(true);
        rvYearBookDetail.setLayoutManager(linearLayoutManager);
        mSearchAdapter = new YearBookDetailsAdapter(this, mYearBookList, this, strBookName, strBookId, strBookPageNo, strBookImage);
        rvYearBookDetail.setAdapter(mSearchAdapter);
    }

    private void loadUiElements() {
        Constantss.ISTIMERFINISH = false;
        rvYearBookDetail = findViewById(R.id.rvYearBookDetail);
        llKeywordCount = findViewById(R.id.llKeywordCount);
        tvKeywordCount = findViewById(R.id.tvKeywordCount);
        llExport = (LinearLayout) findViewById(R.id.llExport);
        tvDetails = (TextView) findViewById(R.id.textViewName);
        btnPageNo = findViewById(R.id.btnPageNo);
        ivBook = findViewById(R.id.ivBook);
        //loadData();
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
        ivHeaderIcon.setImageResource(R.mipmap.book);

        ivBack.setVisibility(View.VISIBLE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(strBookName + " ");
        ssb.setSpan(new ImageSpan(YearBookDetailsActivity.this, R.drawable.ic_baseline_chevron_right_24),
                ssb.length() - 1,
                ssb.length(),
                0);
        ssb.append(strBookName);
        tvPageName.setText(ssb);
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(YearBookDetailsActivity.this, REFERENCE_CODE);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(YearBookDetailsActivity.this);
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", dialogClickListener)
                .show();

    }

    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(YearBookDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(YearBookDetailsActivity.this, "Please Wait...", false);

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
                                Toast.makeText(YearBookDetailsActivity.this, "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("download--", "file not exits" + strPdfFile);
                            }
                        }*/
                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            Utils.downloadLocalPDF(strPdfFile, YearBookDetailsActivity.this);
                        }
                        else {
                            Utils.showInfoDialog(YearBookDetailsActivity.this, "Pdf not found");
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
            Utils.showInfoDialog(YearBookDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(YearBookDetailsActivity.this, "Please Wait...", false);

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

    private void callAddMyShelfApi(String strUserId, List<String> mList, File mFile, String strEdtRenamefile, boolean isShare) {
        //Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strKeyName + "_" + strPageNo + "_" + mYearBookList.size() + " /";
        MultipartBody.Part filePart = null;


        if (mFile.exists())
            filePart = MultipartBody.Part.createFormData("pdf_file", mFile.getName(), RequestBody.create(MediaType.parse("*/*"), mFile));

        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUserId);
        RequestBody filename = RequestBody.create(MediaType.parse("text/*"),strEdtRenamefile);
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "3");
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), REF_TYPE_REFERENCE_PAGE);
        RequestBody typeId = RequestBody.create(MediaType.parse("text/*"), strBookId);
        RequestBody count = RequestBody.create(MediaType.parse("text/*"), strTotalCount);
        RequestBody fileType = RequestBody.create(MediaType.parse("text/*"), "3");
        Log.e("fileType :", " "+fileType);
        Utils.showProgressDialog(YearBookDetailsActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfs(uid, null, typeId, type, typeref, filename, null, count, fileType, filePart, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    Utils.dismissProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().isStatus()) {
                            strPdfLink = response.body().getPdf_url();
                            if (isShare) {
                                callShareMyShelfsApi(strUserId, shareText, strPdfFile);
                            }
                            else {
                                Utils.showInfoDialog(YearBookDetailsActivity.this, "Year Book Added In My Reference.");
                            }
                        } else {
                            Utils.showInfoDialog(YearBookDetailsActivity.this, response.body().getMessage());
                            //Toast.makeText(getApplicationContext(), "Some Error Occured..", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("error--", "ResultError--" + response.message());
                    }
                }
            }


            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(YearBookDetailsActivity.this, "Something went wrong please try again later");

            }
        });

    }


    public void getShareDialog(String strPdfFile) {
        List<String> mReferenceStringList = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(YearBookDetailsActivity.this, R.style.BottomSheetDialogTheme);
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
        String strLanguage = SharedPrefManager.getInstance(YearBookDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        strUID = SharedPrefManager.getInstance(YearBookDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        edtRenameFile.setFocusable(true);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        String strEdtRenamefile = pagecount + "_Reference_Addresses_For_Year_" + "'" + strBookName + "'";
        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(YearBookDetailsActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(YearBookDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, YearBookDetailsActivity.this, strLanguage, bottomSheetDialog, null);
//                return false;
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomSheetDialog.cancel();
//                shareText = strEdtRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
//                Constantss.FILE_NAME = strEdtRenamefile;
//                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + strBookPageNo + "_" + mYearBookList.size() + " /";
//                callShareMyShelfsApi(strUID, shareText, strPdfFile);
                //strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + strBookPageNo + "_" + mYearBookList.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + strBookPageNo + "_" + mYearBookList.size() + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        callAddMyShelfApi(strUID, mReferenceStringList, mFile,strEdtRenamefile, true);
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
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + strBookPageNo + "_" + mYearBookList.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + strBookPageNo + "_" + mYearBookList.size() + " /";
                callDownloadMyShelfsApi(strUID, strPdfFile);
                // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);

            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + strBookPageNo + "_" + mYearBookList.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + strBookPageNo + "_" + mYearBookList.size() + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        callAddMyShelfApi(strUID, mReferenceStringList, mFile,strEdtRenamefile, false);
                    } else {
                        Log.e("download--", "file not exits" + strPdfFile);
                    }
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
            if (mYearBookList != null && mYearBookList.size() > 0) {
                for (int i = 0; i < mYearBookList.size(); i++) {
                    String strImagePath = mYearBookList.get(i).getName();
                    if (strImagePath != null && strImagePath.length() > 0) {
                        mKeywordStringList.add(strImagePath);
                    }
                }
                String strPdfFile = null;
                if (mKeywordStringList != null && mKeywordStringList.size() > 0) {
                    Constantss.FILE_NAME = strEdtRenamefile;
                    Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strBookName + "_" + strBookPageNo + " /";
                    strPdfFile = PdfCreator.createTextPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), Constantss.FILE_NAME, mKeywordStringList);
                }
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    Uri fileUri = Uri.fromFile(new File(strPdfFile));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    {
                        fileUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(strPdfFile));
                    }
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
                Utils.showInfoDialog(YearBookDetailsActivity.this, "No book data found");
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
    }

    private void setBookData(ArrayList<YearBookResModel.YearBookList> mBookDataList) {
        if (mBookDataList == null || mBookDataList.size() == 0) {
            return;
        }
        mYearBookList = mBookDataList;
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
                bookModels = new ArrayList<>();
                bookModels = mBookDataList.get(i).getBooks();
                pagecount = pagecount + bookModels.size();
            }
            strTotalCount = String.valueOf(pagecount);
            tvKeywordCount.setText("" + pagecount + " Reference Address");
        } else {
            llKeywordCount.setVisibility(View.GONE);
        }
        mYearBookList = mBookDataList;
        ///      strpageno = new ArrayList<>();
        // strpagenodetail = new ArrayList<>();
        Log.e(strpageno + "", strpageno.size() + "");
        Log.e(strpagenodetail + "", strpagenodetail.size() + "");
        /*rvYearBookDetail.setHasFixedSize(true);
        rvYearBookDetail.setLayoutManager(new LinearLayoutManager(YearBookDetailsActivity.this));
        rvYearBookDetail.setVisibility(View.VISIBLE);
        mSearchAdapter = new YearBookDetailsAdapter(YearBookDetailsActivity.this, mBookDataList, this, strBookName, strBookId, strBookPageNo, strBookImage);
        rvYearBookDetail.setAdapter(mSearchAdapter);*/
        if (mBookDataList.size() == 1)
            if (mBookDataList.get(0).getBooks().size() == 1) {
                timer = new CountDownTimer(3000, 2000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if (Constantss.ISREFERENCE) {
                            if (Constantss.ISTIMERFINISH) {
                                Constantss.ISREFERENCE = false;
                                Intent i = new Intent(YearBookDetailsActivity.this, ReferencePageActivity.class);
                                i.putExtra("strTotalCount", strTotalCount);
                                i.putExtra("pageno", bookModels.get(0).getPdf_page_no());
                                i.putExtra("pdfpage", bookModels.get(0).getPage_no());
                                i.putExtra("bookid", bookModels.get(0).getBook_id());
                                i.putExtra("pagemodel", mBookDataList.get(0).getBooks());
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


    public void share(String shareData, String mFilePath) {
        File mFile = new File(mFilePath);
        Uri fileUri = Uri.fromFile(mFile);//  Uri fileUri = FileProvider.getUriForFile(YearBookDetailsActivity.this, YearBookDetailsActivity.this.getApplicationContext().getPackageName() + ".provider", mFile);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType(URLConnection.guessContentTypeFromName(mFile.getName()));
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, shareData);
        intentShareFile.putExtra(Intent.EXTRA_TEXT, shareData);
        startActivity(Intent.createChooser(intentShareFile, shareData));
    }

    /*private void callBookDetailsPdfApi(String strKeyId,  String strBookIds) {
        if (!ConnectionManager.checkInternetConnection(YearBookDetailsActivity.this)) {
            Toast.makeText(YearBookDetailsActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        Utils.showProgressDialog(YearBookDetailsActivity.this, "Please Wait...", false);

        ApiClient.getKeywordBookDetailsPdf(strKeyId,strBookIds, new Callback<ResponseBody>() {
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
                        Toast.makeText(YearBookDetailsActivity.this, "Pdf data not download", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Toast.makeText(YearBookDetailsActivity.this, "Pdf data not download", Toast.LENGTH_SHORT).show();
                Utils.dismissProgressDialog();
            }
        });
    }*/

    public String downloadFile(ResponseBody body) {
        try {
            Log.d("downloadFile", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            String strEdtRenamefile =  strBookName;

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

    @Override
    public void onDetailsClick(YearBookResModel.YearBookList mBookDetails, int position) {

            /*String s = view.getTag().toString();
            String[] str = s.split(",");
            String str1 = str[0];
            Log.e("stringAll", str.toString());
            Log.e("strKeyId", strBookId);*/
            //mBookDetails.setKeywordId(strKeyId);

            if (Constantss.ISREFERENCE) {
                Constantss.ISREFERENCE = false;
                Intent i = new Intent(YearBookDetailsActivity.this, ReferencePageActivity.class);
                i.putExtra("strTotalCount", strTotalCount);
                /*i.putExtra("pageno", str[0]);
                i.putExtra("pdfpage", str[3]);
                i.putExtra("bookid", str[1]);
                i.putExtra("keywordid", str[2]);*/
                i.putExtra("pagemodel", mBookDetails.getBooks());
                i.putExtra("moduleNo", TYPE_KEYWORD_PAGE);
                i.putExtra("model", mBookDetails);
                i.putExtra("type_id", "3");
                startActivity(i);
                finish();

            }
    }

    /*private void getYearBookPdf(String strTypeId, String strYear) {
        if (!ConnectionManager.checkInternetConnection(YearBookDetailsActivity.this)) {
            Toast.makeText(YearBookDetailsActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(YearBookDetailsActivity.this, "Please Wait...", false);
        ApiClient.getYearBookPdf(strTypeId, strYear, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    ResponseBody keywordSearchModel1 = response.body();
                    Log.e("responseData Keyword :", new GsonBuilder().setPrettyPrinting().create().toJson(keywordSearchModel1));
                    String strPdfFile = downloadFile(keywordSearchModel1);
                    if (strPdfFile != null && strPdfFile.length() > 0) {
                        // showExportDialog(view, strPdfFile);
                        //getShareDialog(strPdfFile);
                    } else {
                        Toast.makeText(YearBookDetailsActivity.this, "Pdf data not download", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }*/

}
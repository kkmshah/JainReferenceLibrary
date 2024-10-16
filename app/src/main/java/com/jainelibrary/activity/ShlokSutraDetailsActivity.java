package com.jainelibrary.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.jainelibrary.R;
import com.jainelibrary.adapter.ShlokSutraDetailsListAdapter;
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

public class ShlokSutraDetailsActivity extends AppCompatActivity implements ShlokSutraDetailsListAdapter.SearchInterfaceListener {
    private RecyclerView rvList;
    private ArrayList<BookListResModel.BookDetailsModel> mSlokSutraDetailsList = new ArrayList<>();
    private ArrayList<BookListResModel.BookDetailsModel> mReferenceBookList = new ArrayList<>();
    private ShlokSutraDetailsListAdapter mSearchAdapter;
    private int selected = 1;
    private String strShlokGranthId, strShlokGranthName, strSutraName;
    String strGSID;
    String TAG = "ShlokSutraDetailsActivity";
    private LinearLayout llExport;
    TextView tvNoRecord;
    private LinearLayout llKeywordCount;
    private TextView tvHeaderCount, tvKeywordCount;
    private ImageView ivHeaderIcon;
    private String strUsername, strUID;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private BottomSheetDialog bottomSheetDialog;
    private String PackageName;
    List<String> mReferenceStringList;
    private String strCount;
    String strEdtRenamefile = null, strUserId, shareText, strPdfFile, strTypeRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shlok_sutra_details);
        PackageName = ShlokSutraDetailsActivity
                .this.getPackageName();
        Log.e(TAG, "ABC");
        setHeader();
        Declaration();
        loadUiElements();
        onClickLiseners();
    }

    private void Declaration() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(ShlokSutraDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);
        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
            strUID = userDetailsModel.getId();
        }
    }

    private void onClickLiseners() {
        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(ShlokSutraDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    /*Log.e("mSlokSutraDetailsList", mSlokSutraDetailsList.size() + "");
                    Log.e("mReferenceBookList", mReferenceBookList.size() + "");
                    if (mSlokSutraDetailsList != null && mSlokSutraDetailsList.size() > 0) {
                        Log.e("llExport", "llExport");
                        mReferenceStringList = new ArrayList<>();
                        if (mReferenceBookList != null && mReferenceBookList.size() > 0) {
                            for (int i = 0; i < mReferenceBookList.size(); i++) {
                                String strName = getReferenceName(mReferenceBookList.get(i));
                                if (strName != null && strName.length() > 0) {
                                    mReferenceStringList.add(strName);
                                    Log.e("llExport", "llExport" + i);
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
                    getShareDialog();
                } else {
                    askLogin();
                }
            }

        });
    }


    public String downloadFile(ResponseBody body) {
        try {
            Log.d("downloadFile", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            strEdtRenamefile = strShlokGranthName;

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


    public void getShareDialog() {
        List<String> mReferenceStringList = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(ShlokSutraDetailsActivity.this, R.style.BottomSheetDialogTheme);
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
        strUserId = SharedPrefManager.getInstance(ShlokSutraDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        String strLanguage = SharedPrefManager.getInstance(ShlokSutraDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.setFocusable(true);
        if(mSlokSutraDetailsList.size() > 1){
            strEdtRenamefile = mSlokSutraDetailsList.size() + "_Reference_Addresses_For_ShlokSutra_" + strSutraName + "_" + strShlokGranthName;
        }else {
            strEdtRenamefile = mSlokSutraDetailsList.size() + "_Reference_Address_For_ShlokSutra_" + strSutraName + "_" + strShlokGranthName;
        }

        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(ShlokSutraDetailsActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(ShlokSutraDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, ShlokSutraDetailsActivity.this, strLanguage, bottomSheetDialog, null);
//                return false;
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomSheetDialog.cancel();
//                String strRenamefile = edtRenameFile.getText().toString();
//                String shareText = strRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
//                Constantss.FILE_NAME = strRenamefile;
//                //String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strRenamefile, mReferenceStringList);
//                Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strRenamefile + "_" + mReferenceBookList.size() + " /";
//                Log.e("strPdfFile--", "" + strPdfFile);
//                String strFilePath = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strRenamefile + ".pdf";
//                new File(strPdfFile).renameTo(new File(strFilePath));
//                callShareMyShelfsApi(strUserId, shareText, strFilePath);
                bottomSheetDialog.cancel();
                String strTotalCount = ""+mSlokSutraDetailsList.size();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                //String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if ( mSlokSutraDetailsList.size() > 0) {
                    callCreateShlokGranthDetailsPdf(strGSID, strUID, strEdtRenamefile, strTotalCount, true);
                }
            }
        });

//        btnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog.cancel();
//                String strRenamefile = edtRenameFile.getText().toString();
//                Constantss.FILE_NAME = strRenamefile;
//                Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strRenamefile + "_" + mReferenceBookList.size() + " /";
//                //String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strRenamefile, mReferenceStringList);
//                String strFilePath = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strRenamefile + ".pdf";
//                new File(strPdfFile).renameTo(new File(strFilePath));
//                callDownloadMyShelfsApi(strUserId, strFilePath);
//            }
//        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strTotalCount = ""+mSlokSutraDetailsList.size();
                //String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if ( mSlokSutraDetailsList.size() > 0) {
                    saveDetailsFile(strGSID, strUID, strEdtRenamefile, strTotalCount, false);
                }

//                Toast.makeText(ShlokSutraDetailsActivity.this, "work in process", Toast.LENGTH_SHORT).show();
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


    private void saveDetailsFile( String strGSId,  String strUId, String strEdtRenamefile, String totalKeywordCount, boolean isShare) {
        if (!ConnectionManager.checkInternetConnection(ShlokSutraDetailsActivity.this)) {
            Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(ShlokSutraDetailsActivity.this, "Please Wait...", false);
        ApiClient.checkMyShelfFileName(strUId, strEdtRenamefile, new Callback<CheckMyShelfFileNameResModel>() {
            @Override
            public void onResponse(Call<CheckMyShelfFileNameResModel> call, retrofit2.Response<CheckMyShelfFileNameResModel> response) {
                if (!response.isSuccessful()  ) {
                    Utils.dismissProgressDialog();
                    Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "Please try again!");
                    return;
                }

                if(!response.body().isStatus()) {
                    Utils.dismissProgressDialog();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ShlokSutraDetailsActivity.this);
                    builder.setMessage(response.body().getMessage());

                    builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            callCreateShlokGranthDetailsPdf(strGSId, strUId, strEdtRenamefile, totalKeywordCount, true);
                        }
                    });

                    builder.setNegativeButton("Save Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            callCreateShlokGranthDetailsPdf(strGSId, strUId, strEdtRenamefile, totalKeywordCount, isShare);
                        }
                    });

                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

//                    Dialog dialog = new IosDialog.Builder(ShlokSutraDetailsActivity.this)
//                            .setMessage(response.body().getMessage())
//                            .setMessageColor(Color.parseColor("#1565C0"))
//                            .setMessageSize(18)
//                            .setNegativeButtonColor(Color.parseColor("#981010"))
//                            .setNegativeButtonSize(18)
//                            .setNegativeButton("OK", new IosDialog.OnClickListener() {
//                                @Override
//                                public void onClick(IosDialog dialog, View v) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .setPositiveButtonColor(Color.parseColor("#981010"))
//                            .setPositiveButtonSize(18)
//                            .setPositiveButton("Save Again", new IosDialog.OnClickListener() {
//                                @Override
//                                public void onClick(IosDialog dialog, View v) {
//                                    dialog.dismiss();
//                                    callCreateShlokGranthDetailsPdf(strGSId, strUId, strEdtRenamefile, totalKeywordCount, isShare);
//                                }
//                            }).build();
//
//                    dialog.show();
                }
                else
                {
                    callCreateShlokGranthDetailsPdf(strGSId, strUId, strEdtRenamefile, totalKeywordCount, isShare);
                }
            }
            @Override
            public void onFailure(Call<CheckMyShelfFileNameResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });

    }

    private void callCreateShlokGranthDetailsPdf(String strGSId,  String strUId, String strEdtRenamefile, String totalKeywordCount, boolean isShare)
    {
        Utils.showProgressDialog(ShlokSutraDetailsActivity.this, "Please Wait...", false);
        Log.e("responseData Req", strGSId + "");
        ApiClient.createShlokGranthDetailsPdf( strGSId,  new Callback<CreatePdfFileUrlResModel>() {
            @Override
            public void onResponse(Call<CreatePdfFileUrlResModel> call, retrofit2.Response<CreatePdfFileUrlResModel> response) {
                Utils.dismissProgressDialog();
                Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        String strTmpPdfUrl = response.body().getPdf_url();
                        if (strTmpPdfUrl != null && strTmpPdfUrl.length() > 0) {
                            String strPdfLink = response.body().getPdf_url();
                            String strPdfImage = response.body().getPdf_image();
                            if (isShare) {
                                callShareMyShelfsApi(strUserId, shareText, strPdfLink, strPdfImage);
                            }
                            else {
                                callAddMyShelfApi(strTmpPdfUrl, strGSId, strUId, strEdtRenamefile, totalKeywordCount, isShare);
                            }
                        } else {
                            Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "KeywordData not saved");
                        }
                    }else {
                        Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "KeywordData not saved");
                    }

                } else {
                    Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "KeywordData not saved");
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

    private void callAddMyShelfApi(String fileUrl, String strGSId, String strUId, String strEdtRenamefile, String totalCount, boolean isShare) {
        Log.e("TotalCountMyshelf", totalCount);
        String type =  "1";
        String typeref = REF_TYPE_REFERENCE_PAGE;

        Utils.showProgressDialog(ShlokSutraDetailsActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfsWithUrl(strUId, null, strGSId, type, typeref, strEdtRenamefile, null, totalCount, "3", fileUrl, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "Shlok Added In My Reference..");
                    } else {
                        Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "Something went wrong please try again later");
            }
        });
    }


//    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
//        if (!ConnectionManager.checkInternetConnection(this)) {
//            Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "Please check internet connection");
//            return;
//        }
//
//        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
//        Log.e("strUserId :", ""+strUserId);
//        Log.e("strTypeRef :", " "+strTypeRef);
//        Utils.showProgressDialog(ShlokSutraDetailsActivity.this, "Please Wait...", false);
//
//        ApiClient.downloadMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
//            @Override
//            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
//                if (response.isSuccessful()) {
//                    Utils.dismissProgressDialog();
//
//                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));
//
//                    if (response.body().isStatus()) {
//                        /*strUserId = SharedPrefManager.getInstance(ReferencePageActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
//                        if (strUserId != null && strUserId.length() > 0) {
//                            callListHoldSearchKeyword(strUserId);
//                        }*/
//                        /*if (strPdfFile != null && strPdfFile.length() > 0) {
//                            File mFile = new File(strPdfFile);
//                            if (mFile.exists()) {
//                                Toast.makeText(ShlokSutraDetailsActivity.this, "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e("download--", "file not exits" + strPdfFile);
//                            }
//                        }*/
//                        if (strPdfFile != null && strPdfFile.length() > 0) {
//                            Utils.downloadLocalPDF(strPdfFile, ShlokSutraDetailsActivity.this);
//                        }
//                        else {
//                            Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "Pdf not found");
//                        }
//                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }else{
//                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
//                Utils.dismissProgressDialog();
//                Log.e("onFailure :", "Move All Api : "+throwable.getMessage());
//            }
//        });
//    }

    private void callShareMyShelfsApi(String strUserId, String shareText, String strPdfLink, String strPdfImage) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(ShlokSutraDetailsActivity.this, "Please Wait...", false);

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
                              //  String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                                String strMessage = strPdfLink; //" " + strBookName;
                                Utils.shareContentWithImage(ShlokSutraDetailsActivity.this, "JRL Sutra Book(s) PDF", strMessage, strPdfImage);
//
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



    public void getInfoDialogs(String strNotes, String strType, String strTypeRef, String
            strTite, String strKeywordId) {
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
                        Intent intent = new Intent(ShlokSutraDetailsActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strSutra", strSutraName);
                        intent.putExtra("strShlockId", strKeywordId);
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
        //  Uri fileUri = FileProvider.getUriForFile(ShlokSutraDetailsActivity.this, ShlokSutraDetailsActivity.this.getApplicationContext().getPackageName() + ".provider", mFile);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType(URLConnection.guessContentTypeFromName(mFile.getName()));
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, shareData);
        intentShareFile.putExtra(Intent.EXTRA_TEXT, shareData);
        startActivity(Intent.createChooser(intentShareFile, shareData));
    }

    private String getReferenceName(BookListResModel.BookDetailsModel bookDetailsModel) {
        String strReferenceName = "";

        String strBookname = bookDetailsModel.getBook_name();
        String strPageNo = bookDetailsModel.getPage_no();
        String strPdfPageNo = bookDetailsModel.getPdf_page_no();
        String strAutherName = bookDetailsModel.getAuthor_name();
        String strTranslator = bookDetailsModel.getTranslator();
        String strEditorName = bookDetailsModel.getEditor_name();


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
        Utils.showLoginDialogForResult(ShlokSutraDetailsActivity.this, 1);
    }

    private void loadUiElements() {
        Util.commonKeyboardHide(ShlokSutraDetailsActivity.this);
        rvList = findViewById(R.id.rvList);
        tvNoRecord = findViewById(R.id.tvNoRecord);
        llKeywordCount = findViewById(R.id.llKeywordCount);
        tvKeywordCount = findViewById(R.id.tvKeywordCount);
        llExport = (LinearLayout) findViewById(R.id.llExport);
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

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            strSutraName = intent.getExtras().getString("shlokSutraName");
            strShlokGranthName = intent.getExtras().getString("shlokGranthName");
            strGSID = intent.getExtras().getString("gsid");
            strShlokGranthId = intent.getExtras().getString("shlokGranthId");
            //tvPageName.setText(strShlokGranthName + " " + strSutraName);
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(strShlokGranthName + " ");
        ssb.setSpan(new ImageSpan(ShlokSutraDetailsActivity.this, R.drawable.ic_baseline_chevron_right_24),
                ssb.length() - 1,
                ssb.length(),
                0);
        ssb.append(strSutraName);
        tvPageName.setText(ssb);
        callShlokBookApi();
    }

    private void setBookData
            (ArrayList<BookListResModel.BookDetailsModel> mSlokSutraDetailsList) {
        if (mSlokSutraDetailsList == null || mSlokSutraDetailsList.size() == 0) {
            ivHeaderIcon.setVisibility(View.INVISIBLE);
            tvHeaderCount.setVisibility(View.INVISIBLE);
            return;
        }
        mReferenceBookList = mSlokSutraDetailsList;

        /*ivHeaderIcon.setVisibility(View.VISIBLE);
        tvHeaderCount.setVisibility(View.VISIBLE);
        tvHeaderCount.setText("" + mSlokSutraDetailsList.size());
*/
        if (mSlokSutraDetailsList != null && mSlokSutraDetailsList.size() > 0) {
            llKeywordCount.setVisibility(View.VISIBLE);
            tvKeywordCount.setVisibility(View.VISIBLE);
            tvKeywordCount.setText("Select Shlok No.");
        }
        strCount = "" + mSlokSutraDetailsList.size();
        tvKeywordCount.setText("" + mSlokSutraDetailsList.size() + " Reference Address");

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(ShlokSutraDetailsActivity.this));
        rvList.setVisibility(View.VISIBLE);
        mSearchAdapter = new ShlokSutraDetailsListAdapter(ShlokSutraDetailsActivity.this, mSlokSutraDetailsList, this, strShlokGranthName, strSutraName, strGSID);
        rvList.setAdapter(mSearchAdapter);
    }


    public void callShlokBookApi() {
        mSlokSutraDetailsList = new ArrayList<>();
        if (!ConnectionManager.checkInternetConnection(ShlokSutraDetailsActivity.this)) {
            Utils.showInfoDialog(ShlokSutraDetailsActivity.this, "Please check internet connection");
            return;
        }
        strUID = SharedPrefManager.getInstance(ShlokSutraDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        Log.e("UserId --->>",""+strUID);
        if(strUID == null){
            strUID = "0";
        }

        Utils.showProgressDialog(ShlokSutraDetailsActivity.this, "Please Wait...", false);
        ApiClient.getShlokBooks(strUID, strGSID, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<BookListResModel.BookDetailsModel> bookDetailsModels = new ArrayList<>();
                        mSlokSutraDetailsList = response.body().getData();
                        bookDetailsModels = response.body().getData();
                        if (bookDetailsModels != null && bookDetailsModels.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);
                            setBookData(bookDetailsModels);
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                            rvList.setVisibility(View.GONE);
                        }
                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        rvList.setVisibility(View.GONE);
                        // Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
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
    public void onDetailsClick(View view, BookListResModel.BookDetailsModel granthList, int position) {
        String s = view.getTag().toString();
        String[] str = s.split(",");
        String str1 = str[0];
        Log.e("str1", str1);
        String str2 = str[1];
        Log.e("str2", str2);
        String str3 = str[2];
        Log.e("str3", str3);
        Log.e("stringAll", str.toString());

        Intent i = new Intent(ShlokSutraDetailsActivity.this, ReferencePageActivity.class);
        i.putExtra("pageno", str[2]);
        i.putExtra("pdfpage", str[0]);
        i.putExtra("bookid", str[1]);
        i.putExtra("keywordid", str[2]);
        i.putExtra("pagemodel", granthList.getPageModels());
        i.putExtra("model", granthList);
        i.putExtra("moduleNo", Utils.TYPE_SHLOK_PAGE);
        i.putExtra("type_id", "1");
        i.putExtra("strGSID", strGSID);
        startActivity(i);
    }


    public void onZoomClick(String strImageUrl, String fallbackImage) {
        Intent i = new Intent(ShlokSutraDetailsActivity.this, ZoomImageActivity.class);
        i.putExtra("image", strImageUrl);
        i.putExtra("fallbackImage", fallbackImage);
        i.putExtra("url", true);
        startActivity(i);
    }
}

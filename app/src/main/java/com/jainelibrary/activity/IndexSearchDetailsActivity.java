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
import android.os.StrictMode;
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
import com.jainelibrary.adapter.IndexSearchDetailsListAdapter;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jainelibrary.utils.Utils.REF_TYPE_REFERENCE_PAGE;

public class IndexSearchDetailsActivity extends AppCompatActivity
        implements IndexSearchDetailsListAdapter.SearchInterfaceListener {

    private RecyclerView rvList;
    private ArrayList<BookListResModel.BookDetailsModel> bookDetailsModels = new ArrayList<>();
    private ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookPageModels = new ArrayList<>();
    private IndexSearchDetailsListAdapter mIndexSearchDetailsListAdapter;
    private int selected = 1;
    private String strIndexWordName, strBookId, strIndexId, strIndexName;
    TextView tvNoRecord;
    private ImageView ivHeaderIcon;
    private TextView tvHeaderCount;
    private LinearLayout llExport;
    TextView tvContBook;
    public String TAG = "IndexSearchDetailsActivity";
    private ArrayList<BookListResModel.BookDetailsModel> mReferenceBookList;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private BottomSheetDialog bottomSheetDialog;
    private String strUsername, strUID;
    private String PackageName,  strTypeRef;
    String strUserId, shareText, strBookName, strPdfLink;
    Activity activity;
    private String strTotalCount = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_search_details);
        Log.e(TAG, "ABC");
        PackageName = IndexSearchDetailsActivity.this.getPackageName();

        Intent i = getIntent();
        strBookName = i.getStringExtra("IndexBookName");
        strBookId = i.getStringExtra("BookId");
        strIndexId = i.getStringExtra("IndexBookId");
        strIndexName = i.getStringExtra("IndexName");
        Log.e("strId--", "index--" + strBookId);
        Log.e("strName--", "strName--" + strBookName);
        Log.e("strIndexId--", "strIndexId--" + strIndexId);
        Log.e("strIndexName--", "strIndexName--" + strIndexName);
        setHeader();
        loadUiElements();

        if(strIndexId != null && strIndexId.length() > 0){
            callBookIndexApi(strIndexId);
        }

        declaration();
        setHeader();
        loadUiElements();
        onClickLiseners();



    }

    private void declaration() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(IndexSearchDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);
        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
            strUID = userDetailsModel.getId();
        }
    }

    private void callBookIndexApi(String strIndexId) {
        if (!ConnectionManager.checkInternetConnection(IndexSearchDetailsActivity.this)) {
            Utils.showInfoDialog(IndexSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(IndexSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.getBookIndex(strIndexId, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        bookDetailsModels = new ArrayList<>();
                        bookDetailsModels = response.body().getData();
                        if (bookDetailsModels != null && bookDetailsModels.size() > 0) {
                            strTotalCount = String.valueOf(bookDetailsModels.size());
                            setWordData(bookDetailsModels);
                            rvList.setVisibility(View.VISIBLE);
                        }
                       /* bookDetailsModels = response.body().getData();

                        if (bookDetailsModels != null && bookDetailsModels.size() > 0) {
                            //bookPageModels = bookDetailsModels.get(0).getPageModels();
                            setWordData(bookDetailsModels);
                        }*/
                    } else {
                        tvHeaderCount.setVisibility(View.VISIBLE);
                        Utils.showInfoDialog(IndexSearchDetailsActivity.this, response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<BookListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void onClickLiseners() {
        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(IndexSearchDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    getShareDialog(strIndexId);
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
                            }
                        }

                    } else {
                        //InfoDialog("Please search any keywords");
                    }*/
                } else {
                    askLogin();
                }
            }
        });
    }

    public void getShareDialog(String strIndexId) {
        bottomSheetDialog = new BottomSheetDialog(IndexSearchDetailsActivity.this, R.style.BottomSheetDialogTheme);
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
        String strLanguage = SharedPrefManager.getInstance(IndexSearchDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.setFocusable(true);
        strUserId = SharedPrefManager.getInstance(IndexSearchDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        String strEdtRenamefile = mReferenceBookList.size() + "_Reference_Addresses_For_" + "'" + strIndexName + "'" + "_Index";
        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(IndexSearchDetailsActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(IndexSearchDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, IndexSearchDetailsActivity.this, strLanguage, bottomSheetDialog, null);
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
//                shareText = strEdtRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
//                Constantss.FILE_NAME = strEdtRenamefile;
//                Constantss.FILE_NAME_PDF = "JainRefLibrary / " + strIndexWordName + " / ";
////                strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), Constantss.FILE_NAME, mReferenceStringList);
//                callShareMyShelfsApi(strUserId, shareText, strNewPDFile);
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                if(Integer.valueOf(strTotalCount) > 0) {
                    saveBookIndexFile(strIndexId, strUID, strEdtRenamefile, strTotalCount, true);
                }
            }
        });
//        btnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog.cancel();
//                String strEdtRenamefile = edtRenameFile.getText().toString();
//                String strNewPDFile = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strEdtRenamefile + ".pdf";
//                new File(strPdfFile).renameTo(new File(strNewPDFile));
//                Constantss.FILE_NAME = strEdtRenamefile;
//                Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strIndexWordName + " /";
////                strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
//                callDownloadMyShelfsApi(strUserId, strNewPDFile);
//
//            }
//        });
        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                if(Integer.valueOf(strTotalCount) > 0) {
                    saveBookIndexFile(strIndexId, strUID, strEdtRenamefile, strTotalCount, false);
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

    private void saveBookIndexFile(String strIndexId, String strUId, String strEdtRenamefile, String totalKeywordCount, boolean isShare) {
        if (!ConnectionManager.checkInternetConnection(IndexSearchDetailsActivity.this)) {
            Utils.showInfoDialog(IndexSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(IndexSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.checkMyShelfFileName(strUId, strEdtRenamefile, new Callback<CheckMyShelfFileNameResModel>() {
            @Override
            public void onResponse(Call<CheckMyShelfFileNameResModel> call, retrofit2.Response<CheckMyShelfFileNameResModel> response) {
                if (!response.isSuccessful()  ) {
                    Utils.dismissProgressDialog();
                    Utils.showInfoDialog(IndexSearchDetailsActivity.this, "Please try again!");
                    return;
                }

                if(!response.body().isStatus()) {
                    Utils.dismissProgressDialog();
                    Dialog dialog = new IosDialog.Builder(IndexSearchDetailsActivity.this)
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
                                    callCreateBookIndexPdf(strIndexId, strUId, strEdtRenamefile, totalKeywordCount, isShare);
                                }
                            }).build();

                    dialog.show();
                }
                else
                {
                    callCreateBookIndexPdf(strIndexId, strUId, strEdtRenamefile, totalKeywordCount, isShare);
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

    private void callCreateBookIndexPdf(String strIndexId, String strUId, String strEdtRenamefile, String totalKeywordCount, boolean isShare)
    {
        Utils.showProgressDialog(IndexSearchDetailsActivity.this, "Please Wait...", false);
        Log.e("responseData Req", strIndexId);
        ApiClient.createBookIndexPdf(strIndexId, new Callback<CreatePdfFileUrlResModel>() {
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
                            Utils.showInfoDialog(IndexSearchDetailsActivity.this, "KeywordData not saved");
                        }
                    }else {
                        Utils.showInfoDialog(IndexSearchDetailsActivity.this, "KeywordData not saved");
                    }

                } else {
                    Utils.showInfoDialog(IndexSearchDetailsActivity.this, "KeywordData not saved");
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
   /* private void callAddMyShelfApi(JSONArray myShelfArray, String strUserId,String filename) {
        Utils.showProgressDialog(IndexSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.addMyShelf(strUserId,strBookId, strIndexId, "3", Utils.REF_TYPE_REFERENCE_PAGE, filename,     myShelfArray,
                new Callback<AddShelfResModel>() {
                    @Override
                    public void onResponse(Call<AddShelfResModel> call, retrofit2.Response<AddShelfResModel> response) {
                        Utils.dismissProgressDialog();
                        if (response.isSuccessful()) {
                            if (response.body().isStatus()) {
                                String strNotes = response.body().getNotes();
                                String strType = response.body().getType();
                                String strTypeRef = response.body().getType_ref();
                                String strKeywordId = response.body().getType_id();
                                Toast.makeText(IndexSearchDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                getInfoDialogs(strNotes, strType, strTypeRef, "Do you want to add notes?", strKeywordId);
                            } else {
                                Toast.makeText(IndexSearchDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("error--", "ResultError--" + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                        Utils.dismissProgressDialog();
                        Log.e("error", "" + t.getMessage());
                    }
                });
    }*/

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
                        Intent intent = new Intent(IndexSearchDetailsActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strIndex", strIndexWordName);
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

    private String getReferenceName(BookListResModel.BookDetailsModel bookDetailsModel) {
        String strReferenceName = "";
        String strBookname = strBookName;
        //String strPageNo = bookDetailsModel.getPage_no();
        /*String strPageNo = bookDetailsModel.get(0).getPageModels();
        String strPdfPageNo = bookDetailsModel.getPdf_page_no();
        String strAutherName = bookDetailsModel.getAuthor_name();
        String strTranslator = bookDetailsModel.getTranslator();
        String strEditorName = bookDetailsModel.getEditor_name();*/
        if (strBookname != null && strBookname.length() > 0) {
            strReferenceName = strBookname;
        } else {
            strBookname = "";
        }
       /* if (strPageNo != null && strPageNo.length() > 0) {
            strPageNo = ", " + "P. " + strPageNo;
            strReferenceName = strBookname + strPageNo;
        } else {
            strPageNo = "";
        }
        if (strAutherName != null && strAutherName.length() > 0) {
            strAutherName = ", " + strAutherName *//*+ "[" + "ले" + "]"*//*;
            strReferenceName = strBookname + strPageNo + strAutherName;
        } else {
            strAutherName = "";
        }
        if (strTranslator != null && strTranslator.length() > 0) {
            strTranslator = ", " + strTranslator*//* + "[" + "अनु" + "]"*//*;
            strReferenceName = strBookname + strPageNo + strAutherName + strTranslator;
        } else {
            strTranslator = "";
        }
        if (strEditorName != null && strEditorName.length() > 0) {
            strEditorName = ", " + strEditorName *//*+ "[" + "संपा" + "]"*//*;
            strReferenceName = strBookname + strPageNo + strAutherName + strTranslator + strEditorName;
        } else {
            strEditorName = "";
        }*/
        return strReferenceName;
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(IndexSearchDetailsActivity.this, 1);
    }

    private void loadUiElements() {
        rvList = findViewById(R.id.rvList);
        tvNoRecord = findViewById(R.id.tvNoRecord);
        tvContBook = findViewById(R.id.tvKeywordCount);
        llExport = findViewById(R.id.llExport);

    }

    // set header
    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        ivHeaderIcon.setVisibility(View.INVISIBLE);
        tvHeaderCount.setVisibility(View.INVISIBLE);
        ivHeaderIcon.setImageResource(R.mipmap.index_start_word);
        llAddItem.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText(strIndexName);
        /*Intent intent = getIntent();
        if (intent.getExtras() != null) {
            strIndexId = intent.getExtras().getString("indexId");
            strIndexWordName = intent.getExtras().getString("indexName");
            strBookId = intent.getExtras().getString("bookId");
            tvPageName.setText(strIndexWordName);
        }*/
        if (strIndexId != null && strIndexId.length() > 0) {
            //callIndexBookApi();
        }
    }

    private void setWordData(ArrayList<BookListResModel.BookDetailsModel> mBookDataList) {
        Log.e("setdata", "");
        if (mBookDataList == null || mBookDataList.size() == 0) {
            ivHeaderIcon.setVisibility(View.INVISIBLE);
            tvHeaderCount.setVisibility(View.INVISIBLE);
            return;
        }
        mReferenceBookList = mBookDataList;
        Log.e("mReferenceBookList", ""+mReferenceBookList.size());
        ivHeaderIcon.setVisibility(View.VISIBLE);
        tvHeaderCount.setVisibility(View.VISIBLE);
        tvHeaderCount.setVisibility(View.GONE);
        //tvContBook.setText("" + mBookDataList.size() + "  Books Found");
        rvList.setVisibility(View.VISIBLE);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(IndexSearchDetailsActivity.this));
        rvList.setVisibility(View.VISIBLE);
        mIndexSearchDetailsListAdapter = new IndexSearchDetailsListAdapter(IndexSearchDetailsActivity.this, mBookDataList, this, strIndexWordName, strIndexId);
        rvList.setAdapter(mIndexSearchDetailsListAdapter);


    }

    @Override

    public void onDetailsClick(View view, BookListResModel.BookDetailsModel indexListModel, String page_no, int position) {
        String s = view.getTag().toString();
        String[] str = s.split(",");
        String strPdfPageNo = str[0];
        Log.e("str1", strPdfPageNo);
        String strPageNo = str[1];
        Log.e("str2", strPageNo);
        /*String str3 = str[2];
        Log.e("str3", str3);*/
        Log.e("stringAll", str.toString());
        String strIndexName = indexListModel.getName();
        String strIndexIniId = indexListModel.getId();
        /*ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookPageModels = indexListModel.getPageModels();
        String strPageNo = bookPageModels.get(position).getPage_no();*/
        Log.e("strIndexName", "strIndexName--" + strIndexName);
        Log.e("strIndexIniId", "strIndexIniId -- " + strIndexIniId);

        Intent i = new Intent(IndexSearchDetailsActivity.this, ReferencePageActivity.class);
       /* i.putExtra("pageno", str[2]);
        i.putExtra("pdfpage", str[0]);
        i.putExtra("bookid", str[1]);
        i.putExtra("keywordid", str[2]);*/
        i.putExtra("from","IndexSearchDetailsActivity");
        i.putExtra("pageno", strPageNo);
        i.putExtra("pdfpage", strPdfPageNo);
        i.putExtra("bookid", strBookId);
        i.putExtra("bookname", strBookName);
        i.putExtra("keywordid", "");
        i.putExtra("indexname", strIndexName);
        i.putExtra("indexId", strIndexId);
        i.putExtra("indexIniId", strIndexIniId);
        //i.putExtra("model", indexListModel);
        i.putExtra("pagemodel", indexListModel.getPageModels());
        i.putExtra("moduleNo", Utils.TYPE_INDEX_PAGE);
        i.putExtra("type_id", "2");
        startActivity(i);
    }

    // callindexbook API
    /*public void callIndexBookApi() {
        if (!ConnectionManager.checkInternetConnection(IndexSearchDetailsActivity.this)) {
            Toast.makeText(IndexSearchDetailsActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(IndexSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.getIndexBookWithPage(strIndexId, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                Log.e("setdata", response.isSuccessful() + "");

                if (response.isSuccessful()) {

                     *//*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*//*

                    if (response.body().isStatus()) {
                        bookDetailsModels = new ArrayList<>();
                        bookDetailsModels = response.body().getData();
                        if (bookDetailsModels != null && bookDetailsModels.size() > 0) {
                            setWordData(bookDetailsModels);
                            rvList.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvHeaderCount.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
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


    }*/

    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(IndexSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(IndexSearchDetailsActivity.this, "Please Wait...", false);

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
                                Toast.makeText(IndexSearchDetailsActivity.this, "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("download--", "file not exits" + strPdfFile);
                            }
                        }*/
                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            Utils.downloadLocalPDF(strPdfFile, IndexSearchDetailsActivity.this);
                        }
                        else {
                            Utils.showInfoDialog(IndexSearchDetailsActivity.this, "Pdf not found");
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
            Utils.showInfoDialog(IndexSearchDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(IndexSearchDetailsActivity.this, "Please Wait...", false);

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
                                Utils.shareContentWithImage(IndexSearchDetailsActivity.this, "JRL Book Index Data PDF", strMessage, strPdfImage);
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


    private void callAddMyShelfApi(String fileUrl, String strUId, String strEdtRenamefile, String strTotalCount, boolean isShare) {
        String type =  "2";
        String typeref = REF_TYPE_REFERENCE_PAGE;
        String fileType = "3";
        Utils.showProgressDialog(IndexSearchDetailsActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfsWithUrl(strUId, strBookId, strIndexId, type, typeref, strEdtRenamefile, null, strTotalCount, fileType, fileUrl, new Callback<AddShelfResModel>() {
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
                            Utils.showInfoDialog(IndexSearchDetailsActivity.this, "Index added in My Reference.");
                        }
                    } else {
                        Utils.showInfoDialog(IndexSearchDetailsActivity.this, "" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(IndexSearchDetailsActivity.this, "Something went wrong please try again later");
            }
        });
    }


}

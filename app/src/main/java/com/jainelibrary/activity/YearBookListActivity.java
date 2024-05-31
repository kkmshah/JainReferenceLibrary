package com.jainelibrary.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.jainelibrary.adapter.YearBookListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

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

public class YearBookListActivity extends AppCompatActivity implements YearBookListAdapter.BookClickListener{

    ArrayList<BookListResModel.BookDetailsModel> mYearBookModel = new ArrayList<>();
    ArrayList<BookListResModel.BookDetailsModel.BookPageModel> mBookModel = new ArrayList<>();
    BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
    RecyclerView rvYearBook;
    String strYearTypeId, strYearId, strYearValue, strBookName, strTypeRef, strUID, shareText, strUsername, strUserDetails, PackageName, strTypeId, strPdfLink;
    LinearLayout llExplore;
    private YearBookListAdapter mSearchListAdapter;
    private static final int REFERENCE_CODE = 1;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private BottomSheetDialog bottomSheetDialog;
    String strBookIds = "", strTotalCount = "", strYearTypeName, strReferenceCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_book);

        PackageName = YearBookListActivity.this.getPackageName();

        initComponent();
        setHeader();
        declaration();



        /*ArrayList<YearBookResModel.YearBookList> yearBookLists = new ArrayList<>();
        yearBookLists.set(0).*/
    }

    private void declaration() {
        Intent i = getIntent();
        mYearBookModel = (ArrayList<BookListResModel.BookDetailsModel>)i.getSerializableExtra("YearBookList");
        strTotalCount = String.valueOf(mYearBookModel.size());
        strYearTypeId = i.getStringExtra("YearType");
        strYearId = i.getStringExtra("YearId");
        strYearValue = i.getStringExtra("Year");
        strTypeId  = i.getStringExtra("strTypeId");
        strBookIds = i.getStringExtra("BookIds");
        strYearTypeName = i.getStringExtra("YearTypeName");
        strReferenceCount = i.getStringExtra("YearRefCount");
        Log.e("BookList",  mYearBookModel.get(0).getName());

        strBookName = mYearBookModel.get(0).getName();

        mBookModel = mYearBookModel.get(0).getBooks();

        setYearBookList(mYearBookModel, mBookModel);

        Gson gson = new Gson();
        strUserDetails = SharedPrefManager.getInstance(YearBookListActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);
        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
            strUID = userDetailsModel.getId();
        }

        llExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(YearBookListActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    //callBookDetailsPdfApi(strKeyId,strBookIds);
                    getYearBookPdf(strYearTypeId, strYearValue);
                } else {
                    askLogin();
                }

            }
        });
    }

    private void setYearBookList(ArrayList<BookListResModel.BookDetailsModel> models, ArrayList<BookListResModel.BookDetailsModel.BookPageModel> mBookModel) {
        if (models == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvYearBook.setHasFixedSize(true);
        rvYearBook.setLayoutManager(linearLayoutManager);
        rvYearBook.setVisibility(View.VISIBLE);
        mSearchListAdapter = new YearBookListAdapter(this, models, this, mBookModel);
        rvYearBook.setAdapter(mSearchListAdapter);
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
        tvPageName.setText("Year Book List");
    }

    private void initComponent() {
        rvYearBook = findViewById(R.id.rvYearBook);
        llExplore = findViewById(R.id.llExplore);
    }



    private void getYearBookPdf(String strYearTypeId, String strYearValue) {
        if (!ConnectionManager.checkInternetConnection(YearBookListActivity.this)) {
            Utils.showInfoDialog(YearBookListActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(YearBookListActivity.this, "Please Wait...", false);
        ApiClient.getYearBookPdf(strYearTypeId, strYearValue, strBookIds, new Callback<ResponseBody>() {
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
                        Utils.showInfoDialog(YearBookListActivity.this, "Pdf data not download");
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
    }


    @Override
    public void onClick(View view, BookListResModel.BookDetailsModel.BookPageModel bookLists) {
        String s = view.getTag().toString();
        String[] str = s.split(",");
        String str1 = str[0];
        Log.e("str1", str1);
        String str2 = str[1];
        Log.e("str2", str2);
        String str3 = str[2];
        Log.e("str3", str3);
        Log.e("stringAll", str.toString());


        BookListResModel.BookDetailsModel model = new BookListResModel.BookDetailsModel();

        model.setBook_id(bookLists.getBook_id());
        model.setPdf_page_no(bookLists.getPdf_page_no());
        model.setPage_no(bookLists.getPage_no());
        model.setBook_name(bookLists.getBook_name());

        //dataList = yearBookLists.get(position).getBooks();
        Intent i = new Intent(YearBookListActivity.this, ReferencePageActivity.class);
        i.putExtra("pageno", str[2]);
        i.putExtra("bookImage", bookLists.getBook_image());
        i.putExtra("pdfpage", str[0]);
        i.putExtra("bookid", bookLists.getBook_id());
        i.putExtra("bookname", bookLists.getBook_name());
        i.putExtra("typeName", bookLists.getType_name());
        i.putExtra("yearId", strYearId);
        i.putExtra("typeValue", strYearValue);
        i.putExtra("YearTypeId", strYearTypeId);
        i.putExtra("ApiTypeId", strTypeId);
        i.putExtra("moduleNo", TYPE_KEYWORD_PAGE);
        i.putExtra("model", model);
        i.putExtra("type_id","3");
        i.putExtra("from","YearBookActivity");
        startActivity(i);
        //int cPosition = Integer.parseInt(childPosition);
        Log.e("YearClick -- ", "theme---" + bookLists.getBook_name());
        /*if (bookLists != null && bookLists.size() > 0) {

        }*/
    }


    private void askLogin() {
        Utils.showLoginDialogForResult(YearBookListActivity.this, REFERENCE_CODE);
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

    public void getShareDialog(String strPdfFile) {
        List<String> mReferenceStringList = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(YearBookListActivity.this, R.style.BottomSheetDialogTheme);
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
        String strLanguage = SharedPrefManager.getInstance(YearBookListActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        strUID = SharedPrefManager.getInstance(YearBookListActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        edtRenameFile.setFocusable(true);
        edtRenameFile.requestFocus();
        String strEdtRenamefile = strReferenceCount + "_Reference_Addresses_For_Year_" + "'" + strYearTypeName + "'" + " '" + strYearValue + "'";
        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(YearBookListActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(YearBookListActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, YearBookListActivity.this, strLanguage, bottomSheetDialog, null);
//                return false;
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomSheetDialog.cancel();
//                shareText = strEdtRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
//                Constantss.FILE_NAME = strEdtRenamefile;
//                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + mYearBookModel.size() + " /";
//                String strFilePath = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + edtRenameFile.getText().toString() + ".pdf";
//                new File(strPdfFile).renameTo(new File(strFilePath));
//                callShareMyShelfsApi(strUID, shareText, strFilePath);
                //strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + mYearBookModel.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + mYearBookModel.size() + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    callAddMyShelfApi(strUID, mReferenceStringList, mFile,strEdtRenamefile, true);
                }

            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                Constantss.FILE_NAME = strEdtRenamefile;
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + mYearBookModel.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + mYearBookModel.size() + " /";
                /*Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + strBookPageNo + "_" + mBookModel.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + strBookPageNo + "_" + mYearBookModel.size() + " /";*/
                String strFilePath = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + edtRenameFile.getText().toString() + ".pdf";
                new File(strPdfFile).renameTo(new File(strFilePath));
                callDownloadMyShelfsApi(strUID, strFilePath);
                // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordRefDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);

            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + mYearBookModel.size() + " /";
                Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + mYearBookModel.size() + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                // String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mReferenceStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    callAddMyShelfApi(strUID, mReferenceStringList, mFile,strEdtRenamefile, false);
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

    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(YearBookListActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(YearBookListActivity.this, "Please Wait...", false);

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
                                Toast.makeText(YearBookListActivity.this, "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("download--", "file not exits" + strPdfFile);
                            }
                        }*/
                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            Utils.downloadLocalPDF(strPdfFile, YearBookListActivity.this);
                        }
                        else {
                            Utils.showInfoDialog(YearBookListActivity.this, "Pdf not found");
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
            Utils.showInfoDialog(YearBookListActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(YearBookListActivity.this, "Please Wait...", false);

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
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "3" + strYearTypeId);
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), "1");
        RequestBody typeId = RequestBody.create(MediaType.parse("text/*"), strYearId);
        RequestBody count = RequestBody.create(MediaType.parse("text/*"), strReferenceCount);
        RequestBody fileType = RequestBody.create(MediaType.parse("text/*"), "3");
        Log.e("fileType :", " "+fileType);
        Utils.showProgressDialog(YearBookListActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfs(uid, null, typeId, type, typeref, filename, null, count, fileType, filePart, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    Utils.dismissProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().isStatus()) {
                            //Toast.makeText(getApplicationContext(), strYearValue + "Year's books added in My Reference.", Toast.LENGTH_LONG).show();
                            strPdfLink = response.body().getPdf_url();
                            if (isShare) {
                                callShareMyShelfsApi(strUserId, shareText, "");
                            }
                            else {
                                Utils.showInfoDialog(YearBookListActivity.this, "Data added in My Reference.");
                            }
                        } else {
                            Utils.showInfoDialog(YearBookListActivity.this, response.body().getMessage());
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
                Utils.showInfoDialog(YearBookListActivity.this, "Something went wrong please try again later");

            }
        });

    }

    public void share(String shareData, String mFilePath) {
        File mFile = new File(mFilePath);
        Uri fileUri = Uri.fromFile(mFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            fileUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", mFile);
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
}
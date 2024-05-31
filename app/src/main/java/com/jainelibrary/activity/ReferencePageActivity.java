package com.jainelibrary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.BuildConfig;
import com.jainelibrary.Constantss;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.adapter.ReferencePageAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddMyShelfModel;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.BooksDataModel;
import com.jainelibrary.model.HoldAndSearchResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.FileUtils;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.jainelibrary.utils.ZipCompressor;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.viven.imagezoom.ImageZoomHelper;
import com.wc.widget.dialog.IosDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferencePageActivity extends AppCompatActivity implements ReferencePageAdapter.PdfInterfaceListener {

    private static final int SEND_HOLD_DATA = 0;
    private static final int REFERENCE_CODE = 1;
    private static final String TAG = ReferencePageActivity.class.getSimpleName();

    private static final int total_pages = 4;
    private static final int first_index_gap = 2;
    private static final int last_index_gap = 3;

    public int pageNoFirstIndex, pageNoLastIndex, pageNo;
    boolean isAppendix = false;
    JRL myApplication;
    BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
    ArrayList<BooksDataModel.BookImageDetailsModel> mReferencePageBook = new ArrayList<>();
    ArrayList<BookListResModel.BookDetailsModel> mBookDetailsList = new ArrayList<>();
    ArrayList<BookListResModel.BookDetailsModel> mSelectedBookDetailsList = new ArrayList<>();
    ArrayList<HashMap<String, Uri>> imageFiles = new ArrayList<HashMap<String, Uri>>();
    View viewQuickLInk;
    Button btnMyShelf;
    ArrayList<Uri> mImageUriList = new ArrayList<>();
    Keyboard mKeyboard;
    CustomKeyboardView mKeyboardView;
    ImageZoomHelper imageZoomHelper;
    ArrayList<AddMyShelfModel> addMyShlefList = new ArrayList<>();
    boolean isImageSelected = false;
    Handler handler;
    int count = 0;
    Activity activity;
    String strEdtRenamefile = null;
    Boolean isOKClick = false;
    private LinearLayout ll_buttons, llShare, llBookDetails, llQuickLink;
    private TextView buttonFilter;
    private String strAppendixId, strHeaderPageNo, strUserID, strWordName, strBookName, strBookId, strKeyWordId, strEditorName, strTranslatorName, strAuthorName, strHoldPageNo, strPageNo, strPdfPageNo, strSutraName,
            strTypeName, strGSId, strYearId, strTypeValue, strRefrenceTypeId, strBookUrl, strFrom, strApiTypeId, strIndexName, strIndexId, strIndexIniId, strPdfLink;
    private String PackageName;
    private XRecyclerView rvPdf;
    private ReferencePageAdapter mReferencePageAdapter;
    private LinearLayout llBookTitle, llHold, llFeedback, llTotal, llSelectOption, llPdfOption;
    private String strPageName;
    private String strPageCount;
    private TextView tvSelectedImageCount, tvInfo, tvQuickLink, tvFooterPageName, bookDetailsPages;
    private ImageView ivQuickLink;
    private boolean isFirstTime = false;
    private boolean isLogin = false;
    private ArrayList<BooksDataModel.BookImageDetailsModel> mSelectedPdfPageList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private String strType, strNotes, strTypeRef;
    private boolean isHighlight = true;
    private String strPdfFile;
    private String strTotalCount, strTypeId, strYearTypeId = "";

    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_page);
        activity = this;
        Log.e(TAG, "ABC");
        myApplication = (JRL) getApplicationContext();
        strUserID = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_USER_ID);
        loadUiElements();
        declaration();
        setHeader();
        onBtnEventListener();
    }

    private void onBtnEventListener() {
        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llPdfOption.setVisibility(View.VISIBLE);
                llSelectOption.setVisibility(View.GONE);
                isOKClick = true;
                showExportDialog(true);
            }
        });

        llBookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (strFrom != null && strFrom.length() > 0) {
                    BookListResModel.BookDetailsModel model = new BookListResModel.BookDetailsModel();
                    if (strFrom.equals("YearBookActivity")) {
                        model.setBook_id(strBookId);
                        model.setBook_name(strBookName);
                        model.setKeywordId(strApiTypeId);
                        model.setKeyword(strTypeValue);
                        model.setBook_url(strBookUrl);
                        Intent i = new Intent(activity, BookDetailsActivity.class);
                        i.putExtra("model", model);
                        i.putExtra("From", "Year");
                        startActivity(i);
                    } else {
                        model.setBook_id(strBookId);
                        model.setBook_name(strBookName);
                        model.setKeywordId(strIndexId);
                        model.setKeyword(strIndexName);
                        model.setBook_url(strBookUrl);
                        Intent i = new Intent(activity, BookDetailsActivity.class);
                        i.putExtra("model", model);
                        i.putExtra("From", "Index");
                        startActivity(i);
                    }


                } else {
                    Intent i = new Intent(activity, BookDetailsActivity.class);
                    i.putExtra("model", mBookDataModels);
                    startActivity(i);
                }


            }
        });
        llQuickLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(activity).setBooleanPreference("CustomBackPressed", true);
                onBackPressed();
            }
        });
        tvFooterPageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOKClick) {
                    showExportDialog(false);
                } else {
                    isOKClick = false;
                }
            }
        });
        llHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(activity).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    String strUserId = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_USER_ID);

                    if (strFrom != null && strFrom.equals("IndexSearchDetailsActivity")) {
                        callAddHoldSearchKeyword(strUserId, strBookId, strIndexId, Utils.TYPE_INDEX_PAGE + strIndexIniId);
                    } else if (strFrom != null && strFrom.equals("YearBookActivity")) {
                        callAddHoldSearchKeyword(strUserId, strBookId, strYearId, Utils.TYPE_YEAR_PAGE + strYearTypeId);
                    } else if (strFrom != null && strFrom.equals("BiodataMemoryDetailsActivity")) {
                        callAddHoldSearchKeyword(strUserId, strBookId, strRefrenceTypeId, Utils.TYPE_BIODATA_BOOK_REFERENCE);
                    } else if (strFrom != null && strFrom.equals("RelationDetailsActivity")) {
                        callAddHoldSearchKeyword(strUserId, strBookId, strRefrenceTypeId, Utils.TYPE_RELATION_BOOK_REFERENCE);
                    } else {
                        /*if (strGSId != null)
                            callAddHoldSearchKeyword(strUserId, strBookId, strGSId, Utils.TYPE_SHLOK_PAGE);
                        else
                            callAddHoldSearchKeyword(strUserId, strBookId, strKeyWordId, Utils.TYPE_KEYWORD_PAGE);*/
                        String strBookID = mBookDataModels.getBook_id();
                        String strKeyword = null;
                        if (mBookDataModels.getKeyword() != null && mBookDataModels.getKeyword().length() > 0) {
                            strKeyword = mBookDataModels.getKeyword();
                        }

                        Log.e("HoldGSID", "strGSId --" + strGSId);
                        Log.e("strUserId", strUserId + " strKeyword" + strKeyword + "strSutraName" + strSutraName + " strBookID" + strBookID);
                        if (strUserId != null && strBookID != null) {
                            if (strSutraName != null)
                                callAddHoldSearchKeyword(strUserId, strBookID, strGSId, Utils.TYPE_SHLOK_PAGE);
                            else if (isAppendix)
                                callAddHoldSearchKeyword(strUserId, strBookID, strAppendixId, "4");
                            else if (strKeyword != null)
                                callAddHoldSearchKeyword(strUserId, strBookId, strKeyWordId, Utils.TYPE_KEYWORD_PAGE);
                        }
                    }

                } else {
                    askLogin();
                }
                //     setBaseClassData();
            }
        });
        llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(activity).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    String strUserId = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_USER_ID);
                    String book_id = "";
                    String book_name = "";
                    String type = "";
                    String type_id = "";

                    if (strFrom != null && strFrom.equals("IndexSearchDetailsActivity")) {
                        book_id = strBookId;
                        book_name = strBookName;
                        type = Utils.TYPE_INDEX_PAGE + strIndexIniId;
                        type_id = strIndexId;
                    } else if (strFrom != null && strFrom.equals("YearBookActivity")) {
                        book_id = strBookId;
                        book_name = strBookName;
                        type = Utils.TYPE_YEAR_PAGE + strYearTypeId;
                        type_id = strYearId;

                    } else if (strFrom != null && strFrom.equals("BiodataMemoryDetailsActivity")) {
                        book_id = strBookId;
                        book_name = strBookName;
                        type = Utils.TYPE_BIODATA_BOOK_REFERENCE;
                        type_id = strRefrenceTypeId;
                    } else if (strFrom != null && strFrom.equals("RelationDetailsActivity")) {
                        book_id = strBookId;
                        book_name = strBookName;
                        type = Utils.TYPE_RELATION_BOOK_REFERENCE;
                        type_id = strRefrenceTypeId;
                    } else {
                        book_id = mBookDataModels.getBook_id();
                        book_name = mBookDataModels.getBook_name();
                        String strKeyword = null;
                        if (mBookDataModels.getKeyword() != null && mBookDataModels.getKeyword().length() > 0) {
                            strKeyword = mBookDataModels.getKeyword();
                        }

                        if (strUserId != null && book_id != null) {
                            if (strSutraName != null) {
                                type = Utils.TYPE_SHLOK_PAGE;
                                type_id = strGSId;
                            } else if (isAppendix) {
                                type = "4";
                                type_id = strAppendixId;
                            } else if (strKeyword != null) {
                                type = Utils.TYPE_KEYWORD_PAGE;
                                type_id = strKeyWordId;
                            }
                        }
                    }

                    Intent i = new Intent(ReferencePageActivity.this, FeedbackActivity.class);
                    i.putExtra("strBookName", book_name);
                    i.putExtra("strBookId", book_id);
                    i.putExtra("strType", type);
                    i.putExtra("strTypeId", type_id);
                    i.putExtra("strPageNo", strPageNo);
                    i.putExtra("strPdfPageNo", strPdfPageNo);
                    startActivity(i);

                } else {
                    askLogin();
                }
                //     setBaseClassData();
            }
        });
    }

    private void showExportDialog(boolean isOk) {
        Utils.showProgressDialog(ReferencePageActivity.this, "Please Wait...", false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.dismissProgressDialog();
                boolean isLogin = SharedPrefManager.getInstance(activity).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    // getShareDialog();
                    if (mImageUriList != null && mImageUriList.size() > 0) {
                        mImageUriList.clear();
                        mImageUriList = new ArrayList<>();
                    }

                    if (imageFiles != null && imageFiles.size() > 0) {
                        if (imageFiles != null && imageFiles.size() > 0) {
                            for (int i = 0; i < imageFiles.size(); i++) {
                                HashMap<String, Uri> map = imageFiles.get(i);
                                for (String key : map.keySet()) {
                                    Log.e("(map.get(key)", (map.get(key).toString()));
                                    if (!mImageUriList.contains(map.get(key)))
                                        mImageUriList.add(map.get(key));
                                    Log.e("contains", (map.get(key).toString()));
                                }
                            }
                        }
                        //showShareDialog(v);
                        if (mImageUriList != null && mImageUriList.size() > 0) {
                            //    getShareDialog();
                            if (mImageUriList == null || mImageUriList.size() == 0) {
                                InfoDialogs("Long Press on Image for Select\n" +
                                        "You can select multiple images, please scroll.\n\nSelected Images :" +
                                        mImageUriList.size());
                            } else {
                                getShareDialog();
                            }
                        }
                    } else {

                        if (!isOk) {
                            Log.e(TAG, "setSelectedOptionData exportDialogOk");
                            setSelectedOptionData();
                        }
                        // InfoDialog("Long Press on Image for Select.\nYou can select multiple images, please scroll.");
                    }
                } else {
                    askLogin();
                }
            }
        }, 300);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", dialogClickListener)
                .show();
    }

    private void InfoDialogs(String strMessage) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(strMessage)
                .setCancelable(true)
                .setPositiveButton("Ok", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFERENCE_CODE) {
            if (resultCode == RESULT_OK) {
                isLogin = data.getBooleanExtra("isLogin", false);
            }
        }
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(ReferencePageActivity.this, REFERENCE_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mKeyboardView != null && mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        }
        onBtnEventListener();
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed : " + mImageUriList.size());
        if (mImageUriList != null && mImageUriList.size() > 0) {
            mImageUriList.clear();
        }

        if (mKeyboardView != null && mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void callBookPageApi(String strTypeId, String strBId, int strPId, String strDirection, boolean isRefresh) {
        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check internet connection");
            return;
        }
        String highlight;
        if (isHighlight)
            highlight = "1";
        else
            highlight = "0";

        String strTypeIdValue = "-1";

        strTypeId = strTypeId.toString();

        if (strTypeId.equals(Utils.TYPE_KEYWORD_PAGE)) {
            strTypeIdValue = strKeyWordId;
        } else if (strTypeId.equals(Utils.TYPE_SHLOK_PAGE)) {
            strTypeIdValue = strGSId;
        } else if (strTypeId.equals(Utils.TYPE_INDEX_PAGE)) {
            strTypeIdValue = strIndexId + "-" + strIndexIniId;
        } else if (strTypeId.equals(Utils.TYPE_YEAR_PAGE)) {
            strTypeIdValue = strYearId + "-" + strYearTypeId;
        } else if (strTypeId.equals(Utils.TYPE_BIODATA_BOOK_REFERENCE) && strRefrenceTypeId !=null) {
            strTypeIdValue = strRefrenceTypeId;
        }

        Log.e("ReferenceTypeData", "Type " + strTypeId + " Type Id " + strTypeIdValue + " Page No " + strPId);

        ApiClient.getBookPage(strTypeId, strTypeIdValue, strBId, String.valueOf(strPId), strDirection, strType, highlight, new Callback<BooksDataModel>() {
            @Override
            public void onResponse(Call<BooksDataModel> call, retrofit2.Response<BooksDataModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        BooksDataModel mBookDataMOdel = response.body();
                        Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(mBookDataMOdel));
                        isHighlight = false;
                        if (isFirstTime) {
                            mReferencePageBook.clear();
                            mReferencePageBook.addAll(mBookDataMOdel.getData());
                            setPdfData(mReferencePageBook);
                            isFirstTime = false;

                        } else {
                            int totSize = mBookDataMOdel.getData().size();
                            if (isRefresh) {
                                rvPdf.refreshComplete();
                                if (totSize > 0) {
                                    for (int i = 0; i < totSize; i++) {
                                        mReferencePageBook.add(i, mBookDataMOdel.getData().get(i));
                                    }
//                                    pageNoFirstIndex = (pageNo - mReferencePageBook.size()) + first_index_gap;
//                                    pageNoLastIndex = (pageNo + mReferencePageBook.size()) - last_index_gap;

                                    pageNoFirstIndex = Integer.parseInt(mReferencePageBook.get(0).getPageno());
                                }
                            } else {
                                rvPdf.loadMoreComplete();
                                mReferencePageBook.addAll(mBookDataMOdel.getData());
//                                pageNoFirstIndex = (pageNo - mReferencePageBook.size()) + first_index_gap;
//                                pageNoLastIndex = (pageNo + mReferencePageBook.size()) - last_index_gap;

                                pageNoLastIndex = Integer.parseInt(mReferencePageBook.get(mReferencePageBook.size() - 1).getPageno()) + 1;
                            }
                            mReferencePageAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Utils.showInfoDialog(ReferencePageActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BooksDataModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void setBaseClassData() {
        mBookDetailsList = myApplication.getmBookDetailsList();
        String strTempUniqueName = "";
        if (mBookDataModels != null) {
            //   strTempUniqueName = mBookDataModels.getStrUniqueName();
        }
        try {
            if (mBookDetailsList != null && mBookDetailsList.size() > 0) {
                Log.e("mBookDetailsList---", "mBookDetailsList" + mBookDetailsList.size());
                boolean isMatch = false;
                for (int k = 0; k < mBookDetailsList.size(); k++) {
                    String strUniqueName = null; /*mBookDetailsList.get(k).getStrUniqueName()*/
                    if (strUniqueName != null && strUniqueName.length() > 0) {
                        if (strTempUniqueName.equalsIgnoreCase(strUniqueName)) {
                            isMatch = true;
                        }
                    } else {
                        Log.e("null=---", "strTempUniqueName--" + strUniqueName);
                    }
                }
                if (!isMatch) {
                    mBookDetailsList.add(mBookDataModels);
                    myApplication.setmBookDetailsList(mBookDetailsList);
                }
                Intent i = new Intent(activity, HoldAndSearchActivity.class);
                i.putExtra("model", mBookDataModels);
                startActivity(i);
            } else {
                if (mBookDataModels != null) {
                    myApplication.getmBookDetailsList().add(mBookDataModels);
                    Intent i = new Intent(activity, HoldAndSearchActivity.class);
                    i.putExtra("model", mBookDataModels);
                    startActivity(i);
                }
                Log.e("mBookDetailsListElse---", "mBookDetailsListElse" + mBookDetailsList.size());
            }
        } catch (Exception e) {
            Log.e("ExceptionMessage---", "Message--" + e.getMessage());
        }
    }


    private void declaration() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        PackageName = activity.getPackageName();
        Intent i = getIntent();
        if (i != null) {
            strTypeId = i.getStringExtra("type_id");
            strRefrenceTypeId = i.getStringExtra("refrenceTypeId");
            mBookDataModels = (BookListResModel.BookDetailsModel) i.getSerializableExtra("model");

            if (mBookDataModels != null) {
                Log.e("bookmodel", mBookDataModels.toString());
                Log.e("tempReferenceModel :", "" + new GsonBuilder().setPrettyPrinting().create().toJson(mBookDataModels));
                if (i.getExtras().getString("keywordid") != null) {
                    strWordName = i.getExtras().getString("keywordid");
                }
                //strTotalCount = i.getExtras().getString("strTotalCount");

                if (mBookDataModels.getKeywordId() != null) {
                    strKeyWordId = mBookDataModels.getKeywordId();
                }

                Log.e(TAG, "strKeywordId--" + strKeyWordId);
                Log.e(TAG, "strWordName--" + strWordName);
                if (i.getExtras().getString("keyword") != null) {
                    strWordName = i.getExtras().getString("keyword");
                } else if (mBookDataModels.getKeyword() != null) {
                    Log.e("keyword", mBookDataModels.getKeyword());
                    strWordName = mBookDataModels.getKeyword();
                } else if (mBookDataModels.getIndex_name() != null) {
                    strWordName = mBookDataModels.getIndex_name();
                } else if (mBookDataModels.getYear() != null) {
                    strWordName = mBookDataModels.getYear();
                }
                if (i.getExtras().getString("bookname") != null) {
                    strBookName = i.getExtras().getString("bookname");
                } else {
                    if (!mBookDataModels.getBook_name().equalsIgnoreCase("0"))
                        strBookName = mBookDataModels.getBook_name();
                }
                //Log.e(TAG, "strWordName--" + strWordName);
                //Log.e(TAG, "strBookName--" + strBookName);
                strBookId = mBookDataModels.getBook_id();
                if (i.getExtras().getString("pageno") != null) {
                    strPageNo = i.getExtras().getString("pageno");
                    Log.e("strpageno intent", strPageNo);
                } else {
                    strPageNo = mBookDataModels.getPdf_page_no();
                }
                if (i.getExtras().getString("pdfpage") != null) {
                    strPdfPageNo = i.getExtras().getString("pdfpage");
                    Log.e("strpdfpageno intent", strPdfPageNo);
                } else {
                    strPdfPageNo = mBookDataModels.getPdf_page_no();
                }

                //strPdfPageNo = i.getExtras().getString("pdfpage");
                if (i.getExtras().getString("pageno") != null) {
                    strHeaderPageNo = i.getExtras().getString("pageno");
                    Log.e("strpageno intent", strHeaderPageNo);
                } else {
                    strHeaderPageNo = mBookDataModels.getPage_no();
                }
                strAuthorName = mBookDataModels.getAuthor_name();
                strTranslatorName = mBookDataModels.getTranslator();
                strEditorName = mBookDataModels.getEditor_name();
                strSutraName = mBookDataModels.getStrSutraName();
                strType = mBookDataModels.getFlag();
                mBookDetailsList.add(mBookDataModels);
                Log.e(TAG, "strWordName--" + strWordName);
                Log.e(TAG, "strType--" + strType);

                Log.e("mBookDataModels----", "" + mBookDataModels);
                Log.e("strPageNo---", "strPageNo---" + strPageNo);

                strGSId = i.getExtras().getString("strGSID");
            }

            strFrom = i.getStringExtra("from");
            if (strFrom != null && strFrom.length() > 0) {
                if (strFrom.equals("Hold")) {
                    strBookName = i.getExtras().getString("strBookName");
                    strTypeName = i.getExtras().getString("strTypeName");
                    strHoldPageNo = i.getExtras().getString("strHoldPageNo");
                } else if (strFrom.equals("YearBookActivity")) {
                    strBookName = i.getExtras().getString("bookname");
                    strPageNo = i.getExtras().getString("pageno");
                    strPdfPageNo = i.getExtras().getString("pdfpage");
                    strTypeName = i.getExtras().getString("typeName");
                    strYearId = i.getExtras().getString("yearId");
                    strTypeValue = i.getExtras().getString("typeValue");
                    strApiTypeId = i.getExtras().getString("ApiTypeId");
                    strBookUrl = i.getExtras().getString("bookImage");
                    strBookId = i.getExtras().getString("bookid");
                    strYearTypeId = i.getExtras().getString("YearTypeId");
                    Log.e(TAG, "strBookName--" + strBookName);
                    Log.e(TAG, "strPageNo--" + strPageNo);
                } else if (strFrom.equals("IndexSearchDetailsActivity")) {
                    strBookName = i.getExtras().getString("bookname");
                    strPageNo = i.getExtras().getString("pageno");
                    strTypeName = i.getExtras().getString("typeName");
                    strTypeValue = i.getExtras().getString("typeValue");
                    strBookId = i.getExtras().getString("bookid");
                    strIndexName = i.getExtras().getString("indexname");
                    strIndexId = i.getExtras().getString("indexId");
                    strIndexIniId = i.getExtras().getString("indexIniId");
                    strPdfPageNo = i.getExtras().getString("pdfpage");
                } else {
                    strBookName = i.getExtras().getString("bookname");
                    strPageNo = i.getExtras().getString("pageno");
                    strTypeName = i.getExtras().getString("typeName");
                    strTypeValue = i.getExtras().getString("typeValue");
                    strBookId = i.getExtras().getString("bookid");
                    strKeyWordId = i.getExtras().getString("keywordid");
                    strIndexName = i.getExtras().getString("indexname");
                    strIndexId = i.getExtras().getString("indexId");
                    strPdfPageNo = i.getExtras().getString("pdfpage");
                    Log.e(TAG, "strBookName--" + strBookName);
                    Log.e(TAG, "strPageNo--" + strPageNo);
                }
            }
            /*if (strBookName != null && strBookName.length() > 0) {
                SharedPrefManager.getInstance(activity).saveStringPref(SharedPrefManager.KEY_TITLE_NAME, strBookName);
            }*/
            /*if (strPageNo != null && strPageNo.length() > 0) {
                pageNo = Integer.parseInt(strPageNo);
            }*/

            if (strPdfPageNo != null && strPdfPageNo.length() > 0) {
                pageNo = Integer.parseInt(strPdfPageNo);
            }

            /*if (strWordName != null && strWordName.length() > 0) {
                SharedPrefManager.getInstance(activity).saveStringPref(SharedPrefManager.KEY_PREVIOUSTITLE_NAME, strWordName);
            }*/

            //mBookModels =  (ArrayList<BookListResModel.BookDetailsModel.BookPageModel>) i.getSerializableExtra("bookList");
            isAppendix = i.getBooleanExtra("isAppendix", false);
        }
        if (isAppendix) {
            strTypeId = "4";
            tvFooterPageName.setText(strPageName);
            strPageName = i.getStringExtra("page");
            strPageNo = i.getStringExtra("page_no");
            strPageCount = i.getStringExtra("count");
            strAppendixId = i.getStringExtra("aid");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
            );

            strPdfPageNo = strPageCount;

            if (strPageCount != null && strPageCount.length() > 0) {
                pageNo = Integer.parseInt(strPageCount);
            }
            llBookDetails.setLayoutParams(param);
            llQuickLink.setVisibility(View.VISIBLE);
            viewQuickLInk.setVisibility(View.VISIBLE);
//            tvQuickLink.setText(strPageName);
            String strFlag = i.getStringExtra("flag");
            if (strFlag != null) {
                if (strFlag.equals("5")) {
                    ivQuickLink.setImageResource(R.mipmap.book_store);
                    tvQuickLink.setText("Book Store");
                } else if (strFlag.equals("6")) {
                    ivQuickLink.setImageResource(R.mipmap.my_reference);
                    tvQuickLink.setText("My References");
                } else {
                    ivQuickLink.setImageResource(R.mipmap.file);
                    tvQuickLink.setText("Reference Page");
                }
            } else {
                ivQuickLink.setImageResource(R.mipmap.file);
                tvQuickLink.setText("Reference Page");
            }
        } else {
            llBookDetails.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f);
            llBookDetails.setLayoutParams(param);
            llQuickLink.setVisibility(View.GONE);
            viewQuickLInk.setVisibility(View.GONE);
            Log.e("strPageName---", "strPageName--" + strPageName);
            tvFooterPageName.setText(strPageName);
        }
        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check your internet connection..");
            return;
        }
        isFirstTime = true;
        Log.e("pageNo---", "pageNo---" + pageNo + "strTypeId---" + strTypeId +"strBookId---" + strBookId );
        callBookPageApi(strTypeId, strBookId, pageNo, "0", true);
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        TextView tvPgCount = headerView.findViewById(R.id.tvPgCount);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //    strBookName = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_TITLE_NAME);
        //     strWordName = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_PREVIOUSTITLE_NAME);
        if (isAppendix) {
            if (tvPgCount != null && tvPgCount.length() > 0) {
                tvPgCount.setVisibility(View.VISIBLE);
                tvPgCount.setText(strPageCount);
            }
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append("Book Details ");
            ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                    ssb.length() - 1,
                    ssb.length(),
                    0);
            ssb.append(strPageName);
            tvPageName.setText(ssb);
        } else {
            if (strFrom != null && strFrom.equals("Hold")) {
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                ssb.append(strBookName + " ");
                ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);
                ssb.append(strTypeName + " ");
                ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);
                ssb.append(strHoldPageNo);
                tvPageName.setText(ssb);
            } else if (strSutraName != null && strSutraName.length() > 0) {
                if (!strSutraName.equalsIgnoreCase("0")) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();

                    if (!strWordName.equalsIgnoreCase("0")) {
                        ssb.append(strWordName + " ");
                    }

                    Log.e("strWordName -- ", strWordName);
                    ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                            ssb.length() - 1,
                            ssb.length(),
                            0);
                    Log.e("strSutraName -- ", strSutraName);
                    ssb.append(strSutraName + " ");
                    ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                            ssb.length() - 1,
                            ssb.length(),
                            0);
                    if (!strBookName.equalsIgnoreCase("0")) {
                        ssb.append(strBookName);
                    }
                    //      Log.e("strHeaderPageNo", strHeaderPageNo);
                    ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                            ssb.length() - 1,
                            ssb.length(),
                            0);
                    //   Log.e("strBookName", strBookName);

                    ssb.append("P. " + strHeaderPageNo + " ");
                    Log.e("SSB----", "" + ssb);
                    tvPageName.setText(ssb);
                }
            } else if (strTypeName != null && strTypeName.length() > 0) {
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                //     Log.e("strWordName",strWordName);
                ssb.append(strTypeName + " ");
                ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);
                if (!strBookName.equalsIgnoreCase("0")) {
                    ssb.append(strBookName);
                }

                //Log.e("strBookName",strBookName);
                ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);
                //Log.e("strBookName",strBookName);
                if (strPageNo != null)
                    if (!strPageNo.equalsIgnoreCase("0")) {
                        ssb.append("P. " + strPageNo);
                    }
                tvPageName.setText(ssb);
            } else if (strFrom != null && strFrom.equals("IndexSearchDetailsActivity")) {
                SpannableStringBuilder ssb = new SpannableStringBuilder();

                if (strWordName != null && strWordName.length() > 0) {
                    if (!strWordName.equalsIgnoreCase("0")) {
                        ssb.append(strWordName + " ");
                        ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                                ssb.length() - 1,
                                ssb.length(),
                                0);
                    }
                    Log.e("strWordName -- ", strWordName);
                }

                if (!strBookName.equalsIgnoreCase("0")) {
                    ssb.append(strBookName);
                    ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                            ssb.length() - 1,
                            ssb.length(),
                            0);
                }

                if (strIndexName != null && strIndexName.length() > 0) {
                    //ssb.append(strBookName);
                    ssb.append(" (" + strIndexName + ") ");
                    ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                            ssb.length() - 1,
                            ssb.length(),
                            0);
                }

                if (strHeaderPageNo != null && strHeaderPageNo.length() > 0) {
                    ssb.append("P. " + strHeaderPageNo + " ");
                    Log.e("SSB----", "" + ssb);
                } else if (strPageNo != null && strPageNo.length() > 0) {
                    ssb.append("P. " + strPageNo + " ");
                    Log.e("SSB----", "" + ssb);
                }
                tvPageName.setText(ssb);
            } else {
                if (!strWordName.equalsIgnoreCase("0")) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();

                    if (!strWordName.equalsIgnoreCase("0")) {
                        ssb.append(strWordName + " ");
                    }

                    Log.e("strWordName -- ", strWordName);
                    ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                            ssb.length() - 1,
                            ssb.length(),
                            0);

                    if (!strBookName.equalsIgnoreCase("0")) {
                        ssb.append(strBookName);
                    }
                    //      Log.e("strHeaderPageNo", strHeaderPageNo);
                    ssb.setSpan(new ImageSpan(activity, R.drawable.ic_baseline_chevron_right_24),
                            ssb.length() - 1,
                            ssb.length(),
                            0);
                    //   Log.e("strBookName", strBookName);

                    ssb.append("P. " + strHeaderPageNo + " ");
                    Log.e("SSB----", "" + ssb);
                    tvPageName.setText(ssb);
                }
            }

        }
    }

    private int getScrollPos1(ArrayList<BooksDataModel.BookImageDetailsModel> pdfList) {
        int scrollPos = 0;
        Log.e("PDF Size", "" + pdfList.size());
        if (pdfList != null && pdfList.size() > 0) {
            if (pdfList.size() <= total_pages && pageNo < (last_index_gap + 1)) {
                scrollPos = (pageNo - 1);
                pageNoFirstIndex = Integer.parseInt(pdfList.get(0).getPageno()) - 1;// (pageNo - scrollPos);
                pageNoLastIndex = Integer.parseInt(pdfList.get(pdfList.size() - 1).getPageno()) + 1;
            } else if (pdfList.size() == total_pages && pageNo >= (last_index_gap + 1)) {
                scrollPos = (pdfList.size() / 2);
                pageNoFirstIndex = (pageNo - scrollPos);
                pageNoLastIndex = pageNo + first_index_gap;
            }
        }
        return scrollPos;
    }

    private int getScrollPos(ArrayList<BooksDataModel.BookImageDetailsModel> pdfList) {
        int scrollPos = 0;
        Log.e("PDF Size", "" + pdfList.size() + " Page No " + pageNo);
        if (pdfList != null && pdfList.size() > 0) {
            if (pdfList.size() < total_pages) {
                scrollPos = pageNo - 1;
                pageNoFirstIndex = 0;
                pageNoLastIndex = -1;
            } else if (pdfList.size() == total_pages) {
                scrollPos = (pdfList.size() / 2) - 1;
                pageNoFirstIndex = Integer.parseInt(pdfList.get(0).getPageno());
                pageNoLastIndex = Integer.parseInt(pdfList.get(pdfList.size() - 1).getPageno()) + 1;
            }
        }
        return scrollPos;
    }

    private void setPdfData(ArrayList<BooksDataModel.BookImageDetailsModel> pdfList) {
        mReferencePageAdapter = new ReferencePageAdapter(activity, pdfList, this);
        rvPdf.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        int scrollPos = getScrollPos(pdfList);
        layoutManager.scrollToPositionWithOffset(scrollPos + 1, 0);
      /*XRecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(activity) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
           }
        };
        smoothScroller.setTargetPosition(scrollPos);
        layoutManager.startSmoothScroll(smoothScroller);*/
        rvPdf.setLayoutManager(layoutManager);
        rvPdf.setVisibility(View.VISIBLE);
        rvPdf.setAdapter(mReferencePageAdapter);
        rvPdf.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rvPdf.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        // rvPdf.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        // rvPdf.setRefreshProgressStyle(ProgressStyle.BallPulse);
        ImageZoomHelper.setZoom(rvPdf, true);
        ImageZoomHelper.setViewZoomable(rvPdf);
        rvPdf.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Log.e("Prev ", "" + pageNoFirstIndex);
                if (pageNoFirstIndex == 0)
                    rvPdf.loadMoreComplete();
                else
                    callBookPageApi(strTypeId, strBookId, pageNoFirstIndex, "1", true);
            }

            @Override
            public void onLoadMore() {
                Log.e("Next ", "" + pageNoLastIndex);
                if (pageNoLastIndex == -1)
                    rvPdf.refreshComplete();
                else
                    callBookPageApi(strTypeId, strBookId, pageNoLastIndex, "2", false);
            }
        });
    }

    private void loadUiElements() {
        Constantss.ISREFERENCE = true;
        Constantss.ISTIMERFINISH = false;
        imageZoomHelper = new ImageZoomHelper(this);// Util.commonKeyboardHide(activity);
        llBookDetails = findViewById(R.id.llBookDetails);
        llQuickLink = findViewById(R.id.llQuickLink);
        viewQuickLInk = findViewById(R.id.viewQuickLInk);
        llBookTitle = findViewById(R.id.llBookTitle);
        rvPdf = findViewById(R.id.rvPdf);
        llShare = findViewById(R.id.llShare);
        llHold = findViewById(R.id.llHold);
        llFeedback = findViewById(R.id.llFeedback);
        ll_buttons = findViewById(R.id.ll_buttons);
        buttonFilter = findViewById(R.id.button_filter);
        tvFooterPageName = findViewById(R.id.tvtitlePages);
        bookDetailsPages = findViewById(R.id.bookDetailsPages);
        ivQuickLink = findViewById(R.id.ivQuickLink);
        tvQuickLink = findViewById(R.id.tvQuickLink);
        tvInfo = findViewById(R.id.tvInfo);
        tvSelectedImageCount = findViewById(R.id.tvSelectedImageCount);
        llTotal = findViewById(R.id.llTotal);
        llSelectOption = findViewById(R.id.llSelectOption);
        llPdfOption = findViewById(R.id.rlPdfOption);
        btnMyShelf = findViewById(R.id.btnMyShelf);
        handler = new Handler();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return imageZoomHelper.onDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public void onImageClick(ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList,
                             int position) {

        BooksDataModel.BookImageDetailsModel mBookDetails = mPfdList.get(position);
        String page_bytes = mBookDetails.getPage_bytes();

        Log.e(TAG, "PageBytes : " + page_bytes);
        Log.e(TAG, "PageImage : " + mBookDetails.getPage_image());
        Log.e(TAG, "mBookDetails : " + new Gson().toJson(mBookDetails));

        if (mBookDetails.getType().equalsIgnoreCase("1") && page_bytes != null && page_bytes.length() > 0) {
            byte[] imgBytes = Base64.decode(page_bytes, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();
            Bitmap myBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


            File tempFile = new File(getCacheDir(), System.currentTimeMillis() + "1");
            try {
                tempFile.createNewFile();
                //Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (mBookDataModels == null) {
                mBookDataModels = new BookListResModel.BookDetailsModel();
            }
            mBookDataModels.setTempImageArray(tempFile);

            Intent i = new Intent(activity, DownloadFileActivity.class);
            i.putExtra("modelPdfOtherData", mBookDataModels);
            i.putExtra("url", false);
            startActivity(i);
        } else {
            String strImage = mPfdList.get(position).getPage_image();
            if (strImage != null && strImage.length() > 0) {
                Intent i = new Intent(activity, DownloadFileActivity.class);
                i.putExtra("modelPdfOtherData", mBookDataModels);
                i.putExtra("image", strImage);
                i.putExtra("url", true);
                startActivity(i);
            }
        }
    }

    @Override
    public void onShareImage(ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList,
                             int position, boolean isSelected) {
//        Utils.showProgressDialog(ReferencePageActivity.this, "Please Wait...", false);
        isImageSelected = false;
        if (mPfdList != null && mPfdList.size() > 0) {
            mSelectedPdfPageList = mPfdList;
            String strImage = mPfdList.get(position).getPage_image();
            String strPageNo = mPfdList.get(position).getPageno();
            Log.e(TAG, "strImage---" + strImage);
            if (strImage != null && strImage.length() > 0) {
                if (!isSelected) {
                    isImageSelected = true;
                    for (int i = 0; i < addMyShlefList.size(); i++) {
                        String strUrl = addMyShlefList.get(i).getStrUrl();
                        if (strUrl != null && strUrl.equalsIgnoreCase(strImage)) {
                            addMyShlefList.remove(i);
                            Log.e("AdmyShelf", i + "not");
                            break;
                        }
                    }
                    for (int i = 0; i < imageFiles.size(); i++) {
                        Set<String> strImages = imageFiles.get(i).keySet();
                        for (String key : strImages) {
                            if (key.equalsIgnoreCase(strImage)) {
                                imageFiles.remove(i);
                                Log.e("imageFiles", i + "removed");
                                break;
                            }
                        }
                    }
                    isImageSelected = true;

//                    Utils.dismissProgressDialog();
                    setSelectedOptionData();
                } else {
                    AddMyShelfModel mAddShelfModel = new AddMyShelfModel();
                    mAddShelfModel.setStrPageNo(strPageNo);
                    mAddShelfModel.setStrUrl(strImage);
                    addMyShlefList.add(mAddShelfModel);
                    Glide.with(activity).load(strImage).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            resource.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
                            HashMap<String, Uri> map = new HashMap<>();
                            map.put(strImage, Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), resource, "JRL_" + System.currentTimeMillis(), null)));
                            imageFiles.add(map);
                            if (!mImageUriList.contains(map.get(strImage)))
                                mImageUriList.add(map.get(strImage));
                            isImageSelected = true;
                            Log.e("imageFiles", "pdflist Pos" + count++);

//                            Utils.dismissProgressDialog();
                            setSelectedOptionData();
                        }
                    });
                }
            }

           /* String strImage =0 mPfdList.get(position).getPage_image();
            if (strImage != null && strImage.length() > 0) {
                Glide.with(activity).load(strImage).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        resource.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
                        imageFiles.add(Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), resource, "", null)));
                    }
                });
            }*/

        }
    }

    private void setSelectedOptionData() {
        Log.e(TAG, "setSelectedOptionData");

        if (bottomSheetDialog != null) {
            return;
        }

        llPdfOption.setVisibility(View.GONE);
        llSelectOption.setVisibility(View.VISIBLE);
        if (addMyShlefList != null && addMyShlefList.size() > 0) {
            tvInfo.setVisibility(View.GONE);
            llTotal.setVisibility(View.VISIBLE);
            tvSelectedImageCount.setText("" + addMyShlefList.size());
        } else {
            tvInfo.setVisibility(View.VISIBLE);
            llTotal.setVisibility(View.GONE);
        }
    }

    public void dismissBottomSheetDialog() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
            bottomSheetDialog = null;
        }
    }

    public void getShareDialog() {


        bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        View bottomSheetDialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_share, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        LinearLayout bottomSheetContainer = bottomSheetDialogView.findViewById(R.id.bottomSheetContainer);
        Button btnShare = bottomSheetDialogView.findViewById(R.id.btnShare);
        Button btnMyShelf = bottomSheetDialogView.findViewById(R.id.btnMyShelf);
//        Button btnDownload = bottomSheetDialogView.findViewById(R.id.btnDownload);
        TextView tvSelectedImageCount = bottomSheetDialogView.findViewById(R.id.tvSelectedImageCount);
        EditText edtRenameFile = bottomSheetDialogView.findViewById(R.id.edtRenameFile);
        ImageView ivClose = bottomSheetDialogView.findViewById(R.id.ivClose);
        mKeyboardView = bottomSheetDialogView.findViewById(R.id.keyboardView);
        edtRenameFile.setFocusable(true);
        strTotalCount = "" + mImageUriList.size();
        if (strFrom != null && strFrom.equals("IndexSearchDetailsActivity")) {
            edtRenameFile.setText("JainRefLibrary" + "_" + strIndexName + "_" + strBookName + " (" + strPageNo + ")_" + mImageUriList.size());
        } else if (strFrom != null && strFrom.equals("YearBookActivity")) {
            edtRenameFile.setText("JainRefLibrary" + "_" + strTypeName + "_" + strBookName + " (" + strPageNo + ")_" + mImageUriList.size());
        } else if (strFrom != null && strFrom.equals("BiodataMemoryDetailsActivity")) {
            edtRenameFile.setText("JainRefLibrary" + "_" + strTypeName + "_" + strBookName + " (" + strPageNo + ")_" + mImageUriList.size());
        } else if (strFrom != null && strFrom.equals("RelationDetailsActivity")) {
            edtRenameFile.setText("JainRefLibrary" + "_" + strTypeName + "_" + strBookName + " (" + strPageNo + ")_" + mImageUriList.size());
        } else {
            if (strSutraName != null && strSutraName.length() > 0) {
                edtRenameFile.setText("JainRefLibrary" + "_" + strSutraName + "_" + strBookName + " (" + strPageNo + ")_" + mImageUriList.size());
            } else if (isAppendix == true) {
                edtRenameFile.setText("JainRefLibrary" + "_" + strPageName + "_" + strBookName + " (" + strPageNo + ")_" + mImageUriList.size());
            } else {
                String strKeyword = null;
                if (mBookDataModels.getKeyword() != null && mBookDataModels.getKeyword().length() > 0) {
                    strKeyword = mBookDataModels.getKeyword();
                }
                edtRenameFile.setText("JainRefLibrary" + "_" + strKeyword + "_" + strBookName + " (" + strPageNo + ")_" + mImageUriList.size());
            }
        }
        //edtRenameFile.setText("JainRefLibrary" + "_" +strSutraName + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName);
        String strLanguage = SharedPrefManager.getInstance(activity).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        edtRenameFile.requestFocus();
/*
        edtRenameFile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(activity, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, activity, strLanguage, bottomSheetDialog);
                return false;
            }
        });*/

        tvSelectedImageCount.setText("" + mImageUriList.size());
        strEdtRenamefile = edtRenameFile.getText().toString();
        if (strEdtRenamefile == null || strEdtRenamefile.length() == 0) {
            strEdtRenamefile = "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName;
        }

        strEdtRenamefile = "JainRefLibrary" + "_" + strPageNo + "_" + mImageUriList.size();

        strPdfFile = getPdfFile(strEdtRenamefile);

        bottomSheetDialog.setOnCancelListener(dialogInterface -> {
            Log.e(TAG, "bottomSheetDialog cancel callback");
        });

        bottomSheetDialog.setOnDismissListener(dialogInterface -> {
            Log.e(TAG, "bottomSheetDialog dismiss callback");
            bottomSheetDialog = null;
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 /*
                if (mImageUriList != null && mImageUriList.size() > 0) {
                   String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                    String strMessage = " " + strBookName;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                    intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                    intent.setType(""); //This example is sharing jpeg images.
                    Log.e(TAG, "imageFilesss--" + mImageUriList.size());
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mImageUriList);
                    startActivity(Intent.createChooser(intent, shareData));

                } else {
                    Toast.makeText(activity, "Please select image", Toast.LENGTH_SHORT).show();
               }
*/
                if (addMyShlefList != null && addMyShlefList.size() > 0) {
                    dismissBottomSheetDialog();
                    JSONArray myShelfArray = new JSONArray();
                    if ((addMyShlefList != null) && (addMyShlefList.size() > 0)) {
                        for (int j = 0; j < addMyShlefList.size(); j++) {
                            String strPageNo = addMyShlefList.get(j).getStrPageNo();
                            String strUrl = addMyShlefList.get(j).getStrUrl();
                            JSONObject jsonObject = new JSONObject();
                            Log.e("CheckString-", "" + strUrl);
                            try {
                                if (strPageNo != null && strPageNo.length() > 0) {
                                    jsonObject.put("page_no", strPageNo);
                                }
                                /*if (strUrl != null && strUrl.length() > 0) {
                                    jsonObject.put("url", strUrl);
                                }*/
                                if (strPageNo != null && strUrl != null) {
                                    myShelfArray.put(jsonObject);
                                } else {
                                    Log.e(TAG, "isProductFile");
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                    if (myShelfArray != null && myShelfArray.length() > 0) {

                        strEdtRenamefile = edtRenameFile.getText().toString();
                        if (strEdtRenamefile == null || strEdtRenamefile.length() == 0) {
                            strEdtRenamefile = "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName;
                        }

                        if (strUserID != null && strUserID.length() > 0) {
                            Log.e("userid", strUserID);
                            callAddMyShelfApi(myShelfArray, strUserID, strEdtRenamefile, false);
                        }
                    }
                }

                /*
                strEdtRenamefile = edtRenameFile.getText().toString();
                if (strEdtRenamefile == null || strEdtRenamefile.length() == 0) {
                    strEdtRenamefile = "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName;
                }

                if (strEdtRenamefile != null && strEdtRenamefile.length() > 0) {
                    bottomSheetDialog.cancel();
                    callShareMyShelfsApi(strUserID);

                } else {
                    edtRenameFile.setError("Please enter file name");
                    edtRenameFile.requestFocus();
                }*/
            }
        });
//        btnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                strEdtRenamefile = edtRenameFile.getText().toString();
//                Log.e("mImageUriList", "size : " + mImageUriList.size());
//
//                if (mImageUriList != null && mImageUriList.size() > 0) {
//                    bottomSheetDialog.cancel();
//                    callDownloadMyShelfsApi(strUserID);
//                    //downloadAndStoreImages(mImageUriList, "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName);
//                }
//            }
//        });

        String finalStrEdtRenamefile = strEdtRenamefile;
        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addMyShlefList != null && addMyShlefList.size() > 0) {
                    dismissBottomSheetDialog();
                    JSONArray myShelfArray = new JSONArray();
                    if ((addMyShlefList != null) && (addMyShlefList.size() > 0)) {
                        for (int j = 0; j < addMyShlefList.size(); j++) {
                            String strPageNo = addMyShlefList.get(j).getStrPageNo();
                            String strUrl = addMyShlefList.get(j).getStrUrl();
                            JSONObject jsonObject = new JSONObject();
                            Log.e("CheckString-", "" + strUrl);
                            try {
                                if (strPageNo != null && strPageNo.length() > 0) {
                                    jsonObject.put("page_no", strPageNo);
                                }
                                /*if (strUrl != null && strUrl.length() > 0) {
                                    jsonObject.put("url", strUrl);
                                }*/
                                if (strPageNo != null && strUrl != null) {
                                    myShelfArray.put(jsonObject);
                                } else {
                                    Log.e(TAG, "isProductFile");
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                    if (myShelfArray != null && myShelfArray.length() > 0) {

                        strEdtRenamefile = edtRenameFile.getText().toString();
                        if (strUserID != null && strUserID.length() > 0) {
                            Log.e("userid", strUserID);
                            callAddMyShelfApi(myShelfArray, strUserID, strEdtRenamefile, true);
                        }
                    }
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissBottomSheetDialog();
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
                    dismissBottomSheetDialog();
                } else {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        if (mKeyboardView.getVisibility() == View.VISIBLE) {
                            mKeyboardView.setVisibility(View.GONE);
                        } else {
                            dismissBottomSheetDialog();
                        }
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void callDownloadMyShelfsApi(String strUserID) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(ReferencePageActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserID);
        Log.e("strTypeRef :", " " + strTypeRef);
        Utils.showProgressDialog(ReferencePageActivity.this, "Please Wait...", false);

        ApiClient.downloadMyShelfs(strUserID, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
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
                        Utils.downloadLocalPDF(strPdfFile, ReferencePageActivity.this);
//                        try {
//                            downloadAndStoreImages(mImageUriList, strEdtRenamefile);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : " + throwable.getMessage());
            }
        });
    }

    private void callShareMyShelfsApi(String strUserID) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(ReferencePageActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserID);
        Log.e("strTypeRef :", " " + strTypeRef);
        Utils.showProgressDialog(ReferencePageActivity.this, "Please Wait...", false);

        ApiClient.shareMyShelfs(strUserID, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
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
//                        sharePDFFiles(strEdtRenamefile);
                        sharePDFLink();
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : " + throwable.getMessage());
            }
        });
    }

    private void callAddMyShelfApi(JSONArray myShelfArray, String strUserId, String strEdtRenamefile, boolean notesDialog) {
        Utils.showProgressDialog(activity, "Please Wait...", false);
        Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + strBookName + "_" + strWordName + " /";
        MultipartBody.Part filePart = null;
        if (strPdfFile != null && strPdfFile.length() > 0) {
            File mFile = new File(strPdfFile);
            if (mFile.exists()) {
                filePart = MultipartBody.Part.createFormData("pdf_file", mFile.getName(), RequestBody.create(MediaType.parse("pdf/*"), mFile));
            }
        }
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUserId);
        RequestBody bookid = RequestBody.create(MediaType.parse("text/*"), strBookId);

        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "");
        RequestBody typeid = RequestBody.create(MediaType.parse("text/*"), "");
        if (strFrom != null && strFrom.equals("IndexSearchDetailsActivity")) {
            type = RequestBody.create(MediaType.parse("text/*"), "2" + strIndexIniId);
            typeid = RequestBody.create(MediaType.parse("text/*"), strIndexId);
        } else if (strFrom != null && strFrom.equals("YearBookActivity")) {
            type = RequestBody.create(MediaType.parse("text/*"), "3" + strYearTypeId);
            typeid = RequestBody.create(MediaType.parse("text/*"), strYearId);
        } else if (strFrom != null && strFrom.equals("BiodataMemoryDetailsActivity")) {
            type = RequestBody.create(MediaType.parse("text/*"), strTypeId);
            typeid = RequestBody.create(MediaType.parse("text/*"), strRefrenceTypeId);
        } else if (strFrom != null && strFrom.equals("RelationDetailsActivity")) {
            type = RequestBody.create(MediaType.parse("text/*"), strTypeId);
            typeid = RequestBody.create(MediaType.parse("text/*"), strRefrenceTypeId);
        } else {
            String strBookID = mBookDataModels.getBook_id();
            String strKeyword = null;
            if (mBookDataModels.getKeyword() != null && mBookDataModels.getKeyword().length() > 0) {
                strKeyword = mBookDataModels.getKeyword();
            }

            if (strSutraName != null) {
                type = RequestBody.create(MediaType.parse("text/*"), "1");
                typeid = RequestBody.create(MediaType.parse("text/*"), strGSId);
            } else if (isAppendix) {
                type = RequestBody.create(MediaType.parse("text/*"), "4");
                typeid = RequestBody.create(MediaType.parse("text/*"), strAppendixId);
            } else if (strKeyword != null) {
                type = RequestBody.create(MediaType.parse("text/*"), "0");
                typeid = RequestBody.create(MediaType.parse("text/*"), strKeyWordId);
            }
        }

        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), strEdtRenamefile);
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), "1");
        RequestBody count = RequestBody.create(MediaType.parse("text/*"), strTotalCount);
        RequestBody fileType = RequestBody.create(MediaType.parse("text/*"), "4");
        Log.e("fileType :", " " + fileType);
        Log.e("strUserId " + strUserId, "strBookId" + strBookId + " strKeyWordId " + strKeyWordId + " file " + "pdf_" + "JainRefLibrary" + "_" + strBookName + " type " + strType + " typeref " + typeref);
        Utils.showProgressDialog(activity, "Please Wait...", false);
        ApiClient.addMyShelfs(uid, bookid, typeid, type, typeref, filename, null, count, fileType, filePart, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                    AddShelfResModel addShelfResModel = response.body();
                    Log.e("responseMyShelfData :", new GsonBuilder().setPrettyPrinting().create().toJson(addShelfResModel));

                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        //   String strNotes = response.body().getNotes();
                        String strid = response.body().getId();
                        strPdfLink = response.body().getPdf_url();
                        //      Log.e("id", strid);
//                        Utils.showInfoDialog(activity, "" + response.body().getMessage());
                        if (notesDialog) {
                            getInfoDialogs(response.body().getMessage(), strType, strTypeRef, "Do you want to add notes?", strid);
                        } else {
                            callShareMyShelfsApi(strUserID);
                        }
                    } else {
                        Utils.showInfoDialog(activity, "" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());

                }
            }


            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(activity, "Something went wrong please try again later");

            }
        });

    }

    private void saveIntoMyShelf(ArrayList<Uri> mImageUriList) {
        ArrayList<String> mOldImageBitmapList = new ArrayList<>();
        ArrayList<String> tempBitmapList = new ArrayList<>();

        for (int i = 0; i < mImageUriList.size(); i++) {
            Uri mUri = mImageUriList.get(i);
            String path = FileUtils.getPath(activity, mUri);
            if (path != null) {
                mOldImageBitmapList.add(path);
            }
        }
        if (mOldImageBitmapList != null && mOldImageBitmapList.size() > 0) {
            tempBitmapList = SharedPrefManager.getInstance(activity).getMyShelfList(SharedPrefManager.KEY_MY_SHELF);
            if (tempBitmapList != null && tempBitmapList.size() > 0) {
                mOldImageBitmapList.addAll(tempBitmapList);
            }
            SharedPrefManager.getInstance(activity).saveMyShelfList(SharedPrefManager.KEY_MY_SHELF, mOldImageBitmapList);
            Utils.showInfoDialog(activity, "Downloaded Successfully");
            if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
                this.mImageUriList.clear();
            }
        } else {
            return;
        }
        Log.e("mImageUriList---", "Shelf---" + mOldImageBitmapList.size());
    }

    private void downloadAndStoreImages(ArrayList<Uri> mImageUriList, String strFileName) throws
            IOException {
        String strFileFinalPath = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        if (!Utils.mediaStoreDownloadsDir.exists()) {
            Utils.mediaStoreDownloadsDir.mkdirs();
        }
        for (int i = 0; i < mImageUriList.size(); i++) {
            Uri uri = mImageUriList.get(i);
            strFileFinalPath = strFileName + strFileFinalPath + ".jpg";
            File file = new File(Utils.mediaStoreDownloadsDir.getAbsolutePath(), "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Log.d("bitmap--", "bitmap---" + bitmap);
            View view = rvPdf.getChildAt(i);
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            String dateToStr = format.format(today);
            TextView tvFooterRight = view.findViewById(R.id.tvFooterRight);
            TextView tvHeaderRight = view.findViewById(R.id.tvHeaderRight);
            TextView tvHeaderLeft = view.findViewById(R.id.tvHeaderLeft);
            Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName;
            Constantss.FILE_NAME_PDF = "JainRefLibrary" + "/" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName;
            Log.e("ConstantFime", Constantss.FILE_NAME);
            Constantss.PAGE_COUNT = String.valueOf(mImageUriList.size());
            tvFooterRight.setText(Constantss.FILE_NAME);
            tvHeaderLeft.setText(dateToStr);
            tvHeaderRight.setText(Constantss.PAGE_COUNT);
            try {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            } catch (Exception ex) {
                Log.d(TAG, "Non Navigation button");
            }// getBitmapFromView(view);
            downloadAndStoreImages(strFileName, getBitmapFromView(view));/*
          if (!file.exists()) {
                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }*/
        }
        Utils.showInfoDialog(activity, "Downloaded Successfully "
                + "\n" + Utils.mediaStoreDownloadsDir.getAbsolutePath() + "/" + strFileName);
        if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
            this.mImageUriList.clear();
        }
    }

    private void downloadAndStoreImages(String strFileName, Bitmap imageBitmap) throws
            IOException {
        Log.e("sucess", "success");
        String strFileFinalPath = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        if (!Utils.mediaStoreDownloadsDir.exists()) {
            Utils.mediaStoreDownloadsDir.mkdirs();
        }

        if (imageBitmap != null) {
            Uri uri = Utils.getImageUri(activity, imageBitmap);
            strFileFinalPath = strFileName + strFileFinalPath + ".jpg";
            File file = new File(Utils.mediaStoreDownloadsDir.getAbsolutePath(), strFileFinalPath);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Log.d("bitmap--", "bitmap---" + bitmap);
            if (!file.exists()) {
                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            Utils.showInfoDialog(activity, "Downloaded Successfully " + "\n" + Utils.mediaStoreDownloadsDir.getAbsolutePath() + "/" + strFileName);
        } else {
            Utils.showInfoDialog(this, "Image not found");
        }
    }

    private void shareZipFiles(String strEdtRenamefile) {
        try {
            if (mImageUriList != null && mImageUriList.size() > 0) {
                String strZipperFileName = ZipCompressor.zip(activity, mImageUriList, strEdtRenamefile);
                if (strZipperFileName != null && strZipperFileName.length() > 0) {
                    Log.e(TAG, "strZipperFileName--" + strZipperFileName);
                    Uri fileUri = Uri.fromFile(new File(strZipperFileName));
                    Log.e(TAG, "fileUri--" + fileUri);
                    String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                    String strMessage = " " + strBookName;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                    intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    intent.setType("application/zip");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Log.e(TAG, "imageFilesss--" + mImageUriList.size());
                    startActivity(Intent.createChooser(intent, shareData));
                }
                if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
                    this.mImageUriList.clear();
                }
            } else {
                Utils.showInfoDialog(activity, "Please select image");
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
    }

    private void sharePDFFiles(String strEdtRenamefile) {
        try {
            if (mImageUriList != null && mImageUriList.size() > 0) {
                List<String> mImagePath = new ArrayList<>();
                for (int i = 0; i < mImageUriList.size(); i++) {
                    String strImagePath = FileUtils.getPath(activity, mImageUriList.get(i));
                    if (strImagePath != null && strImagePath.length() > 0) {
                        mImagePath.add(strImagePath);
                        strPdfFile = null;
                        if (mImagePath != null && mImagePath.size() > 0) {
                            Constantss.PAGE_COUNT = String.valueOf(mImageUriList.size());
                            Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName;
                            Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strWordName + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size();
                            Log.e("ConstantFime", Constantss.FILE_NAME);
                            strPdfFile = PdfCreator.creatPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mImagePath);
                        }

                    }
                }

                if (strPdfFile != null && strPdfFile.length() > 0) {
                    Log.e(TAG, "strZipperFileName--" + strPdfFile);

                    Uri fileUri = Uri.fromFile(new File(strPdfFile));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        fileUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(strPdfFile));
                    }

                    Log.e(TAG, "fileUri--" + fileUri);
                    String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                    String strMessage = " " + strBookName;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                    intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Log.e(TAG, "imageFiles" + mImageUriList.size());
                    startActivity(Intent.createChooser(intent, shareData));
                }

                if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
                    this.mImageUriList.clear();
                }

            } else {
                Utils.showInfoDialog(activity, "Please select image");
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
    }

    private void sharePDFLink() {
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
    }

    public void callAddHoldSearchKeyword(String strUId, String strBookId, String strTypeId, String strType) {

        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check internet connection");
            return;
        }


        Utils.showProgressDialog(activity, "Please Wait...", false);
        // ApiClient.addHoldSearchKeyword(strUId, strBookId, strKeyword, strPageNo, strAuthorName, strTranslatorName, strEditorName, new Callback<HoldAndSearchResModel>() {
        ApiClient.addNewHoldSearchKeyword(strUId, strBookId, strTypeId, strType, strPageNo, strPdfPageNo, new Callback<HoldAndSearchResModel>() {
            @Override
            public void onResponse(Call<HoldAndSearchResModel> call, retrofit2.Response<HoldAndSearchResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    Dialog dialog = new IosDialog.Builder(activity)
                            .setMessage(response.body().getMessage())
                            .setMessageColor(Color.parseColor("#1565C0"))
                            .setMessageSize(18)
                            .setNegativeButtonColor(Color.parseColor("#981010"))
                            .setNegativeButtonSize(18)
                            .setNegativeButton("Ok", new IosDialog.OnClickListener() {
                                @Override
                                public void onClick(IosDialog dialog, View v) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButtonColor(Color.parseColor("#981010"))
                            .setPositiveButtonSize(18)
                            .setPositiveButton("View Hold", new IosDialog.OnClickListener() {
                                @Override
                                public void onClick(IosDialog dialog, View v) {
                                    dialog.dismiss();

                                    Intent i = new Intent(activity, HoldAndSearchActivity.class);
                                    i.putExtra("model", mBookDataModels);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.putExtra("hold", true);
                                    startActivity(i);
                                }
                            }).build();

                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<HoldAndSearchResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public void getInfoDialogs(String strMessage, String strType, String strTypeRef, String
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
                        Utils.showInfoDialog(ReferencePageActivity.this, strMessage);
                    }
                })
                .setPositiveButtonColor(Color.parseColor("#981010"))
                .setPositiveButtonSize(18)
                .setPositiveButton("Yes", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, NotesActivity.class);

                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strWordName);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }

    public String getPdfFile(String strEdtRenamefile) {
        try {
            if (mImageUriList != null && mImageUriList.size() > 0) {
                List<String> mImagePath = new ArrayList<>();
                for (int i = 0; i < mImageUriList.size(); i++) {
                    String strImagePath = FileUtils.getPath(activity, mImageUriList.get(i));
                    if (strImagePath != null && strImagePath.length() > 0) {
                        mImagePath.add(strImagePath);
                        strPdfFile = null;
                        if (mImagePath != null && mImagePath.size() > 0) {
                            Constantss.PAGE_COUNT = String.valueOf(mImageUriList.size());
                            Constantss.FILE_NAME = "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size() + "_" + strWordName;
                            Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strWordName + "_" + strBookName + "_" + strPageNo + "_" + mImageUriList.size();
                            Log.e("ConstantFime", Constantss.FILE_NAME);
                            strPdfFile = PdfCreator.creatPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mImagePath);
                        }
                    }
                }

            } else {
                Utils.showInfoDialog(activity, "Please select image");
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
        return strPdfFile;
    }


}

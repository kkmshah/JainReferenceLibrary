/*
package com.jainelibrary.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.adapter.ReferencePageAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddMyShelfModel;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.BooksDataModel;
import com.jainelibrary.model.HoldAndSearchResModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.FileUtils;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.jainelibrary.utils.ZipCompressor;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;

public class MyReferencePdfActivity extends AppCompatActivity implements ReferencePageAdapter.PdfInterfaceListener {
    private static final int SEND_HOLD_DATA = 0;
    private static final int REFERENCE_CODE = 1;
    private static final String TAG = MyReferencePdfActivity.class.getSimpleName();
    public int pageNoFirstIndex, pageNoLastIndex, pageNo;
    boolean isAppendix = false;
    JRL myApplication;
    BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
    ArrayList<BooksDataModel.BookImageDetailsModel> mReferencePageBook = new ArrayList<>();
    ArrayList<BookListResModel.BookDetailsModel> mBookDetailsList = new ArrayList<>();
    ArrayList<BookListResModel.BookDetailsModel> mSelectedBookDetailsList = new ArrayList<>();
    ArrayList<HashMap<String, Uri>> imageFiles = new ArrayList<HashMap<String, Uri>>();
    private LinearLayout ll_buttons, llShare, llBookDetails, llQuickLink;
    private TextView buttonFilter;
    private String strUserID, strWordName, strBookName, strBookId, strKeyWordId, strEditorName, strTranslatorName, strAuthorName, strPageNo, strSutraName;
    private String PackageName;
    private XRecyclerView rvPdf;
    private ReferencePageAdapter mReferencePageAdapter;
    private LinearLayout llBookTitle, llHold;
    private String strPageName;
    private String strPageCount;
    private TextView tvQuickLink, tvFooterPageName, bookDetailsPages;
    private boolean isFirstTime = false;
    private boolean isLogin = false;
    private ArrayList<BooksDataModel.BookImageDetailsModel> mSelectedPdfPageList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    ArrayList<Uri> mImageUriList = new ArrayList<>();
    Keyboard mKeyboard;
    CustomKeyboardView mKeyboardView;

    ArrayList<AddMyShelfModel> addMyShlefList = new ArrayList<>();
    private String strType, strNotes, strTypeRef;
    private boolean isHighlight = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reference_pdf);
        Log.e(TAG, "  aa");
        myApplication = (JRL) getApplicationContext();
        loadUiElements();
        declaration();
        setHeader();
        onBtnEventListener();
    }

    private void onBtnEventListener() {
        llBookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyReferencePdfActivity.this, BookDetailsActivity.class);
                i.putExtra("model", mBookDataModels);
                startActivity(i);
            }
        });

        llQuickLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                boolean isLogin = SharedPrefManager.getInstance(MyReferencePdfActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
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
                                    mImageUriList.add(map.get(key));
                                    Log.e(TAG, "imageFilesssIn--" + mImageUriList.size() + "key -- " + map.get(key));
                                }
                            }
                        }
                        //showShareDialog(v);
                        if (mImageUriList != null && mImageUriList.size() > 0) {
                            //getShareDialog();
                            sharePDFFiles(strBookName);
                        }
                    } else {
                        InfoDialog("Long Press on Image for Select.\nYou can select multiple images, please scroll.");
                    }
                } else {
                    askLogin();
                }
            }
        });
        llHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isLogin = SharedPrefManager.getInstance(MyReferencePdfActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    String strUserId = SharedPrefManager.getInstance(MyReferencePdfActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                    String strKeyword = mBookDataModels.getKeyword();
                    String strBookID = mBookDataModels.getBook_id();
                    if (strUserId != null && strKeyword != null && strBookID != null) {
                        callAddHoldSearchKeyword(strUserId, strBookID, strKeyword);
                    }
                } else {
                    askLogin();
                }
                // setBaseClassData();
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MyReferencePdfActivity.this);
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", dialogClickListener)
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
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent intent = new Intent(MyReferencePdfActivity.this, LoginWithPasswordActivity.class);
                        intent.putExtra("isLoginId", Utils.Is_Reference_Login_Id);
                        startActivityForResult(intent, REFERENCE_CODE);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MyReferencePdfActivity.this);
        builder.setMessage(android.text.Html.fromHtml("<font color='#1565C0'>Please login to use this functionality.</font>")).setPositiveButton("Login", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mKeyboardView != null && mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        if (mImageUriList != null && mImageUriList.size() > 0) {
            mImageUriList.clear();
        }

        if (mKeyboardView != null && mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }

    }

    public void callBookPageApi(String strKId, String strBId, int strPId, String strDirection, boolean isRefresh) {

        if (!ConnectionManager.checkInternetConnection(MyReferencePdfActivity.this)) {
            Toast.makeText(MyReferencePdfActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getBookPage(strKId, strBId, String.valueOf(strPId), strDirection, strType,"0", new Callback<BooksDataModel>() {
            @Override
            public void onResponse(Call<BooksDataModel> call, retrofit2.Response<BooksDataModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     */
/*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*//*


                    if (response.body().isStatus()) {

                        BooksDataModel mBookDataMOdel = response.body();

                        if (isFirstTime) {
                            mReferencePageBook.clear();
                            mReferencePageBook.addAll(mBookDataMOdel.getData());
                            setPdfData(mReferencePageBook);
                            isFirstTime = false;
                        } else {
                            if (isRefresh) {
                                rvPdf.refreshComplete();
                                int totSize = mBookDataMOdel.getData().size();
                                for (int i = 0; i < totSize; i++) {
                                    mReferencePageBook.add(i, mBookDataMOdel.getData().get(i));
                                }
                                pageNoFirstIndex = (pageNo - mReferencePageBook.size()) + 5;
                                pageNoLastIndex = (pageNo + mReferencePageBook.size()) - 6;
                            } else {
                                rvPdf.loadMoreComplete();
                                mReferencePageBook.addAll(mBookDataMOdel.getData());

                                pageNoFirstIndex = (pageNo - mReferencePageBook.size()) + 5;
                                pageNoLastIndex = (pageNo + mReferencePageBook.size()) - 6;

                            }
                            mReferencePageAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
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
                    String strUniqueName = null; */
/*mBookDetailsList.get(k).getStrUniqueName()*//*

                    ;
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
                Intent i = new Intent(MyReferencePdfActivity.this, HoldAndSearchActivity.class);
                i.putExtra("model", mBookDataModels);
                startActivity(i);

            } else {
                if (mBookDataModels != null) {
                    myApplication.getmBookDetailsList().add(mBookDataModels);
                    Intent i = new Intent(MyReferencePdfActivity.this, HoldAndSearchActivity.class);
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
        PackageName = MyReferencePdfActivity.this.getPackageName();

        Intent i = getIntent();
        if (i != null) {

            mBookDataModels = (BookListResModel.BookDetailsModel) i.getSerializableExtra("model");

            if (mBookDataModels != null) {
                strBookName = mBookDataModels.getBook_name();
                strWordName = mBookDataModels.getKeyword();
                strBookId = mBookDataModels.getBook_id();
                strPageNo = mBookDataModels.getPage_no();
                strAuthorName = mBookDataModels.getAuthor_name();
                strTranslatorName = mBookDataModels.getTranslator();
                strEditorName = mBookDataModels.getEditor_name();
                strSutraName = mBookDataModels.getStrSutraName();
                strType = mBookDataModels.getFlag();
                mBookDetailsList.add(mBookDataModels);
                Log.e("mBookDataModels----", "" + mBookDataModels);
                Log.e("strPageNo---", "strPageNo---" + strPageNo);
            }

            */
/*if (strBookName != null && strBookName.length() > 0) {
                SharedPrefManager.getInstance(MyReferencePdfActivity.this).saveStringPref(SharedPrefManager.KEY_TITLE_NAME, strBookName);
            }*//*


            if (strPageNo != null && strPageNo.length() > 0) {
                pageNo = Integer.parseInt(strPageNo);
            }

            */
/*if (strWordName != null && strWordName.length() > 0) {
                SharedPrefManager.getInstance(MyReferencePdfActivity.this).saveStringPref(SharedPrefManager.KEY_PREVIOUSTITLE_NAME, strWordName);
            }*//*


            isAppendix = i.getBooleanExtra("isAppendix", false);

        }

        if (isAppendix) {
            tvFooterPageName.setText(strPageName);
            strPageName = i.getStringExtra("page");
            strPageCount = i.getStringExtra("count");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            llBookDetails.setLayoutParams(param);
            llQuickLink.setVisibility(View.VISIBLE);
            tvQuickLink.setText(strPageName);
        } else {
            llBookDetails.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    2.0f
            );
            llBookDetails.setLayoutParams(param);
            llQuickLink.setVisibility(View.GONE);
            Log.e("strPageName---", "strPageName--" + strPageName);
            tvFooterPageName.setText(strPageName);
        }

        if (!ConnectionManager.checkInternetConnection(MyReferencePdfActivity.this)) {
            Toast.makeText(MyReferencePdfActivity.this, "Please check your internet connection..", Toast.LENGTH_SHORT).show();
            return;
        }

        isFirstTime = true;
        Log.e("pageNo---", "pageNo---" + pageNo);
        callBookPageApi(strKeyWordId, strBookId, pageNo, "0", true);

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

        //strBookName = SharedPrefManager.getInstance(MyReferencePdfActivity.this).getStringPref(SharedPrefManager.KEY_TITLE_NAME);
        //strWordName = SharedPrefManager.getInstance(MyReferencePdfActivity.this).getStringPref(SharedPrefManager.KEY_PREVIOUSTITLE_NAME);
        if (isAppendix) {
            if (tvPgCount != null && tvPgCount.length() > 0) {
                tvPgCount.setVisibility(View.VISIBLE);
                tvPgCount.setText(strPageCount);
            }
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append("Book Detail ");
            ssb.setSpan(new ImageSpan(MyReferencePdfActivity.this, R.drawable.ic_baseline_chevron_right_24),
                    ssb.length() - 1,
                    ssb.length(),
                    0);
            ssb.append(strPageName);
            tvPageName.setText(ssb);

        } else {
            if (strSutraName != null && strSutraName.length() > 0) {
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                ssb.append(strWordName + " ");
                ssb.setSpan(new ImageSpan(MyReferencePdfActivity.this, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);

                ssb.append(strSutraName + " ");
                ssb.setSpan(new ImageSpan(MyReferencePdfActivity.this, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);

                ssb.append(strPageNo + " ");
                ssb.setSpan(new ImageSpan(MyReferencePdfActivity.this, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);

                ssb.append(strBookName);
                tvPageName.setText(ssb);
            } else {
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                ssb.append(strWordName + " ");
                ssb.setSpan(new ImageSpan(MyReferencePdfActivity.this, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);

                ssb.append(strBookName + " ");
                ssb.setSpan(new ImageSpan(MyReferencePdfActivity.this, R.drawable.ic_baseline_chevron_right_24),
                        ssb.length() - 1,
                        ssb.length(),
                        0);

                ssb.append(strPageNo);
                tvPageName.setText(ssb);
            }
        }
    }

    private int getScrollPos(ArrayList<BooksDataModel.BookImageDetailsModel> pdfList) {
        int scrollPos = 0;
        if (pdfList != null && pdfList.size() > 0) {
            if (pdfList.size() <= 10 && pageNo < 7) {
                scrollPos = (pageNo - 1);
                pageNoFirstIndex = (pageNo - scrollPos);
                pageNoLastIndex = pdfList.size() - 1;
            } else if (pdfList.size() == 10 && pageNo >= 7) {
                scrollPos = (pdfList.size() / 2);
                pageNoFirstIndex = (pageNo - scrollPos);
                pageNoLastIndex = pageNo + 5;
            }
        }
        return scrollPos;
    }

    private void setPdfData(ArrayList<BooksDataModel.BookImageDetailsModel> pdfList) {

        mReferencePageAdapter = new ReferencePageAdapter(MyReferencePdfActivity.this, pdfList, this);
        rvPdf.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        int scrollPos = getScrollPos(pdfList);
        layoutManager.scrollToPositionWithOffset(scrollPos + 1, 0);
      */
/*  XRecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(MyReferencePdfActivity.this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(scrollPos);
        layoutManager.startSmoothScroll(smoothScroller);*//*

        rvPdf.setVisibility(View.VISIBLE);
        rvPdf.setAdapter(mReferencePageAdapter);
        rvPdf.setLayoutManager(layoutManager);
        rvPdf.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        rvPdf.setRefreshProgressStyle(ProgressStyle.BallPulse);
        rvPdf.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                callBookPageApi(strKeyWordId, strBookId, pageNoFirstIndex, "1", true);
            }

            @Override
            public void onLoadMore() {
                callBookPageApi(strKeyWordId, strBookId, pageNoLastIndex, "2", false);
            }
        });

    }


    private void loadUiElements() {
        // Util.commonKeyboardHide(MyReferencePdfActivity.this);
        llBookDetails = findViewById(R.id.llBookDetails);
        llQuickLink = findViewById(R.id.llQuickLink);
        llBookTitle = findViewById(R.id.llBookTitle);
        rvPdf = findViewById(R.id.rvPdf);
        llShare = findViewById(R.id.llShare);
        llHold = findViewById(R.id.llHold);
        ll_buttons = findViewById(R.id.ll_buttons);
        buttonFilter = findViewById(R.id.button_filter);
        tvFooterPageName = findViewById(R.id.tvtitlePages);
        bookDetailsPages = findViewById(R.id.bookDetailsPages);
        tvQuickLink = findViewById(R.id.tvQuickLink);

    }


    @Override
    public void onImageClick(ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList, int position) {
        String strImage = mPfdList.get(position).getPage_image();
        if (strImage != null && strImage.length() > 0) {
            Intent i = new Intent(MyReferencePdfActivity.this, DownloadFileActivity.class);
            i.putExtra("modelPdf", mPfdList.get(position));
            i.putExtra("modelPdfOtherData", mBookDataModels);
            i.putExtra("image", strImage);
            startActivity(i);
        }
    }

    @Override
    public void onShareImage(ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList, int position, boolean isSelected) {
        if (mPfdList != null && mPfdList.size() > 0) {
            mSelectedPdfPageList = mPfdList;
            String strImage = mPfdList.get(position).getPage_image();
            String strPageNo = mPfdList.get(position).getPageno();
            Log.e(TAG, "strImage---" + strImage);
            if (strImage != null && strImage.length() > 0) {
                if (!isSelected) {
                    for (int i = 0; i < addMyShlefList.size(); i++) {
                        String strUrl = addMyShlefList.get(i).getStrUrl();
                        if (strUrl != null && strUrl.equalsIgnoreCase(strImage)) {
                            addMyShlefList.remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < imageFiles.size(); i++) {
                        Set<String> strImages = imageFiles.get(i).keySet();
                        for (String key : strImages) {
                            if (key.equalsIgnoreCase(strImage)) {
                                imageFiles.remove(i);
                                break;
                            }
                        }
                    }
                } else {
                    AddMyShelfModel mAddShelfModel = new AddMyShelfModel();
                    mAddShelfModel.setStrPageNo(strPageNo);
                    mAddShelfModel.setStrUrl(strImage);
                    addMyShlefList.add(mAddShelfModel);

                    Glide.with(MyReferencePdfActivity.this).load(strImage).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            resource.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
                            HashMap<String, Uri> map = new HashMap<>();
                            map.put(strImage, Uri.parse(MediaStore.Images.Media.insertImage(MyReferencePdfActivity.this.getContentResolver(), resource, "JRL_" + System.currentTimeMillis(), null)));
                            imageFiles.add(map);
                        }
                    });
                }
            }
           */
/* String strImage = mPfdList.get(position).getPage_image();
            if (strImage != null && strImage.length() > 0) {
                Glide.with(MyReferencePdfActivity.this).load(strImage).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        resource.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
                        imageFiles.add(Uri.parse(MediaStore.Images.Media.insertImage(MyReferencePdfActivity.this.getContentResolver(), resource, "", null)));
                    }
                });
            }*//*

        }
    }

    public void getShareDialog() {

        bottomSheetDialog = new BottomSheetDialog(MyReferencePdfActivity.this, R.style.BottomSheetDialogTheme);

        View bottomSheetDialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_share, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        LinearLayout bottomSheetContainer = bottomSheetDialogView.findViewById(R.id.bottomSheetContainer);
        Button btnShare = bottomSheetDialogView.findViewById(R.id.btnShare);
        Button btnMyShelf = bottomSheetDialogView.findViewById(R.id.btnMyShelf);
        Button btnDownload = bottomSheetDialogView.findViewById(R.id.btnDownload);
        TextView tvSelectedImageCount = bottomSheetDialogView.findViewById(R.id.tvSelectedImageCount);
        EditText edtRenameFile = bottomSheetDialogView.findViewById(R.id.edtRenameFile);
        ImageView ivClose = bottomSheetDialogView.findViewById(R.id.ivClose);
        mKeyboardView = bottomSheetDialogView.findViewById(R.id.keyboardView);
        edtRenameFile.setText(strWordName + "," + strBookName + "," + "Page." + strPageNo);
        String strLanguage = SharedPrefManager.getInstance(MyReferencePdfActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        edtRenameFile.requestFocus();
        edtRenameFile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(MyReferencePdfActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, MyReferencePdfActivity.this, strLanguage, bottomSheetDialog, null);
                return false;
            }
        });
        tvSelectedImageCount.setText("" + mImageUriList.size());
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 */
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
                    Toast.makeText(MyReferencePdfActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }

*//*

                String strEdtRenamefile = null;
                strEdtRenamefile = edtRenameFile.getText().toString();

                if (strEdtRenamefile == null || strEdtRenamefile.length() == 0) {
                    strEdtRenamefile = strWordName + strBookName + strPageNo;
                }

                if (strEdtRenamefile != null && strEdtRenamefile.length() > 0) {
                    sharePDFFiles(strEdtRenamefile);
                    bottomSheetDialog.cancel();
                } else {
                    edtRenameFile.setError("Please enter file name");
                    edtRenameFile.requestFocus();
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String strEdtRenamefile = edtRenameFile.getText().toString();
                    if (mImageUriList != null && mImageUriList.size() > 0) {
                        bottomSheetDialog.cancel();
                        downloadAndStoreImages(mImageUriList, strEdtRenamefile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addMyShlefList != null && addMyShlefList.size() > 0) {
                    bottomSheetDialog.cancel();
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
                                if (strUrl != null && strUrl.length() > 0) {
                                    jsonObject.put("url", strUrl);
                                }
                                if (strPageNo != null && strUrl != null) {
                                    myShelfArray.put(jsonObject);
                                } else {
                                    Log.e(TAG, "isProductFile");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (myShelfArray != null && myShelfArray.length() > 0) {
                        String strUserId = SharedPrefManager.getInstance(MyReferencePdfActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        if (strUserId != null && strUserId.length() > 0) {
                            callAddMyShelfApi(myShelfArray, strUserId);
                        }
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

    private void callAddMyShelfApi(JSONArray myShelfArray, String strUserId) {
        Utils.showProgressDialog(MyReferencePdfActivity.this, "Please Wait...", false);
        ApiClient.addMyShelf(strUserId, strBookId, strKeyWordId, strType, Utils.REF_TYPE_REFERENCE_PAGE, "JainELibrary_" + strWordName + "_" + strBookName + "_" + "Page." + strPageNo, null,
                new Callback<AddShelfResModel>() {
                    @Override
                    public void onResponse(Call<AddShelfResModel> call, retrofit2.Response<AddShelfResModel> response) {
                        Utils.dismissProgressDialog();
                        if (response.isSuccessful()) {
                             */
/*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*//*


                            if (response.body().isStatus()) {

                                String strNotes = response.body().getNotes();
                                String strType = response.body().getType();
                                String strTypeRef = response.body().getType_ref();
                                Toast.makeText(MyReferencePdfActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                getInfoDialogs(strNotes, strType, strTypeRef, "Do you want to add notes?");
                            } else {
                                Toast.makeText(MyReferencePdfActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    private void saveIntoMyShelf(ArrayList<Uri> mImageUriList) {
        ArrayList<String> mOldImageBitmapList = new ArrayList<>();
        ArrayList<String> tempBitmapList = new ArrayList<>();

        for (int i = 0; i < mImageUriList.size(); i++) {
            Uri mUri = mImageUriList.get(i);
            String path = FileUtils.getPath(MyReferencePdfActivity.this, mUri);
            if (path != null) {
                mOldImageBitmapList.add(path);
            }
        }

        if (mOldImageBitmapList != null && mOldImageBitmapList.size() > 0) {
            tempBitmapList = SharedPrefManager.getInstance(MyReferencePdfActivity.this).getMyShelfList(SharedPrefManager.KEY_MY_SHELF);
            if (tempBitmapList != null && tempBitmapList.size() > 0) {
                mOldImageBitmapList.addAll(tempBitmapList);
            }
            SharedPrefManager.getInstance(MyReferencePdfActivity.this).saveMyShelfList(SharedPrefManager.KEY_MY_SHELF, mOldImageBitmapList);
            Toast.makeText(MyReferencePdfActivity.this, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
            if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
                this.mImageUriList.clear();
            }
        } else {
            return;
        }

        Log.e("mImageUriList---", "Shelf---" + mOldImageBitmapList.size());
    }

    private void downloadAndStoreImages(ArrayList<Uri> mImageUriList, String strFileName) throws IOException {
        String strFileFinalPath = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        if (!Utils.storeDownloadImage.exists()) {
            Utils.storeDownloadImage.mkdirs();
        }
        for (int i = 0; i < mImageUriList.size(); i++) {
            Uri uri = mImageUriList.get(i);

            strFileFinalPath = strFileName + strFileFinalPath + ".jpg";

            File file = new File(Utils.storeDownloadImage.getAbsolutePath(), strFileFinalPath);

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
        }
        Toast.makeText(MyReferencePdfActivity.this, "Downloaded Successfully " + "\n" + Utils.storeDownloadImage.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
            this.mImageUriList.clear();
        }
    }

    private void shareZipFiles(String strEdtRenamefile) {
        try {
            if (mImageUriList != null && mImageUriList.size() > 0) {

                String strZipperFileName = ZipCompressor.zip(MyReferencePdfActivity.this, mImageUriList, strEdtRenamefile);
                //ZipCompressor.unzip(strZipperFileName, Utils.mediaStorageDir.getAbsolutePath());

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
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Log.e(TAG, "imageFilesss--" + mImageUriList.size());
                    startActivity(Intent.createChooser(intent, shareData));
                }

                if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
                    this.mImageUriList.clear();
                }
            } else {
                Toast.makeText(MyReferencePdfActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
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
                    String strImagePath = FileUtils.getPath(MyReferencePdfActivity.this, mImageUriList.get(i));
                    if (strImagePath != null && strImagePath.length() > 0) {
                        mImagePath.add(strImagePath);
                    }
                }
                if (!Utils.mediaStoragePDfDir.exists()) {
                    Utils.mediaStoragePDfDir.mkdirs();
                }
                String strPdfFile = null;
                if (mImagePath != null && mImagePath.size() > 0) {
                    strPdfFile = PdfCreator.creatPdf(Utils.mediaStoragePDfDir.getAbsolutePath(), strEdtRenamefile, mImagePath);
                }

                if (strPdfFile != null && strPdfFile.length() > 0) {
                    Log.e(TAG, "strZipperFileName--" + strPdfFile);
                    Uri fileUri = Uri.fromFile(new File(strPdfFile));
                    Log.e(TAG, "fileUri--" + fileUri);

                    String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                    String strMessage = " " + strBookName;

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                    intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    intent.setType("application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Log.e(TAG, "imageFilesss--" + mImageUriList.size());
                    startActivity(Intent.createChooser(intent, shareData));
                }

                if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
                    this.mImageUriList.clear();
                }
            } else {
                Toast.makeText(MyReferencePdfActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
    }

    public void callAddHoldSearchKeyword(String strUId, String strBookId, String strKeyword) {
        if (!ConnectionManager.checkInternetConnection(MyReferencePdfActivity.this)) {
            Toast.makeText(MyReferencePdfActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(MyReferencePdfActivity.this, "Please Wait...", false);
      //  ApiClient.addHoldSearchKeyword(strUId, strBookId, strKeyword, strPageNo, strAuthorName, strTranslatorName, strEditorName, new Callback<HoldAndSearchResModel>() {
        ApiClient.addNewHoldSearchKeyword(strUId, strBookId, strKeyWordId, strType, strPageNo, new Callback<HoldAndSearchResModel>() {
            @Override
            public void onResponse(Call<HoldAndSearchResModel> call, retrofit2.Response<HoldAndSearchResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     */
/*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*//*


                    if (response.body().isStatus()) {
                        Toast.makeText(MyReferencePdfActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MyReferencePdfActivity.this, SearchReferenceActivity.class);
                        i.putExtra("model", mBookDataModels);
                        i.putExtra("hold", true);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(MyReferencePdfActivity.this, SearchReferenceActivity.class);
                        i.putExtra("model", mBookDataModels);
                        i.putExtra("hold", true);
                        startActivity(i);
                        Toast.makeText(MyReferencePdfActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
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


    public void getInfoDialogs(String strNotes, String strType, String strTypeRef, String strTite) {
        Dialog dialog = new IosDialog.Builder(this)
                .setTitle("Info")
                .setTitleColor(Color.RED)
                .setTitleSize(20)
                .setMessage(strTite)
                .setMessageColor(Color.parseColor("#D26A1B9A"))
                .setMessageSize(18)
                .setNegativeButtonColor(Color.parseColor("#D26A1B9A"))
                .setNegativeButtonSize(18)
                .setNegativeButton("No", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButtonColor(Color.parseColor("#D26A1B9A"))
                .setPositiveButtonSize(18)
                .setPositiveButton("Yes", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(MyReferencePdfActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strWordName);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }


    public void downloadFile(String strUrl, String strBookName) {

        if (!Utils.mediaStorageCommonPDFDir.exists()) {
            Utils.mediaStorageCommonPDFDir.mkdirs();
        }
        DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(strUrl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        String imagePath = "JRL" + strBookName + System.currentTimeMillis();
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("JRL" + strBookName + System.currentTimeMillis())
                .setDescription("Jrl " + strBookName + "Download")
                .setDestinationInExternalPublicDir(Utils.mediaStorageCommonDir.getAbsolutePath(), imagePath + ".pdf");
        mgr.enqueue(request);
        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

    }

    public void openNote(MyShelfResModel.MyShelfModel myShelfModel) {
        String stType = myShelfModel.getType();
        String strType_Ref = myShelfModel.getType_ref();
        String strNotes = myShelfModel.getNotes();
        String strKeyword = myShelfModel.getType_name();
        Intent i = new Intent(MyReferencePdfActivity.this, NotesActivity.class);
        i.putExtra("strType", stType);
        i.putExtra("strTypeRef", strType_Ref);
        i.putExtra("strNotes", strNotes);
        i.putExtra("strKeyword", strKeyword);
        startActivity(i);
    }


}
*/

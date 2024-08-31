package com.jainelibrary.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.adapter.ReferencePageAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddMyShelfModel;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.BooksDataModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.FileUtils;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wc.widget.dialog.IosDialog;

import org.jetbrains.annotations.Nullable;
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

public class AppendixActivity extends AppCompatActivity implements ReferencePageAdapter.PdfInterfaceListener {
    private static final String TAG = AppendixActivity.class.getSimpleName();
    private static final int APPENDIX_CODE = 1;
    Keyboard mKeyboard;
    CustomKeyboardView mKeyboardView;

    private boolean isFirstTime = false;
    public int strPageNo = 0, pageNoFirstIndex, pageNoLastIndex, pageNo;
    private String PackageName, strKeyword, strBookId, strAppendixName, strBookName, strKeywordName, strBookPageNo;

    private LinearLayout llReferencePage, llBookDetails, llShare;
    private TextView tvPgCount;
    private BottomSheetDialog bottomSheetDialog;
    private XRecyclerView rvPdf;

    private ReferencePageAdapter mReferencePageAdapter;

    private BookListResModel.BookDetailsModel.BookAppendixModel mAppendixDataModels = new BookListResModel.BookDetailsModel.BookAppendixModel();
    private BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();

    private ArrayList<Uri> mImageUriList = new ArrayList<>();
    private ArrayList<HashMap<String, Uri>> imageFiles = new ArrayList<HashMap<String, Uri>>();
    private ArrayList<AddMyShelfModel> addMyShlefList = new ArrayList<>();
    private ArrayList<BooksDataModel.BookImageDetailsModel> mReferencePageBook = new ArrayList<>();
    private String strFlag;
    private View viewReference, viewExport;
    private String strKeywordId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appendix);
        loadUiElements();
        declaration();
        btnEventListener();
    }

    @Override
    public void onImageClick(ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList, int position) {

        String strImage = mPfdList.get(position).getPage_image();
        if (strImage != null && strImage.length() > 0) {
            Intent i = new Intent(AppendixActivity.this, DownloadFileActivity.class);
            i.putExtra("modelPdf", mPfdList.get(position));
            i.putExtra("image", strImage);
            startActivity(i);
        }

    }
// Share image
    @Override
    public void onShareImage(ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList, int position, boolean isSelected) {
        if (mPfdList != null && mPfdList.size() > 0) {
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

                    Glide.with(AppendixActivity.this).asBitmap().load(strImage).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                            resource.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
                            HashMap<String, Uri> map = new HashMap<>();
                            map.put(strImage, Uri.parse(MediaStore.Images.Media.insertImage(AppendixActivity.this.getContentResolver(), resource, "JRL_" + System.currentTimeMillis(), null)));
                            imageFiles.add(map);
                        }
                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            // Handle if the image load is canceled
                        }
                    });
                }
            }
        }
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
        super.onBackPressed();
        if (mImageUriList != null && mImageUriList.size() > 0) {
            mImageUriList.clear();
        }

        if (mKeyboardView != null && mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }


    private void loadUiElements() {
        rvPdf = findViewById(R.id.rvPdf);
        llShare = findViewById(R.id.llShare);
        llBookDetails = findViewById(R.id.llBookDetails);
        viewReference = findViewById(R.id.viewReference);
        viewExport = findViewById(R.id.viewExport);
        llReferencePage = findViewById(R.id.llReferencePage);
    }

    private void setHeader() {

        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        tvPgCount = headerView.findViewById(R.id.tvPgCount);
        tvPgCount.setVisibility(View.GONE);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);

        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tvPageName = headerView.findViewById(R.id.tvPage);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("Book Detail ");
        ssb.setSpan(new ImageSpan(AppendixActivity.this, R.drawable.ic_baseline_chevron_right_24),
                ssb.length() - 1,
                ssb.length(),
                0);
        ssb.append(strAppendixName);
        tvPageName.setText(ssb);

    }

    private void declaration() {
        Util.commonKeyboardHide(AppendixActivity.this);

        Intent i = getIntent();
        if (i != null) {

            mAppendixDataModels = (BookListResModel.BookDetailsModel.BookAppendixModel) getIntent().getSerializableExtra("appendixModel");
            mBookDataModels = (BookListResModel.BookDetailsModel) getIntent().getSerializableExtra("model");
            strFlag = getIntent().getStringExtra("flag");

            if (mAppendixDataModels != null) {
                strAppendixName = mAppendixDataModels.getApendix_name();
                if (mAppendixDataModels.getStarting_page() != null && mAppendixDataModels.getStarting_page().length() > 0) {
                    strPageNo = Integer.parseInt(mAppendixDataModels.getStarting_page());
                }
            }

            if (mBookDataModels != null) {
                Log.e(TAG, "strKeywordId--" + strKeywordId);
                strBookId = mBookDataModels.getBook_id();
                strBookName = mBookDataModels.getBook_name();
                strBookPageNo = mBookDataModels.getPage_no();
                //strAppendixName = mBookDataModels.getName();
                strKeywordId = mBookDataModels.getKeywordId();
                strKeyword = mBookDataModels.getKeyword();
                Log.e(TAG, "strKeyword--" + strKeyword);
            }
        }

        if (strFlag != null && strFlag.equalsIgnoreCase("3")) {
            llShare.setVisibility(View.VISIBLE);
            llReferencePage.setVisibility(View.VISIBLE);
            viewExport.setVisibility(View.VISIBLE);
            viewReference.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    4.0f
            );
            llBookDetails.setLayoutParams(param);
        }


        PackageName = AppendixActivity.this.getPackageName();
        isFirstTime = true;
        Log.e("pageNo---", "pageNo---" + strPageNo);

        callBookPageApi("", "", strBookId, strPageNo, "0", true);
        setHeader();
    }


    private void btnEventListener() {
        llReferencePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppendixActivity.this, ReferencePageActivity.class);
                i.putExtra("page", strAppendixName);
                i.putExtra("count", strPageNo);
                i.putExtra("isAppendix", true);
                i.putExtra("model", mBookDataModels);
                startActivity(i);
            }
        });

        llBookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(AppendixActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
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
                        if (mImageUriList != null && mImageUriList.size() > 0) {
                            getShareDialog();
                        }
                    } else {
                        InfoDialog("Long Press on Image for Select.\nYou can select multiple images, please scroll.");
                    }
                } else {
                    askLogin();
                }

            }
        });
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

        mReferencePageAdapter = new ReferencePageAdapter(AppendixActivity.this, pdfList, this);
        rvPdf.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        int scrollPos = getScrollPos(pdfList);
        layoutManager.scrollToPositionWithOffset(scrollPos + 2, 0);

        if (pdfList != null && pdfList.size() > 0) {
            tvPgCount.setVisibility(View.VISIBLE);
            tvPgCount.setVisibility(View.VISIBLE);
            tvPgCount.setText("" + pdfList.size());
        }

        rvPdf.setVisibility(View.VISIBLE);
        rvPdf.setAdapter(mReferencePageAdapter);
        rvPdf.setLayoutManager(layoutManager);
        rvPdf.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        rvPdf.setRefreshProgressStyle(ProgressStyle.BallPulse);
        rvPdf.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                callBookPageApi("","", strBookId, pageNoFirstIndex, "1", true);
            }

            @Override
            public void onLoadMore() {
                callBookPageApi("","", strBookId, pageNoLastIndex, "2", false);
            }
        });

    }


    private void downloadAndStoreImages(ArrayList<Uri> mImageUriList, String strFileName) throws IOException {
        String strFileFinalPath = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        for (int i = 0; i < mImageUriList.size(); i++) {
            Uri uri = mImageUriList.get(i);

            strFileFinalPath = strFileName + strFileFinalPath + ".jpg";

            File file = new File(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strFileFinalPath);

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
        Utils.showInfoDialog(AppendixActivity.this, "Downloaded Successfully");
        if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
            this.mImageUriList.clear();
        }
    }

    private void sharePDFFiles(String strEdtRenamefile) {
        try {
            if (mImageUriList != null && mImageUriList.size() > 0) {
                List<String> mImagePath = new ArrayList<>();
                for (int i = 0; i < mImageUriList.size(); i++) {
                    String strImagePath = FileUtils.getPath(AppendixActivity.this, mImageUriList.get(i));
                    if (strImagePath != null && strImagePath.length() > 0) {
                        mImagePath.add(strImagePath);
                    }
                }

                String strPdfFile = null;
                if (mImagePath != null && mImagePath.size() > 0) {
                    strPdfFile = PdfCreator.creatPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mImagePath);
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
                Utils.showInfoDialog(AppendixActivity.this, "Please select image");
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
    }


    private void askLogin() {
        Utils.showLoginDialogForResult(AppendixActivity.this, APPENDIX_CODE);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(AppendixActivity.this);
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", dialogClickListener)
                .show();

    }

    public void getShareDialog() {

        bottomSheetDialog = new BottomSheetDialog(AppendixActivity.this, R.style.BottomSheetDialogTheme);

        View bottomSheetDialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_share, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        LinearLayout bottomSheetContainer = bottomSheetDialogView.findViewById(R.id.bottomSheetContainer);
        Button btnShare = bottomSheetDialogView.findViewById(R.id.btnShare);
        Button btnMyShelf = bottomSheetDialogView.findViewById(R.id.btnMyShelf);
        Button btnDownload = bottomSheetDialogView.findViewById(R.id.btnDownload);
        TextView tvSelectedImageCount = bottomSheetDialogView.findViewById(R.id.tvSelectedImageCount);
        EditText edtRenameFile = bottomSheetDialogView.findViewById(R.id.edtRenameFile);
        ImageView ivClose = bottomSheetDialogView.findViewById(R.id.ivClose);
        CustomKeyboardView mKeyboardView = bottomSheetDialogView.findViewById(R.id.keyboardView);
        edtRenameFile.setText(strBookName + "_" + strAppendixName);
        String strLanguage = SharedPrefManager.getInstance(AppendixActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(AppendixActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(AppendixActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, AppendixActivity.this, strLanguage, bottomSheetDialog,null);
//                return false;
            }
        });

        tvSelectedImageCount.setText("" + mImageUriList.size());

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strEdtRenamefile = null;
                strEdtRenamefile = edtRenameFile.getText().toString();

                if (strEdtRenamefile == null || strEdtRenamefile.length() == 0) {
                    strEdtRenamefile = strBookName + "_" + strAppendixName;
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
                        String strUserId = SharedPrefManager.getInstance(AppendixActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        if (strUserId != null && strUserId.length() > 0) {
                        //    callAddMyShelfApi(myShelfArray, strUserId);
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
                        Intent intent = new Intent(AppendixActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strKeyword);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }


  /*  private void callAddMyShelfApi(JSONArray myShelfArray, String strUserId) {
        Utils.showProgressDialog(AppendixActivity.this, "Please Wait...", false);
        ApiClient.addMyShelf(strUserId, strBookId, strKeywordId,  Utils.TYPE_PDF_BOOK_PAGE, Utils.REF_TYPE_REFERENCE_PAGE, "JainELibrary_"+strKeywordName+"_"+strBookName + "_" + strAppendixName,myShelfArray,
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
                                Toast.makeText(AppendixActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                getInfoDialogs(strNotes, strType, strTypeRef, "Do you want to add notes?",strKeywordId);
                            } else {
                                Toast.makeText(AppendixActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("error--", "statusFalse--" + response.body().getMessage());
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
*/
    public void callBookPageApi(String strType, String strKId, String strBId, int strPId, String strDirection, boolean isRefresh) {
        if (!ConnectionManager.checkInternetConnection(AppendixActivity.this)) {
            Utils.showInfoDialog(AppendixActivity.this, "Please check internet connection");
            return;
        }
        ApiClient.getBookPage(strType, strKId, strBId, String.valueOf(strPId), strDirection, "0","0" ,new Callback<BooksDataModel>() {
            @Override
            public void onResponse(Call<BooksDataModel> call, retrofit2.Response<BooksDataModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

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
                        Utils.showInfoDialog(AppendixActivity.this, response.body().getMessage());
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


}

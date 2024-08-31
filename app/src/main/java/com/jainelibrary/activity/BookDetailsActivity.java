package com.jainelibrary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.adapter.BookDetailsAppendixAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.StorageManager;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wc.widget.dialog.IosDialog;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.jainelibrary.utils.Utils.TYPE_PDF_BOOK_PAGE;

public class BookDetailsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, BookDetailsAppendixAdapter.AppendixListener {
    private static final byte BOOK_DETAILS = 1;
    private static final String TAG = BookDetailsActivity.class.getSimpleName();
    LinearLayout llIntroductionPage, llTitlePage, llSuddhiPatrak, llReferencePage;
    private TextView tvNoDataFound, tvTitlePgCount;
    ImageView ivShare,ivBookLogo;
    private String PackageName;
    private String strTitleName, strPreviousName;
    RecyclerView rvAppendixBookDetails;
    TextView tvHintName, tvHintLanguage, tvHintPrakashak, tvHintGranthMala, tvHintAavruti, tvHintPatra,
            tvHintVikramSavant, tvHintIsviSan, tvHintVeerSavant, tvHintUncountedPageNo, tvHintPDFPageNo;
    TextView txtName, txtLanguage,  txtPrakashak, txtGranthmala, txtApurti, txtPatr, txtAuthor, txtVikramSavant,
            txtIsviSan, txtVeerSavant, txtUncountedPageNo, txtPDFPageNo;
    private String strBookImage, strBookLargeImage, strBookId, strBookName, strFlag;
    LinearLayout llName, llAavruti, llGranthmala, llPrakashak,  llLanguage, llPatr, llAuthor, llVikramSavant,
            llIsviSan, llVeerSavant, llUncountedPageNo, llPDFPageNo;
    private CardView cvDetails;
    private ArrayList<BookListResModel.BookDetailsModel> mBookDetails = new ArrayList<>();
//    RelativeLayout rlPdfOption;
//    LinearLayout llReferPage, llDownload, llShare, llMyReference;
    int colorCode;
    private String strUserId;
    String strKeyword;
    String strPdfLink, strPdfUrl;
    BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
    private String strKeywordId, strTypeRef, strFrom;
    String strEdtRenamefile = null;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Log.e(strUserId,"KeywordSearchDetailsActivity");
        PackageName = BookDetailsActivity.this.getPackageName();
        loadUiElements();
        declaration();
        onEventListner();
        setHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Resume", "CustomBackPressed");
        Log.e("CustomBackPressed", "--" + SharedPrefManager.getInstance(BookDetailsActivity.this).getBooleanPreference("CustomBackPressed"));
        if (SharedPrefManager.getInstance(BookDetailsActivity.this).getBooleanPreference("CustomBackPressed")) {
            SharedPrefManager.getInstance(BookDetailsActivity.this).setBooleanPreference("CustomBackPressed", false);
            super.onBackPressed();
        }
    }


    private void declaration() {

        mBookDataModels = (BookListResModel.BookDetailsModel) getIntent().getSerializableExtra("model");
        strFrom = getIntent().getStringExtra("From");
        if (mBookDataModels != null) {
             strKeywordId = mBookDataModels.getKeywordId();
            Log.e(TAG, "strKeywordId--" + strKeywordId);
            strBookId = mBookDataModels.getBook_id();
            strBookName = mBookDataModels.getBook_name();
            strKeyword = mBookDataModels.getKeyword();
            strFlag = mBookDataModels.getFlag();
            strBookImage = mBookDataModels.getBook_image();
            strBookLargeImage = mBookDataModels.getBook_large_image();
            strPdfUrl = mBookDataModels.getPdf_link();
        }else {
            strBookId = getIntent().getStringExtra("bookid");
            Log.e(TAG, "strBookId--" + strBookId);
        }

        strUserId = SharedPrefManager.getInstance(BookDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (strBookId != null && strBookId.length() > 0) {
            callBookDetailsAppendiApi(strBookId, strFlag);
        }

        if (strBookImage != null && strBookImage.length() > 0) {
            Picasso.get().load(strBookImage).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(ivBookLogo);
        }
        /*if (strFlag != null && strFlag.equalsIgnoreCase("0")) {
            rlPdfOption.setVisibility(View.GONE);
            llReferPage.setVisibility(View.VISIBLE);
            colorCode = getResources().getColor(R.color.pink_300);
        } else if (strFlag != null && strFlag.equalsIgnoreCase("1")) {
            rlPdfOption.setVisibility(View.GONE);
            llReferPage.setVisibility(View.VISIBLE);
            colorCode = getResources().getColor(R.color.color_shlok_number_search);
        } else if (strFlag != null && strFlag.equalsIgnoreCase("2")) {
            rlPdfOption.setVisibility(View.GONE);
            llReferPage.setVisibility(View.VISIBLE);
            colorCode = getResources().getColor(R.color.color_index_search);
        } else if (strFlag != null && strFlag.equalsIgnoreCase("3")) {
            rlPdfOption.setVisibility(View.VISIBLE);
            llReferPage.setVisibility(View.GONE);
            strKeyword = strBookName;
            colorCode = getResources().getColor(R.color.color_pdf_store);
        }*/
       /* tvHintName.setTextColor(colorCode);
        tvHintAavruti.setTextColor(colorCode);
        tvHintCode.setTextColor(colorCode);
        tvHintGranthMala.setTextColor(colorCode);
        tvHintPart.setTextColor(colorCode);
        tvHintPatra.setTextColor(colorCode);
        tvHintPrakashak.setTextColor(colorCode);
        tvHintSampandak.setTextColor(colorCode);*/
    }

    private void showShareDialog(View v) {
        PopupMenu popup = new PopupMenu(BookDetailsActivity.this, v);
        popup.setOnMenuItemClickListener(BookDetailsActivity.this);
        popup.inflate(R.menu.share_menu);
        popup.show();
    }

    private void onEventListner() {

        ivBookLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onZoomClick(strBookLargeImage, strBookImage);

            }
        });
        /*llReferencePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(BookDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    showShareDialog(view);
                } else {
                    askLogin();
                }
            }
        });*/
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(BookDetailsActivity.this, BOOK_DETAILS);
    }


    private void loadUiElements() {
        llTitlePage = findViewById(R.id.llTitlePage);
        tvTitlePgCount = findViewById(R.id.txtTitlePgCount);
        llReferencePage = findViewById(R.id.llReferencePage);
        ivShare = findViewById(R.id.ivShare);
        ivBookLogo = findViewById(R.id.ivBookLogo);
        rvAppendixBookDetails = findViewById(R.id.rvAppendixBookDetails);
//        rlPdfOption = findViewById(R.id.rlPdfOption);
//        llReferPage = findViewById(R.id.llReferPage);
//        llMyReference = findViewById(R.id.llMyReference);
//        llShare = findViewById(R.id.llShare);
//        llDownload = findViewById(R.id.llDownload);
        cvDetails = findViewById(R.id.cvDetails);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);
        txtName = findViewById(R.id.txtName);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtLanguage = findViewById(R.id.txtLanguage);
        txtPrakashak = findViewById(R.id.txtPrakashak);
        txtGranthmala = findViewById(R.id.txtGranthmala);
        txtApurti = findViewById(R.id.txtApurti);
        txtPatr = findViewById(R.id.txtPatr);
        llName = findViewById(R.id.llName);
        llAavruti = findViewById(R.id.llAavruti);
        llGranthmala = findViewById(R.id.llGranthmala);
        llPrakashak = findViewById(R.id.llPrakashak);
        llPatr = findViewById(R.id.llPatr);
        llLanguage = findViewById(R.id.llLanguage);
        llAuthor = findViewById(R.id.llAuthor);
        tvHintName = findViewById(R.id.tvHintName);
        tvHintLanguage = findViewById(R.id.tvHintLanguage);
        tvHintPrakashak = findViewById(R.id.tvHintPrakashak);
        tvHintGranthMala = findViewById(R.id.tvHintGranthMala);
        tvHintAavruti = findViewById(R.id.tvHintAavruti);
        tvHintPatra = findViewById(R.id.tvHintPatra);
        tvHintVikramSavant = findViewById(R.id.tvHintVikramSavant);
        tvHintIsviSan = findViewById(R.id.tvHintIsviSan);
        tvHintVeerSavant = findViewById(R.id.tvHintVeerSavant);
        tvHintUncountedPageNo = findViewById(R.id.tvHintUncountedPageNo);
        tvHintPDFPageNo = findViewById(R.id.tvHintPDFPageNo);
        txtVikramSavant = findViewById(R.id.txtVikramSavant);
        txtIsviSan = findViewById(R.id.txtIsviSan);
        txtVeerSavant = findViewById(R.id.txtVeerSavant);
        txtUncountedPageNo = findViewById(R.id.txtUncountedPageNo);
        txtPDFPageNo = findViewById(R.id.txtPDFPageNo);
        llVikramSavant = findViewById(R.id.llVikramSavant);
        llIsviSan = findViewById(R.id.llIsviSan);
        llVeerSavant = findViewById(R.id.llVeerSavant);
        llUncountedPageNo = findViewById(R.id.llUncountedPageNo);
        llPDFPageNo = findViewById(R.id.llPDFPageNo);

        strUserId = SharedPrefManager.getInstance(BookDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);

/*        llMyReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(BookDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                strUserId = SharedPrefManager.getInstance(BookDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                if (isLogin) {
                    callAddMyShelfApi();
                } else {
                    askLogin();
                }
            }
        });

        llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(BookDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {

                    if (strPdfUrl != null && strPdfUrl.length() > 0) {
                        callDownloadMyShelfsApi(strUserId);
                    } else {
                        Toast.makeText(BookDetailsActivity.this, "Pdf not found", Toast.LENGTH_SHORT).show();
                        Log.e("strPdfUrl---", "pdfLink--" + strPdfUrl);
                    }
                } else {
                    askLogin();
                }
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(BookDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    if (strPdfLink != null && strPdfLink.length() > 0) {
                        callShareMyShelfsApi(strUserId);

                    } else {
                        Toast.makeText(BookDetailsActivity.this, "Pdf not found", Toast.LENGTH_SHORT).show();
                        Log.e("strPdfLinkShare---", "pdfLink--" + strPdfLink);
                    }
                } else {
                    askLogin();
                }
            }
        });*/
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.INVISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        if (strFlag != null && strFlag.equalsIgnoreCase("3")){
            tvPageName.setText(strBookName);
        }else{
            tvPageName.setText("Book Info");
        }
    }

    public void onZoomClick(String strImageUrl, String fallbackImage) {
        Intent i = new Intent(BookDetailsActivity.this, ZoomImageActivity.class);
        i.putExtra("image", strImageUrl);
        i.putExtra("fallbackImage", fallbackImage);
        i.putExtra("url", true);
        startActivity(i);
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareData);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareData);
                startActivity(Intent.createChooser(sharingIntent, shareData));
                return true;
            case R.id.download:
                // do your code
                return true;
            case R.id.shelf:
                // do your code
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onDetailsClick(BookListResModel.BookDetailsModel.BookAppendixModel appendixBookResModel, int position) {
        Log.e(TAG, "pageNo before" + appendixBookResModel.getPdf_pages());
        Intent i = new Intent(BookDetailsActivity.this, ReferencePageActivity.class);
        i.putExtra("appendixModel", appendixBookResModel);
        i.putExtra("model", mBookDataModels);
        i.putExtra("isAppendix", true);
        i.putExtra("page", appendixBookResModel.getApendix_name());
        i.putExtra("aid", appendixBookResModel.getAid());
        i.putExtra("page_no", appendixBookResModel.getStarting_page());
        i.putExtra("count", appendixBookResModel.getPdf_pages());
        i.putExtra("flag", strFlag);
        startActivity(i);
    }

    private void setBookDetailsAppendixResponse(ArrayList<BookListResModel.BookDetailsModel> mBookDetailsRes) {
        if (mBookDetailsRes != null && mBookDetailsRes.size() > 0) {
            mBookDetails = mBookDetailsRes;
            tvNoDataFound.setVisibility(View.GONE);
            cvDetails.setVisibility(View.VISIBLE);
            strBookName = mBookDetails.get(0).getBook_name();
            String strAuthorName = mBookDetails.get(0).getAuthor_name();
            String strEditorName = mBookDetails.get(0).getEditor_name();
            String strPublisherName = mBookDetails.get(0).getPublisher_name();
            String strLetter = mBookDetails.get(0).getPatr();
            String strPart = mBookDetails.get(0).getPart();
            String strAavruti = mBookDetails.get(0).getEdition();
            String strGranthMala = mBookDetails.get(0).getGranthmala();
            String strLanguage = mBookDetails.get(0).getLanguage();
            strPdfLink = mBookDetails.get(0).getPdf_link();
            String strVikramSavant = mBookDetails.get(0).getVikram_savant();
            String strIsviSan = mBookDetails.get(0).getIsvi_san();
            String strVeerSavant = mBookDetails.get(0).getVeer_savant();
            String strUncountedPageNo = mBookDetails.get(0).getUncounted_page_no();
            String strPdfPageNo = mBookDetails.get(0).getPdf_page_no();

            if (strBookName != null && strBookName.length() > 0) {
                txtName.setText(strBookName);
            } else {
                llName.setVisibility(View.GONE);
            }

            if (strAuthorName != null && strAuthorName.length() > 0) {
                txtAuthor.setText(strAuthorName);
            } else {
                llAuthor.setVisibility(View.GONE);
            }

            if (strPublisherName != null && strPublisherName.length() > 0) {
                txtPrakashak.setText(strPublisherName);
            } else {
                llPrakashak.setVisibility(View.GONE);
            }


            if (strLetter != null && strLetter.length() > 0) {
                txtPatr.setText(strLetter);
            } else {
                llPatr.setVisibility(View.GONE);
            }

            if (strAavruti != null && strAavruti.length() > 0) {
                txtApurti.setText(strAavruti);
            } else {
                llAavruti.setVisibility(View.GONE);
            }
            if (strGranthMala != null && strGranthMala.length() > 0) {
                txtGranthmala.setText(strGranthMala);
            } else {
                llGranthmala.setVisibility(View.GONE);
            }
            if (strLanguage != null && strLanguage.length() > 0) {
                txtLanguage.setText(strLanguage);
            } else {
                llLanguage.setVisibility(View.GONE);
            }

            if (strVikramSavant != null && strVikramSavant.length() > 0) {
                txtVikramSavant.setText(strVikramSavant);
            } else {
                llVikramSavant.setVisibility(View.GONE);
            }

            if (strIsviSan != null && strIsviSan.length() > 0) {
                txtIsviSan.setText(strIsviSan);
            } else {
                llIsviSan.setVisibility(View.GONE);
            }

            if (strVeerSavant != null && strVeerSavant.length() > 0) {
                txtVeerSavant.setText(strVeerSavant);
            } else {
                llVeerSavant.setVisibility(View.GONE);
            }

            if (strUncountedPageNo != null && strUncountedPageNo.length() > 0) {
                txtUncountedPageNo.setText(strUncountedPageNo);
            } else {
                llUncountedPageNo.setVisibility(View.GONE);
            }

            if (strPdfPageNo != null && strPdfPageNo.length() > 0) {
                txtPDFPageNo.setText(strPdfPageNo);
            } else {
                llPDFPageNo.setVisibility(View.GONE);
            }

            ArrayList<BookListResModel.BookDetailsModel.BookAppendixModel> mAppensixModel = mBookDetails.get(0).getAppendix();
            if (mAppensixModel != null && mAppensixModel.size() > 0) {
                setAppendixData(mAppensixModel);
            }

        }
    }
    public void callBookDetailsAppendiApi(String strBookID, String strFlag) {
        String cacheKey ="_"+ strBookID+ "_"+strFlag;
        boolean notCacheData = true;
        ArrayList<BookListResModel.BookDetailsModel> mBookDetailsCacheModelList = StorageManager.getBookAppendixDetails(cacheKey);
        if (mBookDetailsCacheModelList != null && mBookDetailsCacheModelList.size() > 0) {
            notCacheData = false;
            setBookDetailsAppendixResponse(mBookDetailsCacheModelList);
        }
        if (!ConnectionManager.checkInternetConnection(BookDetailsActivity.this)) {
            if(notCacheData) {
                Utils.showInfoDialog(BookDetailsActivity.this, "Please check internet connection");
            }
            return;
        }
        if(notCacheData) {
            Utils.showProgressDialog(BookDetailsActivity.this, "Please Wait...", false);
        }
        ApiClient.getBookAppendixDetails(strBookID, strFlag, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    BookListResModel bookListResModel = response.body();
                     Log.e(" AppendixDetails  :", new GsonBuilder().setPrettyPrinting().create().toJson(bookListResModel));

                    if (response.body().isStatus()) {
                        mBookDetails = new ArrayList<>();
                        mBookDetails = response.body().getData();
                        if (mBookDetails != null && mBookDetails.size() > 0) {
                            setBookDetailsAppendixResponse(mBookDetails);
                            StorageManager.setBookAppendixDetails(mBookDetails, cacheKey);
                        } else {
                            cvDetails.setVisibility(View.GONE);
                            tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Utils.showInfoDialog(BookDetailsActivity.this, response.body().getMessage());
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

    private void setAppendixData(ArrayList<BookListResModel.BookDetailsModel.BookAppendixModel> mAppensixModel) {
        if (mAppensixModel == null || mAppensixModel.size() == 0) {
            return;
        }
        BookDetailsAppendixAdapter mAppendixAdapter = new BookDetailsAppendixAdapter(BookDetailsActivity.this, mAppensixModel, this,colorCode);
        rvAppendixBookDetails.setHasFixedSize(true);
        rvAppendixBookDetails.setLayoutManager(new LinearLayoutManager(BookDetailsActivity.this));
        rvAppendixBookDetails.setVisibility(View.VISIBLE);
        rvAppendixBookDetails.setAdapter(mAppendixAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("oar", "" + requestCode + " res " + resultCode);

        if (requestCode == BOOK_DETAILS) {
            if (resultCode == RESULT_OK) {
                boolean isLogin = data.getBooleanExtra("isLogin", false);
            }
        }
    }

    private void callDownloadMyShelfsApi(String strUserId) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(BookDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(BookDetailsActivity.this, "Please Wait...", false);

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
                        Log.e("strPdfUrl :", strPdfUrl);
                        //downloadFile(strPdfUrl);
                        Utils.downloadPdf(strBookName, strPdfUrl, BookDetailsActivity.this);
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

    private void callShareMyShelfsApi(String strUserId) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(BookDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(BookDetailsActivity.this, "Please Wait...", false);

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
                        String shareData = " Get Latest JRl Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                        String strMessage = " " + strPdfLink;
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                        intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent, shareData));
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

    private void callAddMyShelfApi() {
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUserId);
        RequestBody bid = RequestBody.create(MediaType.parse("text/*"), strBookId);
        RequestBody kid = RequestBody.create(MediaType.parse("text/*"), strKeywordId);
        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), "filename" + "JainRefLibrary" + "_" + strBookName +  "_" + mBookDetails.size() + "_" + strKeyword);
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "1");
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), TYPE_PDF_BOOK_PAGE);
        Utils.showProgressDialog(BookDetailsActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfs(uid,bid, kid , type, typeref,filename ,null,null, null,null,
                new Callback<AddShelfResModel>() {
                    @Override
                    public void onResponse(Call<AddShelfResModel> call, retrofit2.Response<AddShelfResModel> response) {
                        Utils.dismissProgressDialog();
                        if (response.isSuccessful()) {
                           /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                            if (response.body().isStatus()) {

                              //  String strType = response.body().getType();
                               // String strTypeRef = response.body().getType_ref();
                                //String strKeywordId = response.body().getType_id();
                                Utils.showInfoDialog(BookDetailsActivity.this, "" + response.body().getMessage());
//                                getInfoDialogs("", "1", TYPE_PDF_BOOK_PAGE, "Do you want to add notes?",strKeywordId);
                            } else {
                                Utils.showInfoDialog(BookDetailsActivity.this, "" + response.body().getMessage());
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

    public void downloadFile(String uRl) {

        if (!Utils.mediaStoreDownloadsDir.exists())
            Utils.mediaStoreDownloadsDir.mkdirs();

        DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        String imagePath = "JRL" + strBookName + System.currentTimeMillis();
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("JRL" + strBookName + System.currentTimeMillis())
                .setDescription("Jrl " + strBookName + "Download")
                .setDestinationInExternalPublicDir(Utils.mediaStoreDownloadsDir.getAbsolutePath(), imagePath + ".pdf");
        mgr.enqueue(request);
        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
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
                        Intent intent = new Intent(BookDetailsActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strKeyword);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }}).build();
        dialog.show();
    }

}
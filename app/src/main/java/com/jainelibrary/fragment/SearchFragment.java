package com.jainelibrary.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.activity.BookDetailsActivity;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.NotesActivity;
import com.jainelibrary.activity.ReferencePageActivity;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.adapter.GalleryAdapter;
import com.jainelibrary.adapter.SearchBookStoreAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.model.SearchBookStoreResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jainelibrary.utils.Utils.REF_TYPE_BOOK_PAGE;

public class SearchFragment extends Fragment implements GalleryAdapter.OnImageClickListener {// implements SearchBookStoreAdapter.OnImageClickListener {
    private static final String TAG = SearchFragment.class.getSimpleName();
    private static final int PDF_CODE = 13;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend;
    RecyclerView rvList;
    NestedScrollView nestedScrollView;
    LinearLayout llFilter, llClose, ivShare, llKeywordCount;
    private String strLanguage;
    private EditText etSearchView;
    private String strSearchtext;
    String strFlag = "3";
    private final String book_names[] = {
            "Curd causes of high data usage",
            "Manage data used for video streaming"
    };

    private final String image_urls[] = {
            "http://api.learn2crack.com/android/images/eclair.png",
            "http://api.learn2crack.com/android/images/froyo.png"
    };
    private SearchBookStoreAdapter mViewListAdapter;
    private GalleryAdapter mGalleryListAdapter;
    private ArrayList<SearchBookStoreResModel> viewListResModels = new ArrayList<>();
    private TextView tvNoRecord;
    private String PackageName, strBookName, shareData, strMessage;
    String strEdtRenamefile = null, strUserId, shareText, strPDfUrl, strTypeRef;
    Activity activity;

    int page_no = 1, total_pages;
    private boolean isLoading = false;
    private boolean isFirstTimeCall = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        loadUiElements(view);
        declaration(view);
        return view;
    }

    private void declaration(View view) {
        PackageName = getActivity().getPackageName();
//        etSearchView.requestFocus();
        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null,ivSend);
                Util.hideKeyBoard(getActivity(), etSearchView);
                Utils.showDefaultKeyboardDialog(getActivity());
            }
        });
        etSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                etSearchView.requestFocus();
                etSearchView.getText().clear();
                Util.hideKeyBoard(getActivity(), etSearchView);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Log.e(TAG, "strLanguage--" + strLanguage);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null,ivSend);
            }
        });
        etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                strSearchtext = editable.toString();
                if (strSearchtext == null || strSearchtext.length() == 0) {
                    llFilter.setVisibility(View.VISIBLE);
                    llClose.setVisibility(View.GONE);
                    return;
                } else {
                    ivSend.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_voilet)));
                    llFilter.setVisibility(View.GONE);
                    llClose.setVisibility(View.VISIBLE);
                    if (strSearchtext != null && strSearchtext.length() > 0) {
                        llClose.setVisibility(View.VISIBLE);
                        llFilter.setVisibility(View.GONE);
                    } else {
                        llClose.setVisibility(View.GONE);
                        llFilter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strSearchtext = null;
                etSearchView.getText().clear();
                viewListResModels = new ArrayList<>();
                rvList.setVisibility(View.GONE);
                llFilter.setVisibility(View.VISIBLE);
                llClose.setVisibility(View.GONE);
                tvNoRecord.setVisibility(View.GONE);
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strValue = etSearchView.getText().toString();
                if (strValue != null && strValue.length() > 0) {
                    rvList.setVisibility(View.VISIBLE);
                    if (mKeyboardView.getVisibility() == View.VISIBLE) {
                        mKeyboardView.setVisibility(View.GONE);
                        mKeyboard = null;
                    }

                    page_no = 1;
                    isFirstTimeCall = true;

                    getPdfList(strValue, "0", "0");
                } else {
                    Utils.showInfoDialog(getActivity(), "Please enter value in search box");
                }
            }
        });

        isFirstTimeCall = true;

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight()) {
                    if (!isLoading) {
                        if (page_no < total_pages) {
                            page_no++;
                            isFirstTimeCall = false;

                            getPdfList(etSearchView.getText().toString(), "0", "0");
                        }
                    } else {
                        Log.e("SearchFragment", "loading");
                    }
                }
            }
        });
    }

    private void setViewListData(ArrayList<PdfStoreListResModel.PdfListModel> viewListResModels) {
        if (viewListResModels == null) {
            return;
        }

        if (isFirstTimeCall) {
            rvList.setHasFixedSize(true);
            rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvList.setVisibility(View.VISIBLE);

            mGalleryListAdapter = new GalleryAdapter(getActivity(), viewListResModels, this, new GalleryAdapter.OnImageZoomListener() {
                @Override
                public void onImageClick(int position, String s, String fallbackImage) {
                    SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.IMG_URL, s);
                    s = SharedPrefManager.getInstance(getContext()).getStringPref(SharedPrefManager.IMG_URL);
                    if ((s != null) && (s.length() != 0)) {
                        Intent i = new Intent(getActivity(), ZoomImageActivity.class);
                        i.putExtra("image", s);
                        i.putExtra("fallbackImage", fallbackImage);
                        i.putExtra("url", true);
                        startActivity(i);
                    }
                }
            });
            rvList.setAdapter(mGalleryListAdapter);

//            mViewListAdapter = new SearchBookStoreAdapter(getActivity(), viewListResModels, this);
//            rvList.setAdapter(mViewListAdapter);
        }
        else
        {
            mGalleryListAdapter.newData(viewListResModels);
//            mViewListAdapter.newData(viewListResModels);
        }
    }


    private ArrayList<SearchBookStoreResModel> prepareData() {
        ArrayList<SearchBookStoreResModel> filesModelArrayList = new ArrayList<>();
        for (int i = 0; i < image_urls.length; i++) {
            SearchBookStoreResModel filesModel = new SearchBookStoreResModel();
            filesModel.setBookName(book_names[i]);
            filesModel.setImage_url(image_urls[i]);
            filesModelArrayList.add(filesModel);
        }
        return filesModelArrayList;
    }

    private void loadUiElements(View view) {
        etSearchView = view.findViewById(R.id.etSearchView);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        ivKeyboard = view.findViewById(R.id.ivKeyboard);
        llFilter = view.findViewById(R.id.llFilter);
        llClose = view.findViewById(R.id.llClose);
        ivClose = view.findViewById(R.id.ivClose);
        ivSend = view.findViewById(R.id.ivSend);
        rvList = view.findViewById(R.id.rvList);
        nestedScrollView = view.findViewById(R.id.scroll_view);
        tvNoRecord = view.findViewById(R.id.tvRecords);
        etSearchView.setShowSoftInputOnFocus(false);
        Util.commonKeyboardHide(getActivity());
    }


    private void showPopUpMenu(ImageView ivMenu, PdfStoreListResModel.PdfListModel filesModel) {
        PopupMenu popup = new PopupMenu(getActivity(), ivMenu);
        popup.getMenuInflater().inflate(R.menu.gallery_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String strBookId = filesModel.getBook_id();
                String strKeywordId = filesModel.getKeyword_id();
                strBookName = filesModel.getBook_name();
                String strPdfPageNo = filesModel.getPdf_page_no();
                String strEditorName = filesModel.getEditor_name();
                String strPublisherName = filesModel.getPublisher_name();
                String strTranslator = filesModel.getTranslator();
                String strBookUrl = filesModel.getBook_url();
                String strBookImageUrl = filesModel.getBook_image();
                String strBookLargeImageUrl = filesModel.getBook_large_image();
                strPDfUrl = filesModel.getPdf_url();

                strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

                BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
                mBookDataModels.setKeyword(strBookName);
                mBookDataModels.setBook_id(strBookId);
                mBookDataModels.setBook_name(strBookName);
                if (strPdfPageNo != null && strPdfPageNo.length() > 0) {
                    mBookDataModels.setPdf_page_no(strPdfPageNo);
                    mBookDataModels.setPage_no(strPdfPageNo);
                } else {
                    mBookDataModels.setPdf_page_no("0");
                    mBookDataModels.setPage_no("0");
                }
                if (strPDfUrl != null && strPDfUrl.length() > 0) {
                    mBookDataModels.setPdf_link(strPdfPageNo);
                }
                mBookDataModels.setEditor_name(strEditorName);
                mBookDataModels.setPublisher_name(strPublisherName);
                mBookDataModels.setTranslator(strTranslator);
                mBookDataModels.setBook_url(strBookUrl);
                mBookDataModels.setBook_image(strBookImageUrl);
                mBookDataModels.setBook_large_image(strBookLargeImageUrl);
                mBookDataModels.setFlag(strFlag);

                switch (item.getItemId()) {
                    case R.id.open:
                        Intent is = new Intent(getActivity(), ReferencePageActivity.class);
                        is.putExtra("model", mBookDataModels);
                        startActivity(is);
                        return true;
                    case R.id.book_info:
                        Intent i = new Intent(getActivity(), BookDetailsActivity.class);
                        i.putExtra("model", mBookDataModels);
                        startActivity(i);
                        return true;
                    case R.id.share:
                        if (strPDfUrl != null && strPDfUrl.length() > 0) {
                            shareData = "JRL Book: "+ strBookName;
                            strMessage = "Book Name: "+ strBookName + "\nBook Link:" + strPDfUrl;

                            callShareMyShelfsApi(strUserId, shareData, strMessage, strBookImageUrl);
                        } else {
                            Utils.showInfoDialog(getActivity(), "Pdf not found");
                            Log.e("strPdfLinkShare---", "pdfLink--" + strPDfUrl);
                        }
                        return true;
                    case R.id.download:
                        callDownloadMyShelfsApi(strUserId, strPDfUrl, strBookName);
                        return true;
                    case R.id.my_reference:
                        if (strBookId != null && strBookName != null){
                            UploadFile(strBookId, strBookName,strKeywordId);
                           // callAddMyShelfApi(strBookId, strBookName,strKeywordId);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }


    private void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), PDF_CODE);
    }

    private void callDownloadMyShelfsApi(String strUserId, String strPDfUrl, String strBookName) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);

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
                        if (strPDfUrl != null && strPDfUrl.length() > 0) {
                            Utils.downloadPdf(strBookName, strPDfUrl, getActivity());
                        } else {
                            Utils.showInfoDialog(getActivity(), "Pdf not found");
                            Log.e("strPdfLinkShare---", "pdfLink--" + strPDfUrl);
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

    private void callShareMyShelfsApi(String strUserId, String shareData, String strMessage, String strImageUrl) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);

        ApiClient.shareMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (!response.isSuccessful() || !response.body().isStatus()) {
                    return;
                }

                Utils.shareContentWithImage(getActivity(), shareData, strMessage, strImageUrl);

            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : "+throwable.getMessage());
            }
        });
    }

    private void UploadFile(String strBookId, String strBookName, String strKeywordId) {
        RequestBody bookId = RequestBody.create(MediaType.parse("text/*"), strBookId);
//        RequestBody keywordId = RequestBody.create(MediaType.parse("text/*"), strKeywordId);
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUserId);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), "JainELibrary_"+strBookName+"_"+strKeywordId);
//        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "0");
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), REF_TYPE_BOOK_PAGE);
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.addMyShelfs(uid, bookId,null, null, typeref, null, null,null, null, null, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        String strNotes = response.body().getNotes();
                        String strType = response.body().getType();
                        String strTypeRef = response.body().getType_ref();
                        String strKeywordId = response.body().getType_id();
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
//                        getInfoDialogs(strNotes, strType, strTypeRef, "Do you want to add notes?", strBookName,strKeywordId);
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(getActivity(), "Something went wrong please try again later");
            }
        });
    }


    private void callAddMyShelfApi(String strBookId, String strBookName, String strKeywordId) {
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.addMyShelf(strUserId, strBookId, strKeywordId,  Utils.TYPE_PDF_BOOK_PAGE, Utils.REF_TYPE_BOOK_PAGE, "JainELibrary_"+strKeywordId + "_" + strBookName ,null,
                new Callback<AddShelfResModel>() {
                    @Override
                    public void onResponse(Call<AddShelfResModel> call, retrofit2.Response<AddShelfResModel> response) {
                        Utils.dismissProgressDialog();
                        if (response.isSuccessful()) {
                             /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                            if (response.body().isStatus()) {
                                String strNotes = response.body().getNotes();
                                String strType = response.body().getType();
                                String strTypeRef = response.body().getType_ref();
                                String strKeywordId = response.body().getType_id();
                                Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                                getInfoDialogs(strNotes, strType, strTypeRef, "Do you want to add notes?", strBookName,strKeywordId);
                            } else {
                                Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
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
    public void getInfoDialogs(String strNotes, String strType, String strTypeRef, String strTite, String strBookName, String strKeywordId) {

        Dialog dialog = new IosDialog.Builder(getActivity())
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
                        Intent intent = new Intent(getActivity(), NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strBookName);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }

    public void getPdfList(String strSearch, String strCatId, String strType) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        String strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

        isLoading = true;

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getPdfList(strUserId, strSearch, strCatId, strType, String.valueOf(page_no), new Callback<PdfStoreListResModel>() {
            @Override
            public void onResponse(Call<PdfStoreListResModel> call, retrofit2.Response<PdfStoreListResModel> response) {
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.dismissProgressDialog();

                        total_pages = response.body().getTotal_pages();

                        ArrayList<PdfStoreListResModel.PdfListModel> data = new ArrayList<>();
                        data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);
                            setViewListData(data);
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                            rvList.setVisibility(View.GONE);
                        }

                    } else {
                        Utils.dismissProgressDialog();
                        rvList.setVisibility(View.GONE);
                        tvNoRecord.setVisibility(View.VISIBLE);
                    }
                    isLoading = false;

                } else {
                    Utils.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<PdfStoreListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }


    @Override
    public void onMenuClick(PdfStoreListResModel.PdfListModel filesModel, int position, ImageView ivMenu) {
        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (isLogin) {
            showPopUpMenu(ivMenu, filesModel);
        } else {
            askLogin();
        }


    }
}


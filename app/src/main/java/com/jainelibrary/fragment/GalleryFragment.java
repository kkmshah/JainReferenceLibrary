package com.jainelibrary.fragment;

import static com.jainelibrary.utils.Utils.REF_TYPE_BOOK_PAGE;
import static com.jainelibrary.utils.Utils.convertUrlToBitmap;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.BuildConfig;
import com.jainelibrary.activity.BookDetailsActivity;
import com.jainelibrary.activity.BookReferenceDetailsActivity;
import com.jainelibrary.activity.NotesActivity;
import com.jainelibrary.activity.PdfViewActivity;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.adapter.EndlessRecyclerViewScrollListener;
import com.jainelibrary.adapter.GalleryAdapter;
import com.jainelibrary.adapter.MyReferenceFilesListAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.model.GalleryResModel;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.StorageManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GalleryFragment extends Fragment implements GalleryAdapter.OnImageClickListener {
    private static final int PDF_CODE = 2;
    private static final String TAG = GalleryFragment.class.getSimpleName();
    private final String book_names[] = {
            "Curd causes of high data usage",
            "Manage data used for video streaming"
    };
    private final String image_urls[] = {
            "http://api.learn2crack.com/android/images/eclair.png",
            "http://api.learn2crack.com/android/images/froyo.png"
    };
    String[] spinnerA = {"Book Name Wise", "Author Name Wise", "Publisher Name Wise"};
    String[] spinnerB = {"All", "Dictionary", "History of Literature", "Tatvagyan", "History of Traditional"};
    Spinner spinnerListA, spinnerListB;
    LinearLayout llSpinner;
    RecyclerView rvImage, rvMyShelf;
    //    NestedScrollView nestedScrollView;
    EndlessRecyclerViewScrollListener scrollListener;
    ArrayList<GalleryResModel> viewListResModels = new ArrayList<>();
    String strFlag = "5";
    boolean is_admin;
    int page_no = 1, total_pages;
    String strEdtRenamefile = null, strUserId, shareText, strPDfUrl, strTypeRef;
    Activity activity;
    private TextView tvDisclaimer, tvNoRecord;
    private MyReferenceFilesListAdapter mAdapter;
    private GalleryAdapter mGalleryListAdapter;
    private boolean isLoading = false;
    private boolean isFirstTimeCall = false;
    private boolean isDataLoaded = false;
    private String strSearch;
    private String strType = "0", strCatId = "0",strPrevCatId = "";
    private String PackageName, strBookName, shareData, strMessage;
    private View appBarLayout;
    private View header1, header2;
    private boolean isCatChanged = false;
    private boolean isApiRunning = false;
    private Call<PdfStoreListResModel> pdfApiCallback = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallary, container, false);
        setHeaders();
        loadUiElements(view);
        declarations(view);
        return view;

    }

    private void setHeaders() {
        header1 = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
        appBarLayout = getActivity().findViewById(R.id.appbar);
    }

    private void loadUiElements(View view) {
        spinnerListA = view.findViewById(R.id.spinnerListA);
        spinnerListB = view.findViewById(R.id.spinnerListB);
        //rvList = view.findViewById(R.id.rvList);
        llSpinner = view.findViewById(R.id.llSpinner);
        rvMyShelf = view.findViewById(R.id.rvMyShelf);
        rvImage = view.findViewById(R.id.rvImage);
        tvDisclaimer = view.findViewById(R.id.tvDisclaimer);
        tvNoRecord = view.findViewById(R.id.tvNoRecord);
//        nestedScrollView = view.findViewById(R.id.scroll_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            if (strUserId != null && strUserId.length() > 0) {
                //  callMyShelfListApi(strUId, strFlag);
                //  clearData();
            }
        } else {
            // askLogin();
        }
    }

    private void declarations(View view) {
        PackageName = getActivity().getPackageName();

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerA);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListA.setAdapter(arrayAdapter);

        strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        ApiClient.getUserAdmin(strUserId, new Callback<UserDetailsResModel>() {
            @Override
            public void onResponse(Call<UserDetailsResModel> call, retrofit2.Response<UserDetailsResModel> response) {
//                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    is_admin = response.body().isAdmin();
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });

        callCategoryApi("");
//        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerB);
//        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerListB.setAdapter(arrayAdapter1);

        spinnerListA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strSpinnerA = spinnerA[i];
                boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);

                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Book Name wise")) {
                    strType = "0";
                }
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Author Name wise")) {
                    strType = "1";
                }
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Publisher Name wise")) {
                    strType = "2";
                }

                page_no = 1;
                isFirstTimeCall = true;

                getPdfList(strSearch, strCatId, strType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        isFirstTimeCall = true;

//        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY == view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight()) {
////                    if (!isLoading) {
////                        if (page_no < total_pages) {
////                            page_no++;
////                            isFirstTimeCall = false;
////
////                            getPdfList(strSearch, strCatId, strType);
////                        }
////                    } else {
////                        Log.e("GalleryFragment", "loading");
////                    }
//                }
//            }
//        });

//        spinnerListB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String strSpinnerB = spinnerB[i];
//                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("All")) {
//                    strCatId = "0";
//                }
//                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("Dictionary")) {
//                    strCatId = "1";
//                }
//                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("History of Literature")) {
//                    strCatId = "2";
//                }
//                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("Tatvagyan")) {
//                    strCatId = "3";
//                }
//                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("History of Traditional")) {
//                    strCatId = "4";
//                }
//                getPdfList(strSearch, strCatId, strType);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        /*boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            if (strUId != null && strUId.length() > 0) {
                getPdfList(strSearch,strCatId, strType);
            }
        } else {
            askLogin();
        }*/
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), PDF_CODE);
    }


    public void callCategoryApi(String strKeyword) {
        boolean isCategoryDataLoaded = false;
        ArrayList<CategoryResModel.CategoryModel> mDataListCache = StorageManager.getCategoryListResponse(strUserId);
        if(mDataListCache != null && mDataListCache.size() > 0) {
            setSpinnerBData(mDataListCache);
            isCategoryDataLoaded = true;
            //Toast.makeText(getContext(), "Category list loaded from cache", Toast.LENGTH_SHORT).show();
        }

        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            if(!isCategoryDataLoaded) {
                Utils.showInfoDialog(getActivity(), "Please check internet connection");
            }
            return;
        }

        strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

//        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getAllCategory(strUserId, new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
//                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    CategoryResModel categoryResModel = response.body();
                    Log.e("responseData Category :", new GsonBuilder().setPrettyPrinting().create().toJson(categoryResModel));
                    if (response.body().isStatus()) {

                        ArrayList<CategoryResModel.CategoryModel> mDataList = new ArrayList<CategoryResModel.CategoryModel>();
                        mDataList = response.body().getData();
                        StorageManager.saveCategoryListResponse(mDataList, strUserId);
                        setSpinnerBData(mDataList);

                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void setSpinnerBData(ArrayList<CategoryResModel.CategoryModel> typeList) {
        ArrayList<String> spinnerBList = new ArrayList<>();
        spinnerBList.add("All");

        if (typeList != null && typeList.size() > 0) {
            for (int i = 0; i < typeList.size(); i++) {
                String strType = typeList.get(i).getName();
                spinnerBList.add(strType);
            }
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerBList);
        spinnerListB.setAdapter(adp);

        spinnerListB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                if (position == 0) {
                    //Toast.makeText(getActivity(), "Please select year type", Toast.LENGTH_SHORT).show();
                    strCatId = "0";
                } else {
                    strCatId = typeList.get(position - 1).getId();
                }

                if(strPrevCatId == null || strPrevCatId.length() == 0){
                    strPrevCatId = strCatId;
                    return;
                }
                page_no = 1;
                isFirstTimeCall = true;

                if (pdfApiCallback != null){
                    pdfApiCallback.cancel();
                }

                getPdfList(strSearch, strCatId, strType);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void getPdfList(String strSearch, String strCatId, String strType) {
        String cacheKey = ""+strSearch+"_"+strCatId+"_"+strType;
        ArrayList<PdfStoreListResModel.PdfListModel> data = StorageManager.getBookListResponse(cacheKey);
        if (data != null && data.size() > 0) {
            Log.e(TAG, "PdfData Size : " + data.size());

            for (int i = 0 ; i < data.size(); i++){
                Log.e(TAG, "PdfData BookName : " + data.get(i).getBook_name());
            }

            tvNoRecord.setVisibility(View.GONE);
            rvImage.setVisibility(View.VISIBLE);
           // Toast.makeText(getContext(), "Book list loaded from cache", Toast.LENGTH_SHORT).show();
            setGalleryListData(data);
            isDataLoaded = true;
        }else {
            isDataLoaded = false;
        }
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            if(!isDataLoaded) {
                Utils.showInfoDialog(getActivity(), "Please check internet connection");
            }
            return;
        }

        String strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

        isLoading = true;

        if (isFirstTimeCall && !isDataLoaded)
            Utils.showProgressDialog(getActivity(), "Please Wait...", false);

        pdfApiCallback = ApiClient.getPdfList(strUserId, strSearch, strCatId, strType, String.valueOf(page_no), new Callback<PdfStoreListResModel>() {
            @Override
            public void onResponse(Call<PdfStoreListResModel> call, retrofit2.Response<PdfStoreListResModel> response) {
                if (isFirstTimeCall && !isDataLoaded)
                    Utils.dismissProgressDialog();

                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        total_pages = response.body().getTotal_pages();

                        ArrayList<PdfStoreListResModel.PdfListModel> data = new ArrayList<>();
                        data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            Log.e(TAG, "PdfData Size : " + data.size());

                            for (int i = 0 ; i < data.size(); i++){
                                Log.e(TAG, "PdfData BookName : " + data.get(i).getBook_name());
                            }

                            tvNoRecord.setVisibility(View.GONE);
                            rvImage.setVisibility(View.VISIBLE);
                            setGalleryListData(data);
                            StorageManager.saveBookListResponse(data, cacheKey);
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                            rvImage.setVisibility(View.GONE);
                        }
                    } else {
//                        Utils.dismissProgressDialog();
                        rvImage.setVisibility(View.GONE);
                        tvNoRecord.setVisibility(View.VISIBLE);
                    }
                    isLoading = false;

                    Log.e("Load Data ", "Page " + page_no + " Total Pages " + total_pages);
                    /*if (!isLoading) {
                        Log.e("Load Data ", "Page " + page_no);
                        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED) || getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                            if (page_no < total_pages) {
                                page_no++;
                                isFirstTimeCall = false;

                                Log.e("Load Data1 ", "Page " + page_no);
                                getPdfList(strSearch, strCatId, strType);
                            }
                        }
                    }*/
                } else {
//                    Utils.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<PdfStoreListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
                isApiRunning = false;
            }
        });
    }

    private void setGalleryListData(ArrayList<PdfStoreListResModel.PdfListModel> myShelfResModels) {
        if (isFirstTimeCall) {
            rvImage.setHasFixedSize(true);
            rvImage.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            mGalleryListAdapter = new GalleryAdapter(getActivity(), myShelfResModels, this, new GalleryAdapter.OnImageZoomListener() {
                @Override
                public void onImageClick(int position, String strImage, String fallbackImage) {
                    SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.IMG_URL, strImage);
                    strImage = SharedPrefManager.getInstance(getContext()).getStringPref(SharedPrefManager.IMG_URL);
                    if ((strImage != null) && (strImage.length() != 0)) {
                        Intent i = new Intent(getActivity(), ZoomImageActivity.class);
                        i.putExtra("image", strImage);
                        i.putExtra("fallbackImage", fallbackImage);
                        i.putExtra("url", true);
                        startActivity(i);
                    }
                }
            });
            rvImage.setAdapter(mGalleryListAdapter);
            // Retain an instance so that you can call `resetState()` for fresh searches
            /*scrollListener = new EndlessRecyclerViewScrollListener(rvImage.getLayoutManager()) {

                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
//                loadNextDataFromApi(page);
                    Log.e("onLoadMore", "" + total_pages);
                    if (!isLoading) {
                        if (page_no < total_pages) {
                            page_no++;
                            isFirstTimeCall = false;

                            getPdfList(strSearch, strCatId, strType);
                        }
                    } else {
                        Log.e("GalleryFragment", "loading");
                    }
                }
            };
            // Adds the scroll listener to RecyclerView
            rvImage.addOnScrollListener(scrollListener);*/

//            rvImage.addOnScrollListener(new HidingScrollListener() {
//                @Override
//                public void onHide() {
//                    header1.setTranslationY(-80);
//                    header2.setTranslationY(-40);
//                    header1.setVisibility(View.GONE);
//                    header2.setVisibility(View.GONE);
//                    appBarLayout.setMinimumHeight(0);
//                }
//
//                @Override
//                public void onShow() {
//                    header1.setTranslationY(0);
//                    header2.setTranslationY(0);
//                    header1.setVisibility(View.VISIBLE);
//                    header2.setVisibility(View.VISIBLE);
//                    appBarLayout.setVisibility(View.VISIBLE);
//                }
//            });
        } else {
            Log.e(TAG, "previousDataFound");
            mGalleryListAdapter.newData(myShelfResModels);
        }
    }


    private ArrayList<GalleryResModel> prepareData() {
        ArrayList<GalleryResModel> filesModelArrayList = new ArrayList<>();
        for (int i = 0; i < image_urls.length; i++) {
            GalleryResModel filesModel = new GalleryResModel();
            filesModel.setBookName(book_names[i]);
            filesModel.setImage_url(image_urls[i]);
            filesModelArrayList.add(filesModel);
        }
        return filesModelArrayList;
    }

    private void showPopUpMenu(ImageView ivMenu, PdfStoreListResModel.PdfListModel filesModel) {
        PopupMenu popup = new PopupMenu(getActivity(), ivMenu);
        popup.getMenuInflater().inflate(R.menu.gallery_menu, popup.getMenu());

        if (is_admin) {
            popup.getMenu().findItem(R.id.reference_info).setVisible(true);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String strKeywordId = filesModel.getKeyword_id();
                String strBookId = filesModel.getBook_id();
                strBookName = filesModel.getBook_name();
                String strPdfPageNo = filesModel.getPdf_page_no();
                String strEditorName = filesModel.getEditor_name();
                String strPublisherName = filesModel.getPublisher_name();
                String strTranslator = filesModel.getTranslator();
                String strBookUrl = filesModel.getBook_url();
                String strBookImageUrl = filesModel.getBook_image();
                String strBookLargeImageUrl = filesModel.getBook_large_image();
                Log.e("strBookUrl :", strBookUrl);
                strPDfUrl = filesModel.getPdf_url();
                Log.e("strPdfLink :", strPDfUrl);
                strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

                BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
                mBookDataModels.setKeyword(strBookName);
                mBookDataModels.setKeywordId(strKeywordId);
                mBookDataModels.setBook_id(strBookId);
                mBookDataModels.setBook_name(strBookName);
                if (strPDfUrl != null && strPDfUrl.length() > 0) {
                    mBookDataModels.setPdf_link(strPDfUrl);
                }
                if (strPdfPageNo != null && strPdfPageNo.length() > 0) {
                    mBookDataModels.setPdf_page_no(strPdfPageNo);
                    mBookDataModels.setPage_no(strPdfPageNo);
                } else {
                    mBookDataModels.setPdf_page_no("0");
                    mBookDataModels.setPage_no("0");
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
                        Intent browserIntent = new Intent(getActivity(), PdfViewActivity.class);
                        browserIntent.putExtra("pdf", strPDfUrl);
                        browserIntent.putExtra("bookName", strBookName);
                        startActivity(browserIntent);

//                        Intent is = new Intent(getActivity(), ReferencePageActivity.class);
//                        is.putExtra("model", mBookDataModels);
//                        startActivity(is);
                        return true;
                    case R.id.book_info:
                        Intent i = new Intent(getActivity(), BookDetailsActivity.class);
                        i.putExtra("model", mBookDataModels);
                        startActivity(i);
                        return true;
                    case R.id.reference_info:
                        Intent reference_intent = new Intent(getActivity(), BookReferenceDetailsActivity.class);
                        reference_intent.putExtra("model", mBookDataModels);
                        startActivity(reference_intent);
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
                        if (strBookId != null && strBookName != null) {
                            callAddMyShelfApi(strUserId, strBookId, strKeywordId);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void callDownloadMyShelfsApi(String strUserId, String strPDfUrl, String strBookName) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserId);
        Log.e("strTypeRef :", " " + strTypeRef);
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

    private void callShareMyShelfsApi(String strUserId, String shareData, String strMessage, String strImageUrl) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserId);
        Log.e("strTypeRef :", " " + strTypeRef);
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
                Log.e("onFailure :", "Move All Api : " + throwable.getMessage());
            }
        });
    }

    private void callAddMyShelfApi(String strUserId, String strBookId, String strKeywordId) {
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUserId);
        RequestBody bid = RequestBody.create(MediaType.parse("text/*"), strBookId);
        /*RequestBody kid = RequestBody.create(MediaType.parse("text/*"), strKeywordId);
        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), "filename" + "JainRefLibrary" );
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "1");*/
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), REF_TYPE_BOOK_PAGE);
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.addMyShelfs(uid, bid, null, null, typeref, null, null, null, null, null,
                new Callback<AddShelfResModel>() {
                    @Override
                    public void onResponse(Call<AddShelfResModel> call, retrofit2.Response<AddShelfResModel> response) {
                        Utils.dismissProgressDialog();
                        if (response.isSuccessful()) {
                            /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                            if (response.body().isStatus()) {

                                //  String strType = response.body().getType();
                                // String strTypeRef = response.body().getType_ref();
                                String strKeywordId = response.body().getType_id();
                                Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
//                                getInfoDialogs("", "1", TYPE_PDF_BOOK_PAGE, "Do you want to add notes?",strBookId,strKeywordId);
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

    @Override
    public void onMenuClick(PdfStoreListResModel.PdfListModel filesModel, int position, ImageView ivMenu) {
        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            showPopUpMenu(ivMenu, filesModel);
        } else {
            askLogin();
        }
    }
}


package com.jainelibrary.fragment;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.activity.BookDetailsActivity;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.activity.KeywordHighlightActivity;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.PdfViewActivity;
import com.jainelibrary.activity.YearBookListActivity;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.adapter.MyReferenceBooksGalleryAdapter;
import com.jainelibrary.adapter.MyReferenceBooksListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.BookImageModel;
import com.jainelibrary.model.DeleteMyShelfResModel;
import com.jainelibrary.model.FilesImageModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.StorageManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class BooksFragment extends Fragment implements
        MyReferenceBooksGalleryAdapter.OnImageClickListener,
        MyReferenceBooksListAdapter.MyShelfInterFaceListener {

    private static final int MY_SHELF_CODE = 1;
    String[] spinnerA = {"Recent", "Name wise"};
    String[] spinnerB = {"Gallery", "List"};
    Spinner spinnerListA, spinnerListB;
    ImageView ivSearch, ivOpenList;
    LinearLayout llSpinner;
    CardView cvSearch;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private final String book_names[] = {
            "Curd causes of high data usage",
            "Manage data used for video streaming"
    };
    private final String image_urls[] = {
            "http://api.learn2crack.com/android/images/eclair.png",
            "http://api.learn2crack.com/android/images/froyo.png"
    };
    private EditText etSearchView;
    private String strSearchtext;
    private LinearLayout llFilter, llClose;
    private ImageView ivSend;
    private ImageView ivClose;
    private TextView tvDisclaimer;
    private ArrayList<BookImageModel> bookImageModels = new ArrayList<>();
    private String PackageName;
    RecyclerView rvMyShelf, rvImage;
    private ArrayList<FilesImageModel> helpfulTipsModels = new ArrayList<>();
    private MyReferenceBooksListAdapter mAdapter;
    String strKeyword, strTypeRef, strBookName, strType;
    private String strNote, strOptionId, strOptionPdfUrl, strBookNames;
    boolean isSelectedButNotPdf = false;
    boolean isReferenceSelected = false;
    private String strUsername;
    private boolean isGallery = true;
    private MyReferenceBooksGalleryAdapter mGalleryListAdapter;
    String strUId, strFlag = "3";
    private View header1, header2, appBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        PackageName = getActivity().getPackageName();
        Log.e(strUsername, "BooksFragment");
        loadUiElements(view);
        setHeaders();
        declarations();
        return view;
    }

    private void setHeaders() {
        header1 = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
        appBarLayout = getActivity().findViewById(R.id.appbar);
    }

    private void setGalleryListData(ArrayList<MyShelfResModel.MyShelfModel> myShelfResModels) {
        //rvImage.setHasFixedSize(true);
        rvImage.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mGalleryListAdapter = new MyReferenceBooksGalleryAdapter(getActivity(), myShelfResModels, this);
        rvImage.setAdapter(mGalleryListAdapter);
        rvImage.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                header1.setTranslationY(-80);
                header2.setTranslationY(-40);
                header1.setVisibility(View.GONE);
                header2.setVisibility(View.GONE);
                appBarLayout.setMinimumHeight(0);
            }
            @Override
            public void onShow() {
                header1.setTranslationY(0);
                header2.setTranslationY(0);
                header1.setVisibility(View.VISIBLE);
                header2.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private ArrayList<BookImageModel> prepareData() {
        ArrayList<BookImageModel> bookImageModels = new ArrayList<>();
        for (int i = 0; i < image_urls.length; i++) {
            BookImageModel bookImageModel = new BookImageModel();
            bookImageModel.setBookName(book_names[i]);
            bookImageModel.setImage_url(image_urls[i]);
            bookImageModels.add(bookImageModel);
        }
        return bookImageModels;
    }

    private void loadUiElements(View view) {
        spinnerListA = view.findViewById(R.id.spinnerListA);
        spinnerListB = view.findViewById(R.id.spinnerListB);
        ivSearch = view.findViewById(R.id.ivSearch);
        llSpinner = view.findViewById(R.id.llSpinner);
        cvSearch = view.findViewById(R.id.cvSearch);
        ivOpenList = view.findViewById(R.id.ivOpenList);
        llFilter = view.findViewById(R.id.llFilter);
        llClose = view.findViewById(R.id.llClose);
        ivSend = view.findViewById(R.id.ivSend);
        ivClose = view.findViewById(R.id.ivClose);
        etSearchView = view.findViewById(R.id.etSearchView);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        tvDisclaimer = view.findViewById(R.id.tvDisclaimer);
        rvImage = view.findViewById(R.id.rvImage);
        rvMyShelf = view.findViewById(R.id.rvMyShelf);
    }

    private void declarations() {
        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            Log.e("UserId---", "UserId---" + strUId);
            if (strUId != null && strUId.length() > 0) {
                callMyShelfListApi(strUId, strFlag, isGallery, false);
            }
        } else {
            askLogin();
        }
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);
        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerA);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListA.setAdapter(arrayAdapter);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerB);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListB.setAdapter(arrayAdapter1);
        spinnerListA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strSpinnerA = spinnerA[i];
                boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                final String strValue = etSearchView.getText().toString();
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Recent")) {
                    strFlag = "0";
                    if (isLogin) {
                        if (strValue != null && strValue.length() > 0) {
                            callSearchMyShelfApi(strValue);
                        } else {
                            callMyShelfListApi(strUId, strFlag, isGallery, false);
                        }
                    }
                }
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Name wise")) {
                    strFlag = "1";
                    if (isLogin) {
                        if (strValue != null && strValue.length() > 0) {
                            callSearchMyShelfApi(strValue);
                        } else {
                            callMyShelfListApi(strUId, strFlag, isGallery, false);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerListB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strSpinnerB = spinnerB[i];
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("List")) {
                    final String strValue = etSearchView.getText().toString();
                    if (isLogin) {
                        if (strValue != null && strValue.length() > 0) {
                            callSearchMyShelfApi(strValue);
                        } else {
                            isGallery = false;
                            callMyShelfListApi(strUId, strFlag, isGallery, false);
                        }
                    }
                }

                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("Gallery")) {
                    isGallery = true;
                    callMyShelfListApi(strUId, strFlag, isGallery, false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSpinner.setVisibility(View.GONE);
                cvSearch.setVisibility(View.VISIBLE);
                rvImage.setVisibility(View.GONE);
            }
        });
        ivOpenList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSpinner.setVisibility(View.VISIBLE);
                cvSearch.setVisibility(View.GONE);
            }
        });
        String strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etSearchView.requestFocus();
                etSearchView.getText().clear();
                Util.hideKeyBoard(getActivity(), etSearchView);

                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                //   setSearchHistoryKeywordData();
                return false;
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
                rvImage.setVisibility(View.GONE);
                rvMyShelf.setVisibility(View.GONE);
                llFilter.setVisibility(View.VISIBLE);
                llClose.setVisibility(View.GONE);
                tvDisclaimer.setVisibility(View.GONE);
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strValue = etSearchView.getText().toString();
                if (strValue != null && strValue.length() > 0) {
                    rvImage.setVisibility(View.VISIBLE);
                    if (mKeyboardView.getVisibility() == View.VISIBLE) {
                        mKeyboardView.setVisibility(View.GONE);
                        mKeyboard = null;
                    }
                    callSearchMyShelfApi(strValue);
                } else {
                    Utils.showInfoDialog(getActivity(), "Please enter value in search box");
                }
            }
        });
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), MY_SHELF_CODE);
    }

    private void showPopUpMenu(ImageView ivMenu, ArrayList<MyShelfResModel.MyShelfModel> notesDetailList, int position) {
        PopupMenu popup = new PopupMenu(getActivity(), ivMenu);
        popup.getMenuInflater().inflate(R.menu.books_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.open) {
                    strKeyword = notesDetailList.get(position).getType_name();
                    strTypeRef = notesDetailList.get(position).getType_ref();
                    strBookName = notesDetailList.get(position).getBook_name();
                    strType = notesDetailList.get(position).getType_ref();
                    String strUrl = notesDetailList.get(position).getUrl();
                    if (strType != null && strType.length() > 0) {
                        MyShelfResModel.MyShelfModel myShelfResModel = new MyShelfResModel.MyShelfModel();
                        myShelfResModel = notesDetailList.get(position);
                        String strPage = myShelfResModel.getBookPdfUrl();
                        String strBookName = myShelfResModel.getBook_name();
                        if (strPage != null) {
                            Intent browserIntent = new Intent(getActivity(), PdfViewActivity.class);
                            browserIntent.putExtra("pdf", strPage);
                            browserIntent.putExtra("bookName", strBookName);
                            startActivity(browserIntent);
                                 /*   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strPage));
                                    startActivity(browserIntent);*/
                                   /* Intent i = new Intent(getActivity(), MyReferenceDetailsActivity.class);
                                    i.putExtra("myShelfModel", myShelfResModel);
                                    startActivity(i);*/
                        } else {
                            Utils.showInfoDialog(getActivity(), "Pdf not found");
                        }
                    }
                    return true;
                } else if (itemId == R.id.book_info) {
                    BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
                    mBookDataModels.setKeywordId(notesDetailList.get(position).getBook_id());
                    mBookDataModels.setBook_id(notesDetailList.get(position).getBook_id());
                    mBookDataModels.setBook_name(notesDetailList.get(position).getBook_name());
                    mBookDataModels.setKeyword(notesDetailList.get(position).getBook_name());
                    mBookDataModels.setFlag("6");
                    mBookDataModels.setBook_url(notesDetailList.get(position).getBook_image());

                    mBookDataModels.setBook_image(notesDetailList.get(position).getBook_image());
                    mBookDataModels.setBook_large_image(notesDetailList.get(position).getBook_large_image());
                    Intent i = new Intent(getActivity(), BookDetailsActivity.class);
                    i.putExtra("model", mBookDataModels);
                    startActivity(i);
                    return true;
                } else if (itemId == R.id.share) {
                    if (strOptionPdfUrl != null && strOptionPdfUrl.length() > 0) {
                        String bookImage = notesDetailList.get(position).getBook_image();
                        String shareText = strBookNames + "shared with you by " + strUsername;
//                            share(shareText, strBookNames + "\n" +strOptionPdfUrl, bookImage);
                        callShareMyShelfsApi(shareText, strBookNames + "\n" + strOptionPdfUrl, bookImage);
                    } else {
                        if (isSelectedButNotPdf) {
                            Utils.showInfoDialog(getActivity(), "This reference does not have pdf");
                        } else {
                            Utils.showInfoDialog(getActivity(), "Long press on reference and select reference");
                        }
                    }
                    return true;
                } else if (itemId == R.id.download) {
                    if (strOptionPdfUrl != null && strOptionPdfUrl.length() > 0) {
                        callDownloadMyShelfsApi(strOptionPdfUrl, strBookNames);
                    } else {
                        if (isSelectedButNotPdf) {
                            Utils.showInfoDialog(getActivity(), "This reference does not have pdf");
                        } else {
                            Utils.showInfoDialog(getActivity(), "Long press on reference and select reference");
                        }
                    }
                    return true;
                } else if (itemId == R.id.delete) {
                    strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
                    if (strUId != null && strUId.length() > 0) {
                        if (isReferenceSelected) {
                            String strId = notesDetailList.get(position).getId();
                            if (strId != null && strId.length() > 0) {
                                callDeleteMyShelfApi(strId);
                            } else {
                                Log.e("Id", "Id not found");
                            }
                        } else {
                            Utils.showInfoDialog(getActivity(), "Long press on reference and select reference");
                        }
                    } else {
                        askLogin();
                    }
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    private void callDeleteMyShelfApi(String strId) {
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.deleteMyShelf(strId, new Callback<DeleteMyShelfResModel>() {
            @Override
            public void onResponse(Call<DeleteMyShelfResModel> call, retrofit2.Response<DeleteMyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                        callMyShelfListApi(strUId, strFlag, isGallery, true);
                    } else {
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<DeleteMyShelfResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }

    private void callDownloadMyShelfsApi(String strUrl, String strBookName) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        if (strUrl == null || strUrl.contains(".jpg")) {
            Utils.showInfoDialog(getActivity(), "This reference does not have pdf");
            return;
        }

        strTypeRef = Utils.REF_TYPE_BOOK_PAGE;
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);

        ApiClient.downloadMyShelfs(strUId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    if (response.body().isStatus()) {
                        Utils.downloadPdf(strBookName, strUrl, getActivity());
                    }else{
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

//    public void downloadFile(String strUrl, String strBookName) {
//
//        if (strUrl == null || strUrl.contains(".jpg")) {
//            Utils.showInfoDialog(getActivity(), "This reference does not have pdf");
//            return;
//        }
//        Utils.downloadPdf(strBookName, strUrl, getActivity());
//    }

    private void callShareMyShelfsApi(String shareData, String strMessage, String imageUrl) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_BOOK_PAGE;
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);

        ApiClient.shareMyShelfs(strUId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    if (response.body().isStatus()) {
                        Utils.shareContentWithImage(getActivity(), shareData, strMessage, imageUrl);
                    }else{
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

//    public void share(String shareData, String strMessage, String imageUrl) {
////        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
////        sharingIntent.setType("text/plain");
////        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareData);
////        sharingIntent.putExtra(Intent.EXTRA_TEXT, strMessage);
////        startActivity(Intent.createChooser(sharingIntent, shareData));
//        Utils.shareContentWithImage(getActivity(), shareData, strMessage, imageUrl);
//    }

    private void setMyShelfList(ArrayList<MyShelfResModel.MyShelfModel> myShelfList) {
        if (myShelfList == null || myShelfList.size() == 0) {
            return;
        }
        /*if (myShelfList != null && myShelfList.size() > 0) {
            ivHeaderIcon.setVisibility(View.VISIBLE);
            tvHeaderCount.setVisibility(View.VISIBLE);
            tvHeaderCount.setText("" + myShelfList.size());
        } else {
            llAddItem.setVisibility(View.GONE);
        }
*/
        rvMyShelf.setVisibility(View.VISIBLE);
        //rvMyShelf.setHasFixedSize(true);
        rvMyShelf.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMyShelf.setVisibility(View.VISIBLE);
        mAdapter = new MyReferenceBooksListAdapter(getActivity(), myShelfList, this);
        rvMyShelf.setAdapter(mAdapter);
        rvMyShelf.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                header1.setTranslationY(-80);
                header2.setTranslationY(-40);
                header1.setVisibility(View.GONE);
                header2.setVisibility(View.GONE);
                appBarLayout.setMinimumHeight(0);
            }
            @Override
            public void onShow() {
                header1.setTranslationY(0);
                header2.setTranslationY(0);
                header1.setVisibility(View.VISIBLE);
                header2.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setMyShelfListApiRes(ArrayList<MyShelfResModel.MyShelfModel> myShelfResModels) {
        if (myShelfResModels != null && myShelfResModels.size() > 0) {
            if (isGallery) {
                rvMyShelf.setVisibility(View.GONE);
                rvImage.setVisibility(View.VISIBLE);
                tvDisclaimer.setVisibility(View.VISIBLE);
                tvDisclaimer.setText(myShelfResModels.size() + " Books Found");
                setGalleryListData(myShelfResModels);
            } else {
                rvMyShelf.setVisibility(View.VISIBLE);
                rvImage.setVisibility(View.GONE);
                tvDisclaimer.setVisibility(View.VISIBLE);
                tvDisclaimer.setText(myShelfResModels.size() + " Books Found");
                setMyShelfList(myShelfResModels);
            }
        }
    }

    public void callMyShelfListApi(String strUId, String strFlag, boolean isGallery, boolean skipCache) {

        String cacheKey =  Utils.REF_TYPE_BOOK_PAGE+ "_"+strUId+"_"+strFlag+"_"+ (isGallery ? "1": "0");
        boolean notCacheData = true;
        ArrayList<MyShelfResModel.MyShelfModel> myShelfCacheResModels = StorageManager.getMyShelfListResponse(cacheKey, skipCache);
        if (myShelfCacheResModels != null && myShelfCacheResModels.size() > 0) {
            notCacheData = false;
            setMyShelfListApiRes(myShelfCacheResModels);
        }
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            if(notCacheData)
                Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        if(notCacheData)
            Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getMyShelfList(strUId, strFlag, Utils.REF_TYPE_BOOK_PAGE, new Callback<MyShelfResModel>() {
            @Override
            public void onResponse(Call<MyShelfResModel> call, retrofit2.Response<MyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    MyShelfResModel myShelfResModel = response.body();
                    Log.e("responseBooksData :", new GsonBuilder().setPrettyPrinting().create().toJson(myShelfResModel));
                    if (response.body().isStatus()) {
                        ArrayList<MyShelfResModel.MyShelfModel> myShelfResModels = new ArrayList<>();
                        myShelfResModels = response.body().getData();
                        if (myShelfResModels != null && myShelfResModels.size() > 0) {
                            setMyShelfListApiRes(myShelfResModels);
                            StorageManager.setMyShelfListResponse(myShelfResModels, cacheKey);
                        } else {
                            rvMyShelf.setVisibility(View.GONE);
                            rvImage.setVisibility(View.GONE);
                        }
                    } else {
                        rvMyShelf.setVisibility(View.GONE);
                        rvImage.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public void callSearchMyShelfApi(String strKeyword) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getSearchMyShelf(strUId, strKeyword, strFlag, Utils.REF_TYPE_BOOK_PAGE, new Callback<MyShelfResModel>() {
            @Override
            public void onResponse(Call<MyShelfResModel> call, retrofit2.Response<MyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<MyShelfResModel.MyShelfModel> myShelfResModels = new ArrayList<>();
                        myShelfResModels = response.body().getData();
                        if (myShelfResModels != null && myShelfResModels.size() > 0) {
                            tvDisclaimer.setVisibility(View.VISIBLE);
                            tvDisclaimer.setText(myShelfResModels.size() + " Books Found");
                            rvMyShelf.setVisibility(View.VISIBLE);
                            rvImage.setVisibility(View.GONE);
                            setMyShelfList(myShelfResModels);
                        } else {
                            rvMyShelf.setVisibility(View.GONE);
                        }
                    } else {
                        rvMyShelf.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onMenuClick(ArrayList<MyShelfResModel.MyShelfModel> notesDetailList, int position, ImageView ivMenu) {
        String strId = notesDetailList.get(position).getId();
        String strUrl = notesDetailList.get(position).getBookPdfUrl();
        String strBookName = notesDetailList.get(position).getBook_name();
        String strKeywords = notesDetailList.get(position).getType_name();
        String strTypes = notesDetailList.get(position).getType();
        String strTypeRefs = notesDetailList.get(position).getType_ref();
        String strNotes = notesDetailList.get(position).getNotes();

        isReferenceSelected = true;

        if (strNotes != null && strNotes.length() > 0) {
            strNote = strNotes;
        }
        if (strKeywords != null && strKeywords.length() > 0) {
            strKeyword = strKeywords;
        }
        if (strTypes != null && strTypes.length() > 0) {
            strType = strTypes;
        }
        if (strTypeRefs != null && strTypeRefs.length() > 0) {
            strTypeRef = strTypeRefs;
        }
        if (strId != null && strId.length() > 0) {
            strOptionId = strId;
        }
        if (strUrl != null && strUrl.length() > 0) {
            strOptionPdfUrl = strUrl;
        } else {
            isSelectedButNotPdf = true;
        }
        if (strBookName != null && strBookName.length() > 0) {
            strBookNames = strBookName;
        }
        showPopUpMenu(ivMenu, notesDetailList, position);
    }

    @Override
    public void onDetailsClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position) {
   /*     strKeyword = searchList.get(position).getKeyword();
        strTypeRef = searchList.get(position).getType_ref();
        strBookName = searchList.get(position).getBook_name();
        strType = searchList.get(position).getType_ref();
        String strUrl = searchList.get(position).getUrl();
        if (strType != null && strType.length() > 0) {
            if (strType.equalsIgnoreCase("1")) {
                MyShelfResModel.MyShelfModel myShelfResModel = new MyShelfResModel.MyShelfModel();
                myShelfResModel = searchList.get(position);
                int strPage = myShelfResModel.getPage_no().size();
                if (strPage != 0 && strPage > 0) {
                    Intent i = new Intent(getActivity(), MyReferenceDetailsActivity.class);
                    i.putExtra("myShelfModel", myShelfResModel);
                    startActivity(i);
                }
            } else {
                //selectOption(searchList.get(position));
            }
        }*/
    }

    public void onZoomClick(MyShelfResModel.MyShelfModel book) {
        Intent i = new Intent(getActivity(), ZoomImageActivity.class);
        String strImageUrl = book.getBook_large_image();
        String fallbackImage = book.getBook_image();
        i.putExtra("image", strImageUrl);
        i.putExtra("fallbackImage", fallbackImage);
        i.putExtra("url", true);
        startActivity(i);
    }

    @Override
    public void onNotesClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position) {

    }

    @Override
    public void onOptionClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position) {

    }

    @Override
    public void onMenuGalleryClick(ArrayList<MyShelfResModel.MyShelfModel> notesDetailList, int position, ImageView ivMenu) {

        String strId = notesDetailList.get(position).getId();
        String strUrl = notesDetailList.get(position).getBookPdfUrl();
        String strBookName = notesDetailList.get(position).getBook_name();
        String strKeywords = notesDetailList.get(position).getType_name();
        String strTypes = notesDetailList.get(position).getType();
        String strTypeRefs = notesDetailList.get(position).getType_ref();
        String strNotes = notesDetailList.get(position).getNotes();

        isReferenceSelected = true;

        if (strNotes != null && strNotes.length() > 0) {
            strNote = strNotes;
        }
        if (strKeywords != null && strKeywords.length() > 0) {
            strKeyword = strKeywords;
        }
        if (strTypes != null && strTypes.length() > 0) {
            strType = strTypes;
        }

        if (strTypeRefs != null && strTypeRefs.length() > 0) {
            strTypeRef = strTypeRefs;
        }

        if (strId != null && strId.length() > 0) {
            strOptionId = strId;
        }

        if (strUrl != null && strUrl.length() > 0) {
            strOptionPdfUrl = strUrl;
        } else {
            isSelectedButNotPdf = true;
        }
        if (strBookName != null && strBookName.length() > 0) {
            strBookNames = strBookName;
        }
        showPopUpMenu(ivMenu, notesDetailList, position);
    }
}

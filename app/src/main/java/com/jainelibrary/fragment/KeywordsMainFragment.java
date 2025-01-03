package com.jainelibrary.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.Constantss;
import com.jainelibrary.IOnBackPressed;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.activity.FilterCategoriesActivity;
import com.jainelibrary.activity.FilterMenuActivity;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.activity.KeywordSearchDetailsActivity;
import com.jainelibrary.activity.SearchReferenceActivity;
import com.jainelibrary.adapter.KeywordSearchViewAdapter;
import com.jainelibrary.adapter.SearchHistoryAdapter;
import com.jainelibrary.extraModel.SearchHistoryModel;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.model.KeywordModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.CheckMyShelfFileNameResModel;
import com.jainelibrary.retrofitResModel.CreatePdfFileUrlResModel;
import com.jainelibrary.retrofitResModel.KeywordSearchModel;
import com.jainelibrary.retrofitResModel.SearchOptionResModel;
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

import static android.app.Activity.RESULT_OK;
import static com.jainelibrary.Constantss.STR_KEYWORD;
import static com.jainelibrary.utils.Utils.REF_TYPE_REFERENCE_PAGE;

public class KeywordsMainFragment extends Fragment implements IOnBackPressed, KeywordSearchViewAdapter.SearchInterfaceListener, PopupMenu.OnMenuItemClickListener, FilterDialogFragment.setSelectedBooks, SearchHistoryAdapter.SearchHistoryInterfaceListener {

    private static final int KEYWORD_SEARCH = 1;
    private static final int KEYWORD_FILTER = 2;
    private final String TAG = KeywordsMainFragment.this.getClass().getSimpleName();
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivSimillarKeyword, ivClose, ivKeyboard, ivSend;
    RecyclerView rvList, rvSearchHistoryList;
    LinearLayout llFilter, llClose, ivExport, llKeywordCount;
    String[] language = {"English", "Gujarati", "Hindi"};
    LinearLayout ll_buttons;
    String strBookIds = "";
    String strOptionTypeID = "";
    String strKeyword;
    ArrayList<SearchHistoryModel> mSearchHistoryModelList = new ArrayList<>();
    ArrayList<SearchHistoryModel> mSearchKeyword = new ArrayList<>();
    boolean isLogin = false;
    View headerView, header2;
    AppBarLayout appBarLayout;
    int page = 1, totalPages = 1, totalAllResults = 0;
    LinearLayout llLensSearch,llFocusSearch,llShowCount, llNewFilter;
    LinearLayout llLens,llFocus;
    TextView tvFocusCounts, tvLensCounts, tvFiltersCounts,tvFocusCountsSearch,tvLensCountsSearch;
    private EditText etSearchView;
    private String strLanguage;
    // private FloatingActionButton fabFilter;
    //private SearchAdapter mSearchListAdapter;
    private KeywordSearchViewAdapter mSearchListAdapter;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private String strSearchtext;
    private TextView buttonFilter;
    private int selected;
    private ArrayList<KeywordModel> keywordList = new ArrayList<>();
    private String PackageName;
    private String strCatName, strBookCount;
    private TextView tvKeywordCount;
    private int totalCount = 0;
    private String selectedString = "";
    private LinearLayout llAddItem;

    private LinearLayout btnExactMatchTab;

    private LinearLayout btnSimilarMatchTab;

    private LinearLayout btnAllMatchTab;

    private TextView tvExactMatchTab;

    private TextView tvSimilarMatchTab;

    private TextView tvAllMatchTab;
    private TextView tvHeaderCount;
    private ImageView ivHeaderIcon;
    private ArrayList<KeywordSearchModel.KeywordModel> mKeywordList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private String strUsername;
    private String strUId;

    private String searchFor = "1";
    private NestedScrollView nestedScroll;
    private boolean isLoading = false;
    private String reqSearchVale;
    private boolean isFirstPage = true;
    private ArrayList<KeywordSearchModel.KeywordModel> tempKeywordList = new ArrayList<>();
    private String lastSearchValue;
    private ArrayList<KeywordSearchModel.KeywordModel> keywordSearchModel = new ArrayList<>();
    private ArrayList<KeywordSearchModel.KeywordModel> allKeywordDataList = new ArrayList<>();
    String strEdtRenamefile = null, strUserId, shareText, strPdfFile, strTypeRef;
    Activity activity;
    String tempKeyword, strPdfLink;

    public KeywordsMainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyword, container, false);
        PackageName = getActivity().getPackageName();
        loadUiElements(view);
        setHeader();
        declaration();
        onEventClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onEventClickListener() {
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyBoard(getActivity(), etSearchView);
                Utils.showDefaultKeyboardDialog(getActivity());
            }
        });

        btnExactMatchTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchFor.equals("1")) {
                    return;
                }
                searchFor="1";
                getSearch(1);
            }
        });
        btnSimilarMatchTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchFor.equals("2")) {
                    return;
                }
                searchFor="2";
                getSearch(1);
            }
        });
        btnAllMatchTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchFor.equals("0")) {
                    return;
                }
                searchFor="0";

                getSearch(1);
            }
        });

        tvFocusCounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("1");
            }
        });

        tvLensCounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("0");
            }
        });

        llFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("1");
            }
        });

        llLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("0");
            }
        });

        llFocusSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("1");
            }
        });

        llLensSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("0");
            }
        });

        etSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Util.hideKeyBoard(getActivity(), etSearchView);
                ll_buttons.setVisibility(View.GONE);
                buttonFilter.setVisibility(View.GONE);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                //   setSearchHistoryKeywordData();

                strSearchtext = etSearchView.getText().toString();
                if (strSearchtext == null || strSearchtext.length() == 0) {
                    llFilter.setVisibility(View.VISIBLE);
                    llClose.setVisibility(View.GONE);
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

        etSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    checkLastKeywordSameAsCurrentKeyword();
                    return true;
                }
                return false;
            }
        });

        ivSend.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                Util.hideKeyBoard(getActivity(), etSearchView);
                Log.e("search", "search");
                ll_buttons.setVisibility(View.GONE);
                buttonFilter.setVisibility(View.GONE);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
//                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                checkLastKeywordSameAsCurrentKeyword();
                return false;
            }
        });
        etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonFilter.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonFilter.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.GONE);
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
                        /* filter(strSearchtext);*/
                    } else {
                        llClose.setVisibility(View.GONE);
                        llFilter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Util.hideKeyBoard(getActivity(), etSearchView);
                Log.e("search", "search");
                ll_buttons.setVisibility(View.GONE);
                buttonFilter.setVisibility(View.GONE);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
//                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);


                checkLastKeywordSameAsCurrentKeyword();

            }
        });

        mKeyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int i) {
                Log.e("Tag i", i + "");
                if (mKeyboardView.getVisibility() == View.VISIBLE)
                    if (i == 151) {
                        Log.e("Search", "search key Pressed");
                    }
                Log.e("Tag i", i + "");
            }

            @Override
            public void onRelease(int i) {

            }

            @Override
            public void onKey(int i, int[] ints) {
                Log.e("Tag i", i + "");
                if (mKeyboardView.getVisibility() == View.VISIBLE)
                    if (i == 151) {
                        Log.e("Search", "search key Pressed");
                    }
                Log.e("Tag i", i + "");
            }

            @Override
            public void onText(CharSequence charSequence) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }


        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("oar", "" + requestCode + " res " + resultCode);
        if (requestCode == KEYWORD_SEARCH) {
            if (resultCode == RESULT_OK) {
                boolean isLogin = data.getBooleanExtra("isLogin", false);
                strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            }
        }

        if (requestCode == KEYWORD_FILTER) {
            if (resultCode == RESULT_OK) {
                totalCount = data.getIntExtra("totalCount", 0);
                strBookIds = data.getStringExtra("strBookIds");
                strOptionTypeID = data.getStringExtra("strOptionTypeId");
                Log.e("strBookIds", "onActivity---" + strBookIds);

                getFocusLensDialogData();
                String strValue = etSearchView.getText().toString();
                if (strValue != null && strValue.length() > 0) {
                    showFilterOption();
                    totalCount = 0;
                    getSearch(1);
                } else {
                    Utils.showInfoDialog(getActivity(), "Please enter value in search box");
                    ll_buttons.setVisibility(View.GONE);
                    llShowCount.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    private void showFilterOption() {
        String filter_data = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_FILTERS_DATA);
        String total_filter_data = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_TOTAL_FILTERS_DATA);

        tvFiltersCounts.setText(filter_data + "/" + total_filter_data);

        /*if (totalCount == 0) {
            strBookIds = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_FILTER_CAT_IDS);
            ArrayList<String> bookList = SharedPrefManager.getInstance(getActivity()).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS);
            if (bookList != null && bookList.size() > 0) {
                totalCount = bookList.size();
            }
        }

        if (strOptionTypeID == null || strOptionTypeID.length() == 0) {
            strOptionTypeID = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_FILTER_LENS_IDS);
        }

        ll_buttons.setVisibility(View.VISIBLE);
        if (totalCount != 0 && totalCount > 0) {
            tvCategory.setText("Selected Books");
            tvCount.setText("" + totalCount);
            ivNext.setVisibility(View.VISIBLE);
            tvCategory.setVisibility(View.VISIBLE);
            tvCount.setVisibility(View.VISIBLE);
            tvBooks.setVisibility(View.VISIBLE);
            tvBooks.setText("Books");
        } else {
            ivNext.setVisibility(View.GONE);
            tvCategory.setVisibility(View.GONE);
            tvCount.setVisibility(View.GONE);
            tvBooks.setText("Select");
        }*/
    }


    @Override
    public boolean onBackPressed() {

        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
            buttonFilter.setVisibility(View.VISIBLE);
            llShowCount.setVisibility(View.VISIBLE);
            ll_buttons.setVisibility(View.GONE);
        } else {
            Intent i = new Intent(getActivity(), SearchReferenceActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish();
        }

        return false;
    }


    private void declaration() {

        mSearchKeyword = SharedPrefManager.getInstance(getActivity()).getSearchkeywordHistory(SharedPrefManager.KEY_KEYWORD_HISTORY);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        buttonFilter.setVisibility(View.GONE);
        ll_buttons.setVisibility(View.GONE);
        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);

        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
        }
        ivKeyboard.setVisibility(View.VISIBLE);

        tempKeyword = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LAST_SEARCH_KEYWORD);
        if(tempKeyword != null && tempKeyword.length() > 0){
            etSearchView.setText(tempKeyword);
            checkLastKeywordSameAsCurrentKeyword();
            etSearchView.setShowSoftInputOnFocus(false);
        } else {
            llShowCount.setVisibility(View.VISIBLE);
        }

        strOptionTypeID = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_FILTER_LENS_IDS);

//        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FOCUS_DATA, "");
//        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_LENS_DATA, "");

        getFocusLensDialogData();
    }

    private void getFocusLensDialogData() {
        //llShowCount.setVisibility(View.VISIBLE);
        String strFocusCounts = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_FOCUS_DATA);
        String strLensCounts = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LENS_DATA);

        //Log.e("FocusCount--", strFocusCounts);
        if (strFocusCounts != null && strFocusCounts.length() > 0) {
            tvFocusCounts.setText(strFocusCounts);
            tvFocusCountsSearch.setText(strFocusCounts);
        } else {
            callCategoryApi("");
            /*if (filterFocusList != null && filterFocusList.length() > 0) {
                tvFocusCounts.setText(":  0/" + filterFocusList);
            }*/
        }

        if (strLensCounts != null && strLensCounts.length() > 0) {
            tvLensCounts.setText(strLensCounts);
            tvLensCountsSearch.setText(strLensCounts);
        } else {
            callSearchOptionApi();
            /*if (filterLensList != null && filterLensList.length() > 0) {
                tvLensCounts.setText(":  0/" + filterLensList);
            }*/
        }
    }

    private void checkLastKeywordSameAsCurrentKeyword() {


        strKeyword = etSearchView.getText().toString();
        /*if (strKeyword != null && strKeyword.length() > 0) {
            if (tempKeyword != null && !tempKeyword.equalsIgnoreCase(strKeyword)) {
                SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_CAT_IDS, "");
                SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
                strBookIds = "";
                totalCount = 0;
            }
        }*/

        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_CAT_IDS, "");
        SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
        strBookIds = "";
        totalCount = 0;
        getSearch(1); // false
        callCategoryFilterApi(strKeyword);
    }

    void getSearch(int page) {
        Util.hideKeyBoard(getActivity(), etSearchView);
        final String strValue = etSearchView.getText().toString();
        int activeTabColor = getResources().getColor(R.color.button_color);
        int tabColor = getResources().getColor(R.color.gray);
        btnAllMatchTab.setBackgroundColor(searchFor.equals("0") ? activeTabColor : tabColor);
        btnExactMatchTab.setBackgroundColor(searchFor.equals("1") ? activeTabColor : tabColor);
        btnSimilarMatchTab.setBackgroundColor(searchFor.equals("2") ? activeTabColor : tabColor);

        if (strValue != null && strValue.length() > 0) {
            rvSearchHistoryList.setVisibility(View.GONE);
            mSearchHistoryModelList = new ArrayList<>();
            mSearchHistoryModelList = SharedPrefManager.getInstance(getActivity()).getSearchHistoryList(SharedPrefManager.KEY_SAVE_HISTORY);
            if (mSearchHistoryModelList != null && mSearchHistoryModelList.size() > 0) {
                boolean isExist = keywordHistoryExistsOrNot(strValue, mSearchHistoryModelList);
                if (!isExist) {
                    SearchHistoryModel mSearchHistoryModel = new SearchHistoryModel();
                    mSearchHistoryModel.setStrSearchKeywordName(strValue);
                    mSearchHistoryModelList.add(mSearchHistoryModel);
                    SharedPrefManager.getInstance(getActivity()).saveSearchLocalHistory(SharedPrefManager.KEY_SAVE_HISTORY, mSearchHistoryModelList);
                }
            } else {
                mSearchHistoryModelList = new ArrayList<>();
                SearchHistoryModel mSearchHistoryModel = new SearchHistoryModel();
                mSearchHistoryModel.setStrSearchKeywordName(strValue);
                mSearchHistoryModelList.add(mSearchHistoryModel);
                SharedPrefManager.getInstance(getActivity()).saveSearchLocalHistory(SharedPrefManager.KEY_SAVE_HISTORY, mSearchHistoryModelList);
            }

            if (mKeyboardView.getVisibility() == View.VISIBLE) {
                mKeyboardView.setVisibility(View.GONE);
                mKeyboard = null;
            }

            if (lastSearchValue == null  || !lastSearchValue.equalsIgnoreCase(strValue)) {
                tvExactMatchTab.setText("Exact" );
                tvSimilarMatchTab.setText("Similar");
                tvAllMatchTab.setText("All" );
                totalPages = 1;
            }

            buttonFilter.setVisibility(View.VISIBLE);
            ll_buttons.setVisibility(View.VISIBLE);
            llShowCount.setVisibility(View.GONE);

            String userUid = (strUId != null) && (strUId.length() != 0) ? strUId : "0";
            callKeywordDataApi(searchFor, strValue, String.valueOf(page), userUid);
        } else {
            Utils.showInfoDialog(getActivity(), "Please enter value in search box");
        }
    }

    private boolean keywordHistoryExistsOrNot(String strName, ArrayList<SearchHistoryModel> mSearchHistoryModelList) {
        for (SearchHistoryModel i : mSearchHistoryModelList) {
            if (i.getStrSearchKeywordName().equals(strName)) {
                return true;
            }
        }
        return false;
    }

    private void callKeywordDataApi(String searchForParam, String strValue, String strPageNo, String strUId) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        if(isLoading && reqSearchVale.equals(strValue + searchForParam)) {
            return;
        }
        isFirstPage = strPageNo.equals("1");
        reqSearchVale = strValue + searchForParam;

        isLoading = true;
        if (isFirstPage) {
            Utils.showProgressDialog(getActivity(), "Please Wait...", false);
            JRL.isKeywordLoading = true;
        }

        page = Integer.parseInt(strPageNo);

        boolean isSimilarData = searchForParam.equals("2");

        Log.e("strBookIds", "keywords---" + strBookIds);
        String langType = Util.lang_code;
        if (langType == null || langType.length() == 0) {
            langType = "0";
        }

        String bookInfo = "0";
        //strPageNo = "";
        Log.e("langType", "langType---" + langType);
        Log.e(TAG,"Lens Ids :"+strOptionTypeID);
        String reqKeyword = strValue;
        ApiClient.getKeyword(searchForParam, strPageNo, reqKeyword, strUId, strBookIds, "0", strOptionTypeID, bookInfo, new Callback<KeywordSearchModel>() {
            @Override
            public void onResponse(Call<KeywordSearchModel> call, retrofit2.Response<KeywordSearchModel> response) {
                if(!reqSearchVale.equals(strValue + searchForParam)) {
                    return;
                }
                JRL.isKeywordLoading = false;
                isLoading = false;
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    KeywordSearchModel keywordSearchModel1 = response.body();
                    Log.e("responseData Keyword :", new GsonBuilder().setPrettyPrinting().create().toJson(keywordSearchModel1));

                    lastSearchValue = strValue;
                    SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_LAST_SEARCH_KEYWORD, lastSearchValue);

//                    ll_buttons.setVisibility(View.VISIBLE);
//
                    if (isSimilarData && searchForParam.equals("1")) {
//                        ArrayList<KeywordSearchModel.KeywordModel> similarKeywordData = new ArrayList<>();
//                        similarKeywordData = response.body().getSimilarKeywords();
//                        if (similarKeywordData != null && similarKeywordData.size() > 0) {
//                            tvKeywordCount.setVisibility(View.GONE);
//                            llKeywordCount.setVisibility(View.GONE);
//                            setSimilarData(similarKeywordData);
//                        } else {
//                            Utils.showInfoDialog(getActivity(), "Similar keyword not found");
//                        }
                    } else {
                        if (response.body().isStatus()) {
                            allKeywordDataList = new ArrayList<>();
                            keywordSearchModel = new ArrayList<>();
                            keywordSearchModel = response.body().getData();
                            if (keywordSearchModel != null && keywordSearchModel.size() > 0) {
                               // keywordSearchModel.get(0).setExact(true);
                                //keywordSearchModel.get(0).setStrTotalSizeText("" + keywordSearchModel.size() + " Exact Result");
                                allKeywordDataList.addAll(keywordSearchModel);
                            }

//                            ArrayList<KeywordSearchModel.KeywordModel> similarKeywordData = new ArrayList<>();
//                            similarKeywordData = response.body().getSimilarKeywords();
//                            if (similarKeywordData != null && similarKeywordData.size() > 0) {
//                                if (allKeywordDataList != null && allKeywordDataList.size() > 0) {
//                                    int size = allKeywordDataList.size();
//                                    allKeywordDataList.addAll(similarKeywordData);
//                                    allKeywordDataList.get(size).setStrTotalSizeText("" + similarKeywordData.size() + " Similar Result");
//                                    allKeywordDataList.get(size).setSimmilar(true);
//                                }
//                            }




                            if (isFirstPage) {
                                if (allKeywordDataList.size() != 0 && allKeywordDataList.size() > 0) {
                                    Log.e("Keyword ", "total count --- " + allKeywordDataList.size());
                                    tvExactMatchTab.setText("Exact (" + response.body().getTotal_keyword_search_count() +")" );
                                    tvSimilarMatchTab.setText("Similar (" + response.body().getTotal_similar_found_count() +")");
                                    tvAllMatchTab.setText("All (" + response.body().getTotal_count() +")" );
                                    totalAllResults = response.body().getTotal_count();
                                    if(searchForParam.equals("0")) {
                                        totalPages = response.body().getTotal_pages();
                                    } else if(searchForParam.equals("1")) {
                                        totalPages = response.body().getTotal_keyword_search_pages();
                                    } else if(searchForParam.equals("2")) {
                                        totalPages = response.body().getTotal_similar_found_pages();
                                    }
                                }
                            }

                            if (allKeywordDataList != null && allKeywordDataList.size() > 0) {
                                setPaginateData(allKeywordDataList);
                            }
                            if (isFirstPage) {
                                if (allKeywordDataList.size() != 0 && allKeywordDataList.size() > 0) {
                                    Log.e("Keyword ", "total count --- " + allKeywordDataList.size());
                                    tvKeywordCount.setVisibility(View.GONE);
                                    llKeywordCount.setVisibility(View.GONE);
                                    tvKeywordCount.setText("" + allKeywordDataList.size() + " Keywords found");

                                    tvExactMatchTab.setText("Exact (" + response.body().getTotal_keyword_search_count() +")" );
                                    tvSimilarMatchTab.setText("Similar (" + response.body().getTotal_similar_found_count() +")");
                                    tvAllMatchTab.setText("All (" + response.body().getTotal_count() +")" );
                                    if(searchForParam.equals("0")) {
                                        totalPages = response.body().getTotal_pages();
                                    } else if(searchForParam.equals("1")) {
                                        totalPages = response.body().getTotal_keyword_search_pages();
                                    } else if(searchForParam.equals("2")) {
                                        totalPages = response.body().getTotal_similar_found_pages();
                                    }
                                } else {
                                    rvList.setVisibility(View.GONE);
                                    tvKeywordCount.setVisibility(View.VISIBLE);
                                    llKeywordCount.setVisibility(View.VISIBLE);
                                    tvKeywordCount.setText("0 Keywords found");
                                }
                            }

                        } else {
                            mKeyboardView.setVisibility(View.GONE);
                            if (isFirstPage) {
                                rvList.setVisibility(View.GONE);
                                tvKeywordCount.setVisibility(View.VISIBLE);
                                llKeywordCount.setVisibility(View.VISIBLE);
                                tvKeywordCount.setText("Keyword not found");
                                Utils.showInfoDialog(getActivity(), response.body().getMessage());
                                setPaginateData(new ArrayList<>());
                            }

                        }
                    }
                }
                else
                {
                    if (isFirstPage) {
                        setPaginateData(new ArrayList<>());
                    }
                }
            }

            @Override
            public void onFailure(Call<KeywordSearchModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                JRL.isKeywordLoading = false;
                isLoading = false;
                Utils.dismissProgressDialog();
            }
        });
    }


    private void setPaginateData(ArrayList<KeywordSearchModel.KeywordModel> keywordModels) {
        Log.d("setPaginateData", "---" + keywordModels.size());
        if (isFirstPage) {
            tempKeywordList = new ArrayList<>();
        }

        if (keywordModels != null && keywordModels.size() > 0) {
            int lastChild = tempKeywordList.size() - 1;
            if(lastChild >= 0 && page > 0) {
                KeywordSearchModel.KeywordModel loader = tempKeywordList.get(lastChild);
                if (loader.isLoader()) {
                    tempKeywordList.remove(lastChild);
                }
            }
            tempKeywordList.addAll(keywordModels);
            if(page < totalPages) {
                KeywordSearchModel.KeywordModel loader = new KeywordSearchModel.KeywordModel();
                loader.setLoader(true);
                loader.setStrTotalSizeText("Loading...");
                tempKeywordList.add(loader);
            }
        }
        else {
            tempKeywordList = new ArrayList<>();
        }


        if (tempKeywordList != null && tempKeywordList.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            setkeywordData(tempKeywordList);
        } else if(isFirstPage) {
            rvList.setVisibility(View.GONE);
            tvKeywordCount.setVisibility(View.VISIBLE);
            llKeywordCount.setVisibility(View.VISIBLE);

            tvKeywordCount.setText("No Data Found.");
        }
    }

    private void setkeywordData(ArrayList<KeywordSearchModel.KeywordModel> keywordList) {
        if (keywordList == null) {
            return;
        }

        mKeywordList = keywordList;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        if (isFirstPage) {
            if (mKeywordList != null && mKeywordList.size() > 0) {
                tvKeywordCount.setVisibility(View.VISIBLE);
                llKeywordCount.setVisibility(View.VISIBLE);
                //tvKeywordCount.setText("" + mKeywordList.size() + " Keywords found");
            } else {
                llKeywordCount.setVisibility(View.GONE);
            }
            //rvList.setHasFixedSize(true);
            rvList.setLayoutManager(linearLayoutManager);
            rvList.setVisibility(View.VISIBLE);
            mSearchListAdapter = new KeywordSearchViewAdapter(getActivity(), mKeywordList, this, mSearchKeyword);
            rvList.setAdapter(mSearchListAdapter);
            rvList.addOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    headerView.setTranslationY(-80);
                    header2.setTranslationY(-40);
                    headerView.setVisibility(View.GONE);
                    header2.setVisibility(View.GONE);
                    appBarLayout.setMinimumHeight(0);
                }
                @Override
                public void onShow() {
                    headerView.setTranslationY(0);
                    header2.setTranslationY(0);
                    headerView.setVisibility(View.VISIBLE);
                    header2.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                }
            });

            LinearLayoutManager layoutManager = (LinearLayoutManager) rvList.getLayoutManager();
            rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = mSearchListAdapter.getItemCount();
                    int pendingItemsToVisible = Math.max(totalItemCount - lastVisibleItemPosition - 1, 0);
                    if (page < totalPages && pendingItemsToVisible <= 20) {
                        getSearch(page+1);
                    }

                    // Handle scroll events
                   // Log.d("ScrollListener", "Scrolled horizontally by " + dx + " and vertically by " + dy);
                };
            });
        } else {
            // tvKeywordCount.setText("" + mKeywordList.size() + " Keywords found");
            rvList.setVisibility(View.VISIBLE);
            Log.e(TAG, "set mGranthDataList --" + mKeywordList.size());
            mSearchListAdapter.newData(mKeywordList);
        }

    }

    private void setSimilarData(ArrayList<KeywordSearchModel.KeywordModel> keywordList) {
        if (keywordList == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setVisibility(View.VISIBLE);
        mSearchListAdapter = new KeywordSearchViewAdapter(getActivity(), keywordList, this, mSearchKeyword);
        rvList.setAdapter(mSearchListAdapter);
    }

    private void setSearchHistoryKeywordData() {
        mSearchHistoryModelList = SharedPrefManager.getInstance(getActivity()).getSearchHistoryList(SharedPrefManager.KEY_SAVE_HISTORY);
        if (mSearchHistoryModelList != null && mSearchHistoryModelList.size() > 0) {
            rvSearchHistoryList.setVisibility(View.VISIBLE);
            //rvSearchHistoryList.setHasFixedSize(true);
            rvSearchHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvSearchHistoryList.setVisibility(View.VISIBLE);
            mSearchHistoryAdapter = new SearchHistoryAdapter(getActivity(), mSearchHistoryModelList, this);
            rvSearchHistoryList.setAdapter(mSearchHistoryAdapter);
            rvSearchHistoryList.addOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    headerView.setTranslationY(-80);
                    header2.setTranslationY(-40);
                    headerView.setVisibility(View.GONE);
                    header2.setVisibility(View.GONE);
                    appBarLayout.setMinimumHeight(0);
                }
                @Override
                public void onShow() {
                    headerView.setTranslationY(0);
                    header2.setTranslationY(0);
                    headerView.setVisibility(View.VISIBLE);
                    header2.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Log.e(TAG, "mSearchHistoryModelList-- null");
            rvSearchHistoryList.setVisibility(View.GONE);
        }
    }


    private void setHeader() {
        appBarLayout = getActivity().findViewById(R.id.appbar);
        headerView = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
    }

    private void loadUiElements(View view) {
        appBarLayout = view.findViewById(R.id.appbar);
        strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
//        if (mKeyboardView != null)
        Util.commonKeyboardHide(getActivity());

        etSearchView = view.findViewById(R.id.etSearchView);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        ivKeyboard = view.findViewById(R.id.ivKeyboard);
        llFilter = view.findViewById(R.id.llFilter);
        llClose = view.findViewById(R.id.llClose);
        ivClose = view.findViewById(R.id.ivClose);
        ivSend = view.findViewById(R.id.ivSend);
        ivExport = view.findViewById(R.id.ivExport);
        //    fabFilter = findViewById(R.id.fabFilter);
        rvList = view.findViewById(R.id.rvList);
        rvSearchHistoryList = view.findViewById(R.id.rvSearchHistoryList);
        ll_buttons = view.findViewById(R.id.ll_buttons);
        buttonFilter = view.findViewById(R.id.button_filter);
        llKeywordCount = view.findViewById(R.id.llKeywordCount);
        tvKeywordCount = view.findViewById(R.id.tvKeywordCount);
        ivSimillarKeyword = view.findViewById(R.id.ivSimillarKeyword);
        llNewFilter = view.findViewById(R.id.llNewFilter);

        btnExactMatchTab = view.findViewById(R.id.llExactMatch);
        btnSimilarMatchTab = view.findViewById(R.id.llSimilarMatch);
        btnAllMatchTab = view.findViewById(R.id.llMatch);
        tvExactMatchTab = view.findViewById(R.id.tvExactMatchTab);
        tvSimilarMatchTab = view.findViewById(R.id.tvSimilarMatchTab);
        tvAllMatchTab = view.findViewById(R.id.tvMatchTab);



        llShowCount = view.findViewById(R.id.llShowCount);
        tvFocusCounts = view.findViewById(R.id.tvFocusCounts);
        tvLensCounts = view.findViewById(R.id.tvLensCounts);
        tvFiltersCounts = view.findViewById(R.id.tvFiltersCounts);

        llFocusSearch = view.findViewById(R.id.llFocusSearch);
        llLensSearch = view.findViewById(R.id.llLensSearch);
        tvLensCountsSearch = view.findViewById(R.id.tvLensCountsSearch);
        tvFocusCountsSearch = view.findViewById(R.id.tvFocusCountsSearch);

        llLens  = view.findViewById(R.id.llLens);
        llFocus  = view.findViewById(R.id.llFocus);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strSearchtext = null;
                etSearchView.getText().clear();
                rvList.setVisibility(View.GONE);
                llKeywordCount.setVisibility(View.GONE);
                tvKeywordCount.setVisibility(View.GONE);
                llFilter.setVisibility(View.VISIBLE);
                llClose.setVisibility(View.GONE);
                llShowCount.setVisibility(View.VISIBLE);
                ll_buttons.setVisibility(View.GONE);
                tvExactMatchTab.setText("Exact" );
                tvSimilarMatchTab.setText("Similar");
                tvAllMatchTab.setText("All" );
                getFocusLensDialogData();
                SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_LAST_SEARCH_KEYWORD, "");
                           /* ivHeaderIcon.setVisibility(View.INVISIBLE);
                            tvHeaderCount.setVisibility(View.INVISIBLE);*/
            }
        });

        llNewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((strKeyword != null) && (strKeyword.length() != 0)) {
                    //showFilterDialog("2");
                    boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                    if (isLogin) {
                        Intent i = new Intent(getActivity(), FilterCategoriesActivity.class);
                        Log.e("keyword", strKeyword);
                        i.putExtra("KID", strKeyword);
                        i.putExtra("From", "Keyword");
                        startActivityForResult(i, KEYWORD_FILTER);
                    } else {
                        askLogin();
                    }
                } else {
                    strKeyword = etSearchView.getText().toString();
                    //showFilterDialog("2");
                    boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                    if (isLogin) {
                        Intent i = new Intent(getActivity(), FilterCategoriesActivity.class);
                        Log.e("keyword", strKeyword);
                        i.putExtra("KID", strKeyword);
                        i.putExtra("From", "Keyword");
                        startActivityForResult(i, KEYWORD_FILTER);
                    } else {
                        askLogin();
                    }
                }
            }
        });

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((strKeyword != null) && (strKeyword.length() != 0)) {
                    //showFilterDialog("2");
                    boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                    if (isLogin) {
                        Intent i = new Intent(getActivity(), FilterCategoriesActivity.class);
                        Log.e("keyword", strKeyword);
                        i.putExtra("KID", strKeyword);
                        i.putExtra("From", "Keyword");
                        startActivityForResult(i, KEYWORD_FILTER);
                    } else {
                        askLogin();
                    }
                } else {
                    strKeyword = etSearchView.getText().toString();
                    //showFilterDialog("2");
                    boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                    if (isLogin) {
                        Intent i = new Intent(getActivity(), FilterCategoriesActivity.class);
                        Log.e("keyword", strKeyword);
                        i.putExtra("KID", strKeyword);
                        i.putExtra("From", "Keyword");
                        startActivityForResult(i, KEYWORD_FILTER);
                    } else {
                        askLogin();
                    }
                }
            }
        });

        ivExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strKeyword = etSearchView.getText().toString();

                boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    String strSearch = etSearchView.getText().toString();
                    if (strSearch != null && strSearch.length() > 0) {
                        showDialog(strSearch,view);
                    } else {
                        InfoDialog("Please search any keywords");
                    }
                } else {
                    askLogin();
                }
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    public void getShareDialog(View view, String strFileType) {

        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View bottomSheetDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_share, (LinearLayout) view.findViewById(R.id.bottomSheetContainer));
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
        String strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.setFocusable(true);
        edtRenameFile.setEnabled(true);
        edtRenameFile.setShowSoftInputOnFocus(false);

        strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        strKeyword = etSearchView.getText().toString();
        String fileName = totalAllResults + "_Search Results For_" + strKeyword;
        if (strFileType == "2") {
            fileName = totalAllResults + "_Search Results With References For_" + strKeyword;
        }
        String strEdtRenamefile = fileName;
        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(getActivity(), edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {27
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/

                String strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, getActivity(), strLanguage, bottomSheetDialog, ivSend);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                Log.e("strKeyword", strKeyword);
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strCount  = "" + totalAllResults;               // String strPdfFile = PdfCreator.createFile(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                if (totalAllResults > 0) {
//                    saveKeywordFile(strKeyword, strUId, strEdtRenamefile, strCount, strFileType, true);
                    shareKeywordFile(strUId, strEdtRenamefile, strCount, strFileType, true);
//                    callCreatePdfApi(strEdtRenamefile, strFileType, strCount, true);

                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString() + " " + strKeyword;
                Log.e("strKeyword", strKeyword);
                try {
                    Constantss.FILE_NAME = strEdtRenamefile + " " + STR_KEYWORD;
                    Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strEdtRenamefile + " " + strKeyword + " /";
                    /*strPdfFile = PdfCreator.createTextPdf(
                            mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mKeywordStringList);*/

                    callDownloadMyShelfsApi(strUserId, strPdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }

            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                Log.e("strKeyword", strKeyword);
                STR_KEYWORD = strKeyword;
                if(strKeyword == null || strKeyword.isEmpty()) {
                    return;
                }
                Constantss.FILE_NAME = strEdtRenamefile + "_" + strKeyword;
                Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strEdtRenamefile + "_" + strKeyword + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strTypeName  = "" + strKeyword;
                String strCount  = "" + totalAllResults;               // String strPdfFile = PdfCreator.createFile(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                if (totalAllResults > 0) {
                    saveKeywordFile(strKeyword, strUId, strEdtRenamefile,strCount, strFileType, false);

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
                        /*if (strPdfFile != null && strPdfFile.length() > 0) {
                            File mFile = new File(strPdfFile);
                            if (mFile.exists()) {
                                Toast.makeText(getActivity(), "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("download--", "file not exits" + strPdfFile);
                            }
                        }*/
                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            Utils.downloadLocalPDF(strPdfFile, getActivity());
                        }
                        else {
                            Utils.showInfoDialog(getActivity(), "Pdf not found");
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
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {
                        /*strUserId = SharedPrefManager.getInstance(ReferencePageActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        if (strUserId != null && strUserId.length() > 0) {
                            callListHoldSearchKeyword(strUserId);
                        }*/

                        try {
                            if (strPdfLink != null && strPdfLink.length() > 0) {
                                String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                                String strMessage = strPdfLink; //" " + strBookName;
                                Utils.shareContentWithImage(getActivity(), "JRL Keyword(s) PDF", strMessage, strPdfImage);
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


//                        if (strPdfFile != null && strPdfFile.length() > 0) {
//                            File mFile = new File(strPdfFile);
//                            if (mFile.exists()) {
//                                share(shareText, strPdfFile);
//                            } else {
//                                Log.e("share--", "file not exits" + strPdfFile);
//                            }
//                        }

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

    private void saveKeywordFile(String strKeyword, String strUId, String strEdtRenamefile, String totalKeywordCount, String strFileType, boolean isShare) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.checkMyShelfFileName(strUId, strEdtRenamefile, new Callback<CheckMyShelfFileNameResModel>() {
            @Override
            public void onResponse(Call<CheckMyShelfFileNameResModel> call, retrofit2.Response<CheckMyShelfFileNameResModel> response) {
                if (!response.isSuccessful()  ) {
                    Utils.dismissProgressDialog();
                    Utils.showInfoDialog(getActivity(), "Please try again!");
                    return;
                }

                if(!response.body().isStatus()) {
                    Utils.dismissProgressDialog();
//                    Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                    String strTmpPdfUrl = response.body().getPdf_url();
                    String strPdfImage = response.body().getPdf_image();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(response.body().getMessage());

                    builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (strTmpPdfUrl != null && strTmpPdfUrl.length() > 0) {
                                callShareMyShelfsApi(strUId, "", strTmpPdfUrl, strPdfImage);
                            }
                        }
                    });

                    builder.setNegativeButton("Save Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            callCreatePdfApi(strEdtRenamefile, strFileType, totalKeywordCount, isShare);
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

//                    Dialog dialog = new AlertDialog.Builder(getActivity())
//                            .setMessage(response.body().getMessage())
//                            .setMessageColor(Color.parseColor("#1565C0"))
//                            .setMessageSize(18)
//                            .setNegativeButtonColor(Color.parseColor("#981010"))
//                            .setNegativeButtonSize(18)
//                            .setNegativeButton("Save Again", new IosDialog.OnClickListener() {
//                                @Override
//                                public void onClick(IosDialog dialog, View v) {
//                                    dialog.dismiss();
//                                    callCreatePdfApi(strEdtRenamefile, strFileType, totalKeywordCount, isShare);
//                                }
//                            })
//                            .setNegativeButtonColor(Color.parseColor("#981010"))
//                            .setNegativeButtonSize(18)
//                            .setNe ("OK", new IosDialog.OnClickListener() {
//                                @Override
//                                public void onClick(IosDialog dialog, View v) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .setPositiveButtonColor(Color.parseColor("#981010"))
//                            .setPositiveButtonSize(18)
//                            .setPositiveButton("Share", new IosDialog.OnClickListener() {
//                                @Override
//                                public void onClick(IosDialog dialog, View v) {
//                                    dialog.dismiss();
//                                    callCreatePdfApi(strEdtRenamefile, strFileType, totalKeywordCount, true);
//                                }
//                            }).build();
//
//                    dialog.show();
                }
                else {
                    callCreatePdfApi(strEdtRenamefile, strFileType, totalKeywordCount, isShare);
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

    private void shareKeywordFile(String strUId, String strEdtRenamefile, String totalKeywordCount, String strFileType, boolean isShare) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.checkMyShelfFileName(strUId, strEdtRenamefile, new Callback<CheckMyShelfFileNameResModel>() {
            @Override
            public void onResponse(Call<CheckMyShelfFileNameResModel> call, retrofit2.Response<CheckMyShelfFileNameResModel> response) {
                Utils.dismissProgressDialog();
                if (!response.isSuccessful()  ) {
                    Utils.showInfoDialog(getActivity(), "Please try again!");
                    return;
                }

                if(!response.body().isStatus()) {

                    String strTmpPdfUrl = response.body().getPdf_url();
                    String strPdfImage = response.body().getPdf_image();

                    if (strTmpPdfUrl != null && strTmpPdfUrl.length() > 0) {
                        callShareMyShelfsApi(strUId, "", strTmpPdfUrl, strPdfImage);
                    }
                }
                else {
                    callCreatePdfApi(strEdtRenamefile, strFileType, totalKeywordCount, isShare);
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

    private void callCreatePdfApi(String strEdtRenamefile, String strFileType, String totalKeywordCount, boolean isShare)
    {
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.createKeywordsPdf( strKeyword, strUId, strBookIds, "0", strFileType, strEdtRenamefile, new Callback<CreatePdfFileUrlResModel>() {
            @Override
            public void onResponse(Call<CreatePdfFileUrlResModel> call, retrofit2.Response<CreatePdfFileUrlResModel> response) {
                Utils.dismissProgressDialog();
//                    Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        String strTmpPdfUrl = response.body().getPdf_url();
                        String strPdfImage = response.body().getPdf_image();
                        if (strTmpPdfUrl != null && strTmpPdfUrl.length() > 0) {
                            if (isShare) {
                                callShareMyShelfsApi(strUserId, "", strTmpPdfUrl, strPdfImage);
                            }
                            else {
                                callAddMyShelfApi(strTmpPdfUrl, strKeyword, strUId, strEdtRenamefile, totalKeywordCount, strFileType, isShare);
                            }
                        } else {
                            Utils.showInfoDialog(getActivity(), "KeywordData not saved");
                        }
                    }else {
                        Utils.showInfoDialog(getActivity(), "KeywordData not saved");
                    }

                } else {
                    Utils.showInfoDialog(getActivity(), "KeywordData not saved");
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

    private void callAddMyShelfApi(String fileUrl, String strKeyword, String strUId, String strEdtRenamefile, String totalKeywordCount, String strFileType, boolean isShare) {
        String type =  "0";
        String typeref = REF_TYPE_REFERENCE_PAGE;
        String typeName = strKeyword;
        Log.e("fileType :", " "+strFileType);
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.addMyShelfsWithUrl(strUId, null, null, type, typeref, strEdtRenamefile, typeName, totalKeywordCount, strFileType, fileUrl, new Callback<AddShelfResModel>() {
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
                            callShareMyShelfsApi(strUserId, "", strPdfLink, strPdfImage);
                        }
                        else {
                            Utils.showInfoDialog(getActivity(), "Keywords Added In My Reference.");
                        }
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
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


    void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), KEYWORD_SEARCH);
    }


    void InfoDialog(String strMessage) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", dialogClickListener)


                .show();
    }


    public void showDialog(String strSearch, View view){
        final Dialog dialog = new Dialog(getActivity());
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_export);
        TextView tvKeyword =  dialog.findViewById(R.id.tvKeywordOption);
     TextView tvReferenceKeyword =  dialog.findViewById(R.id.tvKeywordRefernceOption);
     Button btnCancel =  dialog.findViewById(R.id.btnCancel);
        tvKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
               // callCreateKeywordsPdfApi(strSearch, strUId, view,"1");
                getShareDialog(view, "1");

            }
        }); tvReferenceKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
               // callCreateKeywordsPdfApi( strSearch, strUId, view, "2");
                getShareDialog(view, "2");
            }
        }); btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void showFilterDialog(String tabId) {
        strKeyword = etSearchView.getText().toString();

        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            JRL.isKeywordLoading = false;
            Intent i = new Intent(getActivity(), FilterMenuActivity.class);
            Log.e("keyword", strKeyword);
            i.putExtra("KID", strKeyword);
            i.putExtra("SettingPosition", tabId);
            startActivityForResult(i, KEYWORD_FILTER);
        } else {
            askLogin();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.share) {
            String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareData);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareData);
            startActivity(Intent.createChooser(sharingIntent, shareData));
            return true;
        } else if (itemId == R.id.download) {// do your code
            return true;
        } else if (itemId == R.id.shelf) {// do your code
            return true;
        }
        return false;
    }

    @Override
    public void selectedBook(String cat, String books) {
        tvFiltersCounts.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSelectSearchKeyword(SearchHistoryModel SearchHistoryModel, int position) {

        String strKeywordData = SearchHistoryModel.getStrSearchKeywordName();
        if (strKeywordData != null && strKeywordData.length() > 0) {
            etSearchView.setText(strKeywordData);
            rvSearchHistoryList.setVisibility(View.GONE);
            if (mKeyboardView.getVisibility() == View.VISIBLE) {
                mKeyboardView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDetailsClick(KeywordSearchModel.KeywordModel keywordModel, int position) {
        String strId = keywordModel.getId();
        String strName = keywordModel.getName();

        if (mSearchKeyword == null || mSearchKeyword.size() == 0) {
            mSearchKeyword = new ArrayList<>();
        }
        SearchHistoryModel model = new SearchHistoryModel();
        model.setStrId(strId);
        model.setStrSearchKeywordName(strName);
        mSearchKeyword.add(model);
        SharedPrefManager.getInstance(getActivity()).saveSearchLocalHistory(SharedPrefManager.KEY_KEYWORD_HISTORY, mSearchKeyword);

        Log.e(TAG, "keywordIds: " + strId + ", keywordName : " + strName + ", bookIds : " + strBookIds );
        String strTotalCount  = ""+allKeywordDataList.size();
        JRL.isKeywordLoading = false;
        Intent i = new Intent(getActivity(), KeywordSearchDetailsActivity.class);
        i.putExtra("totalKeywordCount", strTotalCount);
        i.putExtra("keywordId", strId);
        i.putExtra("keywordName", strName);
        i.putExtra("bookIds", strBookIds);
        startActivity(i);
    }

    public void share(String shareData, String mFilePath) {
        File mFile = new File(mFilePath);
        Uri fileUri = Uri.fromFile(mFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            fileUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", new File(mFilePath));
        }
        //  Uri fileUri = FileProvider.getUriForFile(getActivity(), getActivity().getActivity()().getPackageName() + ".provider", mFile);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType(URLConnection.guessContentTypeFromName(mFile.getName()));
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, shareData);
        intentShareFile.putExtra(Intent.EXTRA_TEXT, shareData);
        startActivity(Intent.createChooser(intentShareFile, shareData));

    }


    private void callSearchOptionApi() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        //  Utils.showProgressDialog(getActivity(), "Please wait......", false);
        ApiClient.getFilterSearchOption(strUId, new Callback<SearchOptionResModel>() {
            @Override
            public void onResponse(Call<SearchOptionResModel> call, Response<SearchOptionResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        List<SearchOptionResModel.SearchOptionModel> searchOptionModelList = response.body().getData();
                        if (searchOptionModelList != null && searchOptionModelList.size() > 0) {
                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_TOTAL_LENS_DATA, String.valueOf(searchOptionModelList.size()));
                            String strLens = searchOptionModelList.size()+ "/" + searchOptionModelList.size();
                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_LENS_DATA, strLens);

                            tvLensCounts.setText(strLens);
                            tvLensCountsSearch.setText(strLens);

                        }
                    } else {
                        Utils.showInfoDialog(getActivity(), "No Filter Option Found");
                    }
                } else {
                    Log.e(TAG, "onFilterSearchOption--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchOptionResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e(TAG, "onFilterSearchOption OnFailure --" + t.getMessage());
            }
        });
    }

    public void callCategoryApi(String strKeyword) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getCategory("1", strKeyword, strUId, new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
                if (response.isSuccessful()) {
                    CategoryResModel categoryResModel = response.body();
                    Log.e("responseData Category :", new GsonBuilder().setPrettyPrinting().create().toJson(categoryResModel));
                    if (response.body().isStatus()) {
                        String strTotalBooks = response.body().getTotalBooks();
                        ArrayList<String> selectedFocusList = new ArrayList<>();
                        ArrayList<String> totalFocusList = new ArrayList<>();
                        ArrayList<CategoryResModel.CategoryModel> mainDataList = response.body().getData();
                        if (mainDataList != null && mainDataList.size() > 0) {
                            for (int i = 0; i < mainDataList.size(); i++) {
                                List<CategoryResModel.CategoryModel.CategoryBookModel> booksList = response.body().getData().get(i).getBooks();
                                for (int j = 0; j < booksList.size(); j++) {
                                    String isSelected = booksList.get(j).getFilter();
                                    String strBookId = booksList.get(j).getBook_id();
                                    totalFocusList.add(strBookId);
                                    if (isSelected != null && isSelected.equalsIgnoreCase("1")) {
                                        selectedFocusList.add(strBookId);
                                    }
                                }
                            }
                        }

                        String strFocus = selectedFocusList.size() + "/" + strTotalBooks;

                        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FOCUS_DATA, strFocus);
                        tvFocusCounts.setText(strFocus);
                        tvFocusCountsSearch.setText(strFocus);

                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                    }
                }
                Utils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CategoryResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public void callCategoryFilterApi(String strKeyword) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getCategory("0", strKeyword, strUId, new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
                if (response.isSuccessful()) {
                    CategoryResModel categoryResModel = response.body();
                    Log.e("Filter responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(categoryResModel));
                    if (response.body().isStatus()) {
                        String strTotalBooks = response.body().getTotalBooks();
                        ArrayList<String> selectedFocusList = new ArrayList<>();
                        ArrayList<String> totalFocusList = new ArrayList<>();
                        ArrayList<CategoryResModel.CategoryModel> mainDataList = response.body().getData();
                        if (mainDataList != null && mainDataList.size() > 0) {
                            for (int i = 0; i < mainDataList.size(); i++) {
                                List<CategoryResModel.CategoryModel.CategoryBookModel> booksList = response.body().getData().get(i).getBooks();
                                for (int j = 0; j < booksList.size(); j++) {
                                    String isSelected = booksList.get(j).getFilter();
                                    String strBookId = booksList.get(j).getBook_id();
                                    totalFocusList.add(strBookId);
                                    if (isSelected != null && isSelected.equalsIgnoreCase("1")) {
                                        selectedFocusList.add(strBookId);
                                    }
                                }
                            }
                        }

                        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTERS_DATA, "" + selectedFocusList.size());
                        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_TOTAL_FILTERS_DATA, strTotalBooks);

                        String strFilter = selectedFocusList.size() + "/" + strTotalBooks;
                        tvFiltersCounts.setText(strFilter);

                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                    }
                }
                Utils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CategoryResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }


    public String downloadFile(ResponseBody body) {

        try {
            Log.d("downloadFile", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            String strEdtRenamefile = allKeywordDataList.size() + "_Search Results For_" + strKeyword;

            String strFilePath = Utils.getMediaStorageDir(getContext()) + File.separator + strEdtRenamefile + ".pdf";
            try {
                in = body.byteStream();
                out = new FileOutputStream(strFilePath);

                int length;
                byte[] buffer = new byte[1024];
                while ((length = in.read(buffer)) > -1) {
                    out.write(buffer, 0, length);
                }
//
//                int c;
//
//                while ((c = in.read()) != -1) {
//                    out.write(c);
//                }

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

}

package com.jainelibrary.activity;

import static android.app.Activity.RESULT_OK;
import static com.jainelibrary.utils.Utils.TYPE_KEYWORD_PAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.jainelibrary.Constantss;
import com.jainelibrary.IOnBackPressed;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.adapter.SearchHistoryAdapter;
import com.jainelibrary.adapter.YearWiseListAdapter;
import com.jainelibrary.extraModel.SearchHistoryModel;
import com.jainelibrary.fragment.FilterDialogFragment;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.KeywordModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.model.YearResModel;
import com.jainelibrary.model.YearResponseModel;
import com.jainelibrary.model.YearTypeResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YearFragment extends Fragment implements IOnBackPressed, YearWiseListAdapter.YearClickListener, PopupMenu.OnMenuItemClickListener, FilterDialogFragment.setSelectedBooks, SearchHistoryAdapter.SearchHistoryInterfaceListener {
    private static final int KEYWORD_SEARCH = 1;
    private static final int KEYWORD_FILTER = 2;
    private static final int YEAR_SEARCH = 1;
    private static final int YEAR_FILTER = 2;
    private final String TAG = YearFragment.this.getClass().getSimpleName();
    private final ArrayList<KeywordModel> keywordList = new ArrayList<>();
    private final String selectedString = "";
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend;
    RecyclerView rvList, rvSearchHistoryList;
    LinearLayout llFilter, llClose, llKeywordCount;
    String[] language = {"English", "Gujarati", "Hindi"};
    String strBookIds = "";
    String strKeyword, strYear;
    ArrayList<SearchHistoryModel> mSearchHistoryModelList = new ArrayList<>();
    ArrayList<SearchHistoryModel> mSearchKeyword;
    boolean isLogin = false;
    View headerView, header2;
    AppBarLayout appBarLayout;
    String strEdtRenamefile = null, strUserId, shareText, strPdfFile, strTypeRef, strApiTypeId, strPdfLink;
    Activity activity;
    private EditText etSearchView;
    private String strLanguage;
    // private FloatingActionButton fabFilter;
    //private SearchAdapter mSearchListAdapter;
    private YearWiseListAdapter mSearchListAdapter;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private String strSearchtext;
    private TextView buttonFilter;
    private int selected;
    private String PackageName;
    private String strCatName, strBookCount;
    private TextView tvKeywordCount;
    private Spinner spnYearType;
    private int totalCount = 0;
    private ArrayList<YearResModel.Year> mYearList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private String strUsername, strLastSpiItem;
    private String strUId, strYearId, strYearTypeName, strReferenceCount;
    private final ArrayList<String> yearTypeList = new ArrayList<>();
    private int spnYearTypeId = 0;
    private LinearLayout llFooterFilter;
    private TextView tvFilterCounts;
    public YearFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        rvList.setVisibility(View.GONE);
        if(spnYearTypeId != 0){
            callGetYearApi(String.valueOf(spnYearTypeId), strBookIds);
        }
        //getYearTypeList();
        //spnYearTypeId = typeList.get(position).getId();
        //callGetYearApi(String.valueOf(spnYearTypeId), strBookIds);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_year, container, false);
        PackageName = getActivity().getPackageName();
        loadUiElements(view);
        setHeader();
        declaration();
        Intent intent = new Intent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            totalCount = bundle.getInt("totalCount", 0);
            strSearchtext = bundle.getString("strSearchKeyword");
            if (strSearchtext != null && strSearchtext.length() > 0) {
                etSearchView.setText(strSearchtext);
            }
        }
        ArrayList<String> bookIdList = SharedPrefManager.getInstance(getActivity()).getFilteredBookIdList(SharedPrefManager.KEY_YEAR_FILTER_BOOK_IDS);
        rvList.setVisibility(View.GONE);
        if (bookIdList != null && bookIdList.size() > 0) {
            String strBooIds = null;
            totalCount = bookIdList.size();
            for (int i = 0; i < bookIdList.size(); i++) {
                strBooIds = bookIdList.get(i);
                if (strBooIds != null && strBooIds.length() > 0) {
                    strBooIds = strBooIds + "," + strBooIds;
                } else {
                    strBooIds = strBooIds;
                }
            }
            strBookIds = strBooIds;

        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("oar", "" + requestCode + " res " + resultCode);
        if (requestCode == KEYWORD_SEARCH) {
            if (resultCode == RESULT_OK) {
                boolean isLogin = data.getBooleanExtra("isLogin", false);
            }
        }
        if (requestCode == KEYWORD_FILTER) {
            if (resultCode == RESULT_OK) {
                totalCount = data.getIntExtra("totalCount", 0);
                strBookIds = data.getStringExtra("strBooIds");
                Log.e("strBookIds", "onActivity---" + strBookIds);
                //    selectedString = getIntent().getExtras().getString("selectedString");
                if (totalCount != 0 && totalCount > 0) {
                    tvFilterCounts.setText(totalCount + " Books");
                }
                showFilterOption();
            }
        }
    }

    private void showFilterOption() {

        if (totalCount == 0) {
            strBookIds = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_YEAR_FILTER_CAT_IDS);
            ArrayList<String> bookList = SharedPrefManager.getInstance(getActivity()).getFilteredBookIdList(SharedPrefManager.KEY_YEAR_FILTER_BOOK_IDS);
            if (bookList != null && bookList.size() > 0) {
                totalCount = bookList.size();
                tvFilterCounts.setText(totalCount + " Books");
            }
        }

        if (totalCount != 0 && totalCount > 0) {
            tvFilterCounts.setText(totalCount + " Books");
        }else {
            tvFilterCounts.setText("Select");
        }
    }

    @Override
    public boolean onBackPressed() {


        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
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
        //callKeywordDataApi("", "");
        //  etSearchView.requestFocus();
        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        // Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);

//        String strFocusCounts = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_FOCUS_DATA);
//        String strLensCounts = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LENS_DATA);
//
//        if (strFocusCounts != null && strFocusCounts.length() > 0) {
//            tvFocusCounts.setText(strFocusCounts);
//        }
//
//        if (strLensCounts != null && strLensCounts.length() > 0) {
//            tvLensCounts.setText(strLensCounts);
//        }

        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
        }
        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                Util.hideKeyBoard(getActivity(), etSearchView);
                Utils.showDefaultKeyboardDialog(getActivity());
            }
        });
        //callKeywordDataApi("");
        etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Util.hideKeyBoard(getActivity(), etSearchView);
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                //   setSearchHistoryKeywordData();
                return false;
            }
        });
        etSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getSearch();
                    return true;
                }
                return false;
            }
        });


        ivSend.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Util.hideKeyBoard(getActivity(), etSearchView);
                Log.e("search", "search");
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                getSearch();
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
                    ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            strSearchtext = null;
                            etSearchView.getText().clear();
                            llKeywordCount.setVisibility(View.GONE);
                            tvKeywordCount.setVisibility(View.GONE);
                            llFilter.setVisibility(View.VISIBLE);
                            llClose.setVisibility(View.GONE);
                           /* ivHeaderIcon.setVisibility(View.INVISIBLE);
                            tvHeaderCount.setVisibility(View.INVISIBLE);*/
                        }
                    });
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
                //Util.hideKeyBoard(getActivity(), etSearchView);
                Log.e("search", "search");
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                getSearch();

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
        getYearTypeList();
        //BattleRun.dismiss();
        Log.d("Item", "Clicked");

      /*  if (KeywordsMainFragment.keywordSearchModel != null && KeywordFragment.keywordSearchModel.size() > 0) {
            setkeywordData(KeywordFragment.keywordSearchModel);
        }*/


        llFooterFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((strSearchtext != null) && (strSearchtext.length() != 0)) {
                    //showFilterDialog("2");
                    Intent i = new Intent(getActivity(), FilterCategoriesActivity.class);
                    Log.e("keyword", strSearchtext);
                    i.putExtra("KID",  String.valueOf(spnYearTypeId));
                    i.putExtra("From", "Year");
                    startActivityForResult(i, YEAR_FILTER);
                } else {
                    strSearchtext = etSearchView.getText().toString();
                    //showFilterDialog("2");
                    Intent i = new Intent(getActivity(), FilterCategoriesActivity.class);
                    Log.e("keyword", strSearchtext);
                    i.putExtra("KID",  String.valueOf(spnYearTypeId));
                    i.putExtra("From", "Year");
                    startActivityForResult(i, YEAR_FILTER);
                }
            }
        });

    }


    private void setTypeSpinnerData(ArrayList<YearTypeResModel.YearType> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, yearTypeList);
        spnYearType.setAdapter(adp);

        spnYearType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                if (position == 0){
                    //Toast.makeText(getActivity(), "Please select year type", Toast.LENGTH_SHORT).show();
                    etSearchView.setEnabled(false);
                    return;
                }else{
                    spnYearTypeId = typeList.get(position).getId();
                    strYearTypeName = typeList.get(position).getName();
                    callGetYearApi(String.valueOf(spnYearTypeId), strBookIds);
                    strLastSpiItem = spnYearType.getSelectedItem().toString();

                    //spnYearTypeId = typeList.get(position).getId();
                    etSearchView.setEnabled(true);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void getSearch() {
        //Util.hideKeyBoard(getActivity(), etSearchView);
        final String strValue = etSearchView.getText().toString();
        if (strValue != null && strValue.length() > 0) {
            rvSearchHistoryList.setVisibility(View.GONE);

            if (mKeyboardView.getVisibility() == View.VISIBLE) {
                mKeyboardView.setVisibility(View.GONE);
                mKeyboard = null;
            }
            String strYearTypeId = String.valueOf(spnYearTypeId);
            if ((strYearTypeId != null) && (strYearTypeId.length() != 0)) {
                getYearBookList(String.valueOf(spnYearTypeId), strValue);
            }

        } else {
            Utils.showInfoDialog(getActivity(), "Please enter value in search box");
        }
    }


    private void setYearData(ArrayList<YearResponseModel.YearModel> yearList) {
        if (yearList == null) {
            return;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setVisibility(View.VISIBLE);
        mSearchListAdapter = new YearWiseListAdapter(getActivity(), yearList, this);
        rvList.setAdapter(mSearchListAdapter);


        //   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                header2.setVisibility(View.VISIBLE);
                headerView.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setHeader() {
        appBarLayout = getActivity().findViewById(R.id.appbar);
        headerView = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
    }

    private void loadUiElements(View view) {
        appBarLayout = view.findViewById(R.id.appbar);
        strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (mKeyboardView != null)
            Util.commonKeyboardHide(getActivity());

        spnYearType = view.findViewById(R.id.spnYearType);
        etSearchView = view.findViewById(R.id.etSearchView);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        ivKeyboard = view.findViewById(R.id.ivKeyboard);
        llFilter = view.findViewById(R.id.llFilter);
        llClose = view.findViewById(R.id.llClose);
        ivClose = view.findViewById(R.id.ivClose);
        ivSend = view.findViewById(R.id.ivSend);
        tvFilterCounts = view.findViewById(R.id.tvFilterCounts);
        llFooterFilter = view.findViewById(R.id.llFooterFilter);


        //    fabFilter = findViewById(R.id.fabFilter);
        rvList = view.findViewById(R.id.rvList);
        rvSearchHistoryList = view.findViewById(R.id.rvSearchHistoryList);
        llKeywordCount = view.findViewById(R.id.llKeywordCount);
        tvKeywordCount = view.findViewById(R.id.tvKeywordCount);
    }



    @SuppressLint("ResourceAsColor")
    public void getShareDialog(List<String> mKeywordStringList, View view) {

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
        strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        strKeyword = etSearchView.getText().toString();
        String strEdtRenamefile = mYearList.size() + "_Search Results For_" + strKeyword;
        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Util.hideKeyBoard(getActivity(), edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }
                //Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, getActivity(), strLanguage, bottomSheetDialog, ivSend);
                return false;
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomSheetDialog.cancel();
//                String shareText = strEdtRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
//                Constantss.FILE_NAME = strEdtRenamefile + " " + strKeyword;
//                Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strKeyword + " /";
//                String strPdfFile = PdfCreator.createTextPdf(Utils.getMediaStorageDir(getContext()).getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
//                callShareMyShelfsApi(strUserId, shareText, strPdfFile);
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strPdfFile = PdfCreator.createFile(Utils.getMediaStorageDir(getContext()).getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        UploadFile(strUId, strEdtRenamefile, mFile, true);
                    } else {
                        Log.e("download--", "file not exits" + strPdfFile);
                    }
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strPdfFile = null;
                Constantss.FILE_NAME = strEdtRenamefile + " " + strKeyword;
                Constantss.FILE_NAME_PDF = "JainRefLibrary /" + strKeyword + " /";
                strPdfFile = PdfCreator.createTextPdf(
                        Utils.getMediaStorageDir(getContext()).getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                callDownloadMyShelfsApi(strUserId, strPdfFile);


            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strPdfFile = PdfCreator.createFile(Utils.getMediaStorageDir(getContext()).getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        UploadFile(strUId, strEdtRenamefile, mFile, false);
                    } else {
                        Log.e("download--", "file not exits" + strPdfFile);
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

    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
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

    private void callShareMyShelfsApi(String strUserId, String shareText, String strPdfFile) {
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

    private void UploadFile(String strUId, String strEdtRenamefile, File mFile, boolean isShare) {
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUId);
        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), "pdf_" + strEdtRenamefile);
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "0");
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), TYPE_KEYWORD_PAGE);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("pdf_file", mFile.getName(), RequestBody.create(MediaType.parse("*/*"), mFile));
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.addMyShelfs(uid, null, null, type, typeref, filename, null, null, null, filePart, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        strPdfLink = response.body().getPdf_url();
                        if (isShare) {
                            callShareMyShelfsApi(strUserId, shareText, strPdfFile);
                        }
                        else {
                            Utils.showInfoDialog(getActivity(), "Year Added In My Reference.");
                        }
                    } else {
                        Utils.showInfoDialog(getActivity(), response.body().getMessage());
                        //Toast.makeText(getActivity(), "Some Error Occured..", Toast.LENGTH_LONG).show();
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


    private void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), KEYWORD_SEARCH);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", dialogClickListener)
                .show();

    }

    public void showFilterDialog(String tabId) {
        strSearchtext = etSearchView.getText().toString();


    }

    /*public void showFilterDialog() {
        Intent i = new Intent(getActivity(), FilterActivity.class);
        Log.e("keyword", strKeyword);
        i.putExtra("KID", strKeyword);
        startActivityForResult(i, KEYWORD_FILTER);
       *//* FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = FilterDialogFragment.newInstance(0);
        newFragment.show(ft, "dialog");*//*
       *//* dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();*//*
    }*/

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
    public void selectedBook(String cat, String books) {
        //tvFilterCounts.setText(books + " Books");
        //tvFilterCounts.setText(books);
        /*tvCategory.setText(cat);
        tvCount.setText(books);
        ivNext.setVisibility(View.VISIBLE);
        tvCategory.setVisibility(View.VISIBLE);
        tvCount.setVisibility(View.VISIBLE);
        tvBooks.setVisibility(View.VISIBLE);*/
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

    public void callGetYearApi(String strKeyId, String strBookIds) {

        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Log.e("strBookIds", "strBookIds---" + strBookIds);

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getYear(strUId, strKeyId, strBookIds, new Callback<YearResponseModel>() {
            @Override
            public void onResponse(Call<YearResponseModel> call, retrofit2.Response<YearResponseModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<YearResponseModel.YearModel> yearModels = new ArrayList<>();
                        yearModels = response.body().getYears();
                        rvList.setVisibility(View.VISIBLE);
                        setYearData(yearModels);
                        llFooterFilter.setVisibility(View.VISIBLE);
                        //tvFilterCounts.setText(totalCount + " Books");
                        /*String strYearFilter = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_YEAR_FILTER_CAT_IDS);
                        tvFilterCounts.setText(strYearFilter + " Books");*/
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<YearResponseModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void sharePDFFiles(String strEdtRenamefile) {
        try {
            List<String> mKeywordStringList = new ArrayList<>();
            if (mYearList != null && mYearList.size() > 0) {
                for (int i = 0; i < mYearList.size(); i++) {
                    String strNamr = mYearList.get(i).getYear();
                    if (strNamr != null && strNamr.length() > 0) {
                        mKeywordStringList.add(strNamr);
                    }
                }

                String strPdfFile = null;
                if (mKeywordStringList != null && mKeywordStringList.size() > 0) {
                    // strPdfFile = PdfCreator.createFile(mediaStoragePDfKeyWordDir.getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                    //strPdfFile = PdfCreator.createTextPdf(mediaStoragePDfKeyWordDir.getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                }

                if (strPdfFile != null && strPdfFile.length() > 0) {
                    Uri fileUri = Uri.fromFile(new File(strPdfFile));
                    String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    intent.setType("application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, shareData));
                }
            } else {
                Utils.showInfoDialog(getActivity(), "No keyword data found");
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
    }

    public void share(String shareData, String mFilePath) {
        File mFile = new File(mFilePath);
        Uri fileUri = Uri.fromFile(mFile);
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

    @Override
    public void onYearClick(ArrayList<YearResponseModel.YearModel> yearModels, int position) {
        getYearBookList(String.valueOf(spnYearTypeId), yearModels.get(position).getYear_name());
        strYearId = yearModels.get(position).getId();
        strReferenceCount = yearModels.get(position).getReference_cnt();
    }

 /*   @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        Log.e("Search", "search key Pressed");
        if (mKeyboardView.getVisibility() == View.VISIBLE)
            if(i==151 )
            {
             Log.e("Search", "search key Pressed");
            }
        return true;
    }*/
/*


    @Override
    public void onKey(int i, int[] ints) {
        if (mKeyboardView.getVisibility() == View.VISIBLE)
            if(i==151 )
            {
                Log.e("Search", "search key Pressed");

            }

    }
*/


    private void getYearTypeList() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getYearTypes(new Callback<YearTypeResModel>() {
            @Override
            public void onResponse(Call<YearTypeResModel> call, retrofit2.Response<YearTypeResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        ArrayList<YearTypeResModel.YearType> typeList = response.body().getTypes();
                        YearTypeResModel.YearType yearType = new YearTypeResModel.YearType();
                        yearType.setId(0);
                        yearType.setName("Select Year Type");
                        typeList.add(0,yearType);
                        if (typeList != null && typeList.size() > 0) {
                            for (int i = 0; i < typeList.size(); i++) {
                                String strType = response.body().getTypes().get(i).getName();
                                yearTypeList.add(strType);
                            }

                            if (yearTypeList.size() > 0) {
                                setTypeSpinnerData(typeList);
                            }
                        }

                    } else {
                        Utils.showInfoDialog(getActivity(), "Year Type not found");
                    }


                }
            }

            @Override
            public void onFailure(Call<YearTypeResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }


    private void getYearBookList(String spnYearTypeId, String strYearValue) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getYearBooks(strUId, spnYearTypeId, strYearValue, strBookIds, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        ArrayList<BookListResModel.BookDetailsModel> yearBookLists = new ArrayList<>();
                        yearBookLists = response.body().getData();
                        strApiTypeId = response.body().getType_id();

                        ArrayList<BookListResModel.BookDetailsModel.BookPageModel> books = new ArrayList<>();
                        books = yearBookLists.get(0).getBooks();
                        Log.e("BookSizz -- ", String.valueOf(books.size()));

                        if (yearBookLists != null && yearBookLists.size() > 0) {
                            /*ArrayList<YearBookResModel.YearBookList.YearBook> bookLists = new ArrayList<>();
                            bookLists = response.body().getData();
                            setSimilarData(similarKeywordData);*/
                            Intent intent = new Intent(getActivity(), YearBookListActivity.class);
                            intent.putExtra("YearBookList", yearBookLists);
                            intent.putExtra("YearType", spnYearTypeId);
                            intent.putExtra("YearId", strYearId);
                            intent.putExtra("Year", strYearValue);
                            intent.putExtra("strTypeId", strApiTypeId);
                            Log.e("FilterBookIds", ""+strBookIds);
                            intent.putExtra("BookIds", strBookIds);
                            intent.putExtra("YearTypeName", strYearTypeName);
                            intent.putExtra("YearRefCount", strReferenceCount);
                            startActivity(intent);
                        }

                    } else {
                        //Toast.makeText(getActivity(), "Year Type not found", Toast.LENGTH_SHORT).show();
                        Utils.showInfoDialog(getActivity(), response.body().getMessage());
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
}

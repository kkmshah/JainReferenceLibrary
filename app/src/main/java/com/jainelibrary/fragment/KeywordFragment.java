/*
package com.jainelibrary.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jainelibrary.R;
import com.jainelibrary.activity.HideingScrollListener;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.adapter.KeywordSearchListAdapter;
import com.jainelibrary.adapter.KeywordSearchViewAdapter;
import com.jainelibrary.adapter.SearchHistoryAdapter;
import com.jainelibrary.extraModel.SearchHistoryModel;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UploadPDFModel;
import com.jainelibrary.multicheck.MultiCheckActivity;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.KeywordSearchModel;
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

import static android.app.Activity.RESULT_OK;
import static com.jainelibrary.utils.Utils.TYPE_KEYWORD_PAGE;
import static com.jainelibrary.utils.Utils.mediaStorageKeyWordDir;

public class KeywordFragment extends Fragment implements KeywordSearchListAdapter.SearchInterfaceListener, PopupMenu.OnMenuItemClickListener, FilterDialogFragment.setSelectedBooks, SearchHistoryAdapter.SearchHistoryInterfaceListener {

    private final String TAG = "Keyword";
    private EditText etSearchView;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private TextView tvCategory, tvCount, tvBooks, tvKeywordCount;
    ImageView ivClose, ivKeyboard, ivSend;
    LinearLayout llFilter, llClose, ivShare, llKeywordCount;
    private String strLanguage;
    String[] language = {"English", "Gujarati", "Hindi"};
    private String strSearchtext;
    String strBookIds = "";
    private String strUID = "";
    boolean isLogin;
    ArrayList<SearchHistoryModel> mSearchHistoryModelList = new ArrayList<>();
    private ArrayList<KeywordSearchModel.KeywordModel> mKeywordList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private String strUsername;
    private KeywordSearchViewAdapter mSearchListAdapter;
    private RecyclerView rvList;
    private String strUId;
    View headerView, header2;
    AppBarLayout appBarLayout;
    private String strKeyword;
    private String PackageName;
    private static final int KEYWORD_SEARCH = 1;
    private static final int KEYWORD_FILTER = 2;
    private TextView buttonFilter;
    private LinearLayout ivExport, ivNext, ll_buttons;
    private int totalCount;

    public KeywordFragment() {
    }

    public static ArrayList<KeywordSearchModel.KeywordModel> keywordSearchModel = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyword, container, false);
        PackageName = getActivity().getPackageName();
        loadUiElements(view);
        declaration(view);
        setHeader(view);
      */
/*  Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            totalCount = getIntent().getExtras().getInt("totalCount", 0);

            strSearchtext = getIntent().getStringExtra("strSearchKeyword");
            if (strSearchtext != null && strSearchtext.length() > 0) {
                etSearchView.setText(strSearchtext);
                buttonFilter.setVisibility(View.VISIBLE);
                ll_buttons.setVisibility(View.VISIBLE);
            }
            //    selectedString = getIntent().getExtras().getString("selectedString");
            if (totalCount > 0) {
                tvCategory.setText("Selected Books");
                tvCount.setText("" + totalCount);
                ivNext.setVisibility(View.VISIBLE);
                tvCategory.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
                tvBooks.setVisibility(View.VISIBLE);
            }
        }
*//*


        ArrayList<String> bookIdList = SharedPrefManager.getInstance(getActivity()).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS);
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

            if (totalCount > 0) {
                tvCategory.setText("Selected Books");
                tvCount.setText("" + totalCount);
                ivNext.setVisibility(View.VISIBLE);
                tvCategory.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
                tvBooks.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    private void setHeader(View view) {

        appBarLayout = getActivity().findViewById(R.id.appbar);
        headerView = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);

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

        buttonFilter = view.findViewById(R.id.button_filter);

        tvCategory = view.findViewById(R.id.tvCategory);
        tvCategory.setVisibility(View.INVISIBLE);
        mKeyboardView = bottomSheetDialogView.findViewById(R.id.keyboardView);
        String strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.requestFocus();
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        strKeyword = etSearchView.getText().toString();
        String strEdtRenamefile = mKeywordList.size() + "_Search Results For_" + strKeyword;
        edtRenameFile.setText(strEdtRenamefile);
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(getActivity(), edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, getActivity(), strLanguage, bottomSheetDialog);
                return false;
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String shareText = strEdtRenamefile + " shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
                String strPdfFile = PdfCreator.createTextPdf(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        share(shareText, strPdfFile);
                    } else {
                        Log.e("share--", "file not exits" + strPdfFile);
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
                try {
                    strPdfFile = PdfCreator.createTextPdf(
                            mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mKeywordStringList);

                    if (strPdfFile != null && strPdfFile.length() > 0) {
                        File mFile = new File(strPdfFile);
                        if (mFile.exists()) {
                            Toast.makeText(getActivity(), "Download successfully " + strPdfFile, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("download--", "file not exits" + strPdfFile);
                        }
                    }
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
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strPdfFile = PdfCreator.createFile(mediaStorageKeyWordDir.getAbsolutePath(), strEdtRenamefile, mKeywordStringList);
                if (strPdfFile != null && strPdfFile.length() > 0) {
                    File mFile = new File(strPdfFile);
                    if (mFile.exists()) {
                        UploadFile(strUId, strEdtRenamefile, mFile);
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

    private void UploadFile(String strUId, String strEdtRenamefile, File mFile) {
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUId);
        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), strEdtRenamefile);
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "1");
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), TYPE_KEYWORD_PAGE);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("keywordpdf", mFile.getName(), RequestBody.create(MediaType.parse("image/*"), mFile));
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.UploadFile(filePart, uid, filename, type, typeref, new Callback<UploadPDFModel>() {
            @Override
            public void onResponse(Call<UploadPDFModel> call, Response<UploadPDFModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        Toast.makeText(getActivity(), "Keywords Added In My Reference.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Some Error Occured..", Toast.LENGTH_LONG).show();
                    }
                }

            }


            @Override
            public void onFailure(Call<UploadPDFModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Toast.makeText(getActivity(), "Something went wrong please try again later", Toast.LENGTH_LONG).show();

            }
        });


    }

    private void askLogin() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent intent = new Intent(getActivity(), LoginWithPasswordActivity.class);
                        intent.putExtra("isLoginId", Utils.Is_Keyword_Search_Login_Id);
                        startActivityForResult(intent, KEYWORD_SEARCH);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(android.text.Html.fromHtml("<font color='#1565C0'>Please login to use this functionality.</font>"))
                .setPositiveButton("Login", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    */
/*private void showShareDialog(View v) {
        PopupMenu popup = new PopupMenu(getActivity(),v);
        popup.setOnMenuItemClickListener(getActivity());
        popup.inflate(R.menu.share_menu);
        popup.show();
    }*//*


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


    public void showFilterDialog() {
        Intent i = new Intent(getActivity(), MultiCheckActivity.class);
        Log.e("keyword", strKeyword);
        i.putExtra("KID", strKeyword);
        startActivityForResult(i, KEYWORD_FILTER);
       */
/* FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = FilterDialogFragment.newInstance(0);
        newFragment.show(ft, "dialog");*//*

       */
/* dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();*//*

    }

    private void loadUiElements(View view) {
        rvList = view.findViewById(R.id.rvList);
        Util.commonKeyboardHide(getActivity());
        etSearchView = view.findViewById(R.id.etSearchView);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        ivKeyboard = view.findViewById(R.id.ivKeyboard);
        llFilter = view.findViewById(R.id.llFilter);
        ivExport = view.findViewById(R.id.ivExport);
        buttonFilter = view.findViewById(R.id.button_filter);
        llClose = view.findViewById(R.id.llClose);
        ivClose = view.findViewById(R.id.ivClose);
        llKeywordCount = view.findViewById(R.id.llKeywordCount);
        tvKeywordCount = view.findViewById(R.id.tvKeywordCount);
        ivSend = view.findViewById(R.id.ivSend);
        tvKeywordCount = view.findViewById(R.id.tvKeywordCount);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_buttons.setVisibility(View.GONE);
                buttonFilter.setVisibility(View.GONE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);
            }
        });
        //callKeywordDataApi("");
        etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(getActivity(), etSearchView);
                ll_buttons.setVisibility(View.GONE);
                buttonFilter.setVisibility(View.GONE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);
                //   setSearchHistoryKeywordData();
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

            }
        });
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((strKeyword != null) && (strKeyword.length() != 0)) {
                    showFilterDialog();
                } else {
                    strKeyword = etSearchView.getText().toString();
                    showFilterDialog();
                }
            }
        });

        ivExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    String strSearch = etSearchView.getText().toString();
                    if (strSearch != null && strSearch.length() > 0) {
                        List<String> mKeywordStringList = new ArrayList<>();
                        if (mKeywordList != null && mKeywordList.size() > 0) {
                            for (int i = 0; i < mKeywordList.size(); i++) {
                                String strNamr = mKeywordList.get(i).getName();
                                if (strNamr != null && strNamr.length() > 0) {
                                    mKeywordStringList.add(strNamr);
                                }
                            }

                            if (!mediaStorageKeyWordDir.exists()) {
                                mediaStorageKeyWordDir.mkdirs();
                            }

                            if (mKeywordStringList != null && mKeywordStringList.size() > 0) {
                                getShareDialog(mKeywordStringList, v);
                            }
                        } else {
                            InfoDialog("No keyword data found");
                        }
                    } else {
                        InfoDialog("Please search any keywords");
                    }
                } else {
                    askLogin();
                }
            }
        });
    }

    private void declaration(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        etSearchView.requestFocus();
        buttonFilter = view.findViewById(R.id.button_filter);

        isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        strUID = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);
        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);
            }
        });
        etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(getActivity(), etSearchView);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);//   setSearchHistoryKeywordData();
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
                mKeyboardView.setVisibility(View.GONE);
                strSearchtext = null;
                etSearchView.getText().clear();
                llFilter.setVisibility(View.VISIBLE);
                llClose.setVisibility(View.GONE);
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strValue = etSearchView.getText().toString();
                if (strValue != null && strValue.length() > 0) {
                    if ((strUID != null) && (strUID.length() != 0)) {
                        callKeywordDataApi(strValue, strUID);
                    } else {
                        callKeywordDataApi(strValue, "0");
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter value in search box", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void share(String shareData, String mFilePath) {
        File mFile = new File(mFilePath);
        Uri fileUri = Uri.fromFile(mFile);
        //  Uri fileUri = FileProvider.getUriForFile(KeywordSearchActivity.this, KeywordSearchActivity.this.getActivity()().getPackageName() + ".provider", mFile);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType(URLConnection.guessContentTypeFromName(mFile.getName()));
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, shareData);
        intentShareFile.putExtra(Intent.EXTRA_TEXT, shareData);
        startActivity(Intent.createChooser(intentShareFile, shareData));
    }


    private void callKeywordDataApi(String strValue, String strUID) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        String langType = Util.lang_code;

        if (langType == null || langType.length() == 0) {
            langType = "0";
        }
        ApiClient.getKeyword(strValue, strUID, strBookIds, langType, new Callback<KeywordSearchModel>() {
            @Override
            public void onResponse(Call<KeywordSearchModel> call, retrofit2.Response<KeywordSearchModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        ArrayList<KeywordSearchModel.KeywordModel> keywordSearchModel = new ArrayList<>();
                        keywordSearchModel = response.body().getData();
                        if (keywordSearchModel != null && keywordSearchModel.size() > 0) {
                            setkeywordData(keywordSearchModel);
                        }
                    } else {
                        mKeyboardView.setVisibility(View.GONE);
                        tvKeywordCount.setVisibility(View.VISIBLE);
                        llKeywordCount.setVisibility(View.VISIBLE);
                        tvKeywordCount.setText("Keyword not found");
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        setkeywordData(new ArrayList<>());
                    }
                }
               */
/* if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        ArrayList<KeywordSearchModel.KeywordModel> keywordSearchModel = new ArrayList<>();
                        keywordSearchModel = response.body().getData();
                        if (keywordSearchModel != null && keywordSearchModel.size() > 0) {
                            setkeywordData(keywordSearchModel);
                        }
                    } else {
                        mKeyboardView.setVisibility(View.GONE);
                        tvKeywordCount.setVisibility(View.VISIBLE);
                        llKeywordCount.setVisibility(View.VISIBLE);
                        tvKeywordCount.setText("Keyword not found");
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        setkeywordData(new ArrayList<>());
                        //    keywordSearchModel = response.body().getData();
                        //  if (keywordSearchModel != null && keywordSearchModel.size() > 0) {
                        // Intent i = new Intent(getActivity(), KeywordSearchActivity.class);
                        //i.putExtra("strSearchKeyword", strValue);
                        //startActivity(i);
                        //  getActivity().finish();
                        // } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    //  }
                }*//*

            }

            @Override
            public void onFailure(Call<KeywordSearchModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void setkeywordData(ArrayList<KeywordSearchModel.KeywordModel> keywordList) {
        if (keywordList == null) {
            return;
        }
        mKeywordList = keywordList;
       */
/* if (mKeywordList != null && mKeywordList.size() > 0) {
            ivHeaderIcon.setVisibility(View.VISIBLE);
            tvHeaderCount.setVisibility(View.VISIBLE);
            tvHeaderCount.setText("" + mKeywordList.size());
        } else {
            llAddItem.setVisibility(View.GONE);
        }*//*

        mKeyboardView.setVisibility(View.GONE);

        if (mKeywordList != null && mKeywordList.size() > 0) {
            tvKeywordCount.setVisibility(View.VISIBLE);
            llKeywordCount.setVisibility(View.VISIBLE);
            tvKeywordCount.setText("" + mKeywordList.size() + " Keywords found");
        } else {
            llKeywordCount.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setVisibility(View.VISIBLE);
        mSearchListAdapter = new KeywordSearchViewAdapter(getActivity(), mKeywordList,m,new KeywordSearchViewAdapter.SearchInterfaceListener() {
            @Override
            public void onDetailsClick(KeywordSearchModel.KeywordModel mKeywordModel, int position) {

            }
        });
        rvList.setAdapter(mSearchListAdapter);


        //   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rvList.addOnScrollListener(new HideingScrollListener() {
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onDetailsClick(View view, BookListResModel.BookDetailsModel mKeywordModel, int position) {
    }

    @Override
    public void onSelectSearchKeyword(SearchHistoryModel SearchHistoryModel, int position) {
    }

    @Override
    public void selectedBook(String cat, String books) {
        tvCategory.setText(cat);
        tvCount.setText(books);
        ivNext.setVisibility(View.VISIBLE);
        tvCategory.setVisibility(View.VISIBLE);
        tvCount.setVisibility(View.VISIBLE);
        tvBooks.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        onActivityResult(requestCode, resultCode, data);
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
                    tvCategory.setText("Selected Books");
                    tvCount.setText("" + totalCount);
                    ivNext.setVisibility(View.VISIBLE);
                    tvCategory.setVisibility(View.VISIBLE);
                    tvCount.setVisibility(View.VISIBLE);
                    tvBooks.setVisibility(View.VISIBLE);
                } else {
                    ivNext.setVisibility(View.GONE);
                    tvCategory.setVisibility(View.GONE);
                    tvCount.setVisibility(View.GONE);
                    tvBooks.setVisibility(View.GONE);
                }
                String strValue = etSearchView.getText().toString();
                if (strValue != null && strValue.length() > 0) {
                    if ((strUId != null) && (strUId.length() != 0)) {
                        callKeywordDataApi(strValue, strUId);
                    } else {
                        callKeywordDataApi(strValue, "0");
                    }
                }
            }
        }
    }
}*/

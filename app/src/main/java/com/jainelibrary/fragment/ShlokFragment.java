package com.jainelibrary.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.jainelibrary.R;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.activity.ShlokSearchActivity;
import com.jainelibrary.activity.ShlokSearchDetailsActivity;
import com.jainelibrary.adapter.ShlokSearchAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.ShlokSearchResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jainelibrary.utils.Utils.TYPE_SHLOK_PAGE;

public class ShlokFragment extends Fragment implements ShlokSearchAdapter.SearchInterfaceListener {

    private final String TAG = "Shlok";
    private EditText etSearchView;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend;
    LinearLayout llFilter, llClose, ivShare, llKeywordCount;
    private String strLanguage;
    String[] language = {"English", "Gujarati", "Hindi"};
    RecyclerView rvList;
    private String strSearchtext;
    String strBookIds = "";
    TextView tvNoRecord;
    public static ArrayList<ShlokSearchResModel.ShlokResModel> keywordShlokSearchModel = new ArrayList<>();
    public static ArrayList<ShlokSearchResModel.ShlokResModel> tempKeywordShlokSearchModel = new ArrayList<>();
    private ShlokSearchAdapter mSearchListAdapter;
    private String strUid;
    boolean isLogin;
    View headerView;
    private View header1, header2, appBarLayout;

    public ShlokFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shlok, container, false);
        loadUiElements(view);
        declaration(view);
        setHeader(view);
        setHeaders();
        return view;
    }

    private void setHeaders() {
        header1 = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
        appBarLayout = getActivity().findViewById(R.id.appbar);
    }

    private void loadUiElements(View view) {
        strUid = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        Util.commonKeyboardHide(getActivity());
        etSearchView = view.findViewById(R.id.etSearchView);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        ivKeyboard = view.findViewById(R.id.ivKeyboard);
        llFilter = view.findViewById(R.id.llFilter);
        llClose = view.findViewById(R.id.llClose);
        ivClose = view.findViewById(R.id.ivClose);
        ivSend = view.findViewById(R.id.ivSend);
        rvList = view.findViewById(R.id.rvList);
        tvNoRecord = view.findViewById(R.id.tvNoRecord);
        appBarLayout = getActivity().findViewById(R.id.appbar);
    }

    private void declaration(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            if ((strUid != null) && (strUid.length() != 0)) {
                callShlokKeywordSearchApi(strUid, "");
            }
        } else {

            callShlokKeywordSearchApi("0", "");
        }
        etSearchView.requestFocus();
        etSearchView.setShowSoftInputOnFocus(false);

        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        //  Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);

        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                Util.hideKeyBoard(getActivity(), etSearchView);
                Utils.showDefaultKeyboardDialog(getActivity());
            }
        });
        etSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Util.hideKeyBoard(getActivity(), etSearchView);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);//   setSearchHistoryKeywordData();
//                return false;
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
        ivSend.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                Util.hideKeyBoard(getActivity(), etSearchView);
                Log.e("search", "search");
            //    llFilter.setVisibility(View.GONE);
               // buttonFilter.setVisibility(View.GONE);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
//                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
               getSearch(); return false;
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strSearchtext = null;
                etSearchView.getText().clear();
                llFilter.setVisibility(View.VISIBLE);
                llClose.setVisibility(View.GONE);
                mKeyboardView.setVisibility(View.GONE);

                if (tempKeywordShlokSearchModel != null && tempKeywordShlokSearchModel.size() > 0) {
                    setShlokKeywordData(tempKeywordShlokSearchModel);
                    rvList.setVisibility(View.VISIBLE);
                    tvNoRecord.setVisibility(View.GONE);
                } else {
                    Log.e("null size--", "keyword--" + tempKeywordShlokSearchModel.size());
                    rvList.setVisibility(View.GONE);
                    tvNoRecord.setVisibility(View.VISIBLE);
                }
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSearch();
            }
        });
    }
    private void getSearch() {
        Util.hideKeyBoard(getActivity(), etSearchView);
        final String strValue = etSearchView.getText().toString();
        Log.e("strvalue", strValue);
        if (strValue != null && strValue.length() > 0) {
            Log.e("strUid3", strUid);
            if (isLogin) {
                if ((strUid != null) && (strUid.length() != 0)) {
                    callShlokKeywordSearchApi(strUid, strValue);
                }
            } else {

                callShlokKeywordSearchApi("0", strValue);
            }
        } else {
            Utils.showInfoDialog(getActivity(), "Please enter value in search box");
        }
    }
    private void callShlokKeywordSearchApi(String strUID, String strValue) {

        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getSearchShlok(strUID, strValue, "1", new Callback<ShlokSearchResModel>() {
            @Override
            public void onResponse(Call<ShlokSearchResModel> call, retrofit2.Response<ShlokSearchResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        keywordShlokSearchModel = response.body().getData();
                        if (keywordShlokSearchModel != null && keywordShlokSearchModel.size() > 0) {
                            if (strValue != null && strValue.length() > 0) {
                                Intent i = new Intent(getActivity(), ShlokSearchActivity.class);
                                i.putExtra("strSearchKeyword", strValue);
                                startActivity(i);
                                getActivity().finish();
                            } else {
                                tempKeywordShlokSearchModel = new ArrayList<>();
                                tempKeywordShlokSearchModel = keywordShlokSearchModel;
                                setShlokKeywordData(tempKeywordShlokSearchModel);
                                rvList.setVisibility(View.VISIBLE);
                                tvNoRecord.setVisibility(View.GONE);
                            }
                        } else {
                            rvList.setVisibility(View.GONE);
                            tvNoRecord.setVisibility(View.GONE);
                        }
                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        rvList.setVisibility(View.GONE);
                        mKeyboardView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ShlokSearchResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });

    }

    private void setShlokKeywordData(ArrayList<ShlokSearchResModel.ShlokResModel> keywordList) {
        if (keywordList == null || keywordList.size() == 0) {
            return;
        }
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setVisibility(View.VISIBLE);
        mSearchListAdapter = new ShlokSearchAdapter(getActivity(), keywordList, this);
        rvList.setAdapter(mSearchListAdapter);
        rvList.addOnScrollListener(new HidingScrollListener() {
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


    private void setHeader(View view) {

        headerView = view.findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        TextView tvPgCount = headerView.findViewById(R.id.tvPgCount);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Shlok Search");
    }

    @Override
    public void onDetailsClick(ShlokSearchResModel.ShlokResModel shlokResModel, int position) {
        String strName = shlokResModel.getName();
        String strId = shlokResModel.getId();
        Intent i = new Intent(getActivity(), ShlokSearchDetailsActivity.class);
        i.putExtra("shlokGranthName", strName);
        i.putExtra("shlokGranthId", strId);
        startActivity(i);
    }
    private void UploadFile(String strUId, String strEdtRenamefile, File mFile) {
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUId);
        RequestBody filename = RequestBody.create(MediaType.parse("text/*"),"pdf_"+ strEdtRenamefile);
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "0");
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), TYPE_SHLOK_PAGE);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("pdf_file", mFile.getName(), RequestBody.create(MediaType.parse("image/*"), mFile));
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.addMyShelfs(uid, null, null, type, typeref, filename,null,null, null, filePart, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        Utils.showInfoDialog(getActivity(), "Keywords Added In My Reference.");
                    } else {
                        Utils.showInfoDialog(getActivity(), "Some Error Occured..");
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

}


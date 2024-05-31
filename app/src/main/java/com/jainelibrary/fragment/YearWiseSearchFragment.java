package com.jainelibrary.fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.jainelibrary.IOnBackPressed;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.activity.FilterCategoriesActivity;
import com.jainelibrary.activity.FilterMenuActivity;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.SearchReferenceActivity;
import com.jainelibrary.activity.YearListActivity;
import com.jainelibrary.adapter.YearWiseListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.YearResModel;
import com.jainelibrary.model.YearResponseModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
public class YearWiseSearchFragment extends Fragment implements IOnBackPressed {

    private final String TAG = "IndexFragment";
    private static final int YEAR_SEARCH = 1;
    private static final int YEAR_FILTER = 2;
    private EditText etSearchView;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend;
    LinearLayout llFilter, llClose, ivShare, llKeywordCount;
    private String strLanguage;
    String[] language = {"English", "Gujarati", "Hindi"};
    View headerView;
    private String strSearchtext;
    String strBookIds = "";
    public static ArrayList<YearResModel.Year> yearModel = new ArrayList<>();
    public static ArrayList<YearResModel.Year> tempYearModel = new ArrayList<>();
    private RecyclerView rvList;
    private YearWiseListAdapter mSearchListAdapter;
    private TextView tvNoRecord;
    View view1, view2;
    AppBarLayout appBarLayout;
    private LinearLayout llShowCount, llFooterFilter;
    private TextView tvFilterCounts;

    public YearWiseSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yearwise, container, false);
        loadUiElements(view);
        declaration(view);
        setHeader(view);
        onEventClickListener();
        return view;

    }



    private void loadUiElements(View view) {
        if (mKeyboardView != null)
            Util.commonKeyboardHide(getActivity());

        etSearchView = view.findViewById(R.id.etSearchView);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        ivKeyboard = view.findViewById(R.id.ivKeyboard);
        llFilter = view.findViewById(R.id.llFilter);
        llClose = view.findViewById(R.id.llClose);
        ivClose = view.findViewById(R.id.ivClose);
        ivSend = view.findViewById(R.id.ivSend);
        rvList = view.findViewById(R.id.rvYearlist);
        tvNoRecord = view.findViewById(R.id.tvNoRecord);
        llShowCount = view.findViewById(R.id.llShowCount);
        llFooterFilter = view.findViewById(R.id.llFooterFilter);
        tvFilterCounts = view.findViewById(R.id.tvFilterCounts);
    }

    private void onEventClickListener() {
        tvFilterCounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("1");
            }
        });

        llFooterFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("1");
            }
        });
    }

    private void getYearList() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getYear(new Callback<YearResModel>() {
            @Override
            public void onResponse(Call<YearResModel> call, retrofit2.Response<YearResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    assert response.body() != null;
                    yearModel = response.body().getData();
                    if (yearModel != null && yearModel.size() > 0) {
                      /*  if (strValue != null && strValue.length() > 0) {
                            Intent i = new Intent(getActivity(), YearListActivity.class);
                            i.putExtra("strSearchKeyword", strValue);
                            startActivity(i);
                            getActivity().finish();
                        } else {*/
                        tempYearModel = new ArrayList<>();
                        tempYearModel = yearModel;
                        //  setIndexKeywordData(yearModel);
                        //setIndexKeywordData(yearModel);
                        tvNoRecord.setVisibility(View.GONE);
                        rvList.setVisibility(View.VISIBLE);

                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        rvList.setVisibility(View.GONE);
                    }
                } else {
                    tvNoRecord.setVisibility(View.VISIBLE);
                    rvList.setVisibility(View.GONE);
                    mKeyboardView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<YearResModel> call, Throwable throwable) {
                String message = throwable.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }


        });
    }



    private void declaration(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        getYearList();
        etSearchView.requestFocus();
        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        // Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);

        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.showDefaultKeyboardDialog(getActivity());
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
            }
        });
      /*  etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(getActivity(), etSearchView);

                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                //   setSearchHistoryKeywordData();
                return false;
            }
        });*/
        etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
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
                Util.hideKeyBoard(getActivity(), etSearchView);
                Log.e("search", "search");
                //   ll_buttons.setVisibility(View.GONE);
                // buttonFilter.setVisibility(View.GONE);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                final String strValue = etSearchView.getText().toString();
                if (strValue != null && strValue.length() > 0) {
                    getYearList();
                } else {
                    Utils.showInfoDialog(getActivity(), "Please enter value in search box");
                }
                return false;
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
                if (tempYearModel != null && tempYearModel.size() > 0) {
                    //setIndexKeywordData(tempYearModel);
                    rvList.setVisibility(View.VISIBLE);
                    tvNoRecord.setVisibility(View.GONE);
                } else {
                    rvList.setVisibility(View.GONE);
                    tvNoRecord.setVisibility(View.VISIBLE);
                }
            }
        });

        etSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                  //  getSearch();
                    return true;
                }
                return false;
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strValue = etSearchView.getText().toString();
               // if (strValue != null && strValue.length() > 0) {
                    getYearList();
               /* } else {
                    Toast.makeText(getActivity(), "Please enter value in search box", Toast.LENGTH_SHORT).show();
                }*/
            }
        });


        String strFocusCounts = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_FOCUS_DATA);
        String strLensCounts = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LENS_DATA);

        if (strFocusCounts != null && strFocusCounts.length() > 0) {
            tvFilterCounts.setText(strFocusCounts);
        }

        if (strLensCounts != null && strLensCounts.length() > 0) {
            tvFilterCounts.setText(strLensCounts);
        }
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<YearResModel.Year> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (int i = 0; i < yearModel.size(); i++) {
            //if the existing elements contains the search input
            if (yearModel.get(i).getYear().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(yearModel.get(i));
            }
        }

        //calling a method of the adapter class and passing the filtered list
        if (mSearchListAdapter != null)
            filterdNames.add(yearModel.get(0));
           // mSearchListAdapter.filterList(filterdNames);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setHeader(View view) {
        appBarLayout = getActivity().findViewById(R.id.appbar);
        view1 = getActivity().findViewById(R.id.header);
        view2 = getActivity().findViewById(R.id.header2);
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
        tvPageName.setText("Year Search");
    }
    @Override
    public boolean onBackPressed() {


        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
           // buttonFilter.setVisibility(View.VISIBLE);
           // ll_buttons.setVisibility(View.VISIBLE);
        } else {
            Intent i = new Intent(getActivity(), SearchReferenceActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish();
        }
        return false;
    }

    public void showFilterDialog(String tabId) {
        strSearchtext = etSearchView.getText().toString();

        if ((strSearchtext != null) && (strSearchtext.length() != 0)) {
            //showFilterDialog("2");
            Intent i = new Intent(getActivity(), FilterCategoriesActivity.class);
            Log.e("keyword", strSearchtext);
            i.putExtra("KID", strSearchtext);
            startActivityForResult(i, YEAR_FILTER);
        } else {
            strSearchtext = etSearchView.getText().toString();
            //showFilterDialog("2");
            Intent i = new Intent(getActivity(), FilterCategoriesActivity.class);
            Log.e("keyword", strSearchtext);
            i.putExtra("KID", strSearchtext);
            startActivityForResult(i, YEAR_FILTER);
        }
    }

    void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), YEAR_SEARCH);
    }
    /*private void setIndexKeywordData(ArrayList<YearResModel.Year> keywordList) {
        if (keywordList == null || keywordList.size() == 0) {
            return;
        }
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setVisibility(View.VISIBLE);
        mSearchListAdapter = new YearWiseListAdapter(getActivity(), keywordList, new YearWiseListAdapter.YearClickListener() {
            @Override
            public void onYearClick(ArrayList<YearResponseModel.YearModel> view, int position) {


                Intent i = new Intent(getActivity(), YearListActivity.class);
                i.putExtra("YearID", yearModel.get(position).getId());
                i.putExtra("YearName", yearModel.get(position).getYear());
                startActivity(i);


            }
        });
        rvList.setAdapter(mSearchListAdapter);
        rvList.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                view1.setTranslationY(-80);
                view2.setTranslationY(-40);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                appBarLayout.setMinimumHeight(0);
            }

            @Override
            public void onShow() {
                view1.setTranslationY(0);
                view2.setTranslationY(0);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });
      *//*  rvList.addOnScrollListener(new HideingScrollListener() {
            @Override
            public void onHide() {
                view1.setTranslationY(-40);
                view1.setVisibility(View.GONE);
                appBarLayout.setMinimumHeight(0);
            }

            @Override
            public void onShow() {
                view1.setTranslationY(0);
                view1.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });*//*
      *//*  rvList.setOnScrollListener(new HideingScrollListener() {
            @Override
            public void onShow() {
                //   headerView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                appBarLayout.setMinimumHeight(80);
                view1.setTranslationY(0);
                view1.setVisibility(View.VISIBLE);
                //view2.setTranslationY(0);
                //view2.setVisibility(View.VISIBLE);
                appBarLayout .setVisibility(View.VISIBLE);
                //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                //  appBarLayout.setVisibility(View.VISIBLE);
                Log.e("headerView", "VISIBLE");
            }

            @Override
            public void onHide() {
                //       headerView.animate().translationY(-40).setInterpolator(new DecelerateInterpolator(2)).start();
             *//**//*   params.setMarginStart(-40);
                rvkey.setLayoutParams(params);*//**//*
                appBarLayout .setMinimumHeight(40);
                view1.setTranslationY(-40);
                view1.setVisibility(View.GONE);
           //     view2.setTranslationY(-40);
            //    view2.setVisibility(View.GONE);//((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                //appBarLayout.setMinimumHeight(0);

                Log.e("headerView", "GONE");
            }
        });*//*
    }*/



    }


/*
                list.add("1953");
                list.add("1954");
                list.add("1955");
                list.add("1956");
                list.add("1957");
                list.add("1958");
                list.add("1959");
                list.add("1960");
                list.add("1961");
                list.add("1962");
                list.add("1963");
                list.add("1964");
                list.add("1965");
                list.add("1966");
                list.add("1967");
                list.add("1968");
                list.add("1969");
                list.add("1970");
                list.add("1971");
                list.add("1972");
                list.add("1973");
                list.add("1974");
                list.add("1975");
                list.add("1976");
                list.add("1977");
                list.add("1978");
                list.add("1979");
                list.add("1980");
                list.add("1981");
                list.add("1982");
                list.add("1983");
                list.add("1984");
                list.add("1985");
                list.add("1986");
                list.add("1987");
                list.add("1988");
                list.add("1989");
                list.add("1990");
                list.add("1991");
                list.add("1992");
                list.add("1993");
                list.add("1994");
                list.add("1995");
                list.add("1996");
                list.add("1997");
                list.add("1998");
                list.add("1999");
                list.add("2000");
                list.add("2001");
                list.add("2002");
                list.add("2003");
                list.add("2004");
                list.add("2005");
                list.add("2006");
                list.add("2007");
                list.add("2008");
                list.add("2009");
                list.add("2010");
                list.add("2011");
                list.add("2012");
                list.add("2013");
                list.add("2014");
                list.add("2015");
                list.add("2016");
                list.add("2017");
                list.add("2018");
                list.add("2019");
                list.add("2020");*/

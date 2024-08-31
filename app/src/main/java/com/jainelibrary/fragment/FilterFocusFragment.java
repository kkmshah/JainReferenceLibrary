package com.jainelibrary.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.adapter.FilterBooksAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.FilterBookResModel;
import com.jainelibrary.retrofitResModel.SaveFilterBooksResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FilterFocusFragment extends Fragment implements FilterBooksAdapter.onFocusSelectListener {
    private static final String TAG = FilterFocusFragment.class.getSimpleName();
    LinearLayout llButtons;
    RecyclerView rvFocus;
    Button btnCancel, btnApply;
    List<FilterBookResModel.FilterModel> focusBookList = new ArrayList<>();
    private String strUid;
    private ArrayList<String> myBookIdList = new ArrayList<>();
    private FilterBooksAdapter filterBooksAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_focus, container, false);
        loadUiElements(view);
        declaration();
        return view;
    }

    private void loadUiElements(View view) {
        rvFocus = view.findViewById(R.id.rvFocus);
        llButtons = view.findViewById(R.id.llButtons);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnApply = view.findViewById(R.id.btnApply);
    }

    private void declaration() {

        if (myBookIdList == null || myBookIdList.size() == 0) {
            myBookIdList = new ArrayList<>();
        }

        strUid = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

        callFocusBookList();

        onEventClickListener();

    }

    private void onEventClickListener() {

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBookIdList != null && myBookIdList.size() > 0) {
                    String strTempMyBookIds = null;
                    for (int i = 0; i < myBookIdList.size(); i++) {
                        String strBookId = myBookIdList.get(i);
                        if (strBookId != null && strBookId.length() > 0) {
                            if (strTempMyBookIds != null && strTempMyBookIds.length() > 0) {
                                strTempMyBookIds = strTempMyBookIds + "," + strBookId;
                            } else {
                                strTempMyBookIds = strBookId;
                            }
                        }
                    }

                    if (strTempMyBookIds != null && strTempMyBookIds.length() > 0) {
                        callSaveFocusBooksApi(strTempMyBookIds, false);
                    }
                } else {
                    SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT, "");
                    callSaveFocusBooksApi("", false);
                }
            }
        });
    }

    public void transferToHome(boolean isClear) {
        if (isClear) {
            return;
        }

        Intent i = new Intent(getActivity(), KeywordsMainFragment.class);
        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();
    }


    private void callFocusBookList() {

        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please wait...", false);
        ApiClient.getFilterBookList(strUid, new Callback<FilterBookResModel>() {
            @Override
            public void onResponse(Call<FilterBookResModel> call, retrofit2.Response<FilterBookResModel> response) {

                if (response.isSuccessful()) {
                    FilterBookResModel tempFilter = response.body();
                    Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(tempFilter));

                    if (response.body().isStatus()) {
                        focusBookList = response.body().getData();

                        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_TOTAL_FOCUS_DATA, String.valueOf(focusBookList.size()));

                        String strMyBookCount = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT);

                        int bookCount = 0;
                        if (strMyBookCount != null && strMyBookCount.length() != 0) {
                            bookCount = Integer.parseInt(strMyBookCount);
                        }

                        if (focusBookList != null && focusBookList.size() > 0) {
                            if (bookCount > 0) {
                                for (int i = 0; i < focusBookList.size(); i++) {
                                    String strId = String.valueOf(focusBookList.get(i).getId());
                                    int strFilter = focusBookList.get(i).getFilter();
                                    if (strFilter == 1) {
                                        if (myBookIdList != null && !myBookIdList.contains(strId)) {
                                            myBookIdList.add(strId);
                                        }
                                    } else {
                                        if (myBookIdList != null && myBookIdList.contains(strId)) {
                                            focusBookList.get(i).setFilter(1);
                                            focusBookList.get(i).setSelected(true);
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < focusBookList.size(); i++) {
                                    String strId = String.valueOf(focusBookList.get(i).getId());
                                    focusBookList.get(i).setSelected(true);
                                    focusBookList.get(i).setFilter(1);
                                    if (myBookIdList != null && !myBookIdList.contains(strId)) {
                                        myBookIdList.add(strId);
                                    }
                                }
                            }
                           /* if (myBookIdList != null && myBookIdList.size() > 0) {
                                tvFocusCount.setText("(" + myBookIdList.size() + "/" + focusBookList.size() + ")");
                            } else {
                                tvFocusCount.setText("(0" + "/" + focusBookList.size() + ")");
                            }*/
                            setMyBooksAdapter(focusBookList);
                        }
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                    }
                    Utils.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<FilterBookResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e(TAG, "getFilterBookList OnFailure --" + t.getMessage());
            }
        });
    }


    private void callSaveFocusBooksApi(String strBookIds, boolean isClear) {

        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait...", false);

        ApiClient.saveFilterBooks(strUid, strBookIds, new Callback<SaveFilterBooksResModel>() {
            @Override
            public void onResponse(Call<SaveFilterBooksResModel> call, retrofit2.Response<SaveFilterBooksResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        if (myBookIdList != null && myBookIdList.size() > 0) {

                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT, String.valueOf(myBookIdList.size()));
                            Utils.showInfoDialog(getActivity(), "You focused on " + myBookIdList.size() + " books from " + focusBookList.size());

                            String strFocus = myBookIdList.size() + "/" + focusBookList.size();
                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FOCUS_DATA, strFocus);
                        }
                        transferToHome(isClear);
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                        transferToHome(isClear);
                    }
                } else {
                    transferToHome(isClear);
                }
            }

            @Override
            public void onFailure(Call<SaveFilterBooksResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                transferToHome(isClear);
                Log.e(TAG, "saveFilterBooks OnFailure --" + t.getMessage());
            }
        });
    }

    private void setMyBooksAdapter(List<FilterBookResModel.FilterModel> filterModelList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFocus.setLayoutManager(linearLayoutManager);
        filterBooksAdapter = new FilterBooksAdapter(getActivity(), filterModelList, this);
        rvFocus.setAdapter(filterBooksAdapter);
        rvFocus.setHasFixedSize(true);
    }


    public void onBookImageClick(FilterBookResModel.FilterModel filterBook, int position) {
        String strBookIds = String.valueOf(filterBook.getId());
        boolean isBookSelected = filterBook.isSelected();
        Log.e(TAG, "strBookIds or selected --" + strBookIds + isBookSelected);

        String strImageUrl = filterBook.getBook_large_image();
        String fallbackImage = filterBook.getBook_image();

        Log.e("strImageUrl--", "index--" + strImageUrl);
        Intent i = new Intent(getActivity(), ZoomImageActivity.class);
        i.putExtra("image", strImageUrl);
        i.putExtra("fallbackImage", fallbackImage);

        i.putExtra("url", true);
        startActivity(i);

    }
    @Override
    public void onFocusSelect(List<FilterBookResModel.FilterModel> filterBookList, int position) {
        String strBookIds = String.valueOf(filterBookList.get(position).getId());
        boolean isBookSelected = filterBookList.get(position).isSelected();
        Log.e(TAG, "strBookIds or selected --" + strBookIds + isBookSelected);

        if (strBookIds != null && strBookIds.length() > 0) {
            if (isBookSelected) {
                if (!myBookIdList.contains(strBookIds)) {
                    myBookIdList.add(strBookIds);
                }
            } else {
                if (myBookIdList.contains(strBookIds)) {
                    myBookIdList.remove(strBookIds);
                }
            }

            //  tvFocusCount.setText("(" + myBookIdList.size() + "/" + filterBookList.size() + ")");
        }

    }
}

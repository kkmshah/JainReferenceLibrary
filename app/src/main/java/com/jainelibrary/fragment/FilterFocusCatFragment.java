package com.jainelibrary.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.multicheck.Book;
import com.jainelibrary.multicheck.GenreDataFactory;
import com.jainelibrary.multicheck.MultiCheckGenre;
import com.jainelibrary.multicheck.MultiCheckGenreAdapter;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.SaveFilterBooksResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class FilterFocusCatFragment extends Fragment {
    private static final String TAG = FilterFocusCatFragment.class.getSimpleName();
    LinearLayout llButtons;
    ArrayList<String> selectedBookIdList = new ArrayList<>();
    String strTotalBooks = null;
    String strDescription = null;
    MultiCheckGenreAdapter multiCheckAdapter;
    int totalCount;
    String strBookIds = "";
    String strKeyword = "";
    RecyclerView rvCategory;
    Button btnCancel, btnApply;
    RelativeLayout llInfo;
    LinearLayout llDetailsInfo;
    TextView tvDetails;
    TextView tvInfo;
    ArrayList<CategoryResModel.CategoryModel> mDataList = new ArrayList<>();
    private String strUid;
    private View header1, header2, appBarLayout;

    public FilterFocusCatFragment(String strKeyword) {
        this.strKeyword = strKeyword;
        Log.e(TAG, "keywordName construct : " + strKeyword);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_categories, container, false);
        setHeaders();
        loadUiElements(view);
        declaration();
        return view;
    }

    private void setHeaders() {
        header1 = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
        appBarLayout = getActivity().findViewById(R.id.appbar);
    }

    private void declaration() {
        selectedBookIdList = SharedPrefManager.getInstance(getActivity()).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_FOCUS_BOOK_IDS);
        // strKeyword = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LAST_SEARCH_KEYWORD);
        strUid = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

        Log.e(TAG, "keywordName : " + strKeyword);

        if (selectedBookIdList == null || selectedBookIdList.size() == 0) {
            selectedBookIdList = new ArrayList<>();
        }

        tvDetails.setText("What is Focus?");
        callCategoryApi(strKeyword);

        onEventClickListener();

    }

    private void onEventClickListener() {

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        llInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getInfoDialogs(strDescription);
                if (strDescription != null && strDescription.length() > 0) {
                    if (llDetailsInfo.getVisibility() == View.VISIBLE){
                        llDetailsInfo.setVisibility(View.GONE);
                    }else{
                        llDetailsInfo.setVisibility(View.VISIBLE);
                    }
                    tvInfo.setText(strDescription);
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showProgressDialog(getActivity(), "Please wait...", false);
                totalCount = selectedBookIdList.size();
                Log.e("TotalCount", ""+totalCount);
                if (totalCount > 0) {
                    if (selectedBookIdList != null && selectedBookIdList.size() > 0) {
                        for (int i = 0; i < selectedBookIdList.size(); i++) {
                            String strTempBookId = selectedBookIdList.get(i);
                            if (strTempBookId != null && strTempBookId.length() > 0) {
                                if (strBookIds != null && strBookIds.length() > 0) {
                                    strBookIds = strBookIds + "," + strTempBookId;
                                } else {
                                    strBookIds = strTempBookId;
                                }
                            }
                        }
                        SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_FOCUS_BOOK_IDS, selectedBookIdList);
                        if (strBookIds != null && strBookIds.length() > 0) {
                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_FOCUS_CAT_IDS, strBookIds);
                        }
                    } else {
                        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_FOCUS_CAT_IDS, "");

                        SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_FOCUS_BOOK_IDS, new ArrayList<>());
                    }
                } else {
                    strBookIds = "-1";
                    if (selectedBookIdList == null || selectedBookIdList.size() == 0) {
                        SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_FOCUS_BOOK_IDS, new ArrayList<>());
                    }
                }

                callSaveFocusBooksApi(strBookIds, false);

            }
        });
    }

    private void loadUiElements(View view) {
        rvCategory = view.findViewById(R.id.rvCategory);
        llButtons = view.findViewById(R.id.llButtons);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnApply = view.findViewById(R.id.btnApply);
        llInfo = view.findViewById(R.id.llInfo);
        llDetailsInfo = view.findViewById(R.id.llDetailsInfo);
        tvDetails = view.findViewById(R.id.tvDetails);
        tvInfo = view.findViewById(R.id.tvInfo);
    }


    public void callCategoryApi(String strKeyword) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Log.e("FocusCategoryApi", "Call");

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getCategory("1",strKeyword, strUid,new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
                if (response.isSuccessful()) {
                    CategoryResModel categoryResModel = response.body();
                    Log.e("responseData Category :", new GsonBuilder().setPrettyPrinting().create().toJson(categoryResModel));
                    if (response.body().isStatus()) {

                        strTotalBooks = response.body().getTotalBooks();
                        strDescription = response.body().getDescription();
                        mDataList = new ArrayList<>();
                        mDataList = response.body().getData();
                        if (mDataList != null && mDataList.size() > 0) {
                            setCatBooksAdapter(mDataList);
                        }
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

    public void setCatBooksAdapter(ArrayList<CategoryResModel.CategoryModel> mCatList) {

        if (selectedBookIdList == null || selectedBookIdList.size() == 0) {
            selectedBookIdList = new ArrayList<>();
            List<MultiCheckGenre> parentBookList = GenreDataFactory.makeMultiCheckGenres(mCatList);
            if (parentBookList != null && parentBookList.size() > 0) {
                for (int i = 0; i < parentBookList.size(); i++) {
                    MultiCheckGenre multiCheckGenre = parentBookList.get(i);
                    for (int j = 0; j < multiCheckGenre.getItems().size(); j++) {
                        Book mBook = (Book) multiCheckGenre.getItems().get(j);
                        if(mBook.isSelected()){
                            selectedBookIdList.add(mBook.getId());
                        }
                    }
                }
                totalCount = selectedBookIdList.size();
                Log.e("totalCount", "totalCount---" + totalCount);
            }
        }

        int count = 0;
        for (int i = 0; i < mCatList.size(); i++) {
            for (int j = 0; j < mCatList.get(i).getBooks().size(); j++) {
                count++;
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        multiCheckAdapter = new MultiCheckGenreAdapter(true, GenreDataFactory.makeMultiCheckGenres(mCatList), selectedBookIdList, count, getActivity(), new MultiCheckGenreAdapter.OnZoomListener() {
            @Override
            public void OnZoomClick(String strImageUrl, String fallbackImage) {

                Intent i = new Intent(getActivity(), ZoomImageActivity.class);
                i.putExtra("image", strImageUrl);
                i.putExtra("fallbackImage", fallbackImage);

                i.putExtra("url", true);
                startActivity(i);
            }
        });
        Log.e("gen : " + GenreDataFactory.makeMultiCheckGenres(mCatList).size() + "", selectedBookIdList.size() + "");
        rvCategory.setLayoutManager(layoutManager);
        //rvCategory.setHasFixedSize(true);
        rvCategory.setAdapter(multiCheckAdapter);
        rvCategory.addOnScrollListener(new HidingScrollListener() {
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

    public void transferToHome(boolean isClear) {

        Utils.dismissProgressDialog();

        if (isClear) {
            return;
        }

        Intent i = new Intent(getActivity(), KeywordsMainFragment.class);
        i.putExtra("totalCount", totalCount);
        i.putExtra("strBookIds", strBookIds);
        Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, i);
        getActivity().finish();
    }


    public void callSaveFocusBooksApi(String strBookIds, boolean isClear) {

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
                    SaveFilterBooksResModel saveFilterBooksResModel = response.body();
                    Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(saveFilterBooksResModel));
                    if (response.body().isStatus()) {
                        if (selectedBookIdList != null && selectedBookIdList.size() > 0) {
                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT, String.valueOf(selectedBookIdList.size()));
                            Utils.showInfoDialog(getActivity(), "You focused on " + selectedBookIdList.size() + " books from " + strTotalBooks);
                            String strFocus = selectedBookIdList.size() + "/" + strTotalBooks;
                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FOCUS_DATA, strFocus);
                        } else {
                            String strFocus = "0 /" + strTotalBooks;
                            Utils.showInfoDialog(getActivity(), "You focused on " + "0 books from " + strTotalBooks);

                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT, null);
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

    public void getInfoDialogs(String strMessage) {
        Dialog dialog = new IosDialog.Builder(getActivity())
                .setMessage(strMessage)
                .setMessageColor(Color.parseColor("#1565C0"))
                .setMessageSize(18)
                .setPositiveButtonColor(Color.parseColor("#981010"))
                .setPositiveButtonSize(18)
                .setPositiveButton("OK", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                    }
                }).build();
        dialog.show();
    }


}

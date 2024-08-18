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
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.multicheck.Book;
import com.jainelibrary.multicheck.GenreDataFactory;
import com.jainelibrary.multicheck.MultiCheckGenre;
import com.jainelibrary.multicheck.MultiCheckGenreAdapter;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FilterCategoriesFragment extends Fragment {
    private static final String TAG = FilterCategoriesFragment.class.getSimpleName();
    LinearLayout llButtons;
    ArrayList<String> bookIdList = new ArrayList<>();
    MultiCheckGenreAdapter multiCheckAdapter;
    int totalCount;
    String strBookIds = "";
    String strKeyword = "";
    RecyclerView rvCategory;
    Button btnCancel, btnApply;
    ArrayList<CategoryResModel.CategoryModel> mDataList = new ArrayList<>();
    private String strUid;
    RelativeLayout llInfo;
    LinearLayout llDetailsInfo;
    TextView tvDetails;
    TextView tvInfo;
    private String strDescription;
    private View header1, header2, appBarLayout;

    public FilterCategoriesFragment(String strKeyword) {
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
        bookIdList = SharedPrefManager.getInstance(getActivity()).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS);
        // strKeyword = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LAST_SEARCH_KEYWORD);

        Log.e(TAG, "keywordName : " + strKeyword);

        if (bookIdList == null || bookIdList.size() == 0) {
            bookIdList = new ArrayList<>();
        }
        tvDetails.setText("What is Filter?");

        if (strKeyword != null && strKeyword.length() > 0) {
            callCategoryApi(strKeyword);
        } else {
            //  Toast.makeText(getActivity(), "No search keyword found", Toast.LENGTH_SHORT).show();
        }

        onEventClickListener();

    }

    private void onEventClickListener() {

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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showProgressDialog(getActivity(), "Please wait...", false);

                totalCount = bookIdList.size();
                if (totalCount > 0) {
                    if (bookIdList != null && bookIdList.size() > 0) {
                        for (int i = 0; i < bookIdList.size(); i++) {
                            String strTempBookId = bookIdList.get(i);
                            if (strTempBookId != null && strTempBookId.length() > 0) {
                                if (strBookIds != null && strBookIds.length() > 0) {
                                    strBookIds = strBookIds + "," + strTempBookId;
                                } else {
                                    strBookIds = strTempBookId;
                                }
                            }
                        }
                        SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, bookIdList);
                        if (strBookIds != null && strBookIds.length() > 0) {
                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_CAT_IDS, strBookIds);
                        }
                    } else {
                        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_CAT_IDS, "");
                        SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
                    }
                } else {
                    if (bookIdList == null || bookIdList.size() == 0) {
                        SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
                    }
                }

                transferToHome(false);
            }
        });
    }

    private void loadUiElements(View view) {
        rvCategory = view.findViewById(R.id.rvCategory);
        llButtons = view.findViewById(R.id.llButtons);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnApply = view.findViewById(R.id.btnApply);
        llDetailsInfo = view.findViewById(R.id.llDetailsInfo);
        llInfo = view.findViewById(R.id.llInfo);
        tvDetails = view.findViewById(R.id.tvDetails);
        tvInfo = view.findViewById(R.id.tvInfo);
    }


    public void callCategoryApi(String strKeyword) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        strUid = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getCategory("0", strKeyword, strUid, new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
                if (response.isSuccessful()) {
                    CategoryResModel categoryResModel = response.body();
                    Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(categoryResModel));
                    if (response.body().isStatus()) {
                        strDescription = response.body().getDescription();

                        Log.e("strDescription", strDescription);

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

        if (bookIdList == null || bookIdList.size() == 0) {
            bookIdList = new ArrayList<>();
            List<MultiCheckGenre> parentBookList = GenreDataFactory.makeMultiCheckGenres(mCatList);
            if (parentBookList != null && parentBookList.size() > 0) {
                for (int i = 0; i < parentBookList.size(); i++) {
                    MultiCheckGenre multiCheckGenre = parentBookList.get(i);
                    for (int j = 0; j < multiCheckGenre.getItems().size(); j++) {
                        Book mBook = (Book) multiCheckGenre.getItems().get(j);
                        bookIdList.add(mBook.getId());
                    }
                }
                totalCount = bookIdList.size();
            }
        }

        int count = 0;
        for (int i = 0; i < mCatList.size(); i++) {
            for (int j = 0; j < mCatList.get(i).getBooks().size(); j++) {
                count++;
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        multiCheckAdapter = new MultiCheckGenreAdapter(false, GenreDataFactory.makeMultiCheckGenres(mCatList), bookIdList, count, getActivity(), new MultiCheckGenreAdapter.OnZoomListener() {
            @Override
            public void OnZoomClick(View view, int position, String s) {

            }
        });
        Log.e("gen : " + GenreDataFactory.makeMultiCheckGenres(mCatList).size() + "", bookIdList.size() + "");
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
        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();
    }

}

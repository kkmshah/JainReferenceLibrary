package com.jainelibrary.multicheck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.fragment.KeywordsMainFragment;
import com.jainelibrary.adapter.FilterBooksAdapter;
import com.jainelibrary.adapter.FilterMainCatAdapter;
import com.jainelibrary.adapter.FilterSearchOptionAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.FilterBookResModel;
import com.jainelibrary.retrofitResModel.SaveFilterBooksResModel;
import com.jainelibrary.retrofitResModel.SearchOptionResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity implements FilterMainCatAdapter.onSubCatSelectListner, FilterSearchOptionAdapter.onLensSelectListener, FilterBooksAdapter.onFocusSelectListener {

    private static final String TAG = FilterActivity.class.getSimpleName();
    String strBookIds = "";
    String strOptionIds = "";
    private Button btnCancel, btnApply;
    private ArrayList<String> bookIdList = new ArrayList<>();
    private ArrayList<String> optionBookIdList = new ArrayList<>();
    private ArrayList<String> myBookIdList = new ArrayList<>();
    private FilterMainCatAdapter filterMainCatAdapter;
    private FilterBooksAdapter filterBooksAdapter;
    private FilterSearchOptionAdapter filterSearchOptionAdapter;
    private MultiCheckGenreAdapter multiCheckAdapter;
    private RecyclerView rvCategory, rvFilter;
    private int totalCount;
    private String strUid;
    private ArrayList<CategoryResModel.CategoryModel> mCatList = new ArrayList<>();
    private ArrayList<CategoryResModel.CategoryModel> mDataList = new ArrayList<>();
    private List<SearchOptionResModel.SearchOptionModel> searchOptionModelList = new ArrayList<>();
    private List<FilterBookResModel.FilterModel> focusBookList = new ArrayList<>();
    private String kid;
    private TextView tvCategory, tvFocus, tvLens;
    private TextView tvFocusCount, tvLensCount;
    private LinearLayout llCategory, llFocus, llLens;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setHeader();
        loadUiElements();
        declaration();
        onEventListeners();
    }

    private void declaration() {
        kid = getIntent().getExtras().getString("KID");

        bookIdList = SharedPrefManager.getInstance(FilterActivity.this).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS);
        optionBookIdList = SharedPrefManager.getInstance(FilterActivity.this).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_OPTION_BOOK_IDS);
        strUid = SharedPrefManager.getInstance(FilterActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);

        if (bookIdList == null || bookIdList.size() == 0) {
            bookIdList = new ArrayList<>();
        }

        if (optionBookIdList == null || optionBookIdList.size() == 0) {
            optionBookIdList = new ArrayList<>();
        }

        if (myBookIdList == null || myBookIdList.size() == 0) {
            myBookIdList = new ArrayList<>();
        }

        setMainCatAdapter();

    }

    private void onEventListeners() {
        llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llCategory.setBackgroundColor(Color.parseColor("#ffffff"));
                llFocus.setBackgroundColor(Color.parseColor("#f2f2f2"));
                llLens.setBackgroundColor(Color.parseColor("#f2f2f2"));
                callCategoryApi();
            }
        });

        llFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llFocus.setBackgroundColor(Color.parseColor("#ffffff"));
                llCategory.setBackgroundColor(Color.parseColor("#f2f2f2"));
                llLens.setBackgroundColor(Color.parseColor("#f2f2f2"));
                callFocusBookList();
            }
        });

        llLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llLens.setBackgroundColor(Color.parseColor("#ffffff"));
                llCategory.setBackgroundColor(Color.parseColor("#f2f2f2"));
                llFocus.setBackgroundColor(Color.parseColor("#f2f2f2"));
                callSearchOptionApi();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totalCount = bookIdList.size();
                totalCount = totalCount + myBookIdList.size();
                if (totalCount > 0 || optionBookIdList != null && optionBookIdList.size() > 0) {
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
                        SharedPrefManager.getInstance(FilterActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, bookIdList);
                    } else {
                        SharedPrefManager.getInstance(FilterActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
                    }

                    if (optionBookIdList != null && optionBookIdList.size() > 0) {
                        for (int i = 0; i < optionBookIdList.size(); i++) {
                            String strTempOpnId = optionBookIdList.get(i);
                            if (strTempOpnId != null && strTempOpnId.length() > 0) {
                                if (strOptionIds != null && strOptionIds.length() > 0) {
                                    strOptionIds = strOptionIds + "," + strTempOpnId;
                                } else {
                                    strOptionIds = strTempOpnId;
                                }
                            }
                        }
                        SharedPrefManager.getInstance(FilterActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_OPTION_BOOK_IDS, optionBookIdList);
                    } else {
                        SharedPrefManager.getInstance(FilterActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_OPTION_BOOK_IDS, new ArrayList<>());
                    }

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
                        SharedPrefManager.getInstance(FilterActivity.this).saveStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT, "");
                        callSaveFocusBooksApi("", false);
                    }


                } else {
                    if (bookIdList == null || bookIdList.size() == 0) {
                        bookIdList.clear();
                        SharedPrefManager.getInstance(FilterActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
                    }

                    if (optionBookIdList == null || optionBookIdList.size() == 0) {
                        optionBookIdList.clear();
                        SharedPrefManager.getInstance(FilterActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_OPTION_BOOK_IDS, new ArrayList<>());
                    }

                    if (myBookIdList == null || myBookIdList.size() == 0) {
                        myBookIdList.clear();
                        SharedPrefManager.getInstance(FilterActivity.this).saveStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT, "");
                        callSaveFocusBooksApi("", false);
                    }
                }
            }
        });
    }

    private void loadUiElements() {
        btnCancel = findViewById(R.id.button_cancel);
        btnApply = findViewById(R.id.button_apply);
        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        rvFilter = (RecyclerView) findViewById(R.id.rvMain);
        tvCategory = findViewById(R.id.tvCategory);
        tvFocus = findViewById(R.id.tvFocus);
        tvLens = findViewById(R.id.tvLens);
        tvFocusCount = findViewById(R.id.tvFocusCount);
        tvLensCount = findViewById(R.id.tvLensCount);
        llFocus = findViewById(R.id.llFocus);
        llCategory = findViewById(R.id.llCategory);
        llLens = findViewById(R.id.llLens);
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        ImageView ivDelete = headerView.findViewById(R.id.ivDelete);
        TextView tvKey = headerView.findViewById(R.id.tvKey);
        tvKey.setVisibility(View.VISIBLE);
        ivDelete.setVisibility(View.GONE);
        tvKey.setText("Clear");
        tvKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookIdList.clear();
                optionBookIdList.clear();
                myBookIdList.clear();
                setMainCatAdapter();
                callSaveFocusBooksApi("", true);
            }
        });
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Filter");
    }


    private void setMainCatAdapter() {

       /* mCatList = new ArrayList<>();
        CategoryResModel.CategoryModel categoryModel = new CategoryResModel.CategoryModel();
        categoryModel.setId("1");
        categoryModel.setName("Categories");
        mCatList.add(categoryModel);

        categoryModel = new CategoryResModel.CategoryModel();
        categoryModel.setId("2");
        categoryModel.setName("Focus");
        mCatList.add(categoryModel);

        categoryModel = new CategoryResModel.CategoryModel();
        categoryModel.setId("3");
        categoryModel.setName("Lens");
        mCatList.add(categoryModel);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCategory.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCategory.getContext(),
                linearLayoutManager.getOrientation());
        rvCategory.addItemDecoration(dividerItemDecoration);
        filterMainCatAdapter = new FilterMainCatAdapter(this, mCatList, this::onSubSelect);
        rvCategory.setAdapter(filterMainCatAdapter);
        rvCategory.setHasFixedSize(true);*/

        llCategory.setBackgroundColor(Color.parseColor("#ffffff"));
        llFocus.setBackgroundColor(Color.parseColor("#f2f2f2"));
        llLens.setBackgroundColor(Color.parseColor("#f2f2f2"));

        callCategoryApi();
    }

    @Override
    public void onCategorySelect(ArrayList<CategoryResModel.CategoryModel> filterValues, int position) {

        String strId = filterValues.get(position).getId();
        if (strId != null && strId.equalsIgnoreCase("1")) {
            callCategoryApi();
        } else if (strId != null && strId.equalsIgnoreCase("2")) {
            callFocusBookList();
        } else if (strId != null && strId.equalsIgnoreCase("3")) {
            callSearchOptionApi();
        }
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

            tvFocusCount.setText("(" + myBookIdList.size() + "/" + filterBookList.size() + ")");
        }

    }

    public void onBookImageClick(FilterBookResModel.FilterModel filterBook, int position) {
        String strBookIds = String.valueOf(filterBook.getId());
        boolean isBookSelected = filterBook.isSelected();
        Log.e(TAG, "strBookIds or selected --" + strBookIds + isBookSelected);

        String strImageUrl = filterBook.getBook_large_image();
        String fallbackImage = filterBook.getBook_image();

        Log.e("strImageUrl--", "index--" + strImageUrl);
        Intent i = new Intent(this, ZoomImageActivity.class);
        i.putExtra("image", strImageUrl);
        i.putExtra("fallbackImage", fallbackImage);

        i.putExtra("url", true);
        startActivity(i);

    }
    @Override
    public void onLensSelect(List<SearchOptionResModel.SearchOptionModel> optionModelList, int position) {
        String strBookIds = String.valueOf(optionModelList.get(position).getId());
        boolean isBookSelected = optionModelList.get(position).isSelected();
        if (strBookIds != null && strBookIds.length() > 0) {
            if (isBookSelected) {
                if (!optionBookIdList.contains(strBookIds)) {
                    optionBookIdList.add(strBookIds);
                }
            } else {
                if (optionBookIdList.contains(strBookIds)) {
                    optionBookIdList.remove(strBookIds);
                }
            }

            tvLensCount.setText("(" + optionBookIdList.size() + "/" + optionModelList.size() + ")");

        }
    }

    @Override
    public void onInfo(List<SearchOptionResModel.SearchOptionModel> optionValuesList, int position) {

    }


    private void setCatBooksAdapter(ArrayList<CategoryResModel.CategoryModel> mCatList) {

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        multiCheckAdapter = new MultiCheckGenreAdapter(false, GenreDataFactory.makeMultiCheckGenres(mCatList), bookIdList, count, FilterActivity.this, new MultiCheckGenreAdapter.OnZoomListener() {
            public void OnZoomClick(String strImageUrl, String fallbackImage) {

                Intent i = new Intent(FilterActivity.this, ZoomImageActivity.class);
                i.putExtra("image", strImageUrl);
                i.putExtra("fallbackImage", fallbackImage);

                i.putExtra("url", true);
                startActivity(i);
            }
        });
        Log.e("gen : " + GenreDataFactory.makeMultiCheckGenres(mCatList).size() + "", bookIdList.size() + "");
        rvFilter.setLayoutManager(layoutManager);
        rvFilter.setHasFixedSize(true);
        rvFilter.setAdapter(multiCheckAdapter);
    }

    private void setMyBooksAdapter(List<FilterBookResModel.FilterModel> filterModelList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFilter.setLayoutManager(linearLayoutManager);
        filterBooksAdapter = new FilterBooksAdapter(this, filterModelList, this);
        rvFilter.setAdapter(filterBooksAdapter);
        rvFilter.setHasFixedSize(true);
    }

    private void setSearchOptionAdapter(List<SearchOptionResModel.SearchOptionModel> searchOptionModelList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFilter.setLayoutManager(linearLayoutManager);
        filterSearchOptionAdapter = new FilterSearchOptionAdapter(this, searchOptionModelList, this, optionBookIdList);
        rvFilter.setAdapter(filterSearchOptionAdapter);
        rvFilter.setHasFixedSize(true);
    }

    public void callCategoryApi() {
        Log.e("kid", kid);
        if (!ConnectionManager.checkInternetConnection(FilterActivity.this)) {
            Utils.showInfoDialog(FilterActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(FilterActivity.this, "Please Wait...", false);
        ApiClient.getCategory("0",kid, strUid, new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        mDataList = new ArrayList<>();
                        mDataList = response.body().getData();
                        if (mDataList != null && mDataList.size() > 0) {
                            setCatBooksAdapter(mDataList);
                        }
                    } else {
                        Utils.showInfoDialog(FilterActivity.this, "" + response.body().getMessage());
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

    private void callFocusBookList() {

        if (!ConnectionManager.checkInternetConnection(FilterActivity.this)) {
            Utils.showInfoDialog(FilterActivity.this, "Please check internet connection");
            return;
        }


        ApiClient.getFilterBookList(strUid, new Callback<FilterBookResModel>() {
            @Override
            public void onResponse(Call<FilterBookResModel> call, retrofit2.Response<FilterBookResModel> response) {

                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        focusBookList = response.body().getData();
                        String strMyBookCount = SharedPrefManager.getInstance(FilterActivity.this).getStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT);
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

                            if (myBookIdList != null && myBookIdList.size() > 0) {
                                tvFocusCount.setText("(" + myBookIdList.size() + "/" + focusBookList.size() + ")");
                            } else {
                                tvFocusCount.setText("(0" + "/" + focusBookList.size() + ")");
                            }
                            setMyBooksAdapter(focusBookList);
                        }
                    } else {
                        Utils.showInfoDialog(FilterActivity.this, "" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<FilterBookResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e(TAG, "getFilterBookList OnFailure --" + t.getMessage());
            }
        });
    }

    private void callSearchOptionApi() {
        if (!ConnectionManager.checkInternetConnection(FilterActivity.this)) {
            Utils.showInfoDialog(FilterActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(FilterActivity.this, "Please wait......", false);
        ApiClient.getFilterSearchOption(strUid, new Callback<SearchOptionResModel>() {
            @Override
            public void onResponse(Call<SearchOptionResModel> call, Response<SearchOptionResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        searchOptionModelList = response.body().getData();
                        if (searchOptionModelList != null && searchOptionModelList.size() > 0) {

                            if (optionBookIdList == null || optionBookIdList.size() == 0) {
                                for (int i = 0; i < searchOptionModelList.size(); i++) {
                                    String strId = String.valueOf(searchOptionModelList.get(i).getId());
                                    searchOptionModelList.get(i).setSelected(true);
                                    if (optionBookIdList != null && !optionBookIdList.contains(strId)) {
                                        optionBookIdList.add(strId);
                                    }
                                }
                            }

                            tvLensCount.setText("(" + optionBookIdList.size() + "/" + searchOptionModelList.size() + ")");

                            setSearchOptionAdapter(searchOptionModelList);
                        }
                    } else {
                        Utils.showInfoDialog(FilterActivity.this, "No Filter Option Found");
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

    private void callSaveFocusBooksApi(String strBookIds, boolean isClear) {

        if (!ConnectionManager.checkInternetConnection(FilterActivity.this)) {
            Utils.showInfoDialog(FilterActivity.this, "Please check internet connection");
            return;
        }

        ApiClient.saveFilterBooks(strUid, strBookIds, new Callback<SaveFilterBooksResModel>() {
            @Override
            public void onResponse(Call<SaveFilterBooksResModel> call, retrofit2.Response<SaveFilterBooksResModel> response) {

                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        if (myBookIdList != null && myBookIdList.size() > 0) {
                            SharedPrefManager.getInstance(FilterActivity.this).saveStringPref(SharedPrefManager.KEY_MY_BOOK_COUNT, String.valueOf(myBookIdList.size()));
                            Utils.showInfoDialog(FilterActivity.this, "You focused on " + myBookIdList.size() + " books from " + focusBookList.size());
                        }
                        //Toast.makeText(FilterActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        transferToHome(isClear);
                    } else {
                        Utils.showInfoDialog(FilterActivity.this, "" + response.body().getMessage());
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

    public void transferToHome(boolean isClear) {
        if (isClear) {
            return;
        }

        Intent i = new Intent(FilterActivity.this, KeywordsMainFragment.class);
        i.putExtra("totalCount", totalCount);
        i.putExtra("strBookIds", strBookIds);
        i.putExtra("strOptionTypeId", strOptionIds);
        setResult(RESULT_OK, i);
        finish();
    }


}

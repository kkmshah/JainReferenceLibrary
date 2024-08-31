package com.jainelibrary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.fragment.FilterCategoriesFragment;
import com.jainelibrary.fragment.KeywordsMainFragment;
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

public class FilterCategoriesActivity extends AppCompatActivity {

    private static final String TAG = FilterCategoriesActivity.class.getSimpleName();
    LinearLayout llButtons;
    ArrayList<String> bookIdList = new ArrayList<>();
    MultiCheckGenreAdapter multiCheckAdapter;
    int totalCount;
    String strBookIds = "", strKeyword = "", strFrom;
    RecyclerView rvCategory;
    Button btnCancel, btnApply;
    ArrayList<CategoryResModel.CategoryModel> mDataList = new ArrayList<>();
    private String strUid;
    RelativeLayout llInfo;
    LinearLayout llDetailsInfo;
    TextView tvDetails;
    TextView tvInfo;
    private String strDescription, strTotalFilter;
    private View header1, header2, appBarLayout;
    public boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_categories);

        Intent intent = getIntent();
        strKeyword = intent.getStringExtra("KID");
        strFrom = intent.getStringExtra("From");

        setHeader();
        loadUiElements();
        declaration();
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        View header2 = findViewById(R.id.header2);
        ImageView menu = headerView.findViewById(R.id.menu);
        menu.setImageResource(R.mipmap.backarrow);
        menu.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tvTitlePage = header2.findViewById(R.id.tvTitlePage);
        tvTitlePage.setText("Filter");

        ImageView ivSelectLogo = header2.findViewById(R.id.ivSelectLogo);

        ImageView ivFirst = headerView.findViewById(R.id.ivFirst);
        ImageView ivSecond = headerView.findViewById(R.id.ivSecond);
        ImageView ivThird = headerView.findViewById(R.id.ivThird);
        ImageView ivFour = headerView.findViewById(R.id.ivFour);
        ImageView ivFive = headerView.findViewById(R.id.ivFive);
        ImageView ivSix = headerView.findViewById(R.id.ivSix);
        ImageView ivseven = header2.findViewById(R.id.ivseven);

        ivSelectLogo.setImageResource(R.mipmap.ic_filter_setting);
        ivFirst.setImageResource(R.mipmap.home);
        ivSecond.setImageResource(R.mipmap.my_reference);
        ivThird.setImageResource(R.mipmap.book_store);
        ivFour.setImageResource(R.mipmap.user_guide);
        ivFive.setImageResource(R.mipmap.app_info);

        ivseven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterCategoriesActivity.this, HoldAndSearchActivity.class);
                startActivity(i);
            }
        });

        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterCategoriesActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterCategoriesActivity.this, MyReferenceActivity.class);
                startActivity(i);
            }
        });

        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterCategoriesActivity.this, UserGuideActivity.class);
                startActivity(i);
            }
        });

        ivFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterCategoriesActivity.this, AppInfoActivity.class);
                startActivity(i);
            }
        });

        isLogin = SharedPrefManager.getInstance(FilterCategoriesActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);

        if (isLogin) {
            ivSix.setImageResource(R.mipmap.logout_new);
        } else {
            ivSix.setImageResource(R.mipmap.login);
        }

        ivSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(FilterCategoriesActivity.this, isLogin, false);
            }
        });

        ImageView ivSearchSetting = header2.findViewById(R.id.ivSearchSetting);
        ivSearchSetting.setVisibility(View.VISIBLE);
        ivSearchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showInfoDialog(FilterCategoriesActivity.this, "You already on this screen");
            }
        });

    }

    private void declaration() {

        tvDetails.setText("What is Filter?");

        if (strKeyword != null && strKeyword.length() > 0) {
            if (strFrom != null && strFrom.length() > 0 && strFrom.equals("Keyword")){
                bookIdList = SharedPrefManager.getInstance(FilterCategoriesActivity.this).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS);
                // strKeyword = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LAST_SEARCH_KEYWORD);

                Log.e(TAG, "keywordName : " + strKeyword);

                if (bookIdList == null || bookIdList.size() == 0) {
                    bookIdList = new ArrayList<>();
                }
                callCategoryApi(strKeyword);
            }else {
                bookIdList = SharedPrefManager.getInstance(FilterCategoriesActivity.this).getFilteredBookIdList(SharedPrefManager.KEY_YEAR_FILTER_BOOK_IDS);
                // strKeyword = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LAST_SEARCH_KEYWORD);

                Log.e(TAG, "keywordName : " + strKeyword);

                if (bookIdList == null || bookIdList.size() == 0) {
                    bookIdList = new ArrayList<>();
                }
                callYearCategoryApi(strKeyword);
            }

        }

        onEventClickListener();

    }

    private void callYearCategoryApi(String strYearType) {
        if (!ConnectionManager.checkInternetConnection(FilterCategoriesActivity.this)) {
            Utils.showInfoDialog(FilterCategoriesActivity.this, "Please check internet connection");
            return;
        }
        strUid = SharedPrefManager.getInstance(FilterCategoriesActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);

        Utils.showProgressDialog(FilterCategoriesActivity.this, "Please Wait...", false);
        ApiClient.getYearCategory("1", strYearType, strUid, new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    CategoryResModel categoryResModel = response.body();
                    Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(categoryResModel));
                    if (response.body().isStatus()) {
                        mDataList = new ArrayList<>();
                        mDataList = response.body().getData();
                        String strYearTotalFilter = response.body().getTotalBooks();
                        SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveStringPref(SharedPrefManager.KEY_YEAR_FILTER_CAT_IDS, strYearTotalFilter);
                        if (mDataList != null && mDataList.size() > 0) {
                            setCatBooksAdapter(mDataList);
                        }
                    } else {
                        Utils.showInfoDialog(FilterCategoriesActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
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
                FilterCategoriesActivity.this.onBackPressed();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showProgressDialog(FilterCategoriesActivity.this, "Please wait...", false);
                if (strFrom != null && strFrom.length() > 0 && strFrom.equals("Keyword")){
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
                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveStringPref(SharedPrefManager.KEY_FILTERS_DATA, bookIdList.size() + "");

                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, bookIdList);
                            if (strBookIds != null && strBookIds.length() > 0) {
                                SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveStringPref(SharedPrefManager.KEY_FILTER_CAT_IDS, strBookIds);
                            }
                        } else {
                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveStringPref(SharedPrefManager.KEY_FILTERS_DATA, "0");

                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveStringPref(SharedPrefManager.KEY_FILTER_CAT_IDS, "");
                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
                        }
                    } else {
                        if (bookIdList == null || bookIdList.size() == 0) {
                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
                        }
                    }
                }else {
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
                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_YEAR_FILTER_BOOK_IDS, bookIdList);
                            if (strBookIds != null && strBookIds.length() > 0) {
                                SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveStringPref(SharedPrefManager.KEY_YEAR_FILTER_CAT_IDS, strBookIds);
                            }
                        } else {
                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveStringPref(SharedPrefManager.KEY_YEAR_FILTER_CAT_IDS, "");
                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_YEAR_FILTER_BOOK_IDS, new ArrayList<>());
                        }
                    } else {
                        if (bookIdList == null || bookIdList.size() == 0) {
                            SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_YEAR_FILTER_BOOK_IDS, new ArrayList<>());
                        }
                    }
                }


                transferToHome(false);
            }
        });
    }

    private void loadUiElements() {
        rvCategory = findViewById(R.id.rvCategory);
        llButtons = findViewById(R.id.llButtons);
        btnCancel = findViewById(R.id.btnCancel);
        btnApply = findViewById(R.id.btnApply);
        llDetailsInfo = findViewById(R.id.llDetailsInfo);
        llInfo = findViewById(R.id.llInfo);
        tvDetails = findViewById(R.id.tvDetails);
        tvInfo = findViewById(R.id.tvInfo);
    }


    public void callCategoryApi(String strKeyword) {
        if (!ConnectionManager.checkInternetConnection(FilterCategoriesActivity.this)) {
            Utils.showInfoDialog(FilterCategoriesActivity.this, "Please check internet connection");
            return;
        }
        strUid = SharedPrefManager.getInstance(FilterCategoriesActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);

        Utils.showProgressDialog(FilterCategoriesActivity.this, "Please Wait...", false);
        ApiClient.getCategory("0", strKeyword, strUid, new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
                if (response.isSuccessful()) {
                    CategoryResModel categoryResModel = response.body();
                    Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(categoryResModel));
                    if (response.body().isStatus()) {
                        mDataList = new ArrayList<>();
                        mDataList = response.body().getData();
                        strTotalFilter = response.body().getTotalBooks();
                        strDescription = response.body().getDescription();
                        SharedPrefManager.getInstance(FilterCategoriesActivity.this).saveStringPref(SharedPrefManager.KEY_FILTER_CAT_IDS, strTotalFilter);
                        if (mDataList != null && mDataList.size() > 0) {
                            setCatBooksAdapter(mDataList);
                        }
                    } else {
                        Utils.showInfoDialog(FilterCategoriesActivity.this, "" + response.body().getMessage());
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(FilterCategoriesActivity.this);
        multiCheckAdapter = new MultiCheckGenreAdapter(false, GenreDataFactory.makeMultiCheckGenres(mCatList), bookIdList, count,FilterCategoriesActivity.this, new MultiCheckGenreAdapter.OnZoomListener() {
            @Override
            public void OnZoomClick(String strImageUrl, String fallbackImage) {
                if(strImageUrl.isEmpty()) {
                    return;
                }
                Intent i = new Intent(FilterCategoriesActivity.this, ZoomImageActivity.class);
                i.putExtra("image", strImageUrl);
                i.putExtra("fallbackImage", fallbackImage);

                i.putExtra("url", true);
                startActivity(i);
            }
        });
        Log.e("gen : " + GenreDataFactory.makeMultiCheckGenres(mCatList).size() + "", bookIdList.size() + "");
        rvCategory.setLayoutManager(layoutManager);
        //rvCategory.setHasFixedSize(true);
        rvCategory.setAdapter(multiCheckAdapter);
        /*rvCategory.addOnScrollListener(new HidingScrollListener() {
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
        });*/
    }

    public void transferToHome(boolean isClear) {

        Utils.dismissProgressDialog();

        if (isClear) {
            return;
        }

        if(strFrom != null && strFrom.equals("Keyword")){
            Intent i = new Intent(FilterCategoriesActivity.this, KeywordsMainFragment.class);
            i.putExtra("totalCount", totalCount);
            i.putExtra("strBookIds", strBookIds);
            FilterCategoriesActivity.this.setResult(Activity.RESULT_OK, i);
            FilterCategoriesActivity.this.finish();
        }else {
            Intent i = new Intent(FilterCategoriesActivity.this, YearFragment.class);
            i.putExtra("totalCount", totalCount);
            i.putExtra("strBookIds", strBookIds);
            FilterCategoriesActivity.this.setResult(Activity.RESULT_OK, i);
            FilterCategoriesActivity.this.finish();
        }


    }
}
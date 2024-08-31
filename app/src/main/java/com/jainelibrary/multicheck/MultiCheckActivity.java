package com.jainelibrary.multicheck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.fragment.KeywordsMainFragment;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MultiCheckActivity extends AppCompatActivity {

    private MultiCheckGenreAdapter adapter;
    private Button button_cancel, button_apply;
    private ArrayList<String> bookIdList = new ArrayList<>();
    private ArrayList<CategoryResModel.CategoryModel> mCatList = new ArrayList<>();
    private String strUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_check);
        setHeader();
        loadUiElements();
        onEventListeners();

   /*Button clear = (Button) findViewById(R.id.clear_button);
    clear.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        adapter.clearChoices();
      }
    });

    Button check = (Button) findViewById(R.id.check_first_child);
    check.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        adapter.checkChild(true, 0, 3);
      }
    });*/
    }

    int totalCount = 0;

    private void onEventListeners() {
        button_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        button_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> bookIdList = new ArrayList<>();
                String strBooIds = "";
                List<MultiCheckGenre> parentBookList = adapter.getParentBookList();
                if (parentBookList != null && parentBookList.size() > 0) {
                    for (int i = 0; i < parentBookList.size(); i++) {
                        MultiCheckGenre multiCheckGenre = parentBookList.get(i);
                        for (int j = 0; j < multiCheckGenre.getItems().size(); j++) {
                            Book mBook = (Book) multiCheckGenre.getItems().get(j);
                            if (mBook.isSelected()) {
                                bookIdList.add(mBook.getId());
                                if (strBooIds != null && strBooIds.length() > 0) {
                                    strBooIds = strBooIds + "," + mBook.getId();
                                } else {
                                    strBooIds = mBook.getId();
                                }
                                Log.e("strBooIds---", "strBooIds--" + strBooIds);
                            }
                        }
                    }

                    SharedPrefManager.getInstance(MultiCheckActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, bookIdList);
                    totalCount = bookIdList.size();
                    /*if (bookIdList != null && bookIdList.size() > 0) {
                        SharedPrefManager.getInstance(MultiCheckActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, bookIdList);
                    } else {
                        Log.e("bookIdList---", "null--");
                    }*/

                   /* Intent i = new Intent();
                    i.putExtra("totalCount",totalCount);
                    setResult(RESULT_OK,i);*/
                    Intent i = new Intent(MultiCheckActivity.this, KeywordsMainFragment.class);
                    i.putExtra("totalCount", totalCount);
                    i.putExtra("strBooIds", strBooIds);
                    setResult(RESULT_OK, i);
                    finish();
                 /*  startActivity(new Intent(MultiCheckActivity.this, KeywordSearchActivity.class).putExtra("totalCount",totalCount));
                    MultiCheckActivity.this.finish();*/
                }
            }
        });
    }

    private void loadUiElements() {
        button_cancel = findViewById(R.id.button_cancel);
        button_apply = findViewById(R.id.button_apply);
        callFilterListApi();
    }

    public void callFilterListApi() {
        String kid = getIntent().getExtras().getString("KID");
        Log.e("kid", kid);
        if (!ConnectionManager.checkInternetConnection(MultiCheckActivity.this)) {
            Utils.showInfoDialog(MultiCheckActivity.this, "Please check internet connection");
            return;
        }
        strUid = SharedPrefManager.getInstance(MultiCheckActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);

        Utils.showProgressDialog(MultiCheckActivity.this, "Please Wait...", false);
        ApiClient.getCategory("0",kid, strUid, new Callback<CategoryResModel>() {
            @Override
            public void onResponse(Call<CategoryResModel> call, retrofit2.Response<CategoryResModel> response) {
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        mCatList = new ArrayList<>();
                        mCatList = response.body().getData();
                        if (mCatList != null && mCatList.size() > 0) {
                            setAdapter(mCatList);
                        }
                    } else {
                        Utils.showInfoDialog(MultiCheckActivity.this, "" + response.body().getMessage());
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

    private void setAdapter(ArrayList<CategoryResModel.CategoryModel> mCatList) {
        bookIdList = SharedPrefManager.getInstance(MultiCheckActivity.this).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS);
        if (bookIdList == null || bookIdList.size() == 0) {
            bookIdList = new ArrayList<>();
        }
        int count = 0;
        for (int i = 0; i < mCatList.size(); i++) {
            for (int j = 0; j < mCatList.get(i).getBooks().size(); j++) {
                count++;
            }
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new MultiCheckGenreAdapter(true,GenreDataFactory.makeMultiCheckGenres(mCatList), bookIdList, count,MultiCheckActivity.this, new MultiCheckGenreAdapter.OnZoomListener() {
            public void OnZoomClick(String strImageUrl, String fallbackImage) {
                Intent i = new Intent(MultiCheckActivity.this, ZoomImageActivity.class);
                i.putExtra("image", strImageUrl);
                i.putExtra("fallbackImage", fallbackImage);

                i.putExtra("url", true);
                startActivity(i);
            }
        });
        Log.e("gen" + GenreDataFactory.makeMultiCheckGenres(mCatList).size() + "", bookIdList.size() + "");
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


    }
          /*    @Override
            public void onImageClick(int child, String s) {

                Log.e("String", s);
                SharedPrefManager.getInstance(getApplicationContext()).saveStringPref(SharedPrefManager.IMG_URL, s);
                s = SharedPrefManager.getInstance(getApplicationContext()).getStringPref(SharedPrefManager.IMG_URL);
                Log.e("String", s);
                if ((s != null) && (s.length() != 0)) {
                    Intent i = new Intent(getApplicationContext(), ZoomImageActivity.class);
                    i.putExtra("image", s);
                    i.putExtra("url", true);
                    startActivity(i);
                }


            }*/


    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        ImageView ivDelete = headerView.findViewById(R.id.ivDelete);
        TextView tvKey = headerView.findViewById(R.id.tvKey);
        tvKey.setVisibility(View.VISIBLE);
        ivDelete.setVisibility(View.GONE);
        tvKey.setText("Clear");
        tvKey.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(MultiCheckActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());
                if (mCatList != null && mCatList.size() > 0) {
                    setAdapter(mCatList);
                }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader();
        loadUiElements();
        onEventListeners();
        if ((mCatList != null) && (mCatList.size() != 0)) {
            adapter.notifyDataSetChanged();
        }
    }
}

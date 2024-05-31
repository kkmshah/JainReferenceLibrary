package com.jainelibrary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jainelibrary.R;
import com.jainelibrary.adapter.IndexSearchAdapter;
import com.jainelibrary.adapter.IndexSearchDetailsListAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.IndexSearchResModel;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class BookIndexActivity extends AppCompatActivity implements IndexSearchAdapter.SearchInterfaceListener{

    private RecyclerView rvIndexList;
    private LinearLayout llAddItem;
    private ImageView ivHeaderIcon;
    private TextView tvHeaderCount;
    public static ArrayList<IndexSearchResModel.IndexResModel> bookIndexModel = new ArrayList<>();
    private String strBookName, strBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_index);

        Intent i = getIntent();
        strBookName = i.getStringExtra("IndexBookName");
        strBookId = i.getStringExtra("IndexBookId");

        setHeader();
        loadUiElements();

        if(strBookId != null && strBookId.length() > 0){
            //callBookIndexApi(strBookId);
        }
    }

    private void loadUiElements() {
        rvIndexList = findViewById(R.id.rvIndexList);
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        llAddItem = headerView.findViewById(R.id.llAddItem);
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        ivHeaderIcon.setVisibility(View.INVISIBLE);
        tvHeaderCount.setVisibility(View.INVISIBLE);

        if(strBookName != null && strBookName.length() > 0){
            Log.e("strBookName--", "index--" + strBookName);
            tvPageName.setText(""+strBookName);
        }

        ivHeaderIcon.setImageResource(R.mipmap.book);
        llAddItem.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
       /* Intent intent = getIntent();
        if (intent.getExtras() != null) {
            strKeyName = intent.getExtras().getString("keywordName");
            strKeyId = intent.getExtras().getString("keywordId");
            strBookIds = intent.getExtras().getString("bookIds");
            tvPageName.setText(strKeyName);
        }
        if (strKeyId != null && strKeyId.length() > 0) {
            callKeywordBookApi();
        }*/
    }

   /* private void callBookIndexApi(String strBookId) {
        if (!ConnectionManager.checkInternetConnection(BookIndexActivity.this)) {
            Toast.makeText(BookIndexActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        Utils.showProgressDialog(BookIndexActivity.this, "Please Wait...", false);
        ApiClient.getBookIndex(strBookId, new Callback<IndexSearchResModel>() {
            @Override
            public void onResponse(Call<IndexSearchResModel> call, retrofit2.Response<IndexSearchResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    *//*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*//*

                    if (response.body().isStatus()) {
                        bookIndexModel = response.body().getData();
                        if (bookIndexModel != null && bookIndexModel.size() > 0) {
                            setBookIndexData(bookIndexModel);
                        }
                    } else {
                        tvHeaderCount.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<IndexSearchResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }*/

    private void setBookIndexData(ArrayList<IndexSearchResModel.IndexResModel> bookIndexModel) {
        Log.e("setdata", "");
        if (bookIndexModel == null || bookIndexModel.size() == 0) {
            ivHeaderIcon.setVisibility(View.INVISIBLE);
            tvHeaderCount.setVisibility(View.INVISIBLE);
            return;
        }
        rvIndexList.setHasFixedSize(true);
        rvIndexList.setLayoutManager(new LinearLayoutManager(BookIndexActivity.this));
        rvIndexList.setVisibility(View.VISIBLE);
        IndexSearchAdapter mSearchListAdapter = new IndexSearchAdapter(BookIndexActivity.this, bookIndexModel, this);
        rvIndexList.setAdapter(mSearchListAdapter);


    }

    @Override
    public void onDetailsClick(IndexSearchResModel.IndexResModel indexResModel, int position) {
        String strName = indexResModel.getName();
        String strId = indexResModel.getId();
        String strIndexId = indexResModel.getId();
        Log.e("strId--", "index--" + strId);
        Intent i = new Intent(BookIndexActivity.this, IndexSearchDetailsActivity.class);
        i.putExtra("indexName", strName);
        i.putExtra("indexId", strIndexId);
        startActivity(i);
    }

    @Override
    public void onContactSelected(IndexSearchResModel.IndexResModel mIndexKeywordModel) {

    }
}
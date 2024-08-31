package com.jainelibrary.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.jainelibrary.R;
import com.jainelibrary.activity.IndexSearchDetailsActivity;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.adapter.IndexBooksAdapter;
import com.jainelibrary.adapter.IndexSearchAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.IndexSearchResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class IndexFragment extends Fragment implements IndexSearchAdapter.SearchInterfaceListener, IndexBooksAdapter.OnBookClickListeners {

    private final String TAG = "IndexFragment";
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
    private RecyclerView rvList;
    private IndexBooksAdapter mSearchListAdapter;
    private TextView tvNoRecord;
    View view1,view2;
    AppBarLayout appBarLayout;
    private ArrayList<BookListResModel.BookDetailsModel> bookDetailsModels = new ArrayList<>();
    private ArrayList<BookListResModel.BookDetailsModel> tempBookDetailsModels = new ArrayList<>();

    public IndexFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        loadUiElements(view);
        declaration(view);
        setHeader(view);
        return view;
    }
    private void loadUiElements(View view) {
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
    }

    private void declaration(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        callIndexBookApi();
        //callIndexKeywordSearchApi("", "1");
        etSearchView.requestFocus();
        etSearchView.setShowSoftInputOnFocus(false);

        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        // Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);

        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null,ivSend);
                Util.hideKeyBoard(getActivity(), etSearchView);
                Utils.showDefaultKeyboardDialog(getActivity());
            }
        });
        etSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Util.hideKeyBoard(getActivity(), etSearchView);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null,ivSend);
                //   setSearchHistoryKeywordData();
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
                    llFilter.setVisibility(View.VISIBLE);
                    llClose.setVisibility(View.GONE);
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
                strSearchtext = null;
                etSearchView.getText().clear();
                llFilter.setVisibility(View.VISIBLE);
                llClose.setVisibility(View.GONE);
                mKeyboardView.setVisibility(View.GONE);
                if (tempBookDetailsModels != null && tempBookDetailsModels.size() > 0) {
                    setBookIndexData(tempBookDetailsModels);
                    rvList.setVisibility(View.VISIBLE);
                    tvNoRecord.setVisibility(View.GONE);
                } else {
                    rvList.setVisibility(View.GONE);
                    tvNoRecord.setVisibility(View.VISIBLE);
                }
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strValue = etSearchView.getText().toString();
                Log.e("index send click", strValue);
                if (strValue != null && strValue.length() > 0) {
                    //callIndexKeywordSearchApi(strValue, "1");
                    mKeyboardView.setVisibility(View.GONE);
                    filter(strValue);
                } else {
                    Utils.showInfoDialog(getActivity(), "Please enter value in search box");
                }
            }
        });
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<BookListResModel.BookDetailsModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (int i = 0; i < bookDetailsModels.size(); i++) {
            //if the existing elements contains the search input
            if (bookDetailsModels.get(i).getIndex_name().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(bookDetailsModels.get(i));
            }
        }

        //calling a method of the adapter class and passing the filtered list
        if (mSearchListAdapter != null)
            mSearchListAdapter.filterList(filterdNames);

    }
    @Override
    public void onResume() {
        super.onResume();

    }
    private void setHeader(View view) {
        appBarLayout= getActivity().findViewById(R.id.appbar);
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
        tvPageName.setText("Index Search");
    }
    @Override
    public void onDetailsClick(IndexSearchResModel.IndexResModel indexResModel, int position) {
        String strName = indexResModel.getName();
        String strId = indexResModel.getId();
        String strIndexId = indexResModel.getId();
        Log.e("strId--", "index--" + strId);
        Log.e("strName--", "strName--" + strName);
        Intent i = new Intent(getActivity(), IndexSearchDetailsActivity.class);
        i.putExtra("indexName", strName);
        i.putExtra("indexId", strIndexId);
        startActivity(i);
    }
    @Override
    public void onContactSelected(IndexSearchResModel.IndexResModel mIndexKeywordModel) {
    }

    private void callIndexBookApi() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getIndexBookList(new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        bookDetailsModels = response.body().getData();
                        tempBookDetailsModels = bookDetailsModels;
                        if (bookDetailsModels != null && bookDetailsModels.size() > 0) {
                            setBookIndexData(bookDetailsModels);
                        }
                    } else {
                        //tvHeaderCount.setVisibility(View.VISIBLE);
                        Utils.showInfoDialog(getActivity(), response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<BookListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void setBookIndexData(ArrayList<BookListResModel.BookDetailsModel> bookDetailsModels) {
        Log.e("setdata", "");
        /*if (bookIndexModel == null || bookIndexModel.size() == 0) {
            ivHeaderIcon.setVisibility(View.INVISIBLE);
            tvHeaderCount.setVisibility(View.INVISIBLE);
            return;
        }*/
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setVisibility(View.VISIBLE);
        mSearchListAdapter = new IndexBooksAdapter(getActivity(), bookDetailsModels, this);
        rvList.setAdapter(mSearchListAdapter);


    }

    @Override
    public void onImageClick(ArrayList<BookListResModel.BookDetailsModel> indexBookList, int position, ImageView ivMenu) {
        String strImageUrl = indexBookList.get(position).getBook_large_image();
        String fallbackImage = indexBookList.get(position).getBook_image();

        Log.e("strImageUrl--", "index--" + strImageUrl);
        Intent i = new Intent(getActivity(), ZoomImageActivity.class);
        i.putExtra("image", strImageUrl);
        i.putExtra("fallbackImage", fallbackImage);

        i.putExtra("url", true);
        startActivity(i);
    }

    @Override
    public void onBookClick(ArrayList<BookListResModel.BookDetailsModel> indexBookList, int position, ImageView ivMenu) {
        String strName = indexBookList.get(position).getBook_name();
        String strBookId = indexBookList.get(position).getBook_id();
        String strIndexId = indexBookList.get(position).getIndex_book_id();
        String strIndexName = indexBookList.get(position).getIndex_name();
        Log.e("strId--", "index--" + strBookId);
        Log.e("strName--", "index--" + strName);
        Log.e("strIndexId--", "index--" + strIndexId);
        Intent i = new Intent(getActivity(), IndexSearchDetailsActivity.class);
        i.putExtra("IndexBookName", strName);
        i.putExtra("IndexBookId", strIndexId);
        i.putExtra("BookId", strBookId);
        i.putExtra("IndexName", strIndexName);
        startActivity(i);
    }
}
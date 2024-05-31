package com.jainelibrary.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.adapter.PdfStoreListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class PdfStoreActivity extends AppCompatActivity  implements PdfStoreListAdapter.PdfStoreInterfaceListener {
    private static final int PDF_CODE = 1;
    private RecyclerView rvPdfStore;
    String[] spinnerA = {"Name wise", "Author wise", "Publisher wise"};
    String[] spinnerB = {"All Books", "Dictionary", "History", "Tatvagyan"};

    Spinner spinnerList, spinnerBooks;
    String strUId, strFlag = "3";
    private String strType = "0", strCatId = "0",strSearch;
    private PdfStoreListAdapter mPdfAdadpter;
    TextView tvNoDataFound;
    private LinearLayout llAddItem;
    private TextView tvHeaderCount;
    private ImageView ivHeaderIcon;

    EditText etSearchView;
    private String strLanguage;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend, ivShare;
    private String strSearchtext;
    LinearLayout llFilter, llClose;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pdf_store);
        loadUiElements();
        declaration();
        setHeader();
    }

    private void loadUiElements() {
        rvPdfStore = findViewById(R.id.rvPdfStore);
        spinnerList = findViewById(R.id.spinnerList);
        spinnerBooks = findViewById(R.id.spinnerBooks);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);
        etSearchView = findViewById(R.id.etSearchView);
        ivKeyboard = findViewById(R.id.ivKeyboard);
        mKeyboardView = findViewById(R.id.keyboardView);
        ivClose = findViewById(R.id.ivClose);
        ivSend = findViewById(R.id.ivSend);
        llClose = findViewById(R.id.llClose);
        llFilter = findViewById(R.id.llFilter);

    }

    private void declaration() {

        ArrayAdapter arrayAdapter = new ArrayAdapter(PdfStoreActivity.this, android.R.layout.simple_spinner_item, spinnerA);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.setAdapter(arrayAdapter);

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(PdfStoreActivity.this, android.R.layout.simple_spinner_item, spinnerB);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBooks.setAdapter(arrayAdapter1);
        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String strSpinnerA = spinnerA[position];

                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Name wise")) {
                    strType = "0";
                }
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Author wise")) {
                    strType = "1";
                }
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Publisher wise")) {
                    strType = "2";
                }
                strSearch = etSearchView.getText().toString();
                getPdfList(strSearch,strCatId, strType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String strSpinnerB = spinnerB[position];
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("All Books")) {
                    strCatId = "0";
                }
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("Dictionary")) {
                    strCatId = "1";
                }
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("History")) {
                    strCatId = "2";
                }
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("Tatvagyan")) {
                    strCatId = "3";
                }
                strSearch = etSearchView.getText().toString();
                getPdfList(strSearch,strCatId, strType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
            strUId = SharedPrefManager.getInstance(PdfStoreActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
            if (strUId != null && strUId.length() > 0) {
                strSearch = etSearchView.getText().toString();
                getPdfList(strSearch,strCatId, strType);
            }
        strLanguage = SharedPrefManager.getInstance(PdfStoreActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Util.hideKeyBoard(PdfStoreActivity.this, etSearchView);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, PdfStoreActivity.this, strLanguage, null,ivSend);*/
                Utils.showDefaultKeyboardDialog(PdfStoreActivity.this);
            }
        });
        etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(PdfStoreActivity.this, etSearchView);
                strLanguage = SharedPrefManager.getInstance(PdfStoreActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, PdfStoreActivity.this, strLanguage, null,ivSend);
                //   setSearchHistoryKeywordData();
                return false;
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
                    ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            strSearchtext = null;
                            etSearchView.getText().clear();
                            llFilter.setVisibility(View.VISIBLE);
                            rvPdfStore.setVisibility(View.GONE);
                            llClose.setVisibility(View.GONE);
                        }
                    });
                    if (strSearchtext != null && strSearchtext.length() > 0) {
                        llFilter.setVisibility(View.GONE);
                        llClose.setVisibility(View.VISIBLE);
                    } else {
                        llClose.setVisibility(View.GONE);
                        llFilter.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strValue = etSearchView.getText().toString();
                if (strValue != null && strValue.length() > 0) {
                    if (mKeyboardView.getVisibility() == View.VISIBLE) {
                        mKeyboardView.setVisibility(View.GONE);
                    }
                    getPdfList(strValue,strCatId,strType);
                } else {
                    Utils.showInfoDialog(PdfStoreActivity.this, "Please enter value in search box");
                }
            }
        });

    }
    private void askLogin() {
        Utils.showLoginDialogForResult(PdfStoreActivity.this, PDF_CODE);
    }

    private void setBookData(ArrayList<PdfStoreListResModel.PdfListModel> bookDetailList) {
        if (bookDetailList == null || bookDetailList.size() == 0) {
            return;
        }
        if (bookDetailList != null && bookDetailList.size() > 0) {
            ivHeaderIcon.setVisibility(View.VISIBLE);
            tvHeaderCount.setVisibility(View.VISIBLE);
            tvHeaderCount.setText("" + bookDetailList.size());
        } else {
            llAddItem.setVisibility(View.GONE);
        }
        rvPdfStore.setHasFixedSize(true);
        rvPdfStore.setLayoutManager(new LinearLayoutManager(PdfStoreActivity.this));
        rvPdfStore.setVisibility(View.VISIBLE);
        mPdfAdadpter = new PdfStoreListAdapter(PdfStoreActivity.this, this, bookDetailList);
        rvPdfStore.setAdapter(mPdfAdadpter);
    }

    private void setHeader() {

        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        TextView tvPgCount = headerView.findViewById(R.id.tvPgCount);
        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        ivHeaderIcon.setVisibility(View.INVISIBLE);
        tvHeaderCount.setVisibility(View.INVISIBLE);
        ivHeaderIcon.setImageResource(R.mipmap.pdf_store);
        llAddItem = headerView.findViewById(R.id.llAddItem);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PdfStoreActivity.this.onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Pdf Store");
    }


    public void getPdfList(String strSearch,String strCatId, String strType) {
        if (!ConnectionManager.checkInternetConnection(PdfStoreActivity.this)) {
            Utils.showInfoDialog(PdfStoreActivity.this, "Please check internet connection");
            return;
        }
        String strUserId = SharedPrefManager.getInstance(getApplicationContext()).getStringPref(SharedPrefManager.KEY_USER_ID);

        Utils.showProgressDialog(PdfStoreActivity.this, "Please Wait...", false);
        ApiClient.getPdfList(strUserId, strSearch,strCatId, strType, "1", new Callback<PdfStoreListResModel>() {
            @Override
            public void onResponse(Call<PdfStoreListResModel> call, retrofit2.Response<PdfStoreListResModel> response) {
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.dismissProgressDialog();
                        ArrayList<PdfStoreListResModel.PdfListModel> data = new ArrayList<>();
                        data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            rvPdfStore.setVisibility(View.VISIBLE);
                            tvNoDataFound.setVisibility(View.GONE);
                            setBookData(data);
                        } else {
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            rvPdfStore.setVisibility(View.GONE);
                        }
                    } else {
                        Utils.dismissProgressDialog();
                        tvNoDataFound.setVisibility(View.VISIBLE);
                        rvPdfStore.setVisibility(View.GONE);
                    }
                } else {
                    Utils.dismissProgressDialog();
                }
            }
            @Override
            public void onFailure(Call<PdfStoreListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onImageClick(ArrayList<PdfStoreListResModel.PdfListModel> pdfBookList, int position) {
        String strBookId = pdfBookList.get(position).getBook_id();
        String strBookName = pdfBookList.get(position).getBook_name();
        String strPdfPageNo = pdfBookList.get(position).getPdf_page_no();
        String strEditorName = pdfBookList.get(position).getEditor_name();
        String strPublisherName = pdfBookList.get(position).getPublisher_name();
        String strTranslator = pdfBookList.get(position).getTranslator();
        BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
        mBookDataModels.setKeyword(strBookName);
        mBookDataModels.setBook_id(strBookId);
        mBookDataModels.setBook_name(strBookName);
        mBookDataModels.setPdf_page_no(strPdfPageNo);
        mBookDataModels.setEditor_name(strEditorName);
        mBookDataModels.setPublisher_name(strPublisherName);
        mBookDataModels.setTranslator(strTranslator);
        mBookDataModels.setFlag(strFlag);
        Intent i = new Intent(PdfStoreActivity.this, BookDetailsActivity.class);
        i.putExtra("model", mBookDataModels);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}

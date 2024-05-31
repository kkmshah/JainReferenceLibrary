package com.jainelibrary.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.activity.BookDetailsActivity;
import com.jainelibrary.activity.HomeActivity;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.MainActivity;
import com.jainelibrary.adapter.PdfStoreListAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class PdfStoreFragment extends Fragment implements PdfStoreListAdapter.PdfStoreInterfaceListener {

    private static final int PDF_CODE = 1;
    private RecyclerView rvPdfStore;
    String[] spinnerA = {"Name wise", "Author wise", "Publisher wise"};
    String[] spinnerB = {"All Books", "Dictionary", "History", "Tatvagyan"};
    Spinner spinnerList, spinnerBooks;
    String strUId, strFlag = "3";
    private String strType = "0", strCatId = "0";
    private PdfStoreListAdapter mPdfAdadpter;
    TextView tvNoDataFound;
    private LinearLayout llAddItem;
    private TextView tvHeaderCount;
    private ImageView ivHeaderIcon;
    public PdfStoreFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_store, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().hide();
        loadUiElements(view);
        declaration(view);
        setHeader(view);
        return view;
    }
    private void loadUiElements(View view) {
        rvPdfStore = view.findViewById(R.id.rvPdfStore);
        spinnerList = view.findViewById(R.id.spinnerList);
        spinnerBooks = view.findViewById(R.id.spinnerBooks);
        tvNoDataFound = view.findViewById(R.id.tvNoDataFound);
    }
    private void declaration(View view) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerA);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.setAdapter(arrayAdapter);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerB);
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

                getPdfList(strCatId, strType);
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

                getPdfList(strCatId, strType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            if (strUId != null && strUId.length() > 0) {
                getPdfList(strCatId, strType);
                // callMyShelfListApi(strUId, strFlag);
            }
        } else {
            askLogin();
        }
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), PDF_CODE);
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
        rvPdfStore.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPdfStore.setVisibility(View.VISIBLE);
        mPdfAdadpter = new PdfStoreListAdapter(getActivity(), this, bookDetailList);
        rvPdfStore.setAdapter(mPdfAdadpter);
    }

    private void setHeader(View view) {

        View headerView = view.findViewById(R.id.header);
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
                getActivity().onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Pdf Store");
    }


    public void getPdfList(String strCatId, String strType) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        String strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getPdfList(strUserId,"",strCatId, strType, "1", new Callback<PdfStoreListResModel>() {
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
        Intent i = new Intent(getActivity(), BookDetailsActivity.class);
        i.putExtra("strBookId", strBookId);
        i.putExtra("strFlag", strFlag);
        startActivity(i);
    }
}
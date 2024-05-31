package com.jainelibrary.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jainelibrary.R;
import com.jainelibrary.activity.HomeActivity;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.adapter.MyReferenceFilesListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MyShelfFragment extends androidx.fragment.app.Fragment  {
/*

    private RecyclerView rvMyShelf;
    TextView tvNoRecord;

    private MyReferenceFilesListAdapter mAdapter;
    private ArrayList<MyShelfResModel> myShelfList = new ArrayList<>();

    String strUId, strFlag = "3";
    String[] spinner = {"recent", "sort by date", "sort by name"};
    Spinner spn;
    EditText etSearchView;
    private String strLanguage;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend, ivShare;
    private String strSearchtext,strUsername;
    LinearLayout llFilter, llClose;
    private TextView tvHeaderCount,tvUsername;
    private ImageView ivHeaderIcon;
    private LinearLayout llAddItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_shelf, container, false);
        ((HomeActivity) getActivity()).getSupportActionBar().hide();
        loadUiElements(view);
        declaration(view);
        setHeader(view);
        return view;
    }

    private void declaration(View view) {

        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);

        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
        }
        tvUsername.setVisibility(View.VISIBLE);
        if (tvUsername != null && tvUsername.length() > 0) {
            tvUsername.setText(strUsername);
        }

        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);

        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyBoard(getActivity(), etSearchView);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);
            }
        });

        etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(getActivity(), etSearchView);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);
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
                            rvMyShelf.setVisibility(View.GONE);
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
                    callSearchMyShelfApi(strValue);
                } else {
                    Toast.makeText(getActivity(), "Please enter value in search box", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spn.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinner);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(aa);

        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            if (strUId != null && strUId.length() > 0) {
                callMyShelfListApi(strUId, strFlag);
            }
        } else {
            askLogin();
        }

    }

    private void askLogin() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent intent = new Intent(getActivity(), LoginWithPasswordActivity.class);
                        intent.putExtra("isReference", false);
                        intent.putExtra("isKeywordSearch", false);
                        intent.putExtra("isBookSearch", false);
                        intent.putExtra("isMyShelf", true);
                        startActivityForResult(intent, 0000);
                        break;

                   */
/* case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;*//*

                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(android.text.Html.fromHtml("<font color='#1565C0'>Please login to use this functionality.</font>")).setPositiveButton("Login", dialogClickListener)
                .setCancelable(false).show();
    }

    public void callMyShelfListApi(String strUId, String strFlag) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getMyShelfList(strUId, strFlag,"", new Callback<MyShelfResModel>() {
            @Override
            public void onResponse(Call<MyShelfResModel> call, retrofit2.Response<MyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        ArrayList<MyShelfResModel.MyShelfModel> myShelfResModels = new ArrayList<>();
                        myShelfResModels = response.body().getData();
                        if (myShelfResModels != null && myShelfResModels.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            setMyShelfList(myShelfResModels);
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                            rvMyShelf.setVisibility(View.GONE);
                        }
                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        rvMyShelf.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public void callSearchMyShelfApi(String strKeyword) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getSearchMyShelf(strUId, strKeyword, strFlag, new Callback<MyShelfResModel>() {
            @Override
            public void onResponse(Call<MyShelfResModel> call, retrofit2.Response<MyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        ArrayList<MyShelfResModel.MyShelfModel> myShelfResModels = new ArrayList<>();
                        myShelfResModels = response.body().getData();
                        if (myShelfResModels != null && myShelfResModels.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            setMyShelfList(myShelfResModels);
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                            rvMyShelf.setVisibility(View.GONE);
                        }
                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        rvMyShelf.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void setHeader(View view) {

        View headerView = view.findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        TextView tvPgCount = headerView.findViewById(R.id.tvPgCount);
        llAddItem = headerView.findViewById(R.id.llAddItem);

        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        ivHeaderIcon.setVisibility(View.INVISIBLE);
        tvHeaderCount.setVisibility(View.INVISIBLE);
        ivHeaderIcon.setImageResource(R.mipmap.shelf);
        llAddItem.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("My References");
    }

    private void setMyShelfList(ArrayList<MyShelfResModel.MyShelfModel> myShelfList) {
        if (myShelfList == null || myShelfList.size() == 0) {
            return;
        }
        if (myShelfList != null && myShelfList.size() > 0) {
            ivHeaderIcon.setVisibility(View.VISIBLE);
            tvHeaderCount.setVisibility(View.VISIBLE);
            tvHeaderCount.setText("" + myShelfList.size());
        } else {
            llAddItem.setVisibility(View.GONE);
        }


        rvMyShelf.setVisibility(View.VISIBLE);

        rvMyShelf.setHasFixedSize(true);
        rvMyShelf.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMyShelf.setVisibility(View.VISIBLE);
        mAdapter = new MyReferenceFilesListAdapter(getActivity(), myShelfList, this);
        rvMyShelf.setAdapter(mAdapter);
    }

    private void loadUiElements(View view) {
        rvMyShelf = view.findViewById(R.id.rvMyShelf);
        tvNoRecord = view.findViewById(R.id.tvNoRecord);
        spn = view.findViewById(R.id.spinner);
        etSearchView = view.findViewById(R.id.etSearchView);
        ivKeyboard = view.findViewById(R.id.ivKeyboard);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        ivClose = view.findViewById(R.id.ivClose);
        ivSend = view.findViewById(R.id.ivSend);
        llClose = view.findViewById(R.id.llClose);
        llFilter = view.findViewById(R.id.llFilter);
        tvUsername = view.findViewById(R.id.tvUsername);


    }

    @Override
    public void onMenuClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position, ImageView ivMenu) {

    }

    @Override
    public void onDetailsClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position) {
        String strType = searchList.get(position).getType();
        if (strType != null && strType.equalsIgnoreCase("0") || strType.equalsIgnoreCase("1") || strType.equalsIgnoreCase("2")) {
            MyShelfResModel.MyShelfModel myShelfResModel = new MyShelfResModel.MyShelfModel();
            myShelfResModel = searchList.get(position);
            Intent i = new Intent(getActivity(), MyReferenceDetailsActivity.class);
            i.putExtra("myShelfModel", myShelfResModel);
            startActivity(i);
        }else{

        }
    }

    @Override
    public void onNotesClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position) {
    }

    @Override
    public void onOptionClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position) {

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        String strSpinner = spinner[position];
        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (strSpinner != null && strSpinner.equalsIgnoreCase("recent")) {
            strFlag = "3";
            if (isLogin) {
                callMyShelfListApi(strUId, strFlag);
            }
        }
        if (strSpinner != null && strSpinner.equalsIgnoreCase("sort by date")) {
            strFlag = "1";
            if (isLogin) {
                callMyShelfListApi(strUId, strFlag);
            }
        }
        if (strSpinner != null && strSpinner.equalsIgnoreCase("sort by name")) {
            strFlag = "2";
            if (isLogin) {
                callMyShelfListApi(strUId, strFlag);
            }
        }
        //Toast.makeText(getActivity(),spinner[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
*/

}


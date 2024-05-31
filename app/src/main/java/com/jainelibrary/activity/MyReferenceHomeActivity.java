package com.jainelibrary.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.gson.Gson;
import com.jainelibrary.R;
import com.jainelibrary.adapter.MyReferenceFilesListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.DeleteMyShelfResModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MyReferenceHomeActivity extends AppCompatActivity {
/*
    private static final int MY_SHELF_CODE = 1;
    private RecyclerView rvMyShelf;
    TextView tvNoRecord;

    private MyReferenceFilesListAdapter mAdapter;
    private ArrayList<MyShelfResModel> myShelfList = new ArrayList<>();

    String strUId, strFlag = "3";
    String[] spinner = {"Recent", "Sort by date", "Sort by name"};
    Spinner spn;
    EditText etSearchView;
    private String strLanguage;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend, ivShare;
    private String strSearchtext;
    LinearLayout llFilter, llClose, llShare, llDownload, llDelete, llNotes;
    String strKeyword, strTypeRef, strBookName, strType;
    private TextView tvHeaderCount, tvUsername;
    private ImageView ivHeaderIcon;
    private LinearLayout llAddItem;
    private String strUsername;
    private String strNote, strOptionId, strOptionPdfUrl, strBookNames;
    boolean isSelectedButNotPdf = false;
    boolean isReferenceSelected = false;
    private String PackageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_shelf);
        PackageName = MyReferenceHomeActivity.this.getPackageName();

        loadUiElements();
        declaration();
        setHeader();
        onBtnClickListenerEvent();
    }

    private void onBtnClickListenerEvent() {

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strOptionPdfUrl != null && strOptionPdfUrl.length() > 0) {
                    String shareText = strBookNames + "shared with you by " + strUsername + "\n" + "Download app from here :" + "\n" + "https://play.google.com/store/apps/details?id=" + PackageName;
                    share(shareText);
                } else {
                    if (isSelectedButNotPdf) {
                        Toast.makeText(MyReferenceHomeActivity.this, "This reference does not have pdf", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyReferenceHomeActivity.this, "Long press on reference and select reference", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strOptionPdfUrl != null && strOptionPdfUrl.length() > 0) {
                    downloadFile(strOptionPdfUrl, strBookNames);
                } else {
                    if (isSelectedButNotPdf) {
                        Toast.makeText(MyReferenceHomeActivity.this, "This reference does not have pdf", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyReferenceHomeActivity.this, "Long press on reference and select reference", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUId = SharedPrefManager.getInstance(MyReferenceHomeActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                if (strUId != null && strUId.length() > 0) {
                    if (isReferenceSelected) {
                        if (strKeyword != null && strKeyword.length() > 0) {
                            if (strType != null && strTypeRef != null) {
                                callDeleteMyShelfApi();
                            }
                        } else {
                            Log.e("strKeyword", "strKeyword not found");
                        }
                    } else {
                        Toast.makeText(MyReferenceHomeActivity.this, "Long press on reference and select reference", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    askLogin();
                }
            }
        });

        llNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReferenceSelected) {
                    if (strNote != null && strNote.length() > 0) {
                        Intent i = new Intent(MyReferenceHomeActivity.this, NotesActivity.class);
                        i.putExtra("strType", strType);
                        i.putExtra("strTypeRef", strTypeRef);
                        i.putExtra("strNotes", strNote);
                        i.putExtra("strKeyword", strKeyword);
                        i.putExtra("isMyShelf", true);
                        startActivity(i);
                    } else {
                        Toast.makeText(MyReferenceHomeActivity.this, "Not included notes ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyReferenceHomeActivity.this, "Long press on reference and select reference", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectOption(MyShelfResModel.MyShelfModel myShelfModel) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MyReferenceHomeActivity.this);
        builder.setTitle("Select Action");
// add a list
        String[] animals = {"Share", "Download", "Delete", "Notes", "Cancel"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0:
                        share("Download book from this link " + myShelfModel.getUrl());
                        break;
                    case 1:
                        downloadFile(myShelfModel.getUrl(), myShelfModel.getBook_name());
                        break;
                    case 2:
                        strUId = SharedPrefManager.getInstance(MyReferenceHomeActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        if (strUId != null && strUId.length() > 0) {
                            callDeleteMyShelfApi();
                        }
                        break;
                    case 3:
                        openNote(myShelfModel);
                        break;
                    case 4:
                        break;
                }
            }
        });
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void declaration() {
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(MyReferenceHomeActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);

        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
        }
        tvUsername.setVisibility(View.VISIBLE);
        if (tvUsername != null && tvUsername.length() > 0) {
            tvUsername.setText(strUsername);
        }

        strLanguage = SharedPrefManager.getInstance(MyReferenceHomeActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);

        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyBoard(MyReferenceHomeActivity.this, etSearchView);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, MyReferenceHomeActivity.this, strLanguage, null);
            }
        });

        etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Util.hideKeyBoard(MyReferenceHomeActivity.this, etSearchView);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, MyReferenceHomeActivity.this, strLanguage, null);
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
                    Toast.makeText(MyReferenceHomeActivity.this, "Please enter value in search box", Toast.LENGTH_SHORT).show();
                }
            }
        });
        spn.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(MyReferenceHomeActivity.this, android.R.layout.simple_spinner_item, spinner);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(aa);

    }

    private void askLogin() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent intent = new Intent(MyReferenceHomeActivity.this, LoginWithPasswordActivity.class);
                        intent.putExtra("isLoginId", Utils.Is_My_Shelf_Login_Id);
                        startActivityForResult(intent, MY_SHELF_CODE);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        onBackPressed();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MyReferenceHomeActivity.this);
        builder.setMessage(android.text.Html.fromHtml("<font color='#1565C0'>Please login to use this functionality.</font>")).setPositiveButton("Login", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setCancelable(false).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean isLogin = SharedPrefManager.getInstance(MyReferenceHomeActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUId = SharedPrefManager.getInstance(MyReferenceHomeActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
            if (strUId != null && strUId.length() > 0) {
                callMyShelfListApi(strUId, strFlag);
                clearData();
            }
        } else {
            askLogin();
        }
    }

    private void clearData() {
        strNote = "";
        strKeyword = "";
        strType = "";
        strTypeRef = "";
        strOptionId = "";
        strOptionPdfUrl = "";
        strBookNames = "";
        isSelectedButNotPdf = false;
        isReferenceSelected = false;
    }

    public void callMyShelfListApi(String strUId, String strFlag) {
        if (!ConnectionManager.checkInternetConnection(MyReferenceHomeActivity.this)) {
            Toast.makeText(MyReferenceHomeActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(MyReferenceHomeActivity.this, "Please Wait...", false);
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
        if (!ConnectionManager.checkInternetConnection(MyReferenceHomeActivity.this)) {
            Toast.makeText(MyReferenceHomeActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(MyReferenceHomeActivity.this, "Please Wait...", false);
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

    private void setHeader() {

        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        TextView tvPgCount = headerView.findViewById(R.id.tvPgCount);
        llAddItem = headerView.findViewById(R.id.llAddItem);

        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        ivHeaderIcon.setVisibility(View.INVISIBLE);
        tvHeaderCount.setVisibility(View.INVISIBLE);
        ivHeaderIcon.setImageResource(R.mipmap.my_reference);
        llAddItem.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyReferenceHomeActivity.this.onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("My References");


        ImageView ivSelectLogo = headerView.findViewById(R.id.ivSelectLogo);

        ImageView ivFirst = headerView.findViewById(R.id.ivFirst);
        ImageView ivSecond = headerView.findViewById(R.id.ivSecond);
        ImageView ivThird = headerView.findViewById(R.id.ivThird);
        ImageView ivFour = headerView.findViewById(R.id.ivFour);
        ImageView ivFive = headerView.findViewById(R.id.ivFive);
        ImageView ivSix = headerView.findViewById(R.id.ivSix);

        ivSelectLogo.setImageResource(R.mipmap.my_reference);

        ivFirst.setImageResource(R.mipmap.home);
        ivSecond.setImageResource(R.mipmap.search);
        ivThird.setImageResource(R.mipmap.book_store);
        ivFour.setImageResource(R.mipmap.user_guide);
        ivFive.setImageResource(R.mipmap.app_info);
        ivSix.setImageResource(R.mipmap.login);

        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyReferenceHomeActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyReferenceHomeActivity.this, SearchReferenceActivity.class);
                startActivity(i);
            }
        });

        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyReferenceHomeActivity.this, BookStoreActivity.class);
                startActivity(i);
            }
        });

        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyReferenceHomeActivity.this, UserGuideActivity.class);
                startActivity(i);
            }
        });

        ivFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyReferenceHomeActivity.this, AppInfoActivity.class);
                startActivity(i);
            }
        });

        ivSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyReferenceHomeActivity.this, LoginWithPasswordActivity.class);
                startActivity(i);
            }
        });
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
        rvMyShelf.setLayoutManager(new LinearLayoutManager(MyReferenceHomeActivity.this));
        rvMyShelf.setVisibility(View.VISIBLE);
        mAdapter = new MyReferenceFilesListAdapter(MyReferenceHomeActivity.this, myShelfList, this);
        rvMyShelf.setAdapter(mAdapter);
    }

    private void loadUiElements() {
        rvMyShelf = findViewById(R.id.rvMyShelf);
        tvNoRecord = findViewById(R.id.tvNoRecord);
        spn = findViewById(R.id.spinner);
        etSearchView = findViewById(R.id.etSearchView);
        ivKeyboard = findViewById(R.id.ivKeyboard);
        mKeyboardView = findViewById(R.id.keyboardView);
        ivClose = findViewById(R.id.ivClose);
        ivSend = findViewById(R.id.ivSend);
        llClose = findViewById(R.id.llClose);
        llFilter = findViewById(R.id.llFilter);
        tvUsername = findViewById(R.id.tvUsername);
        llShare = findViewById(R.id.llShare);
        llDownload = findViewById(R.id.llDownload);
        llDelete = findViewById(R.id.llDelete);
        llNotes = findViewById(R.id.llNotes);
    }

    @Override
    public void onMenuClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position, ImageView ivMenu) {

    }

    @Override
    public void onDetailsClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position) {

        strKeyword = searchList.get(position).getKeyword();
        strTypeRef = searchList.get(position).getType_ref();
        strBookName = searchList.get(position).getBook_name();
        strType = searchList.get(position).getType_ref();
        String strUrl = searchList.get(position).getUrl();
        if (strType != null && strType.length() > 0) {
            if (strType.equalsIgnoreCase("1")) {
                MyShelfResModel.MyShelfModel myShelfResModel = new MyShelfResModel.MyShelfModel();
                myShelfResModel = searchList.get(position);
                int strPage = myShelfResModel.getPage_no().size();
                if (strPage != 0 && strPage > 0) {
                    Intent i = new Intent(MyReferenceHomeActivity.this, MyReferenceDetailsActivity.class);
                    i.putExtra("myShelfModel", myShelfResModel);
                    startActivity(i);
                }
            } else {
                //selectOption(searchList.get(position));
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        String strSpinner = spinner[position];
        final String strValue = etSearchView.getText().toString();
        boolean isLogin = SharedPrefManager.getInstance(MyReferenceHomeActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (strSpinner != null && strSpinner.equalsIgnoreCase("Recent")) {
            strFlag = "3";
            if (isLogin) {
                if (strValue != null && strValue.length() > 0) {
                    callSearchMyShelfApi(strValue);
                } else {
                    callMyShelfListApi(strUId, strFlag);
                }
            }
        }
        if (strSpinner != null && strSpinner.equalsIgnoreCase("Sort by date")) {
            strFlag = "1";
            if (isLogin) {
                if (strValue != null && strValue.length() > 0) {
                    callSearchMyShelfApi(strValue);
                } else {
                    callMyShelfListApi(strUId, strFlag);
                }
            }
        }
        if (strSpinner != null && strSpinner.equalsIgnoreCase("Sort by name")) {
            strFlag = "2";
            if (isLogin) {
                if (strValue != null && strValue.length() > 0) {
                    callSearchMyShelfApi(strValue);
                } else {
                    callMyShelfListApi(strUId, strFlag);
                }
            }
        }
        //Toast.makeText(MyShelfActivity.this,spinner[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNotesClick(ArrayList<MyShelfResModel.MyShelfModel> notesDetailList,
                             int position) {
        //  openNote(notesDetailList.get(position));
    }

    @Override
    public void onOptionClick(ArrayList<MyShelfResModel.MyShelfModel> notesDetailList,
                              int position) {
        boolean isSelectItem = notesDetailList.get(position).isChecked();
        if (isSelectItem) {
            String strId = notesDetailList.get(position).getId();
            String strUrl = notesDetailList.get(position).getUrl();
            String strBookName = notesDetailList.get(position).getBook_name();
            String strKeywords = notesDetailList.get(position).getKeyword();
            String strTypes = notesDetailList.get(position).getType();
            String strTypeRefs = notesDetailList.get(position).getType_ref();
            String strNotes = notesDetailList.get(position).getNotes();

            isReferenceSelected = true;

            if (strNotes != null && strNotes.length() > 0) {
                strNote = strNotes;
            }
            if (strKeywords != null && strKeywords.length() > 0) {
                strKeyword = strKeywords;
            }
            if (strTypes != null && strTypes.length() > 0) {
                strType = strTypes;
            }

            if (strTypeRefs != null && strTypeRefs.length() > 0) {
                strTypeRef = strTypeRefs;
            }

            if (strId != null && strId.length() > 0) {
                strOptionId = strId;
            }

            if (strUrl != null && strUrl.length() > 0) {
                strOptionPdfUrl = strUrl;
            } else {
                isSelectedButNotPdf = true;
            }
            if (strBookName != null && strBookName.length() > 0) {
                strBookNames = strBookName;
            }
        } else {
            clearData();
        }
    }

    public void openNote(MyShelfResModel.MyShelfModel myShelfModel) {
        String stType = myShelfModel.getType();
        String strType_Ref = myShelfModel.getType_ref();
        String strNotes = myShelfModel.getNotes();
        String strKeyword = myShelfModel.getKeyword();

        Intent i = new Intent(MyReferenceHomeActivity.this, NotesActivity.class);
        i.putExtra("strType", stType);
        i.putExtra("strTypeRef", strType_Ref);
        i.putExtra("strNotes", strNotes);
        i.putExtra("strKeyword", strKeyword);
        startActivity(i);
    }
    public void getInfoDialogs(String strBookName, String strUrl, String strTite) {

        Dialog dialog = new IosDialog.Builder(this)
                .setTitle("Download Book")
                .setTitleColor(Color.RED)
                .setTitleSize(20)
                .setMessage(strTite)
                .setMessageColor(Color.parseColor("#D26A1B9A"))
                .setMessageSize(18)
                .setNegativeButtonColor(Color.parseColor("#D26A1B9A"))
                .setNegativeButtonSize(18)
                .setNegativeButton("No", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButtonColor(Color.parseColor("#D26A1B9A"))
                .setPositiveButtonSize(18)
                .setPositiveButton("Yes", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        if (strUrl != null && strUrl.length() > 0) {
                            downloadFile(strUrl, strBookName);
                        }
                    }
                }).build();
        dialog.show();
    }

    public void share(String shareData) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareData);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareData);
        startActivity(Intent.createChooser(sharingIntent, shareData));
    }

    public void downloadFile(String strUrl, String strBookName) {

        if (strUrl == null || strUrl.contains(".jpg")) {
            Toast.makeText(MyReferenceHomeActivity.this, "This reference does not have pdf", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utils.mediaStorageCommonPDFDir.exists()) {
            Utils.mediaStorageCommonPDFDir.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(strUrl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        String imagePath = "JRL_" + strBookName + System.currentTimeMillis();
        String customPath = "/JRL_/" + strBookName + System.currentTimeMillis();
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("JRL_" + strBookName + System.currentTimeMillis())
                .setDescription("Jrl_ " + strBookName + "_" + "Download")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, customPath + ".pdf");
        mgr.enqueue(request);
        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

    }

    private void callDeleteMyShelfApi() {
        Utils.showProgressDialog(MyReferenceHomeActivity.this, "Please Wait...", false);
        ApiClient.deleteMyShelf(strUId, strKeyword, strType, strTypeRef, new Callback<DeleteMyShelfResModel>() {
            @Override
            public void onResponse(Call<DeleteMyShelfResModel> call, retrofit2.Response<DeleteMyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Toast.makeText(MyReferenceHomeActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        callMyShelfListApi(strUId, strFlag);
                    } else {
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<DeleteMyShelfResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
*/
}

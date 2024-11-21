package com.jainelibrary.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.jainelibrary.R;

import com.jainelibrary.adapter.ShlokSearchAdapter;
import com.jainelibrary.fragment.ShlokFragment;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.KeywordModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.ShlokSearchResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ShlokSearchActivity extends AppCompatActivity implements ShlokSearchAdapter.SearchInterfaceListener, PopupMenu.OnMenuItemClickListener {
    private EditText etSearchView;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend, ivShare;
    RecyclerView rvList;
    TextView tvNoRecord;
    LinearLayout llFilter, llClose, llKeywordCount;
    private String strLanguage;
    String[] language = {"English", "Gujarati", "Hindi"};
    private ShlokSearchAdapter mSearchListAdapter;
    private String strSearchtext;
    private TextView buttonFilter;
    LinearLayout ll_buttons;
    private ArrayList<String> arrayListFirst;
    private ArrayList<String> arrayListSecond;
    private int selected;
    private ArrayList<KeywordModel> keywordList = new ArrayList<>();
    private String PackageName;
    private String strCatName, strBookCount;
    private TextView tvCategory, tvCount, tvBooks, tvKeywordCount;
    private ImageView ivNext;
    private int totalCount = 0;
    private String selectedString = "";
    private LinearLayout llAddItem;
    private TextView tvHeaderCount;
    private ImageView ivHeaderIcon;
    private String strUid = "";
    boolean isLogin = false;
    private ImageView ivExport;
    String TAG = "ShlokSearchActivity";
    private View headerView;
    AppBarLayout appBarLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shlok_serach);
        PackageName = ShlokSearchActivity.this.getPackageName();
        Log.e(TAG, "ABC");
        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = SearchActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }*/
        loadUiElements();
        setHeader();
        declaration();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("oar", "" + requestCode + " res " + resultCode);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            int totalCount = data.getExtras().getInt("totalCount");
            if (totalCount > 0) {
                tvCategory.setText("Selected Books");
                tvCount.setText("" + totalCount);
                ivNext.setVisibility(View.VISIBLE);
                tvCategory.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
            buttonFilter.setVisibility(View.VISIBLE);
            ll_buttons.setVisibility(View.VISIBLE);
        } else {
            Intent i = new Intent(ShlokSearchActivity.this, SearchReferenceActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    private void fillData() {
        arrayListFirst = new ArrayList<>();
        arrayListSecond = new ArrayList<>();
        arrayListFirst.add("तत्वार्थ");
        arrayListFirst.add("अपडेश माला");
        arrayListFirst.add("जीव विचार");
        arrayListFirst.add("नव तत्त्व");
        arrayListFirst.add("सिद्धहेमचन्द्रसब्दानुसाशन");
        arrayListFirst.add("अपडेश माला");
        arrayListFirst.add("तत्वार्थ");
        arrayListFirst.add("सिद्धहेमचन्द्रसब्दानुसाशन");
        arrayListFirst.add("जीव विचार");
        arrayListFirst.add("नव तत्त्व");
        arrayListSecond.add("सिद्धहेमचन्द्रसब्दानुसाशन");
        arrayListSecond.add("तत्वार्थ");
        arrayListSecond.add("अपडेश माला");
        arrayListSecond.add("जीव विचार");
        arrayListSecond.add("नव तत्त्व");

    }

    private void declaration() {
        Intent i = getIntent();
        if (i != null) {
            strSearchtext = i.getStringExtra("strSearchKeyword");
            if (strSearchtext != null && strSearchtext.length() > 0) {
                etSearchView.setText(strSearchtext);
            }
        }
        buttonFilter.setVisibility(View.GONE);
        ll_buttons.setVisibility(View.GONE);
        etSearchView.requestFocus();
        strLanguage = SharedPrefManager.getInstance(ShlokSearchActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        if (isLogin)
            if ((strUid != null) && (strUid.length() != 0)) {
                    callShlokKeywordSearchApi(strUid, "", "1");

            } else {
                callShlokKeywordSearchApi("0", "", "1");
            }

        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*buttonFilter.setVisibility(View.GONE);
                strLanguage = language[2];
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, ShlokSearchActivity.this, strLanguage, null,ivSend);*/
                Utils.showDefaultKeyboardDialog(ShlokSearchActivity.this);
            }
        });

        etSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Util.hideKeyBoard(ShlokSearchActivity.this, etSearchView);
                mKeyboardView.setVisibility(View.VISIBLE);
                strLanguage = SharedPrefManager.getInstance(ShlokSearchActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, ShlokSearchActivity.this, strLanguage, null,ivSend);
                buttonFilter.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.GONE);
                return false;
            }
        });

        etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonFilter.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonFilter.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.GONE);
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
                            rvList.setVisibility(View.GONE);
                            llKeywordCount.setVisibility(View.GONE);
                            llFilter.setVisibility(View.VISIBLE);
                            llClose.setVisibility(View.GONE);
                            /*ivHeaderIcon.setVisibility(View.INVISIBLE);
                            tvHeaderCount.setVisibility(View.INVISIBLE);*/
                        }
                    });

                    if (strSearchtext != null && strSearchtext.length() > 0) {
                        llClose.setVisibility(View.VISIBLE);
                        llFilter.setVisibility(View.GONE);
                        /* filter(strSearchtext);*/
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
                String strValue = etSearchView.getText().toString();

                if (strValue != null && strValue.length() > 0) {
                    if (mKeyboardView.getVisibility() == View.VISIBLE) {
                        mKeyboardView.setVisibility(View.GONE);
                    }

                    buttonFilter.setVisibility(View.VISIBLE);
                    ll_buttons.setVisibility(View.VISIBLE);
                    Log.e("strUid3", strUid);
                    if (isLogin) {
                        if ((strUid != null) && (strUid.length() != 0)) {
                            callShlokKeywordSearchApi(strUid, strValue, "1");
                        }
                    } else {

                        callShlokKeywordSearchApi("0", strValue, "1");
                    }

                } else {
                    Utils.showInfoDialog(ShlokSearchActivity.this, "Please enter any value in searchbox");
                }
            }
        });


        if (ShlokFragment.keywordShlokSearchModel != null && ShlokFragment.keywordShlokSearchModel.size() > 0) {
            setShlokKeywordData(ShlokFragment.keywordShlokSearchModel);
        }
    }

    private void loadData(String strValue, ArrayList<String> arrayList) {

        keywordList = new ArrayList<>();

        Log.e("", "search value " + strValue);
        for (int i = 0; i < arrayList.size(); i++) {
            KeywordModel keywordModel = new KeywordModel();
            keywordModel.setId("" + i);
            keywordModel.setName(arrayList.get(i));
            if (arrayList.get(i).equalsIgnoreCase("सिद्धहेमचन्द्रसब्दानुसाशन")) {
                keywordModel.setBookCount("30");
            } else {
                keywordModel.setBookCount(arrayList.get(i));
            }
            keywordList.add(keywordModel);
        }
        // setkeywordData(keywordList);
    }

    private void setShlokKeywordData(ArrayList<ShlokSearchResModel.ShlokResModel> keywordList) {
        if (keywordList == null || keywordList.size() == 0) {
            return;
        }
       /* if (keywordList != null && keywordList.size() > 0) {
            ivHeaderIcon.setVisibility(View.VISIBLE);
            tvHeaderCount.setVisibility(View.VISIBLE);
            tvHeaderCount.setText("" + keywordList.size());
        } else {
            llAddItem.setVisibility(View.GONE);
        }*/

        if (keywordList != null && keywordList.size() > 0) {
            tvKeywordCount.setVisibility(View.VISIBLE);
            llKeywordCount.setVisibility(View.VISIBLE);
            tvKeywordCount.setText("" + keywordList.size() + " Granth names");
        } else if (keywordList != null && keywordList.size() >= 1) {
            tvKeywordCount.setVisibility(View.VISIBLE);
            llKeywordCount.setVisibility(View.VISIBLE);
            tvKeywordCount.setText("" + keywordList.size() + "Granth Found");
        } else {
            llKeywordCount.setVisibility(View.GONE);
        }

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(ShlokSearchActivity.this));
        rvList.setVisibility(View.VISIBLE);
        mSearchListAdapter = new ShlokSearchAdapter(ShlokSearchActivity.this, keywordList, this);
        rvList.setAdapter(mSearchListAdapter);
        rvList.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                headerView.setTranslationY(-40);
                headerView.setVisibility(View.GONE);
                appBarLayout.setMinimumHeight(0);
            }

            @Override
            public void onShow() {
                headerView.setTranslationY(0);
                headerView.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }


        });
    }


    private void loadUiElements() {
        strUid = SharedPrefManager.getInstance(ShlokSearchActivity.this).getStringKeyword(SharedPrefManager.KEY_USER_ID);

        Util.commonKeyboardHide(ShlokSearchActivity.this);
        tvCategory = findViewById(R.id.tvCategory);
        tvCount = findViewById(R.id.tvCount);
        tvBooks = findViewById(R.id.tvBooks);
        etSearchView = findViewById(R.id.etSearchView);
        mKeyboardView = findViewById(R.id.keyboardView);
        ivKeyboard = findViewById(R.id.ivKeyboard);
        llFilter = findViewById(R.id.llFilter);
        llClose = findViewById(R.id.llClose);
        ivClose = findViewById(R.id.ivClose);
        ivSend = findViewById(R.id.ivSend);
        ivExport = findViewById(R.id.ivExport);
        ivNext = findViewById(R.id.ivNext);
        ivNext.setVisibility(View.INVISIBLE);
        tvCategory.setVisibility(View.INVISIBLE);
        tvCount.setVisibility(View.INVISIBLE);
        tvBooks.setVisibility(View.INVISIBLE);
        ivShare = findViewById(R.id.ivShare);
        //    fabFilter = findViewById(R.id.fabFilter);
        rvList = findViewById(R.id.rvList);
        tvNoRecord = findViewById(R.id.tvNoRecord);
        ll_buttons = findViewById(R.id.ll_buttons);
        buttonFilter = findViewById(R.id.button_filter);
        llKeywordCount = findViewById(R.id.llKeywordCount);
        tvKeywordCount = findViewById(R.id.tvKeywordCount);

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchView.getText().clear();
                Log.e("strUid2", strUid);

                if (isLogin) {
                    if ((strUid != null) && (strUid.length() != 0)) {
                        callShlokKeywordSearchApi(strUid, "", "1");
                    }
                } else {
                    callShlokKeywordSearchApi("0", "", "1");
                }

                //   showFilterDialog();
            }
        });
       /* ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog(v);
            }
        });*/
    }

    private void showShareDialog(View v) {
        PopupMenu popup = new PopupMenu(ShlokSearchActivity.this, v);
        popup.setOnMenuItemClickListener(ShlokSearchActivity.this);
        popup.inflate(R.menu.share_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.share) {
            String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareData);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareData);
            startActivity(Intent.createChooser(sharingIntent, shareData));
            return true;
        } else if (itemId == R.id.download) {// do your code
            return true;
        } else if (itemId == R.id.shelf) {// do your code
            return true;
        }
        return false;
    }

    /////// Header //////////
    private void setHeader() {
        headerView = findViewById(R.id.header);
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
        tvTitlePage.setText("Granth Search");

        ImageView ivSelectLogo = header2.findViewById(R.id.ivSelectLogo);

        ImageView ivFirst = headerView.findViewById(R.id.ivFirst);
        ImageView ivSecond = headerView.findViewById(R.id.ivSecond);
        ImageView ivThird = headerView.findViewById(R.id.ivThird);
        ImageView ivFour = headerView.findViewById(R.id.ivFour);
        ImageView ivFive = headerView.findViewById(R.id.ivFive);
        ImageView ivSix = headerView.findViewById(R.id.ivSix);

        ivSelectLogo.setImageResource(R.mipmap.search);
        ivFirst.setImageResource(R.mipmap.home);
        ivSecond.setImageResource(R.mipmap.my_reference);
        ivThird.setImageResource(R.mipmap.book_store);
        ivFour.setImageResource(R.mipmap.user_guide);
        ivFive.setImageResource(R.mipmap.app_info);


        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShlokSearchActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShlokSearchActivity.this, MyReferenceActivity.class);
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
                Intent i = new Intent(ShlokSearchActivity.this, UserGuideActivity.class);
                startActivity(i);
            }
        });
        ivFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShlokSearchActivity.this, AppInfoActivity.class);
                startActivity(i);
            }
        });

        isLogin = SharedPrefManager.getInstance(ShlokSearchActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);

        if (isLogin) {
            ivSix.setImageResource(R.mipmap.logout_new);
        } else {
            ivSix.setImageResource(R.mipmap.login);
        }
        ivSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(ShlokSearchActivity.this, isLogin, false);
            }
        });

        ImageView ivSearchSetting = header2.findViewById(R.id.ivSearchSetting);
        ivSearchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(ShlokSearchActivity.this, FilterMenuActivity.class);
                    startActivity(i);
                }else{
                    Utils.showInfoDialog(ShlokSearchActivity.this, "Please login");
                }
            }
        });

        /* View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        llAddItem = headerView.findViewById(R.id.llAddItem);
        ivHeaderIcon.setVisibility(View.INVISIBLE);
        tvHeaderCount.setVisibility(View.INVISIBLE);
      *//*  ivHeaderIcon.setVisibility(View.VISIBLE);
        tvHeaderCount.setVisibility(View.VISIBLE);*//*
        ivHeaderIcon.setImageResource(R.mipmap.granth);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Granth Search");*/
    }


    private void callShlokKeywordSearchApi(String strUid, String strValue, String strFlag) {

        if (!ConnectionManager.checkInternetConnection(ShlokSearchActivity.this)) {
            Utils.showInfoDialog(ShlokSearchActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(ShlokSearchActivity.this, "Please Wait...", false);
        ApiClient.getSearchShlok(strUid, strValue, strFlag, new Callback<ShlokSearchResModel>() {
            @Override
            public void onResponse(Call<ShlokSearchResModel> call, retrofit2.Response<ShlokSearchResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<ShlokSearchResModel.ShlokResModel> keywordShlokSearchModel = new ArrayList<>();
                        keywordShlokSearchModel = response.body().getData();
                        if (keywordShlokSearchModel != null && keywordShlokSearchModel.size() > 0) {
                            setShlokKeywordData(keywordShlokSearchModel);
                            tvNoRecord.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                            rvList.setVisibility(View.GONE);
                        }
                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        rvList.setVisibility(View.GONE);
                        mKeyboardView.setVisibility(View.GONE);// Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ShlokSearchResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "onFailure--- " + message);
                Utils.dismissProgressDialog();
            }
        });
    }
    @Override
    public void onDetailsClick(ShlokSearchResModel.ShlokResModel shlokResModel, int position) {
        String strName = shlokResModel.getName();
        String strId = shlokResModel.getId();
        Intent i = new Intent(ShlokSearchActivity.this, ShlokSearchDetailsActivity.class);
        i.putExtra("shlokGranthName", strName);
        Log.e("shlokGranthName", "" + strName);
        i.putExtra("shlokGranthId", strId);
        startActivity(i);
    }
}

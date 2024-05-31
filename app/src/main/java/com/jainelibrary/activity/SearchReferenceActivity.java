package com.jainelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.jainelibrary.IOnBackPressed;
import com.jainelibrary.R;
import com.jainelibrary.fragment.YearWiseSearchFragment;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.multicheck.MultiCheckActivity;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.jainelibrary.viewPagerAdapter.SearchReferenceAdapter;

import java.util.ArrayList;

public class SearchReferenceActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private static final String TAG = SearchReferenceActivity.class.getSimpleName();
    public TabLayout tabLayout;
    public ViewPager viewPager;
    private boolean isHold = false;
    private SearchReferenceAdapter adapter;
    boolean isLogin = false;
    int totalCount = 0;
    String strBookIds;
    String strOptionTypeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.e(TAG, "");
        setHeader();
        loadUiElements();
        declaration();

    }

    private void loadUiElements() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }

    private void declaration() {
        Intent i = getIntent();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setOnTabSelectedListener(this);

        Boolean isUserAdmin =SharedPrefManager.getInstance(SearchReferenceActivity.this).getBooleanPref(SharedPrefManager.KEY_USER_IS_ADMIN);

        if (i != null) {
            isHold = i.getBooleanExtra("hold", false);
            totalCount = i.getIntExtra("totalCount",0);
            strBookIds = i.getStringExtra("strBookIds");
            strOptionTypeId = i.getStringExtra("strOptionTypeId");

            String position = i.getStringExtra("SearchPosition");

            Log.e(TAG, "HoldType--" + isHold);
            if (isHold) {
                adapter = new SearchReferenceAdapter(getSupportFragmentManager(), isHold, isUserAdmin);
                viewPager.setAdapter(adapter);
                if(position != null && position.length() > 0){
                    viewPager.setCurrentItem(Integer.parseInt(position));
                }
            } else {
                adapter = new SearchReferenceAdapter(getSupportFragmentManager(), isHold, isUserAdmin);
                viewPager.setAdapter(adapter);
                if(position != null && position.length() > 0){
                    viewPager.setCurrentItem(Integer.parseInt(position));
                }
            }
        }


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.e(TAG, tab.getPosition() + "");
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
        tvTitlePage.setText("REFERENCE SEARCH");

        ImageView ivSelectLogo = header2.findViewById(R.id.ivSelectLogo);

        ImageView ivFirst = headerView.findViewById(R.id.ivFirst);
        ImageView ivSecond = headerView.findViewById(R.id.ivSecond);
        ImageView ivThird = headerView.findViewById(R.id.ivThird);
        ImageView ivFour = headerView.findViewById(R.id.ivFour);
        ImageView ivFive = headerView.findViewById(R.id.ivFive);
        ImageView ivSix = headerView.findViewById(R.id.ivSix);
        ImageView ivseven = header2.findViewById(R.id.ivseven);

        ivSelectLogo.setImageResource(R.mipmap.search);
        ivFirst.setImageResource(R.mipmap.home);
        ivSecond.setImageResource(R.mipmap.my_reference);
        ivThird.setImageResource(R.mipmap.book_store);
        ivFour.setImageResource(R.mipmap.user_guide);
        ivFive.setImageResource(R.mipmap.app_info);

        ivseven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchReferenceActivity.this, HoldAndSearchActivity.class);
                startActivity(i);
            }
        });

        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchReferenceActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchReferenceActivity.this, MyReferenceActivity.class);
                startActivity(i);
            }
        });

        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(SearchReferenceActivity.this, BookStoreActivity.class);
                    startActivity(i);
                }else{
                    Utils.showInfoDialog(SearchReferenceActivity.this, "Please login");
                }
            }
        });

        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchReferenceActivity.this, UserGuideActivity.class);
                startActivity(i);
            }
        });

        ivFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchReferenceActivity.this, AppInfoActivity.class);
                startActivity(i);
            }
        });

        isLogin = SharedPrefManager.getInstance(SearchReferenceActivity.this).
                getBooleanPreference(SharedPrefManager.IS_LOGIN);

        if (isLogin) {
            ivSix.setImageResource(R.mipmap.logout_new);
        } else {
            ivSix.setImageResource(R.mipmap.login);
        }

        ivSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(SearchReferenceActivity.this, isLogin, false);
            }
        });


        ImageView ivSearchSetting = header2.findViewById(R.id.ivSearchSetting);
        ivSearchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(SearchReferenceActivity.this, FilterMenuActivity.class);
                    startActivity(i);
                }else{
                    Utils.showInfoDialog(SearchReferenceActivity.this, "Please login");
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SearchReferenceActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_layout);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
       // SharedPrefManager.getInstance(SearchReferenceActivity.this).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_BOOK_IDS, new ArrayList<>());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

package com.jainelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jainelibrary.IOnBackPressed;
import com.jainelibrary.R;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.jainelibrary.viewPagerAdapter.FilterMenuAdapter;
import com.jainelibrary.viewPagerAdapter.MyReferenceAdapter;

import java.util.ArrayList;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class FilterMenuActivity extends AppCompatActivity  implements TabLayout.OnTabSelectedListener{
    public static final String TAG = FilterMenuActivity.class.getSimpleName();
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public boolean isLogin = false;
    public String[] tabTitles = new String[]{"Lens", "Focus", "Filter"};
    public FilterMenuAdapter filterMenuAdapter;
    private String strKeyword, position;
    private String strTabMove = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_menu);

        setHeader();


        loadUiElements();
        declaration();

    }

    private void loadUiElements() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pager);
    }

    private void declaration() {
        Intent i = getIntent();

        if (i != null) {
            strKeyword = i.getStringExtra("KID");
            position = i.getStringExtra("SettingPosition");
            //Log.e("position", position);
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        filterMenuAdapter= new FilterMenuAdapter(getSupportFragmentManager(), strKeyword);
        viewPager.setAdapter(filterMenuAdapter);
        if(position != null && position.length() > 0){
            viewPager.setCurrentItem(Integer.parseInt(position));
        }
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
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
        tvTitlePage.setText("Search Setting");

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
                Intent i = new Intent(FilterMenuActivity.this, HoldAndSearchActivity.class);
                startActivity(i);
            }
        });

        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterMenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterMenuActivity.this, MyReferenceActivity.class);
                startActivity(i);
            }
        });

        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(FilterMenuActivity.this, BookStoreActivity.class);
                    startActivity(i);
                }else{
                    Utils.showInfoDialog(FilterMenuActivity.this, "Please login");
                }
            }
        });

        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterMenuActivity.this, UserGuideActivity.class);
                startActivity(i);
            }
        });

        ivFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterMenuActivity.this, AppInfoActivity.class);
                startActivity(i);
            }
        });

        isLogin = SharedPrefManager.getInstance(FilterMenuActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);

        if (isLogin) {
            ivSix.setImageResource(R.mipmap.logout_new);
        } else {
            ivSix.setImageResource(R.mipmap.login);
        }

        ivSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(FilterMenuActivity.this, isLogin, false);
            }
        });

        ImageView ivSearchSetting = header2.findViewById(R.id.ivSearchSetting);
        ivSearchSetting.setImageResource(R.mipmap.search);

        ivSearchSetting.setVisibility(View.VISIBLE);
        ivSearchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(FilterMenuActivity.this, "You already on this screen", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FilterMenuActivity.this, SearchReferenceActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void switchToTab(String tab) {
        if (tab != null && tab.equalsIgnoreCase("1")) {
            viewPager.setCurrentItem(0);
        } else if (tab != null && tab.equalsIgnoreCase("0")) {
            viewPager.setCurrentItem(1);
        } else if (tab != null && tab.equalsIgnoreCase("2")) {
            viewPager.setCurrentItem(2);
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

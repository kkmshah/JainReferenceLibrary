package com.jainelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jainelibrary.R;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.jainelibrary.viewPagerAdapter.AppInfoAdapter;

public class AppInfoActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    private boolean isLogin = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String position = intent.getStringExtra("Position");

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        AppInfoAdapter adapter = new AppInfoAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        if(position != null && position.length() > 0){
            viewPager.setCurrentItem(Integer.parseInt(position));
        }
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
        setHeader();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
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
        View headerView2 = findViewById(R.id.header2);
        ImageView menu = headerView.findViewById(R.id.menu);
        menu.setImageResource(R.mipmap.backarrow);
        menu.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvTitlePage = headerView2.findViewById(R.id.tvTitlePage);
        tvTitlePage.setText("APP INFO");
        ImageView ivSelectLogo = headerView2.findViewById(R.id.ivSelectLogo);
        ImageView ivSeven = headerView2.findViewById(R.id.ivseven); ivSeven.setImageResource(R.drawable.hold);
        ivSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppInfoActivity.this, HoldAndSearchActivity.class);
                startActivity(i);
            }
        });

        ImageView ivFirst = headerView.findViewById(R.id.ivFirst);
        ImageView ivSecond = headerView.findViewById(R.id.ivSecond);
        ImageView ivThird = headerView.findViewById(R.id.ivThird);
        ImageView ivFour = headerView.findViewById(R.id.ivFour);
        ImageView ivFive = headerView.findViewById(R.id.ivFive);
        ImageView ivSix = headerView.findViewById(R.id.ivSix);
        ivSelectLogo.setImageResource(R.mipmap.app_info);
        ivFirst.setImageResource(R.mipmap.home);
        ivSecond.setImageResource(R.mipmap.search);
        ivThird.setImageResource(R.mipmap.my_reference);
        ivFour.setImageResource(R.mipmap.book_store);
        ivFive.setImageResource(R.mipmap.user_guide);
        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppInfoActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppInfoActivity.this, SearchReferenceActivity.class);
                startActivity(i);
            }
        });

        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppInfoActivity.this, MyReferenceActivity.class);
                startActivity(i);
            }
        });

        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(AppInfoActivity.this, BookStoreActivity.class);
                    startActivity(i);
                }else{
                    Utils.showInfoDialog(AppInfoActivity.this, "Please login");
                }
            }
        });

        ivFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppInfoActivity.this, UserGuideActivity.class);
                startActivity(i);
            }
        });

        isLogin = SharedPrefManager.getInstance(AppInfoActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            ivSix.setImageResource(R.mipmap.logout_new);
        }else{
            ivSix.setImageResource(R.mipmap.login);
        }

        ivSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(AppInfoActivity.this,isLogin, false);
            }
        });
    }
}

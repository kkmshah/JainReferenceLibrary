package com.jainelibrary.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jainelibrary.R;
import com.jainelibrary.activity.AppInfoActivity;
import com.jainelibrary.activity.AppendixActivity;
import com.jainelibrary.activity.BookStoreActivity;
import com.jainelibrary.activity.FilterMenuActivity;
import com.jainelibrary.activity.HoldAndSearchActivity;
import com.jainelibrary.activity.HomeActivity;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.MainActivity;
import com.jainelibrary.activity.MyReferenceActivity;
import com.jainelibrary.activity.PdfStoreActivity;
import com.jainelibrary.activity.SearchReferenceActivity;
import com.jainelibrary.activity.UserGuideActivity;
import com.jainelibrary.adapter.HomePagerSlider;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

public class HomeFragment extends Fragment {


    private static final int FILTER_SETTINGS = 100;
    CardView cvSearchSetting,cvKeywordSearch, cvShlokSearch, cvIndexSearch, cvUserGuide, cvAppInfo, cvBookStore, cvMyReference, cvHold,cvLogin, cvSearchReference, cvPdfStore, cvMyShelf;
    Fragment frag;
    private boolean isLogin = false;
    private ImageView ivLogin;
    private TextView tvlogin;
    private ViewPager mViewPager;
    private int[] images = {R.mipmap.ic_jrl_logo};
    private HomePagerSlider mViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_design, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().hide();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loadUiElements(view);
        setHeader(view);
        declaration();
        return view;
    }


    private void setHeader(View view) {
        View headerView = view.findViewById(R.id.header);
        View header2 = view.findViewById(R.id.header2);
        TextView tvTitlePage = view.findViewById(R.id.tvTitlePage);
        tvTitlePage.setText("HOME");

        ImageView menu = headerView.findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        ImageView ivSelectLogo = header2.findViewById(R.id.ivSelectLogo);

        ImageView ivFirst = headerView.findViewById(R.id.ivFirst);
        ImageView ivSecond = headerView.findViewById(R.id.ivSecond);
        ImageView ivThird = headerView.findViewById(R.id.ivThird);
        ImageView ivFour = headerView.findViewById(R.id.ivFour);
        ImageView ivFive = headerView.findViewById(R.id.ivFive);
        ImageView ivSix = headerView.findViewById(R.id.ivSix);
        ImageView ivSeven = header2.findViewById(R.id.ivseven);

        ivSeven.setVisibility(View.VISIBLE);

        ivSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HoldAndSearchActivity.class);
                startActivity(i);
            }
        });

        ivSelectLogo.setImageResource(R.mipmap.home);
        ivFirst.setImageResource(R.mipmap.search);
        ivSecond.setImageResource(R.mipmap.my_reference);
        ivThird.setImageResource(R.mipmap.book_store);
        ivFour.setImageResource(R.mipmap.user_guide);
        ivFive.setImageResource(R.mipmap.app_info);

        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchReferenceActivity.class);
                i.putExtra("SearchPosition","0");
                startActivity(i);
            }
        });

        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyReferenceActivity.class);
                startActivity(i);

            }
        });

        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(getActivity(), BookStoreActivity.class);
                    startActivity(i);
                }else{
                    askLogin();
                }
            }
        });

        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserGuideActivity.class);
                startActivity(i);
            }
        });

        ivFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AppInfoActivity.class);
                i.putExtra("Position","0");
                startActivity(i);
            }
        });

        isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            ivSix.setImageResource(R.mipmap.logout_new);
            ivLogin.setImageResource(R.mipmap.logout_new);
            tvlogin.setText("Logout");
        } else {
            ivSix.setImageResource(R.mipmap.login);
            ivLogin.setImageResource(R.mipmap.login);
            tvlogin.setText("Login");
        }

        ivSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(getActivity(), isLogin, false);
            }
        });

        ImageView ivSearchSetting = header2.findViewById(R.id.ivSearchSetting);
        ivSearchSetting.setVisibility(View.VISIBLE);
        ivSearchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(getActivity(), FilterMenuActivity.class);
                    startActivity(i);
                }else{
                    Utils.showInfoDialog(getActivity(), "Please login");
                }
            }
        });
    }


    private void loadUiElements(View view) {
        cvSearchSetting = view.findViewById(R.id.cvSearchSetting);
        cvKeywordSearch = view.findViewById(R.id.cvKeywordSearch);
        cvShlokSearch = view.findViewById(R.id.cvShlokSearch);
        cvIndexSearch = view.findViewById(R.id.cvIndexSearch);
        cvMyShelf = view.findViewById(R.id.cvMyShelf);
        cvPdfStore = view.findViewById(R.id.cvPdfStore);
        cvSearchReference = view.findViewById(R.id.cvSearchReference);
        cvHold = view.findViewById(R.id.cvHold);
        cvLogin = view.findViewById(R.id.cvLogin);
        cvMyReference = view.findViewById(R.id.cvMyReference);
        cvBookStore = view.findViewById(R.id.cvBookStore);
        cvAppInfo = view.findViewById(R.id.cvAppInfo);
        cvUserGuide = view.findViewById(R.id.cvUserGuide);
        ivLogin = view.findViewById(R.id.ivLogin);
        tvlogin = view.findViewById(R.id.tvlogin);
        mViewPager = view.findViewById(R.id.viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity())
                .setActionBarTitle("JRL");
    }

    private void declaration() {
        setIconPager();
        cvPdfStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PdfStoreActivity.class);
                startActivity(i);
            }
        });

        cvKeywordSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag = new KeywordsMainFragment();
                Utils.transferFragment(frag, getActivity());
            }
        });

        cvSearchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    Intent i = new Intent(getActivity(), FilterMenuActivity.class);
                    startActivity(i);
                } else {
                    askLogin();
                }

            }
        });

        cvShlokSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag = new ShlokFragment();
                Utils.transferFragment(frag, getActivity());
            }
        });

        cvIndexSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag = new IndexFragment();
                Utils.transferFragment(frag, getActivity());
            }
        });

        cvMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent i = new Intent(getActivity(), MyReferenceHomeActivity.class);
                startActivity(i);*/
            }
        });


        cvSearchReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchReferenceActivity.class);
                startActivity(i);
            }
        });


        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(getActivity(), isLogin, true);
            }
        });
        cvMyReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyReferenceActivity.class);
                startActivity(i);
            }
        });

        cvHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HoldAndSearchActivity.class);
                startActivity(i);
            }
        });

        cvBookStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(getActivity(), BookStoreActivity.class);
                    startActivity(i);
                }else{
                    askLogin();
                }
            }
        });

        cvAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AppInfoActivity.class);
                startActivity(i);
            }
        });

        cvUserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserGuideActivity.class);
                startActivity(i);
            }
        });
    }

    private void askLogin() {
        Utils.showLoginDialog(getActivity());
    }

    public void setIconPager() {

        mViewPagerAdapter = new HomePagerSlider(getActivity(), images);
        mViewPager.setAdapter(mViewPagerAdapter);
    }
}

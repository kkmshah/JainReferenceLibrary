package com.jainelibrary.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.fragment.HomeFragment;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.VersionResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wc.widget.dialog.IosDialog;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AAH_FabulousFragment.Callbacks, AAH_FabulousFragment.AnimationListener{

    public DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    NavigationView navigationView;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private View navigationMenuView;
    TextView tvHome, tvSearchReference, tvKeyword, tvShlok, tvIndex, tvYear, tvParampara, tvLens, tvFocus, tvFiles, tvBook, tvActivity,
            tvGallery, tvSearch, tvUGSetting, tvUGSearch, tvUGStorage, tvAbout, tvDataAndUsers, tvContact, tvLogout;
    ImageView ivDownSS, ivUpSS, ivDownSR, ivUpSR, ivDownMR, ivUpMR, ivDownBS, ivUpBS, ivDownUG, ivUpUG, ivDownAI, ivUpAI;
    LinearLayout llSearchSetting, llSearchReference, llMyRef, llBookStore, llUserGuide, llAppInfo, llLogout;
    RelativeLayout rlSearchSetting, rlSearchReference, rlMyRef, rlBookStore, rlUserGuide, rlAppInfo, rlHome, rlHoldRef, rlLogout;
    TextView tv_header_email, nav_header_name;
    private Toolbar toolbar;
    private String strName, strEmail, strPicture, strUniqueToken, deviceId = "";
    private com.mikhaellopez.circularimageview.CircularImageView iv_header_icon;
    private boolean isLogin = false;
    String[] language = {"English", "Gujarati", "Hindi"};
    String isFirstTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.checkAndRequestPermissions(MainActivity.this);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager())
            {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }*/

        JRL.isKeywordLoading = false;
        isFirstTime = SharedPrefManager.getInstance(this).getStringKeyword(SharedPrefManager.KEY_IS_FIRST_TIME);
        Log.e("First Time Installed",""+isFirstTime);
        
        if(isFirstTime == null){
//            Utils.showDefaultKeyboardDialog(MainActivity.this);
            SharedPrefManager.getInstance(MainActivity.this).saveStrinKeyword(SharedPrefManager.KEY_IS_FIRST_TIME,"False");
            SharedPrefManager.getInstance(MainActivity.this).saveStrinKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE, "Indic");
        }

        /*String strLanguage = SharedPrefManager.getInstance(this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        if (strLanguage == null || strLanguage.length() == 0) {
            //showLanguage();
            //String strLanguage = language["English"];
            SharedPrefManager.getInstance(MainActivity.this).saveStrinKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE, "Hindi");
        }*/

        checkForUpdate();

        initImplements();
        setToolbar();
        declaration();
        setNavHeader();

        /*if (savedInstanceState == null) {
            SelectItem(0);
        }*/
        setNavigationDrawer();

        setOnClickListener();





        /*if (savedInstanceState == null) {
            SelectItem(0);
        }*/


    }

    private void checkForUpdate() {

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);

            int currentVersionCode = pInfo.versionCode;
            if (!ConnectionManager.checkInternetConnection(MainActivity.this)) {
                return;
            }

            ApiClient.getVersion(new Callback<VersionResModel>() {
                @Override
                public void onResponse(Call<VersionResModel> call, retrofit2.Response<VersionResModel> response) {
                    Utils.dismissProgressDialog();
                    if (response.isSuccessful()) {
                        VersionResModel mVersionResMOdel = response.body();
                        boolean result = mVersionResMOdel.isStatus();
                        if (result) {
                            VersionResModel.VersionModel versionModel = mVersionResMOdel.getData();

                            if (versionModel != null) {
                                String strServerVersion = versionModel.getCurrent_version();
                                String strServerVersionName = versionModel.getCurrent_version_name();
                                String strMessage = versionModel.getMessage();

                                if (strServerVersion != null && strServerVersion.length() > 0) {
                                    int serverVersion = Integer.parseInt(strServerVersion);
                                    if (serverVersion != 0) {
                                        if (serverVersion > currentVersionCode)
                                        {
                                            Dialog dialog = new Dialog(MainActivity.this);
                                            dialog.setContentView(R.layout.dialog_update_app);
                                            TextView tvVersionName = dialog.findViewById(R.id.tvVersionName);
                                            TextView tvMessage = dialog.findViewById(R.id.tvDescription);
                                            TextView btnUpdate = dialog.findViewById(R.id.btnUpdate);
                                            tvMessage.setText(strMessage);
                                            String strVersionNames = "New Version (" + strServerVersionName + ")" + " Available";
                                            tvVersionName.setText(strVersionNames);
                                            btnUpdate.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName()));
                                                        MainActivity.this.startActivity(intent);
                                                    } catch (Exception ignored) {
                                                    }
                                                }
                                            });
                                            dialog.setCancelable(false);
                                            dialog.show();
                                        }
                                    }
                                }
                            }
                        } else {
                            return;
                        }
                    }
                }

                @Override
                public void onFailure(Call<VersionResModel> call, Throwable t) {
                    Log.e("MainActivity", "UpdateAppFailure---" + t.getMessage());
                }
            });

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showDefaultKeyboardDialog() {
        final Dialog dialogView = new Dialog(MainActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog);
        dialogView.setContentView(R.layout.dialog_default_language);
        dialogView.setCancelable(false);
        dialogView.show();

        Button btnSave = dialogView.findViewById(R.id.btnSave);
        RadioButton rbDevnagari = dialogView.findViewById(R.id.rbDevnagari);
        RadioButton rbEnglish = dialogView.findViewById(R.id.rbEnglish);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strDefaultKeyboard = null;
                if (rbDevnagari.isChecked()) {
                    strDefaultKeyboard = "Devnagari";
                } else if (rbEnglish.isChecked()) {
                    strDefaultKeyboard = "English";
                } else {
                    Utils.showInfoDialog(MainActivity.this, "Please select Language");
                }

                if (strDefaultKeyboard != null && strDefaultKeyboard.length() > 0) {
                    SharedPrefManager.getInstance(MainActivity.this).saveStrinKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE, strDefaultKeyboard);
                    SharedPrefManager.getInstance(MainActivity.this).saveStrinKeyword(SharedPrefManager.KEY_IS_FIRST_TIME,"False");
                    dialogView.dismiss();
                }
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    private void setNavigationDrawer() {
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);


        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) /*{
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        }*/;

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);
        //mDrawerLayout.setDrawerListener(mDrawerToggle);


        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setOnClickListener() {

        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = null;
                frag = new HomeFragment();
                toolbar.setTitle("JRL");
                Utils.transferFragment(frag, MainActivity.this);
            }
        });

        rlSearchReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llSearchReference.getVisibility() == View.VISIBLE){
                    llSearchReference.setVisibility(View.GONE);
                    ivDownSR.setVisibility(View.VISIBLE);
                    ivUpSR.setVisibility(View.GONE);
                }else {
                    llSearchReference.setVisibility(View.VISIBLE);
                    ivDownSR.setVisibility(View.GONE);
                    ivUpSR.setVisibility(View.VISIBLE);
                }

            }
        });

        rlSearchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llSearchSetting.getVisibility() == View.VISIBLE){
                    llSearchSetting.setVisibility(View.GONE);
                    ivDownSS.setVisibility(View.VISIBLE);
                    ivUpSS.setVisibility(View.GONE);
                }else {
                    llSearchSetting.setVisibility(View.VISIBLE);
                    ivDownSS.setVisibility(View.GONE);
                    ivUpSS.setVisibility(View.VISIBLE);
                }

            }
        });

        rlMyRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llMyRef.getVisibility() == View.VISIBLE){
                    llMyRef.setVisibility(View.GONE);
                    ivDownMR.setVisibility(View.VISIBLE);
                    ivUpMR.setVisibility(View.GONE);
                }else {
                    llMyRef.setVisibility(View.VISIBLE);
                    ivDownMR.setVisibility(View.GONE);
                    ivUpMR.setVisibility(View.VISIBLE);
                }

            }
        });

        rlBookStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llBookStore.getVisibility() == View.VISIBLE){
                    llBookStore.setVisibility(View.GONE);
                    ivDownBS.setVisibility(View.VISIBLE);
                    ivUpBS.setVisibility(View.GONE);
                }else {
                    llBookStore.setVisibility(View.VISIBLE);
                    ivDownBS.setVisibility(View.GONE);
                    ivUpBS.setVisibility(View.VISIBLE);
                }

            }
        });

        rlUserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llUserGuide.getVisibility() == View.VISIBLE){
                    llUserGuide.setVisibility(View.GONE);
                    ivDownUG.setVisibility(View.VISIBLE);
                    ivUpUG.setVisibility(View.GONE);
                }else {
                    llUserGuide.setVisibility(View.VISIBLE);
                    ivDownUG.setVisibility(View.GONE);
                    ivUpUG.setVisibility(View.VISIBLE);
                }

            }
        });

        rlAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llAppInfo.getVisibility() == View.VISIBLE){
                    llAppInfo.setVisibility(View.GONE);
                    ivDownAI.setVisibility(View.VISIBLE);
                    ivUpAI.setVisibility(View.GONE);
                }else {
                    llAppInfo.setVisibility(View.VISIBLE);
                    ivDownAI.setVisibility(View.GONE);
                    ivUpAI.setVisibility(View.VISIBLE);
                }

            }
        });

        rlHoldRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, HoldAndSearchActivity.class);
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchReferenceActivity.class);
                i.putExtra("SearchPosition","0");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvShlok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchReferenceActivity.class);
                i.putExtra("SearchPosition","1");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchReferenceActivity.class);
                i.putExtra("SearchPosition","2");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchReferenceActivity.class);
                i.putExtra("SearchPosition","3");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvParampara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchReferenceActivity.class);
                i.putExtra("SearchPosition","4");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(MainActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    Intent i = new Intent(MainActivity.this, FilterMenuActivity.class);
                    i.putExtra("SettingPosition","0");
                    startActivity(i);
                } else {
                    askLogin();
                }
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(MainActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    Intent i = new Intent(MainActivity.this, FilterMenuActivity.class);
                    i.putExtra("SettingPosition","1");
                    startActivity(i);
                } else {
                    askLogin();
                }
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MyReferenceActivity.class);
                i.putExtra("MyRefPosition","0");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MyReferenceActivity.class);
                i.putExtra("MyRefPosition","1");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MyReferenceActivity.class);
                i.putExtra("MyRefPosition","2");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(MainActivity.this, BookStoreActivity.class);
                    i.putExtra("BookStorePosition","0");
                    startActivity(i);
                    mDrawerLayout.closeDrawer(navigationView);
                }else{
                    askLogin();
                }
            }
        });


        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(MainActivity.this, BookStoreActivity.class);
                    i.putExtra("BookStorePosition","2");
                    startActivity(i);
                    mDrawerLayout.closeDrawer(navigationView);
                }else{
                    askLogin();
                }
            }
        });

        tvUGSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserGuideActivity.class);
                i.putExtra("UGPosition","0");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvUGSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserGuideActivity.class);
                i.putExtra("UGPosition","1");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvUGStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserGuideActivity.class);
                i.putExtra("UGPosition","2");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AppInfoActivity.class);
                i.putExtra("Position","0");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvDataAndUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AppInfoActivity.class);
                i.putExtra("Position","1");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AppInfoActivity.class);
                i.putExtra("Position","2");
                startActivity(i);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });

        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(MainActivity.this, isLogin, false);
                mDrawerLayout.closeDrawer(navigationView);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void initImplements() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navi_view);
        navigationMenuView = findViewById(R.id.navigationMenuView);
        tvHome = navigationMenuView.findViewById(R.id.tvHome);
        tvSearchReference = navigationMenuView.findViewById(R.id.tvSearchReference);
        tvKeyword = navigationMenuView.findViewById(R.id.tvKeyword);
        tvShlok = navigationMenuView.findViewById(R.id.tvShlok);
        tvIndex = navigationMenuView.findViewById(R.id.tvIndex);
        tvYear = navigationMenuView.findViewById(R.id.tvYear);
        tvParampara = navigationMenuView.findViewById(R.id.tvParampara);
        llSearchReference = navigationMenuView.findViewById(R.id.llSearchReference);
        ivDownSR = navigationMenuView.findViewById(R.id.ivDownSR);
        ivUpSR = navigationMenuView.findViewById(R.id.ivUpSR);
        rlSearchReference = navigationMenuView.findViewById(R.id.rlSearchReference);
        tvLens = navigationMenuView.findViewById(R.id.tvLens);
        tvFocus = navigationMenuView.findViewById(R.id.tvFocus);
        tvFiles = navigationMenuView.findViewById(R.id.tvFiles);
        tvBook = navigationMenuView.findViewById(R.id.tvBook);
        tvActivity = navigationMenuView.findViewById(R.id.tvActivity);
        tvGallery = navigationMenuView.findViewById(R.id.tvGallery);
        tvSearch = navigationMenuView.findViewById(R.id.tvSearch);
        tvUGSetting = navigationMenuView.findViewById(R.id.tvUGSetting);
        tvUGSearch = navigationMenuView.findViewById(R.id.tvUGSearch);
        tvUGStorage = navigationMenuView.findViewById(R.id.tvUGStorage);
        tvAbout = navigationMenuView.findViewById(R.id.tvAbout);
        tvDataAndUsers = navigationMenuView.findViewById(R.id.tvDataAndUsers);
        tvContact = navigationMenuView.findViewById(R.id.tvContact);
        ivDownSS = navigationMenuView.findViewById(R.id.ivDownSS);
        ivUpSS = navigationMenuView.findViewById(R.id.ivUpSS);
        ivDownMR = navigationMenuView.findViewById(R.id.ivDownMR);
        ivUpMR = navigationMenuView.findViewById(R.id.ivUpMR);
        ivDownBS = navigationMenuView.findViewById(R.id.ivDownBS);
        ivUpBS = navigationMenuView.findViewById(R.id.ivUpBS);
        ivDownUG = navigationMenuView.findViewById(R.id.ivDownUG);
        ivUpUG = navigationMenuView.findViewById(R.id.ivUpUG);
        ivDownAI = navigationMenuView.findViewById(R.id.ivDownAI);
        ivUpAI = navigationMenuView.findViewById(R.id.ivUpAI);
        rlHome = navigationMenuView.findViewById(R.id.rlHome);
        llSearchSetting = navigationMenuView.findViewById(R.id.llSearchSetting);
        rlHoldRef = navigationMenuView.findViewById(R.id.rlHoldRef);
        rlLogout = navigationMenuView.findViewById(R.id.rlLogout);
        llMyRef = navigationMenuView.findViewById(R.id.llMyRef);
        llBookStore = navigationMenuView.findViewById(R.id.llBookStore);
        llUserGuide = navigationMenuView.findViewById(R.id.llUserGuide);
        llAppInfo = navigationMenuView.findViewById(R.id.llAppInfo);
        rlSearchSetting = navigationMenuView.findViewById(R.id.rlSearchSetting);
        rlMyRef = navigationMenuView.findViewById(R.id.rlMyRef);
        rlBookStore = navigationMenuView.findViewById(R.id.rlBookStore);
        rlUserGuide = navigationMenuView.findViewById(R.id.rlUserGuide);
        rlAppInfo = navigationMenuView.findViewById(R.id.rlAppInfo);
        tvLogout = navigationMenuView.findViewById(R.id.tvLogout);
    }

    @Override
    public void onOpenAnimationStart() {

    }

    @Override
    public void onOpenAnimationEnd() {

    }

    @Override
    public void onCloseAnimationStart() {

    }

    @Override
    public void onCloseAnimationEnd() {

    }

    @Override
    public void onResult(Object result) {

    }

    private MainActivity.onBackPressed onBackClickListener;

    public void setOnBackClickListener(onBackPressed onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public interface onBackPressed {
        boolean onBackClick();
    }

    @Override
    public void onBackPressed() {
        Log.e("Dashboard---", " onBackPress");
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (onBackClickListener != null && onBackClickListener.onBackClick()) {
                return;
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        declaration();
        setNavHeader();
    }

    private void declaration() {
        deviceId = Settings.System.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        strUniqueToken = SharedPrefManager.getInstance(MainActivity.this).getStringPref(SharedPrefManager.KEY_IS_UNIQUE_TOKEN);
        isLogin = SharedPrefManager.getInstance(MainActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
    }

    private void setNavHeader() {
        View headerView = findViewById(R.id.navHeader);
        Menu menu = navigationView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.nav_logout);
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(MainActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);
        if (isLogin) {
            tvLogout.setText("Logout");
            //nav_login.setIcon(R.mipmap.logout_new);
        } else {
            tvLogout.setText("Login");
            //nav_login.setIcon(R.mipmap.login);
        }
        if (userDetailsModel != null) {
            strEmail = userDetailsModel.getEmail();
            strName = userDetailsModel.getName();
            Log.e("strEmail--", "" + strEmail);
            Log.e("strName--", "" + strName);
        } else {
            strEmail = "";
            strName = "";
        }
        iv_header_icon = headerView.findViewById(R.id.nav_header_icon);
        tv_header_email = headerView.findViewById(R.id.nav_header_email);
        nav_header_name = headerView.findViewById(R.id.nav_header_name);
        if (strEmail != null && strEmail.length() > 0) {
            Log.e("strEmailIn--", "" + strEmail);
            tv_header_email.setText(strEmail);
        } else {
            tv_header_email.setVisibility(View.GONE);
            //     tv_header_email.setText("Jrl@gmail.com");
        }
        if (strName != null && strName.length() > 0) {
            Log.e("strnameIn--", "" + strName);
            nav_header_name.setText(strName);
        } else {
            nav_header_name.setVisibility(View.GONE);
            //  nav_header_name.setText("JRL");
        }

        if (strPicture != null && strPicture.length() > 0) {
            Picasso.get().load(strPicture).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(iv_header_icon);
        }
    }
    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }*/

    private void askLogin() {
        Utils.showLoginDialog(MainActivity.this);
    }
}
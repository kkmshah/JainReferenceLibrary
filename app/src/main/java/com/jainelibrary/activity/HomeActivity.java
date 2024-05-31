package com.jainelibrary.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.fragment.HomeFragment;
import com.jainelibrary.fragment.IndexFragment;

import com.jainelibrary.fragment.ShlokFragment;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AAH_FabulousFragment.Callbacks, AAH_FabulousFragment.AnimationListener {
    private static final String TAG = "Dashboard";
    private Toolbar toolbar;
    TextView tv_header_email, nav_header_name;
    NavigationView navigationView;
    private com.mikhaellopez.circularimageview.CircularImageView iv_header_icon;
    private String strName, strEmail, strPicture, strUniqueToken;
    JRL myApp;
    private boolean isLogin = false;
    String[] language = {"English", "Gujarati", "Hindi"};
    public DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("Dashboard", "Dashboard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeDeclration();
    }

    private void homeDeclration() {
        Utils.checkAndRequestPermissions(HomeActivity.this);
        findViewBYID();
        String strLanguage = SharedPrefManager.getInstance(this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        if (strLanguage == null || strLanguage.length() == 0) {
            //showLanguage();
            //String strLanguage = language["English"];
            SharedPrefManager.getInstance(HomeActivity.this).saveStrinKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE, "English");
        }
        setToolbar();
        setMenuDrawer();
        declaration();
        setNavHeader();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        Log.e("Dashboard---", " onBackPress");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
/*
   @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.nav_logout);
        Log.e("isLogin---", "isLogin--" + isLogin);
        if (isLogin) {
            item.setTitle("nav_logout");
        } else {
            item.setTitle("Login");
        }
        return super.onPrepareOptionsMenu(menu);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }
*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment frag = null;
        int id = item.getItemId();
        Log.e("isLogin---", "isLogin--" + isLogin);
        if (id == R.id.nav_logout) {
            Utils.logout(HomeActivity.this, isLogin, false);
          /*  if (isLogin) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(HomeActivity.this);
                a_builder.setMessage("Are you sure you want to logout?");
                a_builder.setCancelable(false);
                a_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!ConnectionManager.checkInternetConnection(HomeActivity.this)) {
                            Toast.makeText(HomeActivity.this, "please check your internt connection...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SharedPrefManager.getInstance(HomeActivity.this).logout();
                        onResume();
                    }
                });
                a_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = a_builder.create();
                alert.setTitle("JRL");
                alert.show();
            } else {
                Intent intent = new Intent(HomeActivity.this, LoginWithPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("isLoginId", Utils.Is_Common_Login);
                startActivity(intent);
                finish();
            }*/
        }

        if (id == R.id.nav_home) {
            frag = new HomeFragment();
            toolbar.setTitle("JRL");
            Utils.transferFragment(frag, HomeActivity.this);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(HomeActivity.this, AppInfoActivity.class);
            i.putExtra("Position","0");
            startActivity(i);
        }  else if (id == R.id.nav_keyword) {
            Intent i = new Intent(HomeActivity.this, SearchReferenceActivity.class);
            i.putExtra("SearchPosition","0");
            startActivity(i);
        } else if (id == R.id.nav_user_guide) {
            Intent i = new Intent(HomeActivity.this, UserGuideActivity.class);
            startActivity(i);
            //   Utils.transferFragment(frag, HomeActivity.this);
        } else if (id == R.id.nav_shlok) {
            Intent i = new Intent(HomeActivity.this, SearchReferenceActivity.class);
            i.putExtra("SearchPosition","1");
            startActivity(i);
            //    Utils.transferFragment(frag, HomeActivity.this);
        } else if (id == R.id.nav_index) {
            Intent i = new Intent(HomeActivity.this, SearchReferenceActivity.class);
            i.putExtra("SearchPosition","2");
            startActivity(i);
            //    Utils.transferFragment(frag, HomeActivity.this);
        } else if (id == R.id.nav_my_shelf) {
           /* frag = new MyShelfFragment();
            toolbar.setTitle("My Shelf");
            Utils.transferFragment(frag, HomeActivity.this);*/
            Intent i = new Intent(HomeActivity.this, MyReferenceActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_pdf_store) {
            if (isLogin) {
                Intent i = new Intent(HomeActivity.this, BookStoreActivity.class);
                startActivity(i);
            }else{
                Utils.showInfoDialog(HomeActivity.this, "Please login");
            }
        } else if (id == R.id.data_and_users) {
            Intent i = new Intent(HomeActivity.this, AppInfoActivity.class);
            i.putExtra("Position","1");
            startActivity(i);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        fragment.onActivityResult(requestCode, resultCode, data);
    }


    String deviceId = "";

    private void declaration() {

        deviceId = Settings.System.getString(HomeActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        strUniqueToken = SharedPrefManager.getInstance(HomeActivity.this).getStringPref(SharedPrefManager.KEY_IS_UNIQUE_TOKEN);
        isLogin = SharedPrefManager.getInstance(HomeActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();

    }

    private void findViewBYID() {
        navigationView = findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

    }

    private void setMenuDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    private void setNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.nav_logout);
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(HomeActivity.this).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);
        if (isLogin) {
            nav_login.setTitle("Logout");
            nav_login.setIcon(R.mipmap.logout_new);
        } else {
            nav_login.setTitle("Login");
            nav_login.setIcon(R.mipmap.login);
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
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    private onBackPressed onBackClickListener;
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
    public interface onBackPressed {
        boolean onBackClick();
    }
    public void setOnBackClickListener(onBackPressed onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }
    @Override
    protected void onResume() {
        super.onResume();
        declaration();
        setNavHeader();
    }
    public void showLanguage() {
        ArrayAdapter genderAdapter = new ArrayAdapter(HomeActivity.this, R.layout.dropdown_textview, language);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Select Language");
        builder.setSingleChoiceItems(genderAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
                String strLanguage = language[position];
                SharedPrefManager.getInstance(HomeActivity.this).saveStrinKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE, strLanguage);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}

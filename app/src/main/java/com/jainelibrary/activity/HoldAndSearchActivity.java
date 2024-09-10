package com.jainelibrary.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.adapter.HoldAndSearchAdapter;
import com.jainelibrary.fragment.KeywordsMainFragment;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.HoldAndSearchResModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.AddAllMyShelfResModel;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.StorageManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class HoldAndSearchActivity extends AppCompatActivity implements HoldAndSearchAdapter.SearchInterfaceListener, PopupMenu.OnMenuItemClickListener {
    private static final int HOLD_SEARCH = 1;
    JRL myApplication;
    String strUserId;
    private RecyclerView rvHoldReference;
    private ArrayList<BookListResModel.BookDetailsModel> mBookDetailsList = new ArrayList<>();
    private ArrayList<BookListResModel.BookDetailsModel> mTempBookDetailsList = new ArrayList<>();
    private HoldAndSearchAdapter mSearchAdapter;
    private int selected = 1;
    private String strKeyName = "";
    private String PackageName;
    private ImageView ivShare;
    private LinearLayout llShlokNumberSearch, llKeywordSearch, llIndexSearch;
    private LinearLayout button_filter;
    private TextView tvSearchNewRef, tvMoveAll, tvHoldRef, tvViewHoldRef;
    private BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
    private TextView tvNoDataFound;
    private String strType;
    private LinearLayout llAddItem;
    private TextView tvHeaderCount;
    private ImageView ivHeaderIcon;
    private boolean isLogin = false;
    private String strSelectedBookIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hold_and_search_fragment);
        myApplication = (JRL) getApplicationContext();
        Log.e("Hold", "hold reference fragment");
        loadUiElements();
        declaration();
        setHeader();
        setFooter();
        onEventClickListener();
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
        tvTitlePage.setText("HOLD REFERENCE");
        ImageView ivSelectLogo = headerView2.findViewById(R.id.ivSelectLogo);
        ImageView ivFirst = headerView.findViewById(R.id.ivFirst);
        ImageView ivSecond = headerView.findViewById(R.id.ivSecond);
        ImageView ivThird = headerView.findViewById(R.id.ivThird);
        ImageView ivFour = headerView.findViewById(R.id.ivFour);
        ImageView ivFive = headerView.findViewById(R.id.ivFive);
        ImageView ivSix = headerView.findViewById(R.id.ivSix);
        ImageView ivseven = headerView2.findViewById(R.id.ivseven);

        ivSelectLogo.setImageResource(R.drawable.hold);
        ivFirst.setImageResource(R.mipmap.home);
        ivSecond.setImageResource(R.mipmap.search);
        ivThird.setImageResource(R.mipmap.my_reference);
        ivFour.setImageResource(R.mipmap.book_store);
        ivFive.setImageResource(R.mipmap.user_guide);
        ivseven.setImageResource(R.mipmap.app_info);
        ivseven.setPadding(10, 10, 10, 10);

        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HoldAndSearchActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HoldAndSearchActivity.this, SearchReferenceActivity.class);
                startActivity(i);
            }
        });

        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HoldAndSearchActivity.this, MyReferenceActivity.class);
                startActivity(i);
            }
        });
        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(HoldAndSearchActivity.this, BookStoreActivity.class);
                    startActivity(i);
                }else{
                    Utils.showInfoDialog(HoldAndSearchActivity.this, "Please login");
                }
            }
        });

        ivFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HoldAndSearchActivity.this, UserGuideActivity.class);
                startActivity(i);
            }
        });

        if (isLogin) {
            ivSix.setImageResource(R.mipmap.logout_new);
        } else {
            ivSix.setImageResource(R.mipmap.login);
        }

        ivSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(HoldAndSearchActivity.this, isLogin, false);
            }
        });

        ivseven.setVisibility(View.VISIBLE);
        ivseven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HoldAndSearchActivity.this, AppInfoActivity.class);
                startActivity(i);
            }
        });


        ImageView ivSearchSetting = headerView2.findViewById(R.id.ivSearchSetting);
        ivSearchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent i = new Intent(HoldAndSearchActivity.this, FilterMenuActivity.class);
                    startActivity(i);
                } else {
                    Utils.showInfoDialog(HoldAndSearchActivity.this, "Please login");
                }
            }
        });

    }

    private void onEventClickListener() {
        llKeywordSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HoldAndSearchActivity.this, KeywordsMainFragment.class);
                startActivity(i);
            }
        });

        tvMoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strUserId != null && strUserId.length() >0){
                    showHoldReferenceDialog(strUserId, "");
                }
            }
        });
        llIndexSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(HoldAndSearchActivity.this, IndexSearchActivity.class);
                startActivity(i);*/
            }
        });
        llShlokNumberSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HoldAndSearchActivity.this, ShlokSearchActivity.class);
                startActivity(i);
            }
        });
        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void setFooter() {
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(HoldAndSearchActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    showShareDialog(v);
                } else {
                    askLogin();
                }
            }
        });
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(HoldAndSearchActivity.this, HOLD_SEARCH);
    }

    private void declaration() {
        isLogin = SharedPrefManager.getInstance(HoldAndSearchActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);

        PackageName = HoldAndSearchActivity.this.getPackageName();
        Intent i = getIntent();
        if (i != null) {
            strType = i.getStringExtra("strType");
            if (strType != null && strType.equalsIgnoreCase("0")) {
                tvSearchNewRef.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_keyword_search)));
                tvViewHoldRef.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_keyword_search)));

            }
            if (strType != null && strType.equalsIgnoreCase("1")) {
                tvSearchNewRef.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_shlok_number_search)));
                tvViewHoldRef.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_shlok_number_search)));

            }
            if (strType != null && strType.equalsIgnoreCase("2")) {
                tvSearchNewRef.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_index_search)));
                tvViewHoldRef.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_index_search)));

            }
        }
        strUserId = SharedPrefManager.getInstance(HoldAndSearchActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (strUserId != null && strUserId.length() > 0) {
            callListHoldSearchKeyword(strUserId, false);
        }
    }


    private void showShareDialog(View v) {
        PopupMenu popup = new PopupMenu(HoldAndSearchActivity.this, v);
        popup.setOnMenuItemClickListener(HoldAndSearchActivity.this);
        popup.inflate(R.menu.share_menu);
        popup.show();
    }

    private void loadUiElements() {
        rvHoldReference = findViewById(R.id.recycler_view_hold_ref);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);
        ivShare = findViewById(R.id.ivShare);
        llIndexSearch = findViewById(R.id.llIndexSearch);
        llShlokNumberSearch = findViewById(R.id.llShlokNumberSearch);
        llKeywordSearch = findViewById(R.id.llKeywordSearch);
        button_filter = findViewById(R.id.button_filter);
        tvSearchNewRef = findViewById(R.id.tvSearchNewRef);
        tvViewHoldRef = findViewById(R.id.tvViewHoldRef);
        tvHoldRef = findViewById(R.id.tvViewHoldRef);
        tvMoveAll = findViewById(R.id.tvMoveAll);
    }

    private void setBookData(ArrayList<BookListResModel.BookDetailsModel> bookDetailList) {
        if (bookDetailList == null || bookDetailList.size() == 0) {
            Log.e("bookList : ", "size : " + bookDetailList.size());
            return;
        }

        rvHoldReference.setHasFixedSize(true);
        rvHoldReference.setLayoutManager(new LinearLayoutManager(HoldAndSearchActivity.this));
        rvHoldReference.setVisibility(View.VISIBLE);
        mSearchAdapter = new HoldAndSearchAdapter(HoldAndSearchActivity.this, bookDetailList, this);
        rvHoldReference.setAdapter(mSearchAdapter);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetailsClick(BookListResModel.BookDetailsModel mBookDataModel, ArrayList<BookListResModel.BookDetailsModel> searchdataList, int position) {
        String strId = searchdataList.get(position).getBook_id();
        String strPageNo = searchdataList.get(position).getPdf_page_no();
        mBookDataModel.setPage_no(strPageNo);
        Log.e("strPageNo--", "strPageNostrPageNo" + strPageNo);
        if (strId != null && strId.length() > 0) {
            Intent i = new Intent(HoldAndSearchActivity.this, ReferencePageActivity.class);
            i.putExtra("model", mBookDataModel);
            startActivity(i);
        }
    }

    public void onZoomClick(BookListResModel.BookDetailsModel mBookDataModel) {
        Intent i = new Intent(HoldAndSearchActivity.this, ZoomImageActivity.class);
        String strImageUrl = mBookDataModel.getBook_large_image();
        String fallbackImage = mBookDataModel.getBook_image();

        i.putExtra("image", strImageUrl);
        i.putExtra("fallbackImage", fallbackImage);
        i.putExtra("url", true);
        startActivity(i);
    }

    @Override
    public void onMenuClick(BookListResModel.BookDetailsModel bookDetailsModel, int position, ImageView ivMenu) {
        boolean isLogin = SharedPrefManager.getInstance(getApplicationContext()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        strUserId = SharedPrefManager.getInstance(getApplicationContext()).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (isLogin) {
            showPopUpMenu(ivMenu, bookDetailsModel);
        } else {
            askLogin();
        }
    }

    @Override
    public void onCancelClick(ArrayList<BookListResModel.BookDetailsModel> mBookList, int position) {

    }

    private void showPopUpMenu(ImageView ivMenu, BookListResModel.BookDetailsModel filesModel) {
        PopupMenu popup = new PopupMenu(HoldAndSearchActivity.this, ivMenu);
        popup.getMenuInflater().inflate(R.menu.hold_search_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String strKeywordId = filesModel.getKeywordId();
                String strHoldId = filesModel.getId();
                String strBookId = filesModel.getBook_id();
                String strBookName = filesModel.getBook_name();
                String strPdfPageNo = filesModel.getPdf_page_no();
                String strPageNo = filesModel.getPage_no();
                String strEditorName = filesModel.getEditor_name();
                String strPublisherName = filesModel.getPublisher_name();
                String strTranslator = filesModel.getTranslator();
                String strBookUrl = filesModel.getBook_url();
                String strPDfUrl = filesModel.getPdf_link();
                String strTypeName = filesModel.getType_name();

                BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
                mBookDataModels.setKeyword(strBookName);
                mBookDataModels.setBook_id(strBookId);
                mBookDataModels.setBook_name(strBookName);
                if (strPDfUrl != null && strPDfUrl.length() > 0) {
                    mBookDataModels.setPdf_link(strPdfPageNo);
                }
                if (strPdfPageNo != null && strPdfPageNo.length() > 0) {
                    mBookDataModels.setPdf_page_no(strPdfPageNo);
                    mBookDataModels.setPage_no(strPdfPageNo);
                } else {
                    mBookDataModels.setPdf_page_no("0");
                    mBookDataModels.setPage_no("0");
                }
                mBookDataModels.setEditor_name(strEditorName);
                mBookDataModels.setPublisher_name(strPublisherName);
                mBookDataModels.setTranslator(strTranslator);
                mBookDataModels.setBook_url(strBookUrl);
                mBookDataModels.setFlag(strType);
                mBookDataModels.setType_name(strTypeName);
                mBookDataModels.setPage_no(strPageNo);

                switch (item.getItemId()) {
                    case R.id.open:
                        Intent is = new Intent(getApplicationContext(), ReferencePageActivity.class);
                        is.putExtra("model", mBookDataModels);
                        is.putExtra("from", "Hold");
                        is.putExtra("strBookName", mBookDataModels.getBook_name());
                        is.putExtra("strTypeName", mBookDataModels.getType_name());
                        is.putExtra("type_id", filesModel.getType_id());
                        is.putExtra("strHoldPageNo", mBookDataModels.getPage_no());
                        startActivity(is);
                        return true;
                    case R.id.delete:
                        Log.e("strId : ", strHoldId + " bookId : " + mBookDataModels.getBook_id());
                        getDeleteDialog(strHoldId, "Are you sure you want to delete " + mBookDataModels.getBook_name() + ", " + mBookDataModels.getType_name() + ", " + mBookDataModels.getPage_no());
                        return true;
                 /*   case R.id.book_info:
                        Intent i = new Intent(getApplicationContext(), BookDetailsActivity.class);
                        i.putExtra("model", mBookDataModels);
                        startActivity(i);
                        return true;*/
                   /* case R.id.share:
                        if (strPDfUrl != null && strPDfUrl.length() > 0) {
                            String shareData = " Get Latest JRl Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                            String strMessage = " " + strPDfUrl;
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                            intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                            intent.setType("text/plain");
                            startActivity(Intent.createChooser(intent, shareData));
                        } else {
                            Toast.makeText(getApplicationContext(), "Pdf not found", Toast.LENGTH_SHORT).show();
                            Log.e("strPdfLinkShare---", "pdfLink--" + strPDfUrl);
                        }
                        return true;*/
                  /*  case R.id.download:
                        if (strPDfUrl != null && strPDfUrl.length() > 0) {
                            Utils.downloadPdf(strBookName, strPDfUrl, getApplicationContext());
                        } else {
                            Toast.makeText(getApplicationContext(), "Pdf not found", Toast.LENGTH_SHORT).show();
                            Log.e("strPdfLinkShare---", "pdfLink--" + strPDfUrl);
                        }
                        return true;*/
                    case R.id.my_reference:
                        if (strHoldId != null &&  strBookName != null) {
                            if(strUserId != null && strUserId.length() >0){
                                showHoldReferenceDialog(strUserId,strHoldId);
                            }
                           // callAddMyShelfApi(strBookId, strBookName, strKeywordId);
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();

    }



    public void getInfoDialogs(String strNotes, String strType, String strTypeRef, String strTite, String strBookName, String strKeywordId) {
        Dialog dialog = new IosDialog.Builder(HoldAndSearchActivity.this)
                .setMessage(strTite)
                .setMessageColor(Color.parseColor("#1565C0"))
                .setMessageSize(18)
                .setNegativeButtonColor(Color.parseColor("#981010"))
                .setNegativeButtonSize(18)
                .setNegativeButton("No", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButtonColor(Color.parseColor("#981010"))
                .setPositiveButtonSize(18)
                .setPositiveButton("Yes", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(HoldAndSearchActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strBookName);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }

    private void setCallListHoldSearchKeywordRef(ArrayList<BookListResModel.BookDetailsModel> mDataList) {
        for (int i = 0; i < mDataList.size(); i++) {
            String strBookId = mDataList.get(i).getBook_id();
            if (strSelectedBookIds == null || strSelectedBookIds.length() == 0) {
                strSelectedBookIds = strBookId;
            } else {
                strSelectedBookIds = strSelectedBookIds + "," + strBookId;
            }
        }

        if (mDataList != null && mDataList.size() > 0) {
            tvNoDataFound.setVisibility(View.GONE);
            rvHoldReference.setVisibility(View.VISIBLE);
            tvHoldRef.setText(mDataList.size() + " HOLD REFERENCE");
            setBookData(mDataList);
        } else {
            tvNoDataFound.setVisibility(View.VISIBLE);
            rvHoldReference.setVisibility(View.GONE);
        }
    }
    public void callListHoldSearchKeyword(String strUId, boolean skipCache) {
        String cacheKey ="_"+ strUId;
        boolean notCacheData = true;
        ArrayList<BookListResModel.BookDetailsModel>  mBookDetailsCacheModelList = StorageManager.getHoldSearchKeywordList(cacheKey, skipCache);
        if (mBookDetailsCacheModelList != null && mBookDetailsCacheModelList.size() > 0) {
            notCacheData = false;
            setCallListHoldSearchKeywordRef(mBookDetailsCacheModelList);
        }
        if (!ConnectionManager.checkInternetConnection(HoldAndSearchActivity.this)) {
            if(notCacheData) {
                Utils.showInfoDialog(HoldAndSearchActivity.this, "Please check internet connection");
            }
            return;
        }
        if(notCacheData) {
            Utils.showProgressDialog(HoldAndSearchActivity.this, "Please Wait...", false);
        }

        ApiClient.getHolderSearchList(strUId, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<BookListResModel.BookDetailsModel> mDataList = new ArrayList<>();
                        mDataList = response.body().getData();

                        setCallListHoldSearchKeywordRef(mDataList);
                        if (mDataList != null && mDataList.size() > 0) {
                            StorageManager.setHoldSearchKeywordList(mDataList, cacheKey);
                        }
                    } else {
                        tvNoDataFound.setVisibility(View.VISIBLE);
                        rvHoldReference.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<BookListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public void deleteHoldSearchKeyword(String strId) {
        if (!ConnectionManager.checkInternetConnection(HoldAndSearchActivity.this)) {
            Utils.showInfoDialog(HoldAndSearchActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(HoldAndSearchActivity.this, "Please Wait...", false);
        ApiClient.deleteHolderSearchList(strId, new Callback<HoldAndSearchResModel>() {
            @Override
            public void onResponse(Call<HoldAndSearchResModel> call, retrofit2.Response<HoldAndSearchResModel> response) {

                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.showInfoDialog(HoldAndSearchActivity.this, "" + response.body().getMessage());
                        Log.e("Message", response.body().getMessage());
                        callListHoldSearchKeyword(strUserId, true);
                    } else {
                        Utils.dismissProgressDialog();
                        Log.e("Message", response.body().getMessage());
                        Intent i = new Intent(HoldAndSearchActivity.this, HoldAndSearchActivity.class);
                        i.putExtra("model", mBookDataModels);
                        startActivity(i);
                        Utils.showInfoDialog(HoldAndSearchActivity.this, "" + response.body().getMessage());
                    }
                } else {
                    Utils.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<HoldAndSearchResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public void getDeleteDialog(String strId, String strTite) {
        Dialog dialog = new IosDialog.Builder(this)
                .setMessage(strTite)
                .setMessageColor(Color.parseColor("#1565C0"))
                .setMessageSize(18)
                .setNegativeButtonColor(Color.parseColor("#981010"))
                .setNegativeButtonSize(18)
                .setNegativeButton("No", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButtonColor(Color.parseColor("#981010"))
                .setPositiveButtonSize(18)
                .setPositiveButton("Yes", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        Log.e("strId : ", "bookId not : " + strId);
                        deleteHoldSearchKeyword(strId);
                    }
                }).build();
        dialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void showHoldReferenceDialog(String strUid, String strBookId)
    {
        final Dialog dialogView = new Dialog(HoldAndSearchActivity.this, R.style.Theme_AppCompat_Light_Dialog);
        dialogView.setContentView(R.layout.dialog_hold_reference);
        dialogView.setCancelable(false);
        dialogView.show();

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        CheckBox cbPreviousPage = dialogView.findViewById(R.id.cbPreviousPage);
        CheckBox cbReferencePage = dialogView.findViewById(R.id.cbReferencePage);
        CheckBox cbNextPage = dialogView.findViewById(R.id.cbNextPage);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean previous = cbPreviousPage.isChecked();
                boolean next = cbNextPage.isChecked();

                dialogView.dismiss();

                callAllAddMyShelfApi(strUid, strBookId, previous, next);
            }
        });
    }

    private void callAllAddMyShelfApi(String strUid, String strBookId, boolean previous, boolean next) {

        if (!ConnectionManager.checkInternetConnection(HoldAndSearchActivity.this)) {
            Utils.showInfoDialog(HoldAndSearchActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(HoldAndSearchActivity.this, "Please Wait...", false);
        ApiClient.addAllMyShelf(strUid, strBookId, Boolean.toString(previous), Boolean.toString(next), new Callback<AddAllMyShelfResModel>() {
            @Override
            public void onResponse(Call<AddAllMyShelfResModel> call, retrofit2.Response<AddAllMyShelfResModel> response) {
                Utils.dismissProgressDialog();

                if (response.isSuccessful()) {
                    //Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                    if (response.body().isStatus()) {
                        strUserId = SharedPrefManager.getInstance(HoldAndSearchActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        callListHoldSearchKeyword(strUid, true);
                        Utils.showInfoDialog(HoldAndSearchActivity.this, "" + response.body().getMessage());
                    }else{
                        Utils.showInfoDialog(HoldAndSearchActivity.this, "" + response.body().getMessage());
                    }
                }else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<AddAllMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : "+throwable.getMessage());
            }
        });
    }

}
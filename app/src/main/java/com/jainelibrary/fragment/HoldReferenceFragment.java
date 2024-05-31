/*
package com.jainelibrary.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.activity.HoldAndSearchActivity;
import com.jainelibrary.activity.HomeActivity;
import com.jainelibrary.activity.IndexSearchActivity;
import com.jainelibrary.fragment.KeywordsMainFragment;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.ReferencePageActivity;
import com.jainelibrary.activity.ShlokSearchActivity;
import com.jainelibrary.adapter.HoldAndSearchAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.HoldAndSearchResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class HoldReferenceFragment extends Fragment implements HoldAndSearchAdapter.SearchInterfaceListener, PopupMenu.OnMenuItemClickListener {
    private static final int HOLD_SEARCH = 1;
    private RecyclerView recycler_view_hold_ref;
    private ArrayList<BookListResModel.BookDetailsModel> mBookDetailsList = new ArrayList<>();
    private ArrayList<BookListResModel.BookDetailsModel> mTempBookDetailsList = new ArrayList<>();
    private HoldAndSearchAdapter mSearchAdapter;
    private int selected = 1;
    private String strKeyName = "";
    private String PackageName;
    private ImageView ivShare;
    private LinearLayout llShlokNumberSearch, llKeywordSearch, llIndexSearch;
    private LinearLayout button_filter;
    private TextView tvSearchNewRef, tvViewHoldRef;
    private BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
    private TextView tvHoldRef, tvNoDataFound;
    String strUserId;
    private String strType;
    private LinearLayout llAddItem;
    private TextView tvHeaderCount;
    private ImageView ivHeaderIcon;

    boolean isHold = false;
    private LinearLayout ll_buttons;

    public HoldReferenceFragment(boolean isHold) {
        this.isHold = isHold;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hold_and_search_fragment, container, false);
        Log.e("Hold","hold reference fragment");
        loadUiElements(view);
        declaration();
        setHeader(view);
        setFooter();
        onEventClickListener();
        return view;
    }

    private void onEventClickListener() {
        llKeywordSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), KeywordsMainFragment.class);
                startActivity(i);
            }
        });

        llIndexSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), IndexSearchActivity.class);
                startActivity(i);
            }
        });

        llShlokNumberSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ShlokSearchActivity.class);
                startActivity(i);
            }
        });
        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

    private void setFooter() {
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    showShareDialog(v);
                } else {
                    askLogin();
                }
            }
        });
    }

    private void askLogin() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(getActivity(), LoginWithPasswordActivity.class);
                        intent.putExtra("isLoginId", Utils.Is_Hold_Login_Id);
                        startActivityForResult(intent, HOLD_SEARCH);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(android.text.Html.fromHtml("<font color='#1565C0'>Please login to use this functionality.</font>"))
                .setPositiveButton("Login", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    private void declaration() {
        PackageName = getActivity().getPackageName();
        if (isHold) {
            ll_buttons.setVisibility(View.VISIBLE);
        } else {
            ll_buttons.setVisibility(View.GONE);
        }
       */
/* Intent i = getActivity().getIntent();
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
        }*//*

        strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (strUserId != null && strUserId.length() > 0) {
            callListHoldSearchKeyword(strUserId);
        }
    }

    private void setHeader(View view) {
        View headerView = view.findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        llAddItem = headerView.findViewById(R.id.llAddItem);
        tvHeaderCount = headerView.findViewById(R.id.tvKey);
        ivHeaderIcon = headerView.findViewById(R.id.ivDelete);
        tvHeaderCount.setText("MENU");
        tvHeaderCount.setVisibility(View.VISIBLE);
        ivHeaderIcon.setVisibility(View.GONE);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.VISIBLE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        tvHeaderCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Hold & Search New");
    }

    private void showShareDialog(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.share_menu);
        popup.show();
    }

    private void loadUiElements(View view) {
        recycler_view_hold_ref = view.findViewById(R.id.recycler_view_hold_ref);
        tvNoDataFound = view.findViewById(R.id.tvNoDataFound);
        ivShare = view.findViewById(R.id.ivShare);
        llIndexSearch = view.findViewById(R.id.llIndexSearch);
        llShlokNumberSearch = view.findViewById(R.id.llShlokNumberSearch);
        llKeywordSearch = view.findViewById(R.id.llKeywordSearch);
        button_filter = view.findViewById(R.id.button_filter);
        tvSearchNewRef = view.findViewById(R.id.tvSearchNewRef);
        tvViewHoldRef = view.findViewById(R.id.tvViewHoldRef);
        tvHoldRef = view.findViewById(R.id.tvHoldRef);
        ll_buttons = view.findViewById(R.id.ll_buttons);
    }

    private void setBookData(ArrayList<BookListResModel.BookDetailsModel> bookDetailList) {
        if (bookDetailList == null || bookDetailList.size() == 0) {
            return;
        }
        recycler_view_hold_ref.setHasFixedSize(true);
        recycler_view_hold_ref.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_hold_ref.setVisibility(View.VISIBLE);
        mSearchAdapter = new HoldAndSearchAdapter(getActivity(), bookDetailList, this);
        recycler_view_hold_ref.setAdapter(mSearchAdapter);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetailsClick(BookListResModel.BookDetailsModel mBookDataModel, ArrayList<BookListResModel.BookDetailsModel> searchdataList, int position) {
        String strId = searchdataList.get(position).getBook_id();
        String strPageNo = searchdataList.get(position).getPdf_page_no();
        mBookDataModel.setPage_no(strPageNo);
        Log.e("strPageNo--", "strPageNostrPageNo" + strPageNo);
        if (strId != null && strId.length() > 0) {
            Intent i = new Intent(getActivity(), ReferencePageActivity.class);
            i.putExtra("model", mBookDataModel);
            startActivity(i);
        }
    }

    @Override
    public void onCancelClick(ArrayList<BookListResModel.BookDetailsModel> mBookList, int position) {
        String strId = mBookList.get(position).getId();
        if (strId != null && strId.length() > 0) {
            getDeleteDialog(strId, position, "Are you sure want to remove this data ?");
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareData);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareData);
                startActivity(Intent.createChooser(sharingIntent, shareData));
                return true;
            case R.id.download:
                // do your code
                return true;
            case R.id.shelf:
                // do your code
                return true;
            case R.id.keyword:
                return true;
            case R.id.shlok:
                return true;
            case R.id.index:
                return true;
            default:
                return false;
        }
    }

    public void callListHoldSearchKeyword(String strUId) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        //Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getHolderSearchList(strUId, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        ArrayList<BookListResModel.BookDetailsModel> mDataList = new ArrayList<>();
                        mDataList = response.body().getData();
                        if (mDataList != null && mDataList.size() > 0) {
                            tvHoldRef.setText(mDataList.size() + " HOLD REFERENCE");
                            tvNoDataFound.setVisibility(View.GONE);
                            recycler_view_hold_ref.setVisibility(View.VISIBLE);
                            setBookData(mDataList);
                        } else {
                            tvHoldRef.setText(" NO REFERENCE FOUND");
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            recycler_view_hold_ref.setVisibility(View.GONE);
                        }
                    } else {
                        tvHoldRef.setText("NO REFERENCE FOUND");
                        //      Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        tvNoDataFound.setVisibility(View.VISIBLE);
                        recycler_view_hold_ref.setVisibility(View.GONE);
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

    public void deleteHoldSearchKeyword(String strId, int position) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.deleteHolderSearchList(strId, new Callback<HoldAndSearchResModel>() {
            @Override
            public void onResponse(Call<HoldAndSearchResModel> call, retrofit2.Response<HoldAndSearchResModel> response) {

                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        callListHoldSearchKeyword(strUserId);
                    } else {
                        Utils.dismissProgressDialog();
                        Intent i = new Intent(getActivity(), HoldAndSearchActivity.class);
                        i.putExtra("model", mBookDataModels);
                        startActivity(i);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    public void getDeleteDialog(String strId, int position, String strTite) {

        Dialog dialog = new IosDialog.Builder(getActivity())
                .setTitle("Alert!")
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
                        deleteHoldSearchKeyword(strId, position);
                    }
                }).build();
        dialog.show();
    }

}
*/

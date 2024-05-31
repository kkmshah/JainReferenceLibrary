package com.jainelibrary.fragment;


import static android.view.animation.Animation.RELATIVE_TO_SELF;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.adapter.FilterSearchOptionAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.SaveFilterBooksResModel;
import com.jainelibrary.retrofitResModel.SearchOptionResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterLensFragment extends Fragment implements FilterSearchOptionAdapter.onLensSelectListener {
    private static final String TAG = FilterLensFragment.class.getSimpleName();
    LinearLayout llButtons;
    String strOptionIds = "";
    RecyclerView rvLens;
    Button btnCancel, btnApply;
    List<SearchOptionResModel.SearchOptionModel> searchOptionModelList = new ArrayList<>();
    private String strUid;
    private ArrayList<String> optionBookIdList = new ArrayList<>();
    private FilterSearchOptionAdapter filterSearchOptionAdapter;
    ImageView ivInfo;
    LinearLayout llDetailsInfo;
    TextView tvDetails;
    TextView tvInfo;
    private String strDescription;
    private View header1, header2, appBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_lens, container, false);
        setHeaders();
        loadUiElements(view);
        declaration();
        return view;
    }

    private void setHeaders() {
        header1 = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
        appBarLayout = getActivity().findViewById(R.id.appbar);
    }

    private void loadUiElements(View view) {
        rvLens = view.findViewById(R.id.rvLens);
        llButtons = view.findViewById(R.id.llButtons);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnApply = view.findViewById(R.id.btnApply);
        ivInfo = view.findViewById(R.id.ivInfo);
        llDetailsInfo = view.findViewById(R.id.llDetailsInfo);
        tvDetails = view.findViewById(R.id.tvDetails);
        tvInfo = view.findViewById(R.id.tvInfo);
    }

    private void declaration() {
        optionBookIdList = SharedPrefManager.getInstance(getActivity()).getFilteredBookIdList(SharedPrefManager.KEY_FILTER_OPTION_BOOK_IDS);

        if (optionBookIdList == null || optionBookIdList.size() == 0) {
            optionBookIdList = new ArrayList<>();
        }

        strUid = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

        callSearchOptionApi();

        onEventClickListener();

    }

    private void onEventClickListener() {
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getInfoDialogs(strDescription);

                if (strDescription != null && strDescription.length() > 0) {
                    if (llDetailsInfo.getVisibility() == View.VISIBLE){
                        RotateAnimation rotate = new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(300);
                        rotate.setFillAfter(true);
                        ivInfo.startAnimation(rotate);

                        llDetailsInfo.setVisibility(View.GONE);
                    }else{
                        RotateAnimation rotate = new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(300);
                        rotate.setFillAfter(true);
                        ivInfo.startAnimation(rotate);

                        llDetailsInfo.setVisibility(View.VISIBLE);
                    }
                    tvInfo.setText(strDescription);
//                    tvInfo.loadData(strDescription, "text/html; charset=utf-8", "utf-8");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (optionBookIdList != null && optionBookIdList.size() > 0) {
                    for (int i = 0; i < optionBookIdList.size(); i++) {
                        String strTempOpnId = optionBookIdList.get(i);
                        if (strTempOpnId != null && strTempOpnId.length() > 0) {
                            if (strOptionIds != null && strOptionIds.length() > 0) {
                                strOptionIds = strOptionIds + "," + strTempOpnId;
                            } else {
                                strOptionIds = strTempOpnId;
                            }
                        }
                    }

                    SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_OPTION_BOOK_IDS, optionBookIdList);

                    if (strOptionIds != null && strOptionIds.length() > 0) {
                        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_LENS_IDS, strOptionIds);

                        ApiClient.saveSearchTypes(strUid, strOptionIds, new Callback<SaveFilterBooksResModel>() {
                            @Override
                            public void onResponse(Call<SaveFilterBooksResModel> call, Response<SaveFilterBooksResModel> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().isStatus()) {
                                        Dialog dialog = new IosDialog.Builder(getActivity())
                                                .setMessage("You selected " +  optionBookIdList.size() + " Lenses from " + searchOptionModelList.size())
                                                .setMessageColor(Color.parseColor("#1565C0"))
                                                .setMessageSize(18)
                                                .setPositiveButtonColor(Color.parseColor("#981010"))
                                                .setPositiveButtonSize(18)
                                                .setPositiveButton("OK", new IosDialog.OnClickListener() {
                                                    @Override
                                                    public void onClick(IosDialog dialog, View v) {
                                                        dialog.dismiss();
                                                        transferToHome(false);
                                                    }
                                                }).build();
                                        dialog.show();
                                    } else {
                                        Utils.showInfoDialog(getActivity(), "No Filter Option Found");
                                    }
                                } else {
                                    Log.e(TAG, "onFilterSearchOption--" + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<SaveFilterBooksResModel> call, Throwable t) {
                                Log.e(TAG, "onFilterSearchOption OnFailure --" + t.getMessage());
                            }
                        });
                    }
                } else {
                    SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_LENS_IDS, "");
                    SharedPrefManager.getInstance(getActivity()).saveFilterBookIdList(SharedPrefManager.KEY_FILTER_OPTION_BOOK_IDS, new ArrayList<>());

                    transferToHome(false);
                }

            }
        });
    }

    public void transferToHome(boolean isClear) {
        if (isClear) {
            return;
        }

        Utils.showInfoDialog(getActivity(), "You selected " +  optionBookIdList.size() + " Lenses from " + searchOptionModelList.size());

        String strLens = "" + optionBookIdList.size() + "/" + searchOptionModelList.size();
        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_LENS_DATA, strLens);
        SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_FILTER_LENS_IDS, strOptionIds);

        Intent i = new Intent(getActivity(), KeywordsMainFragment.class);
        Log.e(TAG,"Lens Ids :"+strOptionIds);
        i.putExtra("strOptionTypeId", strOptionIds);
        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();
    }


    private void callSearchOptionApi() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
//        Utils.showProgressDialog(getActivity(), "Please wait......", false);
        ApiClient.getFilterSearchOption(strUid, new Callback<SearchOptionResModel>() {
            @Override
            public void onResponse(Call<SearchOptionResModel> call, Response<SearchOptionResModel> response) {
//                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        searchOptionModelList = response.body().getData();
                        strDescription = response.body().getDescription();

                        if (searchOptionModelList != null && searchOptionModelList.size() > 0) {
                            SharedPrefManager.getInstance(getActivity()).saveStringPref(SharedPrefManager.KEY_TOTAL_LENS_DATA, String.valueOf(searchOptionModelList.size()));


                            if (optionBookIdList == null || optionBookIdList.size() == 0) {
                                for (int i = 0; i < searchOptionModelList.size(); i++) {
                                    String strId = String.valueOf(searchOptionModelList.get(i).getId());
//                                    searchOptionModelList.get(i).setSelected(true);
                                    if (optionBookIdList != null && searchOptionModelList.get(i).isSelected() && !optionBookIdList.contains(strId)) {
                                        optionBookIdList.add(strId);
                                    }
                                }
                            }

                            //tvLensCount.setText("(" + optionBookIdList.size() + "/" + searchOptionModelList.size() + ")");

                            setSearchOptionAdapter(searchOptionModelList);
                        }
                    } else {
                        Utils.showInfoDialog(getActivity(), "No Filter Option Found");
                    }
                } else {
                    Log.e(TAG, "onFilterSearchOption--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchOptionResModel> call, Throwable t) {
                Log.e(TAG, "onFilterSearchOption OnFailure --" + t.getMessage());
            }
        });

    }

    private void setSearchOptionAdapter(List<SearchOptionResModel.SearchOptionModel> searchOptionModelList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvLens.setLayoutManager(linearLayoutManager);
        filterSearchOptionAdapter = new FilterSearchOptionAdapter(getActivity(), searchOptionModelList, this, optionBookIdList);
        rvLens.setAdapter(filterSearchOptionAdapter);
        //rvLens.setHasFixedSize(true);
        rvLens.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                header1.setTranslationY(-80);
                header2.setTranslationY(-40);
                header1.setVisibility(View.GONE);
                header2.setVisibility(View.GONE);
                appBarLayout.setMinimumHeight(0);
            }
            @Override
            public void onShow() {
                header1.setTranslationY(0);
                header2.setTranslationY(0);
                header1.setVisibility(View.VISIBLE);
                header2.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onLensSelect(List<SearchOptionResModel.SearchOptionModel> optionModelList, int position) {
        String strBookIds = String.valueOf(optionModelList.get(position).getId());
        boolean isBookSelected = optionModelList.get(position).isSelected();
        Log.e("optionBookIdList size", optionBookIdList.toString() + " " + isBookSelected);
        if (strBookIds != null && strBookIds.length() > 0) {
            if (isBookSelected) {
                if (!optionBookIdList.contains(strBookIds)) {
                    optionBookIdList.add(strBookIds);
                }
            } else {
                if (optionBookIdList.contains(strBookIds)) {
                    optionBookIdList.remove(strBookIds);
                }
            }
//            Utils.showInfoDialog(getActivity(), "You selected " +  optionBookIdList.size() + " Lenses from " + optionModelList.size());

           // Toast.makeText(getActivity(), " "+ optionBookIdList.size() + "/" + optionModelList.size() + " selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInfo(List<SearchOptionResModel.SearchOptionModel> optionValuesList, int position) {
        showInfoDialog(optionValuesList.get(position));
    }

    private void showInfoDialog(SearchOptionResModel.SearchOptionModel searchOptionModel) {
        String strMessage  = searchOptionModel.getMessage();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(strMessage)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

}

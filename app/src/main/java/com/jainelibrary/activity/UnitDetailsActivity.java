package com.jainelibrary.activity;

import static com.jainelibrary.utils.Utils.getExportUnitDetailsUrl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jainelibrary.BuildConfig;
import com.jainelibrary.Constantss;
import com.jainelibrary.R;
import com.jainelibrary.adapter.SliderAdapter;
import com.jainelibrary.adapter.UnitBiodataListAdapter;
import com.jainelibrary.adapter.UnitEventListAdapter;
import com.jainelibrary.adapter.UnitRelationListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.BiodataMemoryDetailsModel;
import com.jainelibrary.model.ImageFileModel;
import com.jainelibrary.model.ParamparaFilterDataResModel;
import com.jainelibrary.model.RelationDetailsModel;
import com.jainelibrary.model.RelationModel;
import com.jainelibrary.model.SearchUnitListResModel;
import com.jainelibrary.model.UnitDetailsModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.retrofitResModel.UnitDetailsResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.smarteist.autoimageslider.SliderView;
import com.wc.widget.dialog.IosDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitDetailsActivity extends AppCompatActivity implements UnitBiodataListAdapter.BiodataClickListener, UnitBiodataListAdapter.BiodataHighlight, UnitEventListAdapter.EventClickListener, UnitEventListAdapter.EventHighlight, UnitRelationListAdapter.RelationClickListener, UnitRelationListAdapter.RelationHighlight {

    private static final String TAG = UnitDetailsActivity.class.getSimpleName();
    // Urls of our images.
     Activity activity;
    SearchUnitListResModel.Unit searchUnitModel;
    TextView tvUnitName, tvUnitType, tvUnitSect, tvUnitStatus, tvImageCount, tvBioDataCount, tvEventCount, tvRelationCount, tvTitlePgCount;
    LinearLayout llTitlePage;
    RecyclerView rvBiodataList, rvEventList, rvRelationList;
    UnitBiodataListAdapter biodataListAdapter;
    UnitEventListAdapter eventListAdapter;
    UnitRelationListAdapter relationListAdapter;
    ImageView sliderNext;
    ImageView sliderPrev;
    LinearLayout llSliderControl;
    ArrayList<ParamparaFilterDataResModel.RelationType> relationTypeList = new ArrayList<ParamparaFilterDataResModel.RelationType>();
    LinearLayout llBiodataDetails, llEventDetails, llRelationDetails, llImageDetails;
    SliderView sliderView;
    UnitDetailsModel unitDetailsModel;
    Keyboard mKeyboard;
    CustomKeyboardView mKeyboardView;
    String strTypeRef;
    String shareText, strPdfLink;
    private String PackageName;
    private String strTitleName, strPreviousName;
    private String strUserId, strUnitId;
    private LinearLayout llExport, llTree;
    private BottomSheetDialog bottomSheetDialog;
    TextView tvPageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_details);
        Log.e(TAG + ":" + strUserId, "KeywordSearchDetailsActivity");
        PackageName = UnitDetailsActivity.this.getPackageName();
        loadUiElements();
        declaration();
        setHeader();
        onViewTreeClickLiseners();
        onSaveClickListeners();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG + ":" + "Resume", "CustomBackPressed");
        Log.e(TAG + ":" + "CustomBackPressed", "--" + SharedPrefManager.getInstance(UnitDetailsActivity.this).getBooleanPreference("CustomBackPressed"));
        if (SharedPrefManager.getInstance(UnitDetailsActivity.this).getBooleanPreference("CustomBackPressed")) {
            SharedPrefManager.getInstance(UnitDetailsActivity.this).setBooleanPreference("CustomBackPressed", false);
            super.onBackPressed();
        }
    }

    private void loadSliderUiElements() {
        llImageDetails = findViewById(R.id.llImageDetails);
        tvImageCount = findViewById(R.id.tvImageCount);
        // initializing the slider view.
        sliderView = findViewById(R.id.slider);
        sliderPrev = findViewById(R.id.btnSliderPrev);
        sliderNext = findViewById(R.id.btnSliderNext);
        llSliderControl = findViewById(R.id.llSliderControl);
        sliderPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvImageCount.setText(sliderView.getCurrentPagePosition() + 1 + "/" + sliderView.getSliderAdapter().getCount());
                sliderView.slideToPreviousPosition();
            }
        });

        // Next button click listener
        sliderNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderView.slideToNextPosition();
                tvImageCount.setText(sliderView.getCurrentPagePosition() + 1 + "/" + sliderView.getSliderAdapter().getCount());

            }
        });
    }

    private void setImageSlider(ArrayList<ImageFileModel> imageFiles) {
        // we are creating array list for storing our image urls.
        if (imageFiles.size() > 0) {
            llImageDetails.setVisibility(View.VISIBLE);
        }
        // adding the urls inside array list
        sliderView.setAutoCycle(false);
        SliderAdapter adapter = new SliderAdapter(this, imageFiles);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);


        tvImageCount.setText(sliderView.getCurrentPagePosition() + 1 + "/" + sliderView.getSliderAdapter().getCount());
        if (imageFiles.size() <= 1) {
            llSliderControl.setVisibility(View.GONE);
        }

    }

    ParamparaFilterDataResModel.Samvat searchBioSamvatYear;
    private void declaration() {

        strUnitId = getIntent().getStringExtra("strUnitId");
        searchBioSamvatYear = (ParamparaFilterDataResModel.Samvat) getIntent().getSerializableExtra("searchBioSamvatYear");
        searchUnitModel = (SearchUnitListResModel.Unit) getIntent().getSerializableExtra("searchUnitModel");
        if(searchUnitModel !=null && searchUnitModel.getId().isEmpty()) {
            searchUnitModel = null;
        }
        relationTypeList = (ArrayList<ParamparaFilterDataResModel.RelationType>) getIntent().getSerializableExtra("relationTypeList");
        strUserId = SharedPrefManager.getInstance(UnitDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);

        getUnitDetails();
    }

    private void loadUiElements() {
        llTitlePage = findViewById(R.id.llTitlePage);
        tvTitlePgCount = findViewById(R.id.txtTitlePgCount);
        tvUnitName = findViewById(R.id.tvUnitName);
        tvUnitType = findViewById(R.id.tvUnitType);
        tvUnitStatus = findViewById(R.id.tvUnitStatus);
        tvUnitSect = findViewById(R.id.tvUnitSect);


        tvBioDataCount = findViewById(R.id.tvBioDataCount);
        tvEventCount = findViewById(R.id.tvEventCount);
        tvRelationCount = findViewById(R.id.tvRelationCount);
        llBiodataDetails = findViewById(R.id.llBiodataDetails);
        llEventDetails = findViewById(R.id.llEventDetails);
        llRelationDetails = findViewById(R.id.llRelationDetails);
        rvBiodataList = findViewById(R.id.rvBiodataList);
        rvEventList = findViewById(R.id.rvEventList);
        rvRelationList = findViewById(R.id.rvRelationList);


        llTree = findViewById(R.id.llTree);
        llExport = findViewById(R.id.llExport);
        loadSliderUiElements();
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvPageName = headerView.findViewById(R.id.tvPage);

        if(searchUnitModel != null) {
            tvPageName.setText(searchUnitModel.getName());
        } else if(unitDetailsModel != null) {
            tvPageName.setText(unitDetailsModel.getName());
        } {
            tvPageName.setText("");
        }
//        if (strFlag != null && strFlag.equalsIgnoreCase("3")){
//            tvPageName.setText(strBookName);
//        }else{
//            tvPageName.setText("Book Info");
//        }
    }

    private void setUnitDetails(UnitDetailsModel unitDetailsModel) {
        tvPageName.setText(unitDetailsModel.getName());
        tvUnitName.setText(unitDetailsModel.getName());
        tvUnitStatus.setText(unitDetailsModel.getStatus_name());
        tvUnitType.setText(unitDetailsModel.getType_name());
        tvUnitSect.setText(unitDetailsModel.getSect_name());
        tvImageCount.setText("1/" + unitDetailsModel.getFiles().size());
        ImageView ivUnitType = findViewById(R.id.ivUnitType);
        Glide.with(UnitDetailsActivity.this)
                .load(Utils.getUnitIcon(unitDetailsModel.getType_id()))
                .fitCenter()
                .into(ivUnitType);

        if(searchUnitModel != null && searchUnitModel.getFound_event_ids().size() > 0){
            tvEventCount.setText(searchUnitModel.getFound_event_ids().size() + "/" + unitDetailsModel.getEvent_count());
        } else {
            tvEventCount.setText(unitDetailsModel.getEvent_count());
        }
        if(searchUnitModel != null && searchUnitModel.getFound_biodata_ids().size() > 0){
            tvBioDataCount.setText(searchUnitModel.getFound_biodata_ids().size() + "/" + unitDetailsModel.getBiodata_count());
        } else {
            tvBioDataCount.setText(unitDetailsModel.getBiodata_count());
        }
        if(searchUnitModel != null && searchUnitModel.getFound_relation_ids().size() > 0){
            tvRelationCount.setText(searchUnitModel.getFound_relation_ids().size() + "/" + unitDetailsModel.getRelation_count());
        } else {
            tvRelationCount.setText(unitDetailsModel.getRelation_count());
        }
        if (unitDetailsModel.getBiodata_list().size() == 0) {
            llBiodataDetails.setVisibility(View.GONE);
        }
        if (unitDetailsModel.getEvents_list().size() == 0) {
            llEventDetails.setVisibility(View.GONE);
        }
        if (unitDetailsModel.getRelation_list().size() == 0) {
            llRelationDetails.setVisibility(View.GONE);
        }
        setBiodataListData(unitDetailsModel.getBiodata_list());
        setEventListData(unitDetailsModel.getEvents_list());
        setRelationListData(unitDetailsModel.getRelation_list());
        setImageSlider(unitDetailsModel.getFiles());
    }

    private void setBiodataListData(ArrayList<BiodataMemoryDetailsModel> biodataList) {
        if (biodataList == null) {
            return;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UnitDetailsActivity.this);
        rvBiodataList.setHasFixedSize(true);
        rvBiodataList.setLayoutManager(linearLayoutManager);

        Collections.sort(biodataList, new Comparator<BiodataMemoryDetailsModel>() {
            @Override
            public int compare(BiodataMemoryDetailsModel b1, BiodataMemoryDetailsModel b2) {
                if(!isBiodataIdHighlight(b1.getId()) && isBiodataIdHighlight(b2.getId())) {
                    return  1;
                }
                if(isBiodataIdHighlight(b1.getId()) && !isBiodataIdHighlight(b2.getId())) {
                    return  -1;
                }
                return  0;

            }
        });

        if (biodataList.size() > 1) {
            ViewGroup.LayoutParams layoutParams = rvBiodataList.getLayoutParams();
            Log.e(TAG, "Re height" + layoutParams.height);
            layoutParams.height = (int) Math.round(layoutParams.height * 1.8);
            rvBiodataList.setLayoutParams(layoutParams);
        }

        rvBiodataList.setVisibility(View.VISIBLE);
        biodataListAdapter = new UnitBiodataListAdapter(UnitDetailsActivity.this, biodataList, searchBioSamvatYear, this, this);
        rvBiodataList.setAdapter(biodataListAdapter);


        //   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void setEventListData(ArrayList<BiodataMemoryDetailsModel> eventList) {
        if (eventList == null) {
            return;
        }

        Collections.sort(eventList, new Comparator<BiodataMemoryDetailsModel>() {
            @Override
            public int compare(BiodataMemoryDetailsModel e1, BiodataMemoryDetailsModel e2) {
                if(!isBiodataIdHighlight(e1.getId()) && isBiodataIdHighlight(e2.getId())) {
                    return  1;
                }
                if(isBiodataIdHighlight(e1.getId()) && !isBiodataIdHighlight(e2.getId())) {
                    return  -1;
                }
                return  0;

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UnitDetailsActivity.this);
        rvEventList.setHasFixedSize(true);
        rvEventList.setLayoutManager(linearLayoutManager);
        rvEventList.setVisibility(View.VISIBLE);
        eventListAdapter = new UnitEventListAdapter(UnitDetailsActivity.this, eventList, searchBioSamvatYear, this, this);
        rvEventList.setAdapter(eventListAdapter);
        if (eventList.size() > 1) {
            ViewGroup.LayoutParams layoutParams = rvEventList.getLayoutParams();
            Log.e(TAG, "Re height" + layoutParams.height);
            layoutParams.height = (int) Math.round(layoutParams.height * 1.8);
            rvEventList.setLayoutParams(layoutParams);
        }

        //   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void setRelationListData(ArrayList<RelationDetailsModel> relationList) {
        if (relationList == null) {
            return;
        }

        Collections.sort(relationList, new Comparator<RelationDetailsModel>() {
            @Override
            public int compare(RelationDetailsModel r1, RelationDetailsModel r2) {
                if(!isRelationIdHighlight(r1.getId()) && isRelationIdHighlight(r2.getId())) {
                    return  1;
                }
                if(isRelationIdHighlight(r1.getId()) && !isRelationIdHighlight(r2.getId())) {
                    return  -1;
                }
                return  0;

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UnitDetailsActivity.this);
        rvRelationList.setHasFixedSize(true);
        rvRelationList.setLayoutManager(linearLayoutManager);
        rvRelationList.setVisibility(View.VISIBLE);
        relationListAdapter = new UnitRelationListAdapter(UnitDetailsActivity.this, relationList, this, this);
        rvRelationList.setAdapter(relationListAdapter);
        if (relationList.size() > 1) {
            ViewGroup.LayoutParams layoutParams = rvRelationList.getLayoutParams();
            Log.e(TAG, "Re height" + layoutParams.height);
            layoutParams.height = (int) Math.round(layoutParams.height * 1.8);
            rvRelationList.setLayoutParams(layoutParams);
        }

        //   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void getUnitDetails() {
        if (!ConnectionManager.checkInternetConnection(UnitDetailsActivity.this)) {
            Utils.showInfoDialog(UnitDetailsActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(UnitDetailsActivity.this, "Please Wait...", false);
        ApiClient.getUnitDetails(strUserId, strUnitId, new Callback<UnitDetailsResModel>() {
            @Override
            public void onResponse(Call<UnitDetailsResModel> call, retrofit2.Response<UnitDetailsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        unitDetailsModel = response.body().getDetails();
                        setUnitDetails(unitDetailsModel);
                    } else {
                        //Toast.makeText(UnitDetailsActivity.this, "Year Type not found", Toast.LENGTH_SHORT).show();
                        Utils.showInfoDialog(UnitDetailsActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UnitDetailsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e(TAG, "error:theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void showBioDataMemoryDetails(String bmId) {
        Intent intent = new Intent(UnitDetailsActivity.this, BiodataMemoryDetailsActivity.class);
        intent.putExtra("strBiodataId", bmId);
        intent.putExtra("searchBioSamvatYear", searchBioSamvatYear);
        intent.putExtra("unitDetailsModel", unitDetailsModel);
        startActivity(intent);

    }

    @Override
    public void onBiodataClick(ArrayList<BiodataMemoryDetailsModel> biodataList, int position) {
//
        String strBiodataId = biodataList.get(position).getId();
        showBioDataMemoryDetails(strBiodataId);
    }

    public boolean isBiodataIdHighlight(String strBiodataId) {

        return searchUnitModel != null && (searchUnitModel.getFound_biodata_ids().contains(strBiodataId));
    }

    public boolean isBiodataHighlight(ArrayList<BiodataMemoryDetailsModel> biodataList, int position) {

        String strBiodataId = biodataList.get(position).getId();
        return isBiodataIdHighlight(strBiodataId);
    }

    @Override
    public void onEventClick(ArrayList<BiodataMemoryDetailsModel> eventList, int position) {
        String strEventId = eventList.get(position).getId();
        showBioDataMemoryDetails(strEventId);
    }

    public boolean isEventHighlight(ArrayList<BiodataMemoryDetailsModel> eventList, int position) {

        String strEventId = eventList.get(position).getId();
        return searchUnitModel != null && (searchUnitModel.getFound_event_ids().contains(strEventId));
    }

    private void showRelationDetails(String relationId) {
        Intent intent = new Intent(UnitDetailsActivity.this, RelationDetailsActivity.class);
        intent.putExtra("strRelationId", relationId);

        intent.putExtra("unitDetailsModel", unitDetailsModel);
        startActivity(intent);

    }

    @Override
    public void onRelationClick(ArrayList<RelationDetailsModel> relationList, int position) {
//
        String strRelationId = relationList.get(position).getId();
        showRelationDetails(strRelationId);
//        showUnitDetails(strUnitId, unitModels.get(position));

    }

    public boolean isRelationIdHighlight( String strRelationId) {

        return searchUnitModel != null && (searchUnitModel.getFound_relation_ids().contains(strRelationId));
    }

    public boolean isRelationHighlight(ArrayList<RelationDetailsModel> relationList, int position) {

        String strRelationId = relationList.get(position).getId();
        return isRelationIdHighlight(strRelationId);
    }

    private void onSaveClickListeners() {
        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(UnitDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    getShareDialog();
                   // callUnitDetailExportPdf(unitDetailsModel.getId());
                } else {
                    askLogin();
                }
            }
        });
    }

    private void showUnitRelationTree() {
        Intent intent = new Intent(UnitDetailsActivity.this, UnitRelationTreeActivity.class);
        intent.putExtra("strUnitId", strUnitId);
        intent.putExtra("unitDetailsModel", unitDetailsModel);
        if(relationTypeList !=null) {
            ArrayList<ParamparaFilterDataResModel.RelationType> cloneRelationTypes = new ArrayList<>();
            for(int i = 1; i < relationTypeList.size(); i++ ) {
                cloneRelationTypes.add(relationTypeList.get(i));
            }
            intent.putExtra("relationTypeList", cloneRelationTypes);
        }
        startActivity(intent);
    }

    private void onViewTreeClickLiseners() {
        llTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnitRelationTree();
            }
        });
    }

    private String getUnitFormatName() {
        return unitDetailsModel.getNumber() + "_" + unitDetailsModel.getName();

    }

    private String getExportPDFFileName() {
        return "JainRefLibrary_Unit_" + getUnitFormatName();
    }

    public void getShareDialog() {
        bottomSheetDialog = new BottomSheetDialog(UnitDetailsActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetDialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_share, findViewById(R.id.bottomSheetContainer));
        LinearLayout bottomSheetContainer = bottomSheetDialogView.findViewById(R.id.bottomSheetContainer);
        Button btnShare = bottomSheetDialogView.findViewById(R.id.btnShare);
        Button btnMyShelf = bottomSheetDialogView.findViewById(R.id.btnMyShelf);
        Button btnDownload = bottomSheetDialogView.findViewById(R.id.btnDownload);
        btnDownload.setVisibility(View.GONE);
        EditText edtRenameFile = bottomSheetDialogView.findViewById(R.id.edtRenameFile);
        ImageView ivClose = bottomSheetDialogView.findViewById(R.id.ivClose);
        LinearLayout llRename = bottomSheetDialogView.findViewById(R.id.llRename);
        LinearLayout llTotal = bottomSheetDialogView.findViewById(R.id.llTotal);
        llTotal.setVisibility(View.GONE);
        CustomKeyboardView mKeyboardView = bottomSheetDialogView.findViewById(R.id.keyboardView);
        String strLanguage = SharedPrefManager.getInstance(UnitDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.setFocusable(true);
        strUserId = SharedPrefManager.getInstance(UnitDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        String strEdtRenamefile = getExportPDFFileName();
        edtRenameFile.setText(strEdtRenamefile);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        TextView tvRename = bottomSheetDialogView.findViewById(R.id.tvRename);
        //tvRename.setTextColor(getResources().getColor(R.color.color_keyword_search));
        //edtRenameFile.setTextColor(getResources().getColor(R.color.color_keyword_search));
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(UnitDetailsActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(UnitDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, UnitDetailsActivity.this, strLanguage, bottomSheetDialog, null);
//                return false;
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = getExportPDFFileName() + " /";
                Constantss.FILE_NAME_PDF = getExportPDFFileName() + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                callAddMyShelfApi(strUserId, strEdtRenamefile, true);

            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                Constantss.FILE_NAME = strEdtRenamefile;
                String strNewPDFile = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strEdtRenamefile + ".pdf";
                callUnitDetailExportPdf(unitDetailsModel.getId(), strNewPDFile);
            }
        });
        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                Constantss.FILE_NAME = getExportPDFFileName() + " /";
                Constantss.FILE_NAME_PDF = getExportPDFFileName() + " /";
                String strEdtRenamefile = edtRenameFile.getText().toString();
                String strNewPDFile = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strEdtRenamefile + ".pdf";
                callAddMyShelfApi(strUserId, strEdtRenamefile, false);
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        bottomSheetDialog.show();
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) bottomSheetDialogView.getParent());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet,
                                       @BottomSheetBehavior.State int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss();
                } else {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        if (mKeyboardView.getVisibility() == View.VISIBLE) {
                            mKeyboardView.setVisibility(View.GONE);
                        } else {
                            bottomSheetDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void callUnitDetailExportPdf(String strUnitId, String downloadFilePath) {
        if (!ConnectionManager.checkInternetConnection(UnitDetailsActivity.this)) {
            Utils.showInfoDialog(UnitDetailsActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(UnitDetailsActivity.this, "Please Wait...", false);
        ApiClient.getUnitDetailPdfFile(strUnitId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    ResponseBody downloadFileRes = response.body();
                    //Log.e("responseData Unit :", new GsonBuilder().setPrettyPrinting().create().toJson(downloadFileRes));
                    String strPdfFile = downloadFile(downloadFileRes);
                    if (strPdfFile != null && strPdfFile.length() > 0) {
                       // getShareDialog(strPdfFile);

                        new File(strPdfFile).renameTo(new File(downloadFilePath));
                        Constantss.FILE_NAME_PDF = "JainRefLibrary /" + getUnitFormatName() + " /";
                        callDownloadMyShelfsApi(strUserId, downloadFilePath);
                    } else {
                        Utils.showInfoDialog(UnitDetailsActivity.this, "Pdf data not download");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public String downloadFile(ResponseBody body) {
        try {
            Log.d("downloadFile", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            String strEdtRenamefile = getExportPDFFileName();

            String strFilePath = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strEdtRenamefile + ".pdf";
            Log.d(TAG, "Download file path:" + strFilePath);
            try {
                in = body.byteStream();
                out = new FileOutputStream(strFilePath);
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } catch (IOException e) {
                Log.d("DownloadPdf", e.toString());
                return "";
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            return strFilePath;

        } catch (IOException e) {
            Log.d("DownloadPdf", e.toString());
            return "";
        }
    }



    private void askLogin() {
        Utils.showLoginDialogForResult(UnitDetailsActivity.this, 1);
    }

    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(UnitDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserId);
        Log.e("strTypeRef :", " " + strTypeRef);
        Utils.showProgressDialog(UnitDetailsActivity.this, "Please Wait...", false);

        ApiClient.downloadMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {

                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            Utils.downloadLocalPDF(strPdfFile, UnitDetailsActivity.this);
                        } else {
                            Utils.showInfoDialog(UnitDetailsActivity.this, "Pdf not found");
                        }
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : " + throwable.getMessage());
            }
        });
    }

    private void callShareMyShelfsApi(String strUserId, String shareTex) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(UnitDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserId);
        Log.e("strTypeRef :", " " + strTypeRef);
        Utils.showProgressDialog(UnitDetailsActivity.this, "Please Wait...", false);

        ApiClient.shareMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {

                        try {
                            if (strPdfLink != null && strPdfLink.length() > 0) {
                                String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                                String strMessage = strPdfLink; //" " + strBookName;
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                                intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                startActivity(Intent.createChooser(intent, shareData));
                            }
                        } catch (Exception e) {
                            Log.e("Exception Error", "Error---" + e.getMessage());
                        }

                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : " + throwable.getMessage());
            }
        });
    }

    private void callAddMyShelfApi(String strUserId, String strEdtRenamefile, boolean isShare) {
        Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + getUnitFormatName() + " /";
        MultipartBody.Part filePart = null;


        String strFileUrl = getExportUnitDetailsUrl(unitDetailsModel.getId());
        String uid = strUserId;
        String typeid = strUnitId;
        String filename = strEdtRenamefile;
        String type = Utils.TYPE_UNIT; // for unit detail
        String typeref = "1"; // For Files
        String count = "1";
        String fileType = Utils.TYPE_UNIT; // unit_detail need to add
        Log.e("fileType :", " " + fileType);
        String fileUrl = strFileUrl;
        Utils.showProgressDialog(UnitDetailsActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfsWithUrl(uid, null, typeid, type, typeref, filename, null, count, fileType, fileUrl, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    Utils.dismissProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().isStatus()) {
                            strPdfLink = response.body().getPdf_url();
                            if (isShare) {
                                callShareMyShelfsApi(strUserId, shareText);
                            } else {
                                Utils.showInfoDialog(UnitDetailsActivity.this, "Save successfully, you can find in My Reference sections.");
                            }
                        } else {
                            //Toast.makeText(getApplicationContext(), "Some Error Occured..", Toast.LENGTH_LONG).show();
                            Utils.showInfoDialog(UnitDetailsActivity.this, response.body().getMessage());
                        }
                    } else {
                        Log.e("error--", "ResultError--" + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(UnitDetailsActivity.this, "Something went wrong please try again later");

            }
        });

    }

}
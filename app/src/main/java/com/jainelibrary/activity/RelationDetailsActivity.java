package com.jainelibrary.activity;

import static com.jainelibrary.utils.Utils.TYPE_RELATION_BOOK_REFERENCE;
import static com.jainelibrary.utils.Utils.TYPE_RELATION_BOOK_REFERENCE;
import static com.jainelibrary.utils.Utils.getExportBiodataDetailsUrl;
import static com.jainelibrary.utils.Utils.getExportUnitRelationDetailsUrl;

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
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jainelibrary.BuildConfig;
import com.jainelibrary.Constantss;
import com.jainelibrary.R;
import com.jainelibrary.adapter.BiodataRefBookListAdapter;
import com.jainelibrary.adapter.CntMtrListAdapter;
import com.jainelibrary.adapter.RelationRefBookListAdapter;
import com.jainelibrary.adapter.SliderAdapter;
import com.jainelibrary.adapter.UnitEventListAdapter;
import com.jainelibrary.adapter.UnitRelationListAdapter;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.BiodataMemoryDetailsModel;
import com.jainelibrary.model.CntMtrModel;
import com.jainelibrary.model.ImageFileModel;
import com.jainelibrary.model.RelationDetailsModel;
import com.jainelibrary.model.RelationModel;
import com.jainelibrary.model.UnitDetailsModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.RelationDetailsResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelationDetailsActivity extends AppCompatActivity implements CntMtrListAdapter.EntityClickListener, RelationRefBookListAdapter.BookClickListener {
    private static final String TAG = RelationDetailsActivity.class.getSimpleName();

    Activity activity;
    UnitDetailsModel unitDetailsModel;
    TextView tvUnitName, tvUnitType, tvUnitSect, tvUnitStatus, tvTitlePgCount;
    LinearLayout llTitlePage;

    String strRelationId;
    TextView tvRelationType, tvRelationImageCount;
    RecyclerView rvReferenceList, rvCNTList, rvMTRList;
    RelationModel relationDetails;
    LinearLayout llCNTDetails, llMTRDetails, llImageDetails, llRelationReferenceList, llViewUnit;
    TextView tvRelationUnitType, tvRelationUnitStatus, tvRelationUnitSect, tvRelationUnitName;
    private String PackageName;
    private String strTitleName, strPreviousName;
    private String strUserId, strUnitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation_details);
        Log.e(TAG + ":" + strUserId, "KeywordSearchDetailsActivity");
        PackageName = RelationDetailsActivity.this.getPackageName();
        loadUiElements();
        onViewUnitClickListeners();
        declaration();
        setHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG + ":" + "Resume", "CustomBackPressed");
        Log.e(TAG + ":" + "CustomBackPressed", "--" + SharedPrefManager.getInstance(RelationDetailsActivity.this).getBooleanPreference("CustomBackPressed"));
        if (SharedPrefManager.getInstance(RelationDetailsActivity.this).getBooleanPreference("CustomBackPressed")) {
            SharedPrefManager.getInstance(RelationDetailsActivity.this).setBooleanPreference("CustomBackPressed", false);
            super.onBackPressed();
        }
        getRelationDetails();
    }

    private void declaration() {

        strRelationId = getIntent().getStringExtra("strRelationId");
        unitDetailsModel = (UnitDetailsModel) getIntent().getSerializableExtra("unitDetailsModel");

        strUserId = SharedPrefManager.getInstance(RelationDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        setUnitDetails();
        getRelationDetails();
    }

    private void loadUiElements() {
        llTitlePage = findViewById(R.id.llTitlePage);
        tvTitlePgCount = findViewById(R.id.txtTitlePgCount);
        tvUnitName = findViewById(R.id.tvUnitName);
        tvUnitType = findViewById(R.id.tvUnitType);
        tvUnitStatus = findViewById(R.id.tvUnitStatus);
        tvUnitSect = findViewById(R.id.tvUnitSect);


        // cvRelationDetails = findViewById(R.id.cvRelationDetails);
        //llRelationDetails = findViewById(R.id.llRelationU);
        tvRelationUnitType = findViewById(R.id.tvRelationUnitType);
        tvRelationType = findViewById(R.id.tvRelationType);
        tvRelationUnitName = findViewById(R.id.tvRelationUnitName);
        tvRelationUnitSect = findViewById(R.id.tvRelationUnitSect);
        tvRelationUnitStatus = findViewById(R.id.tvRelationUnitStatus);
        tvRelationImageCount = findViewById(R.id.tvRelationImageCount);
        rvReferenceList = findViewById(R.id.rvReferenceList);
        rvCNTList = findViewById(R.id.rvCNTList);
        rvMTRList = findViewById(R.id.rvMTRList);

        llImageDetails = findViewById(R.id.llImageDetails);
        llRelationReferenceList = findViewById(R.id.llRelationReferenceList);
        llCNTDetails = findViewById(R.id.llCNTDetails);
        llMTRDetails = findViewById(R.id.llMTRDetails);
        llExport = findViewById(R.id.llExport);
        llViewUnit = findViewById(R.id.llViewUnit);
        loadSliderUiElements();
    }

    LinearLayout llExport;

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
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        if (unitDetailsModel != null) {
            tvPageName.setText("Unit " + unitDetailsModel.getName());
        }

//        if (strFlag != null && strFlag.equalsIgnoreCase("3")){
//            tvPageName.setText(strBookName);
//        }else{
//            tvPageName.setText("Book Info");
//        }
    }

    private void setRelationHeader(RelationModel relationDetails) {
        View headerView = findViewById(R.id.header);
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("" + relationDetails.getUnit_name1() + " - " + relationDetails.getUnit_name2() + " | " + " " + relationDetails.getRelation_name());
    }


    private void setUnitDetails() {
        if (unitDetailsModel == null) {
            return;
        }
        tvUnitName.setText(unitDetailsModel.getName());
        tvUnitStatus.setText(unitDetailsModel.getStatus_name());
        tvUnitType.setText(unitDetailsModel.getType_name());
        tvUnitSect.setText(unitDetailsModel.getSect_name());

    }

    private void setRelationDetails(RelationModel relationDetails) {

        setRelationHeader(relationDetails);
        ImageView ivUnitType = findViewById(R.id.ivUnitType);

        //Log.e(TAG, ""+unitDetailsModel.getId() + "[==]"+relationDetails.getUnit_id1() + "==" + (Integer.parseInt(unitDetailsModel.getId()) == Integer.parseInt(relationDetails.getUnit_id1())));
        if (unitDetailsModel != null && Integer.parseInt(unitDetailsModel.getId()) == Integer.parseInt(relationDetails.getUnit_id1())) {
            tvUnitName.setText(relationDetails.getUnit_name1());
            tvUnitStatus.setText(relationDetails.getUnit_status_name1());
            tvUnitType.setText(relationDetails.getUnit_type_name1());
            Glide.with(RelationDetailsActivity.this)
                    .load(Utils.getUnitIcon(relationDetails.getUnit1_type_id()))
                    .fitCenter()
                    .into(ivUnitType);
            tvUnitSect.setText(relationDetails.getUnit_sect_name1());

            tvRelationType.setText(relationDetails.getUnit_relation_name2());
            tvRelationUnitName.setText(relationDetails.getUnit_name2());
            tvRelationUnitType.setText(relationDetails.getUnit_type_name2());
            tvRelationUnitStatus.setText(relationDetails.getUnit_status_name2());
            tvRelationUnitSect.setText(relationDetails.getUnit_sect_name2());
        } else {
            tvUnitName.setText(relationDetails.getUnit_name2());
            tvUnitStatus.setText(relationDetails.getUnit_status_name2());
            Glide.with(RelationDetailsActivity.this)
                    .load(Utils.getUnitIcon(relationDetails.getUnit2_type_id()))
                    .fitCenter()
                    .into(ivUnitType);
            tvUnitType.setText(relationDetails.getUnit_type_name2());
            tvUnitSect.setText(relationDetails.getUnit_sect_name2());

            tvRelationType.setText(relationDetails.getUnit_relation_name1());
            tvRelationUnitName.setText(relationDetails.getUnit_name1());
            tvRelationUnitType.setText(relationDetails.getUnit_type_name1());
            tvRelationUnitStatus.setText(relationDetails.getUnit_status_name1());
            tvRelationUnitSect.setText(relationDetails.getUnit_sect_name1());
        }
        if (relationDetails.getRelations_files().size() > 0) {
            tvRelationImageCount.setText("1/" + relationDetails.getRelations_files().size());
            llImageDetails.setVisibility(View.VISIBLE);
        } else {
            tvRelationImageCount.setText("");
        }

        setImageSlider(relationDetails.getRelations_files());

        if (relationDetails.getReference_books().size() > 0) {
            llRelationReferenceList.setVisibility(View.VISIBLE);
        }
        TextView tvReferenceListCount = findViewById(R.id.tvReferenceListCount);
        tvReferenceListCount.setText(String.valueOf(relationDetails.getReference_books().size()));

        setReferenceBookListData(relationDetails.getReference_books());
        TextView tvCNTCount = findViewById(R.id.tvCNTCount);
        tvCNTCount.setText(String.valueOf(relationDetails.getCnt_list().size()));
        setCntMtrListData(relationDetails.getCnt_list(), rvCNTList);
        TextView tvMTRCount = findViewById(R.id.tvMTRCount);
        tvMTRCount.setText(String.valueOf(relationDetails.getMtr_list().size()));

        setCntMtrListData(relationDetails.getMtr_list(), rvMTRList);
        if (relationDetails.getCnt_list().size() > 0) {
            llCNTDetails.setVisibility(View.VISIBLE);
        }
        if (relationDetails.getMtr_list().size() > 0) {
            llMTRDetails.setVisibility(View.VISIBLE);
        }
        onSaveClickListeners();
    }


    private LinearLayout llSliderControl;
    private SliderView sliderView;

    private ImageView sliderNext;
    private ImageView sliderPrev;
    private void loadSliderUiElements() {
        // initializing the slider view.
        sliderView = findViewById(R.id.slider);
        sliderPrev = (ImageView) findViewById(R.id.btnSliderPrev);
        sliderNext = (ImageView) findViewById(R.id.btnSliderNext);
        llSliderControl = (LinearLayout) findViewById(R.id.llSliderControl);
        sliderPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRelationImageCount.setText(sliderView.getCurrentPagePosition()+1 + "/" + sliderView.getSliderAdapter().getCount());
                sliderView.slideToPreviousPosition();
            }
        });

        // Next button click listener
        sliderNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderView.slideToNextPosition();
                tvRelationImageCount.setText(sliderView.getCurrentPagePosition()+1 + "/" + sliderView.getSliderAdapter().getCount());

            }
        });
    }
    private void setImageSlider(ArrayList<ImageFileModel> imageFiles) {
        // we are creating array list for storing our image urls.

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


        tvRelationImageCount.setText(sliderView.getCurrentPagePosition()+1 + "/" + sliderView.getSliderAdapter().getCount());
        if(imageFiles.size()<=1) {
            llSliderControl.setVisibility(View.GONE);
        }

    }


    public void onViewUnitClickListeners() {

        llViewUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unitDetailsModel != null && Integer.parseInt(unitDetailsModel.getId()) == Integer.parseInt(relationDetails.getUnit_id1())) {
                    showUnitDetails(relationDetails.getUnit_id2());
                } else {
                    showUnitDetails(relationDetails.getUnit_id1());
                }

            }
        });
    }


    private void showUnitDetails(String strUnitId) {

        Intent intent = new Intent(RelationDetailsActivity.this, UnitDetailsActivity.class);
        intent.putExtra("strUnitId", strUnitId);
        startActivity(intent);

    }


    private void setReferenceBookListData(ArrayList<RelationModel.ReferenceBook> referenceBooks) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RelationDetailsActivity.this);
        rvReferenceList.setHasFixedSize(true);
        rvReferenceList.setLayoutManager(linearLayoutManager);
        rvReferenceList.setVisibility(View.VISIBLE);
        RelationRefBookListAdapter rvReferenceListAdapter = new RelationRefBookListAdapter(RelationDetailsActivity.this, referenceBooks, this);
        rvReferenceList.setAdapter(rvReferenceListAdapter);
        if (referenceBooks.size() > 1) {
            ViewGroup.LayoutParams layoutParams = rvReferenceList.getLayoutParams();
            Log.e(TAG, "Re height" + layoutParams.height);
            layoutParams.height = (int) Math.round(layoutParams.height * 1.8);
            rvReferenceList.setLayoutParams(layoutParams);
        }

        //   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void setCntMtrListData(ArrayList<CntMtrModel> cntMtrModels, RecyclerView rvCntMtrList) {
        if (cntMtrModels == null) {
            return;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RelationDetailsActivity.this);
        rvCntMtrList.setHasFixedSize(true);
        rvCntMtrList.setLayoutManager(linearLayoutManager);
        rvCntMtrList.setVisibility(View.VISIBLE);
        CntMtrListAdapter rvCntMtrListAdapter = new CntMtrListAdapter(RelationDetailsActivity.this, cntMtrModels, this);
        rvCntMtrList.setAdapter(rvCntMtrListAdapter);

        int totalHeight = 0;
        if(cntMtrModels.size() > 0) {
            View item = linearLayoutManager.findViewByPosition(0);
            if(item !=null) {
                totalHeight += item.getHeight();
            }else {
                totalHeight += 700;
            }
        }
        ViewGroup.LayoutParams layoutParams = rvCntMtrList.getLayoutParams();
        if (cntMtrModels.size() > 1) {
            Log.e(TAG, "Re height" + layoutParams.height);
            layoutParams.height = (int) Math.round(totalHeight * 1.5);
            rvCntMtrList.setLayoutParams(layoutParams);
        } else if(cntMtrModels.size() == 1) {
            if(totalHeight > layoutParams.height) {
                layoutParams.height = (int) Math.round(totalHeight) + 10;
                rvCntMtrList.setLayoutParams(layoutParams);
            }
        }

        //   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void showBioDataMemoryDetails(String bmId) {
        Intent intent = new Intent(RelationDetailsActivity.this, RelationDetailsActivity.class);
        intent.putExtra("strRelationId", bmId);

        // intent.putExtra("unitDetailsModel", unitDetailsModel);
        startActivity(intent);

    }

    private void showRelationDetails(String relationId) {
        Intent intent = new Intent(RelationDetailsActivity.this, RelationDetailsActivity.class);
        intent.putExtra("strRelationId", relationId);
        // intent.putExtra("unitDetailsModel", unitDetailsModel);
        startActivity(intent);
    }

    @Override
    public void onCntMtrEntityClick(ArrayList<CntMtrModel> cntMtrModels, int position) {

        String strEntityId = cntMtrModels.get(position).getLink_entity_id();
        String strEntityType = cntMtrModels.get(position).getLink_entity_type();
        Log.i(TAG, "Selected data" + strEntityType);
        if (strEntityType == "relation") {
            showRelationDetails(strEntityId);
        } else if (strEntityType == "biodata_memory") {
            showBioDataMemoryDetails(strEntityId);
        }
//        showUnitDetails(strUnitId, unitModels.get(position));

    }

    private void getRelationDetails() {
        if (!ConnectionManager.checkInternetConnection(RelationDetailsActivity.this)) {
            Utils.showInfoDialog(RelationDetailsActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(RelationDetailsActivity.this, "Please Wait...", false);
        ApiClient.getRelationDetails(strUserId, strRelationId, new Callback<RelationDetailsResModel>() {
            @Override
            public void onResponse(Call<RelationDetailsResModel> call, retrofit2.Response<RelationDetailsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        RelationModel relationModel = response.body().getDetails();
                        relationDetails  = relationModel;
                        setRelationDetails(relationModel);
                    } else {
                        //Toast.makeText(UnitDetailsActivity.this, "Year Type not found", Toast.LENGTH_SHORT).show();
                        Utils.showInfoDialog(RelationDetailsActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RelationDetailsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e(TAG, "error:theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public void onBookClick(View view, RelationModel.ReferenceBook referenceBook, int position) {
        String s = view.getTag().toString();
        String[] str = s.split(",");
        String str1 = str[0];
        Log.e("str1", str1);
        String str2 = str[1];
        Log.e("str2", str2);
        String str3 = str[2];
        Log.e("str3", str3);
        Log.e("stringAll", str.toString());


        BookListResModel.BookDetailsModel model = new BookListResModel.BookDetailsModel();

        model.setBook_id(referenceBook.getBook_id());
        model.setPdf_page_no(referenceBook.getBook_pdf_page_no());
        model.setBook_name(referenceBook.getBook_name());
        model.setAuthor_name(referenceBook.getAuthor_name());
        model.setTranslator(referenceBook.getTranslator());
        model.setEditor_name(referenceBook.getEditor_name());
        Intent i = new Intent(RelationDetailsActivity.this, ReferencePageActivity.class);
        i.putExtra("pageno", str[2]);
        i.putExtra("bookImage", model.getBook_image());
        i.putExtra("pdfpage", str[0]);
        i.putExtra("bookid", model.getBook_id());
        i.putExtra("bookname", model.getBook_name());
        i.putExtra("typeName", relationDetails.getUnit_name1() + "-" + relationDetails.getUnit_name2() + " - " + relationDetails.getRelation_name() +" (" + relationDetails.getNumber() + ")");
        i.putExtra("refrenceTypeId", relationDetails.getId());
        i.putExtra("typeValue", relationDetails.getUnit_name1() + "-" + relationDetails.getUnit_name2() + " - " + relationDetails.getRelation_name() +" (" + relationDetails.getNumber() + ")");

        //i.putExtra("ApiTypeId", 4);
        i.putExtra("moduleNo", TYPE_RELATION_BOOK_REFERENCE);
        i.putExtra("model", model);
        i.putExtra("type_id",TYPE_RELATION_BOOK_REFERENCE);
        i.putExtra("from","RelationDetailsActivity");
        startActivity(i);
        //int cPosition = Integer.parseInt(childPosition);
        Log.e("Book page click -- ", "theme---" + referenceBook.getBook_name());
        /*if (bookLists != null && bookLists.size() > 0) {

        }*/
    }


    private void onSaveClickListeners() {
        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(RelationDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    getShareDialog();
                } else {
                    askLogin();
                }
            }
        });
    }

    private BottomSheetDialog bottomSheetDialog;

    private String getExportPdfFileName() {
        return relationDetails.getNumber() +"_" + relationDetails.getUnit_name1() + "_" + relationDetails.getRelation_name() + "_" + relationDetails.getUnit_name2();

    }
    private String getExportPDFFileName() {
        return "JainRefLibrary_Unit_" + getExportPdfFileName();
    }
    Keyboard mKeyboard;
    CustomKeyboardView mKeyboardView;

    public void getShareDialog() {
        bottomSheetDialog = new BottomSheetDialog(RelationDetailsActivity.this, R.style.BottomSheetDialogTheme);
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
        String strLanguage = SharedPrefManager.getInstance(RelationDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.setFocusable(true);
        strUserId = SharedPrefManager.getInstance(RelationDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
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
                /*Util.hideKeyBoard(RelationDetailsActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(RelationDetailsActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, RelationDetailsActivity.this, strLanguage, bottomSheetDialog, null);
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
                callRelationDetailExportPdf(relationDetails.getId(), strNewPDFile);
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

    private void callRelationDetailExportPdf(String strRelationId, String downloadFilePath) {
        if (!ConnectionManager.checkInternetConnection(RelationDetailsActivity.this)) {
            Utils.showInfoDialog(RelationDetailsActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(RelationDetailsActivity.this, "Please Wait...", false);
        ApiClient.getUnitRelationDetailPdfFile(strRelationId, new Callback<ResponseBody>() {
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
                        Constantss.FILE_NAME_PDF = "JainRefLibrary /" + getExportPdfFileName() + " /";
                        callDownloadMyShelfsApi(strUserId, downloadFilePath);
                    } else {
                        Utils.showInfoDialog(RelationDetailsActivity.this, "Pdf data not download");
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

            String strEdtRenamefile =  getExportPDFFileName();

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
        Utils.showLoginDialogForResult(RelationDetailsActivity.this, 1);
    }

    String strTypeRef;
    private void callDownloadMyShelfsApi(String strUserId, String strPdfFile) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(RelationDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserId);
        Log.e("strTypeRef :", " " + strTypeRef);
        Utils.showProgressDialog(RelationDetailsActivity.this, "Please Wait...", false);

        ApiClient.downloadMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {

                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            Utils.downloadLocalPDF(strPdfFile, RelationDetailsActivity.this);
                        } else {
                            Utils.showInfoDialog(RelationDetailsActivity.this, "Pdf not found");
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

    String  shareText, strPdfLink;

    private void callShareMyShelfsApi(String strUserId, String shareTex) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(RelationDetailsActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserId);
        Log.e("strTypeRef :", " " + strTypeRef);
        Utils.showProgressDialog(RelationDetailsActivity.this, "Please Wait...", false);

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
        Constantss.FILE_NAME_PDF = "JainRefLibrary" + " / " + getExportPdfFileName() + " /";

        String strFileUrl = getExportUnitRelationDetailsUrl(relationDetails.getId());
        String uid = strUserId;
        String typeid = relationDetails.getId();
        String filename = strEdtRenamefile;
        String type = Utils.TYPE_RELATION; // for unit detail
        String typeref = "1"; // For Files
        String count = "1";
        String fileType = Utils.TYPE_RELATION; // unit_detail need to add
        Log.e("fileType :", " " + fileType);
        String fileUrl = strFileUrl;
        Utils.showProgressDialog(RelationDetailsActivity.this, "Please Wait...", false);
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
                                Utils.showInfoDialog(RelationDetailsActivity.this, "Save successfully, you can find in My Reference sections.");
                            }
                        } else {
                            //Toast.makeText(getApplicationContext(), "Some Error Occured..", Toast.LENGTH_LONG).show();
                            Utils.showInfoDialog(RelationDetailsActivity.this, response.body().getMessage());
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
                Utils.showInfoDialog(RelationDetailsActivity.this, "Something went wrong please try again later");

            }
        });

    }

}
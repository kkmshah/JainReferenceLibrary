package com.jainelibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;
import com.jainelibrary.Constantss;
import com.jainelibrary.R;
import com.jainelibrary.adapter.UnitParentRelationTreeAdapter;
import com.jainelibrary.adapter.viewHolder.UnitParentRelationTreeNode;
import com.jainelibrary.adapter.viewHolder.UnitRelationNodeViewHolder;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.ParamparaFilterDataResModel;
import com.jainelibrary.model.UnitDetailsModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.RelationTypeListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.retrofitResModel.UnitRelationChartDataResModel;
import com.jainelibrary.treeview.TreeNode;
import com.jainelibrary.treeview.TreeViewAdapter;
import com.jainelibrary.treeview.TreeViewHolderFactory;
import com.jainelibrary.utils.CaptureRecyclerView;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitRelationTreeActivity extends AppCompatActivity implements UnitRelationNodeViewHolder.OnViewUnitClickListener, UnitParentRelationTreeAdapter.UnitParentNodeClickListener {
    private static final String TAG = UnitRelationTreeActivity.class.getSimpleName();
    public boolean showRelationParent = false;
    Activity activity;
    UnitDetailsModel unitDetailsModel;
    TextView tvUnitName, tvUnitType, tvUnitSect, tvUnitStatus, tvTitlePgCount;
    LinearLayout llTitlePage;
    String headerText = "";
    ArrayList<ParamparaFilterDataResModel.RelationType> relationTypeList = new ArrayList<ParamparaFilterDataResModel.RelationType>();
    Spinner spnRelationType, spnRelationDirection;
    LinearLayout llUnitRelationParent, llUnitRelation, llExport;
    RecyclerView rvUnitRelationTree;
    RecyclerView rvUnitParentRelationTree;
    int maxDepth = 50;
    ArrayList<String[]> viewTreeUnitHistory = new ArrayList<String[]>();
    UnitParentRelationTreeAdapter unitParentRelationTreeAdapter;
    Handler handler;
    Keyboard mKeyboard;
    CustomKeyboardView mKeyboardView;
    String strTypeRef;
    String shareText, strPdfLink;
    private String PackageName;
    private String strUserId, strUnitId;
    private HorizontalScrollView hsvUnitParentRelationTree;
    private int spnRelationTypeId = 0;
    private TreeViewAdapter treeViewAdapter;
    private ParamparaFilterDataResModel.RelationType selectedRelationType;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_relations_tree);
        Log.e(TAG + ":" + strUserId, "KeywordSearchDetailsActivity");
        PackageName = UnitRelationTreeActivity.this.getPackageName();
        handler = new Handler();
        loadUiElements();
        declaration();
        headerText = unitDetailsModel.getName();
        setHeader();

        onSaveClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG + ":" + "Resume", "CustomBackPressed");
        Log.e(TAG + ":" + "CustomBackPressed", "--" + SharedPrefManager.getInstance(UnitRelationTreeActivity.this).getBooleanPreference("CustomBackPressed"));
        if (SharedPrefManager.getInstance(UnitRelationTreeActivity.this).getBooleanPreference("CustomBackPressed")) {
            SharedPrefManager.getInstance(UnitRelationTreeActivity.this).setBooleanPreference("CustomBackPressed", false);
            super.onBackPressed();
        }
    }

    private void declaration() {

        strUnitId = getIntent().getStringExtra("strUnitId");
        unitDetailsModel = (UnitDetailsModel) getIntent().getSerializableExtra("unitDetailsModel");
        relationTypeList = (ArrayList<ParamparaFilterDataResModel.RelationType>) getIntent().getSerializableExtra("relationTypeList");

        strUserId = SharedPrefManager.getInstance(UnitRelationTreeActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (relationTypeList == null || relationTypeList.size() == 0) {
            getRelationTypeList();
        } else {
            Log.i(TAG, "Relation node" + relationTypeList.get(0).toString());
            spnRelationTypeId = relationTypeList.get(0).getId();
            setRelationTypeSpinnerData(relationTypeList);
        }

    }

    private void loadUiElements() {
        llTitlePage = findViewById(R.id.llTitlePage);
        tvTitlePgCount = findViewById(R.id.txtTitlePgCount);
        tvUnitName = findViewById(R.id.tvUnitName);
        tvUnitType = findViewById(R.id.tvUnitType);
        tvUnitStatus = findViewById(R.id.tvUnitStatus);
        tvUnitSect = findViewById(R.id.tvUnitSect);
        spnRelationType = findViewById(R.id.spnRelationType);
        spnRelationDirection = findViewById(R.id.spnRelationDirection);
        rvUnitRelationTree = findViewById(R.id.rvUnitRelationTree);
        llUnitRelationParent = findViewById(R.id.llUnitRelationParent);
        llUnitRelation = findViewById(R.id.llUnitRelation);
        llExport = findViewById(R.id.llExport);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UnitRelationTreeActivity.this);
        // rvBiodataList.setHasFixedSize(true);
        rvUnitRelationTree.setLayoutManager(linearLayoutManager);
        rvUnitRelationTree.setVisibility(View.VISIBLE);
        rvUnitRelationTree.setNestedScrollingEnabled(false);

        rvUnitParentRelationTree = findViewById(R.id.rvUnitParentRelationTree);
        LinearLayoutManager linearParentLayoutManager = new LinearLayoutManager(UnitRelationTreeActivity.this);
        // rvBiodataList.setHasFixedSize(true);
        rvUnitParentRelationTree.setLayoutManager(linearParentLayoutManager);
        rvUnitParentRelationTree.setVisibility(View.VISIBLE);
        rvUnitParentRelationTree.setNestedScrollingEnabled(false);
        setRelationDirection();


    }

    private String getRelationVisibleType() {
        if (selectedRelationType == null) {
            return "";
        }
        String name = selectedRelationType.getName();
        String[] arrOfNameStr = name.split("-");
        if (showRelationParent) {
            return arrOfNameStr[0];
        } else {
            return arrOfNameStr[1];
        }

    }

    private void setRelationDirectionSpinnerData(ParamparaFilterDataResModel.RelationType relationType) {
        ArrayList<RelationTypeNode> relationNodeDirList = new ArrayList<RelationTypeNode>();


        if (relationType != null) {
            String name = relationType.getName();
            String[] arrOfNameStr = name.split("-");
            //strRelationTypeList.add(strYear);
            relationNodeDirList.add(new RelationTypeNode(relationType, " -> " + arrOfNameStr[1], false));
            relationNodeDirList.add(new RelationTypeNode(relationType, " <- " + arrOfNameStr[0], true));
        }
        selectedRelationType = relationType;

        ArrayList<String> strRelationTypeDirList = new ArrayList<>();
        if (relationNodeDirList != null && relationNodeDirList.size() > 0) {
            for (int i = 0; i < relationNodeDirList.size(); i++) {
                String strName = relationNodeDirList.get(i).getName();
                strRelationTypeDirList.add(strName);
            }
        }
        if (strRelationTypeDirList.size() <= 0) {
            return;
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(UnitRelationTreeActivity.this, android.R.layout.simple_spinner_dropdown_item, strRelationTypeDirList);


        spnRelationDirection.setAdapter(adp);

        spnRelationDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {
                showRelationParent = relationNodeDirList.get(position).getIsParent();
                setRelationDirection();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


    }

    private void setRelationDirection() {

        if (showRelationParent) {
            llUnitRelation.setVisibility(View.GONE);
            llUnitRelationParent.setVisibility(View.VISIBLE);
            // Scroll to the last position smoothly
            if (rvUnitParentRelationTree.getAdapter() != null) {
                rvUnitParentRelationTree.post(new Runnable() {
                    @Override
                    public void run() {
                        rvUnitParentRelationTree.smoothScrollToPosition(rvUnitParentRelationTree.getAdapter().getItemCount() - 1);
                    }
                });
            }


        } else {
            llUnitRelationParent.setVisibility(View.GONE);
            llUnitRelation.setVisibility(View.VISIBLE);
        }
    }

    private void setRelationTypeSpinnerData(ArrayList<ParamparaFilterDataResModel.RelationType> typeList) {

        ArrayList<String> strRelationTypeList = new ArrayList<>();

        if (typeList != null && typeList.size() > 0) {
            for (int i = 0; i < typeList.size(); i++) {
                String strName = typeList.get(i).getName();
                strRelationTypeList.add(strName);
            }
        }
        if (strRelationTypeList.size() <= 0) {
            return;
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(UnitRelationTreeActivity.this, android.R.layout.simple_spinner_dropdown_item, strRelationTypeList);
        spnRelationType.setAdapter(adp);

        spnRelationTypeId = typeList.get(0).getId();
        setRelationDirectionSpinnerData(typeList.get(0));
        spnRelationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {
                //relationNodeDirList.get(position);
                spnRelationTypeId = typeList.get(position).getId();
                setRelationDirectionSpinnerData(typeList.get(position));
                getUnitRelationChartData(spnRelationTypeId);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        getUnitRelationChartData(spnRelationTypeId);
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
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText(headerText);

    }

    private void setUnitParentRelationTreeChartNodesData(UnitRelationChartDataResModel.UnitParentRelation rootUnitRelation) {


        LinkedList<UnitParentRelationTreeNode> parentNodeList = new LinkedList<UnitParentRelationTreeNode>();
        int level = 0;
        parentNodeList.addFirst(new UnitParentRelationTreeNode(level, rootUnitRelation));

        int maxParentDepth = 10;
        setParentTreeNode(rootUnitRelation, level + 1, parentNodeList, maxParentDepth);
        unitParentRelationTreeAdapter = new UnitParentRelationTreeAdapter(UnitRelationTreeActivity.this, parentNodeList, this);
        rvUnitParentRelationTree.setAdapter(unitParentRelationTreeAdapter);
        if (showRelationParent) {
            // Scroll to the last position smoothly
            rvUnitParentRelationTree.post(new Runnable() {
                @Override
                public void run() {
                    rvUnitParentRelationTree.smoothScrollToPosition(unitParentRelationTreeAdapter.getItemCount() - 1);
                }
            });
        }

    }

    private void setParentTreeNode(UnitRelationChartDataResModel.UnitParentRelation unitRelation, int level, LinkedList<UnitParentRelationTreeNode> parentNodeList, int maxParentDepth) {

        if (maxParentDepth < level) {
            return;
        }
        for (int i = 0; i < unitRelation.getParents().size(); i++) {
            UnitRelationChartDataResModel.UnitParentRelation childUnitRelation = unitRelation.getParents().get(i);
            parentNodeList.addFirst(new UnitParentRelationTreeNode(level + 1, childUnitRelation));
            setParentTreeNode(childUnitRelation, level + 1, parentNodeList, maxParentDepth);
        }
    }

    private void setUnitRelationTreeChartNodesData(UnitRelationChartDataResModel.RootUnitRelation rootUnitRelation) {
        TreeViewHolderFactory factory = ((v, layout) -> {
            UnitRelationNodeViewHolder holder = new UnitRelationNodeViewHolder(v);
            holder.setTreeNodeViewClickListener(this);
            return holder;
        });

        treeViewAdapter = new TreeViewAdapter(factory);
        // treeViewAdapter.setTreeNodeClickListener();
        rvUnitRelationTree.setAdapter(treeViewAdapter);

        List<TreeNode> rootUnitNodeList = new ArrayList<>();
        TreeNode rootUnitNode = new TreeNode(rootUnitRelation, R.layout.tv_unit_relation_node);

        setChildTreeNode(rootUnitNode, rootUnitRelation);
        rootUnitNodeList.add(rootUnitNode);
        treeViewAdapter.updateTreeNodes(rootUnitNodeList);

    }

    private void setChildTreeNode(TreeNode node, UnitRelationChartDataResModel.UnitRelation unitRelation) {

        for (int i = 0; i < unitRelation.getChildren().size(); i++) {
            UnitRelationChartDataResModel.UnitRelation childUnitRelation = unitRelation.getChildren().get(i);
            TreeNode childNode = new TreeNode(childUnitRelation, R.layout.tv_unit_relation_node);
            node.addChild(childNode);
            setChildTreeNode(childNode, childUnitRelation);
        }
    }

    private void getUnitRelationChartData(int spnRelationTypeId) {
        if (!ConnectionManager.checkInternetConnection(UnitRelationTreeActivity.this)) {
            Utils.showInfoDialog(UnitRelationTreeActivity.this, "Please check internet connection");
            return;
        }
        if (spnRelationTypeId == 0) {
            return;
        }
        Utils.showProgressDialog(UnitRelationTreeActivity.this, "Please Wait...", false);
        ApiClient.getUnitRelationChartData(strUserId, strUnitId, String.valueOf(spnRelationTypeId), maxDepth, new Callback<UnitRelationChartDataResModel>() {
            @Override
            public void onResponse(Call<UnitRelationChartDataResModel> call, retrofit2.Response<UnitRelationChartDataResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        UnitRelationChartDataResModel.RootUnitRelation rootUnitRelation = response.body().getRoot_node();
                        setUnitRelationTreeChartNodesData(rootUnitRelation);
                        setUnitParentRelationTreeChartNodesData(response.body().getRote_parent_node());
                    } else {
                        //Toast.makeText(UnitDetailsActivity.this, "Year Type not found", Toast.LENGTH_SHORT).show();
                        Utils.showInfoDialog(UnitRelationTreeActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UnitRelationChartDataResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e(TAG, "error:theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void getRelationTypeList() {
        if (!ConnectionManager.checkInternetConnection(UnitRelationTreeActivity.this)) {
            Utils.showInfoDialog(UnitRelationTreeActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(UnitRelationTreeActivity.this, "Please Wait...", false);
        ApiClient.getRelationTypes(new Callback<RelationTypeListResModel>() {
            @Override
            public void onResponse(Call<RelationTypeListResModel> call, retrofit2.Response<RelationTypeListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        relationTypeList = response.body().getData();
                        setRelationTypeSpinnerData(relationTypeList);
                    }
                }
            }

            @Override
            public void onFailure(Call<RelationTypeListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e(TAG, "error:theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onTreeNodeViewClick(UnitRelationChartDataResModel.UnitRelation unitRelation, View view) {
        Log.e(TAG, "onTreeNodeViewClick" + unitRelation.getId());
        Intent intent = new Intent(UnitRelationTreeActivity.this, UnitDetailsActivity.class);
        intent.putExtra("strUnitId", unitRelation.getId());
        startActivity(intent);
    }

    public void onTreeNodeViewTreeClick(UnitRelationChartDataResModel.UnitRelation unitRelation, View view) {
        Log.e(TAG, "onTreeNodeViewClick" + unitRelation.getId());
        viewTreeUnitHistory.add(new String[]{strUnitId, headerText});
        strUnitId = unitRelation.getId();
        headerText = unitRelation.getName();
        setHeader();
        getUnitRelationChartData(spnRelationTypeId);
    }

    public void onUnitParentNodeClick(UnitParentRelationTreeNode node) {
        Log.e(TAG, "onTreeNodeViewClick" + node.getValue().getId());
        Intent intent = new Intent(UnitRelationTreeActivity.this, UnitDetailsActivity.class);
        intent.putExtra("strUnitId", node.getValue().getId());
        startActivity(intent);
    }

    public void onUnitParentNodeTreeClick(UnitParentRelationTreeNode node) {
        Log.e(TAG, "onTreeNodeViewClick" + node.getValue().getId());
        viewTreeUnitHistory.add(new String[]{strUnitId, headerText});
        headerText = node.getValue().getName();
        strUnitId = node.getValue().getId();
        setHeader();
        getUnitRelationChartData(spnRelationTypeId);
    }

    @Override
    public void onBackPressed() {
        if (viewTreeUnitHistory.size() == 0) {
            super.onBackPressed();
        } else {
            int index = viewTreeUnitHistory.size() - 1;
            strUnitId = viewTreeUnitHistory.get(index)[0];
            headerText = viewTreeUnitHistory.get(index)[1];
            viewTreeUnitHistory.remove(index);
            setHeader();
            getUnitRelationChartData(spnRelationTypeId);
            // Delete last element by passing index
        }

    }

    public void createTreePDFFile(String strEdtRenamefile, Boolean isShare) {
        Utils.showProgressDialog(UnitRelationTreeActivity.this, "Creating a file, Please Wait...", false);

        int totalHeight = 0;
        int itemWidth = 0;
        int itemHeight = 0;
        int maxLevel = 0;
        int offsetX = 0;
        int startCaptureDelay = 10;
        RecyclerView recyclerView;
        if (showRelationParent) {
            recyclerView = rvUnitParentRelationTree;
            for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                if (unitParentRelationTreeAdapter.getTreeNodes().get(i).getLevel() > maxLevel) {
                    maxLevel = 1;
                }
            }
            offsetX = maxLevel * 50;
            RecyclerView.LayoutManager layoutManager = rvUnitRelationTree.getLayoutManager();
            if (layoutManager != null && layoutManager.getChildCount() > 0) {
                View itemView = layoutManager.getChildAt(0);
                itemWidth = itemView.getWidth();
                itemHeight = itemView.getHeight();

                int itemCount = recyclerView.getAdapter().getItemCount() + 1;
                totalHeight = itemCount * itemHeight;
                // Log or use totalHeight as needed
                Log.d("RecyclerView", "Total Height: " + itemCount + "[][]" + itemHeight + "==" + totalHeight);
            }

        } else {
            recyclerView = rvUnitRelationTree;
            // treeViewAdapter.expandAll();
            for (int i = 0; i < treeViewAdapter.getItemCount(); i++) {
                if (treeViewAdapter.getTreeNodes().get(i).getLevel() > maxLevel) {
                    maxLevel = 1;
                }
            }
            offsetX = maxLevel * 50;

            RecyclerView.LayoutManager layoutManager = rvUnitRelationTree.getLayoutManager();
            if (layoutManager != null && layoutManager.getChildCount() > 0) {
                View itemView = layoutManager.getChildAt(0);
                itemWidth = itemView.getWidth();
                itemHeight = itemView.getHeight();
                int itemCount = layoutManager.getItemCount();
                totalHeight = itemCount * itemHeight;
                // Log or use totalHeight as needed
                Log.d("RecyclerView", "Total Height: " + itemCount + "[][]" + itemHeight + "==" + totalHeight);
            }
            startCaptureDelay = 200;
        }
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        int prevHeight = layoutParams.height;
        int prevWidth = layoutParams.width;
        Log.e(TAG, "Re height" + layoutParams.height);
        layoutParams.height = (int) totalHeight;
        layoutParams.width = (int) (offsetX + itemWidth);
        recyclerView.setLayoutParams(layoutParams);
        int maxImageHeight = 20 * itemHeight;
        final int captureDelay = startCaptureDelay;

        recyclerView.post(new Runnable() {
            @Override
            public void run() {

                CaptureRecyclerView captureUnitRelationTree = new CaptureRecyclerView(recyclerView);
                ArrayList<String> strPdfFiles = captureUnitRelationTree.captureImages(
                        maxImageHeight, Color.LTGRAY, Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath());
                layoutParams.height = prevHeight;
                layoutParams.width = prevWidth;
                recyclerView.setLayoutParams(layoutParams);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Utils.dismissProgressDialog();
                                callAddMyShelfApi(strPdfFiles, strUserId, getRelationVisibleType(), strEdtRenamefile, isShare);
                            }
                        }, 500);
                    }
                }, captureDelay);
            }
        });

    }

    private void onSaveClickListeners() {
        llExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(UnitRelationTreeActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    getShareDialog();
                    // callUnitDetailExportPdf(unitDetailsModel.getId());
                } else {
                    askLogin();
                }
            }
        });
    }

    private String getExportPdfFileName() {
        String timeStamp = new SimpleDateFormat("yyyy-mm-dd_HH-mm-ss", Locale.getDefault()).format(new Date());

        return headerText.replace(" ", "_") + "_" + getRelationVisibleType().trim() + "_Tree_" + timeStamp;

    }

    private String getExportPDFFileName() {
        return "JainRefLibrary_Unit_" + getExportPdfFileName();
    }

    public void getShareDialog() {
        bottomSheetDialog = new BottomSheetDialog(UnitRelationTreeActivity.this, R.style.BottomSheetDialogTheme);
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
        String strLanguage = SharedPrefManager.getInstance(UnitRelationTreeActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        edtRenameFile.setFocusable(true);
        strUserId = SharedPrefManager.getInstance(UnitRelationTreeActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
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
                /*Util.hideKeyBoard(UnitRelationTreeActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(UnitRelationTreeActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, UnitRelationTreeActivity.this, strLanguage, bottomSheetDialog, null);
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
                createTreePDFFile(strEdtRenamefile, true);

            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                String strEdtRenamefile = edtRenameFile.getText().toString();
                Constantss.FILE_NAME = strEdtRenamefile;
                String strNewPDFile = Utils.getMediaStorageDir(getApplicationContext()) + File.separator + strEdtRenamefile + ".pdf";
                //callBiodataDetailExportPdf(biodataDetails.getId(), strNewPDFile);
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
                createTreePDFFile(strEdtRenamefile, false);
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

    private void askLogin() {
        Utils.showLoginDialogForResult(UnitRelationTreeActivity.this, 1);
    }

    private void callShareMyShelfsApi(String strUserId, String shareTex) {
        if (!ConnectionManager.checkInternetConnection(this)) {
            Utils.showInfoDialog(UnitRelationTreeActivity.this, "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", "" + strUserId);
        Log.e("strTypeRef :", " " + strTypeRef);
        Utils.showProgressDialog(UnitRelationTreeActivity.this, "Please Wait...", false);

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

    private void callAddMyShelfApi(ArrayList<String> strPdfFiles, String strUserId, String strTypeName, String strEdtRenamefile, boolean isShare) {
        Utils.showProgressDialog(UnitRelationTreeActivity.this, "Please Wait...", false);
        MultipartBody.Part filePart = null;


        List<MultipartBody.Part> fileParts = new ArrayList<>();
        for (String strPdfFile : strPdfFiles) {

            if (strPdfFile != null && strPdfFile.length() > 0) {
                File mFile = new File(strPdfFile);
                if (mFile.exists()) {
                    filePart = MultipartBody.Part.createFormData("pdf_file[]", mFile.getName(), RequestBody.create(MediaType.parse("pdf/*"), mFile));

                    //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    //MultipartBody.Part filePart = MultipartBody.Part.createFormData("files[]", file.getName(), requestFile);
                    fileParts.add(filePart);
                }
            }

        }
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUserId);
        RequestBody bookid = RequestBody.create(MediaType.parse("text/*"), "");

        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "1");
        RequestBody typeid = RequestBody.create(MediaType.parse("text/*"), strUnitId);

        RequestBody typeName = RequestBody.create(MediaType.parse("text/*"), strTypeName);


        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), strEdtRenamefile);
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), "1");
        RequestBody count = RequestBody.create(MediaType.parse("text/*"), "1");
        RequestBody fileType = RequestBody.create(MediaType.parse("text/*"), Utils.TYPE_UNIT_RELATION_TREE);
        Log.e("fileType :", " " + fileType);
        Utils.showProgressDialog(UnitRelationTreeActivity.this, "Please Wait...", false);
        ApiClient.addMyShelfsMultipleFiles(uid, bookid, typeid, type, typeref, filename, typeName, count, fileType, fileParts, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {

                if (response.isSuccessful()) {
                    for (String strPdfFile : strPdfFiles) {
                        if (strPdfFile != null && strPdfFile.length() > 0) {
                            File mFile = new File(strPdfFile);
                            if (mFile.exists()) {
                                mFile.delete();
                            }
                        }
                    }
                    Utils.dismissProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().isStatus()) {
                            strPdfLink = response.body().getPdf_url();
                            if (isShare) {
                                callShareMyShelfsApi(strUserId, shareText);
                            } else {
                                Utils.showInfoDialog(UnitRelationTreeActivity.this, "Save successfully, you can find in My Reference sections.");
                            }
                        } else {
                            //Toast.makeText(getApplicationContext(), "Some Error Occured..", Toast.LENGTH_LONG).show();
                            Utils.showInfoDialog(UnitRelationTreeActivity.this, response.body().getMessage());
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
                for (String strPdfFile : strPdfFiles) {
                    if (strPdfFile != null && strPdfFile.length() > 0) {
                        File mFile = new File(strPdfFile);
                        if (mFile.exists()) {
                            mFile.delete();
                        }
                    }
                }
                Utils.showInfoDialog(activity, "Something went wrong please try again later");

            }
        });

    }

    class RelationTypeNode {
        ParamparaFilterDataResModel.RelationType relationType;

        String name;

        Boolean parent = false;

        RelationTypeNode(ParamparaFilterDataResModel.RelationType relationType, String name, Boolean parent) {
            this.relationType = relationType;
            this.name = name;
            this.parent = parent;
        }

        public ParamparaFilterDataResModel.RelationType getRelationType() {
            return relationType;
        }

        public String getName() {
            return name;
        }

        public Boolean getIsParent() {
            return parent;
        }
    }


}
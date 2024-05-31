package com.jainelibrary.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.adapter.FilterAdapter;
import com.jainelibrary.adapter.FilterValuesAdapter;
import com.jainelibrary.model.MainCatModel;
import com.jainelibrary.model.SubCatModel;
import com.jainelibrary.multicheck.MultiCheckActivity;

import java.util.ArrayList;

public class FilterDialogFragment extends DialogFragment implements FilterAdapter.onSelectListner, FilterValuesAdapter.onSubCatSelectListner {
    public TextView tvClear;
    private RecyclerView recyclerViewMain;
    private RecyclerView recyclerViewSub;
    private FilterAdapter filterAdapterMain;
    private FilterValuesAdapter filterAdapterSub;
    private ArrayList<MainCatModel> mainCatList = new ArrayList<>();
    private ArrayList<SubCatModel> subCatList = new ArrayList<>();
    private Button btnCancel, btnApply, btn_select_books;
    private String strCategory;
    private String strBookCount;
    private int checkedCount = 0;
    private String catName;

    public static FilterDialogFragment newInstance(int myIndex) {
        FilterDialogFragment yourDialogFragment = new FilterDialogFragment();
        return yourDialogFragment;
    }
    setSelectedBooks selectedBooks;

    public interface setSelectedBooks{
        public void selectedBook(String cat,String books);
    }
    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        selectedBooks = (setSelectedBooks) a;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //read the int from args
        //    View view = inflater.inflate(R.layout.filter_view, null);
        View view = inflater.inflate(R.layout.filter_view, container, false);
        //here read the different parts of your layout i.e :
        //tv = (TextView) view.findViewById(R.id.yourTextView);
        //tv.setText("some text")
        initializeControls(view);
        return view;
    }

    private void initializeControls(View view) {
        tvClear = (view).findViewById(R.id.tvClear);
        recyclerViewMain = (view).findViewById(R.id.filterRV);
        recyclerViewSub = (view).findViewById(R.id.filterValuesRV);
        btn_select_books = (view).findViewById(R.id.btn_select_books);
        btnApply = (view).findViewById(R.id.button_apply);
        btnCancel = (view).findViewById(R.id.button_cancel);
        loadData(true);
        filterAdapterMain = new FilterAdapter(getActivity(), mainCatList, this);
        recyclerViewMain.setHasFixedSize(true);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMain.setAdapter(filterAdapterMain);

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(false);
                tvClear.setTextColor(Color.parseColor("#cbcbcb"));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("subCatList--", "" + subCatList.size());
                Log.e("checkedCount--", "" + checkedCount);
                Log.e("catName--", "" + catName);
                selectedBooks.selectedBook(strCategory,strBookCount);
                dismiss();
                /*Intent i = new Intent(getActivity(), SearchActivity.class);
                i.putExtra("category", strCategory);
                i.putExtra("bookcount", strBookCount);
                startActivity(i);*/
            }
        });
        btn_select_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent i = new Intent(getActivity(), MultiCheckActivity.class);
               // i.putExtra("KID",strkeyword);
                startActivity(i);
            }
        });
    }


    private void loadData(boolean isFirstTime) {
        mainCatList = new ArrayList<>();
        subCatList = new ArrayList<>();

        MainCatModel mFilter = new MainCatModel();

        subCatList = new ArrayList<>();
        mFilter = new MainCatModel();
        mFilter.setCatName("Dictionary");
        for (int i = 1; i < 4; i++) {
            SubCatModel subCatModel = new SubCatModel();
            subCatModel.setCatId("");
            subCatModel.setCatName("Dictionary " + i);
            subCatList.add(subCatModel);
        }
        mFilter.setSubCatList(subCatList);
        mainCatList.add(mFilter);
        subCatList = new ArrayList<>();
        mFilter = new MainCatModel();
        mFilter.setCatName("History");
        for (int i = 1; i < 5; i++) {
            SubCatModel subCatModel = new SubCatModel();
            subCatModel.setCatId("");
            subCatModel.setCatName("History " + i);
            subCatList.add(subCatModel);
        }
        mFilter.setSubCatList(subCatList);
        mainCatList.add(mFilter);

        subCatList = new ArrayList<>();
        mFilter = new MainCatModel();
        mFilter.setCatName("TatvaGyan");
        for (int i = 1; i < 2; i++) {
            SubCatModel subCatModel = new SubCatModel();
            subCatModel.setCatId("");
            subCatModel.setCatName("TatvaGyan " + i);
            subCatList.add(subCatModel);
        }
        mFilter.setSubCatList(subCatList);
        mainCatList.add(mFilter);

        subCatList = new ArrayList<>();
        mFilter = new MainCatModel();
        mFilter.setCatName("Story");
        for (int i = 1; i < 7; i++) {
            SubCatModel subCatModel = new SubCatModel();
            subCatModel.setCatId("");
            subCatModel.setCatName("Story " + i);
            subCatList.add(subCatModel);
        }
        mFilter.setSubCatList(subCatList);
        mainCatList.add(mFilter);

        subCatList = new ArrayList<>();
        mFilter = new MainCatModel();
        mFilter.setCatName("Question-Answer");
        for (int i = 1; i < 6; i++) {
            SubCatModel subCatModel = new SubCatModel();
            subCatModel.setCatId("");
            subCatModel.setCatName("Question-Answer " + i);
            subCatList.add(subCatModel);
        }
        mFilter.setSubCatList(subCatList);
        mainCatList.add(mFilter);

        selectCount = 0;
       /* if (!isFirstTime) {
            if (mainCatList.get(selectedPos).getSubCatList() != null && mainCatList.get(selectedPos).getSubCatList().size() > 0) {
                filterAdapterSub = new FilterValuesAdapter(getActivity(), mainCatList.get(selectedPos).getSubCatList(), this);
                recyclerViewSub.setHasFixedSize(true);
                recyclerViewSub.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewSub.setAdapter(filterAdapterSub);
            } else {
                return;
            }
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }*/
    }

    @Override
    public void onSelect(ArrayList<MainCatModel> mainCategoryList, int pos) {
        subCatList = mainCategoryList.get(pos).getSubCatList();
        if (subCatList != null && subCatList.size() > 0) {
            strBookCount = String.valueOf(subCatList.size()); }
        catName = mainCategoryList.get(pos).getCatName();
        if (catName != null && catName.length() > 0) { strCategory = catName;
        }
        checkedCount = 0;
        for (int i = 0; i < mainCategoryList.size(); i++) {
            MainCatModel catModel = mainCategoryList.get(i);
            if (catModel.isSelected()) {
                checkedCount++;
            }
        }
        /*filterAdapterSub = new FilterValuesAdapter(getActivity(), mainCatList.get(pos).getSubCatList(), this);
        recyclerViewSub.setHasFixedSize(true);
        recyclerViewSub.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSub.setAdapter(filterAdapterSub);*/
    }
    boolean isClearEnable = false;
    int selectCount = 0, selectedPos = 0;
    @Override
    public void onSubSelect(boolean isSelected) {
        if (isSelected) {
            selectCount++;
        } else {
            selectCount--;
        }
        if (selectCount > 0) {
            tvClear.setTextColor(Color.BLACK);
        } else {
            tvClear.setTextColor(Color.parseColor("#cbcbcb"));
        }
    }
}

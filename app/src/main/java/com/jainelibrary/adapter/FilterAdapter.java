package com.jainelibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.MainCatModel;
import com.jainelibrary.model.SubCatModel;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MainCatModel> mainCategoryList = new ArrayList<>();
    private int selectedPos = 0;
    private onSelectListner onSelectListner;
    ArrayList<SubCatModel> subCatList = new ArrayList<>();

    public FilterAdapter(Context context, ArrayList<MainCatModel> mainCategoryList, onSelectListner activity) {
        this.context = context;
        this.mainCategoryList = mainCategoryList;
        this.onSelectListner = (onSelectListner) activity;
    }

    public interface onSelectListner {
        public void onSelect(ArrayList<MainCatModel> mainCategoryList, int pos);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_item, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        final MainCatModel tmpFilter = mainCategoryList.get(position);

        subCatList = tmpFilter.getSubCatList();
        myViewHolder.tvName.setText(tmpFilter.getCatName());


        Log.e("subCatList---", "" + subCatList.size());
        if (mainCategoryList != null && mainCategoryList.size() > 1) {
            myViewHolder.tvBooksCount.setText(" " + mainCategoryList.size());
            myViewHolder.tvBooks.setText("Books");
        } else {
            myViewHolder.tvBooksCount.setText(" " + mainCategoryList.size());
            myViewHolder.tvBooks.setText("Book");
        }


       /* if (selectedPos == position) {
            myViewHolder.llmain.setBackgroundColor(Color.WHITE);
        } else {
            myViewHolder.llmain.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }*/

        myViewHolder.cbValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myViewHolder.cbValue.isChecked()) {
                    mainCategoryList.get(position).setSelected(true);
                } else {
                    mainCategoryList.get(position).setSelected(false);
                }
                onSelectListner.onSelect(mainCategoryList, position);
            }
        });

        /*myViewHolder.llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // myViewHolder.llmain.setBackgroundColor(Color.WHITE);
                onSelectListner.onSelect(mainCategoryList, position);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        Log.e("size", "" + mainCategoryList.size());
        return mainCategoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View container;
        TextView tvName;
        RelativeLayout llmain;
        TextView tvBooksCount, tvBooks;

        AppCompatCheckBox cbValue;

        public MyViewHolder(View view) {
            super(view);
            container = view;
            tvName = view.findViewById(R.id.tvName);
            cbValue = view.findViewById(R.id.cbValue);
            llmain = view.findViewById(R.id.llmain);
            tvBooksCount = view.findViewById(R.id.tvBooksCount);
            tvBooks = view.findViewById(R.id.tvBooks);
        }
    }

}

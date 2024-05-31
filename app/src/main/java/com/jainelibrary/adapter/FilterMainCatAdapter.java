package com.jainelibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.CategoryResModel;

import java.util.ArrayList;

public class FilterMainCatAdapter extends RecyclerView.Adapter<FilterMainCatAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CategoryResModel.CategoryModel> filterValues = new ArrayList<>();
    private onSubCatSelectListner onSelectListner;
    private int selectedPosition = 0;

    public FilterMainCatAdapter(Context context, ArrayList<CategoryResModel.CategoryModel> filterList, onSubCatSelectListner onSelectListner) {
        this.context = context;
        this.filterValues = filterList;
        this.onSelectListner = onSelectListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_category_item, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        final CategoryResModel.CategoryModel tmpFilter = filterValues.get(position);
        myViewHolder.tvName.setText(tmpFilter.getName());

        if(selectedPosition== position){
            myViewHolder.llmain.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            myViewHolder.llmain.setBackgroundColor(Color.parseColor("#f2f2f2"));
        }
        myViewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                onSelectListner.onCategorySelect(filterValues, position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return filterValues.size();
    }

    public interface onSubCatSelectListner {
        void onCategorySelect(ArrayList<CategoryResModel.CategoryModel> filterValues, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        LinearLayout llmain;

        public MyViewHolder(View view) {
            super(view);
            llmain = view.findViewById(R.id.llmain);
            tvName = view.findViewById(R.id.tvCategory);
        }
    }

}

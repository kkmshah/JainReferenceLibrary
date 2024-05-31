package com.jainelibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.Filter;
import com.jainelibrary.model.SubCatModel;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class FilterValuesAdapter extends RecyclerView.Adapter<FilterValuesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<SubCatModel> filterValues;
    private onSubCatSelectListner onSelectListner;

    public FilterValuesAdapter(Context context,  ArrayList<SubCatModel> filterValues, onSubCatSelectListner activity) {
        this.context = context;
        this.filterValues = filterValues;
        this.onSelectListner = (onSubCatSelectListner)activity;
    }
    public interface onSubCatSelectListner{
        public void onSubSelect(boolean isSelected);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_value_item, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        final SubCatModel tmpFilter = filterValues.get(position);
       /* myViewHolder.value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selected = tmpFilter.getSelected();
                if(myViewHolder.value.isChecked()) {
                    selected.add(tmpFilter.getValues().get(position));
                    tmpFilter.setSelected(selected);
                } else {
                    selected.remove(tmpFilter.getValues().get(position));
                    tmpFilter.setSelected(selected);
                }
            //    Preferences.filters.put(filterIndex, tmpFilter);
            }
        });*/
        myViewHolder.tvName.setText(tmpFilter.getCatName());
        myViewHolder.value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListner.onSubSelect(myViewHolder.value.isChecked());
            }
        });
        /*if(tmpFilter.getSelected().contains(tmpFilter.getValues().get(position))) {
            myViewHolder.value.setChecked(true);
        }*/
    }

    @Override
    public int getItemCount() {
        return filterValues.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View container;
        CheckBox value;
        TextView tvName;

        public MyViewHolder(View view) {
            super(view);
            container = view;
            value = view.findViewById(R.id.cbValue);
            tvName = view.findViewById(R.id.tvName);
        }
    }

}

package com.jainelibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.ShlokGranthSutraResModel;

import java.util.ArrayList;
import java.util.List;

public class ShlokSearchDetailsListAdapter extends RecyclerView.Adapter<ShlokSearchDetailsListAdapter.ViewHolder> implements Filterable {

    private static final String TAG = ShlokSearchDetailsListAdapter.class.getSimpleName();
    Context mContext;
    SearchInterfaceListener searchInterfaceListener;
    ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> granthList;
    ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> granthFullList;

    public ShlokSearchDetailsListAdapter(Context mContext, ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> granthList, SearchInterfaceListener searchInterfaceListener) {
        this.mContext = mContext;
        this.granthList = granthList;
        this.searchInterfaceListener = searchInterfaceListener;
        granthFullList = new ArrayList<>(granthList);
    }

    @Override
    public ShlokSearchDetailsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_shlok_search_details, parent, false);
        return new ShlokSearchDetailsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShlokSearchDetailsListAdapter.ViewHolder holder, final int position) {
        String strShlokName = granthList.get(position).getName();
        String strBookCount = granthList.get(position).getBook_count();
        holder.textViewName.setText(strShlokName);
        holder.tvBookCount.setText(strBookCount);
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(granthList.get(position), position);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return sutraFilter;
    }

    private Filter sutraFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(granthFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ShlokGranthSutraResModel.ShlokGranthSutraModel item : granthFullList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            granthList.clear();
            granthList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface SearchInterfaceListener {
        void onDetailsClick(ShlokGranthSutraResModel.ShlokGranthSutraModel grathModel, int position);
    }

    public void newData(ArrayList<ShlokGranthSutraResModel.ShlokGranthSutraModel> list) {
        Log.e(TAG, "set list --" + list.size());
        if (granthList != null) {
            granthList = new ArrayList<>();
            granthList = list;
            Log.e(TAG, "granthList in list --" + granthList.size());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "granthList list --" + granthList.size());
        return granthList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, tvBookCount;
        CardView cvList;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            tvBookCount = (TextView) itemView.findViewById(R.id.tvBookCount);
            cvList = itemView.findViewById(R.id.cvList);
        }
    }
}


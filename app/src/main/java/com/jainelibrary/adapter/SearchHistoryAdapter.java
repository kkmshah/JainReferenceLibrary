package com.jainelibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.extraModel.SearchHistoryModel;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    Context mContext;
    ArrayList<SearchHistoryModel> mSearchHistoryList = new ArrayList<>();
    SearchHistoryInterfaceListener searchInterfaceListener;

    public SearchHistoryAdapter(Context mContext, ArrayList<SearchHistoryModel> mSearchHistoryList, SearchHistoryInterfaceListener searchInterfaceListener) {
        this.mContext = mContext;
        this.mSearchHistoryList = mSearchHistoryList;
        this.searchInterfaceListener = searchInterfaceListener;
    }

    @Override
    public SearchHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_keyword, parent, false);
        return new SearchHistoryAdapter.ViewHolder(v);
    }

    public interface SearchHistoryInterfaceListener {
        void onSelectSearchKeyword(SearchHistoryModel SearchHistoryModel, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.ViewHolder holder, final int position) {
        String strSeachKeyword = mSearchHistoryList.get(position).getStrSearchKeywordName();
        holder.tvKeywordName.setText(strSeachKeyword);
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onSelectSearchKeyword(mSearchHistoryList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvKeywordName;
        CardView cvList;

        ViewHolder(View itemView) {
            super(itemView);
            tvKeywordName = (TextView) itemView.findViewById(R.id.tvKeywordName);
            cvList = itemView.findViewById(R.id.cvList);
        }
    }
}

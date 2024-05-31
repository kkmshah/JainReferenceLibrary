package com.jainelibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.ShlokSearchResModel;

import java.util.ArrayList;

public class ShlokSearchAdapter extends RecyclerView.Adapter<ShlokSearchAdapter.ViewHolder> {

    Context mContext;
    SearchInterfaceListener searchInterfaceListener;
    ArrayList<ShlokSearchResModel.ShlokResModel> keywordList = new ArrayList<>();

    public ShlokSearchAdapter(Context mContext, ArrayList<ShlokSearchResModel.ShlokResModel> keywordList, SearchInterfaceListener searchInterfaceListener) {
        this.mContext = mContext;
        this.keywordList = keywordList;
        this.searchInterfaceListener = searchInterfaceListener;
    }

    @Override
    public ShlokSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_shlok_search, parent, false);
        return new ShlokSearchAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShlokSearchAdapter.ViewHolder holder, final int position) {
        String strKeyWordName = keywordList.get(position).getName();
        String strSutraCount = keywordList.get(position).getSutra_count();
        holder.textViewName.setText(strKeyWordName);
        holder.tvShlokCount.setText(strSutraCount);
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(keywordList.get(position), position);
            }
        });

    }

    public interface SearchInterfaceListener {
        void onDetailsClick(ShlokSearchResModel.ShlokResModel ShlokSearchResModel, int position);
    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, tvShlokCount;
        CardView cvList;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            cvList = itemView.findViewById(R.id.cvList);
            tvShlokCount = itemView.findViewById(R.id.tvShlokCount);
        }
    }
}


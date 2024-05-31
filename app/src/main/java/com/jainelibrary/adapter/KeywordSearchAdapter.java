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
import com.jainelibrary.model.KeywordModel;

import java.util.ArrayList;

public class KeywordSearchAdapter extends RecyclerView.Adapter<KeywordSearchAdapter.ViewHolder> {
    Context mContext;
    ArrayList<KeywordModel> keywordList = new ArrayList<>();
    SearchInterfaceListener searchInterfaceListener;

    public KeywordSearchAdapter(Context mContext, ArrayList<KeywordModel> keywordList, KeywordSearchAdapter.SearchInterfaceListener searchInterfaceListener) {
        this.mContext = mContext;
        this.keywordList = keywordList;
        this.searchInterfaceListener = searchInterfaceListener;
    }

    @Override
    public KeywordSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search, parent, false);
        return new KeywordSearchAdapter.ViewHolder(v);
    }

    public interface SearchInterfaceListener {
        void onDetailsClick(KeywordModel keywordModel, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordSearchAdapter.ViewHolder holder, final int position) {
        String strBookName = keywordList.get(position).getName();
        holder.tvBookName.setText(strBookName);
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(keywordList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBookName;
        CardView cvList;

        ViewHolder(View itemView) {
            super(itemView);
            tvBookName = (TextView) itemView.findViewById(R.id.tvBookName);
            cvList = itemView.findViewById(R.id.cvList);
        }
    }
}


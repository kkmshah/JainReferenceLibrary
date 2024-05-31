package com.jainelibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.IndexSearchResModel;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

public class IndexSearchAdapter extends RecyclerView.Adapter<IndexSearchAdapter.ViewHolder> {
    Context mContext;
    SearchInterfaceListener searchInterfaceListener;
    ArrayList<IndexSearchResModel.IndexResModel> mIndexList;
    ArrayList<IndexSearchResModel.IndexResModel> mIndexListfilter;

    public IndexSearchAdapter(Context mContext,
                              ArrayList<IndexSearchResModel.IndexResModel> keywordList,
                              SearchInterfaceListener searchInterfaceListener
    ) {
        this.mContext = mContext;
        this.mIndexList = keywordList;
        this.searchInterfaceListener = searchInterfaceListener;
        this.mIndexListfilter = keywordList;
    }

    public void filterList(ArrayList<IndexSearchResModel.IndexResModel> filterdNames) {
        this.mIndexList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public IndexSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_index_search, parent, false);
        return new IndexSearchAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IndexSearchAdapter.ViewHolder holder, final int position) {
        String strKeyWordName = mIndexList.get(position).getName();
        Log.e("count", mIndexList.get(position).getCount());
        Log.e("name", mIndexList.get(position).getName());
        holder.tvDetails.setText(mIndexList.get(position).getName());
        holder.tvCount.setText(mIndexList.get(position).getCount());
        String strOtherName = "";
        String strFinalWord = "";

        String strIndexName = Utils.getColoredSpanned(strKeyWordName, String.valueOf(mContext.getResources().getColor(R.color.blue)));
        strOtherName = Utils.getColoredSpanned(strOtherName, String.valueOf(mContext.getResources().getColor(R.color.Grey_15)));

        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(mIndexList.get(position), position);

            }
        });


    }


    public interface SearchInterfaceListener {
        void onDetailsClick(IndexSearchResModel.IndexResModel mIndexKeywordModel, int position);

        void onContactSelected(IndexSearchResModel.IndexResModel mIndexKeywordModel);
    }

    @Override
    public int getItemCount() {
        return mIndexList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCount;
        TextView tvDetails;
        CardView cvList;
        ImageView ivBookImage;

        ViewHolder(View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvDetails = (TextView) itemView.findViewById(R.id.tvIndex);
            cvList = itemView.findViewById(R.id.cvList);
            ivBookImage = itemView.findViewById(R.id.ivBookImage);

            cvList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchInterfaceListener.onContactSelected(mIndexListfilter.get(getAdapterPosition()));

                }
            });
        }
    }
}

package com.jainelibrary.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.BookListResModel;

import java.util.ArrayList;

public class BookDetailsAppendixAdapter extends RecyclerView.Adapter<BookDetailsAppendixAdapter.ViewHolder> {

    Context mContext;
    AppendixListener mAppendixListener;
    ArrayList<BookListResModel.BookDetailsModel.BookAppendixModel> appendixResModelList = new ArrayList<>();
    int colorCode;

    public BookDetailsAppendixAdapter(Context mContext, ArrayList<BookListResModel.BookDetailsModel.BookAppendixModel> appendixResModelList, AppendixListener mAppendixListener, int strFlag) {
        this.mContext = mContext;
        this.appendixResModelList = appendixResModelList;
        this.mAppendixListener = mAppendixListener;
        this.colorCode = strFlag;
    }

    @Override
    public BookDetailsAppendixAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_appendix_book_details, parent, false);
        return new BookDetailsAppendixAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookDetailsAppendixAdapter.ViewHolder holder, final int position) {
        String strTitle = appendixResModelList.get(position).getApendix_name();
        String strCount = appendixResModelList.get(position).getPdf_pages();
       /* if (colorCode != 0) {
            holder.tvQl.setTextColor(colorCode);
        }*/

        int pos = position + 1;
        holder.tvQl.setText("Quick Link :" + pos);

     //   holder.llTitlePage.setBackgroundTintList(ColorStateList.valueOf(colorCode));

        if (strTitle != null) {
            holder.tvTitlePage.setText(strTitle);
        }

        if (strTitle != null) {
            holder.txtTitlePgCount.setText(strCount);
        }

        holder.tvTitlePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppendixListener.onDetailsClick(appendixResModelList.get(position), position);
            }
        });

    }

    public interface AppendixListener {
        void onDetailsClick(BookListResModel.BookDetailsModel.BookAppendixModel mBookApendixModel, int position);
    }

    @Override
    public int getItemCount() {
        return appendixResModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQl, tvTitlePage, txtTitlePgCount;
        LinearLayout llTitlePage;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitlePage = (TextView) itemView.findViewById(R.id.tvTitlePage);
            txtTitlePgCount = (TextView) itemView.findViewById(R.id.txtTitlePgCount);
            tvQl = (TextView) itemView.findViewById(R.id.tvQL);
            llTitlePage =  itemView.findViewById(R.id.llTitlePage);
        }
    }
}

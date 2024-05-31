package com.jainelibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.model.PdfStoreListResModel.PdfListModel;
import com.jainelibrary.model.PdfStoreListResModel.PdfListModel;

import java.util.ArrayList;

public class SearchBookStoreAdapter extends RecyclerView.Adapter<SearchBookStoreAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PdfStoreListResModel.PdfListModel> filesModelsList = new ArrayList<>();
    private OnImageClickListener onImageClickListener;

    public SearchBookStoreAdapter(Context context, ArrayList<PdfStoreListResModel.PdfListModel> filesModels, OnImageClickListener onImageClickListener) {
        this.context = context;
        this.filesModelsList = filesModels;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public SearchBookStoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_book_store, parent, false);
        return new SearchBookStoreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookStoreAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvBookName.setText(filesModelsList.get(position).getBook_name());

        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickListener.onMenuClick(filesModelsList.get(position), position, holder.ivMenu);
            }
        });
    }

    public void newData(ArrayList<PdfStoreListResModel.PdfListModel> list) {
        if (filesModelsList != null) {
            filesModelsList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filesModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivMenu;
        TextView tvBookName;
        CardView cvList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.textViewName);
            cvList = itemView.findViewById(R.id.cvList);
            ivMenu = itemView.findViewById(R.id.ivMenu);
        }
    }

    public interface OnImageClickListener {
        void onMenuClick(PdfStoreListResModel.PdfListModel filesModel, int position, ImageView ivMenu);
    }
}


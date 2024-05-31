package com.jainelibrary.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyReferenceBooksGalleryAdapter extends RecyclerView.Adapter<MyReferenceBooksGalleryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyShelfResModel.MyShelfModel> filesModelsList = new ArrayList<>();
    private OnImageClickListener onImageClickListener;

    public MyReferenceBooksGalleryAdapter(Context context, ArrayList<MyShelfResModel.MyShelfModel> filesModels, OnImageClickListener onImageClickListener) {
        this.context = context;
        this.filesModelsList = filesModels;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public MyReferenceBooksGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        String strKeyword = filesModelsList.get(position).getType_name();
        String strBookname = filesModelsList.get(position).getBook_name();
        String strAuthorName = filesModelsList.get(position).getAuthor_name();
        String strPublisherName = filesModelsList.get(position).getPrakashak_name();

        String strCDate = filesModelsList.get(position).getcDate();

        if (strBookname != null && strBookname.length() > 0) {
            holder.tvBookName.setText(Html.fromHtml(strBookname));
        }

        if (strAuthorName != null && strAuthorName.length() > 0) {
            holder.tvAuthorName.setText(Html.fromHtml(strAuthorName));
        }

        if (strPublisherName != null && strPublisherName.length() > 0) {
            holder.tvPublisherName.setText(Html.fromHtml(strPublisherName));
        }

        if (strCDate != null && strCDate.length() > 0) {
            holder.tvCDate.setText(strCDate);
        } else {
            holder.tvCDate.setVisibility(View.GONE);
        }

        String strPdfUrl = filesModelsList.get(position).getBookPdfUrl();
        String strBookImage = filesModelsList.get(position).getBook_image();
        if (strPdfUrl != null && strPdfUrl.length() > 0 && strPdfUrl.contains("pdf")) {
            Picasso.get().load(R.mipmap.pdf_file).into(holder.ivImage);
        }
        /*if (filesModelsList.get(position).getPage_no() != null && filesModelsList.get(position).getPage_no().size() > 0) {
            String strUrl = filesModelsList.get(position).getPage_no().get(0).getUrl();
            Picasso.get().load(strUrl).into(holder.ivImage);
        } else {
            Picasso.get().load(R.drawable.noimage).into(holder.ivImage);
        }*/
        if (strBookImage != null && strBookImage.length() > 0) {
            Picasso.get().load(strBookImage).into(holder.ivImage);
        }else{
            Picasso.get().load(R.drawable.noimage).into(holder.ivImage);
        }
        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickListener.onMenuGalleryClick(filesModelsList, position, holder.ivMenu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filesModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivMenu;
        TextView tvBookName, tvAuthorName, tvPublisherName, tvCDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuthorName = itemView.findViewById(R.id.tvOtherName);
            tvPublisherName = itemView.findViewById(R.id.tvPublisherName);
            tvCDate = itemView.findViewById(R.id.tvCDate);
        }
    }
    public interface OnImageClickListener {
        void onMenuGalleryClick(ArrayList<MyShelfResModel.MyShelfModel> filesModelsList, int position, ImageView ivMenu);
    }
}

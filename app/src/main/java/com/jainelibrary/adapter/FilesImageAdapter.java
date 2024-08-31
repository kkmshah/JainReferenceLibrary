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

public class FilesImageAdapter extends RecyclerView.Adapter<FilesImageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyShelfResModel.MyShelfModel> filesModelsList = new ArrayList<>();
    private OnImageClickListener onImageClickListener;

    public FilesImageAdapter(Context context, ArrayList<MyShelfResModel.MyShelfModel> filesModels, OnImageClickListener onImageClickListener) {
        this.context = context;
        this.filesModelsList = filesModels;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public FilesImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_file_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String strKeyword = filesModelsList.get(position).getType_name();
        String strBookname = filesModelsList.get(position).getBook_name();
        String strAutherName = filesModelsList.get(position).getAuthor_name();
        String strPublisherName = filesModelsList.get(position).getPrakashak_name();
        String strBookImage = filesModelsList.get(position).getBook_image();
        String strOtherName = "";
        String strFinalWord = "";
        String isNotesAdded = filesModelsList.get(position).getIs_note_added();

        strOtherName = Utils.getColoredSpanned(strOtherName, String.valueOf(context.getResources().getColor(R.color.black)));
        String strCDate = filesModelsList.get(position).getcDate();
        if (strCDate != null && strCDate.length() > 0) {
            holder.tvCDate.setVisibility(View.VISIBLE);
            holder.tvCDate.setText("" + Html.fromHtml(strCDate));
        } else {
            holder.tvCDate.setVisibility(View.GONE);
        }

        Log.e("isNotesAdded ---->>",""+isNotesAdded);

        if(isNotesAdded.equals("1")){
            holder.tvNoteInfo.setVisibility(View.VISIBLE);
            holder.tvNoteInfo.setText("Notes added");
        } else {
            holder.tvNoteInfo.setVisibility(View.GONE);
        }

        if (strKeyword != null && strKeyword.length() > 0) {
            strOtherName = strKeyword;
        }
        if (strBookname != null && strBookname.length() > 0) {
            strOtherName = strOtherName + " , " + strBookname;
            holder.tvBookName.setText(Html.fromHtml(strBookname));
        }

        if (strKeyword != null && strKeyword.length() > 0) {
            holder.tvKeywordName.setText(Html.fromHtml(strKeyword));
            holder.tvKeywordName.setVisibility(View.VISIBLE);
        }else{
            holder.tvKeywordName.setVisibility(View.GONE);
        }

        String strPdfPageSize = null;
        int pageSize = filesModelsList.get(position).getPage_no().size();

        if (pageSize != 0 && pageSize > 0) {
            if (pageSize > 1) {
                strPdfPageSize = "• " + pageSize + " Pages";
            } else {
                strPdfPageSize = "• " + pageSize + " Page";
            }
        }

        strPdfPageSize = Utils.getColoredSpanned(strPdfPageSize, String.valueOf(context.getResources().getColor(R.color.Grey_15)));
        if (strPdfPageSize != null && strPdfPageSize.length() > 0) {
            strFinalWord = strOtherName + " , " + strPdfPageSize;
        }

        if (strFinalWord != null && strFinalWord.length() > 0) {

        }
        String strPdfUrl = filesModelsList.get(position).getUrl();

        if (strPdfUrl != null && strPdfUrl.length() > 0 && strPdfUrl.contains("pdf")) {
            Picasso.get().load(R.mipmap.pdf_file).into(holder.ivImage);
        }

        if (strBookImage != null && strBookImage.length() > 0) {
            Picasso.get().load(strBookImage).into(holder.ivImage);
            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickListener.onZoomClick(filesModelsList.get(position));
                }
            });
        }else{
            Picasso.get().load(R.drawable.noimage).into(holder.ivImage);
        }

       /* if (filesModelsList.get(position).getPage_no() != null && filesModelsList.get(position).getPage_no().size() > 0) {
            String strUrl = filesModelsList.get(position).getPage_no().get(0).getUrl();
            Picasso.get().load(strUrl).into(holder.ivImage);
        } else {
            Picasso.get().load(R.drawable.noimage).into(holder.ivImage);
        }*/


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
        TextView tvBookName, tvKeywordName,tvNoteInfo,tvCDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvKeywordName = itemView.findViewById(R.id.tvKeywordName);
            tvCDate = itemView.findViewById(R.id.tvCDate);
            tvNoteInfo = itemView.findViewById(R.id.tvNoteInfo);
        }
    }
    public interface OnImageClickListener {
        void onMenuGalleryClick(ArrayList<MyShelfResModel.MyShelfModel> filesModelsList, int position, ImageView ivMenu);
        void onZoomClick(MyShelfResModel.MyShelfModel filesModelsList);
    }
}

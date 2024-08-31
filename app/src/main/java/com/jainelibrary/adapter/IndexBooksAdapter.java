package com.jainelibrary.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.BookDetailsModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.IndexSearchResModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IndexBooksAdapter extends RecyclerView.Adapter<IndexBooksAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BookListResModel.BookDetailsModel> indexBookList = new ArrayList<>();
    private OnBookClickListeners clickListener;

    public IndexBooksAdapter(Context context, ArrayList<BookListResModel.BookDetailsModel> filesModels, OnBookClickListeners clickListener) {
        this.context = context;
        this.indexBookList = filesModels;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public IndexBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_reference_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String strIndexName = indexBookList.get(position).getIndex_name();
        String strBookname = indexBookList.get(position).getBook_name();
        String strAutherName = indexBookList.get(position).getAuthor_name();
        String strPublisherName = indexBookList.get(position).getPublisher_name();
        String strEditorName = indexBookList.get(position).getEditor_name();

        /*String strCDate = indexBookList.get(position).getcDate();

        Log.e("cDate ","cDate " +strCDate);
        if (strCDate != null && strCDate.length() > 0) {
            holder.tvCDate.setVisibility(View.GONE);
            holder.tvCDate.setText("" + strCDate);
        } else {
            holder.tvCDate.setVisibility(View.GONE);
        }*/

        String strOtherName = "";
        if (strBookname != null && strBookname.length() > 0) {
            strOtherName =  strBookname;
            if (strAutherName != null && strAutherName.length() > 0) {
                strOtherName = strOtherName + ", " + strAutherName;
                //holder.tvAuthor.setText(strAutherName);
            }
            if (strPublisherName != null && strPublisherName.length() > 0) {
                strOtherName = strOtherName + ", " + strPublisherName;
                //holder.tvPublisher.setText(strPublisherName);
            }
            if (strEditorName != null && strEditorName.length() > 0) {
                strOtherName = strOtherName + ", " + strEditorName;
                //holder.tvEditor.setText(strEditorName);
            }
            holder.tvBookName.setText(strOtherName);
        }






        if (strIndexName != null && strIndexName.length() > 0) {
            holder.tvIndexName.setText(strIndexName);
        }

        /*if (strKeyword != null && strKeyword.length() > 0) {
            Log.e("strKeyword",strKeyword);
            holder.tvKewordname.setText(Html.fromHtml(strKeyword));
        }*/
        //String strPdfUrl = indexBookList.get(position).getBookPdfUrl();
        String strBookImage = indexBookList.get(position).getBook_image();
        if (strBookImage != null && strBookImage.length() > 0) {
            Picasso.get().load(strBookImage).into(holder.ivImage);
        }else{
            Picasso.get().load(R.drawable.noimage).into(holder.ivImage);
        }

        holder.llIndexBookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onBookClick(indexBookList, position, holder.ivMenu);
            }
        });
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onImageClick(indexBookList, position, holder.ivMenu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return indexBookList.size();
    }

    public void filterList(ArrayList<BookListResModel.BookDetailsModel> filterdNames) {
        this.indexBookList = filterdNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivMenu;
        TextView tvIndexName, tvBookName, tvAuthor, tvEditor, tvPublisher;
        LinearLayout llIndexBook, llIndexBookDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvIndexName = itemView.findViewById(R.id.tvIndexName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            llIndexBook = itemView.findViewById(R.id.llIndexBook);
            llIndexBookDetail = itemView.findViewById(R.id.llIndexBookDetail);
            tvEditor = itemView.findViewById(R.id.tvEditor);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
        }
    }
    public interface OnBookClickListeners {
        void onBookClick(ArrayList<BookListResModel.BookDetailsModel> filesModelsList, int position, ImageView ivMenu);
        void onImageClick(ArrayList<BookListResModel.BookDetailsModel> filesModelsList, int position, ImageView ivMenu);
    }
}

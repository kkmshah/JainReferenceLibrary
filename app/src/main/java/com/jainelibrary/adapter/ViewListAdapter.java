package com.jainelibrary.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.PdfStoreListResModel;

import java.util.ArrayList;

public class ViewListAdapter extends RecyclerView.Adapter<ViewListAdapter.ViewHolder> {

    Context mContext;
    ViewListInteface ViewListInteface;
    ArrayList<PdfStoreListResModel.PdfListModel> viewBookList = new ArrayList<>();

    public ViewListAdapter(Context mContext, ArrayList<PdfStoreListResModel.PdfListModel> viewBookList, ViewListInteface ViewListInteface) {
        this.mContext = mContext;
        this.viewBookList = viewBookList;
        this.ViewListInteface = ViewListInteface;
    }

    @Override
    public ViewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_book_store, parent, false);
        return new ViewListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewListAdapter.ViewHolder holder, final int position) {
        String strKeyWordName = viewBookList.get(position).getBook_name();
        String strAutherName = viewBookList.get(position).getAuthor_name();
        String strPublisherName = viewBookList.get(position).getPublisher_name();

        String strOtherName = "";

        if (strKeyWordName != null && strKeyWordName.length() > 0) {
            strOtherName = strKeyWordName;
        }

        if (strAutherName != null && strAutherName.length() > 0) {
            if (strAutherName.equalsIgnoreCase("0")) {

            } else {
                strAutherName = ", " + strAutherName /*+"&#91;" + "ले" + "&#93; "*/;
                strOtherName = strOtherName + strAutherName;
            }
        }

        if (strPublisherName != null && strPublisherName.length() > 0) {
            if (strPublisherName.equalsIgnoreCase("0")) {

            }else {
                strPublisherName = ", " + strPublisherName /*+ "&#91;" + "प्रका" + "&#93;"*/;
                strOtherName = strOtherName + strPublisherName;
            }
        }

        if (strOtherName != null && strOtherName.length() > 0) {
            holder.tvDetails.setText(Html.fromHtml(strOtherName));
        }

        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewListInteface.onMenuClick(viewBookList.get(position), position, holder.ivMenu);
            }
        });
    }

    public interface ViewListInteface {
        void onMenuClick(PdfStoreListResModel.PdfListModel filesModel, int position, ImageView ivMenu);
    }

    @Override
    public int getItemCount() {
        return viewBookList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDetails;
        CardView cvList;
        ImageView ivMenu;

        ViewHolder(View itemView) {
            super(itemView);
            tvDetails = itemView.findViewById(R.id.textViewName);
            cvList = itemView.findViewById(R.id.cvList);
            ivMenu = itemView.findViewById(R.id.ivMenu);

        }
    }
}

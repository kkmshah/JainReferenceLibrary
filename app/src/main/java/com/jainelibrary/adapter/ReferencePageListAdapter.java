package com.jainelibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.activity.AddReferenceActivity;
import com.jainelibrary.activity.BookReferenceDetailsActivity;
import com.jainelibrary.activity.EditReferenceActivity;
import com.jainelibrary.activity.ReferencePageDetailsActivity;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.BookReferenceDetailsResModel;
import com.jainelibrary.retrofitResModel.ShlokGranthSutraResModel;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReferencePageListAdapter extends RecyclerView.Adapter<ReferencePageListAdapter.ViewHolder> {

    Context mContext;
    String strBookId;
    int reference, referenceType;
    ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel> referencePageModels;

    public ReferencePageListAdapter(Context mContext, String strBookId, int page_no, int reference, int referenceType, ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel> referencePageModels) {
        this.mContext = mContext;

        this.strBookId = strBookId;
        this.reference = reference;
        this.referenceType = referenceType;
        this.referencePageModels = referencePageModels;
    }

    @Override
    public ReferencePageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_reference_page_details, parent, false);
        return new ReferencePageListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReferencePageListAdapter.ViewHolder holder, final int position) {
        BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel referencePageModel = referencePageModels.get(position);

        String page_header = "PDF Page > " + referencePageModel.getPdf_page_no() + "-" + (referencePageModel.getPdf_page_no() - referencePageModel.getPage_no()) + "=" + referencePageModel.getPage_no() + " < Book Page";
        holder.tvPageHeader.setText(page_header);

        holder.ivAddReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIndent = new Intent(mContext, AddReferenceActivity.class);
                addIndent.putExtra("book_id", strBookId);
                addIndent.putExtra("reference", reference);
                addIndent.putExtra("reference_type", referenceType);
                addIndent.putExtra("pdf_page_no", referencePageModel.getPdf_page_no());
                addIndent.putExtra("page_no", referencePageModel.getPage_no());
                mContext.startActivity(addIndent);
            }
        });

        ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel.ReferencePageRefModel> referencePageRefModels = referencePageModel.getReferencePageRefModels();
        holder.rvItems.setLayoutManager(new LinearLayoutManager(mContext));

        ReferencePageItemAdapter refPageItemAdapter = new ReferencePageItemAdapter(mContext, reference, referenceType, referencePageRefModels);
        holder.rvItems.setAdapter(refPageItemAdapter);
    }

    public void newData(ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel> list) {
        referencePageModels.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return referencePageModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPageHeader;
        ImageView ivAddReference;
        RecyclerView rvItems;

        ViewHolder(View itemView) {
            super(itemView);

            tvPageHeader = (TextView) itemView.findViewById(R.id.tvPageHeader);
            ivAddReference = itemView.findViewById(R.id.ivAddReference);
            rvItems = itemView.findViewById(R.id.rvItems);
        }
    }
}

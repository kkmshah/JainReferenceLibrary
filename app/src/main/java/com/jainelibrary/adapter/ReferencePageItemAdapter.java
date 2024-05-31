package com.jainelibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.activity.BookReferenceDetailsActivity;
import com.jainelibrary.activity.ReferencePageDetailsActivity;
import com.jainelibrary.retrofitResModel.BookReferenceDetailsResModel;

import java.util.ArrayList;

public class ReferencePageItemAdapter extends RecyclerView.Adapter<ReferencePageItemAdapter.ViewHolder> {

    Context mContext;
    int reference, referenceType;
    ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel.ReferencePageRefModel> referencePageRefModels;

    public ReferencePageItemAdapter(Context mContext, int reference, int referenceType, ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel.ReferencePageRefModel> referencePageRefModels) {
        this.mContext = mContext;
        this.reference = reference;
        this.referenceType = referenceType;
        this.referencePageRefModels = referencePageRefModels;
    }

    @Override
    public ReferencePageItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_reference_page_item, parent, false);
        return new ReferencePageItemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReferencePageItemAdapter.ViewHolder holder, final int position) {
        BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel.ReferencePageRefModel referencePageRefModel = referencePageRefModels.get(position);

        if (referencePageRefModel.getType_id() == 0)
            holder.ivKeyword.setVisibility(View.VISIBLE);
        else if (referencePageRefModel.getType_id() == 1)
            holder.ivShlok.setVisibility(View.VISIBLE);
        else if (referencePageRefModel.getType_id() == 2)
            holder.ivIndex.setVisibility(View.VISIBLE);
        else if (referencePageRefModel.getType_id() == 3)
            holder.ivYear.setVisibility(View.VISIBLE);

        String type_value = referencePageRefModel.getType_value();

        if (!referencePageRefModel.getType_name().equals(""))
            type_value += " (" + referencePageRefModel.getType_name() + ")";

        holder.tvDetails.setText(type_value);

        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reference_intent = new Intent(mContext, ReferencePageDetailsActivity.class);
                reference_intent.putExtra("type_id", referencePageRefModel.getType_id());
                reference_intent.putExtra("reference_id", referencePageRefModel.getId());
                reference_intent.putExtra("reference", reference);
                reference_intent.putExtra("reference_type", referenceType);
                mContext.startActivity(reference_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return referencePageRefModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llDetails;
        TextView tvDetails;
        ImageView ivKeyword, ivShlok, ivIndex, ivYear;

        ViewHolder(View itemView) {
            super(itemView);

            llDetails = itemView.findViewById(R.id.llDetails);
            ivKeyword = itemView.findViewById(R.id.ivKeyword);
            ivShlok = itemView.findViewById(R.id.ivShlok);
            ivIndex = itemView.findViewById(R.id.ivIndex);
            ivYear = itemView.findViewById(R.id.ivYear);
            tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);
        }
    }
}

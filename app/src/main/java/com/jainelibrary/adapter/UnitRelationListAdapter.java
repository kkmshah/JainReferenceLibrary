package com.jainelibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.BiodataMemoryDetailsModel;
import com.jainelibrary.model.RelationDetailsModel;
import com.jainelibrary.model.RelationDetailsModel;

import java.util.ArrayList;

public class UnitRelationListAdapter extends RecyclerView.Adapter<UnitRelationListAdapter.ViewHolder> {
    ArrayList<RelationDetailsModel> relationList;
    Context context;
    RelationClickListener relationClickListener;
    RelationHighlight relationHighlight;

    public UnitRelationListAdapter(Context context, ArrayList<RelationDetailsModel> relationList, RelationClickListener relationClickListener, RelationHighlight relationHighlight) {
        this.context = context;
        this.relationList = relationList;
        this.relationClickListener = relationClickListener;

        this.relationHighlight = relationHighlight;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_unit_relation_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvRelationType.setText(relationList.get(position).getRelation_name());
        holder.tvUnitName.setText(relationList.get(position).getName());
        holder.tvUnitType.setText(relationList.get(position).getType_name());

        holder.tvUnitStatus.setText(relationList.get(position).getStatus_name());
        holder.tvUnitSect.setText(relationList.get(position).getSect_name());

        holder.tvRelationImageCount.setText(relationList.get(position).getImage_count());
        holder.tvRelationRefCount.setText(""+relationList.get(position).getReference_book_pages().size()+"");
        holder.tvRelationCntCount.setText(relationList.get(position).getCnt_count());
        holder.tvRelationMtrCount.setText(relationList.get(position).getMtr_count());
        if(relationHighlight.isRelationHighlight(relationList, position)) {
            holder.cvList.getBackground().setTint(context.getResources().getColor(R.color.yellow_100));
        } else {
            holder.cvList.getBackground().setTint(context.getResources().getColor(R.color.colorwhite));
        }
        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relationClickListener.onRelationClick(relationList, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return relationList.size();
    }

    public interface RelationClickListener{
        public void onRelationClick(ArrayList<RelationDetailsModel> view, int position);

    }


    public interface RelationHighlight{
        public boolean isRelationHighlight(ArrayList<RelationDetailsModel> view, int position);

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRelationType, tvUnitName, tvUnitType, tvUnitStatus, tvUnitSect, tvRelationImageCount, tvRelationRefCount, tvRelationCntCount, tvRelationMtrCount;
        CardView cvList;
        LinearLayout llDetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvList = itemView.findViewById(R.id.cvList);
            llDetails = itemView.findViewById(R.id.llDetails);
            tvRelationType = itemView.findViewById(R.id.tvRelationType);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            tvUnitType = itemView.findViewById(R.id.tvUnitType);
            tvUnitStatus = itemView.findViewById(R.id.tvUnitStatus);
            tvUnitSect = itemView.findViewById(R.id.tvUnitSect);

            tvRelationImageCount = itemView.findViewById(R.id.tvRelationImageCount);
            tvRelationRefCount = itemView.findViewById(R.id.tvRelationRefCount);
            tvRelationCntCount = itemView.findViewById(R.id.tvRelationCntCount);
            tvRelationMtrCount = itemView.findViewById(R.id.tvRelationMtrCount);
        }
    }
}

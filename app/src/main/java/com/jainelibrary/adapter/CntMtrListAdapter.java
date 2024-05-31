package com.jainelibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jainelibrary.R;
import com.jainelibrary.model.CntMtrModel;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

public class CntMtrListAdapter extends RecyclerView.Adapter<CntMtrListAdapter.ViewHolder> {
    ArrayList<CntMtrModel> cntmtrList;
    Context context;
    EntityClickListener entityClickListener;

    EntityHighlight entityHighlight;

    public CntMtrListAdapter(Context context, ArrayList<CntMtrModel> cntmtrList, EntityClickListener entityClickListener) {
        this.context = context;
        this.cntmtrList = cntmtrList;
        this.entityClickListener = entityClickListener;
        this.entityHighlight = entityHighlight;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cnt_mtr_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        CntMtrModel cntmtr = cntmtrList.get(position);
        holder.tvEntityType.setText(cntmtr.getType_name());
        holder.tvEntityDetails.setText(cntmtr.getDetails());
        if(cntmtr.getBiodata_entity() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Utils.getUnitIcon(String.valueOf(cntmtr.getBiodata_entity().getUnit_type_id())))
                    .fitCenter()
                    .into(holder.ivUnitType);

            holder.tvUnitType.setText(cntmtr.getBiodata_entity().getUnit_type());
            holder.tvUnitName.setText(cntmtr.getBiodata_entity().getUnit_name());
            holder.tvUnitSect.setText(cntmtr.getBiodata_entity().getSect_name());
            holder.tvUnitStatus.setText(cntmtr.getBiodata_entity().getStatus_name());
            holder.tvBiodataType.setText(cntmtr.getBiodata_entity().getType_name());
            holder.tvBiodataLocation.setText(cntmtr.getBiodata_entity().getLocation());
            holder.tvBiodataDateDetails.setText(cntmtr.getBiodata_entity().getDate_details());
            holder.tvBiodataNotes.setText(cntmtr.getBiodata_entity().getNote());
            holder.llBioDetails.setVisibility(View.VISIBLE);

        }else if(cntmtr.getRelation_entity() != null ) {
            Glide.with(holder.itemView.getContext())
                    .load(Utils.getUnitIcon(String.valueOf(cntmtr.getRelation_entity().getUnit1_type_id())))
                    .fitCenter()
                    .into(holder.ivUnitType);

            holder.tvUnitType.setText(cntmtr.getRelation_entity().getUnit_type_name1());
            holder.tvUnitName.setText(cntmtr.getRelation_entity().getUnit_name1());
            holder.tvUnitSect.setText(cntmtr.getRelation_entity().getUnit_sect_name1());
            holder.tvUnitStatus.setText(cntmtr.getRelation_entity().getUnit_status_name1());
            holder.tvRelationType.setText(cntmtr.getRelation_entity().getRelation_name());
            holder.tvRelationUnitName.setText(cntmtr.getRelation_entity().getUnit_name2());
            holder.tvRelationUnitType.setText(cntmtr.getRelation_entity().getUnit_type_name2());
            holder.tvRelationUnitSect.setText(cntmtr.getRelation_entity().getUnit_sect_name2());
            holder.tvRelationUnitSect.setText(cntmtr.getRelation_entity().getUnit_status_name2());
            holder.llRelationDetails.setVisibility(View.VISIBLE);
        } else {
            holder.llEntityDetails.setVisibility(View.VISIBLE);
            holder.llEntityType.setVisibility(View.VISIBLE);
        }
        holder.tvEntityImageCount.setText(""+cntmtr.getImage_count());
        holder.tvEntityRefCount.setText(""+cntmtr.getRef_count());
        holder.tvEntityCntCount.setText(""+cntmtr.getCnt_count());
        holder.tvEntityMtrCount.setText(""+cntmtr.getMtr_count());


        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entityClickListener.onCntMtrEntityClick(cntmtrList, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cntmtrList.size();
    }

    public interface EntityClickListener{
        public void onCntMtrEntityClick(ArrayList<CntMtrModel> view, int position);

    }


    public interface EntityHighlight{
        public boolean isEntityHighlight(ArrayList<CntMtrModel> view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitName, tvUnitType, tvUnitStatus, tvUnitSect, tvEntityType, tvEntityDetails, tvEntityImageCount, tvEntityRefCount, tvEntityCntCount, tvEntityMtrCount;

        TextView tvBiodataType, tvBiodataDateDetails, tvBiodataLocation, tvBiodataNotes;
        TextView tvRelationType, tvRelationUnitName, tvRelationUnitType, tvRelationUnitStatus, tvRelationUnitSect;

        LinearLayout llDetails, llEntityDetails, llEntityType, llBioDetails,llRelationDetails;
        ImageView ivUnitType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llDetails = itemView.findViewById(R.id.llDetails);
            llEntityDetails = itemView.findViewById(R.id.llEntityDetails);
            llEntityType = itemView.findViewById(R.id.llEntityType);
            llBioDetails = itemView.findViewById(R.id.llBioDetails);
            llRelationDetails = itemView.findViewById(R.id.llRelationDetails);


            tvEntityType = itemView.findViewById(R.id.tvEntityType);
            tvEntityDetails = itemView.findViewById(R.id.tvEntityDetails);

            ivUnitType = itemView.findViewById(R.id.ivUnitType);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            tvUnitType = itemView.findViewById(R.id.tvUnitType);
            tvUnitStatus = itemView.findViewById(R.id.tvUnitStatus);
            tvUnitSect = itemView.findViewById(R.id.tvUnitSect);

            // biodata
            tvBiodataType = itemView.findViewById(R.id.tvBiodataType);
            tvBiodataDateDetails= itemView.findViewById(R.id.tvBiodataDateDetails);
            tvBiodataLocation = itemView.findViewById(R.id.tvBiodataLocation);
            tvBiodataNotes = itemView.findViewById(R.id.tvBiodataNotes);

            //relation
            tvRelationType = itemView.findViewById(R.id.tvRelationType);
            tvRelationUnitName= itemView.findViewById(R.id.tvRelationUnitName);
            tvRelationUnitType = itemView.findViewById(R.id.tvRelationUnitType);
            tvRelationUnitStatus = itemView.findViewById(R.id.tvRelationUnitStatus);
            tvRelationUnitSect = itemView.findViewById(R.id.tvRelationUnitSect);



            tvEntityImageCount = itemView.findViewById(R.id.tvEntityImageCount);
            tvEntityRefCount = itemView.findViewById(R.id.tvEntityRefCount);
            tvEntityCntCount = itemView.findViewById(R.id.tvEntityCntCount);
            tvEntityMtrCount = itemView.findViewById(R.id.tvEntityMtrCount);
        }
    }
}

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jainelibrary.R;
import com.jainelibrary.model.SearchUnitListResModel;
import com.jainelibrary.model.YearResponseModel;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

public class UnitWisePramparaSearchListAdapter extends RecyclerView.Adapter<UnitWisePramparaSearchListAdapter.ViewHolder> {
    ArrayList<SearchUnitListResModel.Unit> unitList;
    Context context;
    UnitClickListener unitClickListener;

    public UnitWisePramparaSearchListAdapter(Context context, ArrayList<SearchUnitListResModel.Unit> unitList, UnitClickListener unitClickListener) {
        this.context = context;
        this.unitList = unitList;
        this.unitClickListener = unitClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_unit_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public ArrayList<SearchUnitListResModel.Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(ArrayList<SearchUnitListResModel.Unit> unitList) {
        this.unitList = unitList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvUnitName.setText(unitList.get(position).getName());
        holder.tvUnitType.setText(unitList.get(position).getType_name());
        holder.tvUnitStatus.setText(unitList.get(position).getStatus_name());
        holder.tvUnitSect.setText(unitList.get(position).getSect_name());
        Glide.with(holder.itemView.getContext())
                .load(Utils.getUnitIcon(String.valueOf(unitList.get(position).getType_id())))
                .fitCenter()
                .into(holder.ivUnitType);


        Glide.with(holder.itemView.getContext())
                .load(Utils.getUnitIcon(String.valueOf(unitList.get(position).getType_id())))
                .fitCenter()
                .into(holder.ivUnitType);


        holder.tvUniteDetails.setText(unitList.get(position).getUnit_details());

        holder.tvRelations.setText(unitList.get(position).getFormat_relation_count());
        holder.tvBioCounts.setText(unitList.get(position).getFormat_biodata_count());
        holder.tvEventCounts.setText(unitList.get(position).getFormat_event_count());

        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitClickListener.onUnitClick(unitList, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return unitList.size() ;
    }

    public interface UnitClickListener{
        public void onUnitClick(ArrayList<SearchUnitListResModel.Unit> view, int position);

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitName, tvRelations, tvBioCounts, tvEventCounts, tvUnitSect, tvUnitStatus, tvUnitType;
        CardView cvList;
        LinearLayout llDetails;

        TextView tvUniteDetails;
        ImageView ivUnitType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUnitType = itemView.findViewById(R.id.ivUnitType);

            tvUnitName = itemView.findViewById(R.id.textUnitName);
            tvUnitSect = itemView.findViewById(R.id.tvUnitSect);
            tvUnitStatus = itemView.findViewById(R.id.tvUnitStatus);
            tvUnitType = itemView.findViewById(R.id.tvUnitType);

            tvRelations = itemView.findViewById(R.id.tvRelationCounts);
            tvBioCounts = itemView.findViewById(R.id.tvBioCounts);
            tvEventCounts = itemView.findViewById(R.id.tvEventCounts);
            llDetails = itemView.findViewById(R.id.llDetails);
            tvUniteDetails = itemView.findViewById(R.id.tvUnitDetails);
            cvList = itemView.findViewById(R.id.cvList);
        }
    }
}

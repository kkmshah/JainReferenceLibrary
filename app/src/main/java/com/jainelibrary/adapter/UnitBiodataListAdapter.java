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
import com.jainelibrary.model.ParamparaFilterDataResModel;

import java.util.ArrayList;

public class UnitBiodataListAdapter extends RecyclerView.Adapter<UnitBiodataListAdapter.ViewHolder> {
    ArrayList<BiodataMemoryDetailsModel> bmList;
    Context context;
    BiodataClickListener bmClickListener;

    BiodataHighlight bmHighlight;

    ParamparaFilterDataResModel.Samvat searchBioSamvatYear;

    public UnitBiodataListAdapter(Context context, ArrayList<BiodataMemoryDetailsModel> bmList, ParamparaFilterDataResModel.Samvat searchBioSamvatYear, BiodataClickListener bmClickListener, BiodataHighlight bmHighlight) {
        this.context = context;
        this.bmList = bmList;
        this.bmClickListener = bmClickListener;
        this.searchBioSamvatYear = searchBioSamvatYear;
        this.bmHighlight = bmHighlight;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_unit_biodata_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvBiodataType.setText(bmList.get(position).getType_name());
        holder.tvBiodataDateDetails.setText(bmList.get(position).getDate_details());
        if(searchBioSamvatYear !=null  && bmHighlight.isBiodataHighlight(bmList, position) && !searchBioSamvatYear.getSamvat_type().equals(bmList.get(position).getSamvat_year_type())) {
            holder.tvYearSimilarSearch.setText("("+bmList.get(position).getSamvat_year_name() + "=" + searchBioSamvatYear.getSamvatFormatedName() +")");
            holder.llYearSimilarSearch.setVisibility(View.VISIBLE);
        }

        holder.tvBiodataLocation.setText(bmList.get(position).getLocation());
        holder.tvBiodataNotes.setText(bmList.get(position).getNote());

        holder.tvBiodataImageCount.setText(bmList.get(position).getImage_count());
        holder.tvBiodataRefCount.setText(""+bmList.get(position).getReference_book_pages().size()+"");
        holder.tvBiodataCntCount.setText(bmList.get(position).getCnt_count());
        holder.tvBiodataMtrCount.setText(bmList.get(position).getMtr_count());
        if(bmHighlight.isBiodataHighlight(bmList, position)) {
            holder.cvList.getBackground().setTint(context.getResources().getColor(R.color.yellow_100));
        } else {
            holder.cvList.getBackground().setTint(context.getResources().getColor(R.color.colorwhite));
        }
        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmClickListener.onBiodataClick(bmList, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bmList.size();
    }

    public interface BiodataClickListener{
        public void onBiodataClick(ArrayList<BiodataMemoryDetailsModel> view, int position);

    }

    public interface BiodataHighlight{
        public boolean isBiodataHighlight(ArrayList<BiodataMemoryDetailsModel> view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBiodataType, tvBiodataDateDetails, tvBiodataLocation, tvBiodataNotes, tvBiodataImageCount, tvBiodataRefCount, tvBiodataCntCount, tvBiodataMtrCount, tvYearSimilarSearch;
        CardView cvList;
        LinearLayout llDetails, llYearSimilarSearch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvList = itemView.findViewById(R.id.cvList);
            llDetails = itemView.findViewById(R.id.llDetails);
            tvBiodataType = itemView.findViewById(R.id.tvBiodataType);
            tvBiodataDateDetails = itemView.findViewById(R.id.tvBiodataDateDetails);
            tvBiodataLocation = itemView.findViewById(R.id.tvBiodataLocation);
            tvBiodataNotes = itemView.findViewById(R.id.tvBiodataNotes);
            tvYearSimilarSearch = itemView.findViewById(R.id.tvYearSimilarSearch);
            llYearSimilarSearch = itemView.findViewById(R.id.llYearSimilarSearch);


            tvBiodataImageCount = itemView.findViewById(R.id.tvBiodataImageCount);
            tvBiodataRefCount = itemView.findViewById(R.id.tvBiodataRefCount);
            tvBiodataCntCount = itemView.findViewById(R.id.tvBiodataCntCount);
            tvBiodataMtrCount = itemView.findViewById(R.id.tvBiodataMtrCount);
        }
    }
}

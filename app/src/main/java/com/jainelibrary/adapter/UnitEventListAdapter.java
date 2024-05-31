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

public class UnitEventListAdapter extends RecyclerView.Adapter<UnitEventListAdapter.ViewHolder> {
    ArrayList<BiodataMemoryDetailsModel> bmList;
    Context context;
    EventClickListener eventClickListener;

    EventHighlight eventHighlight;
    ParamparaFilterDataResModel.Samvat searchBioSamvatYear;

    public UnitEventListAdapter(Context context, ArrayList<BiodataMemoryDetailsModel> bmList, ParamparaFilterDataResModel.Samvat searchBioSamvatYear, EventClickListener eventClickListener, EventHighlight eventHighlight) {
        this.context = context;
        this.bmList = bmList;
        this.eventClickListener = eventClickListener;
        this.searchBioSamvatYear = searchBioSamvatYear;
        this.eventHighlight = eventHighlight;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_unit_event_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvEventType.setText(bmList.get(position).getType_name());
        holder.tvEventDateDetails.setText(bmList.get(position).getDate_details());

        if(searchBioSamvatYear !=null && eventHighlight.isEventHighlight(bmList, position) && !searchBioSamvatYear.getSamvat_type().equals(bmList.get(position).getSamvat_year_type())) {
            holder.tvYearSimilarSearch.setText("("+bmList.get(position).getSamvat_year_name() + "=" + searchBioSamvatYear +")");
            holder.llYearSimilarSearch.setVisibility(View.VISIBLE);
        }

        holder.tvEventLocation.setText(bmList.get(position).getLocation());
        holder.tvEventNotes.setText(bmList.get(position).getNote());

        holder.tvEventImageCount.setText(bmList.get(position).getImage_count());
        holder.tvEventRefCount.setText(""+bmList.get(position).getReference_book_pages().size()+"");
        holder.tvEventCntCount.setText(bmList.get(position).getCnt_count());
        holder.tvEventMtrCount.setText(bmList.get(position).getMtr_count());

        if(eventHighlight.isEventHighlight(bmList, position)) {
            holder.cvList.getBackground().setTint(context.getResources().getColor(R.color.yellow_100));
        } else {
            holder.cvList.getBackground().setTint(context.getResources().getColor(R.color.colorwhite));
        }
        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventClickListener.onEventClick(bmList, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bmList.size() > 50 ? 50 : bmList.size();
    }

    public interface EventClickListener{
        public void onEventClick(ArrayList<BiodataMemoryDetailsModel> view, int position);

    }


    public interface EventHighlight{
        public boolean isEventHighlight(ArrayList<BiodataMemoryDetailsModel> view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventType, tvEventDateDetails, tvEventLocation, tvEventNotes, tvEventImageCount, tvEventRefCount, tvEventCntCount, tvEventMtrCount;
        CardView cvList;
        LinearLayout llDetails, llYearSimilarSearch;
        TextView tvYearSimilarSearch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvList = itemView.findViewById(R.id.cvList);
            llDetails = itemView.findViewById(R.id.llDetails);
            tvEventType = itemView.findViewById(R.id.tvEventType);
            tvEventDateDetails = itemView.findViewById(R.id.tvEventDateDetails);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
            tvEventNotes = itemView.findViewById(R.id.tvEventNotes);
            tvYearSimilarSearch = itemView.findViewById(R.id.tvYearSimilarSearch);
            llYearSimilarSearch = itemView.findViewById(R.id.llYearSimilarSearch);



            tvEventImageCount = itemView.findViewById(R.id.tvEventImageCount);
            tvEventRefCount = itemView.findViewById(R.id.tvEventRefCount);
            tvEventCntCount = itemView.findViewById(R.id.tvEventCntCount);
            tvEventMtrCount = itemView.findViewById(R.id.tvEventMtrCount);
        }
    }
}

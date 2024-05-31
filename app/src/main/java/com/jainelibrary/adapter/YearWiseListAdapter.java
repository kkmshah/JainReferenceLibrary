package com.jainelibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jainelibrary.R;
import com.jainelibrary.model.YearResponseModel;

import java.util.ArrayList;

public class YearWiseListAdapter extends RecyclerView.Adapter<YearWiseListAdapter.ViewHolder> {
    ArrayList<YearResponseModel.YearModel> yearlist;
    Context context;
    YearClickListener yearClickListener;

    public YearWiseListAdapter(Context context, ArrayList<YearResponseModel.YearModel> yearlist, YearClickListener yearClickListener) {
        this.context = context;
        this.yearlist = yearlist;
        this.yearClickListener = yearClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_year_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvYear.setText(yearlist.get(position).getYear_name());
        holder.tvReference.setText(yearlist.get(position).getReference_cnt());

        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearClickListener.onYearClick(yearlist,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return yearlist.size();
    }

    public interface YearClickListener{
        public void onYearClick(ArrayList<YearResponseModel.YearModel> view, int position);

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvYear, tvReference;
        CardView cvList;
        LinearLayout llDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvYear = itemView.findViewById(R.id.textViewYear);
            tvReference = itemView.findViewById(R.id.tvReference);
            llDetails = itemView.findViewById(R.id.llDetails);
            cvList = itemView.findViewById(R.id.cvList);
        }
    }
}

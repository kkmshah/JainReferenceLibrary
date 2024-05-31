package com.jainelibrary.adapter;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.SearchOptionResModel;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchOptionAdapter extends RecyclerView.Adapter<FilterSearchOptionAdapter.MyViewHolder> {

    private Context context;
    private List<SearchOptionResModel.SearchOptionModel> optionValuesList = new ArrayList<>();
    private ArrayList<String> selectedOptionIdList = new ArrayList<>();
    private onLensSelectListener onLensSelectListener;
    private List<FilterSearchOptionAdapter.MyViewHolder> myViewHolderList = new ArrayList<>();

    public FilterSearchOptionAdapter(Context context, List<SearchOptionResModel.SearchOptionModel> filterList, onLensSelectListener onLensSelectListener, ArrayList<String> selectedOptionIdList) {
        this.context = context;
        this.optionValuesList = filterList;
        this.onLensSelectListener = onLensSelectListener;
        this.selectedOptionIdList = selectedOptionIdList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_option_item, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(view);
        myViewHolderList.add(mvh);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        final SearchOptionResModel.SearchOptionModel searchOptionModel = optionValuesList.get(position);
        myViewHolder.tvName.setText(searchOptionModel.getName());

        String strId = String.valueOf(searchOptionModel.getId());
        Log.e("optionLIst : ", "" + selectedOptionIdList.size());
        Log.e("optionSelect : ", "" + optionValuesList.get(position).isSelected());

        if (selectedOptionIdList != null && selectedOptionIdList.size() > 0 && selectedOptionIdList.contains(strId)) {
            if (optionValuesList.get(position).isSelected()) {
                myViewHolder.cbDetails.setChecked(true);
            } else {
                myViewHolder.cbDetails.setChecked(false);
            }
//            myViewHolder.cbDetails.setChecked(true);
//            optionValuesList.get(position).setSelected(true);
        } else if (selectedOptionIdList == null || selectedOptionIdList.size() == 0) {
            if (optionValuesList.get(position).isSelected()) {
                myViewHolder.cbDetails.setChecked(true);
            } else {
                myViewHolder.cbDetails.setChecked(false);
                optionValuesList.get(position).setSelected(false);
            }
        }

        myViewHolder.tvInfo.setText(optionValuesList.get(position).getMessage());

        myViewHolder.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myViewHolder.llDetailsInfo.getVisibility() == View.VISIBLE) {
                    myViewHolder.llDetailsInfo.setVisibility(View.GONE);

                    RotateAnimation rotate = new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(300);
                    rotate.setFillAfter(true);
                    myViewHolder.ivInfo.startAnimation(rotate);
                } else {
                    for (FilterSearchOptionAdapter.MyViewHolder myViewHolder: myViewHolderList)
                    {
                        if (myViewHolder.llDetailsInfo.getVisibility() == View.VISIBLE) {
                            myViewHolder.llDetailsInfo.setVisibility(View.GONE);

                            RotateAnimation rotate = new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                            rotate.setDuration(300);
                            rotate.setFillAfter(true);
                            myViewHolder.ivInfo.startAnimation(rotate);
                        }
                    }

                    myViewHolder.llDetailsInfo.setVisibility(View.VISIBLE);

                    RotateAnimation rotate = new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(300);
                    rotate.setFillAfter(true);
                    myViewHolder.ivInfo.startAnimation(rotate);
                }
                //onLensSelectListener.onInfo(optionValuesList, position);
            }
        });
        myViewHolder.cbDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionValuesList != null && optionValuesList.size() > 0 && optionValuesList.get(position).getName().equalsIgnoreCase("Exact Search")) {
                    optionValuesList.get(position).setSelected(true);
                    myViewHolder.cbDetails.setChecked(true);
                } else {
                    Log.e("item selected", "1 " + optionValuesList.get(position).isSelected());
                    if (myViewHolder.cbDetails.isChecked()) {
                        optionValuesList.get(position).setSelected(true);
                    } else {
                        optionValuesList.get(position).setSelected(false);
                    }
                    Log.e("item selected", "2 " + optionValuesList.get(position).isSelected());
                    onLensSelectListener.onLensSelect(optionValuesList, position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return optionValuesList.size();
    }

    public interface onLensSelectListener {
        void onLensSelect(List<SearchOptionResModel.SearchOptionModel> optionValuesList, int position);

        void onInfo(List<SearchOptionResModel.SearchOptionModel> optionValuesList, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbDetails;
        TextView tvName;
        ImageView ivInfo;
        LinearLayout llDetailsInfo;
        TextView tvInfo;

        public MyViewHolder(View view) {
            super(view);
            cbDetails = view.findViewById(R.id.cbDetails);
            ivInfo = view.findViewById(R.id.ivInfo);
            tvName = view.findViewById(R.id.tvDetails);
            llDetailsInfo = view.findViewById(R.id.llDetailsInfo);
            tvInfo = view.findViewById(R.id.tvInfo);
        }
    }
}

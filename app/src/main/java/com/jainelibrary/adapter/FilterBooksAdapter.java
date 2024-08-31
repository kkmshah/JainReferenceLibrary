package com.jainelibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.FilterBookResModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FilterBooksAdapter extends RecyclerView.Adapter<FilterBooksAdapter.MyViewHolder> {

    private static final String TAG = FilterBooksAdapter.class.getSimpleName();
    private Context context;
    private List<FilterBookResModel.FilterModel> filterBookList = new ArrayList<>();
    private onFocusSelectListener onFocusSelectListener;

    public FilterBooksAdapter(Context context, List<FilterBookResModel.FilterModel> filterList, onFocusSelectListener onFocusSelectListener) {
        this.context = context;
        this.filterBookList = filterList;
        this.onFocusSelectListener = onFocusSelectListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_books_items, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        myViewHolder.tvName.setText(filterBookList.get(position).getName());
        String strUrl = filterBookList.get(position).getBook_image();

        if (strUrl != null && strUrl.length() > 0) {
            Picasso.get().load(strUrl).placeholder(R.drawable.noimage).into(myViewHolder.ivImage);
            myViewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    onFocusSelectListener.onBookImageClick(filterBookList.get(position), position);
                }
            });
        }
        Log.e(TAG,"url : " + strUrl);
        int filter = filterBookList.get(position).getFilter();
        if (filter == 1) {
            myViewHolder.cbDetails.setChecked(true);
            filterBookList.get(position).setSelected(true);
        } else {
            myViewHolder.cbDetails.setChecked(false);
            filterBookList.get(position).setSelected(false);
        }

        myViewHolder.cbDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myViewHolder.cbDetails.isChecked()) {
                    filterBookList.get(position).setFilter(1);
                    filterBookList.get(position).setSelected(true);
                } else {
                    filterBookList.get(position).setFilter(0);
                    filterBookList.get(position).setSelected(false);
                }

                onFocusSelectListener.onFocusSelect(filterBookList, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filterBookList.size();
    }

    public interface onFocusSelectListener {
        void onFocusSelect(List<FilterBookResModel.FilterModel> filterBookList, int position);
        void onBookImageClick(FilterBookResModel.FilterModel filterBook, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbDetails;
        TextView tvName;
        ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            cbDetails = view.findViewById(R.id.cbDetails);
            ivImage = view.findViewById(R.id.ivImage);
            tvName = view.findViewById(R.id.tvDetails);
        }
    }

}

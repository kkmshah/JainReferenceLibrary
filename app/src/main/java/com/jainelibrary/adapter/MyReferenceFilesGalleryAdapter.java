package com.jainelibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.MyShelfResModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyReferenceFilesGalleryAdapter extends RecyclerView.Adapter<MyReferenceFilesGalleryAdapter.ViewHolder> {

    Context context;
    ArrayList<MyShelfResModel.MyShelfModel.MyShelfImageModel> mImageList, mSelectImageList = new ArrayList<>();
    MyShelfInterFaceListener myShelfInterFaceListener;

    public MyReferenceFilesGalleryAdapter(Context context, ArrayList<MyShelfResModel.MyShelfModel.MyShelfImageModel> mImageList, MyShelfInterFaceListener myShelfInterFaceListener) {
        this.context = context;
        this.mImageList = mImageList;
        this.myShelfInterFaceListener = myShelfInterFaceListener;
        this.mSelectImageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_my_shelf_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyShelfResModel.MyShelfModel.MyShelfImageModel myShelfImageModel = mImageList.get(position);
        String strImageUrl = mImageList.get(position).getUrl();
        String strPageNo = mImageList.get(position).getPage_no();

        if (strImageUrl != null && strImageUrl.length() > 0) {
            holder.viewLine.setVisibility(View.VISIBLE);
            Picasso.get().load(strImageUrl).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(holder.ivPhoto);
        }

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myShelfInterFaceListener.onDetailsClick(mImageList, position);
            }
        });

        holder.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mSelectImageList.contains(myShelfImageModel)) {
                    mSelectImageList.remove(myShelfImageModel);
                    holder.ivSelect.setVisibility(View.GONE);
                    //   holder.llpage.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                    myShelfInterFaceListener.onDeleteImage(mImageList, position, false);
                } else {
                    mSelectImageList.add(myShelfImageModel);
                    holder.ivPhoto.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                    myShelfInterFaceListener.onDeleteImage(mImageList, position, true);
                    // holder.llpage.setBackgroundColor(ContextCompat.getColor(context, R.color.backcolor));
                    holder.ivSelect.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        if (mSelectImageList.contains(myShelfImageModel))
            holder.ivSelect.setVisibility(View.VISIBLE);
        else
            holder.ivSelect.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llpage;
        ImageView ivPhoto, ivSelect;
        View viewLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            llpage = itemView.findViewById(R.id.llpage);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            viewLine = itemView.findViewById(R.id.viewLine);
        }
    }

    public interface MyShelfInterFaceListener {
        void onDetailsClick(ArrayList<MyShelfResModel.MyShelfModel.MyShelfImageModel> searchList, int position);

        void onDeleteImage(ArrayList<MyShelfResModel.MyShelfModel.MyShelfImageModel> searchList, int position, boolean isSelected);

    }
}

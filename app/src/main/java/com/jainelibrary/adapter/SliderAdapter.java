package com.jainelibrary.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.jainelibrary.R;
import com.jainelibrary.model.ImageFileModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.model.SliderData;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<ImageFileModel> mSliderItems;

    private final  boolean enableZoom;

    private SliderAdapter.OnImageClickListener onImageClickListener;
    // Constructor
    public SliderAdapter(Context context, ArrayList<ImageFileModel> sliderDataArrayList, boolean enableZoom, SliderAdapter.OnImageClickListener onImageClickListener) {
        this.mSliderItems = sliderDataArrayList;
        this.onImageClickListener = onImageClickListener;
        this.enableZoom = enableZoom;

    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(enableZoom ? R.layout.slider_zoom_layout : R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final SliderData sliderItem = mSliderItems.get(position);

        String imageUrl = sliderItem.getImgUrl();
        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.noimage)
                .fitCenter()
                .into(viewHolder.imageViewBackground);
//        Picasso.get().load(sliderItem.getImgUrl())
//                .placeholder(R.drawable.progress_animation)
//                .error(R.drawable.noimage)
//                .into(viewHolder.imageViewBackground, new Callback() {
//            @Override
//            public void onSuccess() {
//                viewHolder.imageViewBackground.setScaleType(ImageView.ScaleType.FIT_CENTER);//Or ScaleType.FIT_CENTER
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
        onImageClickListener.
                setCurrentPositions(position);
        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(viewHolder.itemView.getContext(), "This is item in position " + position, Toast.LENGTH_SHORT).show();
                onImageClickListener.onZoomClick(mSliderItems, position);
            }
        });
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public SliderData getSliderData(int position) {

        return mSliderItems.get(position);
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }

    public interface OnImageClickListener {
        void onZoomClick(List<ImageFileModel> imageFileList, int position);

        void setCurrentPositions(int positions);
    }
}
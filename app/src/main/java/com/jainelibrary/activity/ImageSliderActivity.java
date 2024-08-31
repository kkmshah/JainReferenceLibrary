package com.jainelibrary.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.jainelibrary.R;
import com.jainelibrary.adapter.SliderAdapter;
import com.jainelibrary.model.FilesImageModel;
import com.jainelibrary.model.ImageFileModel;
import com.jainelibrary.model.SliderData;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.Utils;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderActivity extends AppCompatActivity {
    PhotoView photoView;

    String strImages;

    boolean isUrl;

    LinearLayout llSliderControl;
    SliderView sliderView;

    ImageView sliderNext;
    ImageView sliderPrev;

    TextView imageCount;

    TextView tvPageName;


    ArrayList<ImageFileModel> imageFileList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        loadUiElements();
        declaration();
        setHeader();
        setImageSlider(imageFileList);
    }

    private void declaration() {
        Intent intent = getIntent();
        imageFileList = (ArrayList<ImageFileModel>) getIntent().getSerializableExtra("imageList");
        for(int i = 0; i< imageFileList.size(); i++) {
            imageFileList.get(i).setLarge(true);
        }
    }


    private void loadUiElements() {
        // initializing the slider view.
        sliderView = findViewById(R.id.slider);
        sliderPrev = findViewById(R.id.btnSliderPrev);
        sliderNext = findViewById(R.id.btnSliderNext);
        llSliderControl = findViewById(R.id.llSliderControl);
        sliderPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sliderView.slideToPreviousPosition();
                Log.e("Zoom Slider", "After Previous: "+sliderView.getCurrentPagePosition());
                //imageCount.setText(sliderView.getCurrentPagePosition()+1 + "/" + sliderView.getSliderAdapter().getCount());

            }
        });

        // Next button click listener
        sliderNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderView.slideToNextPosition();
                Log.e("Zoom Slider", "After Next: "+sliderView.getCurrentPagePosition());;
                //imageCount.setText(sliderView.getCurrentPagePosition()+1 + "/" + sliderView.getSliderAdapter().getCount());

            }
        });
    }
    private void setImageSlider(ArrayList<ImageFileModel> imageFiles) {
        // we are creating array list for storing our image urls.

        // adding the urls inside array list
        sliderView.setAutoCycle(false);
        SliderAdapter adapter = new SliderAdapter(this, imageFiles, true, new SliderAdapter.OnImageClickListener(){
            @Override
            public void onZoomClick(List<ImageFileModel> imageFileList, int position) {

            }
            @Override
            public void setCurrentPositions(int position) {
                imageCount.setText(position+1 + "/" + sliderView.getSliderAdapter().getCount());
            }
        });

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);
        // below method is use to set
        // scroll time in seconds.
        //sliderView.setScrollTimeInSec(3);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setCurrentPagePosition(0);
        if(imageFiles.size()<=1) {
            llSliderControl.setVisibility(View.GONE);
        }

    }

    private void setHeader () {
            View headerView = findViewById(R.id.header);
            ImageView ivBack = headerView.findViewById(R.id.ivBack);
            ImageView ivdownload = headerView.findViewById(R.id.ivDelete);
            imageCount = headerView.findViewById(R.id.tvKey);
            imageCount.setVisibility(View.VISIBLE);
            LinearLayout llItems = headerView.findViewById(R.id.llAddItem);
            llItems.setVisibility(View.VISIBLE);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            ivdownload.setImageResource(R.mipmap.download);
            ivdownload.setVisibility(View.VISIBLE);
            ivdownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageFileModel fileImage = imageFileList.get(sliderView.getCurrentPagePosition());
                    downloadFile(fileImage.getImgUrl());
                }
            });
            tvPageName = headerView.findViewById(R.id.tvPage);
            tvPageName.setText("Zoom View");
    }
    public void downloadFile (String strImage){
        if (strImage == null || strImage.isEmpty()) {
            return;
        }
         Utils.downloadImageFromURL(ImageSliderActivity.this, strImage);

    }
}

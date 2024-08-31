package com.jainelibrary.activity;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.jainelibrary.R;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class ZoomImageActivity extends AppCompatActivity {
    PhotoView photoView;

    String strImages;

    boolean isUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        declaration();
    }

    private void loadImage(PhotoView photoView, String image, String fallbackImage) {

        Log.e("Hold_Image_Zoom_A", image== null ? "": image);

        Log.e("Hold_Image_Zoom_A", fallbackImage == null ? "": fallbackImage);
        Picasso.get().load(image).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(photoView, new Callback() {
            @Override
            public void onSuccess() {
                photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);//Or ScaleType.FIT_CENTER
                strImages = image;
            }

            @Override
            public void onError(Exception e) {
                if(fallbackImage != null && image != null && !image.equals(fallbackImage)) {
                    loadImage(photoView, fallbackImage, null);
                }
            }
        });
    }
    private void declaration() {
        photoView = (PhotoView) findViewById(R.id.ivZoom);
        Intent i = getIntent();
        if (i != null) {
            isUrl = i.getBooleanExtra("url", false);
            if (isUrl) {
                String image = i.getStringExtra("image");
                String fallbackImage = i.getStringExtra("fallbackImage");
                strImages = (image != null && !image.isEmpty()) ? image : fallbackImage;
                loadImage(photoView, strImages, fallbackImage);
            } else {
                byte[] imgBytes = i.getByteArrayExtra("image");
                Bitmap myBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
//                byte[] imgBytes = Base64.decode(strImages, Base64.DEFAULT);
//                Bitmap myBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
//                    File imgFile = new File(strImages);
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                photoView.setImageBitmap(myBitmap);
            }
        }
        setHeader();
    }


        private void setHeader () {
            View headerView = findViewById(R.id.header);
            ImageView ivBack = headerView.findViewById(R.id.ivBack);
            ImageView ivdownload = headerView.findViewById(R.id.ivDelete);
            TextView tvKey = headerView.findViewById(R.id.tvKey);
            tvKey.setVisibility(View.GONE);
            LinearLayout llItems = headerView.findViewById(R.id.llAddItem);
            llItems.setVisibility(View.VISIBLE);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SharedPrefManager.getInstance(getApplicationContext()).saveStringPref(SharedPrefManager.IMG_URL,"");
                    onBackPressed();
                }
            });

            ivdownload.setImageResource(R.mipmap.download);
            ivdownload.setVisibility(View.VISIBLE);
            ivdownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isUrl && strImages != null && strImages.length() > 0) {
                        downloadFile(strImages);
                    } else {
                        Log.e("strImaeg--", "null");
                    }
                }
            });
            TextView tvPageName = headerView.findViewById(R.id.tvPage);
            tvPageName.setText("Zoom View");
        }

        public void downloadFile (String strImage){
            Utils.downloadImageFromURL(ZoomImageActivity.this, strImage);
        }
    }

package com.jainelibrary.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.jainelibrary.R;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ZoomImageActivity extends AppCompatActivity {
    PhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        declaration();
    }

    private void declaration() {
        photoView = (PhotoView) findViewById(R.id.ivZoom);
        Intent i = getIntent();
        if (i != null) {
            boolean isUrl = i.getBooleanExtra("url", false);
            if (isUrl) {
                String strImages = i.getStringExtra("image");
                Picasso.get().load(strImages).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);//Or ScaleType.FIT_CENTER
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
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
            LinearLayout llItems = headerView.findViewById(R.id.llAddItem);
            llItems.setVisibility(View.GONE);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SharedPrefManager.getInstance(getApplicationContext()).saveStringPref(SharedPrefManager.IMG_URL,"");
                    onBackPressed();
                }
            });

            ivdownload.setVisibility(View.GONE);
            ivdownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (strImages != null && strImages.length() > 0) {
//                        //downloadFile(strImages);
//                    } else {
//                        Log.e("strImaeg--", "null");
//                    }
                }
            });
            TextView tvPageName = headerView.findViewById(R.id.tvPage);
            tvPageName.setText("Zoom View");
        }

        public void downloadFile (String uRl){

            if (!Utils.mediaStoreDownloadsDir.exists())
                Utils.mediaStoreDownloadsDir.mkdirs();

            DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(uRl);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            String imagePath = "JRL" + System.currentTimeMillis();
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("JRL" + System.currentTimeMillis())
                    .setDescription("Something useful. No, really.")
                    .setDestinationInExternalPublicDir(Utils.mediaStoreDownloadsDir.getAbsolutePath(), imagePath + ".jpg");
            mgr.enqueue(request);
            startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

        }/*

   @Override
    public void onBackPressed() {
        super.onBackPressed();

                finish();
    }*/
    }

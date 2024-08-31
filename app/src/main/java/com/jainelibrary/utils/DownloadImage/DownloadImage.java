package com.jainelibrary.utils.DownloadImage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jainelibrary.R;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.OutputStream;

public class DownloadImage {

    public static void downloadImageFromURL(Activity activity, String imageUrl) {

        if (!Utils.mediaStoreDownloadsDir.exists())
            Utils.mediaStoreDownloadsDir.mkdirs();

        Utils.showProgressDialog(activity, "Please Wait...", false);

        Target imageTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Utils.dismissProgressDialog();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, "JRL_"+System.currentTimeMillis()+".jpeg");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                ContentResolver contentResolver = activity.getContentResolver();
                Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                // Open an output stream to write the image
                try (OutputStream out = contentResolver.openOutputStream(imageUri)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    //Utils.showInfoDialog(activity, "Image Saved successfully.");
                    Toast.makeText(activity.getApplicationContext(), "Image Saved!.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Utils.dismissProgressDialog();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Utils.dismissProgressDialog();
            }
        };

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.noimage)
                .into(imageTarget);
    }


    public static void downloadImage(Activity activity, String imageUrl, Handler.Callback callback) {

        if (!Utils.mediaStoreDownloadsDir.exists())
            Utils.mediaStoreDownloadsDir.mkdirs();

        Utils.showProgressDialog(activity, "Please Wait...", false);


        Target imageTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Utils.dismissProgressDialog();
                Message msg = new Message();
                // Wrap the bitmap in a Message
                msg.obj = bitmap;

                // Pass the message to the callback
                callback.handleMessage(msg);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Utils.dismissProgressDialog();
                Message msg = new Message();
                msg.obj = null;
                callback.handleMessage(msg);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Utils.dismissProgressDialog();
            }
        };

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.noimage)
                .into(imageTarget);
    }


}

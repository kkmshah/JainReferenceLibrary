package com.jainelibrary.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CaptureRecyclerView {
    private static final String TAG = CaptureRecyclerView.class.getSimpleName();

    RecyclerView recyclerView;

    public CaptureRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    private static Bitmap cropBitmap(Bitmap originalBitmap, int left, int top, int right, int bottom) {
        // Calculate the width and height of the cropped area
        int width = right - left;
        int height = bottom - top;

        // Create a new Bitmap for the cropped image
        Bitmap croppedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Create a Rect object to define the cropping region
        Rect cropRect = new Rect(left, top, right, bottom);

        // Create a Canvas and draw the cropped portion onto the new Bitmap
        android.graphics.Canvas canvas = new android.graphics.Canvas(croppedBitmap);
        canvas.drawBitmap(originalBitmap, cropRect, new Rect(0, 0, width, height), null);
        return croppedBitmap;
    }

    public String capture(int maxImageHeight, int backgroundColor, String filePath) {
        // Create a Bitmap of the RecyclerView content
        ArrayList<Bitmap> recyclerViewBitmap = getRecyclerViewScreenshot(recyclerView, maxImageHeight, backgroundColor);

        // Save the Bitmap as an image file (optional)
        return saveImageAsPDF(filePath, recyclerViewBitmap);
        //return saveBitmapAsImage(recyclerViewBitmap);
    }

    public ArrayList<String> captureImages(int maxImageHeight, int backgroundColor, String filePath) {
        // Create a Bitmap of the RecyclerView content
        ArrayList<Bitmap> recyclerViewBitmap = getRecyclerViewScreenshot(recyclerView, maxImageHeight, backgroundColor);
        ArrayList<String> files = new ArrayList<String>();
        // Save the Bitmap as an image file (optional)
        //return saveBitmapAsImage(recyclerViewBitmap);
        for (Bitmap file : recyclerViewBitmap) {
            files.add(saveBitmapAsImage(file , filePath));
        }
        return files;
    }

    private String saveImageAsPDF(String filePath, ArrayList<Bitmap> imagePaths) {
        String timeStamp = new SimpleDateFormat("yyyy-mm-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        String fileName = "recyclerViewScreenshot_" + timeStamp + "";
        try {
            return PdfCreator.createBitmapImagesToPDF(filePath, fileName, imagePaths);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Bitmap> getRecyclerViewScreenshot(RecyclerView recyclerView, int maxImageHeight, int backgroundColor) {
        Bitmap screenshot = Bitmap.createBitmap(recyclerView.getWidth(), recyclerView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenshot);
        canvas.drawColor(backgroundColor);
        recyclerView.draw(canvas);

        int imageHeight = recyclerView.getHeight();
        int imageWidth = recyclerView.getWidth();
        int imageCount = 1;
        if (imageHeight > maxImageHeight) {
            imageCount = (int) Math.ceil(recyclerView.getHeight()*1.0 / maxImageHeight);
            imageHeight = maxImageHeight;
        }
        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        for (int i = 0; i < imageCount; i++) {
                    //return  screenshot;
            images.add(cropBitmap(screenshot, 0, i * imageHeight, imageWidth, (i + 1) * imageHeight));
        }
        return images;
    }
//    private Bitmap resizeBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
//        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
//    }

//
    private String saveBitmapAsImage(Bitmap bitmap, String filePath) {


        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String timeStamp = new SimpleDateFormat("yyyy-mm-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        String fileName = "recyclerViewCapture_" + timeStamp + ".png";

        File file = new File(folder, fileName);
        Log.i(TAG, "Save file path:" + file);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file.getPath();
    }
}

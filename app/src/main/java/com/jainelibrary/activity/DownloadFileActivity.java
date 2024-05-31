package com.jainelibrary.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.jainelibrary.R;
import com.jainelibrary.model.BooksDataModel;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DownloadFileActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ProgressDialog pDialog;
    PhotoView photoView;
    private BooksDataModel.BookImageDetailsModel mBookDataModels = new BooksDataModel.BookImageDetailsModel();
    private BookListResModel.BookDetailsModel mKeywordDataModel = new BookListResModel.BookDetailsModel();
    private ArrayList<BooksDataModel> mBookDetailsList = new ArrayList<>();
    private String strPageName;
    private RelativeLayout rlFile;
    Layout_to_Image mConvertImage;
    private boolean isPermissionGranted = false;
    private TextView tvBookDetails, textheader;
    private int position;

    ArrayList<BooksDataModel.BookImageDetailsModel> mImageList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);
        loadUiElements();
        declaration();
    }
    private void loadUiElements() {
        textheader = findViewById(R.id.textheader);
        photoView = findViewById(R.id.ivZoom);
        rlFile = findViewById(R.id.rlFile);
        tvBookDetails = findViewById(R.id.tvBookDetails);
    }

    private void declaration() {
        mKeywordDataModel = (BookListResModel.BookDetailsModel) getIntent().getSerializableExtra("modelPdfOtherData");

        boolean isUrl = getIntent().getBooleanExtra("url", false);

        if (isUrl) {
            String strImages = getIntent().getStringExtra("image");
            Picasso.get().load(strImages).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(photoView);
        }
        else {
            //  byte[] imgBytes = getIntent().getByteArrayExtra("image");

            //   Bitmap myBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            photoView.setImageBitmap(BitmapFactory.decodeFile(mKeywordDataModel.getTempImageArray().getAbsolutePath()));
            //photoView.setImageBitmap(myBitmap);
        }
//
//        boolean isBytesData = false;
//        Bitmap imageBitmap = null;
//
//        String strByteImageUrl = mBookDetails.getPage_bytes();
//        if (mBookDetails.getType().equalsIgnoreCase("1"))
//        {
//            if (strByteImageUrl != null && strByteImageUrl.length() > 0) {
//                byte[] imgBytes = Base64.decode(strByteImageUrl, Base64.DEFAULT);
//                imageBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
//                if (imageBitmap != null) {
//                    isBytesData = true;
//                }
//            }
//        }
//
//        Log.e("position----", "position---" + position);
//        Log.e("strImages----", "strImages---" + strImages);
//
//        if (isBytesData)
//        {
//            photoView.setImageBitmap(imageBitmap);
//        }
//        else if (strImages != null && strImages.length() > 0) {
//            Picasso.get().load(strImages).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(photoView);
//            //  Glide.with(DownloadFileActivity.this).load(strImages).into(photoView);//Picasso.get().load(strImages).into(photoView);
//        }
        if (mKeywordDataModel != null) {
            String strBookname = mKeywordDataModel.getBook_name();
            String strPageNo = mKeywordDataModel.getPage_no();
            String strAutherName = mKeywordDataModel.getAuthor_name();
            String strTranslator = mKeywordDataModel.getTranslator();
            String strEditorName = mKeywordDataModel.getEditor_name();
            String strFinalWord = null;
            if (strBookname != null && strBookname.length() > 0) {
                strFinalWord = strBookname;
            } else {
                strBookname = "";
            }
            if (strPageNo != null && strPageNo.length() > 0) {
                strPageNo = ", " + " P." + strPageNo;
                strFinalWord = strBookname + strPageNo;
                //strFinalWord.concat("," + " P." + strPageNo);
            } else {
                strPageNo = "";
            }
            if (strAutherName != null && strAutherName.length() > 0) {
                strAutherName = ", " + strAutherName  /*"&#91;" + "ले" + "&#93; "*/;
                strFinalWord = strBookname + strPageNo + strAutherName;

            } else {
                strAutherName = "";
            }
            if (strTranslator != null && strTranslator.length() > 0) {
                strTranslator = ", " + strTranslator  /*"&#91;" + "अनु" + "&#93; "*/;
                strFinalWord = strBookname + strPageNo + strAutherName + strTranslator;

            } else {
                strTranslator = "";
            }
            if (strEditorName != null && strEditorName.length() > 0) {
                strEditorName = ", " + strEditorName  /*"&#91;" + "संपा" + "&#93;"*/;
                strFinalWord = strBookname + strPageNo + strAutherName + strTranslator + strEditorName;

            } else {
                strEditorName = "";
            }
            if (strFinalWord != null && strFinalWord.length() > 0) {
                tvBookDetails.setText(Html.fromHtml(strFinalWord));
            }
        }
        setHeader();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    File file = Utils.saveBitMap(DownloadFileActivity.this, rlFile);    //which view you want to pass that view as parameter
                    if (file != null) {
                        Utils.dismissProgressDialog();
                        Utils.showInfoDialog(DownloadFileActivity.this, "Image Downloaded Successfully");
                        Log.i("TAG", "Drawing saved to the gallery!");
                    } else {
                              Utils.dismissProgressDialog();
                            Log.i("TAG", "Oops! Image could not be saved.");
                    }
                }
                return;
            }
        }
    }

    private void setHeader() {

        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        ImageView ivdownload = headerView.findViewById(R.id.ivDelete);
        TextView ivKey = headerView.findViewById(R.id.tvKey);
        ivKey.setVisibility(View.GONE);
        LinearLayout llItems = headerView.findViewById(R.id.llAddItem);
        llItems.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); }
        });
        ivdownload.setVisibility(View.INVISIBLE);
        ivdownload.setImageResource(R.mipmap.download);
        ivdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {//
                        Bitmap b = null;//create directory if not exist
                        File dir = new File("/sdcard/tempfolder/");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File output = Utils.saveBitMap(DownloadFileActivity.this, rlFile);
                        OutputStream os = null;
                        try {
                            os = new FileOutputStream(output);
                            b.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.flush();
                            os.close();//this code will scan the image so that it will appear in your gallery when you open next time
                            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{output.toString()}, null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        public void onScanCompleted(String path, Uri uri) {
                                            Log.d("appname", "image is saved in gallery and gallery is refreshed.");
                                        }
                                    }); }
                        catch (Exception e) {
                        }
                        ////////////////////////
                        File file = Utils.saveBitMap(DownloadFileActivity.this, rlFile);    //which view you want to pass that view as parameter
                        if (file != null) {
                            Utils.dismissProgressDialog();
                            Utils.showInfoDialog(DownloadFileActivity.this, "Page Downloaded successfully");//:-" + file.getAbsolutePath());
                            Log.i("TAG", "Drawing saved to the gallery!");
                        } else {
                            Utils.dismissProgressDialog();
                            Log.i("TAG", "Oops! Image could not be saved.");
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                    }
                }
            }
        });
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Zoom View");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

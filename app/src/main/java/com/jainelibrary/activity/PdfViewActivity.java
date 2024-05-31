package com.jainelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.jainelibrary.R;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class PdfViewActivity extends AppCompatActivity {
    private String bookName,strPdfUrl;
    private PDFView pdfView;
    private MyShelfResModel.MyShelfModel fileModel = new MyShelfResModel.MyShelfModel();
    private boolean isNote = false;
    private String strHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = findViewById(R.id.pdfView);

        if (getIntent() != null) {
            fileModel = (MyShelfResModel.MyShelfModel) getIntent().getSerializableExtra("fileModel");
            strPdfUrl = getIntent().getStringExtra("pdf");
            isNote = getIntent().getBooleanExtra("isNote",false);
            bookName = getIntent().getStringExtra("bookName");
            String[] allowHeader = {Utils.TYPE_UNIT_RELATION_TREE, Utils.TYPE_UNIT, Utils.TYPE_BIODATA, Utils.TYPE_RELATION};

            boolean canAddHeader = fileModel != null ? Arrays.asList(allowHeader).contains(fileModel.getFileType()) : false;

            if(canAddHeader) {
                strHeader = fileModel.getType_name()  + " - " + bookName ;
            }
            setHeader();
            if (strPdfUrl != null && strPdfUrl.length() > 0) {
                Log.e("pdfUrl : ", "url : " + strPdfUrl);
                Utils.showProgressDialog(this,"Please wait..." ,false);
                downloadAndOpenPDF();
            }
        }
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView menu = headerView.findViewById(R.id.ivBack);
        ImageView ivNote = headerView.findViewById(R.id.ivDelete);


        ivNote.setImageResource(R.drawable.notes);
        TextView tvKey = headerView.findViewById(R.id.tvKey);
        tvKey.setVisibility(View.VISIBLE);
        tvKey.setText("NOTES");
        TextView tvPage = headerView.findViewById(R.id.tvPage);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        if (isNote){
            llAddItem.setVisibility(View.VISIBLE);
            ivNote.setVisibility(View.VISIBLE);
        }else{
            llAddItem.setVisibility(View.GONE);
        }

        if (strHeader != null && strHeader.length() > 0) {
            tvPage.setText("" + strHeader);
        } else {
            tvPage.setText("" + bookName);
        }
        menu.setImageResource(R.mipmap.backarrow);
        menu.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PdfViewActivity.this,NotesActivity.class);
                i.putExtra("strTypeRef",fileModel.getType_ref());
                i.putExtra("strType",fileModel.getType());
                i.putExtra("strKeyword",fileModel.getType_name());
                i.putExtra("strKeywordId",fileModel.getId());
                i.putExtra("strNotes",fileModel.getNotes());
                i.putExtra("isMyShelf",true);
                startActivity(i);
            }
        });
    }

    void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {
                File pdfFile = downloadFile(strPdfUrl);
                Utils.dismissProgressDialog();
                pdfView.fromFile(pdfFile).load();
            }
        }).start();

    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath() + System.currentTimeMillis() + ".pdf");
            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            int totalsize = urlConnection.getContentLength();

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                int downloadedSize = bufferLength;
                float per = ((float) downloadedSize / totalsize) * 100;

            }
            // close the output stream when complete //
            fileOutput.close();

        } catch (final Exception e) {
            Utils.dismissProgressDialog();
            e.printStackTrace();
        }
        return file;
    }

}

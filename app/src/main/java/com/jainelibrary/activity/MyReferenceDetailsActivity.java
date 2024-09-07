package com.jainelibrary.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.GsonBuilder;
import com.jainelibrary.BuildConfig;
import com.jainelibrary.R;
import com.jainelibrary.adapter.MyReferenceFilesGalleryAdapter;
import com.jainelibrary.model.AddMyShelfModel;
import com.jainelibrary.model.DeleteMyShelfResModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.FileUtils;
import com.jainelibrary.utils.PdfCreator;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;

import static com.jainelibrary.utils.Utils.getBitmapFromView;

public class MyReferenceDetailsActivity extends AppCompatActivity implements MyReferenceFilesGalleryAdapter.MyShelfInterFaceListener {
    private static final String TAG = MyReferenceDetailsActivity.class.getSimpleName();
    private RecyclerView rvMyShelfImage;
    private MyReferenceFilesGalleryAdapter mAdapter;
    ArrayList<MyShelfResModel.MyShelfModel.MyShelfImageModel> mList = new ArrayList<>();
    String strBookName, strKeyword, strType, strNotes, strTypeRef, strKId;
    private ArrayList<String> removeMyShelfList = new ArrayList<>();
    private String strUserId;

    LinearLayout llShare, llDelete, llNotes, llDownload;
    ArrayList<Uri> mImageUriList = new ArrayList<>();
    ArrayList<HashMap<String, Uri>> imageFiles = new ArrayList<HashMap<String, Uri>>();

    ArrayList<AddMyShelfModel> addMyShlefList = new ArrayList<>();

    ArrayList<String> myPageIdArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shelf);
        Log.e(TAG,"aaaa");
        loadUiElements();
        declaration();
        setHeader();
        btnEventListenet();
    }

    private void btnEventListenet() {
        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharedPrefManager.getInstance(MyReferenceDetailsActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    // getShareDialog();
                    if (mImageUriList != null && mImageUriList.size() > 0) {
                        mImageUriList.clear();
                        mImageUriList = new ArrayList<>();
                    }

                    if (imageFiles != null && imageFiles.size() > 0) {
                        if (imageFiles != null && imageFiles.size() > 0) {
                            for (int i = 0; i < imageFiles.size(); i++) {
                                HashMap<String, Uri> map = imageFiles.get(i);
                                for (String key : map.keySet()) {
                                    mImageUriList.add(map.get(key));
                                    Log.e(TAG, "imageFilesssIn--" + mImageUriList.size() + "key -- " + map.get(key));
                                }
                            }
                        }
                        if (mImageUriList != null && mImageUriList.size() > 0) {
                            sharePDFFiles(strBookName);
                            // getShareDialog();
                        }
                    } else {
                        InfoDialog();
                    }
                }
            }
        });

        llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImageUriList != null && mImageUriList.size() > 0) {
                    mImageUriList.clear();
                    mImageUriList = new ArrayList<>();
                }

                if (imageFiles != null && imageFiles.size() > 0) {
                    if (imageFiles != null && imageFiles.size() > 0) {
                        for (int i = 0; i < imageFiles.size(); i++) {
                            HashMap<String, Uri> map = imageFiles.get(i);
                            for (String key : map.keySet()) {
                                mImageUriList.add(map.get(key));
                                Log.e(TAG, "imageFilesssIn--" + mImageUriList.size() + "key -- " + map.get(key));
                            }
                        }
                    }
                    if (mImageUriList != null && mImageUriList.size() > 0) {
                        try {
                            downloadAndStoreImages(mImageUriList, strBookName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    InfoDialog();
                }
            }
        });

        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUserId = SharedPrefManager.getInstance(MyReferenceDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                if (myPageIdArrayList != null && myPageIdArrayList.size() > 0) {
                    getDeleteDialog(strUserId, "Are you sure want to remove this select images ?");
                } else {
                    InfoDialog();
                }
            }
        });

        llNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyReferenceDetailsActivity.this, NotesActivity.class);
                i.putExtra("strType", strType);
                i.putExtra("strTypeRef", strTypeRef);
                i.putExtra("strNotes", strNotes);
                i.putExtra("strKeyword", strKeyword);
                i.putExtra("isMyShelf", true);
                i.putExtra("strKeywordId", strKId);
                startActivity(i);
            }
        });
    }

    private void declaration() {
        MyShelfResModel.MyShelfModel myShelfModel = new MyShelfResModel.MyShelfModel();
        myShelfModel = (MyShelfResModel.MyShelfModel) getIntent().getSerializableExtra("myShelfModel");

        if (myShelfModel != null) {
            strBookName = myShelfModel.getBook_name();
            strKeyword = myShelfModel.getType_name();
            strType = myShelfModel.getType();
            strTypeRef = myShelfModel.getType_ref();
            strNotes = myShelfModel.getNotes();
            strKId = myShelfModel.getId();
            mList = new ArrayList<>();

            mList = myShelfModel.getPage_no();
            if (mList != null && mList.size() > 0) {
                setMyShelfList(mList);
            }
        }

    }

    private void setMyShelfList(ArrayList<MyShelfResModel.MyShelfModel.MyShelfImageModel> myShelfList) {
        if (myShelfList == null || myShelfList.size() == 0) {
            return;
        }

        rvMyShelfImage.setHasFixedSize(true);
        rvMyShelfImage.setLayoutManager(new LinearLayoutManager(MyReferenceDetailsActivity.this));
        rvMyShelfImage.setVisibility(View.VISIBLE);
        mAdapter = new MyReferenceFilesGalleryAdapter(MyReferenceDetailsActivity.this, myShelfList, this);
        rvMyShelfImage.setAdapter(mAdapter);
    }


    private void loadUiElements() {
        rvMyShelfImage = findViewById(R.id.rvMyShelfImage);
        llShare = findViewById(R.id.llShare);
        llDelete = findViewById(R.id.llDelete);
        llNotes = findViewById(R.id.llNotes);
        llDownload = findViewById(R.id.llDownload);
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        ImageView ivDelete = headerView.findViewById(R.id.ivDelete);
        llAddItem.setVisibility(View.VISIBLE);
        ivDelete.setVisibility(View.GONE);
        ivDelete.setImageResource(R.mipmap.delete);
        ivDelete.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        ivBack.setVisibility(View.VISIBLE);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removeMyShelfList != null && removeMyShelfList.size() > 0) {
                    //callDeleteMyShelfApi();
                } else {
                    Utils.showInfoDialog(MyReferenceDetailsActivity.this, "Please select image");
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (strKeyword != null && strKeyword.length() > 0) {
            ssb.append(strKeyword + " ");
        }

        if (ssb.length()>0){
            ssb.setSpan(new ImageSpan(MyReferenceDetailsActivity.this, R.drawable.ic_baseline_chevron_right_24),
                    ssb.length() - 1,
                    ssb.length(),
                    0);
        }

        if (strBookName != null && strBookName.length() > 0) {
            ssb.append(strBookName + " ");
        }
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText(ssb);
    }

    @Override
    public void onDetailsClick(ArrayList<MyShelfResModel.MyShelfModel.MyShelfImageModel> searchList, int position) {
        String strImage = searchList.get(position).getUrl();
        Intent i = new Intent(MyReferenceDetailsActivity.this, ZoomImageActivity.class);
        i.putExtra("image", strImage);
        i.putExtra("url", true);
        startActivity(i);
    }


    private void callDeleteMyShelfApi(String strUserId) {
        Utils.showProgressDialog(MyReferenceDetailsActivity.this, "Please Wait...", false);
        ApiClient.deleteMyShelf(strUserId, new Callback<DeleteMyShelfResModel>() {
            @Override
            public void onResponse(Call<DeleteMyShelfResModel> call, retrofit2.Response<DeleteMyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.showInfoDialog(MyReferenceDetailsActivity.this, "" + response.body().getMessage());
                        onBackPressed();
                    } else {
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<DeleteMyShelfResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }

    private void callDeleteMyShelfImageApi(JSONArray myPageIdArrayList) {
        Utils.showProgressDialog(MyReferenceDetailsActivity.this, "Please Wait...", false);
        ApiClient.deleteMyReferenceImage(myPageIdArrayList, new Callback<DeleteMyShelfResModel>() {
            @Override
            public void onResponse(Call<DeleteMyShelfResModel> call, retrofit2.Response<DeleteMyShelfResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                    if (response.body().isStatus()) {
                        Dialog dialog = new IosDialog.Builder(MyReferenceDetailsActivity.this)
                                .setMessage(response.body().getMessage())
                                .setMessageColor(Color.parseColor("#1565C0"))
                                .setMessageSize(18)
                                .setPositiveButtonColor(Color.parseColor("#981010"))
                                .setPositiveButtonSize(18)
                                .setPositiveButton("OK", new IosDialog.OnClickListener() {
                                    @Override
                                    public void onClick(IosDialog dialog, View v) {
                                        dialog.dismiss();
                                        onBackPressed();
                                    }
                                }).build();
                        dialog.show();
                        onBackPressed();
                    } else {
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<DeleteMyShelfResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }

    @Override
    public void onDeleteImage(ArrayList<MyShelfResModel.MyShelfModel.MyShelfImageModel> myShelfImageModelArrayList, int position, boolean isSelected) {
        if (myShelfImageModelArrayList != null && myShelfImageModelArrayList.size() > 0) {
            String strImage = myShelfImageModelArrayList.get(position).getUrl();
            String strPageNo = myShelfImageModelArrayList.get(position).getPage_no();
            String strPageID = myShelfImageModelArrayList.get(position).getPage_id();
            Log.e(TAG, "strImage---" + strImage);
            if (strImage != null && strImage.length() > 0) {
                if (!isSelected) {

                    for (int i = 0; i < addMyShlefList.size(); i++) {
                        String strUrl = addMyShlefList.get(i).getStrUrl();
                        if (strUrl != null && strUrl.equalsIgnoreCase(strImage)) {
                            addMyShlefList.remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < myPageIdArrayList.size(); i++) {
                        String strId = myPageIdArrayList.get(i);
                        if (strPageID != null && strPageID.equalsIgnoreCase(strId)) {
                            myPageIdArrayList.remove(i);
                            break;
                        }
                    }

                    for (int i = 0; i < imageFiles.size(); i++) {
                        Set<String> strImages = imageFiles.get(i).keySet();
                        for (String key : strImages) {
                            if (key.equalsIgnoreCase(strImage)) {
                                imageFiles.remove(i);
                                break;
                            }
                        }
                    }
                } else {
                    if (strPageID != null && strPageID.length() > 0) {
                        myPageIdArrayList.add(strPageID);
                    }
                    AddMyShelfModel mAddShelfModel = new AddMyShelfModel();
                    mAddShelfModel.setStrPageNo(strPageNo);
                    mAddShelfModel.setStrUrl(strImage);
                    addMyShlefList.add(mAddShelfModel);

                    Glide.with(MyReferenceDetailsActivity.this).asBitmap().load(strImage).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                            resource.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
                            HashMap<String, Uri> map = new HashMap<>();
                            map.put(strImage, Uri.parse(MediaStore.Images.Media.insertImage(MyReferenceDetailsActivity.this.getContentResolver(), resource, "JRL_" + System.currentTimeMillis(), null)));
                            imageFiles.add(map);
                        }
                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            // Handle cleanup if the target is cleared
                        }
                    });
                }
            }
        }
    }

    public void getDeleteDialog(String strUserId, String strTite) {

        Dialog dialog = new IosDialog.Builder(this)
                .setMessage(strTite)
                .setMessageColor(Color.parseColor("#1565C0"))
                .setMessageSize(18)
                .setNegativeButtonColor(Color.parseColor("#981010"))
                .setNegativeButtonSize(18)
                .setNegativeButton("No", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButtonColor(Color.parseColor("#981010"))
                .setPositiveButtonSize(18)
                .setPositiveButton("Yes", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        JSONArray myShelfIdArray = new JSONArray();
                        if ((myPageIdArrayList != null) && (myPageIdArrayList.size() > 0)) {
                            for (int j = 0; j < myPageIdArrayList.size(); j++) {
                                String strPageNo = myPageIdArrayList.get(j);
                                JSONObject jsonObject = new JSONObject();
                                Log.e("strPageNo-", "strPageNo--" + strPageNo);
                                try {
                                    if (strPageNo != null && strPageNo.length() > 0) {
                                        jsonObject.put("ids", strPageNo);
                                    }
                                    if (strPageNo != null) {
                                        myShelfIdArray.put(jsonObject);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (myShelfIdArray != null && myShelfIdArray.length() > 0) {
                                callDeleteMyShelfImageApi(myShelfIdArray);
                            }
                        }
                    }
                })
                .build();
        dialog.show();
    }


    private void sharePDFFiles(String strEdtRenamefile) {
        try {
            if (mImageUriList != null && mImageUriList.size() > 0) {
                List<String> mImagePath = new ArrayList<>();
                for (int i = 0; i < mImageUriList.size(); i++) {
                    String strImagePath = FileUtils.getPath(MyReferenceDetailsActivity.this, mImageUriList.get(i));
                    if (strImagePath != null && strImagePath.length() > 0) {
                        mImagePath.add(strImagePath);
                    }
                }

                String strPdfFile = null;
                if (mImagePath != null && mImagePath.size() > 0) {

                    strPdfFile = PdfCreator.creatPdf(Utils.getMediaStorageDir(getApplicationContext()).getAbsolutePath(), strEdtRenamefile, mImagePath);
                }

                if (strPdfFile != null && strPdfFile.length() > 0) {
                    Log.e(TAG, "strZipperFileName--" + strPdfFile);
                    Uri fileUri = Uri.fromFile(new File(strPdfFile));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    {
                        fileUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(strPdfFile));
                    }
                    Log.e(TAG, "fileUri--" + fileUri);

                    String PackageName;
                    PackageName = MyReferenceDetailsActivity.this.getPackageName();

                    String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                    String strMessage = " " + strBookName;

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                    intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    intent.setType("application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Log.e(TAG, "imageFilesss--" + mImageUriList.size());
                    startActivity(Intent.createChooser(intent, shareData));
                }

                if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
                    this.mImageUriList.clear();
                }
            } else {
                Utils.showInfoDialog(MyReferenceDetailsActivity.this, "Please select image");
            }
        } catch (Exception e) {
            Log.e("Exception Error", "Error---" + e.getMessage());
        }
    }

    private void downloadAndStoreImages(String strFileName, Bitmap imageBitmap) throws IOException {
        Log.e("sucess", "success");
        String strFileFinalPath = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        if (!Utils.mediaStoreDownloadsDir.exists()) {
            Utils.mediaStoreDownloadsDir.mkdirs();
        }

        if (imageBitmap != null) {
            Uri uri = Utils.getImageUri(MyReferenceDetailsActivity.this, imageBitmap);

            strFileFinalPath = strFileName + strFileFinalPath + ".jpg";

            File file = new File(Utils.mediaStoreDownloadsDir.getAbsolutePath(), strFileFinalPath);

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Log.d("bitmap--", "bitmap---" + bitmap);

            if (!file.exists()) {
                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            Utils.showInfoDialog(MyReferenceDetailsActivity.this, "Downloaded Successfully");// + "\n" + Utils.mediaStoreDownloadsDir.getAbsolutePath() + "/" + strFileName, Toast.LENGTH_SHORT).show();
        } else {
            Utils.showInfoDialog(this, "Image not found");
        }
    }
    private void downloadAndStoreImages(ArrayList<Uri> mImageUriList, String strFileName) throws IOException {
        String strFileFinalPath = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        if (!Utils.mediaStoreDownloadsDir.exists()) {
            Utils.mediaStoreDownloadsDir.mkdirs();
        }


        for (int i = 0; i < mImageUriList.size(); i++) {
            Uri uri = mImageUriList.get(i);

            strFileFinalPath = strFileName + strFileFinalPath + ".jpg";

            File file = new File(Utils.mediaStoreDownloadsDir.getAbsolutePath(), strFileFinalPath);

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Log.d("bitmap--", "bitmap---" + bitmap);

            View view = rvMyShelfImage.getChildAt(i);
            try {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            } catch (Exception ex) {
                Log.d(TAG, "Non Navigation button");
            }
            if (!file.exists()) {
                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    downloadAndStoreImages(strFileName, getBitmapFromView(view));
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Utils.showInfoDialog(MyReferenceDetailsActivity.this, "Downloaded Successfully ");// + "\n" + Utils.mediaStoreDownloadsDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        if (this.mImageUriList != null && this.mImageUriList.size() > 0) {
            this.mImageUriList.clear();
        }
    }
    /*public void deleteSelectedFile() {


     *//*if (removeMyShelfList != null && removeMyShelfList.size() > 0) {
            JSONArray myShelfArray = new JSONArray();
            if ((removeMyShelfList != null) && (removeMyShelfList.size() > 0)) {
                for (int j = 0; j < removeMyShelfList.size(); j++) {
                    String strPageNo = removeMyShelfList.get(j);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if (strPageNo != null && strPageNo.length() > 0) {
                            jsonObject.put("page_no", strPageNo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (myShelfArray != null && myShelfArray.length() > 0) {
                 strUserId = SharedPrefManager.getInstance(MyShelfDetailsActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                if (strUserId != null && strUserId.length() > 0) {
                    callDeleteMyShelfApi();
                }
            }
        }*//*

    }*/

    private void InfoDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MyReferenceDetailsActivity.this);
        builder.setMessage("Long Press on Image for Select.\nYou can select multiple images, please scroll.")
                .setPositiveButton("Ok", dialogClickListener)
                .show();

    }

}

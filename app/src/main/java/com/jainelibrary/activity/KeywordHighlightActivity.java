package com.jainelibrary.activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.MarkingBookModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.wc.widget.dialog.IosDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class KeywordHighlightActivity extends AppCompatActivity {
    private static final String TAG = KeywordHighlightActivity.class.getSimpleName();
    private static final int REFERENCE_CODE = 1;
    LinearLayout llShare, llBookDetails, llNext;
    ImageView ivHighlight;
    BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
    private String strHeaderPageNo, strType, strWordName, strBookName, strBookId, strKeyWordId, strEditorName, strTranslatorName, strAuthorName, strPageNo, strSutraName;
    int pageNo;
    private String strReferenceImage;
    ArrayList<MarkingBookModel.MarkingBook.BookDetails> markingBookDetails = new ArrayList<>();
    private Bitmap imageBitmap;
    private BottomSheetDialog bottomSheetDialog;
    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    private String PackageName;
    LinearLayout llheader, llfooter;
    TextView tvHeaderRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_highlight);
        Log.e(TAG, "ABC");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        loadUiElements();
        tvHeaderRight.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        declaration();
        setHeader();
        onBtnEventListener();
    }

    private void declaration() {
        Intent i = getIntent();
        if (i != null) {
            mBookDataModels = (BookListResModel.BookDetailsModel) i.getSerializableExtra("model");
            if (mBookDataModels != null) {
                strBookName = mBookDataModels.getBook_name();
//                Log.e(strBookName, strWordName);
                strWordName = mBookDataModels.getKeyword();
                strBookId = i.getExtras().getString("bookid");
                strPageNo = i.getExtras().getString("pageno");
                // strHeaderPageNo = mBookDataModels.getPage_no();
                strHeaderPageNo = i.getExtras().getString("pdfpage");
                strAuthorName = mBookDataModels.getAuthor_name();
                strTranslatorName = mBookDataModels.getTranslator();
                strEditorName = mBookDataModels.getEditor_name();
                strSutraName = mBookDataModels.getStrSutraName();
                strType = mBookDataModels.getFlag();
                strKeyWordId = mBookDataModels.getKeywordId();
                Log.e("book details ", strBookName + " " + strWordName + " " + strBookId + " " + strPageNo + " " + strHeaderPageNo + " " + strAuthorName + " " + strTranslatorName + " " + strEditorName + " " + strSutraName + " " + strType);
                Log.e(strBookId, strPageNo + "    " + strKeyWordId);
                //pageModel = mBookDataModels.getPageModels().get()
            }
            if (strPageNo != null && strPageNo.length() > 0) {
                pageNo = Integer.parseInt(strPageNo);
                Log.e("Page NO", String.valueOf(pageNo));
            }

        }

        llBookDetails.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2.0f
        );

        llBookDetails.setLayoutParams(param);
        callMarkingBook();

    }

    private void onBtnEventListener() {
        llBookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(KeywordHighlightActivity.this, BookDetailsActivity.class);
                i.putExtra("model", mBookDataModels);
                startActivity(i);
            }
        });

        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(KeywordHighlightActivity.this, ReferencePageActivity.class);
                i.putExtra("model", mBookDataModels);
                i.putExtra("pdfpage", strHeaderPageNo);
                i.putExtra("pageno", strPageNo);
                i.putExtra("bookname", strBookName);
                i.putExtra("keyword", strWordName);
                startActivity(i);
            }
        });
        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharedPrefManager.getInstance(KeywordHighlightActivity.this).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (isLogin) {
                    if (imageBitmap != null) {
                        getShareDialog();
                    } else {
                        Utils.showInfoDialog(KeywordHighlightActivity.this, "Image Not Found");
                    }
                } else {
                    askLogin();
                }
            }
        });
        ivHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (imageBitmap != null) {
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byte[] byteArray = bStream.toByteArray();

                    Intent anotherIntent = new Intent(KeywordHighlightActivity.this, ZoomImageActivity.class);
                    anotherIntent.putExtra("image", strReferenceImage);
                    startActivity(anotherIntent);
                    finish();*/
                if (strReferenceImage != null && strReferenceImage.length() > 0) {
                    Intent i = new Intent(KeywordHighlightActivity.this, DownloadFileActivity.class);
                    // i.putExtra("modelPdf", mPfdList.get(position));
                    i.putExtra("modelPdfOtherData", mBookDataModels);
                    i.putExtra("image", strReferenceImage);
                    startActivity(i);
                }
                //       Intent intent = new Intent(KeywordHighlightActivity.this, ZoomImageActivity.class);
                //     intent.putExtra("BitmapImage", imageBitmap);
                //startActivity(intent);
            }

        });
    }

    @Override
    protected void onSaveInstanceState(Bundle InstanceState) {
        super.onSaveInstanceState(InstanceState);
        InstanceState.clear();
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(KeywordHighlightActivity.this, REFERENCE_CODE);
    }

    private void loadUiElements() {
        llheader = findViewById(R.id.llHeader);
        llfooter = findViewById(R.id.llFooter);
        ivHighlight = findViewById(R.id.ivHighlight);
        llShare = findViewById(R.id.llShare);
        llBookDetails = findViewById(R.id.llBookDetails);
        llNext = findViewById(R.id.llNext);
        tvHeaderRight = findViewById(R.id.tvHeaderRight);
    }

    private void getScreenShot() {

        CardView z = (CardView) findViewById(R.id.cardImage);
        z.setDrawingCacheEnabled(true);
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();
        z.layout(0, 0, totalWidth, totalHeight);
        z.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(z.getDrawingCache());
        z.setDrawingCacheEnabled(false);
        //Save bitmap
        String extr = Environment.getExternalStorageDirectory().toString() + File.separator + "Folder";
        String fileName = new SimpleDateFormat("yyyyMMddhhmm'_report.jpg'").format(new Date());
        File myPath = new File(extr, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
            Utils.showInfoDialog(KeywordHighlightActivity.this, fileName + " screenshot captured");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        TextView tvPageName = headerView.findViewById(R.id.tvPage);
        TextView tvPgCount = headerView.findViewById(R.id.tvPgCount);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(strWordName + " ");
        ssb.setSpan(new ImageSpan(KeywordHighlightActivity.this, R.drawable.ic_baseline_chevron_right_24),
                ssb.length() - 1,
                ssb.length(),
                0);
        ssb.append(strBookName + " ");
        if (strHeaderPageNo != null) {
            ssb.setSpan(new ImageSpan(KeywordHighlightActivity.this, R.drawable.ic_baseline_chevron_right_24),
                    ssb.length() - 1,
                    ssb.length(),
                    0);
            ssb.append(strHeaderPageNo);
        }
        tvPageName.setText(ssb);
    }

    public void callMarkingBook() {

        if (!ConnectionManager.checkInternetConnection(KeywordHighlightActivity.this)) {
            Utils.showInfoDialog(KeywordHighlightActivity.this, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(KeywordHighlightActivity.this, "Please Wait...", false);
        ApiClient.getMarkingBookPage(strKeyWordId, strBookId, String.valueOf(pageNo), new Callback<MarkingBookModel>() {
            @Override
            public void onResponse(Call<MarkingBookModel> call, retrofit2.Response<MarkingBookModel> response) {
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<MarkingBookModel.MarkingBook> markingBookModels = new ArrayList<>();
                        markingBookModels = response.body().getData();
                        if (markingBookModels != null && markingBookModels.size() > 0) {
                            strReferenceImage = markingBookModels.get(0).getPage_url();
                            markingBookDetails = markingBookModels.get(0).getBook_details();
                            if (markingBookDetails != null && markingBookDetails.size() > 0) {
                                if (strReferenceImage != null && strReferenceImage.length() > 0) {
                                    Bitmap imageBitmap = Utils.convertUrlToBitmap(strReferenceImage);
                                    if (imageBitmap != null) {
                                        getBitmap(imageBitmap);
                                    } else {
                                        Utils.dismissProgressDialog();
                                    }
                                } else {
                                    Utils.dismissProgressDialog();
                                }
                            } else {
                                Utils.dismissProgressDialog();
                                Utils.showInfoDialog(KeywordHighlightActivity.this, " Book details not found");
                            }
                        } else {
                            Utils.dismissProgressDialog();
                            Utils.showInfoDialog(KeywordHighlightActivity.this, " Data not found");
                        }
                    } else {
                        Utils.dismissProgressDialog();
                        Utils.showInfoDialog(KeywordHighlightActivity.this, "" + response.body().getMessage());
                    }
                } else {
                    Utils.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<MarkingBookModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("e    rror", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void getBitmap(Bitmap bitmap) {
        if (markingBookDetails.get(0).getX1() != null) {
            int left = Integer.parseInt(markingBookDetails.get(0).getX1());
            int top = Integer.parseInt(markingBookDetails.get(0).getY1());
            int right = Integer.parseInt(markingBookDetails.get(0).getX2());
            int bottom = Integer.parseInt(markingBookDetails.get(0).getY2());
            imageBitmap = Utils.getHiglightImage(left, top, right, bottom, bitmap);
        } else {
            imageBitmap = bitmap;
        }
        markingBookDetails.remove(0);
        if (markingBookDetails != null && markingBookDetails.size() > 0) {
            getBitmap(imageBitmap);
        } else {
            Utils.dismissProgressDialog();
            llheader.setVisibility(View.VISIBLE);
            llfooter.setVisibility(View.VISIBLE);
            ivHighlight.setImageBitmap(imageBitmap);
        }
    }

    public void getShareDialog() {

        bottomSheetDialog = new BottomSheetDialog(KeywordHighlightActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetDialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_share, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        LinearLayout bottomSheetContainer = bottomSheetDialogView.findViewById(R.id.bottomSheetContainer);
        Button btnShare = bottomSheetDialogView.findViewById(R.id.btnShare);
        Button btnMyShelf = bottomSheetDialogView.findViewById(R.id.btnMyShelf);
        Button btnDownload = bottomSheetDialogView.findViewById(R.id.btnDownload);
        TextView tvSelectedImageCount = bottomSheetDialogView.findViewById(R.id.tvSelectedImageCount);
        tvSelectedImageCount.setText("1");
        EditText edtRenameFile = bottomSheetDialogView.findViewById(R.id.edtRenameFile);
        ImageView ivClose = bottomSheetDialogView.findViewById(R.id.ivClose);
        CustomKeyboardView mKeyboardView = bottomSheetDialogView.findViewById(R.id.keyboardView);
        edtRenameFile.setText(strWordName + "," + strBookName + "," + "Page." + strPageNo);
        String strLanguage = SharedPrefManager.getInstance(KeywordHighlightActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        edtRenameFile.requestFocus();
        edtRenameFile.setShowSoftInputOnFocus(false);
        edtRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Util.hideKeyBoard(KeywordHighlightActivity.this, edtRenameFile);
                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                } else {
                    mKeyboardView.setVisibility(View.VISIBLE);
                }*/
                String strLanguage = SharedPrefManager.getInstance(KeywordHighlightActivity.this).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.selectKeyboard(mKeyboardView, mKeyboard, edtRenameFile, KeywordHighlightActivity.this, strLanguage, bottomSheetDialog,null);
//                return false;
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageBitmap != null)  {
                    CardView z = (CardView) findViewById(R.id.cardImage);
                    try {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    } catch (Exception ex) {
                        Log.d(TAG, "Non Navigation button");
                    }
                    z.setDrawingCacheEnabled(true);
                    int totalHeight = z.getChildAt(0).getHeight();
                    int totalWidth = z.getChildAt(0).getWidth();
                    z.layout(0, 0, totalWidth, totalHeight);
                    z.buildDrawingCache(true);
                    Bitmap b = Bitmap.createBitmap(z.getDrawingCache());
                    Uri uri = Utils.getImageUri(KeywordHighlightActivity.this, b);
                    String shareData = " Get Latest JainTatva Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                    String strMessage = "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + strWordName;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);

                    intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                    intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intent, shareData));
                    bottomSheetDialog.cancel();
                } else {
                    edtRenameFile.setError("Please enter 9 name");
                    edtRenameFile.requestFocus();
                }
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String strEdtRenamefile = edtRenameFile.getText().toString();
                    if (imageBitmap != null) {
                        bottomSheetDialog.cancel();
                        CardView z = (CardView) findViewById(R.id.cardImage);
                        try {
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        } catch (Exception ex) {
                            Log.d(TAG, "Non Navigation button");
                        }
                        z.setDrawingCacheEnabled(true);
                        int totalHeight = z.getChildAt(0).getHeight();
                        int totalWidth = z.getChildAt(0).getWidth();
                        z.layout(0, 0, totalWidth, totalHeight);
                        z.buildDrawingCache(true);
                        Bitmap b = Bitmap.createBitmap(z.getDrawingCache());
                        //     z.setDrawingCacheEnabled(true);
                        tvHeaderRight.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(new Date()));
                        downloadAndStoreImages(strEdtRenamefile, b);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnMyShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                JSONArray myShelfArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try {
                    if (strPageNo != null && strPageNo.length() > 0) {
                        jsonObject.put("page_no", strPageNo);
                    }
                    if (strReferenceImage != null && strReferenceImage.length() > 0) {
                        jsonObject.put("url", strReferenceImage);
                    }
                    if (strPageNo != null && strReferenceImage != null) {
                        myShelfArray.put(jsonObject);
                    } else {
                        Log.e(TAG, "isProductFile");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (myShelfArray != null && myShelfArray.length() > 0) {
                    String strUserId = SharedPrefManager.getInstance(KeywordHighlightActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                    if (strUserId != null && strUserId.length() > 0) {
                //        callAddMyShelfApi(myShelfArray, strUserId);
                    }
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });


        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        bottomSheetDialog.show();
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) bottomSheetDialogView.getParent());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet,
                                       @BottomSheetBehavior.State int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss();
                } else {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        if (mKeyboardView.getVisibility() == View.VISIBLE) {
                            mKeyboardView.setVisibility(View.GONE);
                        } else {
                            bottomSheetDialog.dismiss();
                        }
                    }
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void downloadAndStoreImages(String strFileName, Bitmap imageBitmap) throws IOException {
        String strFileFinalPath = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        if (!Utils.mediaStoreDownloadsDir.exists()) {
            Utils.mediaStoreDownloadsDir.mkdirs();
        }
        if (imageBitmap != null) {
            Uri uri = Utils.getImageUri(KeywordHighlightActivity.this, imageBitmap);
            strFileFinalPath = strFileName + strFileFinalPath + ".jpg";
            File file = new File(Utils.mediaStoreDownloadsDir.getAbsolutePath(), "JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + strWordName+".jpg");
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
            Utils.showInfoDialog(KeywordHighlightActivity.this, "Downloaded Successfully " + "\n" + Utils.mediaStoreDownloadsDir.getAbsolutePath());
        } else {
            Utils.showInfoDialog(this, "Image not found");
        }
    }
   /* private void callAddMyShelfApi(JSONArray myShelfArray, String strUserId) {
        Utils.showProgressDialog(KeywordHighlightActivity.this, "Please Wait...", false);
        ApiClient.addMyShelf(strUserId,strBookId, strKeyWordId,   strType, Utils.REF_TYPE_REFERENCE_PAGE,"JainRefLibrary" + "_" + strBookName + "_" + strPageNo + "_" + strWordName, myShelfArray,
                new Callback<AddShelfResModel>() {
                    @Override
                    public void onResponse(Call<AddShelfResModel> call, retrofit2.Response<AddShelfResModel> response) {
                        Utils.dismissProgressDialog();
                        if (response.isSuccessful()) {
                            if (response.body().isStatus()) {
                                String strNotes = response.body().getNotes();
                                String strType = response.body().getType();
                                String strTypeRef = response.body().getType_ref();
                                String strKeywordId = response.body().getType_id();
                                Toast.makeText(KeywordHighlightActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                getInfoDialogs(strNotes, strType, strTypeRef, "Do you want to add notes?", strKeywordId);
                            } else {
                                Toast.makeText(KeywordHighlightActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("error--", "ResultError--" + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                        Utils.dismissProgressDialog();
                        Log.e("error", "" + t.getMessage());
                    }
                });
    }*/

    public void getInfoDialogs(String strNotes, String strType, String strTypeRef, String strTite, String strKeywordId) {
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
                        Intent intent = new Intent(KeywordHighlightActivity.this, NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strWordName);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }

}

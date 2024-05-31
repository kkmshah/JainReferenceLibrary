package com.jainelibrary.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.jainelibrary.R;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.ReferencePageDetailsResModel;
import com.jainelibrary.retrofitResModel.ReferenceResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.viven.imagezoom.ImageZoomHelper;
import com.wc.widget.dialog.IosDialog;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;

public class ReferencePageDetailsActivity extends AppCompatActivity {
    private static final String TAG = ReferencePageDetailsActivity.class.getSimpleName();

    int type_id, reference_id, reference, reference_type;

    CardView cvPrev, cvNext;

    TextView tvTypeName, tvPageDetails, tvX1, tvY1, tvX2, tvY2;
    ImageView ivPage, ivDrawingPane;

    Bitmap tempBitmap;

    LinearLayout llSource, llBottomOptions, llMarkOptions;
    LinearLayout llEdit, llMark, llPending, llApprove, llGeneral, llSpecial, llDelete;
    LinearLayout llCancel, llUndo, llSetMark;

    Bitmap bitmapMaster;
    Canvas canvasMaster;
    Bitmap bitmapDrawingPane;
    Canvas canvasDrawingPane;
    projectPt startPt;

    boolean mark = false;
    private int oldX,newX;

    ReferencePageDetailsResModel.RefPageDetailsModel refPageDetailsModel;

    ReferencePageDetailsResModel.RefPageDetailsModel.ReferenceModel prevReference;
    ReferencePageDetailsResModel.RefPageDetailsModel.ReferenceModel nextReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_page_details);

        loadUiElements();
        onEventListner();
        declaration();
        setHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPrefManager.getInstance(ReferencePageDetailsActivity.this).getBooleanPreference("ReloadRefPageData")) {
            SharedPrefManager.getInstance(ReferencePageDetailsActivity.this).setBooleanPreference("ReloadRefPageData", false);

            finish();
            startActivity(getIntent());
        }
    }

    private void declaration() {
        type_id = getIntent().getIntExtra("type_id", 0);
        reference_id = getIntent().getIntExtra("reference_id", 0);
        reference = getIntent().getIntExtra("reference", 0);
        reference_type = getIntent().getIntExtra("reference_type", 0);

        callReferencePageDetailsApi();
    }

    private void loadUiElements() {
        cvPrev = findViewById(R.id.cvPrev);
        cvNext = findViewById(R.id.cvNext);

        tvTypeName = findViewById(R.id.tvTypeName);
        tvPageDetails = findViewById(R.id.tvPageDetails);
        ivPage = findViewById(R.id.ivPage);
        ivDrawingPane = findViewById(R.id.ivDrawingPane);
        tvX1 = findViewById(R.id.tvX1);
        tvY1 = findViewById(R.id.tvY1);
        tvX2 = findViewById(R.id.tvX2);
        tvY2 = findViewById(R.id.tvY2);

        llSource = findViewById(R.id.llSource);

        llBottomOptions = findViewById(R.id.llBottomOptions);
        llMarkOptions = findViewById(R.id.llMarkOptions);

        llEdit = findViewById(R.id.llEdit);
        llMark = findViewById(R.id.llMark);
        llPending = findViewById(R.id.llPending);
        llApprove = findViewById(R.id.llApprove);
        llGeneral = findViewById(R.id.llGeneral);
        llSpecial = findViewById(R.id.llSpecial);
        llDelete = findViewById(R.id.llDelete);

        llCancel = findViewById(R.id.llCancel);
        llUndo = findViewById(R.id.llUndo);
        llSetMark = findViewById(R.id.llSetMark);
    }

    private void onEventListner() {
        cvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReferencePageDetails(prevReference.getType_id(), prevReference.getId());
            }
        });

        cvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReferencePageDetails(nextReference.getType_id(), nextReference.getId());
            }
        });

        ivPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                int x = (int) event.getX();
                int y = (int) event.getY();

                switch(action){
                    case MotionEvent.ACTION_DOWN:
                        oldX = (int) event.getX();
                        if (mark) {
                            startPt = projectXY((ImageView) v, bitmapMaster, x, y);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mark) {
                            drawOnRectProjectedBitMap((ImageView) v, bitmapMaster, x, y);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        newX = (int) event.getX();

                        if (Math.abs( newX-oldX ) > 0) {
                            if (mark) {
                                drawOnRectProjectedBitMap((ImageView) v, bitmapMaster, x, y);
                                finalizeDrawing();
                            }
                        }
                        else {
                            Intent i = new Intent(ReferencePageDetailsActivity.this, ZoomImageActivity.class);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                            i.putExtra("image", byteArray);
                            i.putExtra("url", false);
                            startActivity(i);
                        }
                        break;
                }

                return true;
            }}
        );

        llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIndent = new Intent(ReferencePageDetailsActivity.this, EditReferenceActivity.class);
                editIndent.putExtra("type_id", type_id);
                editIndent.putExtra("reference_id", reference_id);
                editIndent.putExtra("reference", reference);
                editIndent.putExtra("reference_type", reference_type);
                editIndent.putExtra("type_value", refPageDetailsModel.getType_value());
                editIndent.putExtra("pdf_page_no", refPageDetailsModel.getPdf_page_no());
                editIndent.putExtra("page_no", refPageDetailsModel.getPage_no());
                startActivity(editIndent);
            }
        });

        llPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new IosDialog.Builder(ReferencePageDetailsActivity.this)
                        .setMessage("Are you sure you want to add this reference to Pending?")
                        .setMessageColor(Color.parseColor("#1565C0"))
                        .setMessageSize(18)
                        .setNegativeButtonColor(Color.parseColor("#981010"))
                        .setNegativeButtonSize(18)
                        .setNegativeButton("Cancel", new IosDialog.OnClickListener() {
                            @Override
                            public void onClick(IosDialog dialog, View v) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButtonColor(Color.parseColor("#981010"))
                        .setPositiveButtonSize(18)
                        .setPositiveButton("Set as Pending", new IosDialog.OnClickListener() {
                            @Override
                            public void onClick(IosDialog dialog, View v) {
                                dialog.dismiss();

                                callUpdateReferenceStatus(0);
                            }
                        }).build();

                dialog.show();
            }
        });

        llApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new IosDialog.Builder(ReferencePageDetailsActivity.this)
                        .setMessage("Are you sure this reference is checked properly?")
                        .setMessageColor(Color.parseColor("#1565C0"))
                        .setMessageSize(18)
                        .setNegativeButtonColor(Color.parseColor("#981010"))
                        .setNegativeButtonSize(18)
                        .setNegativeButton("Cancel", new IosDialog.OnClickListener() {
                            @Override
                            public void onClick(IosDialog dialog, View v) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButtonColor(Color.parseColor("#981010"))
                        .setPositiveButtonSize(18)
                        .setPositiveButton("Approved", new IosDialog.OnClickListener() {
                            @Override
                            public void onClick(IosDialog dialog, View v) {
                                dialog.dismiss();

                                callUpdateReferenceStatus(1);
                            }
                        }).build();

                dialog.show();
            }
        });

        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new IosDialog.Builder(ReferencePageDetailsActivity.this)
                        .setMessage("Are you sure you want to delete this reference permanently?")
                        .setMessageColor(Color.parseColor("#1565C0"))
                        .setMessageSize(18)
                        .setNegativeButtonColor(Color.parseColor("#981010"))
                        .setNegativeButtonSize(18)
                        .setNegativeButton("Cancel", new IosDialog.OnClickListener() {
                            @Override
                            public void onClick(IosDialog dialog, View v) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButtonColor(Color.parseColor("#981010"))
                        .setPositiveButtonSize(18)
                        .setPositiveButton("Delete", new IosDialog.OnClickListener() {
                            @Override
                            public void onClick(IosDialog dialog, View v) {
                                dialog.dismiss();

                                callDeleteReference();
                            }
                        }).build();

                dialog.show();
            }
        });

        llMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mark = true;

                tvX1.setText("");
                tvY1.setText("");
                tvX2.setText("");
                tvY2.setText("");

                llSource.setVisibility(View.VISIBLE);

                llBottomOptions.setVisibility(View.GONE);
                llMarkOptions.setVisibility(View.VISIBLE);
            }
        });

        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDrawing();

                mark = false;

                llSource.setVisibility(View.GONE);

                llBottomOptions.setVisibility(View.VISIBLE);
                llMarkOptions.setVisibility(View.GONE);
            }
        });

        llUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDrawing();
            }
        });

        llSetMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdateReferenceData();

                mark = false;

                llSource.setVisibility(View.GONE);

                llBottomOptions.setVisibility(View.VISIBLE);
                llMarkOptions.setVisibility(View.GONE);

                clearDrawing();

//                Dialog dialog = new IosDialog.Builder(ReferencePageDetailsActivity.this)
//                        .setMessage("Are you sure you want to update reference Data?")
//                        .setMessageColor(Color.parseColor("#1565C0"))
//                        .setMessageSize(18)
//                        .setNegativeButtonColor(Color.parseColor("#981010"))
//                        .setNegativeButtonSize(18)
//                        .setNegativeButton("No", new IosDialog.OnClickListener() {
//                            @Override
//                            public void onClick(IosDialog dialog, View v) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setPositiveButtonColor(Color.parseColor("#981010"))
//                        .setPositiveButtonSize(18)
//                        .setPositiveButton("Yes", new IosDialog.OnClickListener() {
//                            @Override
//                            public void onClick(IosDialog dialog, View v) {
//                                dialog.dismiss();
//
//                                callUpdateReferenceData();
//
//                                mark = false;
//
//                                llSource.setVisibility(View.GONE);
//
//                                llBottomOptions.setVisibility(View.VISIBLE);
//                                llMarkOptions.setVisibility(View.GONE);
//
//                                clearDrawing();
//                            }
//                        }).build();
//
//                dialog.show();
            }
        });
    }

    private void setHeader() {
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPrefManager.getInstance(ReferencePageDetailsActivity.this).setBooleanPreference("ReloadData", true);
                finish();
                onBackPressed();
            }
        });
    }

    private void reloadReferencePageDetails()
    {
        loadReferencePageDetails(type_id, reference_id);
    }

    private void loadReferencePageDetails(int type_id, int reference_id)
    {
        this.type_id = type_id;
        this.reference_id = reference_id;

        callReferencePageDetailsApi();
        /*finish();

        Intent reference_intent = new Intent(ReferencePageDetailsActivity.this, ReferencePageDetailsActivity.class);
        reference_intent.putExtra("type_id", type_id);
        reference_intent.putExtra("reference_id", reference_id);
        reference_intent.putExtra("reference", reference);
        reference_intent.putExtra("reference_type", reference_type);
        startActivity(reference_intent);*/
    }

    private void callReferencePageDetailsApi() {
        if (!ConnectionManager.checkInternetConnection(ReferencePageDetailsActivity.this)) {
            Utils.showInfoDialog(ReferencePageDetailsActivity.this, "Please check internet connection");
            return;
        }

        Log.e("RefPageDetails", "callReferencePageDetailsApi");

        Utils.showProgressDialog(ReferencePageDetailsActivity.this, "Please Wait...", false);
        ApiClient.getBookReferencePageDetails("" + type_id, "" + reference_id, "" + reference, "" + reference_type, new Callback<ReferencePageDetailsResModel>() {
            @Override
            public void onResponse(Call<ReferencePageDetailsResModel> call, retrofit2.Response<ReferencePageDetailsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        refPageDetailsModel = response.body().getData();

                        prevReference = refPageDetailsModel.getPrev_reference();
                        nextReference = refPageDetailsModel.getNext_reference();

                        if (prevReference == null)
                        {
                            cvPrev.setVisibility(View.GONE);
                        }
                        else
                        {
                            cvPrev.setVisibility(View.VISIBLE);
                        }

                        if (nextReference == null)
                        {
                            cvNext.setVisibility(View.GONE);
                        }
                        else
                        {
                            cvNext.setVisibility(View.VISIBLE);
                        }

                        String type_value = refPageDetailsModel.getType_value();
                        String type_name = refPageDetailsModel.getType_name();

                        if (!type_name.equals(""))
                            type_value += " (" + type_name + ")";

                        String page_details = "PDF Page > " + refPageDetailsModel.getPdf_page_no() + "-" + (refPageDetailsModel.getPdf_page_no() - refPageDetailsModel.getPage_no()) + "=" + refPageDetailsModel.getPage_no() + " < Book Page";

                        tvTypeName.setText(type_value);
                        tvPageDetails.setText(page_details);

                        if (refPageDetailsModel.getIs_checked() == 0)
                        {
                            llPending.setVisibility(View.GONE);
                            llApprove.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            llPending.setVisibility(View.VISIBLE);
                            llApprove.setVisibility(View.GONE);
                        }

                        String page_bytes = refPageDetailsModel.getPage_bytes();

                        if (page_bytes != null && page_bytes.length() > 0) {
                            byte[] imgBytes = Base64.decode(page_bytes, Base64.DEFAULT);
                            tempBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);

                            Bitmap.Config config;
                            if(tempBitmap.getConfig() != null){
                                config = tempBitmap.getConfig();
                            }else{
                                config = Bitmap.Config.ARGB_8888;
                            }

                            bitmapMaster = Bitmap.createBitmap(
                                    tempBitmap.getWidth(),
                                    tempBitmap.getHeight(),
                                    config);

                            canvasMaster = new Canvas(bitmapMaster);
                            canvasMaster.drawBitmap(tempBitmap, 0, 0, null);

                            ivPage.setImageBitmap(bitmapMaster);

                            bitmapDrawingPane = Bitmap.createBitmap(
                                    tempBitmap.getWidth(),
                                    tempBitmap.getHeight(),
                                    config);
                            canvasDrawingPane = new Canvas(bitmapDrawingPane);
                            ivDrawingPane.setImageBitmap(bitmapDrawingPane);
                        }
                    } else {
                        Utils.showInfoDialog(ReferencePageDetailsActivity.this, "Something went Wrong!!");
                    }
                }
            }
            @Override
            public void onFailure(Call<ReferencePageDetailsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void callUpdateReferenceData() {
        if (!ConnectionManager.checkInternetConnection(ReferencePageDetailsActivity.this)) {
            Utils.showInfoDialog(ReferencePageDetailsActivity.this, "Please check internet connection");
            return;
        }

        String x1 = tvX1.getText().toString();
        String y1 = tvY1.getText().toString();
        String x2 = tvX2.getText().toString();
        String y2 = tvY2.getText().toString();

        Utils.showProgressDialog(ReferencePageDetailsActivity.this, "Please Wait...", false);
        ApiClient.updateReferenceData("" + type_id, "" + reference_id, x1, y1, x2, y2, new Callback<ReferenceResModel>() {
            @Override
            public void onResponse(Call<ReferenceResModel> call, retrofit2.Response<ReferenceResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        reloadReferencePageDetails();
//                        Dialog dialog = new IosDialog.Builder(ReferencePageDetailsActivity.this)
//                                .setMessage(response.body().getMessage())
//                                .setMessageColor(Color.parseColor("#1565C0"))
//                                .setMessageSize(18)
//                                .setPositiveButtonColor(Color.parseColor("#981010"))
//                                .setPositiveButtonSize(18)
//                                .setPositiveButton("OK", new IosDialog.OnClickListener() {
//                                    @Override
//                                    public void onClick(IosDialog dialog, View v) {
//                                        dialog.dismiss();
//                                        reloadReferencePageDetails();
//                                    }
//                                }).build();
//                        dialog.show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ReferenceResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void callUpdateReferenceStatus(int is_checked) {
        if (!ConnectionManager.checkInternetConnection(ReferencePageDetailsActivity.this)) {
            Utils.showInfoDialog(ReferencePageDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(ReferencePageDetailsActivity.this, "Please Wait...", false);
        ApiClient.updateReferenceStatus("" + type_id, "" + reference_id, is_checked, new Callback<ReferenceResModel>() {
            @Override
            public void onResponse(Call<ReferenceResModel> call, retrofit2.Response<ReferenceResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Dialog dialog = new IosDialog.Builder(ReferencePageDetailsActivity.this)
                                .setMessage(response.body().getMessage())
                                .setMessageColor(Color.parseColor("#1565C0"))
                                .setMessageSize(18)
                                .setPositiveButtonColor(Color.parseColor("#981010"))
                                .setPositiveButtonSize(18)
                                .setPositiveButton("OK", new IosDialog.OnClickListener() {
                                    @Override
                                    public void onClick(IosDialog dialog, View v) {
                                        dialog.dismiss();

                                        SharedPrefManager.getInstance(ReferencePageDetailsActivity.this).setBooleanPreference("ReloadBookData", true);
                                        reloadReferencePageDetails();
                                    }
                                }).build();
                        dialog.show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ReferenceResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void callDeleteReference() {
        if (!ConnectionManager.checkInternetConnection(ReferencePageDetailsActivity.this)) {
            Utils.showInfoDialog(ReferencePageDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(ReferencePageDetailsActivity.this, "Please Wait...", false);
        ApiClient.deleteReference("" + type_id, "" + reference_id, new Callback<ReferenceResModel>() {
            @Override
            public void onResponse(Call<ReferenceResModel> call, retrofit2.Response<ReferenceResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Dialog dialog = new IosDialog.Builder(ReferencePageDetailsActivity.this)
                                .setMessage(response.body().getMessage())
                                .setMessageColor(Color.parseColor("#1565C0"))
                                .setMessageSize(18)
                                .setPositiveButtonColor(Color.parseColor("#981010"))
                                .setPositiveButtonSize(18)
                                .setPositiveButton("OK", new IosDialog.OnClickListener() {
                                    @Override
                                    public void onClick(IosDialog dialog, View v) {
                                        dialog.dismiss();

                                        SharedPrefManager.getInstance(ReferencePageDetailsActivity.this).setBooleanPreference("ReloadBookData", true);

                                        if (nextReference != null)
                                        {
                                            loadReferencePageDetails(nextReference.getType_id(), nextReference.getId());
                                        }
                                        else if (prevReference != null)
                                        {
                                            loadReferencePageDetails(prevReference.getType_id(), prevReference.getId());
                                        }
                                        else
                                        {
                                            finish();
                                            onBackPressed();
                                        }
                                    }
                                }).build();
                        dialog.show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ReferenceResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    class projectPt{
        int x;
        int y;

        projectPt(int tx, int ty){
            x = tx;
            y = ty;
        }
    }

    private projectPt projectXY(ImageView iv, Bitmap bm, int x, int y){
        if(x<0 || y<0 || x > iv.getWidth() || y > iv.getHeight()){
            //outside ImageView
            return null;
        }else{
            int projectedX = (int)((double)x * ((double)bm.getWidth()/(double)iv.getWidth()));
            int projectedY = (int)((double)y * ((double)bm.getHeight()/(double)iv.getHeight()));

            return new projectPt(projectedX, projectedY);
        }
    }

    private void drawOnRectProjectedBitMap(ImageView iv, Bitmap bm, int x, int y){
        if(x<0 || y<0 || x > iv.getWidth() || y > iv.getHeight()){
            //outside ImageView
            return;
        }else{
            int projectedX = (int)((double)x * ((double)bm.getWidth()/(double)iv.getWidth()));
            int projectedY = (int)((double)y * ((double)bm.getHeight()/(double)iv.getHeight()));

            //clear canvasDrawingPane
            canvasDrawingPane.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(3);
            canvasDrawingPane.drawRect(startPt.x, startPt.y, projectedX, projectedY, paint);
            ivDrawingPane.invalidate();

            tvX1.setText("" + startPt.x);
            tvY1.setText("" + startPt.y);
            tvX2.setText("" + projectedX);
            tvY2.setText("" + projectedY);
        }
    }

    private void clearDrawing(){
        canvasDrawingPane.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(3);

        canvasDrawingPane.drawRect(0, 0, bitmapDrawingPane.getWidth(), bitmapDrawingPane.getHeight(), paint);
        ivDrawingPane.invalidate();

        tvX1.setText("");
        tvY1.setText("");
        tvX2.setText("");
        tvY2.setText("");
    }

    private void finalizeDrawing(){
//        canvasMaster.drawBitmap(bitmapDrawingPane, 0, 0, null);
    }
}
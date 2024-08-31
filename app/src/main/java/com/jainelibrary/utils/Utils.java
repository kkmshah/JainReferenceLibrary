package com.jainelibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.activity.HoldAndSearchActivity;
import com.jainelibrary.activity.HomeActivity;
import com.jainelibrary.activity.IndexSearchDetailsActivity;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.MainActivity;
import com.jainelibrary.activity.NotesActivity;
import com.jainelibrary.activity.YearListActivity;
import com.jainelibrary.activity.ZoomImageActivity;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.FilterResModel;
import com.jainelibrary.model.FilterResModelLIst;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ClearHoldReferenceModel;
import com.jainelibrary.utils.DownloadImage.DownloadImage;
import com.wc.widget.dialog.IosDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.DOWNLOAD_SERVICE;

public class Utils {
//    public static final File mediaStorageCommonPDFDir = new File(Environment.getExternalStorageDirectory(), "/JRL" + "/Common PDF");
//    public static final File mediaStorageCommonDir = new File(Environment.getExternalStorageDirectory(), "/JRL" + "/Common Images");
//    public static final File mediaStoragePDfDir = new File(Environment.getExternalStorageDirectory(), "/JRL" + "/PDF File");
//    public static final File mediaStorageDownloadDir = new File(Environment.getExternalStorageDirectory(), "/Download");
//    public static final File mediaStorageKeyWordDir = new File(Environment.getExternalStorageDirectory(), "/JRL" + "/TXT File" + "/Keyword File");
//    public static final File mediaStorageKeyWordRefDir = new File(Environment.getExternalStorageDirectory(), "/JRL" + "/TXT File" + "/Keyword Reference File");
//    public static final File mediaStoragePDfBookKeyWordDir = new File(Environment.getExternalStorageDirectory(), "/JRL" + "/PDF File" + "/BookLIst File");
//    public static final File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/JRL" + "/Zip File");
    public static final File mediaStoreDownloadsDir = new File(Environment.getExternalStorageDirectory(), "/JRL" + "/Downloads");
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    public static String TYPE_KEYWORD_PAGE = "0";
    public static String TYPE_YEAR_PAGE = "3";
    public static String TYPE_SHLOK_PAGE = "1";
    public static String TYPE_INDEX_PAGE = "2";
    public static String TYPE_PDF_BOOK_PAGE = "4";

    public static String TYPE_BIODATA_BOOK_REFERENCE = "5";

    public static String TYPE_RELATION_BOOK_REFERENCE = "6";


    public static String TYPE_BIODATA = "5";

    public static String TYPE_RELATION = "6";

    public static String TYPE_UNIT = "7";

    public static String TYPE_UNIT_RELATION_TREE = "8";





    public static String REF_TYPE_REFERENCE_PAGE = "1";
    public static String REF_TYPE_BOOK_PAGE = "2";
    public static String REF_TYPE_KEYWORD_PAGE = "3";
    public static String REF_TYPE_KEYWORD_REFER_PAGE = "4";
    public static String Is_Common_Login = "0";
    public static String Is_Reference_Login_Id = "1";
    public static String Is_Keyword_Search_Login_Id = "2";
    public static String Is_Keyword_Search_Details_Login_Id = "3";
    public static String Is_Book_Search_Login_Id = "4";
    public static String Is_My_Shelf_Login_Id = "5";
    public static String Is_Hold_Login_Id = "6";
    public static String Is_Appendix_Login_Id = "7";
    public static String Is_Pdf_Store_Login_Id = "8";
    public static String Is_Notes_Login_Id = "9";
    public static String Is_Higlight_Id = "10";
    private static ProgressDialog pDialog;

    public static boolean checkAndRequestPermissions(Context context) {
        int permissionSendMessage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionReadExternal = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionReadExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_FOR_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    public static File getMediaStorageDir(Context context)
    {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "JRL");
        if (!file.exists())
        {
            file.mkdirs();
            Log.e("MediaStorage", file.getAbsolutePath());
        }

        return file;
    }

    public static Bitmap getBitmap(Uri uri, int v, Activity mContext) {
        InputStream in = null;
        try {
            //    final int IMAGE_MAX_SIZE = 1200000 / v; // 1.2MP
            final int IMAGE_MAX_SIZE = 1200000 / v; // 1.2MP
            in = mContext.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            Bitmap b = null;
            in = mContext.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            return b;
        } catch (IOException e) {
            return null;
        }
    }

    public static String getPictureFile(Context mContext) throws IOException {
        String pictureFilePath = null;
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "FaceSpotter" + timeStamp;
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return pictureFilePath;
    }

    public static void showProgressDialog(Activity activity, String Message, boolean isCancelable) {
        if ((pDialog == null) || (!pDialog.isShowing()) && JRL.isKeywordLoading == false) {
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(Message);
            pDialog.setCancelable(isCancelable);
            pDialog.show();
        }
    }

    public static void dismissProgressDialog() {
        if ((pDialog != null) && (pDialog.isShowing()) && JRL.isKeywordLoading == false)
            pDialog.dismiss();
    }

    public static void transferFragment(Fragment fragment, Activity activity) {
        Log.e("Dashboard---", " tranbsfer");

        FragmentManager supportFragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if (supportFragmentManager.getBackStackEntryCount() > 0) {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment).addToBackStack("tag");
        }
        fragmentTransaction.commit();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static Bitmap convertUrlToBitmap(String Url) {
        Bitmap convertedImage = null;
        try {
            URL url = new URL(Url);
            convertedImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        return convertedImage;
    }

    public static Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent", "Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }

    public static Drawable drawableFromUrl(String url) throws IOException {

        Bitmap x = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-agent", "Mozilla/4.0");
            connection.connect();
            InputStream input = connection.getInputStream();
            x = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e("Drawable Exception--", "" + e.getMessage());
        }
        return new BitmapDrawable(Resources.getSystem(), x);
    }

    public static int getOrienatation(String photoPath) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap flipImage(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        ;
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "JRL_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }


    public static boolean makeFolder(String pathName) {
        File directoryName = new File(pathName);

        if (!directoryName.exists()) {
            return directoryName.mkdir();
        }

        return false;
    }


    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    public static FilterResModel getFilterData() {

        List<FilterResModelLIst> mList = new ArrayList<>();
        mList.add(new FilterResModelLIst("0", "All"));
        mList.add(new FilterResModelLIst("1", "Sub Cat 1"));
        mList.add(new FilterResModelLIst("2", "Sub Category 2"));
        mList.add(new FilterResModelLIst("3", "Sub Cat 3"));
        mList.add(new FilterResModelLIst("4", "Sub Cat 4"));
        mList.add(new FilterResModelLIst("5", "Sub Category 5"));
        mList.add(new FilterResModelLIst("6", "Sub Cat 6"));
        return new FilterResModel(mList);

    }


    public static File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "JRL");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + "Download_Page_" + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    //create bitmap from view and returns it
    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    // used for scanning gallery
    public static void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("TAG", "scanGallery the image." + path);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
        // String allIds = TextUtils.join(",", ids);
    }

    public static Bitmap getHiglightImage(int x, int y, int right, int bottom, Bitmap image) {
        Bitmap bmp = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cnvs = new Canvas(bmp);
        Paint paint = new Paint();

        Rect areaRect = new Rect(x, y, right, bottom);
        paint.setARGB(150, 239, 83, 80);
        paint.setStrokeWidth(10);
        cnvs.drawBitmap(image, 0, 0, null);
        cnvs.drawRect(areaRect, paint);
        return bmp;
    }

    public static void logout(Context mContext, boolean isLogin, boolean isHome) {

        if (isLogin) {
            getHoldList(mContext);
        } else {
            if (isHome) {
                Intent intent = new Intent(mContext, LoginWithPasswordActivity.class);
                intent.putExtra("isHome", true);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, LoginWithPasswordActivity.class);
                mContext.startActivity(intent);
            }

        }
    }

    public static void showLoginDialog(Activity activity) {
        Dialog dialog = new IosDialog.Builder(activity)
                .setMessage("Please login to use this functionality.")
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
                .setPositiveButton("Login", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();

                        Intent intent = new Intent(activity, LoginWithPasswordActivity.class);
                        activity.startActivity(intent);
                    }
                }).build();
        dialog.show();
    }

    public static void showLoginDialogForResult(Activity activity, int requestCode) {
        Dialog dialog = new IosDialog.Builder(activity)
                .setMessage("Please login to use this functionality.")
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
                .setPositiveButton("Login", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();

                        Intent intent = new Intent(activity, LoginWithPasswordActivity.class);
                        intent.putExtra("isLoginId", Utils.Is_Keyword_Search_Details_Login_Id);
                        activity.startActivityForResult(intent, requestCode);
                    }
                }).build();
        dialog.show();
    }

    private static void showLogoutDialog(Context mContext,int holdCount,String strUserId) {
        if (holdCount > 0) {
            Dialog dialog = new IosDialog.Builder(mContext)
                .setMessage("Are you sure you want to logout, Your hold page have " + holdCount + " References, After Logout hold page will clear.")
                .setMessageColor(Color.parseColor("#1565C0"))
                .setMessageSize(18)
                .setNegativeButtonColor(Color.parseColor("#981010"))
                .setNegativeButtonSize(18)
                .setNegativeButton("Hold", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        Intent holdIntent = new Intent(mContext, HoldAndSearchActivity.class);
                        mContext.startActivity(holdIntent);
                    }
                })
                .setPositiveButtonColor(Color.parseColor("#981010"))
                .setPositiveButtonSize(18)
                .setPositiveButton("Logout", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        callClearHoldReferenceApi(mContext, strUserId);
                    }
                }).build();

            dialog.show();
        }
        else {
            Dialog dialog = new IosDialog.Builder(mContext)
                .setMessage("Are you sure you want to logout?")
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
                .setPositiveButton("Logout", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                        callClearHoldReferenceApi(mContext,strUserId);
                    }
                }).build();

            dialog.show();
        }

        /*AlertDialog.Builder a_builder = new AlertDialog.Builder(mContext);
        String strMessage;

        if (holdCount > 0) {
            a_builder.setNegativeButton("Hold", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    Intent holdIntent = new Intent(mContext, HoldAndSearchActivity.class);
                    mContext.startActivity(holdIntent);
                }
            });
            strMessage = "Are you sure you want to logout, Your hold page have " + holdCount + " References, After Logout hold page will clear.";
            a_builder.setMessage(Html.fromHtml("<font color='#1565C0'>" + strMessage + "</font>"));

        } else {
            strMessage = "Are you sure you want to logout?";
            a_builder.setMessage(Html.fromHtml("<font color='#1565C0'>" + strMessage + "</font>"));
        }

        a_builder.setCancelable(false);
        a_builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                callClearHoldReferenceApi(mContext,strUserId);

            }
        });
        a_builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });

        AlertDialog alert = a_builder.create();
        alert.setTitle("");
        alert.show();*/
    }

    public static void callClearHoldReferenceApi (Context mContext,String strUserId) {
        if (!ConnectionManager.checkInternetConnection(mContext)) {
            Utils.showInfoDialog((Activity) mContext, "Please check internet connection");
            return;
        }
        Utils.showProgressDialog((Activity) mContext, "Please Wait...", false);
        ApiClient.clearHoldReferenceApi(strUserId, new Callback<ClearHoldReferenceModel>() {
            @Override
            public void onResponse(Call<ClearHoldReferenceModel> call, retrofit2.Response<ClearHoldReferenceModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.showInfoDialog((Activity) mContext, "Logout Successfully");
                        SharedPrefManager.getInstance(mContext).logout();
                        Intent is = new Intent(mContext, MainActivity.class);
                        is.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(is);
                        //Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Utils.showInfoDialog((Activity) mContext, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ClearHoldReferenceModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    public static void getHoldList(Context context) {
        String strUserId = SharedPrefManager.getInstance(context).getStringPref(SharedPrefManager.KEY_USER_ID);

        if (!ConnectionManager.checkInternetConnection(context)) {
            Utils.showInfoDialog((Activity) context, "Please check your internet connection");
        }

        ApiClient.getHolderSearchList(strUserId, new Callback<BookListResModel>() {
            @Override
            public void onResponse(Call<BookListResModel> call, retrofit2.Response<BookListResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<BookListResModel.BookDetailsModel> mDataList = new ArrayList<>();
                        mDataList = response.body().getData();
                        if (mDataList != null && mDataList.size() > 0) {
                            showLogoutDialog(context,mDataList.size(),strUserId);
                        } else {
                            showLogoutDialog(context,0,strUserId);
                        }
                    } else {
                        showLogoutDialog(context,0,strUserId);
                    }
                }
            }

            @Override
            public void onFailure(Call<BookListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
                showLogoutDialog(context,0,strUserId);
            }
        });
    }


    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }


    public static void downloadImageFromURL(Activity activity, String imageUrl) {

         DownloadImage.downloadImageFromURL(activity, imageUrl);
//        if (!Utils.mediaStoreDownloadsDir.exists())
//            Utils.mediaStoreDownloadsDir.mkdirs();
//
//        Utils.showProgressDialog(activity, "Please Wait...", false);
//        Glide.with(activity).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                Utils.dismissProgressDialog();
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.DISPLAY_NAME, "JRL_"+System.currentTimeMillis()+".jpeg");
//                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//                ContentResolver contentResolver = activity.getContentResolver();
//                Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                // Open an output stream to write the image
//                try (OutputStream out = contentResolver.openOutputStream(imageUri)) {
//                    resource.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    //Utils.showInfoDialog(activity, "Image Saved successfully.");
//                    Toast.makeText(activity.getApplicationContext(), "Image Saved!.", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onLoadCleared(Drawable placeholder) {
//                Utils.dismissProgressDialog();
//            }
//        });

    }


    public static void shareContentWithImage(Activity activity, String title, String message, String imageUrl) {

        String strMessage = message + "\n Get Latest JRL APP from here : https://play.google.com/store/apps/details?id=" + activity.getPackageName();

        Utils.showProgressDialog(activity, "Please Wait...", false);
        Glide.with(activity).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                Utils.dismissProgressDialog();

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                shareIntent.putExtra(Intent.EXTRA_TEXT, strMessage);
                shareIntent.putExtra(Intent.EXTRA_TITLE, title);

                ContentResolver contentResolver = activity.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, "jrl_share_"+System.currentTimeMillis()+".jpeg");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                try (OutputStream out = contentResolver.openOutputStream(imageUri)) {
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    String mimeType = activity.getContentResolver().getType(imageUri);

                    if (mimeType == null) {
                        mimeType = "image/jpeg"; // Fallback if MIME type is not found
                    }
                    shareIntent.setType(mimeType);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent chooser = Intent.createChooser(shareIntent, title);
                new Handler().postDelayed(() -> activity.getContentResolver().delete(imageUri, null, null), 60000);
                activity.startActivity(chooser);
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
                // Handle if the image load is canceled
                Utils.dismissProgressDialog();
            }
        });
    }

    public static void downloadPdf(String filename, String url, Activity activity) {
        String imagePath = "JRL_" + filename + System.currentTimeMillis();
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        DownloadManager downloadManager = (DownloadManager) activity.getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle(filename);
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading File");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, imagePath + ".pdf");
//        Toast.makeText(context, "File Downloaded successfully.", Toast.LENGTH_LONG).show();
        //Enqueue a new download and same the referenceId
        long downloadReference = downloadManager.enqueue(request);

        Utils.showInfoDialog(activity, "File Downloaded successfully.");
    }

    public static void downloadLocalPDF(String path, Activity activity)
    {
        File file = new File(path);
        DownloadManager manager = (DownloadManager) activity.getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
        manager.addCompletedDownload(file.getName(), file.getName(), true, "application/pdf", file.getAbsolutePath(), file.length(),true);

        Utils.showInfoDialog(activity, "File Downloaded successfully.");
    }

    public static void showDefaultKeyboardDialog(Context context) {
        final Dialog dialogView = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
        dialogView.setContentView(R.layout.dialog_default_language);
        dialogView.setCancelable(false);
        dialogView.show();

        Button btnSave = dialogView.findViewById(R.id.btnSave);
        RadioButton rbDevnagari = dialogView.findViewById(R.id.rbDevnagari);
        RadioButton rbEnglish = dialogView.findViewById(R.id.rbEnglish);
        RadioButton rbIndic = dialogView.findViewById(R.id.rbIndic);

        String strDefaultKeyboard = SharedPrefManager.getInstance(context).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        if(strDefaultKeyboard != null && strDefaultKeyboard.length() > 0){
            if(strDefaultKeyboard.equals("Devnagari")){
                rbDevnagari.setChecked(true);
            }else if(strDefaultKeyboard.equals("English")){
                rbEnglish.setChecked(true);
            }else {
                rbIndic.setChecked(true);
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newLanguage = null;
                if (rbDevnagari.isChecked()) {
                    newLanguage = "Devnagari";
                } else if (rbEnglish.isChecked()) {
                    newLanguage = "English";
                } else if (rbIndic.isChecked()) {
                    newLanguage = "Indic";
                } else {
                    Utils.showInfoDialog((Activity) context, "Please select language");
                }

                Log.e("newLanguage--", newLanguage);

                if (newLanguage != null && newLanguage.length() > 0) {
                    SharedPrefManager.getInstance(context).saveStrinKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE, newLanguage);
                    dialogView.dismiss();
                }


            }
        });

    }

    public static void showInfoDialog(Activity activity, String strMessage) {
        Dialog dialog = new IosDialog.Builder(activity)
                .setMessage(strMessage)
                .setMessageColor(Color.parseColor("#1565C0"))
                .setMessageSize(18)
                .setPositiveButtonColor(Color.parseColor("#981010"))
                .setPositiveButtonSize(18)
                .setPositiveButton("OK", new IosDialog.OnClickListener() {
                    @Override
                    public void onClick(IosDialog dialog, View v) {
                        dialog.dismiss();
                    }
                }).build();
        dialog.show();
    }

    public static int dpFromPx(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static String getExportUnitDetailsUrl(String unitId) {
        return  ApiClient.BASE_URL+"data/export_unit_details?id="+unitId;
    }

    public static String getExportBiodataDetailsUrl(String bmId) {
        return  ApiClient.BASE_URL+"data/export_bm_details?id="+bmId;
    }
    public static String getExportUnitRelationDetailsUrl(String relationId) {
        return  ApiClient.BASE_URL+"data/export_unit_relation_details?id="+relationId;
    }

    public static String getUnitIcon(String unitType) {
        return "https://jaintatvagyanshala.org/assets/api/images/unit_"+unitType+".png";
    }
}

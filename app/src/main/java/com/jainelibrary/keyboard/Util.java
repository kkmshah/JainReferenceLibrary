/*
 * Copyright (c) 2016 - Firoz Memon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jainelibrary.keyboard;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jainelibrary.R;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * Created by Dhaval Hirpara
 */

public class Util {
    public static Activity mContext;
    static BottomSheetDialog mDialogs;
    static String[] language = {"English", "Gujarati", "Hindi"};
    public static String lang_code = "0";
    static CustomKeyboardView mCustomKeyboardView;
    static Keyboard mCustomKeyboard;
    static EditText mCustomEditText;
    static ImageView mimageView;

    private Util() {
    }


    public static boolean isLangSupported(Context context, String text) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        int w = 200, h = 80;
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
        Bitmap orig = bitmap.copy(conf, false);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextSize((int) (14 * scale));

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;
        canvas.drawText(text, x, y, paint);
        boolean res = false;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            res = !(orig == bitmap);
        } else {
            res = !orig.sameAs(bitmap);
        }
        orig.recycle();
        bitmap.recycle();
        return res;
    }

    public static void displayAlert(Activity activity, String titleText, String messageText) {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(titleText)
                .setMessage(messageText)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    public static void selectKeyboard(final CustomKeyboardView mKeyboardView, Keyboard mKeyboard, final EditText mEditText, final Activity activity, String strLanguage, BottomSheetDialog mDialog, ImageView imageView) {
        mCustomKeyboardView = mKeyboardView;
        mCustomKeyboard = mKeyboard;
        mCustomEditText = mEditText;
        mContext = activity;
        mDialogs = mDialog;
        mimageView = imageView;
        // Do not show the preview balloons
        mKeyboardView.setPreviewEnabled(true);
        Log.e("language---", "" + strLanguage);
        if (strLanguage != null && strLanguage.length() != 0) {
//            hideKeyBoard(activity, mEditText);
            commonKeyboardHide(activity);
            if (strLanguage != null && strLanguage.equalsIgnoreCase("Devnagari")) {
                mKeyboard = new Keyboard(activity, R.xml.kbd_hin1);
                showKeyboardWithAnimation(mKeyboardView, activity);
                mKeyboardView.setVisibility(View.VISIBLE);
                mKeyboardView.setKeyboard(mKeyboard);
                lang_code = "0";
            } else if (strLanguage != null && strLanguage.equalsIgnoreCase("Gujarati")) {
                if (Util.isLangSupported(activity, "ગુજરાતી")) {
                    mKeyboard = new Keyboard(activity, R.xml.kbd_guj1);
                    showKeyboardWithAnimation(mKeyboardView, activity);
                    mKeyboardView.setVisibility(View.VISIBLE);
                    mKeyboardView.setKeyboard(mKeyboard);
                    lang_code = "2";
                } else {
                    Util.displayAlert(activity, activity.getResources().getString(R.string.app_name), "Gujarati keyboard is not supported "
                            + "by your device");
                    //Reset language selection
                    strLanguage = "English";
                    mKeyboard = new Keyboard(activity, R.xml
                            .kbd_hin1);
                    mKeyboardView.setVisibility(View.GONE);
                    showDefaultKeyboard(activity, mEditText);
                }
            } else if (strLanguage != null && strLanguage.equalsIgnoreCase("English")) {
                mKeyboard = new Keyboard(activity, R.xml.kbd_eng1);
                showKeyboardWithAnimation(mKeyboardView, activity);
                mKeyboardView.setVisibility(View.VISIBLE);
                mKeyboardView.setKeyboard(mKeyboard);
                lang_code = "1";
            }else {
                showDefaultKeyboard(activity, mEditText);
            }
        } /*
             else if (strLanguage != null && strLanguage.equalsIgnoreCase("Hindi")) {
                mKeyboard = new Keyboard(activity, R.xml.kbd_mar1);
                showKeyboardWithAnimation(mKeyboardView, activity);
                mKeyboardView.setVisibility(View.VISIBLE);
                mKeyboardView.setKeyboard(mKeyboard);
            }
            else if (strLanguage != null && strLanguage.equalsIgnoreCase("Gujarati")) {
                mKeyboard =
                        new Keyboard(activity, R.xml.kbd_knd1);
                showKeyboardWithAnimation(mKeyboardView, activity);
                mKeyboardView.setVisibility(View.VISIBLE);
                mKeyboardView.setKeyboard(mKeyboard);
            } else if (switchLang.getSelectedItemPosition() == 5) {
                mKeyboard = new Keyboard(activity, R.xml
                        .kbd_tam1);
                showKeyboardWithAnimation(mKeyboardView, activity);
                mKeyboardView.setVisibility(View.VISIBLE);
                mKeyboardView.setKeyboard(mKeyboard);
            } else if (switchLang.getSelectedItemPosition() == 6) {
                if (Util.isLangSupported(activity, "ਪੰਜਾਬੀ ਦੇ")) {

                    mKeyboard = new Keyboard(activity, R.xml
                            .kbd_punj1);
                    showKeyboardWithAnimation(mKeyboardView, activity);
                    mKeyboardView.setVisibility(View.VISIBLE);
                    mKeyboardView.setKeyboard(mKeyboard);
                } else {
                    Util.displayAlert(activity, activity.getResources().getString(R.string.app_name), "Punjabi keyboard is not supported "
                            + "by your device");

                    //Reset language selection
                    switchLang.setSelection(0);
                    mKeyboard = new Keyboard(activity, R.xml
                            .kbd_hin1);
                    mKeyboardView.setVisibility(View.GONE);

                    //Show Default Keyboard
                    InputMethodManager imm =
                            (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEditText, 0);
                    mEditText.setOnTouchListener(null);
                }
            }*/

            /*if (strLanguage != null && strLanguage.equalsIgnoreCase("English")) {
                mKeyboardView.setVisibility(View.GONE);
                //Show Default Keyboard
                showDefaultKeyboard(activity, mEditText);
            }*/

        mKeyboardView.setOnKeyboardActionListener(new BasicOnKeyboardActionListener(activity,
                mEditText,
                mKeyboardView,
                mDialog,imageView));

        final Keyboard finalMKeyboard = mKeyboard;
        final String finalStrLanguage = strLanguage;
        /*mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectKeyboard(mKeyboardView, finalMKeyboard, mEditText, activity, finalStrLanguage, mDialog, imageView);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Layout layout = ((EditText) v).getLayout();
                        float x = event.getX() + mEditText.getScrollX();
                        float y = event.getY() + mEditText.getScrollY();
                        int line = layout.getLineForVertical((int) y);

                        int offset = layout.getOffsetForHorizontal(line, x);
                        if (offset > 0)
                            if (x > layout.getLineMax(0))
                                mEditText.setSelection(offset);     // touch was at end of text
                            else
                                mEditText.setSelection(offset - 1);

                        mEditText.setCursorVisible(true);
                        break;
                }
                return true;
            }

        });*/
      /*  mimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectKeyboard(mKeyboardView, finalMKeyboard, mEditText, activity, finalStrLanguage, mDialog, imageView);

             *//*   switch (event.getAction()) {
                    case MotionEvent.ACTION_BUTTON_PRESS:

                        break;*//*


            }
        });*/
     /*   mimageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectKeyboard(mKeyboardView, finalMKeyboard, mEditText, activity, finalStrLanguage, mDialog, imageView);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_BUTTON_PRESS:

                        break;
                }
                return true;
            }
        });*//* else {
            mKeyboardView.setVisibility(View.GONE);

            //Show Default Keyboard
            InputMethodManager imm =
                    (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEditText, 0);
            mEditText.setOnTouchListener(null);
            Log.e("language---", "" + strLanguage);
        }*/
    }


    private static void showKeyboardWithAnimation(CustomKeyboardView mKeyboardView, Activity activity) {
        if (mKeyboardView.getVisibility() == View.GONE) {
            Animation animation = AnimationUtils
                    .loadAnimation(activity,
                            R.anim.slide_from_bottom);
            mKeyboardView.showWithAnimation(animation);
        }
    }


    public static class BasicOnKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

        EditText editText;
        CustomKeyboardView displayKeyboardView;
        private Activity mTargetActivity;
        BottomSheetDialog mBDialogs;
        ImageView imageView;
        OnItemClickListener onItemClickListener;

        public interface OnItemClickListener {
           public void onKey(  int key);
        }


        public BasicOnKeyboardActionListener(Activity targetActivity, EditText editText,
                                             CustomKeyboardView displayKeyboardView, BottomSheetDialog mBDialog, ImageView imageView) {
            mTargetActivity = targetActivity;
            this.editText = editText;
            this.displayKeyboardView = displayKeyboardView;
            this.mBDialogs = mBDialog;
            this.imageView = imageView;

        }

        @Override
        public void swipeUp() {

            /* no-op */
        }

        @Override
        public void swipeRight() {
            /* no-op */
        }

        @Override
        public void swipeLeft() {
            /* no-op */
        }

        @Override
        public void swipeDown() {
            /* no-op */
        }

        @Override
        public void onText(CharSequence text) {
            int cursorPosition = editText.getSelectionEnd();
            String previousText = editText.getText().toString();
            String before, after;
            if (cursorPosition < previousText.length()) {
                before = previousText.substring(0, cursorPosition);
                after = previousText.substring(cursorPosition);
            } else {
                before = previousText;
                after = "";
            }
            editText.setText(before + text + after);
            editText.setSelection(cursorPosition + 1);
        }

        @Override
        public void onRelease(int primaryCode) {
            /* no-op */
        }

        @Override
        public void onPress(int primaryCode) {
            /* no-op */

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            switch (primaryCode) {
                case 111:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_eng1));
                    break;
                case 112:
                    //For A= A keyboard
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kdb_hin3));
                    keyCodes[-105] = 1;
                    break;
                case 113:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_hin1));
                    break;
                case 114:
                    if (appInstalledOrNot()) {
                      /*  Intent LaunchIntent = mTargetActivity.getPackageManager()
                                .getLaunchIntentForPackage("https://play.google.com/store/apps/details?id=com.google.android.apps.inputmethod.hindi&hl=en\n");
                        mTargetActivity.startActivity(LaunchIntent);*/
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.inputmethod.hindi&hl=en\n"));
                        try {
                            mTargetActivity.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showDefaultKeyboard(mTargetActivity, editText);
                        displayKeyboardView.setVisibility(View.GONE);
                    }
                    break;
                case 151:imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onKey(151);
                        Log.e("hello",""+151);
                    }
                });

                    long eventTimes = System.currentTimeMillis();
                    KeyEvent events = new KeyEvent(eventTimes, eventTimes, KeyEvent.ACTION_DOWN, primaryCode, 0, 0, 0, 0,
                            KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.KEYCODE_SEARCH);
                    //  KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DEL);
                    if (imageView != null) {
                        Log.e("Show Language", "bhugdhjd");
                        imageView.dispatchKeyEvent(events);
                    } else {
                        Log.e("Show Language", "null");
                        mTargetActivity.dispatchKeyEvent(events);
                    }
                    break;
                   /* long eventTimes = System.currentTimeMillis();
                    KeyEvent events = new KeyEvent(eventTimes, eventTimes, KeyEvent.KEYCODE_SEARCH, primaryCode, 0, 0, 0, 0,
                            KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE);
                    //  KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DEL);
                    // if (mBDialogs != null) {
                     //     mBDialogs.dispatchKeyEvent(events);
                 //   } else {
                        mTargetActivity.dispatchKeyEvent(events);
                        break;*/
                //   }
               /*     Log.e("Show Language", "bhugdhjd");
                    showLanguage()*/
                case 66:
                case 67:
                    long eventTime = System.currentTimeMillis();
                    KeyEvent event = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, primaryCode, 0, 0, 0, 0,
                            KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE);
                    //  KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DEL);
                    if (mBDialogs != null) {
                        mBDialogs.dispatchKeyEvent(event);
                    } else {
                        mTargetActivity.dispatchKeyEvent(event);
                    }
                    break;
                case -106:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_hin2));
                    break;
                case -107:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_hin1));
                    break;
                case -108:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_guj2));
                    break;
                case -109:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_guj1));
                    break;
                case -110:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_mar2));
                    break;
                case -111:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_mar1));
                    break;
                case -112:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_knd2));
                    break;
                case -113:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_knd1));
                    break;
                case -114:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_tam2));
                    break;
                case -115:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_tam1));
                    break;
                case -116:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_punj2));
                    break;
                case -117:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_punj1));
                    break;
                case -125:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_eng2));
                    break;
                case -126:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_eng1));
                    break;
                case -130:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_hin1));
                    break;
                case -140:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_guj1));
                    break;
                case -150:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_eng1));
                    break;
                default:
                    break;
            }
        }

        private boolean appInstalledOrNot() {
            PackageManager pm = mTargetActivity.getPackageManager();
            try {
                pm.getPackageInfo("https://play.google.com/store/apps/details?id=com.google.android.apps.inputmethod.hindi&hl=en\n", PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
            }
            return false;
        }
    }

    public static void hideKeyBoard(Activity activity, EditText mEditText) {
        try {
            if (mCustomKeyboardView != null) {
                mCustomKeyboardView.setVisibility(View.GONE);
            }

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        } catch (Exception e) {// TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void commonKeyboardHide(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void showDefaultKeyboard(Activity activity, EditText mEditText) {
        //Show Default Keyboard
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, 0);
        mEditText.setOnTouchListener(null);
    }


    public static void showLanguage() {
        mCustomKeyboardView.setVisibility(View.GONE);
        ArrayAdapter genderAdapter = new ArrayAdapter(mContext, R.layout.dropdown_textview, language);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Language");
        builder.setSingleChoiceItems(genderAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
                String strLanguage = language[position];
                selectKeyboard(mCustomKeyboardView, mCustomKeyboard, mCustomEditText, mContext, strLanguage, mDialogs, mimageView);
            }
        });
        builder.show();
    }


}

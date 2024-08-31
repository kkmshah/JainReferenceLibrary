package com.jainelibrary;

import android.app.Application;
import android.util.Log;

import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.StorageManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JRL extends Application {
    static JRL mApplication;
    ArrayList<BookListResModel.BookDetailsModel> mBookDetailsList;
    public static boolean isKeywordLoading = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mBookDetailsList = new ArrayList<>();
        mApplication = this;
        Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        StorageManager.init(getApplicationContext());
    }

    public static JRL getInstance() {
        return mApplication;
    }

    public ArrayList<BookListResModel.BookDetailsModel> getmBookDetailsList() {
        return mBookDetailsList;
    }

    public void setmBookDetailsList(ArrayList<BookListResModel.BookDetailsModel> mBookDetailsList) {
        Log.e("Jrlll---", "mBookDetailsList" + mBookDetailsList.size());
        if (mBookDetailsList != null && mBookDetailsList.size() > 0) {
            this.mBookDetailsList = mBookDetailsList;
        }
    }
}

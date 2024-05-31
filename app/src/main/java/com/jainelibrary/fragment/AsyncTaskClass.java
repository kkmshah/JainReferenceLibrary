package com.jainelibrary.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jainelibrary.adapter.GalleryAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

class AsyncTaskClass extends AsyncTask {
    Activity activity;
    String strSearch;
    String strCatId;
    String strType;
    int page_no;
    GalleryAdapter mGalleryListAdapter;

    private boolean isLoading = false;

    public AsyncTaskClass(Context context, String strSearch, String strCatId, String strType, int page_no, GalleryAdapter mGalleryListAdapter)
    {
        this.activity = (Activity) context;
        this.strSearch = strSearch;
        this.strCatId = strCatId;
        this.strType = strType;
        this.page_no = page_no;
        this.mGalleryListAdapter = mGalleryListAdapter;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        getPdfList();
        return null;
    }

    public void getPdfList() {
        if (!ConnectionManager.checkInternetConnection(activity)) {
            Utils.showInfoDialog(activity, "Please check internet connection");
            return;
        }
        String strUserId = SharedPrefManager.getInstance(activity).getStringPref(SharedPrefManager.KEY_USER_ID);

        isLoading = true;

//        Utils.showProgressDialog(activity, "Please Wait...", false);
        ApiClient.getPdfList(strUserId, strSearch, strCatId, strType, String.valueOf(page_no), new Callback<PdfStoreListResModel>() {
            @Override
            public void onResponse(Call<PdfStoreListResModel> call, retrofit2.Response<PdfStoreListResModel> response) {
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
//                        Utils.dismissProgressDialog();

                        int total_pages = response.body().getTotal_pages();

                        ArrayList<PdfStoreListResModel.PdfListModel> data = new ArrayList<>();
                        data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            setGalleryListData(data);
                        }
                    } else {
//                        Utils.dismissProgressDialog();
                    }
                    isLoading = false;

                } else {
//                    Utils.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<PdfStoreListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void setGalleryListData(ArrayList<PdfStoreListResModel.PdfListModel> myShelfResModels) {
        mGalleryListAdapter.newData(myShelfResModels);
    }
}

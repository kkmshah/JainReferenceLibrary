package com.jainelibrary.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.activity.KeywordSearchDetailsActivity;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.AppDataAndUsersResModel;
import com.jainelibrary.retrofitResModel.CountResModel;
import com.jainelibrary.retrofitResModel.SearchOptionResModel;
import com.jainelibrary.retrofitResModel.SendEmailResModel;
import com.jainelibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class DataUsersFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    TextView tvBooks, tvKeywords,tvTotalPages;
    TextView tvVisits, tvCountSearch, tvSeenReference;
    TextView tvKeywordLinkage, tvIndexLinkage, tvShlokLinkage, tvYearLinkage;
    TextView tvSharedReferences, tvSharedBooks, tvDownloadReferences, tvDownloadBooks, tvSavedReferences, tvRegisterUser,tvSavedBooks;
    String TAG = "DataUsersFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datauser, container, false);
        LoadUIElements(view);
        getAppDataAndUsers();
        /*getKeywordCount();
        getCountRegisterUser();
        getCountSearch();
        getTotalPages();*/
        return view;

    }

    private void getCountSearch() {

        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait", false);
        ApiClient.countSearch(new Callback<CountResModel>() {
            @Override
            public void onResponse(Call<CountResModel> call, Response<CountResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (Boolean.parseBoolean(response.body().getStatus())) {
                        //  Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        tvCountSearch.setText(" : "+response.body().getCount());
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getCount());
                        tvCountSearch.setText(" : ");
                        Log.e("error--", "statusTrue--" + response.body().getCount());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }
            @Override
            public void onFailure(Call<CountResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }
    private void getCountRegisterUser() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait", false);
        ApiClient.countRegisterUser(new Callback<CountResModel>() {
            @Override
            public void onResponse(Call<CountResModel> call, Response<CountResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (Boolean.parseBoolean(response.body().getStatus())) {
                        //  Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        tvRegisterCount.setText(" : "+response.body().getCount());
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getCount());
                //        tvRegisterCount.setText(" : ");
                        Log.e("error--", "statusTrue--" + response.body().getCount());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }
            @Override
            public void onFailure(Call<CountResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }

    private void LoadUIElements(View view) {
        tvBooks = view.findViewById(R.id.tvBookName);
        tvKeywords = view.findViewById(R.id.tvKeywords);
        tvTotalPages = view.findViewById(R.id.tvTotalPages);
        tvCountSearch = view.findViewById(R.id.tvCountSearch);
        tvKeywordLinkage = view.findViewById(R.id.tvKeywordLinkages);
        tvIndexLinkage = view.findViewById(R.id.tvIndexLinkage);
        tvShlokLinkage = view.findViewById(R.id.tvShlokLinkage);
        tvYearLinkage = view.findViewById(R.id.tvYearLinkage);
        tvVisits = view.findViewById(R.id.tvVisits);
        tvSeenReference = view.findViewById(R.id.tvSeenReference);
        tvSharedReferences = view.findViewById(R.id.tvSharedReferences);
        tvSharedBooks = view.findViewById(R.id.tvSharedBooks);
        tvDownloadReferences = view.findViewById(R.id.tvDownloadReferences);
        tvDownloadBooks = view.findViewById(R.id.tvDownloadBooks);
        tvSavedReferences = view.findViewById(R.id.tvSavedReferences);
        tvSavedBooks = view.findViewById(R.id.tvSavedBooks);
        tvRegisterUser = view.findViewById(R.id.tvRegisterUser);
    }

    private void getAppDataAndUsers() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait......", false);
        ApiClient.getAppDataandUsers(new Callback<AppDataAndUsersResModel>() {
            @Override
            public void onResponse(Call<AppDataAndUsersResModel> call, Response<AppDataAndUsersResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    AppDataAndUsersResModel appDataAndUsersResModel = response.body();
                    Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(appDataAndUsersResModel));

                    if (Boolean.parseBoolean(response.body().getStatus())) {
                        //  Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        tvBooks.setText(" : "+response.body().getBooks_count());
                        tvTotalPages.setText(" : "+response.body().getPages_count());
                        tvKeywords.setText(" : "+response.body().getKeywords_count());
                        tvCountSearch.setText(" : "+response.body().getTotal_searching());
                        tvVisits.setText(" : "+response.body().getTotal_visits());
                        tvSeenReference.setText(" : "+response.body().getSeen_references());
                        tvKeywordLinkage.setText(" : "+response.body().getKeyword_linkages());
                        tvIndexLinkage.setText(" : "+response.body().getIndex_linkages());
                        tvShlokLinkage.setText(" : "+response.body().getShlok_linkages());
                        tvYearLinkage.setText(" : "+response.body().getYear_linkages());
                        tvSharedReferences.setText(" : "+response.body().getShared_references());
                        tvSharedBooks.setText(" : "+response.body().getShared_books_references());
                        tvDownloadReferences.setText(" : "+response.body().getDownloaded_references());
                        tvDownloadBooks.setText(" : "+response.body().getDownloaded_books_references());
                        tvSavedReferences.setText(" : "+response.body().getSaved_references());
                        tvSavedBooks.setText(" : "+response.body().getSaved_books_references());
                        tvRegisterUser.setText(" : "+response.body().getTotal_registered_user());

                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getBooks_count());
                        tvBooks.setText(" : ");
                        Log.e("error--", "statusTrue--" + response.body().getBooks_count());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<AppDataAndUsersResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });

    }

    private void getBookCount() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait......", false);
        ApiClient.countBooks(new Callback<CountResModel>() {
            @Override
            public void onResponse(Call<CountResModel> call, Response<CountResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (Boolean.parseBoolean(response.body().getStatus())) {
                        //  Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        tvBooks.setText(" : "+response.body().getCount());
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getCount());
                        tvBooks.setText(" : ");
                        Log.e("error--", "statusTrue--" + response.body().getCount());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<CountResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });

    }
    private void getKeywordCount() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait", false);
        ApiClient.countKeywords(new Callback<CountResModel>() {
            @Override
            public void onResponse(Call<CountResModel> call, Response<CountResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (Boolean.parseBoolean(response.body().getStatus())) {
                        //  Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        tvKeywords.setText(" : "+response.body().getCount());
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getCount());
                        tvKeywords.setText(" : ");
                        Log.e("error--", "statusTrue--" + response.body().getCount());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }
            @Override
            public void onFailure(Call<CountResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }
    private void getTotalPages() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait", false);
        ApiClient.getTotalPages(new Callback<CountResModel>() {
            @Override
            public void onResponse(Call<CountResModel> call, Response<CountResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (Boolean.parseBoolean(response.body().getStatus())) {
                        //  Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        tvTotalPages.setText(" : "+response.body().getCount());
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getCount());
                        tvTotalPages.setText(" : ");
                        Log.e("error--", "statusTrue--" + response.body().getCount());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }
            @Override
            public void onFailure(Call<CountResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());
            }
        });
    }
}


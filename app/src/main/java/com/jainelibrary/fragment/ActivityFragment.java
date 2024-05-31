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
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.AddAllMyShelfResModel;
import com.jainelibrary.retrofitResModel.CheckResModel;
import com.jainelibrary.retrofitResModel.FilterBookResModel;
import com.jainelibrary.retrofitResModel.SaveFilterBooksResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;

public class ActivityFragment extends Fragment {
    String strUid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView tvUserName, tvMobileNo, tvEmail,  tvFirstLogin, tvTillLogin, tvTotalSearching, tvSeenReference, tvSharedReferences,
            tvSharedBooks, tvDownloadReferences, tvDownloadBooks, tvSavedReferences, tvSavedBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, container, false);
        declaration(view);
        return view;
    }

    private void declaration(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        tvMobileNo = view.findViewById(R.id.tvMobileNo);
        tvEmail = view.findViewById(R.id.tvEmail);

        tvFirstLogin = view.findViewById(R.id.tvFirstLogin);
        tvTillLogin = view.findViewById(R.id.tvTillLogin);
        tvTotalSearching = view.findViewById(R.id.tvTotalSearching);
        tvSeenReference = view.findViewById(R.id.tvSeenReference);
        tvSharedReferences = view.findViewById(R.id.tvSharedReferences);
        tvSharedBooks = view.findViewById(R.id.tvSharedBooks);
        tvDownloadReferences = view.findViewById(R.id.tvDownloadReferences);
        tvDownloadBooks = view.findViewById(R.id.tvDownloadBooks);
        tvSavedReferences = view.findViewById(R.id.tvSavedReferences);
        tvSavedBooks = view.findViewById(R.id.tvSavedBooks);
        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUid = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            Log.e("UserId---", "UserId---" + strUid);
            if (strUid != null && strUid.length() > 0) {
                checkUserName();
            }
        }

    }

    private void checkUserName() {

        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }


        ApiClient.getUserDetails(strUid, new Callback<UserDetailsResModel>() {
            @Override
            public void onResponse(Call<UserDetailsResModel> call, retrofit2.Response<UserDetailsResModel> response) {

                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        UserDetailsResModel.UserDetailsModel userDetailsModel = response.body().getData();
                        Log.e("resActivityUserData :", new GsonBuilder().setPrettyPrinting().create().toJson(userDetailsModel));
                        if(userDetailsModel.getFirst_login() != null && userDetailsModel.getFirst_login().length() > 0){
                            tvFirstLogin.append(userDetailsModel.getFirst_login());
                        }

                        tvEmail.append(userDetailsModel.getEmail());
                        tvMobileNo.append(userDetailsModel.getMobile());
                        tvSeenReference.append(userDetailsModel.getSeen_references());
                        tvTillLogin.append(userDetailsModel.getTill_date_login());
                        tvTotalSearching.append(userDetailsModel.getTotal_searching());
                        tvUserName.append(userDetailsModel.getUsername());
                        tvSharedReferences.append(userDetailsModel.getShared_references());
                        tvSharedBooks.append(userDetailsModel.getShared_books_references());
                        tvDownloadReferences.append(userDetailsModel.getDownloaded_references());
                        tvDownloadBooks.append(userDetailsModel.getDownloaded_books_references());
                        tvSavedReferences.append(userDetailsModel.getSaved_references());
                        tvSavedBooks.append(userDetailsModel.getSaved_books_references());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserDetailsResModel> call, @NotNull Throwable throwable) {

            }
        });
    }



}

package com.jainelibrary.fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.adapter.UserGuideOptionAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UserGuideResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutFragment extends Fragment {
    private final String TAG = "About";
    RecyclerView rvList;
    private View header1, header2, appBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_guide, container, false);
        loadUiElements(view);
        declaration(view);
        return view;
    }

    private void loadUiElements(View view) {
        header1 = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
        appBarLayout = getActivity().findViewById(R.id.appbar);

        rvList = view.findViewById(R.id.rvList);
    }


    private void declaration(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        callUserGuideApi();
    }

    private void callUserGuideApi() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait......", false);
        ApiClient.getUserGuide("4", new Callback<UserGuideResModel>() {
            @Override
            public void onResponse(Call<UserGuideResModel> call, Response<UserGuideResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        List<UserGuideResModel.UserGuideModel> userGuideModelList = response.body().getData();
                        setUserGuideOptionProvider(userGuideModelList);
                    } else {
                        Utils.showInfoDialog(getActivity(), "No Filter Option Found");
                    }
                } else {
                    Log.e(TAG, "onUserGuideSearchOption--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserGuideResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e(TAG, "onUserGuideSearchOption OnFailure --" + t.getMessage());
            }
        });

    }

    private void setUserGuideOptionProvider(List<UserGuideResModel.UserGuideModel> userGuideModelList) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(linearLayoutManager);
        UserGuideOptionAdapter userGuideOptionAdapter = new UserGuideOptionAdapter(getActivity(), userGuideModelList);
        rvList.setAdapter(userGuideOptionAdapter);
        //rvLens.setHasFixedSize(true);
        rvList.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                header1.setTranslationY(-80);
                header2.setTranslationY(-40);
                header1.setVisibility(View.GONE);
                header2.setVisibility(View.GONE);
                appBarLayout.setMinimumHeight(0);
            }
            @Override
            public void onShow() {
                header1.setTranslationY(0);
                header2.setTranslationY(0);
                header1.setVisibility(View.VISIBLE);
                header2.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}


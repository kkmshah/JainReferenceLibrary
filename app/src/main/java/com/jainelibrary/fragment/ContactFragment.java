package com.jainelibrary.fragment;

import android.os.Bundle;
import android.os.TestLooperManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.GsonBuilder;
import com.jainelibrary.R;
import com.jainelibrary.activity.NotesActivity;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.UpdateMyShelfNotesModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.SendEmailResModel;
import com.jainelibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView textemail;
    EditText edtName, edtEmail, edtMessage;
    String strName, strEmail, strMsg;
    Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        LoadUIelements(view);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName = edtName.getText().toString();
                strEmail = edtEmail.getText().toString();
                strMsg = edtMessage.getText().toString();
                if ((strName == null) && (strName.length() == 0)) {
                    edtName.setError("Please Enter Your Name");

                } else if ((strEmail == null) && (strEmail.length() == 0)) {
                    edtEmail.setError("Please, Enter Your Email Address");

                } else if (!Utils.isValidEmail(strEmail)) {
                    edtEmail.setError("Please, Enter Valid Email Address");

                } else if ((strMsg == null) && (strMsg.length() == 0)) {
                    edtMessage.setError("Plese Enter Your Message");

                } else {


                    SendEmail();
                }
            }
        });
        return view;
    }



    private void LoadUIelements(View view) {
        btnSubmit = view.findViewById(R.id.btnSubmit);
        textemail = view.findViewById(R.id.textemail);
        Linkify.addLinks(textemail, Linkify.EMAIL_ADDRESSES);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtMessage = view.findViewById(R.id.edtMessage);
    }

    private void SendEmail() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please wait....", false);
        ApiClient.sendEmail(strName, strEmail, strMsg, new Callback<SendEmailResModel>() {
            @Override
            public void onResponse(Call<SendEmailResModel> call, Response<SendEmailResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().getStatus()) {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());

                        Log.e("error--", "statusTrue--" + response.body().getMessage());
                        getActivity().onBackPressed();
                        edtEmail.setText("");
                        edtMessage.setText("");
                        edtName.setText("");
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                } else {
                    Log.e("error--", "ResultError--" + response.message());
                }
            }

            @Override
            public void onFailure(Call<SendEmailResModel> call, Throwable t) {
                Utils.dismissProgressDialog();
                Log.e("error", "" + t.getMessage());

            }

        });

    }

}
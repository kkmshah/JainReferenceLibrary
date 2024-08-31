package com.jainelibrary.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.jainelibrary.R;
import com.jainelibrary.activity.BookDetailsActivity;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.activity.LoginWithPasswordActivity;
import com.jainelibrary.activity.NotesActivity;
import com.jainelibrary.activity.ReferencePageActivity;
import com.jainelibrary.adapter.ViewListAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.model.ViewListResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.AddAllMyShelfResModel;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.wc.widget.dialog.IosDialog;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jainelibrary.utils.Utils.REF_TYPE_BOOK_PAGE;

public class ViewListFragment extends Fragment implements ViewListAdapter.ViewListInteface, Paginate.Callbacks {

    private static final int PDF_CODE = 1;
    private static final String TAG = ViewListFragment.class.getSimpleName();
    String[] spinnerA = {"Book Name Wise", "Author Name Wise", "Publisher Name Wise"};
    String[] spinnerB = {"All", "Dictionary", "History of Literature", "History of Traditional", "Tatvagyan"};
    Spinner spinnerListA, spinnerListB;
    LinearLayout llSpinner;
    RecyclerView rvList;
    ArrayList<PdfStoreListResModel.PdfListModel> pdfListModelArrayList = new ArrayList<>();
    ArrayList<ViewListResModel> viewListResModels = new ArrayList<>();
    String strUId, strFlag = "3";
    private String strType = "0", strCatId = "0", strSearch;
    private ViewListAdapter mPdfAdadpter;
    TextView tvNoRecord;
    private final String book_names[] = {"Jain Tatva", "Gurunanak", "History Of Book", "Literature book", "Tatvagyan"};
    Paginate paginate;
    boolean isFirstTime = false,
            isLoading = false;
    int page = 1;
    int totalPages = 5000;
    boolean isAPIcalled = false;
    View header1, header2;
    AppBarLayout appBarLayout;
    private String PackageName, strBookName, shareData, strMessage;
    String strEdtRenamefile = null, strUserId, shareText, strPDfUrl, strTypeRef;
    Activity activity;


    public ViewListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewlist, container, false);
        setHeader();
        loadUiElements(view);
        declarations(view);
        return view;
    }

    private void setHeader() {
        header1 = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
        appBarLayout = getActivity().findViewById(R.id.appbar);
    }


    private void loadUiElements(View view) {
        spinnerListA = view.findViewById(R.id.spinnerListA);
        spinnerListB = view.findViewById(R.id.spinnerListB);
        rvList = view.findViewById(R.id.rvList);
        llSpinner = view.findViewById(R.id.llSpinner);
        tvNoRecord = view.findViewById(R.id.tvNoRecord);
    }


    private void declarations(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        PackageName = getActivity().getPackageName();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerA);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListA.setAdapter(arrayAdapter);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerB);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListB.setAdapter(arrayAdapter1);
        spinnerListA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strSpinnerA = spinnerA[i];
                boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Book Name wise")) {
                    strType = "0";
                }
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Author Name wise")) {
                    strType = "1";
                }
                if (strSpinnerA != null && strSpinnerA.equalsIgnoreCase("Publisher Name wise")) {
                    strType = "2";
                }

                Log.e("item", pdfListModelArrayList.size() + "");
                if (!isAPIcalled) {
                    isAPIcalled = true;
                    page = 1;
                    pdfListModelArrayList.clear();
                    if(mPdfAdadpter!= null)
                        mPdfAdadpter.notifyDataSetChanged();
                    else
                      //  mPdfAdadpter.notifyDataSetChanged();
                    getPdfList(strSearch, strCatId, strType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerListB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strSpinnerB = spinnerB[i];
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("All")) {
                    strCatId = "0";
                }
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("Dictionary")) {
                    strCatId = "1";
                }
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("History of Literature")) {
                    strCatId = "2";
                }
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("Tatvagyan")) {
                    strCatId = "3";
                }
                if (strSpinnerB != null && strSpinnerB.equalsIgnoreCase("History of Traditional")) {
                    strCatId = "4";
                }
                Log.e("item", pdfListModelArrayList.size() + "");
                if (isAPIcalled == false) {
                    isAPIcalled = true;
                        page = 1;
                    pdfListModelArrayList.clear();
                    if(mPdfAdadpter!= null)
                        mPdfAdadpter.notifyDataSetChanged();
                    else
                        mPdfAdadpter.notifyDataSetChanged();
                    getPdfList(strSearch, strCatId, strType);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
       /* boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            if (strUId != null && strUId.length() > 0) {
                getPdfList(strSearch,strCatId, strType);
            }
        } else {
            askLogin();
        }*/
    }

    private void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), PDF_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
            if (strUId != null && strUId.length() > 0) {
                Log.e("item", pdfListModelArrayList.size() + "");
                pdfListModelArrayList.clear();
                page = 1;
                if (!isAPIcalled) {
                    isAPIcalled = true;
                    getPdfList(strSearch, strCatId, strType);
                }
            }
        } else {
            //  askLogin();
        }
    }

    public void getPdfList(String strSearch, String strCatId, String strType) {
        isFirstTime = false;
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        Log.e("LOG" + strSearch, strCatId + " " + strType);
        ApiClient.getPaginatewithPDF(strSearch, strCatId, strType, String.valueOf(page), "10", new Callback<PdfStoreListResModel>() {
            @Override
            public void onResponse(Call<PdfStoreListResModel> call, retrofit2.Response<PdfStoreListResModel> response) {
                isAPIcalled = false;
                if (response.isSuccessful()) {
                     /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        Utils.dismissProgressDialog();
                        ArrayList<PdfStoreListResModel.PdfListModel> data = new ArrayList<>();
                        data = response.body().getData();
                        Log.e("pdfsize", pdfListModelArrayList.size() + "");
                        Log.e("datasize", data.size() + "");
                        if (data != null && data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                pdfListModelArrayList.add(data.get(i));
                            }
                            tvNoRecord.setVisibility(View.GONE);

                            rvList.setVisibility(View.VISIBLE);
                            setBookData(pdfListModelArrayList);

                            Log.e("datasize 2", data.size() + "");
                            Log.e("pdfsize 2", pdfListModelArrayList.size() + "");
                        } else {
                            mPdfAdadpter.notifyDataSetChanged();
                            return;
                        }
                        page++;
                        isLoading = false;
                        isFirstTime = true;
                    } else {
                        Utils.dismissProgressDialog();
                        // rvList.setVisibility(View.GONE);
                        //  tvNoRecord.setVisibility(View.VISIBLE);
                    }
                } else {
                    Utils.dismissProgressDialog();
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

    private void setBookData(ArrayList<PdfStoreListResModel.PdfListModel> bookDetailList) {
        if (bookDetailList == null || bookDetailList.size() == 0) {
            return;
        }

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setVisibility(View.VISIBLE);
        mPdfAdadpter = new ViewListAdapter(getContext(), bookDetailList, this);
        mPdfAdadpter.notifyDataSetChanged();
        rvList.setAdapter(mPdfAdadpter);
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
        if (paginate != null) {
            paginate.unbind();
        }
        paginate = Paginate.with(rvList, this)
                .setLoadingTriggerThreshold(0)
                .addLoadingListItem(false)
                //	.setLoadingListItemCreator(customLoadingListItem ? new CustomLoadingListItemCreator() : null)
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return 2;
                    }
                })
                .build();
        paginate.setHasMoreDataToLoad(false);
    }

    @Override
    public void onMenuClick(PdfStoreListResModel.PdfListModel filesModel,
                            int position, ImageView ivMenu) {
        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (isLogin) {
            showPopUpMenu(ivMenu, filesModel);
        } else {
            askLogin();
        }
    }


    private void showPopUpMenu(ImageView ivMenu, PdfStoreListResModel.PdfListModel filesModel) {

        PopupMenu popup = new PopupMenu(getActivity(), ivMenu);
        popup.getMenuInflater().inflate(R.menu.gallery_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String strBookId = filesModel.getBook_id();
                Log.e("strKeywordId---", "strKeywordId--" + strBookId);
                String strKeywordId = filesModel.getKeyword_id();
                Log.e("strKeywordId---", "strKeywordId--" + strKeywordId);
                strBookName = filesModel.getBook_name();
                String strPdfPageNo = filesModel.getPdf_page_no();
                String strEditorName = filesModel.getEditor_name();
                String strPublisherName = filesModel.getPublisher_name();
                String strTranslator = filesModel.getTranslator();
                Log.e("strKeywordId---", "strKeywordId--" + strPublisherName);
                String strBookUrl = filesModel.getBook_url();
                strPDfUrl = filesModel.getPdf_url();
                Log.e("strPDfUrl---", "strPDfUrl--" + strPDfUrl);
                Log.e(strBookId, strBookName + " " + strKeywordId);

                strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);

                BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
                mBookDataModels.setKeyword(strBookName);
                mBookDataModels.setKeywordId(strKeywordId);
                mBookDataModels.setBook_id(strBookId);
                mBookDataModels.setBook_name(strBookName);
                if (strPDfUrl != null && strPDfUrl.length() > 0) {
                    mBookDataModels.setPdf_link(strPdfPageNo);
                }
                if (strPdfPageNo != null && strPdfPageNo.length() > 0) {
                    mBookDataModels.setPdf_page_no(strPdfPageNo);
                    mBookDataModels.setPage_no(strPdfPageNo);
                } else {
                    mBookDataModels.setPdf_page_no("0");
                    mBookDataModels.setPage_no("0");
                }
                mBookDataModels.setEditor_name(strEditorName);
                mBookDataModels.setPublisher_name(strPublisherName);
                mBookDataModels.setTranslator(strTranslator);
                mBookDataModels.setBook_url(strBookUrl);
                mBookDataModels.setBook_image(filesModel.getBook_image());
                mBookDataModels.setBook_large_image(filesModel.getBook_large_image());

                mBookDataModels.setPdf_link(strPDfUrl);
                mBookDataModels.setFlag(strFlag);

                switch (item.getItemId()) {
                    case R.id.open:
                        Intent is = new Intent(getActivity(), ReferencePageActivity.class);
                        is.putExtra("model", mBookDataModels);
                        startActivity(is);
                        return true;
                    case R.id.book_info:
                        Intent i = new Intent(getActivity(), BookDetailsActivity.class);
                        i.putExtra("model", mBookDataModels);
                        startActivity(i);
                        return true;
                    case R.id.share:
                        if (strPDfUrl != null && strPDfUrl.length() > 0) {
                       /*  strBookName = "JRL_" + strBookName ;
                            Utils.downloadsPdf(strBookName, strPDfUrl, getActivity());
                            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                            File fileWithinMyDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Internal Storage/Download/",strBookName);
                            Log.e("File",Environment.getExternalStorageDirectory().getAbsolutePath()+"/Downloads/"+strBookName);
                            Log.e("File","File download");
                            if(fileWithinMyDir.exists()) {
                                Log.e("File","File Existr");
                                intentShareFile.setType("application/pdf");
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+strBookName));
                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                        "Sharing File...");
                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                                startActivity(Intent.createChooser(intentShareFile, "Share File"));
                            }*/
                            shareData = " Get Latest JRl Books here : https://play.google.com/store/apps/details?id=" + PackageName;
                            strMessage = " " + strPDfUrl;
                            callShareMyShelfsApi(strUserId, shareData, strMessage);
                        } else {
                            Utils.showInfoDialog(getActivity(), "Pdf not found");
                            Log.e("strPdfLinkShare---", "pdfLink--" + strPDfUrl);
                        }

                        return true;
                    case R.id.download:
                        callDownloadMyShelfsApi(strUserId, strPDfUrl, strBookName);
                        return true;
                    case R.id.my_reference:
                        if (strBookId != null && strBookName != null) {
                           // callAllAddMyShelfApi(strUserId, "");
                            UploadFile(strBookId, strBookName, strKeywordId);
                            //callAddMyShelfApi(strBookId, strBookName, strKeywordId);
                        } else {
                            Log.e(TAG, "anyone null");
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void callAllAddMyShelfApi(String strUid, String strBookId) {

        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);

        ApiClient.addAllMyShelf(strUid, strBookId,new Callback<AddAllMyShelfResModel>() {
            @Override
            public void onResponse(Call<AddAllMyShelfResModel> call, retrofit2.Response<AddAllMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {
                        strUserId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                    }else{
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddAllMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : "+throwable.getMessage());
            }
        });
    }

    private void callDownloadMyShelfsApi(String strUserId, String strPDfUrl, String strBookName) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);

        ApiClient.downloadMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {
                        /*strUserId = SharedPrefManager.getInstance(ReferencePageActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        if (strUserId != null && strUserId.length() > 0) {
                            callListHoldSearchKeyword(strUserId);
                        }*/
                        if (strPDfUrl != null && strPDfUrl.length() > 0) {
                            Utils.downloadPdf(strBookName, strPDfUrl, getActivity());
                        } else {
                            Utils.showInfoDialog(getActivity(), "Pdf not found");
                            Log.e("strPdfLinkShare---", "pdfLink--" + strPDfUrl);
                        }
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : "+throwable.getMessage());
            }
        });
    }

    private void callShareMyShelfsApi(String strUserId, String shareData, String strMessage) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        strTypeRef = Utils.REF_TYPE_REFERENCE_PAGE;
        Log.e("strUserId :", ""+strUserId);
        Log.e("strTypeRef :", " "+strTypeRef);
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);

        ApiClient.shareMyShelfs(strUserId, strTypeRef, new Callback<ShareOrDownloadMyShelfResModel>() {
            @Override
            public void onResponse(Call<ShareOrDownloadMyShelfResModel> call, retrofit2.Response<ShareOrDownloadMyShelfResModel> response) {
                if (response.isSuccessful()) {
                    Utils.dismissProgressDialog();

                    //   Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                    if (response.body().isStatus()) {
                        /*strUserId = SharedPrefManager.getInstance(ReferencePageActivity.this).getStringPref(SharedPrefManager.KEY_USER_ID);
                        if (strUserId != null && strUserId.length() > 0) {
                            callListHoldSearchKeyword(strUserId);
                        }*/

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_SUBJECT, shareData);
                        intent.putExtra(Intent.EXTRA_TEXT, strMessage);
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent, shareData));

                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(ReferencePageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareOrDownloadMyShelfResModel> call, Throwable throwable) {
                Utils.dismissProgressDialog();
                Log.e("onFailure :", "Move All Api : "+throwable.getMessage());
            }
        });
    }

    private void UploadFile(String strBookId, String strBookName, String strKeywordId) {
        RequestBody bookId = RequestBody.create(MediaType.parse("text/*"), strBookId);
        RequestBody keywordId = RequestBody.create(MediaType.parse("text/*"), strKeywordId);
        RequestBody uid = RequestBody.create(MediaType.parse("text/*"), strUId);
        RequestBody filename = RequestBody.create(MediaType.parse("text/*"), "JainELibrary_"+strBookName+"_"+strKeywordId);
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), "0");
        RequestBody typeref = RequestBody.create(MediaType.parse("text/*"), REF_TYPE_BOOK_PAGE);
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.addMyShelfs(uid, bookId,null, null, typeref, null, null,null, null, null, new Callback<AddShelfResModel>() {
            @Override
            public void onResponse(Call<AddShelfResModel> call, Response<AddShelfResModel> response) {
                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/
                    Utils.dismissProgressDialog();
                    if (response.body().isStatus()) {
                        String strNotes = response.body().getNotes();
                        String strType = response.body().getType();
                        String strTypeRef = response.body().getType_ref();
                        String strKeywordId = response.body().getType_id();
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                        getInfoDialogs(strNotes, strType, strTypeRef, "Do you want to add notes?", strBookName, strKeywordId);
                    } else {
                        Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                        Log.e("error--", "statusFalse--" + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddShelfResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", message);
                Utils.dismissProgressDialog();
                Utils.showInfoDialog(getActivity(), "Something went wrong please try again later");
            }
        });
    }

    private void callAddMyShelfApi(String strBookId, String strBookName, String strKeywordId) {
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.addMyShelf( strUId, strBookId, strKeywordId,  Utils.TYPE_PDF_BOOK_PAGE, REF_TYPE_BOOK_PAGE, "JainELibrary_"+strBookName+"_"+strKeywordId,null,
                new Callback<AddShelfResModel>() {
                    @Override
                    public void onResponse(Call<AddShelfResModel> call, retrofit2.Response<AddShelfResModel> response) {
                        Utils.dismissProgressDialog();
                        if (response.isSuccessful()) {
                             /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                            if (response.body().isStatus()) {
                                String strNotes = response.body().getNotes();
                                String strType = response.body().getType();
                                String strTypeRef = response.body().getType_ref();
                                String strKeywordId = response.body().getType_id();
                                Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                                getInfoDialogs(strNotes, strType, strTypeRef, "Do you want to add notes?", strBookName, strKeywordId);
                            } else {
                                Utils.showInfoDialog(getActivity(), "" + response.body().getMessage());
                                Log.e("error--", "statusFalse--" + response.body().getMessage());
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
    }

    public void getInfoDialogs(String strNotes, String strType, String strTypeRef, String
            strTite, String strBookName, String strKeywordId) {

        Dialog dialog = new IosDialog.Builder(getActivity())
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
                        Intent intent = new Intent(getActivity(), NotesActivity.class);
                        intent.putExtra("strNotes", strNotes);
                        intent.putExtra("strType", strType);
                        intent.putExtra("strTypeRef", strTypeRef);
                        intent.putExtra("strKeyword", strBookName);
                        intent.putExtra("strKeywordId", strKeywordId);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }
    @Override
    public void onLoadMore() {
        Log.e("Paginate", "onLoadMore");
        if (isFirstTime) {
            isLoading = true;
            Log.e("item", pdfListModelArrayList.size() + "");
            getPdfList(strSearch, strCatId, strType);
        }
    }
    @Override
    public boolean isLoading() {
        return isLoading;
    }
    @Override
    public boolean hasLoadedAllItems() {
        isLoading = false;
        Log.e("page total page", (page >= totalPages) + "");
        return page >= totalPages;
    }

    /*
    @Override
    public void onDetailsClick(ArrayList<PdfStoreListResModel.PdfListModel> pdfBookList, int position) {
        String strBookId = filesModel.getBook_id();
        String strBookName = filesModel.getBook_name();
        String strPdfPageNo = filesModel.getPdf_page_no();
        String strEditorName = filesModel.getEditor_name();
        String strPublisherName = filesModel.getPublisher_name();
        String strTranslator = filesModel.getTranslator();
        BookListResModel.BookDetailsModel mBookDataModels = new BookListResModel.BookDetailsModel();
        mBookDataModels.setKeyword(strBookName);
        mBookDataModels.setBook_id(strBookId);
        mBookDataModels.setBook_name(strBookName);
        mBookDataModels.setPdf_page_no(strPdfPageNo);
        mBookDataModels.setEditor_name(strEditorName);
        mBookDataModels.setPublisher_name(strPublisherName);
        mBookDataModels.setTranslator(strTranslator);
        mBookDataModels.setFlag(strFlag);
        Intent i = new Intent(getActivity(), BookDetailsActivity.class);
        i.putExtra("model", mBookDataModels);
        startActivity(i);
    }*/
}


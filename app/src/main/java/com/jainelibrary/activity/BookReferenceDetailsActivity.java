package com.jainelibrary.activity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.adapter.ReferencePageListAdapter;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.BookReferenceDetailsResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class BookReferenceDetailsActivity extends AppCompatActivity {
    private static final String PAGE_LIMIT = "50";
    private static final String TAG = BookReferenceDetailsActivity.class.getSimpleName();

    private String strBookId, strFlag;
    boolean isLoading = false;

    ImageView ivImage;
    TextView tvPageName, tvAuthorName, tvPublisherName, tvEdition, tvLanguage;
    TextView tvPDFPages, tvImages;

    ImageView ivKeyword, ivShlok, ivIndex, ivYear, ivAll;

    TextView tvKeyword, tvShlok, tvIndex, tvYear, tvAll;
    TextView tvKeywordCount, tvShlokCount, tvIndexCount, tvYearCount, tvAllCount;

    TextView tvRAll, tvPending, tvGeneral, tvSpecial, tvMarked, tvNotMarked;
    TextView tvRAllCount, tvPendingCount, tvGeneralCount, tvSpecialCount, tvMarkedCount, tvNotMarkedCount;

    CardView cvKeyword, cvShlok, cvIndex, cvYear, cvAll;
    CardView cvPending, cvGeneral, cvSpecial, cvMarked, cvNotMarked, cvRAll;

    LinearLayout llPageDiffs, llAdjustPages;

    TextView tvRecordsCount;
    RecyclerView rvReferencePages;

    int total_count = 0, total_pages = 1, page_no = 1, selectedReference = -1, selectedReferenceType = 0;
    String selectedPageDiff = "";

    ReferencePageListAdapter refPageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reference_details);

        loadUiElements();
        declaration();
        onEventListner();
        setHeader();

        cvAll.performClick();
        cvRAll.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPrefManager.getInstance(BookReferenceDetailsActivity.this).getBooleanPreference("ReloadBookData")) {
            SharedPrefManager.getInstance(BookReferenceDetailsActivity.this).setBooleanPreference("ReloadBookData", false);

            callBookReferenceDetailsApi(strBookId, strFlag, "" + page_no, "" + selectedReference, "" + selectedReferenceType);
//            finish();
//            startActivity(getIntent());
        }
    }


    private void declaration() {

        BookListResModel.BookDetailsModel mBookDataModels = (BookListResModel.BookDetailsModel) getIntent().getSerializableExtra("model");
        if (mBookDataModels != null) {
            strBookId = mBookDataModels.getBook_id();
            strFlag = mBookDataModels.getFlag();
        }else {
            strBookId = getIntent().getStringExtra("bookid");
            Log.e(TAG, "strBookId--" + strBookId);
        }

        if (strBookId != null && strBookId.length() > 0) {
            callBookReferenceDetailsApi(strBookId, strFlag, "" + page_no, "" + selectedReference, "" + selectedReferenceType);
        }
    }

    private void loadUiElements() {
        ivImage = findViewById(R.id.ivImage);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvPublisherName = findViewById(R.id.tvPublisherName);
        tvEdition = findViewById(R.id.tvEdition);
        tvLanguage = findViewById(R.id.tvLanguage);

        tvPDFPages = findViewById(R.id.tvPDFPages);
        tvImages = findViewById(R.id.tvImages);

        ivKeyword = findViewById(R.id.ivKeyword);
        ivShlok = findViewById(R.id.ivShlok);
        ivIndex = findViewById(R.id.ivIndex);
        ivYear = findViewById(R.id.ivYear);
        ivAll = findViewById(R.id.ivAll);

        cvKeyword = findViewById(R.id.cvKeyword);
        cvShlok = findViewById(R.id.cvShlok);
        cvIndex = findViewById(R.id.cvIndex);
        cvYear = findViewById(R.id.cvYear);
        cvAll = findViewById(R.id.cvAll);

        tvKeyword = findViewById(R.id.tvKeyword);
        tvShlok = findViewById(R.id.tvShlok);
        tvIndex = findViewById(R.id.tvIndex);
        tvYear = findViewById(R.id.tvYear);
        tvAll = findViewById(R.id.tvAll);

        tvKeywordCount = findViewById(R.id.tvKeywordCount);
        tvShlokCount = findViewById(R.id.tvShlokCount);
        tvIndexCount = findViewById(R.id.tvIndexCount);
        tvYearCount = findViewById(R.id.tvYearCount);
        tvAllCount = findViewById(R.id.tvAllCount);

        cvRAll = findViewById(R.id.cvRAll);
        cvPending = findViewById(R.id.cvPending);
        cvGeneral = findViewById(R.id.cvGeneral);
        cvSpecial = findViewById(R.id.cvSpecial);
        cvMarked = findViewById(R.id.cvMarked);
        cvNotMarked = findViewById(R.id.cvNotMarked);

        tvRAll = findViewById(R.id.tvRAll);
        tvPending = findViewById(R.id.tvPending);
        tvGeneral = findViewById(R.id.tvGeneral);
        tvSpecial = findViewById(R.id.tvSpecial);
        tvMarked = findViewById(R.id.tvMarked);
        tvNotMarked = findViewById(R.id.tvNotMarked);

        tvRAllCount = findViewById(R.id.tvRAllCount);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvGeneralCount = findViewById(R.id.tvGeneralCount);
        tvSpecialCount = findViewById(R.id.tvSpecialCount);
        tvMarkedCount = findViewById(R.id.tvMarkedCount);
        tvNotMarkedCount = findViewById(R.id.tvNotMarkedCount);

        llPageDiffs = findViewById(R.id.llPageDiffs);
        llAdjustPages = findViewById(R.id.llAdjustPages);

        tvRecordsCount = findViewById(R.id.tvRecordsCount);
        rvReferencePages = findViewById(R.id.rvReferencePages);
    }

    private void onEventListner() {
        cvKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReference = 0;
                selectedReferenceType = 0;

                perform_select_reference();
            }
        });

        cvShlok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReference = 1;
                selectedReferenceType = 0;

                perform_select_reference();
            }
        });

        cvIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReference = 2;
                selectedReferenceType = 0;

                perform_select_reference();
            }
        });

        cvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReference = 3;
                selectedReferenceType = 0;

                perform_select_reference();
            }
        });

        cvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReference = -1;
                selectedReferenceType = 0;

                perform_select_reference();
            }
        });

        cvRAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReferenceType = 0;

                perform_select_reference_type();
            }
        });

        cvPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReferenceType = 1;

                perform_select_reference_type();
            }
        });

        cvMarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReferenceType = 2;

                perform_select_reference_type();
            }
        });

        cvNotMarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReferenceType = 3;

                perform_select_reference_type();
            }
        });

        cvGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReferenceType = 4;

                perform_select_reference_type();
            }
        });

        cvSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReferenceType = 5;

                perform_select_reference_type();
            }
        });

        rvReferencePages.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (lastVisibleItem == totalItemCount - 1) {
                        if (!isLoading) {
                            if (page_no < total_pages) {
                                page_no++;
                                callBookReferencePagesApi(strBookId, "" + page_no, "" + selectedReference, "" + selectedReferenceType, selectedPageDiff);
                            }
                        }
                    }
                }
            }
        });
    }

    private void perform_select_reference()
    {
        select_reference(selectedReference);
        unselect_references(selectedReference);

        select_reference_type(selectedReferenceType);
        unselect_reference_types(selectedReferenceType);

        page_no = 1;
        selectedPageDiff = "";

        unselectPageDiffs();

        callBookReferencesApi(strBookId, "" + page_no, "" + selectedReference, "" + selectedReferenceType);
    }

    private void perform_select_reference_type()
    {
        select_reference_type(selectedReferenceType);
        unselect_reference_types(selectedReferenceType);

        page_no = 1;
        selectedPageDiff = "";

        unselectPageDiffs();

        callBookReferencePagesApi(strBookId, "" + page_no, "" + selectedReference, "" + selectedReferenceType, selectedPageDiff);
    }

    private void unselect_references(int type)
    {
        int background_color = R.color.yellow_100;
        int text_color = Color.BLACK;

        if (type != -1)
        {
            cvAll.setCardBackgroundColor(getResources().getColor(background_color));
            tvAll.setTextColor(text_color);
            tvAllCount.setTextColor(text_color);
        }

        if (type != 0)
        {
            cvKeyword.setCardBackgroundColor(getResources().getColor(background_color));
            tvKeyword.setTextColor(text_color);
            tvKeywordCount.setTextColor(text_color);
        }

        if (type != 1)
        {
            cvShlok.setCardBackgroundColor(getResources().getColor(background_color));
            tvShlok.setTextColor(text_color);
            tvShlokCount.setTextColor(text_color);
        }

        if (type != 2)
        {
            cvIndex.setCardBackgroundColor(getResources().getColor(background_color));
            tvIndex.setTextColor(text_color);
            tvIndexCount.setTextColor(text_color);
        }

        if (type != 3)
        {
            cvYear.setCardBackgroundColor(getResources().getColor(background_color));
            tvYear.setTextColor(text_color);
            tvYearCount.setTextColor(text_color);
        }
    }

    private void select_reference(int type)
    {
        int background_color = R.color.orange_A700;
        int text_color = Color.WHITE;

        if (type == -1)
        {
            cvAll.setCardBackgroundColor(getResources().getColor(background_color));
            tvAll.setTextColor(text_color);
            tvAllCount.setTextColor(text_color);
        }
        else if (type == 0)
        {
            cvKeyword.setCardBackgroundColor(getResources().getColor(background_color));
            tvKeyword.setTextColor(text_color);
            tvKeywordCount.setTextColor(text_color);
        }
        else if (type == 1)
        {
            cvShlok.setCardBackgroundColor(getResources().getColor(background_color));
            tvShlok.setTextColor(text_color);
            tvShlokCount.setTextColor(text_color);
        }
        else if (type == 2)
        {
            cvIndex.setCardBackgroundColor(getResources().getColor(background_color));
            tvIndex.setTextColor(text_color);
            tvIndexCount.setTextColor(text_color);
        }
        else if (type == 3)
        {
            cvYear.setCardBackgroundColor(getResources().getColor(background_color));
            tvYear.setTextColor(text_color);
            tvYearCount.setTextColor(text_color);
        }
    }

    private void unselect_reference_types(int type)
    {
        int background_color = R.color.light_green_A400;
        int text_color = Color.BLACK;

        if (type != 0)
        {
            cvRAll.setCardBackgroundColor(getResources().getColor(background_color));
            tvRAll.setTextColor(text_color);
            tvRAllCount.setTextColor(text_color);
        }

        if (type != 1)
        {
            cvPending.setCardBackgroundColor(getResources().getColor(background_color));
            tvPending.setTextColor(text_color);
            tvPendingCount.setTextColor(text_color);
        }

        if (type != 2)
        {
            cvMarked.setCardBackgroundColor(getResources().getColor(background_color));
            tvMarked.setTextColor(text_color);
            tvMarkedCount.setTextColor(text_color);
        }

        if (type != 3)
        {
            cvNotMarked.setCardBackgroundColor(getResources().getColor(background_color));
            tvNotMarked.setTextColor(text_color);
            tvNotMarkedCount.setTextColor(text_color);
        }

        if (type != 4)
        {
            cvGeneral.setCardBackgroundColor(getResources().getColor(background_color));
            tvGeneral.setTextColor(text_color);
            tvGeneralCount.setTextColor(text_color);
        }

        if (type != 5)
        {
            cvSpecial.setCardBackgroundColor(getResources().getColor(background_color));
            tvSpecial.setTextColor(text_color);
            tvSpecialCount.setTextColor(text_color);
        }
    }

    private void select_reference_type(int type)
    {
        int background_color = R.color.pink_600;
        int text_color = Color.WHITE;

        if (type == 0)
        {
            cvRAll.setCardBackgroundColor(getResources().getColor(background_color));
            tvRAll.setTextColor(text_color);
            tvRAllCount.setTextColor(text_color);
        }
        else if (type == 1)
        {
            cvPending.setCardBackgroundColor(getResources().getColor(background_color));
            tvPending.setTextColor(text_color);
            tvPendingCount.setTextColor(text_color);
        }
        else if (type == 2)
        {
            cvMarked.setCardBackgroundColor(getResources().getColor(background_color));
            tvMarked.setTextColor(text_color);
            tvMarkedCount.setTextColor(text_color);
        }
        else if (type == 3)
        {
            cvNotMarked.setCardBackgroundColor(getResources().getColor(background_color));
            tvNotMarked.setTextColor(text_color);
            tvNotMarkedCount.setTextColor(text_color);
        }
        else if (type == 4)
        {
            cvGeneral.setCardBackgroundColor(getResources().getColor(background_color));
            tvGeneral.setTextColor(text_color);
            tvGeneralCount.setTextColor(text_color);
        }
        else if (type == 5)
        {
            cvSpecial.setCardBackgroundColor(getResources().getColor(background_color));
            tvSpecial.setTextColor(text_color);
            tvSpecialCount.setTextColor(text_color);
        }
    }

    private void unselectPageDiffs()
    {
        int background_color = R.color.light_blue_A100;
        int text_color = Color.BLACK;

        for (int i=0; i<llAdjustPages.getChildCount(); i++)
        {
            CardView cardView = (CardView) llAdjustPages.getChildAt(i);
            LinearLayout linearLayout = (LinearLayout) cardView.getChildAt(0);
            TextView textView = (TextView) linearLayout.getChildAt(0);

            cardView.setCardBackgroundColor(getResources().getColor(background_color));
            textView.setTextColor(text_color);
        }
    }

    private void setHeader() {
        View headerView = findViewById(R.id.header);
        ImageView ivBack = headerView.findViewById(R.id.ivBack);
        LinearLayout llAddItem = headerView.findViewById(R.id.llAddItem);
        ivBack.setVisibility(View.VISIBLE);
        llAddItem.setVisibility(View.INVISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });
        tvPageName = headerView.findViewById(R.id.tvPage);
        tvPageName.setText("Book Reference Info");
    }

    public void callBookReferenceDetailsApi(String strBookID, String strFlag, String strPageNo, String strReference, String strReferenceType) {
        if (!ConnectionManager.checkInternetConnection(BookReferenceDetailsActivity.this)) {
            Utils.showInfoDialog(BookReferenceDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(BookReferenceDetailsActivity.this, "Please Wait...", false);
        ApiClient.getBookReferenceDetails(strBookID, strFlag, strPageNo, PAGE_LIMIT, strReference, strReferenceType, new Callback<BookReferenceDetailsResModel>() {
            @Override
            public void onResponse(Call<BookReferenceDetailsResModel> call, retrofit2.Response<BookReferenceDetailsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {

                        total_count = response.body().getTotal_count();
                        total_pages = response.body().getTotal_pages();

                        BookReferenceDetailsResModel.BookRefDetailsModel mBookRefDetails = response.body().getData();
                        BookReferenceDetailsResModel.BookRefDetailsModel.ReferenceModel referenceModel = mBookRefDetails.getReferenceModel();
                        BookReferenceDetailsResModel.BookRefDetailsModel.ReferenceTypeModel referenceTypeModel = mBookRefDetails.getReferenceTypeModel();
                        ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.PageDiffModel> pageDiffModels = mBookRefDetails.getPageDiffs();
                        ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel> referencePageModels = mBookRefDetails.gerReferencePageModels();

                        setBookDetails(mBookRefDetails);
                        setReferenceData(referenceModel);
                        setReferenceTypeData(referenceTypeModel);
                        setAdjustPageData(pageDiffModels);
                        setReferencePagesData(referencePageModels);
                    } else {
                        Utils.showInfoDialog(BookReferenceDetailsActivity.this, response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<BookReferenceDetailsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });

    }

    public void callBookReferencesApi(String strBookID, String strPageNo, String strReference, String strReferenceType) {
        if (!ConnectionManager.checkInternetConnection(BookReferenceDetailsActivity.this)) {
            Utils.showInfoDialog(BookReferenceDetailsActivity.this, "Please check internet connection");
            return;
        }

        Utils.showProgressDialog(BookReferenceDetailsActivity.this, "Please Wait...", false);
        ApiClient.getBookReferences(strBookID, strPageNo, PAGE_LIMIT, strReference, strReferenceType, new Callback<BookReferenceDetailsResModel>() {
            @Override
            public void onResponse(Call<BookReferenceDetailsResModel> call, retrofit2.Response<BookReferenceDetailsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        total_count = response.body().getTotal_count();
                        total_pages = response.body().getTotal_pages();

                        BookReferenceDetailsResModel.BookRefDetailsModel mBookRefDetails = response.body().getData();
                        BookReferenceDetailsResModel.BookRefDetailsModel.ReferenceTypeModel referenceTypeModel = mBookRefDetails.getReferenceTypeModel();
                        ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel> referencePageModels = mBookRefDetails.gerReferencePageModels();

                        setReferenceTypeData(referenceTypeModel);
                        setReferencePagesData(referencePageModels);

                    } else {
                        Utils.showInfoDialog(BookReferenceDetailsActivity.this, response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<BookReferenceDetailsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });

    }

    public void callBookReferencePagesApi(String strBookID, String strPageNo, String strReference, String strReferenceType, String strPageDiff) {
        if (!ConnectionManager.checkInternetConnection(BookReferenceDetailsActivity.this)) {
            Utils.showInfoDialog(BookReferenceDetailsActivity.this, "Please check internet connection");
            return;
        }

        isLoading = true;

        Utils.showProgressDialog(BookReferenceDetailsActivity.this, "Please Wait...", false);
        ApiClient.getBookReferencePages(strBookID, strPageNo, PAGE_LIMIT, strReference, strReferenceType, strPageDiff, new Callback<BookReferenceDetailsResModel>() {
            @Override
            public void onResponse(Call<BookReferenceDetailsResModel> call, retrofit2.Response<BookReferenceDetailsResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        total_count = response.body().getTotal_count();
                        total_pages = response.body().getTotal_pages();

                        BookReferenceDetailsResModel.BookRefDetailsModel mBookRefDetails = response.body().getData();
                        ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel> referencePageModels = mBookRefDetails.gerReferencePageModels();

                        setReferencePagesData(referencePageModels);
                        isLoading = false;
                    } else {
                        Utils.showInfoDialog(BookReferenceDetailsActivity.this, response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<BookReferenceDetailsResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });

    }

    private void setBookDetails(BookReferenceDetailsResModel.BookRefDetailsModel mBookRefDetails)
    {
        String strBookName = mBookRefDetails.getBook_name();
        String strAuthorName = mBookRefDetails.getAuthor_name();
        String strPublisherName = mBookRefDetails.getPublisher_name();
        String strEdition = mBookRefDetails.getEdition();
        String strLanguage = mBookRefDetails.getLanguage();
        String strBookUrl = mBookRefDetails.getBook_url();

        if (strBookUrl != null && strBookUrl.length() > 0) {
            Picasso.get().load(strBookUrl).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(ivImage);
        }

        if (strBookName != null && strBookName.length() > 0) {
            tvPageName.setText(strBookName);
        } else {
            tvPageName.setText("Book Reference Info");
        }

        if (strAuthorName != null && strAuthorName.length() > 0) {
            tvAuthorName.setText(strAuthorName);
        } else {
            tvAuthorName.setVisibility(View.GONE);
        }

        if (strPublisherName != null && strPublisherName.length() > 0) {
            tvPublisherName.setText(strPublisherName);
        } else {
            tvPublisherName.setVisibility(View.GONE);
        }

        if (strEdition != null && strEdition.length() > 0) {
            tvEdition.setText(strEdition);
        } else {
            tvEdition.setVisibility(View.GONE);
        }

        if (strLanguage != null && strLanguage.length() > 0) {
            tvLanguage.setText(strLanguage);
        } else {
            tvLanguage.setVisibility(View.GONE);
        }

        tvPDFPages.setText(mBookRefDetails.getPdf_page_no());
        tvImages.setText(mBookRefDetails.getTotal_images());
    }

    private void setReferenceData(BookReferenceDetailsResModel.BookRefDetailsModel.ReferenceModel referenceModel)
    {
        tvKeywordCount.setText(referenceModel.getKeyword_ref_count());
        tvShlokCount.setText(referenceModel.getShlok_ref_count());
        tvIndexCount.setText(referenceModel.getIndex_ref_count());
        tvYearCount.setText(referenceModel.getYear_ref_count());
        tvAllCount.setText(referenceModel.getAll_ref_count());
    }

    private void setReferenceTypeData(BookReferenceDetailsResModel.BookRefDetailsModel.ReferenceTypeModel referenceTypeModel)
    {
        tvRAllCount.setText(referenceTypeModel.getAll_count());
        tvPendingCount.setText(referenceTypeModel.getPending_count());
        tvGeneralCount.setText(referenceTypeModel.getGeneral_count());
        tvSpecialCount.setText(referenceTypeModel.getSpecial_count());
        tvMarkedCount.setText(referenceTypeModel.getMarked_count());
        tvNotMarkedCount.setText(referenceTypeModel.getNotmarked_count());
    }

    private void setAdjustPageData(ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.PageDiffModel> pageDiffModels)
    {
        llAdjustPages.removeAllViews();
        if (pageDiffModels.size() == 0)
        {
            llPageDiffs.setVisibility(View.GONE);
            return;
        }

        llPageDiffs.setVisibility(View.VISIBLE);

        int background_color = R.color.light_blue_A100;
        int text_color = Color.BLACK;

        for (int i=0; i<pageDiffModels.size(); i++)
        {
            BookReferenceDetailsResModel.BookRefDetailsModel.PageDiffModel pageDiffModel = pageDiffModels.get(i);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            int padding = Utils.pxFromDp(BookReferenceDetailsActivity.this, 5);

            CardView cardView = new CardView(BookReferenceDetailsActivity.this);
            cardView.setCardBackgroundColor(getResources().getColor(background_color));
            cardView.setRadius(Utils.pxFromDp(BookReferenceDetailsActivity.this, 10));
            cardView.setPadding(padding, padding,padding, padding);
            cardView.setUseCompatPadding(true);
            cardView.setLayoutParams(layoutParams);

            padding = Utils.pxFromDp(BookReferenceDetailsActivity.this, 10);

            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout linearLayout = new LinearLayout(BookReferenceDetailsActivity.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(padding, padding,padding, padding);
            linearLayout.setWeightSum(1);
            linearLayout.setLayoutParams(layoutParams);

            TextView textView = new TextView(BookReferenceDetailsActivity.this);
            textView.setTextColor(text_color);
            textView.setTextSize(14);
            textView.setText("-" + pageDiffModel.getDiff() + " : " + pageDiffModel.getCount());
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setLayoutParams(layoutParams);

            linearLayout.addView(textView);
            cardView.addView(linearLayout);

            llAdjustPages.addView(cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unselectPageDiffs();

                    selectedPageDiff = "" + pageDiffModel.getDiff();

                    int background_color = R.color.purple_400;
                    int text_color = Color.WHITE;

                    cardView.setCardBackgroundColor(getResources().getColor(background_color));
                    textView.setTextColor(text_color);

                    callBookReferencePagesApi(strBookId, "" + page_no, "" + selectedReference, "" + selectedReferenceType, selectedPageDiff);
                }
            });
        }
    }

    private void setReferencePagesData(ArrayList<BookReferenceDetailsResModel.BookRefDetailsModel.ReferencePageModel> referencePageModels)
    {
        if (total_count == 0) {
            tvRecordsCount.setText("No Records Found.");
        }
        else {
            tvRecordsCount.setText(total_count + " Records Found.");
        }
        Log.e("PageData", "" + page_no);
        if (page_no == 1) {
            rvReferencePages.setLayoutManager(new LinearLayoutManager(BookReferenceDetailsActivity.this));

            refPageListAdapter = new ReferencePageListAdapter(BookReferenceDetailsActivity.this, strBookId, page_no, selectedReference, selectedReferenceType, referencePageModels);
            rvReferencePages.setAdapter(refPageListAdapter);
        }
        else
        {
            Parcelable recyclerViewState = rvReferencePages.getLayoutManager().onSaveInstanceState();
            refPageListAdapter.newData(referencePageModels);
            rvReferencePages.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }
}
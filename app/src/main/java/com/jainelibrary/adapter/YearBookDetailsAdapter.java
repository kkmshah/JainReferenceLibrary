package com.jainelibrary.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.activity.YearBookDetailsActivity;
import com.jainelibrary.extraModel.SearchHistoryModel;
import com.jainelibrary.model.YearBookResModel;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class YearBookDetailsAdapter extends RecyclerView.Adapter<YearBookDetailsAdapter.ViewHolder> {
    Context mContext;
    //ArrayList<BookListResModel.BookDetailsModel> mBookList = new ArrayList<>();
    ArrayList<YearBookResModel.YearBookList> yearBookLists = new ArrayList<>();
    ArrayList<YearBookResModel.YearBookList.YearBook> bookList = new ArrayList<>();
    SearchInterfaceListener searchInterfaceListener;
    //ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookPageModels = new ArrayList<>();
    public static int BARRAY = 100;
    String strBookName, strBookId, strBookPageNo, strBookImage;

    public YearBookDetailsAdapter(Context mContext,
                                  ArrayList<YearBookResModel.YearBookList> yearBookLists,
                                  SearchInterfaceListener searchInterfaceListener) {
                this.mContext = mContext;
             this.yearBookLists = yearBookLists;
             this.searchInterfaceListener = searchInterfaceListener;
    }

    public YearBookDetailsAdapter(Context mContext, ArrayList<YearBookResModel.YearBookList> mBookDataList, SearchInterfaceListener searchInterfaceListener, String strBookName, String strBookId, String strBookPageNo, String strBookImage) {

        this.mContext = mContext;
        this.yearBookLists = mBookDataList;
        this.searchInterfaceListener = searchInterfaceListener;
        this.strBookName = strBookName;
        this.strBookId = strBookId;
        this.strBookPageNo = strBookPageNo;
        this.strBookImage = strBookImage;
    }

    @Override
    public YearBookDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_shlok_sutra_details, parent, false);
        return new YearBookDetailsAdapter.ViewHolder(v);
    }


    public interface SearchInterfaceListener {
        //void onDetailsClick(View view, YearBookResModel.YearBookList mKeywordModel, int position);

        void onDetailsClick(YearBookResModel.YearBookList yearBookList, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {



        //granthList.get(position).setKeywordId(strKeywordId);
        bookList.get(position).setBook_id(strBookId);
        bookList.get(position).setBook_name(strBookName);
        bookList.get(position).setPage_no(strBookPageNo);
        //bookList.get(position).setFlag(Utils.TYPE_SHLOK_PAGE);
        String strBookname = bookList.get(position).getBook_name();
        String strPageNo = bookList.get(position).getPage_no();
        String strAutherName = bookList.get(position).getAuthor_name();
        String strPublisherName = bookList.get(position).getPublisher_name();
        String strBookUrl = bookList.get(position).getBook_image();


        if (strBookImage != null && strBookImage.length() > 0) {
            Picasso.get().load(strBookImage).into(holder.ivBook);
        } else {
            holder.ivBook.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.light_background)));
        }

        String strFinalWord = null;

        if (strBookName != null && strBookName.length() > 0) {
            strFinalWord = strBookName;
        } else {
            strBookName = "";
        }

        if (strBookPageNo != null && strBookPageNo.length() > 0) {
            strBookPageNo = "" + "Page - " + strBookPageNo;
            holder.btnPageNo.setText(strBookPageNo);
        }

        if (strAutherName != null && strAutherName.length() > 0) {
            if (!strAutherName.equalsIgnoreCase("0")) {
                strAutherName = ", " + strAutherName/* + "&#91;" + "ले" + "&#93; "*/;
                strFinalWord = strBookName + strAutherName;
            }
        } else {
            strAutherName = "";
        }

        if (strPublisherName != null && strPublisherName.length() > 0) {
            if(!strPublisherName.equalsIgnoreCase("0"))
            {
                strPublisherName = ", " + strPublisherName/* + "&#91;" + "प्रका" + "&#93;"*/;
                strFinalWord = strBookName + strAutherName + strPublisherName;}
        } else {
            strPublisherName = "";
        }

        if (strFinalWord != null && strFinalWord.length() > 0) {
            holder.tvDetails.setText(Html.fromHtml(strFinalWord));
        }

        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(yearBookLists.get(position), position);
            }
        });


        holder.btnPageNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(yearBookLists.get(position), position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return yearBookLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDetails;
        CardView cvList;
        Button btnPageNo;
        ImageView ivBook;

        ViewHolder(View itemView) {
            super(itemView);
            tvDetails = (TextView) itemView.findViewById(R.id.textViewName);
            cvList = itemView.findViewById(R.id.cvList);
            btnPageNo = itemView.findViewById(R.id.btnPageNo);
            ivBook = itemView.findViewById(R.id.ivBook);
        }
    }
}

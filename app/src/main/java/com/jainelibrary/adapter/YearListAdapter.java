package com.jainelibrary.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;

import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class YearListAdapter extends RecyclerView.Adapter<YearListAdapter.ViewHolder> {
    ArrayList<BookListResModel.BookDetailsModel> yearlist;
    Context context;
    YearClickListener yearClickListener;
    ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookPageModels = new ArrayList<>();

    public YearListAdapter(Context context, ArrayList<BookListResModel.BookDetailsModel> yearlist, YearClickListener yearClickListener) {
        this.context = context;
        this.yearlist = yearlist;
        this.yearClickListener = yearClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_year_booklist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        yearlist.get(position).setFlag(Utils.TYPE_YEAR_PAGE);

        String strPublisherName = yearlist.get(position).getPublisher_name();
        String strBookname = yearlist.get(position).getBook_name();
        bookPageModels = yearlist.get(position).getBooks();
        String strPageNo = yearlist.get(position).getPage_no();
        String strPdfPageNo = yearlist.get(position).getPdf_page_no();
        String strAutherName = yearlist.get(position).getAuthor_name();
        String strBookUrl = yearlist.get(position).getBook_url();
        Log.e(strPublisherName, strPdfPageNo + "  " + strBookname + "  " + strAutherName);
        if (strPdfPageNo == null || strPdfPageNo.length() == 0) {
            yearlist.get(position).setPdf_page_no(strPageNo);
        }
        String strFinalWord = null;
        if (strBookname != null && strBookname.length() > 0) {
            if (!strBookname.equalsIgnoreCase("0")) {
                strFinalWord = strBookname;
                holder.textViewName.setText(strBookname);
            }
        } else {
            strBookname = "";
        }

        if (bookPageModels != null && bookPageModels.size()>0){
            holder.llReferenceLayout.setVisibility(View.VISIBLE);
            holder.tvReferCount.setText( ""+bookPageModels.size() );
        }else{
            holder.llReferenceLayout.setVisibility(View.GONE);
        }


        if (strPageNo != null && strPageNo.length() > 0) {
            strPageNo = "" + "Page - " + strPageNo;
            holder.btnPAgeNo.setText(strPageNo);
        }
        if (strAutherName != null && strAutherName.length() > 0) {
            if (!strAutherName.equalsIgnoreCase("0")) {
                strAutherName = ", " + strAutherName /*+ "&#91;" + "ले" + "&#93; "*/;
                strFinalWord = strBookname + strAutherName;
            }//strFinalWord.concat("," + strAutherName + "&#91;" + "ले" + "&#93; ");
        } else {
            strAutherName = "";
        }
        if (strPublisherName != null && strPublisherName.length() > 0) {
            if (!strPublisherName.equalsIgnoreCase("0")) {
                strPublisherName = ", " + strPublisherName /*+ "&#91;" + "प्रका" + "&#93;"*/;
                strFinalWord = strBookname + strAutherName + strPublisherName;
            }
        } else {
            strPublisherName = "";
        }
        /*if (strFinalWord != null && strFinalWord.length() > 0) {
            holder.tvDetails.setText(Html.fromHtml(strFinalWord));
        }*/
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearClickListener.onYearClick(yearlist.get(position), position);
            }
        });
        String strbookname = yearlist.get(position).getBook_name();
        Log.e("strbookname", strbookname);
        Log.e("strbookname", strbookname);


        holder.btnPAgeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearClickListener.onYearClick(yearlist.get(position), position);
            }
        });
        String strShlokName = yearlist.get(position).getBook_name();
        //  String strBookCount = yearlist.get(position).getBook_count();
        holder.textViewName.setText(strShlokName);

        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearClickListener.onYearClick(yearlist.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return yearlist.size();
    }

    public interface YearClickListener {
        public void onYearClick(BookListResModel.BookDetailsModel wordModel, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, tvReferCount;
        CardView cvList;
        Button btnPAgeNo;
        LinearLayout llReferenceLayout;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.tvNotes);
            tvReferCount = (TextView) itemView.findViewById(R.id.tvReferCount);
            cvList = itemView.findViewById(R.id.cvList);
            btnPAgeNo = itemView.findViewById(R.id.btnPageNo);
            llReferenceLayout = (LinearLayout) itemView.findViewById(R.id.llReferenceLayout);
        }
    }
}

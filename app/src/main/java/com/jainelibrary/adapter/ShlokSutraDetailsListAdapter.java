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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.multicheck.Book;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShlokSutraDetailsListAdapter extends RecyclerView.Adapter<ShlokSutraDetailsListAdapter.ViewHolder> {

    Context mContext;
    SearchInterfaceListener searchInterfaceListener;
    ArrayList<BookListResModel.BookDetailsModel> granthList = new ArrayList<>();
    String keywordName, strSutraName, strKeywordId;
    ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookPageModels = new ArrayList<>();
    public static int BARRAY = 100;

    public ShlokSutraDetailsListAdapter(Context mContext, ArrayList<BookListResModel.BookDetailsModel> granthList, SearchInterfaceListener searchInterfaceListener, String strKeywordName, String strSutraName, String strShlokGranthId) {
        this.mContext = mContext;
        this.granthList = granthList;
        this.searchInterfaceListener = searchInterfaceListener;
        this.keywordName = strKeywordName;
        this.strSutraName = strSutraName;
        this.strKeywordId = strShlokGranthId;
    }

    @Override
    public ShlokSutraDetailsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_shlok_sutra_details, parent, false);
        return new ShlokSutraDetailsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShlokSutraDetailsListAdapter.ViewHolder holder, final int position) {

        //granthList.get(position).setKeywordId(strKeywordId);
        granthList.get(position).setKeywordId("1");
        granthList.get(position).setKeyword(keywordName);
        granthList.get(position).setStrSutraName(strSutraName);
        granthList.get(position).setFlag(Utils.TYPE_SHLOK_PAGE);
        bookPageModels = granthList.get(position).getPageModels();
        String strBookname = granthList.get(position).getBook_name();

        String strAutherName = granthList.get(position).getAuthor_name();
        String strPublisherName = granthList.get(position).getPublisher_name();
        String strBookImage = granthList.get(position).getBook_image();



        if (strBookImage != null && strBookImage.length() > 0) {
            Picasso.get().load(strBookImage).into(holder.ivBook);
            holder.ivBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    searchInterfaceListener.onZoomClick(granthList.get(position).getBook_large_image(), granthList.get(position).getBook_image());
                }
            });

        } else {
            holder.ivBook.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.light_background)));
        }

        String strFinalWord = null;

        if (strBookname != null && strBookname.length() > 0) {
            strFinalWord = strBookname;
        } else {
            strBookname = "";
        }



        if (strAutherName != null && strAutherName.length() > 0) {
            if (!strAutherName.equalsIgnoreCase("0")) {
                strAutherName = ", " + strAutherName/* + "&#91;" + "ले" + "&#93; "*/;
                strFinalWord = strBookname + strAutherName;
            }
        } else {
            strAutherName = "";
        }

        if (bookPageModels != null && bookPageModels.size()>0){
            holder.llReferenceLayout.setVisibility(View.VISIBLE);
            holder.tvReferCount.setText( ""+bookPageModels.size() );
        }else{
            holder.llReferenceLayout.setVisibility(View.GONE);
        }

        if (strPublisherName != null && strPublisherName.length() > 0) {
            if(!strPublisherName.equalsIgnoreCase("0"))
            {
            strPublisherName = ", " + strPublisherName/* + "&#91;" + "प्रका" + "&#93;"*/;
            strFinalWord = strBookname + strAutherName + strPublisherName;}
        } else {
            strPublisherName = "";
        }

        if (strFinalWord != null && strFinalWord.length() > 0) {
            holder.tvDetails.setText(Html.fromHtml(strFinalWord));
        }

        /*holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(view, granthList.get(position), position);
            }
        });*/


        View.OnClickListener btnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(view, granthList.get(position), position);
            }
        };

        holder.llPageButton.removeAllViews();

        for (int i = 0; i < bookPageModels.size(); i++) {
            Log.e(bookPageModels + "", bookPageModels.size() + "");
            //bookList.get(position).setFlag(Utils.TYPE_KEYWORD_PAGE);
            Button btn = new Button(mContext);
            btn.setId(i + 1);
            //btn.setBackgroundColor(mContext.getResources().getColor(R.color.Blue));
            btn.setBackgroundResource(R.drawable.bg_page_button);
            btn.setTextColor(mContext.getResources().getColor(R.color.colorWhite_));
            btn.setText("Page No. " + bookPageModels.get(i).getPage_no());
            btn.setAllCaps(false);
            btn.setTextSize(13F);
            btn.setPadding(0, 0, 0, 0);
            btn.setOnClickListener(btnclick);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 80);
            params.setMargins(20, 30, 10, 20);
            btn.setLayoutParams(params);
            String tag = bookPageModels.get(i).getPdf_page_no() + "," +
                    "" + granthList.get(position).getBook_id() + "," +
                    bookPageModels.get(i).getPage_no();
            Log.e("TAG", tag);
            btn.setTag(tag);
            holder.llPageButton.addView(btn);
            BARRAY++;
        }

    }

    public interface SearchInterfaceListener {
        void onDetailsClick(View view, BookListResModel.BookDetailsModel grathModel, int position);
        void onZoomClick(String image, String fallbackImage);
    }

    @Override
    public int getItemCount() {
        return granthList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDetails, tvReferCount;
        CardView cvList;
        ImageView ivBook;
        LinearLayout llReferenceLayout, llPageButton;

        ViewHolder(View itemView) {
            super(itemView);
            tvDetails = (TextView) itemView.findViewById(R.id.textViewName);
            tvReferCount = (TextView) itemView.findViewById(R.id.tvReferCount);
            cvList = itemView.findViewById(R.id.cvList);
            llPageButton = itemView.findViewById(R.id.llPageButton);
            ivBook = itemView.findViewById(R.id.ivBook);
            llReferenceLayout = (LinearLayout) itemView.findViewById(R.id.llReferenceLayout);
        }
    }
}

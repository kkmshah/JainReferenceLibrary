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
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.Utils;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class KeywordSearchListAdapter extends RecyclerView.Adapter<KeywordSearchListAdapter.ViewHolder> {
    Context mContext;
    ArrayList<BookListResModel.BookDetailsModel> mBookList = new ArrayList<>();
    SearchInterfaceListener searchInterfaceListener;
    ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookPageModels = new ArrayList<>();
    public static int BARRAY = 100;

    public KeywordSearchListAdapter(Context mContext,
                                    ArrayList<BookListResModel.BookDetailsModel> mBookList,
                                    SearchInterfaceListener searchInterfaceListener) {
                this.mContext = mContext;
             this.mBookList = mBookList;
             this.searchInterfaceListener = searchInterfaceListener;
    }
    @Override
    public KeywordSearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_list, parent, false);
        return new KeywordSearchListAdapter.ViewHolder(v);
    }


    public interface SearchInterfaceListener {
        void onDetailsClick(View view, BookListResModel.BookDetailsModel mKeywordModel, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String strPublisherName = mBookList.get(position).getPublisher_name();
        String strBookname = mBookList.get(position).getBook_name();
        bookPageModels = mBookList.get(position).getPageModels();
        String strPageNo = mBookList.get(position).getPage_no();
        String strPdfPageNo = mBookList.get(position).getPdf_page_no();
        String strAutherName = mBookList.get(position).getAuthor_name();
        String strBookUrl = mBookList.get(position).getBook_url();

        if (strPdfPageNo == null || strPdfPageNo.length() == 0) {
            mBookList.get(position).setPdf_page_no(strPageNo);
        }
        String strFinalWord = null;

        if (strBookname != null && strBookname.length() > 0) {
            if (!strBookname.equalsIgnoreCase("0")) {
                strFinalWord = strBookname;
            }
        } else {
            strBookname = "";
        }

        if (strPageNo != null && strPageNo.length() > 0) {
            strPageNo = "" + "Page No. " + strPageNo;
            holder.btnPageNo.setText(strPageNo);
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
        if (strFinalWord != null && strFinalWord.length() > 0) {
            holder.tvDetails.setText(Html.fromHtml(strFinalWord));
        }
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //searchInterfaceListener.onDetailsClick(mBookList.get(position), position);
            }
        });

        View.OnClickListener btnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(view, mBookList.get(position), position);


            }
        };
        Picasso.get().load(strBookUrl).error(R.drawable.noimage).into(holder.ivBook);
        if (strFinalWord != null && strFinalWord.length() > 0) {
            holder.tvDetails.setText(Html.fromHtml(strFinalWord));
        } else {
            holder.ivBook.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.light_background)));
        }

        Log.e("bookpagemodel", bookPageModels.size() + "");

        if (bookPageModels != null && bookPageModels.size()>0){
            holder.llReferenceLayout.setVisibility(View.VISIBLE);
            holder.tvReferCount.setText( ""+bookPageModels.size() );
        }else{
            holder.llReferenceLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < bookPageModels.size(); i++) {
            Log.e(bookPageModels + "", bookPageModels.size() + "");
            mBookList.get(position).setFlag(Utils.TYPE_KEYWORD_PAGE);
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
                    "" + mBookList.get(position).getBook_id() + "," +
                    mBookList.get(position).getKeywordId() + "," +
                    bookPageModels.get(i).getPage_no();
            Log.e("TAG", tag);
            btn.setTag(tag);
            holder.ll.addView(btn);
            BARRAY++;
        }
        holder.btnPageNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvReferCount,tvDetails;
        CardView cvList;
        Button btnPageNo;
        ImageView ivBook;
        LinearLayout ll, llReferenceLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);
            tvReferCount = (TextView) itemView.findViewById(R.id.tvReferCount);
            cvList = itemView.findViewById(R.id.cvList);
            btnPageNo = itemView.findViewById(R.id.btnPageNo);
            ivBook = itemView.findViewById(R.id.ivBook);
            ll = (LinearLayout) itemView.findViewById(R.id.buttonlayout);
            llReferenceLayout = (LinearLayout) itemView.findViewById(R.id.llReferenceLayout);
        }
    }
}

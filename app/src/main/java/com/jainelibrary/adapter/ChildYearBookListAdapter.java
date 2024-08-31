package com.jainelibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChildYearBookListAdapter extends RecyclerView.Adapter<ChildYearBookListAdapter.ViewHolder> {
    private static final String TAG = ChildYearBookListAdapter.class.getSimpleName();
    Context mContext;//  ArrayList<BooksDataModel> mBookList = new ArrayList<>();
    //ArrayList<YearBookResModel.YearBookList> yearBookLists = new ArrayList<>();
    ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookList = new ArrayList<>();
    //ArrayList<String> bookList = new ArrayList<>();
    BookClickListener listener;
    ArrayList<BookListResModel.BookDetailsModel.BookPageModel.Book> bookPageModels = new ArrayList<>();
    public static int BARRAY = 100;

    /*public YearBookListAdapter(Context mContext, ArrayList<KeywordSearchModel.KeywordModel> keywordList, SearchInterfaceListener searchInterfaceListener, ArrayList<SearchHistoryModel> models) {
        this.mContext = mContext;
        this.keywordList = keywordList;
        this.searchInterfaceListener = searchInterfaceListener;
        this.searchHistoryModelArrayList = models;//   this.mBookList = mBookList;
    }*/

    /*public ChildYearBookListAdapter(Context mContext, ArrayList<YearBookResModel.YearBookList> yearBookLists, SearchInterfaceListener searchInterfaceListener, ArrayList<YearBookResModel.YearBookList.YearBook> mBookModel) {
        this.mContext = mContext;
        this.yearBookLists = yearBookLists;
        this.searchInterfaceListener = searchInterfaceListener;
        this.bookList = mBookModel;
    }*/

    public ChildYearBookListAdapter( Context context, ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookList, BookClickListener listener) {
        this.mContext = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    public interface BookClickListener {
        //void onBookClick(ArrayList<BookListResModel.BookDetailsModel.BookPageModel> mBookModel, int position);

        void onBookClick(View view, BookListResModel.BookDetailsModel.BookPageModel bookPageModel, int position);
        void onBookImageClick(View view, BookListResModel.BookDetailsModel.BookPageModel bookPageModel, int position);
    }

    @NonNull
    @Override
    public ChildYearBookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_child_year_book_list, parent, false);
        return new  ChildYearBookListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") final int position) {

        //YearBookResModel.YearBookList.YearBook model = bookList.get(position);

        String strBookName = bookList.get(position).getBook_name();
        //String strPageNo = bookList.get(position).getPage_no();
        String strBookImage = bookList.get(position).getBook_image();
        String strAuthorName = bookList.get(position).getAuthor_name();
        String strPublisher = bookList.get(position).getPublisher_name();
        bookPageModels = bookList.get(position).getPage_nos();

        Log.e(TAG, "BookName in list --" + strBookName);
        holder.textViewName.setTextColor(mContext.getResources().getColor(R.color.gray));
        holder.textViewName.setText(strBookName + ", " + strAuthorName + ", " + strPublisher);
        //holder.btnPageNo.setText("Page No. "+strPageNo);

        if (strBookImage != null && strBookImage.length() > 0) {
            Picasso.get().load(strBookImage).into(holder.ivBook);
            holder.ivBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onBookImageClick(view, bookList.get(position), position);
                }
            });
        } else {
            holder.ivBook.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.light_background)));
        }

        if (bookPageModels != null && bookPageModels.size()>0){
            holder.llReferenceLayout.setVisibility(View.VISIBLE);
            holder.tvReferCount.setText( ""+bookPageModels.size() );

            /*for(int i = 0; i< bookPageModels.size(); i++) {
                String strPageNo = bookPageModels.get(i).getPage_no();
                if (strPageNo != null && strPageNo.length() > 0) {
                    strPageNo = "" + "Page - " + strPageNo;
                    holder.btnPageNo.setText(strPageNo);
                }
            }*/

        }else{
            holder.llReferenceLayout.setVisibility(View.GONE);
        }

        /*if (bookList != null && bookList.size() > 0) {
            for (int i=0; i<bookList.size(); i++){
                String strBookName = bookList.get(i).getBook_name();
                Log.e(TAG, "BookName in list --" + strBookName);
                holder.textViewName.setTextColor(mContext.getResources().getColor(R.color.gray));
                holder.textViewName.setText(strBookName);
            }

        }*/


        View.OnClickListener btnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onDetailsClick --" + position);
                listener.onBookClick(view, bookList.get(position), position);

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
                    "" + bookList.get(position).getBook_id() + "," +
                    bookPageModels.get(i).getPage_no();
            Log.e("TAG", tag);
            btn.setTag(tag);
            holder.llPageButton.addView(btn);
            BARRAY++;
        }


    }


    @Override
    public int getItemCount() {
        Log.e(TAG, "bookList.size --" + bookList.size());
        return bookList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName,tvSimilarKeyword,tvExactKeyword,tvReference, tvReferCount;
        CardView cvList;
        LinearLayout llCounts,llDetails;
        TextView tvDetails;
        ImageView ivBook;
        LinearLayout llReferenceLayout, llPageButton;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            llCounts = itemView.findViewById(R.id.llCounts);
            tvReference = (TextView) itemView.findViewById(R.id.tvReference);
            cvList = itemView.findViewById(R.id.cvList);
            llDetails = itemView.findViewById(R.id.llDetails);
            tvDetails = (TextView) itemView.findViewById(R.id.textViewName);
            ivBook = itemView.findViewById(R.id.ivBook);
            tvReferCount = (TextView) itemView.findViewById(R.id.tvReferCount);
            llReferenceLayout = (LinearLayout) itemView.findViewById(R.id.llReferenceLayout);
            llPageButton = (LinearLayout) itemView.findViewById(R.id.llPageButton);
        }
    }
}

package com.jainelibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

public class YearBookListAdapter extends RecyclerView.Adapter<YearBookListAdapter.ViewHolder> implements ChildYearBookListAdapter.BookClickListener{
    private static final String TAG = YearBookListAdapter.class.getSimpleName();
    Context mContext;//  ArrayList<BooksDataModel> mBookList = new ArrayList<>();
    BookClickListener listener;
    ArrayList<BookListResModel.BookDetailsModel> yearBookLists = new ArrayList<>();
    ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookList = new ArrayList<>();
    String childPosition;

    public YearBookListAdapter(Context mContext, ArrayList<BookListResModel.BookDetailsModel> yearBookLists, BookClickListener listener, ArrayList<BookListResModel.BookDetailsModel.BookPageModel> mBookModel) {
        this.mContext = mContext;
        this.yearBookLists = yearBookLists;
        this.listener = listener;
        this.bookList = mBookModel;
    }

    @NonNull
    @Override
    public YearBookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_book_list_layout, parent, false);
        return new YearBookListAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ///  searchHistoryModelArrayList = SharedPrefManager.getInstance(mContext).getSearchkeywordHistory(SharedPrefManager.KEY_KEYWORD_HISTORY);
        String strKeyWordName = yearBookLists.get(position).getName();
        holder.tvExactKeyword.setBackgroundColor(mContext.getResources().getColor(R.color.colorbuttonpresssss));
        holder.tvExactKeyword.setText(strKeyWordName);
        yearBookLists.get(position).setFlag(Utils.TYPE_YEAR_PAGE);
        bookList = yearBookLists.get(position).getBooks();
        Log.e(TAG, "BookName in list --" + bookList.size());

        /*if (bookList != null && bookList.size() > 0) {
            for (int i=0; i<bookList.size(); i++){
                String strBookName = bookList.get(i).getBook_name();
                //books.add(bookList.get(i).getBook_name());
                Log.e(TAG, "BookName in list --" + strBookName);
               *//* holder.textViewName.setTextColor(mContext.getResources().getColor(R.color.gray));
                holder.textViewName.setText(strBookName);*//*
            }
        }*/



        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.rvBooks.setLayoutManager(layoutManager);
        ChildYearBookListAdapter childRecyclerViewAdapter = new ChildYearBookListAdapter(mContext, bookList, this);
        holder.rvBooks.setAdapter(childRecyclerViewAdapter);

        holder.llBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Log.e(TAG, "onDetailsClick --" + yearBookLists.size());
                listener.onClick(yearBookLists, position, childPosition);*/
            }
        });

    }


    @Override
    public int getItemCount() {
        return yearBookLists.size();
    }

    @Override
    public void onBookClick(View view, BookListResModel.BookDetailsModel.BookPageModel mBookModel, int position) {
        Log.e(TAG, "onBookClick --" + yearBookLists.size());
        listener.onClick(view, mBookModel);
    }


    public interface BookClickListener {
        void onClick(View view, BookListResModel.BookDetailsModel.BookPageModel yearBookLists);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExactKeyword;
        RecyclerView rvBooks;
        LinearLayout llBook;

        public ViewHolder(View itemView) {
            super(itemView);


            llBook = itemView.findViewById(R.id.llBook);
            tvExactKeyword = itemView.findViewById(R.id.tvExactKeyword);
            rvBooks  = itemView.findViewById(R.id.rvBooks);
        }
    }
}

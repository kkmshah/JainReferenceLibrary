package com.jainelibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.utils.Utils;

import java.util.ArrayList;

import static com.jainelibrary.utils.Utils.TYPE_INDEX_PAGE;

public class IndexSearchDetailsListAdapter extends RecyclerView.Adapter<IndexSearchDetailsListAdapter.ViewHolder> {

    Context mContext;
    SearchInterfaceListener searchInterfaceListener;
    ArrayList<BookListResModel.BookDetailsModel> wordList;
    String strIndexWordName, strPageNo;
    String strIndexId;
    ArrayList<BookListResModel.BookDetailsModel.BookPageModel> bookPageModels = new ArrayList<>();
    public static int BARRAY = 100;

    public IndexSearchDetailsListAdapter(Context mContext, ArrayList<BookListResModel.BookDetailsModel> wordList, SearchInterfaceListener searchInterfaceListener, String strIndexWordName, String strIndexId) {
        this.mContext = mContext;
        this.wordList = wordList;
        this.searchInterfaceListener = searchInterfaceListener;
        this.strIndexWordName = strIndexWordName;
        this.strIndexId = strIndexId;
    }

    @Override
    public IndexSearchDetailsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_index_search_details, parent, false);
        return new IndexSearchDetailsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IndexSearchDetailsListAdapter.ViewHolder holder, final int position) {

        String strIndexname = wordList.get(position).getName();
        bookPageModels = wordList.get(position).getPageModels();


        if (strIndexname != null && strIndexname.length() > 0) {
            holder.textViewName.setText(strIndexname);
        } else {
            strIndexname = "";
        }


        /*holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(view, wordList.get(position), position);
            }
        });*/


        View.OnClickListener btnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(view, wordList.get(position), bookPageModels.get(0).getPage_no(), position);
            }
        };

        holder.llPageButton.removeAllViews();

        for (int i = 0; i < bookPageModels.size(); i++) {
            Log.e(bookPageModels + "", bookPageModels.size() + "");
            wordList.get(position).setFlag(Utils.TYPE_INDEX_PAGE);
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
            params.setMargins(20, 5, 10, 5);
            btn.setLayoutParams(params);
            String tag = bookPageModels.get(i).getPdf_page_no() + "," +
                    "" + bookPageModels.get(i).getPage_no();
            Log.e("TAG", tag);
            btn.setTag(tag);
            holder.llPageButton.addView(btn);
            //BARRAY++;
        }

    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public interface SearchInterfaceListener {
        void onDetailsClick(View view, BookListResModel.BookDetailsModel wordModel, String page_no, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        CardView cvList;
        LinearLayout llReferenceLayout, llPageButton;

        ViewHolder(View itemView) {
            super(itemView);
            llPageButton = (LinearLayout) itemView.findViewById(R.id.llPageButton);
            textViewName = (TextView) itemView.findViewById(R.id.tvBookName);
            cvList = itemView.findViewById(R.id.cvList);
            llReferenceLayout = (LinearLayout) itemView.findViewById(R.id.llReferenceLayout);
        }
    }
}

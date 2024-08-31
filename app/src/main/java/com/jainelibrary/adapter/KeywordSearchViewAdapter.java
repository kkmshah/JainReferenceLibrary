package com.jainelibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.extraModel.SearchHistoryModel;
import com.jainelibrary.retrofitResModel.KeywordSearchModel;

import java.util.ArrayList;

public class KeywordSearchViewAdapter extends RecyclerView.Adapter<KeywordSearchViewAdapter.ViewHolder> {
    private static final String TAG = KeywordSearchViewAdapter.class.getSimpleName();
    Context mContext;//  ArrayList<BooksDataModel> mBookList = new ArrayList<>();
    SearchInterfaceListener searchInterfaceListener;
    ArrayList<SearchHistoryModel> searchHistoryModelArrayList = new ArrayList<>();
    ArrayList<KeywordSearchModel.KeywordModel> keywordList = new ArrayList<>();

    public KeywordSearchViewAdapter(Context mContext, ArrayList<KeywordSearchModel.KeywordModel> keywordList, SearchInterfaceListener searchInterfaceListener, ArrayList<SearchHistoryModel> models) {
        this.mContext = mContext;
        this.keywordList = keywordList;
        this.searchInterfaceListener = searchInterfaceListener;
        this.searchHistoryModelArrayList = models;//   this.mBookList = mBookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ///  searchHistoryModelArrayList = SharedPrefManager.getInstance(mContext).getSearchkeywordHistory(SharedPrefManager.KEY_KEYWORD_HISTORY);
        String strKeyWordName = keywordList.get(position).getName();
        boolean isExact = keywordList.get(position).isExact();
        boolean isSimilar = keywordList.get(position).isSimmilar();
        boolean isLoader = keywordList.get(position).isLoader();
        String totalSizeText = keywordList.get(position).getStrTotalSizeText();
        if (isExact || isSimilar){
//            if (isExact){
//                holder.tvExactKeyword.setBackgroundColor(mContext.getResources().getColor(R.color.button_color));
//            }else{
//                holder.tvExactKeyword.setTextColor(mContext.getResources().getColor(R.color.black));
//                holder.tvExactKeyword.setBackgroundColor(mContext.getResources().getColor(R.color.background_color));
//            }
//            holder.tvExactKeyword.setVisibility(View.VISIBLE);
//            holder.tvExactKeyword.setText(totalSizeText);
        } else if(isLoader) {
            holder.cvList.setVisibility(View.GONE);
            holder.tvExactKeyword.setVisibility(View.VISIBLE);
            holder.tvExactKeyword.setText(totalSizeText);
            holder.tvExactKeyword.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tvExactKeyword.setBackgroundColor(mContext.getResources().getColor(R.color.background_color));
        } else{
            holder.tvExactKeyword.setVisibility(View.GONE);
        }

        String strTotalBookKeywordCount = keywordList.get(position).getKeyword_cnt();
        if (strTotalBookKeywordCount != null && strTotalBookKeywordCount.length() > 0) {
            holder.llCounts.setVisibility(View.VISIBLE);
            holder.tvReference.setText(strTotalBookKeywordCount);
        } else {
            holder.llCounts.setVisibility(View.INVISIBLE);
        }

        holder.textViewName.setTextColor(mContext.getResources().getColor(R.color.gray));
        holder.textViewName.setText(strKeyWordName);

        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onDetailsClick --" + keywordList.size());
                searchInterfaceListener.onDetailsClick(keywordList.get(position), position);
            }
        });

        if (strKeyWordName != null && strKeyWordName.length() > 0) {
            if (searchHistoryModelArrayList != null && searchHistoryModelArrayList.size() > 0) {
                for (int i = 0; i < searchHistoryModelArrayList.size(); i++) {
                    if (strKeyWordName.equals(searchHistoryModelArrayList.get(i).getStrSearchKeywordName())) {
                        String text_view_str = "<b>" + strKeyWordName + "</b>";
                        SpannableString spannablecontent = new SpannableString(strKeyWordName);
                        spannablecontent.setSpan(new StyleSpan(Typeface.BOLD),
                                0, spannablecontent.length(), 0);
                        holder.textViewName.setTextColor(Color.BLACK);
                        holder.textViewName.setText(spannablecontent);
                    }
                }
            }
        }

    }

    public interface SearchInterfaceListener {
        void onDetailsClick(KeywordSearchModel.KeywordModel mKeywordModel, int position);
    }

    public void newData(ArrayList<KeywordSearchModel.KeywordModel> list) {
        Log.e(TAG, "set list --" + list.size());
        if (keywordList != null) {
            keywordList = new ArrayList<>();
            keywordList = list;
            Log.e(TAG, "keywordList in list --" + keywordList.size());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName,tvSimilarKeyword,tvExactKeyword,tvReference;
        CardView cvList;
        LinearLayout llCounts,llDetails;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            llCounts = itemView.findViewById(R.id.llCounts);
            tvReference = (TextView) itemView.findViewById(R.id.tvReference);
            cvList = itemView.findViewById(R.id.cvList);
            llDetails = itemView.findViewById(R.id.llDetails);
            tvExactKeyword = itemView.findViewById(R.id.tvExactKeyword);
            tvSimilarKeyword = itemView.findViewById(R.id.tvSimilarKeyword);
        }
    }
}

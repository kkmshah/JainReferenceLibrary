package com.jainelibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HoldAndSearchAdapter extends RecyclerView.Adapter<HoldAndSearchAdapter.ViewHolder> {
    Context mContext;
    ArrayList<BookListResModel.BookDetailsModel> mBookList = new ArrayList<>();
    SearchInterfaceListener searchInterfaceListener;

    public HoldAndSearchAdapter(Context mContext, ArrayList<BookListResModel.BookDetailsModel> mBookList, SearchInterfaceListener searchInterfaceListener) {
        this.mContext = mContext;
        this.mBookList = mBookList;
        this.searchInterfaceListener = searchInterfaceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_hold_search_list, parent, false);
        return new HoldAndSearchAdapter.ViewHolder(v);
    }

    public interface SearchInterfaceListener {
        void onDetailsClick(BookListResModel.BookDetailsModel mBookDataModel, ArrayList<BookListResModel.BookDetailsModel> mBookList, int position);

        void onMenuClick(BookListResModel.BookDetailsModel bookDetailsModel, int position, ImageView ivMenu);

        void onCancelClick(ArrayList<BookListResModel.BookDetailsModel> mBookList, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String strKeyword = mBookList.get(position).getKeyword();
        String strBookname = mBookList.get(position).getBook_name();
        String strPageNo = mBookList.get(position).getPage_no();
        String strAutherName = mBookList.get(position).getAuthor_name();
        String strTranslator = mBookList.get(position).getTranslator();
        String strEditorName = mBookList.get(position).getEditor_name();
        String strPublishername = mBookList.get(position).getPublisher_name();
        String strBookURl = mBookList.get(position).getBook_Url();
        String strTypeName = mBookList.get(position).getType_name();
        String strFinalWord = null;

        if (strPublishername != null && strPublishername.length() > 0) {

            holder.tvPublishername.setText(strPublishername);
        } else {
            strPublishername = "";
        }


        if (strBookname != null && strBookname.length() > 0) {
            strFinalWord = strBookname;
            holder.tvBookname.setText(strPageNo + ", " + strBookname);
        } else {
            strBookname = "";
        }

        if (strTypeName != null && strTypeName.length() > 0) {
            holder.tvTypeName.setText(strTypeName);
        } else {
            strTypeName = "";
        }

//        if (strPageNo != null && strPageNo.length() > 0) {
//            strPageNo = ", " + "P. " + strPageNo;
//
//          /*  if (strPdfPageNo != null && strPdfPageNo.length() > 0) {
//                strPageNo = ", " + "( page no. " + strPageNo + " ," + " Pdf page no " + strPdfPageNo + " )";
//            } else {
//                strPageNo = ", " + "( page no. " + strPageNo + " )";
//            }*/
//            strFinalWord = strBookname + strPageNo;
//            //strFinalWord.concat("," + " P." + strPageNo);
//        } else {
//            strPageNo = "";
//        }

        if (strAutherName != null && strAutherName.length() > 0) {
            if (strAutherName.equalsIgnoreCase("0")) {
            } else {

                holder.tvOthername.setText(strAutherName);
//                strAutherName = ", " + strAutherName /*+ "&#91;" + "ले" + "&#93; "*/;
//                strFinalWord = strBookname + strPageNo + strAutherName;
            }
            //strFinalWord.concat("," + strAutherName + "&#91;" + "ले" + "&#93; ");
        } else {
            strAutherName = "";
        }

//        if (strTranslator != null && strTranslator.length() > 0) {
//            if (strTranslator.equalsIgnoreCase("0")) {
//
//            } else {
//                strTranslator = ", " + strTranslator /*+ "&#91;" + "अनु" + "&#93; "*/;
//                strFinalWord = strBookname + strPageNo + strAutherName + strTranslator;
//            }
//            //strFinalWord.concat("," + strTranslator + "&#91;" + "अनु" + "&#93; ");
//        } else {
//            strTranslator = "";
//        }
//
//        if (strEditorName != null && strEditorName.length() > 0) {
//            if (strEditorName.equalsIgnoreCase("0")) {
//
//            } else {
//                strEditorName = ", " + strEditorName/* + "&#91;" + "संपा" + "&#93;"*/;
//                strFinalWord = strBookname + strPageNo + strAutherName + strTranslator + strEditorName;
//            }
//            //    strFinalWord.concat("," + strEditorName + "&#91;" + "संपा" + "&#93;");
//        } else {
//            strEditorName = "";
//        }
//
//        Log.e("strKeyword---", "strKeyword--" + strKeyword);
        //   holder.tvKeywordHoldReference.setText(strKeyword);

        int pos = mBookList.size() - position;
        //  holder.tvHoldNo.setText("" + pos);
//


        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // searchInterfaceListener.onDetailsClick(mBookList.get(position), mBookList, position);
                searchInterfaceListener.onMenuClick(mBookList.get(position), position, holder.ivMenu);
                //   void onMenuClick(BookListResModel.BookDetailsModel, int position, ImageView ivMenu);
            }
        });
        holder.linear_hold_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  searchInterfaceListener.onDetailsClick(mBookList.get(position), mBookList, position);
            }
        });


        if (strBookURl != null && strBookURl.length() > 0) {
            Picasso.get().load(strBookURl).placeholder(R.drawable.book).into(holder.ivImage);
        }

        /*holder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onCancelClick(mBookList, position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAllDetails, tvKeywordHoldReference;
        LinearLayout linear_hold_search, ivClose;
        TextView tvHoldNo;
        ImageView ivImage, ivMenu;
        TextView tvOthername, tvPublishername, tvBookname, tvTypeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            /*tvKeywordHoldReference = (TextView) itemView.findViewById(R.id.tvKeywordHoldReference);
            tvAllDetails = (TextView) itemView.findViewById(R.id.tvAllDetails);
            ivClose = itemView.findViewById(R.id.ivClose);
            tvHoldNo = itemView.findViewById(R.id.tvHoldNo);*/
            linear_hold_search = itemView.findViewById(R.id.linear_hold_search);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvOthername = itemView.findViewById(R.id.tvOtherName);
            tvPublishername = itemView.findViewById(R.id.tvPublisherName);
            tvBookname = itemView.findViewById(R.id.tvBookName);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvTypeName = itemView.findViewById(R.id.tvTypeName);
        }
    }

    public void setData() {
        notifyDataSetChanged();
    }
}

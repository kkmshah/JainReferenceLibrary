package com.jainelibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class MyReferenceFilesListAdapter extends RecyclerView.Adapter<MyReferenceFilesListAdapter.ViewHolder> {

    Context context;
    ArrayList<MyShelfResModel.MyShelfModel> mImageList = new ArrayList<>();
    MyShelfInterFaceListener myShelfInterFaceListener;
    private int selectedPosition = -1;

    public MyReferenceFilesListAdapter(Context context, ArrayList<MyShelfResModel.MyShelfModel> mImageList, MyShelfInterFaceListener myShelfInterFaceListener) {
        this.context = context;
        this.mImageList = mImageList;
        this.myShelfInterFaceListener = myShelfInterFaceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_my_shelf, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String strBookName = mImageList.get(position).getBook_name();
        String strKeywordName = mImageList.get(position).getType_name();
        String strNotes = mImageList.get(position).getNotes();
        String strNotesDate = mImageList.get(position).getNote_updated_date();
        String strDate = mImageList.get(position).getUpdated_date();
        String strAutherName = mImageList.get(position).getAuthor_name();
        String strPublisherName = mImageList.get(position).getPrakashak_name();
        String isNotesAdded = mImageList.get(position).getIs_note_added();


        String strCDate = mImageList.get(position).getcDate();

        /*if (strCDate != null && strCDate.length() > 0) {
            *//*String[] separated = strCDate.split(" ");
            String date = separated[0];
            String notes = separated[1];
            Log.e("date ---->>",""+separated[0]);
            Log.e("notes ---->>",""+separated[1]);*//*
            holder.tvCDate.setVisibility(View.VISIBLE);
            holder.tvCDate.setText("" + strCDate);
            *//*holder.tvNotes.setVisibility(View.VISIBLE);
            holder.tvNotes.setText(notes);*//*
        } else {
            holder.tvCDate.setVisibility(View.GONE);
        }*/

        if (strNotes != null && strNotes.length() > 0) {
            holder.tvNotes.setVisibility(View.VISIBLE);
            holder.tvNotes.setText(strNotes);
        } else {
            holder.tvNotes.setVisibility(View.GONE);
        }

        /*Log.e("isNotesAdded ---->>",""+isNotesAdded);

        if(isNotesAdded.equals("1")){
            holder.tvNotes.setVisibility(View.VISIBLE);
            holder.tvNotes.setText("Notes added");
        } else {
            holder.tvNotes.setVisibility(View.GONE);
        }*/
Log.e("tegs", mImageList.get(position).getBook_name()+
        " "+mImageList.get(position).getBook_name()+
        " "+mImageList.get(position).getType_name()+
        " "+mImageList.get(position).getNotes()+
        " "+mImageList.get(position).getAuthor_name()+
        " "+mImageList.get(position).getPrakashak_name()+
        " "+mImageList.get(position).getUpdated_date());
        String strOtherName = "";

        if (strKeywordName != null && strKeywordName.length() > 0) {
            strOtherName = strKeywordName;
            Log.e("othername",strOtherName);
        }

        if (strKeywordName != null && strKeywordName.length() > 0) {
            strOtherName = strKeywordName;
            Log.e("othername",strOtherName);
        }

        if (strAutherName != null && strAutherName.length() > 0) {
            if (strAutherName.equalsIgnoreCase("0")) {

            } else {
                strAutherName = ", " + strAutherName /*+"&#91;" + "ले" + "&#93; "*/;
                strOtherName = strOtherName + strAutherName;
                Log.e("othername",strOtherName);
            }
        }

        if (strPublisherName != null && strPublisherName.length() > 0) {
            if (strPublisherName.equalsIgnoreCase("0")) {

            }else {
                strPublisherName = ", " + strPublisherName /*+ "&#91;" + "प्रका" + "&#93;"*/;
                strOtherName = strOtherName + strPublisherName;
                Log.e("othername",strOtherName);
            }
        }
        holder.tvKeyword.setText(strBookName);

        if (strOtherName != null && strOtherName.length() > 0) {
            holder.tvKeyword.setText(strBookName);
        }

        SimpleDateFormat yyyMMddHHmmssFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US); // 2018-08-31 01:55:22
        SimpleDateFormat ddMMyyyyFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);// 31-08-2020
        SimpleDateFormat hhmmampmFormat = new SimpleDateFormat("hh:mm a", Locale.US); // 01:55 PM

        if (strDate != null && strDate.length() > 0) {
            String strDates = Utils.parseDate(strDate, yyyMMddHHmmssFormat, ddMMyyyyFormat);
            String strTime = Utils.parseDate(strDate, yyyMMddHHmmssFormat, hhmmampmFormat);
            if (strDates != null && strTime != null) {
                strDate = strDates + " • " + strTime;
            }
        }

        String strPdfPageSize = null;
        int pageSize = mImageList.get(position).getPage_no().size();

        if (pageSize != 0 && pageSize > 0) {
            if (pageSize > 1) {
                strPdfPageSize = pageSize + " Pages";
            } else {
                strPdfPageSize = pageSize + " Page";
            }
        }

        String strFinalNotes = null;
        /*if (strNotes != null && strNotes.length() > 0) {
            strFinalNotes = "Notes";
        } else {
            holder.tvNotes.setVisibility(View.GONE);
        }*/

        if (strPdfPageSize != null && strPdfPageSize.length() > 0) {
            strFinalNotes = " • " + strPdfPageSize;
        }

        if (strDate != null && strDate.length() > 0) {
            //strFinalNotes = strFinalNotes + " • " + "Notes" + " • " + strDate;
            strFinalNotes = " • " + strDate;
        }

        /*if (strFinalNotes != null && strFinalNotes.length() > 0) {
            holder.tvNotes.setVisibility(View.VISIBLE);
            holder.tvNotes.setText(strFinalNotes);
        } else {
            holder.tvNotes.setVisibility(View.GONE);
        }*/



        holder.cvKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myShelfInterFaceListener.onDetailsClick(mImageList, position);
            }
        });
        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myShelfInterFaceListener.onMenuClick(mImageList, position,holder.ivMenu);
            }
        });

        holder.tvNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myShelfInterFaceListener.onNotesClick(mImageList, position);
            }
        });

        holder.cvKeyword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
              /*  selectedPosition = holder.getAdapterPosition();
                if (selectedPosition == position) {
                    if (mImageList.get(position).isChecked()) {
                        mImageList.get(position).setChecked(false);
                    } else {
                        mImageList.get(position).setChecked(true);
                    }
                } else {
                    mImageList.get(position).setChecked(false);
                }

                myShelfInterFaceListener.onOptionClick(mImageList, position);
                notifyDataSetChanged();*/
                return false;
            }
        });

        /*if (selectedPosition == position) {
            if (mImageList.get(position).isChecked()) {
                holder.ivSelect.setVisibility(View.VISIBLE);
            } else {
                holder.ivSelect.setVisibility(View.GONE);
            }
        } else {
            holder.ivSelect.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotes, tvKeyword,tvCDate;
        CardView cvKeyword;
        ImageView ivSelect, ivMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKeyword = itemView.findViewById(R.id.tvKeyword);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            cvKeyword = itemView.findViewById(R.id.cvKeyword);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvCDate = itemView.findViewById(R.id.tvCDate);
        }
    }

    public interface MyShelfInterFaceListener {
        void onMenuClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position, ImageView ivMenu);

        void onDetailsClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position);

        void onNotesClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position);

        void onOptionClick(ArrayList<MyShelfResModel.MyShelfModel> searchList, int position);
    }
}

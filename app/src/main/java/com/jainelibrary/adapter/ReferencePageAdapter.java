package com.jainelibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.BooksDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ReferencePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int TYPE_PAGE = 1;
    private static int TYPE_WITHOUT_PAGE = 2;
    private static int TYPE_NUll = 0;
    ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList, mList = new ArrayList<>();
    PdfInterfaceListener mPdfListener;
    private Context context;
    private int adapterPosition = 0;

    public ReferencePageAdapter(Context context, ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList, PdfInterfaceListener mPdfListener) {
        this.context = context;
        this.mPfdList = mPfdList;
        this.mPdfListener = mPdfListener;
        this.mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_PAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.rv_pdf_image, parent, false);
            return new OtheViewPageHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.rv_pdf_image_page, parent, false);
            return new PageViewHolder(view);
        }
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        final BooksDataModel.BookImageDetailsModel mBookDetails = mPfdList.get(position);
        String strImage = null;
        String strByteImageUrl = null;
        strImage = mPfdList.get(position).getPage_image();
        strByteImageUrl = mPfdList.get(position).getPage_bytes();
        Bitmap imageBitmap = null;
        Uri tempImageUri = null;

     //   strByteImageUrl = "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";
        Log.e("ReferencePageAdapter", "imageUrl : " + strImage);
        Log.e("ReferencePageAdapter", "byteImageUrl : " + strByteImageUrl);
        boolean isBytesData = false;


        if (mPfdList.get(position).getType().equalsIgnoreCase("1")) {
            /*  if (getItemViewType(position) == TYPE_PAGE) {*/
            Log.e("type", mPfdList.get(position).getType() + " " + position);
            Log.e("OtheViewPageHolder position", position + "");

            if (strByteImageUrl != null && strByteImageUrl.length() > 0) {
                byte[] imgBytes = Base64.decode(strByteImageUrl, Base64.DEFAULT);
                imageBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                if (imageBitmap != null) {
                    isBytesData = true;
                }
            }

            Log.e("isBytesData", "" + isBytesData);

            if (isBytesData) {
                ((OtheViewPageHolder) holder).ivPdf.setImageBitmap(imageBitmap);
                ((OtheViewPageHolder) holder).llRefer.setBackgroundColor(context.getResources().getColor(R.color.normal_voilet));
            } else if (!isBytesData && strImage != null && strImage.length() > 0) {
                Picasso.get()
                        .load(strImage)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.noimage)
                        .into(((OtheViewPageHolder) holder).ivPdf, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                ((OtheViewPageHolder) holder).llRefer.setBackgroundColor(context.getResources().getColor(R.color.normal_voilet));
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
            }
            //    Picasso.get().load(strImage).placeholder(R.drawable.progress_animation).error(R.drawable.noimage).into(((OtheViewPageHolder) holder).ivPdf);
            ((OtheViewPageHolder) holder).viewLine.setVisibility(View.VISIBLE);
            ((OtheViewPageHolder) holder).llHeader.setVisibility(View.VISIBLE);
            ((OtheViewPageHolder) holder).llFooter.setVisibility(View.VISIBLE);
            if (((OtheViewPageHolder) holder).llHeader.getVisibility() == View.VISIBLE) {
                Log.e("OtheViewPageHolder", "llHeader");
            }
            if (((OtheViewPageHolder) holder).llFooter.getVisibility() == View.VISIBLE) {
                Log.e("OtheViewPageHolder", "llFooter");
            }
            ((OtheViewPageHolder) holder).llpage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mList != null && mList.size() > 0) {
                        if (mList.contains(mBookDetails)) {
                            mList.remove(mBookDetails);
                            //((OtheViewPageHolder) holder).ivSelect.setVisibility(View.GONE);
                            ((OtheViewPageHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.transparent), android.graphics.PorterDuff.Mode.SRC_OVER);
                            //   ((OtheViewPageHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            mPdfListener.onShareImage(mPfdList, position, false);
                        } else {
                            mList.add(mBookDetails);
                            mPdfListener.onShareImage(mPfdList, position, true);
                            // ((OtheViewPageHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                            ((OtheViewPageHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.backcolor), android.graphics.PorterDuff.Mode.SRC_OVER);
                            //  ((OtheViewPageHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, R.color.backcolor));
                        }
                    } else {
                        mPdfListener.onImageClick(mPfdList, position);
                    }
                }
            });
            ((OtheViewPageHolder) holder).llpage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mList.contains(mBookDetails)) {
                        mList.remove(mBookDetails);
                        //((OtheViewPageHolder) holder).ivSelect.setVisibility(View.GONE);
                        ((OtheViewPageHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.transparent), android.graphics.PorterDuff.Mode.SRC_OVER);
                        //((OtheViewPageHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        mPdfListener.onShareImage(mPfdList, position, false);
                    } else {
                        mList.add(mBookDetails);
                        mPdfListener.onShareImage(mPfdList, position, true);
                        // ((OtheViewPageHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                        ((OtheViewPageHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.backcolor), android.graphics.PorterDuff.Mode.SRC_OVER);
                        // ((OtheViewPageHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, R.color.backcolor));
                    }
                    return true;
                }
            });
            if (mList.contains(mBookDetails)) {
                //((OtheViewPageHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                ((OtheViewPageHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.backcolor), android.graphics.PorterDuff.Mode.SRC_OVER);
                //((OtheViewPageHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, R.color.backcolor));
            } else {
                //((OtheViewPageHolder) holder).ivSelect.setVisibility(View.GONE);
                ((OtheViewPageHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.transparent), android.graphics.PorterDuff.Mode.SRC_OVER);
                //((OtheViewPageHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            }
        } else if (mPfdList.get(position).getType().equalsIgnoreCase("0"))/*if (getItemViewType(position) == TYPE_WITHOUT_PAGE) {*/ {
            Log.e("type", mPfdList.get(position).getType() + "");
            adapterPosition = holder.getAdapterPosition();
            if (strImage != null && strImage.length() > 0) {
                Picasso.get().load(strImage).placeholder(R.drawable.progress_animation)
                        .error(R.drawable.noimage).into(((PageViewHolder) holder).ivPdf);


                Log.e("PageViewHolder position", position + " " + position);

                ((PageViewHolder) holder).viewLine.setVisibility(View.VISIBLE);
                ((PageViewHolder) holder).llHeader.setVisibility(View.VISIBLE);
                ((PageViewHolder) holder).llFooter.setVisibility(View.VISIBLE);
                if (((PageViewHolder) holder).llHeader.getVisibility() == View.VISIBLE) {
                    Log.e("PageViewHolder", "llHeader");
                }
                if (((PageViewHolder) holder).llFooter.getVisibility() == View.VISIBLE) {
                    Log.e("PageViewHolder", "llFooter");
                }
                ((PageViewHolder) holder).llpage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mList != null && mList.size() > 0) {
                            if (mList.contains(mBookDetails)) {
                                mList.remove(mBookDetails);
                                ((PageViewHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.transparent), android.graphics.PorterDuff.Mode.SRC_OVER);
                                //((PageViewHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                                mPdfListener.onShareImage(mPfdList, position, false);
                                // ((PageViewHolder) holder).ivSelect.setVisibility(View.GONE);
                            } else {
                                mList.add(mBookDetails);
                                ((PageViewHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.backcolor), android.graphics.PorterDuff.Mode.SRC_OVER);
                                // ((PageViewHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, R.color.backcolor));
                                mPdfListener.onShareImage(mPfdList, position, true);
                                //((PageViewHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mPdfListener.onImageClick(mPfdList, position);
                        }
                    }
                });
                ((PageViewHolder) holder).llpage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (mList.contains(mBookDetails)) {
                            mList.remove(mBookDetails);
                            ((PageViewHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.transparent), android.graphics.PorterDuff.Mode.SRC_OVER);
                            //((PageViewHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            mPdfListener.onShareImage(mPfdList, position, false);
                            // ((PageViewHolder) holder).ivSelect.setVisibility(View.GONE);
                        } else {
                            mList.add(mBookDetails);
                            ((PageViewHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.backcolor), android.graphics.PorterDuff.Mode.SRC_OVER);//((PageViewHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, R.color.backcolor));
                            mPdfListener.onShareImage(mPfdList, position, true);// ((PageViewHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });

                if (mList.contains(mBookDetails))
                    ((PageViewHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.backcolor), PorterDuff.Mode.SRC_OVER);
                    // ((PageViewHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, R.color.backcolor));
                    //((PageViewHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                else
                    // ((PageViewHolder) holder).ivSelect.setVisibility(View.GONE);
                    ((PageViewHolder) holder).ivPdf.setColorFilter(ContextCompat.getColor(context, R.color.transparent), PorterDuff.Mode.SRC_OVER);
                // ((PageViewHolder) holder).llpage.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            }

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mPfdList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String strType = mPfdList.get(position).getType();
        if (strType != null && strType.length() > 0 && strType.equalsIgnoreCase("1")) {
            return TYPE_PAGE;
        } else if (strType != null && strType.length() > 0 && strType.equalsIgnoreCase("0")) {
            return TYPE_WITHOUT_PAGE;
        } else {
            return TYPE_NUll;
        }
    }

    public interface PdfInterfaceListener {
        void onImageClick(ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList, int position);

        void onShareImage(ArrayList<BooksDataModel.BookImageDetailsModel> mPfdList, int position, boolean isSelected);
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPdf, ivSelect;
        TextView tvheadeRight;
        View viewLine;

        LinearLayout llHeader, llFooter, llpage;

        PageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPdf = itemView.findViewById(R.id.ivPdf);
            viewLine = itemView.findViewById(R.id.viewLine);
            llpage = itemView.findViewById(R.id.llpage);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            llHeader = itemView.findViewById(R.id.llHeader);
            llFooter = itemView.findViewById(R.id.llFooter);
            tvheadeRight = itemView.findViewById(R.id.tvHeaderRight);
        }
    }

    public static class OtheViewPageHolder extends RecyclerView.ViewHolder {
        View viewLine;
        TextView tvheadeRight;
        ImageView ivPdf, ivSelect;
        LinearLayout llpage, llRefer, llHeader, llFooter;

        OtheViewPageHolder(@NonNull View itemView) {
            super(itemView);
            ivPdf = itemView.findViewById(R.id.ivPdf);
            viewLine = itemView.findViewById(R.id.viewLine);
            llpage = itemView.findViewById(R.id.llpage);
            llRefer = itemView.findViewById(R.id.llRefer);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            llHeader = itemView.findViewById(R.id.llHeader);
            llFooter = itemView.findViewById(R.id.llFooter);
            tvheadeRight = itemView.findViewById(R.id.tvHeaderRight);
        }
    }
}

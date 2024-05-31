package com.jainelibrary.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.jainelibrary.R;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.retrofitResModel.ShlokGranthSutraResModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private static final String TAG = GalleryAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<PdfStoreListResModel.PdfListModel> filesModelsList = new ArrayList<>();
    private OnImageClickListener onImageClickListener;
    private OnImageZoomListener onImageZoomListener;

    public GalleryAdapter(Context context, ArrayList<PdfStoreListResModel.PdfListModel> filesModels, OnImageClickListener onImageClickListener, OnImageZoomListener onImageZoomListener) {
        this.context = context;
        this.filesModelsList = filesModels;
        this.onImageClickListener = onImageClickListener;
        this.onImageZoomListener = onImageZoomListener;
    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        String strBookName = filesModelsList.get(position).getBook_name();
        String strAutherName = filesModelsList.get(position).getAuthor_name();
        String strPublisherName = filesModelsList.get(position).getPublisher_name();
        String strOtherName = "";

        if (strBookName != null && strBookName.length() > 0) {
            // strOtherName = strBookName;
           // Log.e(TAG, "BookName : " + strBookName);
            holder.tvBookName.setText(Html.fromHtml(strBookName));

        }

        if (strAutherName != null && strAutherName.length() > 0) {
            //   strAutherName = " \n " + strAutherName/* + "&#91;" + "ले" + "&#93; "*/;
            // strOtherName = strOtherName + "\n"+strAutherName;
            if (!strAutherName.equalsIgnoreCase("0"))
            {
                holder.tvAuther.setText(Html.fromHtml(strAutherName));}
            else {
                holder.tvAuther.setText("");
            }

        }

        if (strPublisherName != null && strPublisherName.length() > 0) {
            //  strPublisherName = "\n " + strPublisherName /*+ "&#91;" + "प्रका" + "&#93;"*/;
            //  strOtherName = strOtherName + "\n"+strPublisherName;
            //Log.e(TAG, "PublisherName : " + strPublisherName);
            if(!strPublisherName.equalsIgnoreCase("0"))
            holder.tvPublisher.setText(Html.fromHtml(strPublisherName));else {

                    holder.tvPublisher.setText("");

            }
        }

        if (strOtherName != null && strOtherName.length() > 0) {
            //  holder.tvBookName.setText(Html.fromHtml(strOtherName));
        }

        String strBookURl = filesModelsList.get(position).getBook_url();
        if (strBookURl != null && strBookURl.length() > 0) {
            Picasso.get().load(strBookURl).placeholder(R.drawable.book).into(holder.ivImage);
        }
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strBookURl != null && strBookURl.length() > 0) {
                    onImageZoomListener.onImageClick(position, strBookURl);
                }
            }
        });

        System.getProperty(System.lineSeparator());
        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickListener.onMenuClick(filesModelsList.get(position), position, holder.ivMenu);
            }
        });

    }
   /* public static class Zoom extends View {

        private Drawable image;
        ImageButton img,img1;
        private int zoomControler=20;

        public Zoom(Context context){
            super(context);
            Picasso.get().load(strBookURl).into(holder.ivImage);
//            image=context.getResources().getDrawable(R.drawable.j);
            //image=context.getResources().getDrawable(R.drawable.icon);

            setFocusable(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //here u can control the width and height of the images........ this line is very important
            image.setBounds((getWidth()/2)-zoomControler, (getHeight()/2)-zoomControler, (getWidth()/2)+zoomControler, (getHeight()/2)+zoomControler);
            image.draw(canvas);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {

            if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
                // zoom in
                zoomControler+=10;
            }
            if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
                // zoom out
                zoomControler-=10;
            }
            if(zoomControler<10){
                zoomControler=10;
            }

            invalidate();
            return true;
        }
    }*/

    public void newData(ArrayList<PdfStoreListResModel.PdfListModel> list) {
        if (filesModelsList != null) {
            filesModelsList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public ArrayList<PdfStoreListResModel.PdfListModel> getData() {
       return filesModelsList;
    }

    @Override
    public int getItemCount() {
        return filesModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivMenu;
        TextView tvBookName, tvAuther, tvPublisher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuther = itemView.findViewById(R.id.tvOtherName);
            tvPublisher = itemView.findViewById(R.id.tvPublisherName);
        }
    }

    public interface OnImageClickListener {
        void onMenuClick(PdfStoreListResModel.PdfListModel filesModel, int position, ImageView ivMenu);
    }

    public interface OnImageZoomListener {
        public void onImageClick(int position, String s);
    }
}

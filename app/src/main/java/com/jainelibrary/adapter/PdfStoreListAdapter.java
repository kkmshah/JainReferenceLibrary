package com.jainelibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.model.PdfStoreListResModel;

import java.util.ArrayList;

public class PdfStoreListAdapter extends RecyclerView.Adapter<PdfStoreListAdapter.ViewHolder> {

    Context context;
    PdfStoreInterfaceListener pdfStoreInterfaceListener;
    ArrayList<PdfStoreListResModel.PdfListModel> pdfStoreList = new ArrayList<>();

    public PdfStoreListAdapter(Context context, PdfStoreInterfaceListener pdfStoreInterfaceListener, ArrayList<PdfStoreListResModel.PdfListModel> pdfList) {
        this.context = context;
        this.pdfStoreInterfaceListener = pdfStoreInterfaceListener;
        this.pdfStoreList = pdfList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_pdf_store_list, parent, false);
        return new PdfStoreListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final PdfStoreListResModel.PdfListModel PdfStoreListResModel = pdfStoreList.get(position);
        holder.tvName.setText(": " + PdfStoreListResModel.getBook_name());
        holder.tvAuthor.setText(": " + PdfStoreListResModel.getAuthor_name());
        holder.tvPublisher.setText(": " + PdfStoreListResModel.getPublisher_name());
        holder.imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfStoreInterfaceListener.onImageClick(pdfStoreList, position); }
        });
    }


    @Override
    public int getItemCount() {
        return pdfStoreList.size();
    }

    public interface PdfStoreInterfaceListener {
        void onImageClick(ArrayList<PdfStoreListResModel.PdfListModel> pdfBookList, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAuthor, tvPublisher;
        ImageView imgNext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            imgNext = itemView.findViewById(R.id.imgNext);
        }
    }
}

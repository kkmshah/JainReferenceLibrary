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
import com.jainelibrary.fragment.BooksFragment;
import com.jainelibrary.model.BookImageModel;

import java.util.ArrayList;

public class BookImageAdapter extends RecyclerView.Adapter<BookImageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BookImageModel> bookImageList = new ArrayList<>();
    private BookImageAdapter.OnMenuClickListener onMenuClickListener;

    public BookImageAdapter(Context context, ArrayList<BookImageModel> bookImageList, BooksFragment onMenuClickListener) {
        this.context = context;
        this.bookImageList = bookImageList;
        this.onMenuClickListener = (OnMenuClickListener) onMenuClickListener;
    }

    @NonNull
    @Override
    public BookImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_book_image, parent, false);
        return new BookImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookImageAdapter.ViewHolder holder, int position) {
        holder.tvBookName.setText(bookImageList.get(position).getBookName());

        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuClickListener.onMenuClick(bookImageList.get(position), position,holder.ivMenu);

            }
        });
    }

    @Override
    public int getItemCount() {
        return bookImageList.size();
    }

    public interface OnMenuClickListener {
        void onMenuClick(BookImageModel bookImageModel, int position, ImageView ivMenu);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivMenu;
        TextView tvBookName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvBookName = itemView.findViewById(R.id.tvBookName);
        }
    }


}

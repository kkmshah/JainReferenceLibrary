package com.jainelibrary.adapter;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jainelibrary.R;
import com.jainelibrary.model.BooksModel;
import java.util.ArrayList;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.ViewHolder> {
    Context context;
    ArrayList<BooksModel> booksList = new ArrayList<>();
    SearchInterfaceListener searchInterfaceListener;
    public KeywordAdapter(Context context, ArrayList<BooksModel> booksList, SearchInterfaceListener searchInterfaceListener){
        this.context = context;
        this.booksList = booksList;
        this.searchInterfaceListener = searchInterfaceListener;
    }

    @NonNull
    @Override
    public KeywordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_item_list,parent,false);
        return new KeywordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String strBookName = booksList.get(position).getBook_name();
        String strPageNo1 = booksList.get(position).getPage_no1();
        String strPageNo2 = booksList.get(position).getPage_no2();
        String strTotalPg = booksList.get(position).getTotal_pg();
        String strFinalPage = "Pg." + strPageNo1 + "-" + strPageNo2;
        holder.txtBookName.setText(strBookName);
        holder.txtTotalPg.setText(strTotalPg);
        holder.txtPageDetails.setText(Html.fromHtml(strFinalPage));
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInterfaceListener.onDetailsClick(booksList,position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return booksList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookName,txtPageDetails,txtTotalPg;
        CardView cvList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookName = (TextView) itemView.findViewById(R.id.txtBookName);
            txtPageDetails = (TextView) itemView.findViewById(R.id.txtPageDetails);
            txtTotalPg = (TextView) itemView.findViewById(R.id.txtTotalPg);
            cvList = itemView.findViewById(R.id.cvList);
        }
    }
    public interface SearchInterfaceListener {
        void onDetailsClick(ArrayList<BooksModel> searchList, int position);
    }
}

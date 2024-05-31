/*
package com.jainelibrary.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.retrofitResModel.BookListResModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HeaderFooterRecyclerviewpublic extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADERVIEW = 1;
    private static final int FOOTER_VIEW = 2;
    private ArrayList<String> data; // Take any list that matches your requirement.
    private Context context;
    ArrayList<BookListResModel.BookDetailsModel> mBookList;

    // Define a constructor
    public HeaderFooterRecyclerviewpublic(Context context, ArrayList<BookListResModel.BookDetailsModel> mBookList) {
        this.context = context;
        this.data = data;
        this.mBookList = mBookList;
    }

    // Define a ViewHolder for Footer view
    public class HeaderViewHolder extends ViewHolder {
        TextView tvDetails;
        CardView cvList;
        ImageView ivBook;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);
            cvList = itemView.findViewById(R.id.cvList);
            ivBook = itemView.findViewById(R.id.ivBook);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the item
                }
            });
        }
    }

    // Now define the ViewHolder for Normal list item
    public class NormalViewHolder extends ViewHolder {
        Button btnPageNo;
        LinearLayout ll;

        public NormalViewHolder(View itemView) {
            super(itemView);
            btnPageNo = itemView.findViewById(R.id.btnPageNo);
            ll = (LinearLayout) itemView.findViewById(R.id.buttonlayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the normal items
                }
            });
        }
    }

    // And now in onCreateViewHolder you have to pass the correct view
    // while populating the list item.

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        if (viewType == HEADERVIEW) {
            v = LayoutInflater.from(context).inflate(R.layout.list_item_header, parent, false);
            HeaderViewHolder vh = new HeaderViewHolder(v);
            return vh;
        } else

            v = LayoutInflater.from(context).inflate(R.layout.list_item_footer, parent, false);

        NormalViewHolder vh = new NormalViewHolder(v);

        return vh;


    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            ViewHolder itemViewHolder = (ViewHolder) holder;
            String strPublisherName = mBookList.get(position).getPublisher_name();
            String strBookname = mBookList.get(position).getBook_name();
            String strPageNo = mBookList.get(position).getPage_no();
            String strPdfPageNo = mBookList.get(position).getPdf_page_no();
            String strAutherName = mBookList.get(position).getAuthor_name();
            String strBookUrl = mBookList.get(position).getBook_url();


            if (strPdfPageNo == null || strPdfPageNo.length() == 0) {
                mBookList.get(position).setPdf_page_no(strPageNo);
            }

            String strFinalWord = null;

            if (strBookname != null && strBookname.length() > 0) {
                strFinalWord = strBookname;
            } else {
                strBookname = "";
            }

            if (strPageNo != null && strPageNo.length() > 0) {
                strPageNo = "" + "Page - " + strPageNo;
                holder.btnPageNo.setText(strPageNo);
            }

            if (strAutherName != null && strAutherName.length() > 0) {
                strAutherName = ", " + strAutherName */
/*+ "&#91;" + "ले" + "&#93; "*//*
;
                strFinalWord = strBookname + strAutherName;
                //strFinalWord.concat("," + strAutherName + "&#91;" + "ले" + "&#93; ");
            } else {
                strAutherName = "";
            }

            if (strPublisherName != null && strPublisherName.length() > 0) {
                strPublisherName = ", " + strPublisherName */
/*+ "&#91;" + "प्रका" + "&#93;"*//*
;
                strFinalWord = strBookname + strAutherName + strPublisherName;
            } else {
                strPublisherName = "";
            }

            if (strFinalWord != null && strFinalWord.length() > 0) {

                itemViewHolder.tvDetails.setText(Html.fromHtml(strFinalWord));
            }


            holder.cvList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //searchInterfaceListener.onDetailsClick(mBookList.get(position), position);
                }
            });
        } else if (holder instanceof HeaderViewHolder) {
            String strPublisherName = mBookList.get(position).getPublisher_name();
            String strBookname = mBookList.get(position).getBook_name();
            String strPageNo = mBookList.get(position).getPage_no();
            String strPdfPageNo = mBookList.get(position).getPdf_page_no();
            String strAutherName = mBookList.get(position).getAuthor_name();
            String strBookUrl = mBookList.get(position).getBook_url();


            if (strPdfPageNo == null || strPdfPageNo.length() == 0) {
                mBookList.get(position).setPdf_page_no(strPageNo);
            }

            String strFinalWord = null;

            if (strBookname != null && strBookname.length() > 0) {
                strFinalWord = strBookname;
            } else {
                strBookname = "";
            }

            if (strPageNo != null && strPageNo.length() > 0) {
                strPageNo = "" + "Page - " + strPageNo;
                holder.btnPageNo.setText(strPageNo);
            }

            if (strAutherName != null && strAutherName.length() > 0) {
                strAutherName = ", " + strAutherName */
/*+ "&#91;" + "ले" + "&#93; "*//*
;
                strFinalWord = strBookname + strAutherName;
                //strFinalWord.concat("," + strAutherName + "&#91;" + "ले" + "&#93; ");
            } else {
                strAutherName = "";
            }

            if (strPublisherName != null && strPublisherName.length() > 0) {
                strPublisherName = ", " + strPublisherName */
/*+ "&#91;" + "प्रका" + "&#93;"*//*
;
                strFinalWord = strBookname + strAutherName + strPublisherName;
            } else {
                strPublisherName = "";
            }

            if (strFinalWord != null && strFinalWord.length() > 0) {
                holder.tvDetails.setText(Html.fromHtml(strFinalWord));
            }


            holder.cvList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //searchInterfaceListener.onDetailsClick(mBookList.get(position), position);
                }
            });

        } else if (holder instanceof NormalViewHolder) {

        }

    }

    // Now the critical part. You have return the exact item count of your list
    // I've only one footer. So I returned data.size() + 1
    // If you've multiple headers and footers, you've to return total count
    // like, headers.size() + data.size() + footers.size()

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }

        if (data.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return data.size() + 1;
    }

    // Now define getItemViewType of your own.

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    // So you're done with adding a footer and its action on onClick.
    // Now set the default ViewHolder for NormalViewHolder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Define elements of a row here
        public ViewHolder(View itemView) {
            super(itemView);
            // Find view by ID and initialize here
        }


    }
}*/

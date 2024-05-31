package com.jainelibrary.multicheck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jainelibrary.R;
import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MultiCheckGenreAdapter extends CheckableChildRecyclerViewAdapter<MultiCheckGenreAdapter.GenreViewHolder, MultiCheckGenreAdapter.MultiCheckArtistViewHolder> {

    int sizeOfBookList = 0;
    int count;
    boolean isFocus = false;
    Context mContext;
    List<MultiCheckGenre> parentBookList = new ArrayList<>();
    ArrayList<String> bookIdList = new ArrayList<>();
    private int oldFlatPostion;
    private OnZoomListener onzoomListener;

    public MultiCheckGenreAdapter(boolean isFocus, List<MultiCheckGenre> books, ArrayList<String> bookIdList, int count, Context mContexton, OnZoomListener onZoomListener) {
        super(books);
        this.parentBookList = books;
        this.bookIdList = bookIdList;
        this.mContext = mContext;
        this.onzoomListener = onZoomListener;
        this.count = count;
        this.isFocus = isFocus;
    }

    public List<MultiCheckGenre> getParentBookList() {
        return parentBookList;
    }

    @Override
    public MultiCheckArtistViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_value_items, parent, false);
        return new MultiCheckArtistViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindCheckChildViewHolder(final MultiCheckArtistViewHolder holder, final int parentPosition, CheckedExpandableGroup group, final int childIndex) {

        final Book bookTemp = (Book) group.getItems().get(childIndex);
        Book book = (Book) parentBookList.get(bookTemp.getParentPos()).getItems().get(childIndex);
        if (isFocus) {
            if (!book.isSelected) {
                boolean isSelected = isSelected(book.getId());
                book.setSelected(isSelected);
            } else {
                if (bookIdList != null && bookIdList.size() > 0) {
                    String strBookId = book.getId();
                    if (!bookIdList.contains(strBookId)) {
                        bookIdList.add(strBookId);
                    }
                } else {
                    String strBookId = book.getId();
                    bookIdList.add(strBookId);
                }
            }
        } else {
            boolean isSelected = isSelected(book.getId());
            book.setSelected(isSelected);
        }

        //holder.llMain.removeAllViews();

        String strBookname = book.getName();
        String strAutherName = book.getAuthor_name();
        String strPublisherName = book.getPublisher_name();
        String strBookUrl = book.getBook_url();
        String strKeywordCount =  book.getKeyword_count();
        String strYearCount =  book.getYear_count();

        if (strBookUrl != null && strBookUrl.length() > 0) {
            Log.e("bookUrl : ", "tempBookUrl : " + strBookUrl);
            Picasso.get().load(strBookUrl).error(R.drawable.noimage).into(holder.ivBookImage);
        }

        String strFinalWord = "";

        if (strBookname != null && strBookname.length() > 0) {
            strFinalWord = strBookname;
        }

        if (strAutherName != null && strAutherName.length() > 0) {
            strAutherName = ", " + strAutherName/* + "&#91;" + "à¤²à¥‡" + "&#93; "*/;
            strFinalWord = strFinalWord + strAutherName;
        }

        if (strPublisherName != null && strPublisherName.length() > 0) {
            strPublisherName = ", " + strPublisherName /*+ "&#91;" + "à¤ªà¥à¤°à¤•à¤¾" + "&#93;"*/;
            strFinalWord = strFinalWord + strPublisherName;
        }

        if (strFinalWord != null && strFinalWord.length() > 0) {
            holder.tvDetails.setText(Html.fromHtml("" + strFinalWord));
        }

        if (strKeywordCount != null && Integer.parseInt(strKeywordCount) > 0) {
            holder.llKeywordCount.setVisibility(View.VISIBLE);
            holder.tvKeywordCount.setText(":  " + strKeywordCount);
        }else{
            holder.llKeywordCount.setVisibility(View.GONE);
        }

        if (strYearCount != null && Integer.parseInt(strYearCount) > 0) {
            holder.llYearCount.setVisibility(View.VISIBLE);
            holder.tvYearCount.setText(":  " + strYearCount);
        }else{
            holder.llYearCount.setVisibility(View.GONE);
        }

        holder.childCheckedTextView.setChecked(book.isSelected());

        holder.childCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.childCheckedTextView.isChecked()) {
                    Book tempBook = (Book) parentBookList.get(bookTemp.getParentPos()).getItems().get(childIndex);
                    boolean isSelected = isSelected(tempBook.getId());
                    if (!isSelected) {
                        bookIdList.add(tempBook.getId());
                    }
                    tempBook.setSelected(true);
                    parentBookList.get(bookTemp.getParentPos()).getItems().set(childIndex, tempBook);
                } else {
                    Book tempBook = (Book) parentBookList.get(bookTemp.getParentPos()).getItems().get(childIndex);
                    boolean isSelected = isSelected(tempBook.getId());
                    if (isSelected) {
                        bookIdList.remove(tempBook.getId());
                    }
                    tempBook.setSelected(false);
                    parentBookList.get(bookTemp.getParentPos()).getItems().set(childIndex, tempBook);
                }
                notifyDataSetChanged();
            }
        });

        holder.llChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.childCheckedTextView.isChecked()) {
                    Book tempBook = (Book) parentBookList.get(bookTemp.getParentPos()).getItems().get(childIndex);
                    boolean isSelected = isSelected(tempBook.getId());
                    if (!isSelected) {
                        bookIdList.add(tempBook.getId());
                    }
                    tempBook.setSelected(true);
                    parentBookList.get(bookTemp.getParentPos()).getItems().set(childIndex, tempBook);
                } else {
                    Book tempBook = (Book) parentBookList.get(bookTemp.getParentPos()).getItems().get(childIndex);
                    boolean isSelected = isSelected(tempBook.getId());
                    if (isSelected) {
                        bookIdList.remove(tempBook.getId());
                    }
                    tempBook.setSelected(false);
                    parentBookList.get(bookTemp.getParentPos()).getItems().set(childIndex, tempBook);
                }
                notifyDataSetChanged();
            }
        });
        /*holder.ivBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((((Book) parentBookList.get(bookTemp.getParentPos()).getItems().get(childIndex)).getBook_url() != null) &&
                        ((Book) parentBookList.get(bookTemp.getParentPos()).getItems().get(childIndex)).getBook_url().length() == 0)
                    ;
                onzoomListener.OnZoomClick(view, oldFlatPostion, ((Book) parentBookList.get(bookTemp.getParentPos()).getItems().get(childIndex)).getBook_url());
            }
        });*/
    }

    public boolean isSelected(String bookId) {
        boolean isSelect = false;
        if (bookIdList != null && bookIdList.size() > 0) {
            for (int i = 0; i < bookIdList.size(); i++) {
                String strBookId = bookIdList.get(i);
                if (bookId != null && bookId.equalsIgnoreCase(strBookId)) {
                    isSelect = true;
                }
            }
        }
        return isSelect;
    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cat_filter, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setParentTitle(group);
        oldFlatPostion = flatPosition;
    }

    public interface OnZoomListener {
        public void OnZoomClick(View view, int position, String s);
    }

    ////////// Parent View Holder ////////////////
    public class GenreViewHolder extends GroupViewHolder {

        TextView tvBooksCount, tvBooks;
        CheckBox cbBooks;
        private TextView genreName;
        private ImageView arrow;

        public GenreViewHolder(View itemView) {
            super(itemView);
            genreName = itemView.findViewById(R.id.list_item_genre_name);
            arrow = itemView.findViewById(R.id.list_item_genre_arrow);
            tvBooksCount = itemView.findViewById(R.id.tvBooksCount);
            tvBooks = itemView.findViewById(R.id.tvBooks);
            cbBooks = itemView.findViewById(R.id.cbBooks);
        }


        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }


        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        public void setParentTitle(ExpandableGroup genre) {
            if (genre instanceof MultiCheckGenre) {
                sizeOfBookList = genre.getItems().size();
                genreName.setText(genre.getTitle() + " (" + sizeOfBookList + ")");

                final int finalFlatPosition = ((MultiCheckGenre) genre).getParentPos();
                cbBooks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cbBooks.isChecked()) {
                            MultiCheckGenre tempArtist = parentBookList.get(finalFlatPosition);
                            tempArtist.setSelected(true);
                            for (int j = 0; j < tempArtist.getItems().size(); j++) {
                                Book childBook = (Book) tempArtist.getItems().get(j);
                                childBook.setSelected(true);
                                boolean isSelecteds = isSelected(childBook.getId());
                                if (!isSelecteds) {
                                    bookIdList.add(childBook.getId());
                                }
                                tempArtist.getItems().set(j, childBook);
                            }
                            parentBookList.set(finalFlatPosition, tempArtist);
                        } else {
                            MultiCheckGenre tempArtist = parentBookList.get(finalFlatPosition);
                            tempArtist.setSelected(false);
                            for (int j = 0; j < tempArtist.getItems().size(); j++) {
                                Book childBook = (Book) tempArtist.getItems().get(j);
                                childBook.setSelected(false);
                                boolean isSelecteds = isSelected(childBook.getId());
                                if (isSelecteds) {
                                    bookIdList.remove(childBook.getId());
                                }
                                tempArtist.getItems().set(j, childBook);
                            }
                            parentBookList.set(finalFlatPosition, tempArtist);
                        }
                        notifyDataSetChanged();
                    }
                });

                int checkedCount = 0;

                for (int i = 0; i < genre.getItems().size(); i++) {
                    Book bookTemp = (Book) genre.getItems().get(i);
                    if (isSelected(bookTemp.getId())) {
                        checkedCount++;
                    }
                }

                if (checkedCount == sizeOfBookList && sizeOfBookList > 0) {
                    cbBooks.setChecked(true);
                } else {
                    cbBooks.setChecked(false);
                }

                if (bookIdList.size() != 0 && bookIdList.size() > 1) {
                    String s = bookIdList + "";
                    s = s.replace("]", "");
                    s = s.replace("[", "");
                    Log.e("", bookIdList.size() + "");
                    tvBooksCount.setText(" " + sizeOfBookList + "/" + count);
                    tvBooks.setText("Books");

                } else {
                    String s = bookIdList + "";
                    s = s.replace("]", "");
                    s = s.replace("[", "");
                    tvBooksCount.setText(" " + sizeOfBookList + "/" + count);
                    tvBooks.setText("Book");
                }
            }
        }
    }

    ///////////////////// Child View Holder ////////////////////////////

    public class MultiCheckArtistViewHolder extends CheckableChildViewHolder {

        public CheckBox childCheckedTextView;
        TextView tvDetails, tvKeywordCount, tvYearCount;
        ImageView ivBookImage;
        LinearLayout llChecked, llKeywordCount, llYearCount, llMain;

        public MultiCheckArtistViewHolder(View itemView) {
            super(itemView);
            childCheckedTextView = itemView.findViewById(R.id.cbDetails);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvKeywordCount = itemView.findViewById(R.id.tvKeywordCount);
            tvYearCount = itemView.findViewById(R.id.tvYearCount);
            ivBookImage = itemView.findViewById(R.id.ivBookImage);
            llChecked = itemView.findViewById(R.id.llChecked);
            llKeywordCount = itemView.findViewById(R.id.llKeywordCount);
            llYearCount = itemView.findViewById(R.id.llYearCount);
            llMain = itemView.findViewById(R.id.llMain);
        }

        @Override
        public Checkable getCheckable() {
            return childCheckedTextView;
        }
    }
}

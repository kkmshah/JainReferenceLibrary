package com.jainelibrary.utils;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginateWithAnimScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD =300;
    private int scrolledDistance = 25;
    private boolean controlsVisible = true;
    LinearLayoutManager layoutManager;

    public PaginateWithAnimScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            Log.e("scrolledDistance +"+ scrolledDistance,"HIDE_THRESHOLD + "+ HIDE_THRESHOLD);
            controlsVisible = false;
            scrolledDistance =25;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            Log.e("scrolledDistance-"+ scrolledDistance,"HIDE_THRESHOLD - "+ HIDE_THRESHOLD);
            controlsVisible = true;
            scrolledDistance = 25;
        }

        if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
            scrolledDistance += dy;
        }

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() ) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }

    }

    public abstract void onHide();
    public abstract void onShow();
    protected abstract void loadMoreItems();
    public abstract boolean isLoading();
}
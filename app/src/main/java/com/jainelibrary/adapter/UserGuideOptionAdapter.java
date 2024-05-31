package com.jainelibrary.adapter;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.TouchyWebView;
import com.jainelibrary.fragment.UgSettingsFragment;
import com.jainelibrary.model.UserGuideResModel;

import java.util.ArrayList;
import java.util.List;

public class UserGuideOptionAdapter extends RecyclerView.Adapter<UserGuideOptionAdapter.MyViewHolder> {

    private Context context;
    private List<UserGuideResModel.UserGuideModel> userGuideList = new ArrayList<>();
    private List<MyViewHolder> myViewHolderList = new ArrayList<>();

    public UserGuideOptionAdapter(Context context, List<UserGuideResModel.UserGuideModel> userGuideList) {
        this.context = context;
        this.userGuideList = userGuideList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_guide_item, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ViewGroup.LayoutParams layoutParams = mvh.tvDescription.getLayoutParams();
        layoutParams.height = height - (height * 40 / 100);

        mvh.tvDescription.setLayoutParams(layoutParams);

        mvh.tvDescription.getSettings().setSupportZoom(true);
        mvh.tvDescription.getSettings().setBuiltInZoomControls(true);
        mvh.tvDescription.getSettings().setDisplayZoomControls(false);

        myViewHolderList.add(mvh);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        final UserGuideResModel.UserGuideModel userGuideModel = userGuideList.get(position);
        myViewHolder.tvTitle.setText(userGuideModel.getTitle());

        Log.e("UserGuideTitle", "---UGTitle" + userGuideModel.getTitle() + " " + userGuideModel.getContent().length());

        String strId = String.valueOf(userGuideModel.getId());

        myViewHolder.btn_language.setText("English");
        myViewHolder.tvDescription.loadData(userGuideModel.getContent(), "text/html; charset=UTF-8", "utf-8");

        myViewHolder.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myViewHolder.llDetailsInfo.getVisibility() == View.VISIBLE) {
                    myViewHolder.llDetailsInfo.setVisibility(View.GONE);
                    myViewHolder.btn_language.setVisibility(View.GONE);

                    RotateAnimation rotate =
                            new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(300);
                    rotate.setFillAfter(true);
                    myViewHolder.ivInfo.startAnimation(rotate);
                } else {
                    for (MyViewHolder myViewHolder: myViewHolderList)
                    {
                        if (myViewHolder.llDetailsInfo.getVisibility() == View.VISIBLE) {
                            myViewHolder.llDetailsInfo.setVisibility(View.GONE);
                            myViewHolder.btn_language.setVisibility(View.GONE);

                            RotateAnimation rotate =
                                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                            rotate.setDuration(300);
                            rotate.setFillAfter(true);
                            myViewHolder.ivInfo.startAnimation(rotate);
                        }
                    }

                    myViewHolder.llDetailsInfo.setVisibility(View.VISIBLE);
                    myViewHolder.btn_language.setVisibility(View.VISIBLE);

                    RotateAnimation rotate =
                            new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(300);
                    rotate.setFillAfter(true);
                    myViewHolder.ivInfo.startAnimation(rotate);
                }
            }
        });

        myViewHolder.btn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myViewHolder.llDetailsInfo.getVisibility() == View.VISIBLE) {
                 String langText = myViewHolder.btn_language.getText().toString();
                 if (langText.equals("English"))
                 {
                     myViewHolder.btn_language.setText("Hindi");
                     myViewHolder.tvDescription.loadData(userGuideList.get(position).getContent_english(), "text/html; charset=UTF-8", "utf-8");
                 }
                 else if (langText.equals("Hindi"))
                 {
                     myViewHolder.btn_language.setText("English");
                     myViewHolder.tvDescription.loadData(userGuideList.get(position).getContent(), "text/html; charset=UTF-8", "utf-8");
                 }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userGuideList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        LinearLayout llDetailsInfo;
        TouchyWebView tvDescription;
        Button btn_language;
        ImageView ivInfo;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvDetails);
            llDetailsInfo = view.findViewById(R.id.llDetailsInfo);
            tvDescription = view.findViewById(R.id.wvDescription);
            btn_language = view.findViewById(R.id.btn_language);
            ivInfo = view.findViewById(R.id.ivInfo);
        }
    }
}

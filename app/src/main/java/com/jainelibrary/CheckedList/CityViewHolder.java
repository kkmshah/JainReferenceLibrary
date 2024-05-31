/*
package com.jainelibrary.CheckedList;

import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.dhanera.landmarklive.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class CityViewHolder extends GroupViewHolder {

    private CheckBox cityName;
    private ImageView arrow;

    public CityViewHolder(View itemView) {
        super(itemView);
        cityName = (CheckBox) itemView.findViewById(R.id.checkbox_group);
        arrow = (ImageView) itemView.findViewById(R.id.image_group);
    }

    public void setCityTitle(ExpandableGroup city) {
        Log.v("test", "" + city.getTitle());
        Log.v("test2", "" + cityName);
        if (city instanceof MultiCheckCity) {
            cityName.setText(city.getTitle());
        }
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
        RotateAnimation rotate = new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate = new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}*/

/*
package com.jainelibrary.CheckedList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.dhanera.landmarklive.R;
import com.dhanera.landmarklive.model.Child;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MultiCheckCityAdapter extends CheckableChildRecyclerViewAdapter<MultiCheckCityAdapter.CityViewHolder, MultiCheckMensaViewHolder> {

    private final int MAX_MENSA = 5;
    private int mensa_count = 0;
    onCheckBoxClick onCheckBoxClick;
    List<MultiCheckCity> groups;
    Context context;

    public interface onCheckBoxClick {
        void onItemClick(int type, int sectionPosition, int childPosition, boolean state); //type 0 section, 1 child, state true/false
    }

    public void onCheckBoxClickListner(MultiCheckCityAdapter.onCheckBoxClick onCheckBoxClick) {
        this.onCheckBoxClick = onCheckBoxClick;
    }

    public MultiCheckCityAdapter(Context context, List<MultiCheckCity> groups) {
        super(groups);
        this.groups = groups;
        this.context = context;
        onCheckBoxClickListner((onCheckBoxClick) context);
    }

    @Override
    public MultiCheckMensaViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MultiCheckMensaViewHolder(view);
    }

    @Override
    public void onBindCheckChildViewHolder(MultiCheckMensaViewHolder holder, final int flatPosition, CheckedExpandableGroup group, final int childIndex) {
        final Child child = (Child) group.getItems().get(childIndex);
        holder.childCheckedTextView.setVisibility(View.VISIBLE);
        holder.childCheckedTextView.setText(child.getName());
        holder.childCheckedTextView.setChecked(child.isState());
        Log.e("child" + child.getName(), holder.childCheckedTextView.isChecked() + "");
        holder.childCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                onCheckBoxClick.onItemClick(1, flatPosition, childIndex, cb.isChecked());
            }
        });

    }



    @Override
    public void checkChild(boolean checked, int groupIndex, int childIndex) {
        super.checkChild(checked, groupIndex, childIndex);
        onCheckBoxClick.onItemClick(1, groupIndex, childIndex, checked);
    }

    @Override
    public void onChildCheckChanged(View view, boolean checked, int flatPos) {
        CharSequence toastText = "Maximum 5 Mensa";
        if (checked == false) {
            mensa_count--;
        }
        if (checked == true && mensa_count + 1 >= 6) {
            Toast.makeText(view.getContext(), " R.string.max_reached", Toast.LENGTH_SHORT).show();
        }
        super.onChildCheckChanged(view, checked, flatPos);

    }


    @Override
    public CityViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group, parent, false);
        Log.v("test3", "" + view);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(CityViewHolder holder, final int flatPosition, ExpandableGroup group) {

        //SectionHeader header = (SectionHeader) group.getItems().get(flatPosition);
        holder.cityName.setVisibility(View.VISIBLE);
        holder.cityName.setText(group.getTitle());
        holder.cityName.setChecked(holder.cityName.isChecked());
        Log.e("section" + group.getTitle(), holder.cityName.isChecked() + "");
        holder.cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                Log.e("checkbox secton",cb.isChecked()+"");
                onCheckBoxClick.onItemClick(0, flatPosition, 0, cb.isChecked());
            }
        });
    }

    public class CityViewHolder extends GroupViewHolder {

        public CheckBox cityName;
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
    }

}*/

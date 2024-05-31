package com.jainelibrary.adapter.viewHolder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jainelibrary.activity.UnitRelationTreeActivity;
import com.jainelibrary.retrofitResModel.UnitRelationChartDataResModel;
import com.jainelibrary.treeview.TreeNode;
import com.jainelibrary.treeview.TreeViewAdapter;
import com.jainelibrary.treeview.TreeViewHolder;

import com.jainelibrary.R;

public class UnitRelationNodeViewHolder extends TreeViewHolder {
    private static final String TAG = UnitRelationTreeActivity.class.getSimpleName();

    private TextView tvUnitChildUsers, tvUnitName, tvBioCount,tvEventCount,tvRelationCount;
    private ImageView trNodeStateIcon;
    private RelativeLayout trNodeLinkIcon;
    private ImageView ivUnitView, ivUnitTree;

    public interface OnViewUnitClickListener {
        /**
         * Called when a TreeNode has been clicked.
         * @param unitRelation The current clicked node unit
         * @param view The view that was clicked and held.
         */
        void onTreeNodeViewClick(UnitRelationChartDataResModel.UnitRelation unitRelation, View view);
        void onTreeNodeViewTreeClick(UnitRelationChartDataResModel.UnitRelation unitRelation, View view);
    }

    OnViewUnitClickListener treeNodeViewClickListener;
    public UnitRelationNodeViewHolder(@NonNull View itemView) {
        super(itemView);
        initViews();
    }

    /**
     * Register a callback to be invoked when this TreeNode is clicked
     * @param listener The callback that will run
     */
    public void setTreeNodeViewClickListener(UnitRelationNodeViewHolder.OnViewUnitClickListener listener) {
        this.treeNodeViewClickListener = listener;
    }
    private void initViews() {
        tvUnitChildUsers = itemView.findViewById(R.id.tvUnitChildUsers);
        ivUnitView = itemView.findViewById(R.id.ivUnitView);
        ivUnitTree = itemView.findViewById(R.id.ivUnitTree);
        tvUnitName = itemView.findViewById(R.id.tvUnitName);
        trNodeStateIcon = itemView.findViewById(R.id.trNodeStateIcon);
        trNodeLinkIcon = itemView.findViewById(R.id.trNodeLinkIcon);

        tvBioCount = itemView.findViewById(R.id.tvBioCount);
        tvEventCount = itemView.findViewById(R.id.tvEventCount);

        tvRelationCount = itemView.findViewById(R.id.tvRelationCount);

    }

    @Override
    public void bindTreeNode(TreeNode node) {
        super.bindTreeNode(node);

        UnitRelationChartDataResModel.UnitRelation unitRelation = (UnitRelationChartDataResModel.UnitRelation) node.getValue();
//        fileName.setText(fileNameStr);
        tvUnitName.setText(unitRelation.getName());

        tvBioCount.setText("B ("+unitRelation.getBiodata_count()+ ")");
        tvEventCount.setText("E ("+unitRelation.getEvent_count()+ ")");
        tvRelationCount.setText("R ("+unitRelation.getRelation_count()+ ")");
        tvUnitChildUsers.setText("U ("+unitRelation.getUsers_count()+ ")");
        if (node.getLevel() > 0) {
            trNodeLinkIcon.setVisibility(View.VISIBLE);
            ivUnitView.setVisibility(View.VISIBLE);
            ivUnitTree.setVisibility(View.VISIBLE);
        }else {
            trNodeLinkIcon.setVisibility(View.INVISIBLE);
            ivUnitView.setVisibility(View.GONE);
            ivUnitTree.setVisibility(View.GONE);
        }
        if (node.getChildren().isEmpty()) {
            trNodeStateIcon.setVisibility(View.GONE);
            tvUnitChildUsers.setVisibility(View.GONE);
        } else {
            trNodeStateIcon.setVisibility(View.VISIBLE);
            tvUnitChildUsers.setVisibility(View.VISIBLE);
            int stateIcon = node.isExpanded() ? R.drawable.ic_tree_arrow_down : R.drawable.ic_tree_arrow_right;
            trNodeStateIcon.setImageResource(stateIcon);
        }

        ivUnitView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "onTreeNodeClick" + " view unit");
                    if(treeNodeViewClickListener !=null) {
                        treeNodeViewClickListener.onTreeNodeViewClick(unitRelation, view);
                    }
                }
        });


        ivUnitTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onTreeNodeClick" + " view unit");
                if(treeNodeViewClickListener !=null) {
                    treeNodeViewClickListener.onTreeNodeViewTreeClick(unitRelation, view);
                }
            }
        });

    }
}

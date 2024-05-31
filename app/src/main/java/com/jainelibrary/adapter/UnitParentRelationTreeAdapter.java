package com.jainelibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainelibrary.R;
import com.jainelibrary.adapter.viewHolder.UnitParentRelationTreeNode;
import com.jainelibrary.model.RelationDetailsModel;
import com.jainelibrary.retrofitResModel.UnitRelationChartDataResModel;

import java.util.ArrayList;
import java.util.LinkedList;

public class UnitParentRelationTreeAdapter extends RecyclerView.Adapter<UnitParentRelationTreeAdapter.ViewHolder> {


    LinkedList<UnitParentRelationTreeNode> parentNodeList;
    Context context;
    UnitParentNodeClickListener unitParentNodeClickListener;



    public UnitParentRelationTreeAdapter(Context context, LinkedList<UnitParentRelationTreeNode> parentNodeList, UnitParentNodeClickListener unitParentNodeClickListener) {
        this.context = context;
        this.parentNodeList = parentNodeList;
        this.unitParentNodeClickListener = unitParentNodeClickListener;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_unit_parent_relation_node, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private int nodePadding = 50;

    public LinkedList<UnitParentRelationTreeNode> getTreeNodes() {
        return parentNodeList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        UnitParentRelationTreeNode node = (UnitParentRelationTreeNode) parentNodeList.get(position);
        UnitRelationChartDataResModel.UnitParentRelation unitRelation = node.getValue();
        int padding = node.getLevel() * nodePadding;

        Log.e("Padding right", "Postions" + position + "::" + padding + "=="+node.getLevel() +"--"+ unitRelation.getParents().size());
     //   int padding = node.getLevel() * nodePadding;
        holder.itemView.setPadding(
                padding,
                holder.itemView.getPaddingTop(),
                holder.itemView.getPaddingRight(),
                holder.itemView.getPaddingBottom());
  //        fileName.setText(fileNameStr);
        holder.tvUnitName.setText(unitRelation.getName());

        holder.tvBioCount.setText("B ("+unitRelation.getBiodata_count()+ ")");
        holder.tvEventCount.setText("E ("+unitRelation.getEvent_count()+ ")");
        holder.tvRelationCount.setText("R ("+unitRelation.getRelation_count()+ ")");
        holder.tvUnitChildUsers.setText("U ("+unitRelation.getParent_count()+ ")");

        if(node.getLevel() == 0) {
            holder.ivUnitView.setVisibility(View.INVISIBLE);
            holder.ivUnitTree.setVisibility(View.INVISIBLE);
        } else {
            holder.ivUnitView.setVisibility(View.VISIBLE);
            holder.ivUnitTree.setVisibility(View.VISIBLE);
        }
        if (node.getLevel() > 0) {
            holder.trNodeLinkIcon.setVisibility(View.VISIBLE);
            holder.trNodeStateIcon.setVisibility(View.VISIBLE);
        } else {
            holder.trNodeLinkIcon.setVisibility(View.INVISIBLE);
            holder.trNodeStateIcon.setVisibility(View.INVISIBLE);
        }
        //holder.tvUnitChildUsers.setVisibility(View.GONE);


        holder.ivUnitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    unitParentNodeClickListener.onUnitParentNodeClick(node);
            }
        });
        holder.ivUnitTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitParentNodeClickListener.onUnitParentNodeTreeClick(node);
            }
        });


    }

    @Override
    public int getItemCount() {
        return parentNodeList.size();
    }

    public interface UnitParentNodeClickListener{
        public void onUnitParentNodeClick(UnitParentRelationTreeNode node);
        public void onUnitParentNodeTreeClick(UnitParentRelationTreeNode node);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUnitChildUsers, tvUnitName, tvBioCount,tvEventCount,tvRelationCount;
        private ImageView trNodeStateIcon;
        private RelativeLayout trNodeLinkIcon;
        private ImageView ivUnitView, ivUnitTree;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
    }
}

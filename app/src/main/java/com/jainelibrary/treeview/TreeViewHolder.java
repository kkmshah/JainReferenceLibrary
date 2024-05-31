package com.jainelibrary.treeview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Default RecyclerView.ViewHolder for the TreeView the default behaviour is to manage the padding,
 * user should create custom one for each different layout and override bindTreeNode
 */
public class TreeViewHolder extends RecyclerView.ViewHolder {

    /**
     * The default padding value for the TreeNode item
     */
    private int nodePadding = 50;

    public TreeViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * Bind method that provide padding and bind TreeNode to the view list item
     * @param node the current TreeNode
     */
    public void bindTreeNode(TreeNode node) {
        int padding = node.getLevel() * nodePadding;
        itemView.setPadding(
                padding,
                itemView.getPaddingTop(),
                itemView.getPaddingRight(),
                itemView.getPaddingBottom());
    }

    /**
     * Modify the current node padding value
     * @param padding the new padding value
     */
    public void setNodePadding(int padding) {
        this.nodePadding = padding;
    }

    /**
     * Return the current TreeNode padding value
     * @return The current padding value
     */
    public int getNodePadding() {
        return nodePadding;
    }
}

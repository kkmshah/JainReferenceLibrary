package com.jainelibrary.adapter.viewHolder;

import com.jainelibrary.retrofitResModel.UnitRelationChartDataResModel.UnitParentRelation;

public class UnitParentRelationTreeNode {
    int level;
    UnitParentRelation value;

        public UnitParentRelationTreeNode(int level, UnitParentRelation value) {
        this.level = level;
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public UnitParentRelation getValue() {
        return value;
    }

    public void setValue(UnitParentRelation value) {
        this.value = value;
    }
}

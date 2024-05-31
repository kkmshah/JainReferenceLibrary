package com.jainelibrary.retrofitResModel;

import com.jainelibrary.model.UnitDetailsModel;
import com.jainelibrary.treeview.TreeNode;

import java.io.Serializable;
import java.util.ArrayList;

public class UnitRelationChartDataResModel  implements Serializable {

    public boolean status;
    public String message;
    public UnitDetailsModel details;

    public RootUnitRelation root_node = new RootUnitRelation();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UnitDetailsModel getDetails() {
        return details;
    }

    public void setDetails(UnitDetailsModel details) {
        this.details = details;
    }

    public RootUnitRelation getRoot_node() {
        return root_node;
    }

    public void setRoot_node(RootUnitRelation root_node) {
        this.root_node = root_node;
    }

    public  UnitParentRelation  rote_parent_node = new UnitParentRelation();

    public UnitParentRelation getRote_parent_node() {
        return rote_parent_node;
    }

    public void setRote_parent_node(UnitParentRelation rote_parent_node) {
        this.rote_parent_node = rote_parent_node;
    }

    public class RootUnitRelation extends UnitRelation {


    }

    public class UnitParentRelation extends UnitRelation {

        String parent_depth_level;

        String sibling_num;
        String parent_count = "0";

        ArrayList<UnitParentRelation> parents = new ArrayList<UnitParentRelation>();

        public ArrayList<UnitParentRelation> getParents() {
            return parents;
        }

        public void setParents(ArrayList<UnitParentRelation> parents) {
            this.parents = parents;
        }

        public String getParent_depth_level() {
            return parent_depth_level;
        }

        public void setParent_depth_level(String parent_depth_level) {
            this.parent_depth_level = parent_depth_level;
        }

        public String getSibling_num() {
            return sibling_num;
        }

        public void setSibling_num(String sibling_num) {
            this.sibling_num = sibling_num;
        }

        public String getParent_count() {
            return parent_count;
        }

        public void setParent_count(String parent_count) {
            this.parent_count = parent_count;
        }
    }

    public class UnitRelation implements Serializable {
        String id;
        String name;
        String parent_level;
        String child_depth_level;
        String users_count = "0";
        String biodata_count = "0";
        String event_count = "0";
        String relation_count = ")";
        String relation_name;
        ArrayList<UnitRelation> children = new ArrayList<UnitRelation>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent_level() {
            return parent_level;
        }

        public void setParent_level(String parent_level) {
            this.parent_level = parent_level;
        }

        public String getChild_depth_level() {
            return child_depth_level;
        }

        public void setChild_depth_level(String child_depth_level) {
            this.child_depth_level = child_depth_level;
        }

        public String getUsers_count() {
            return users_count;
        }

        public void setUsers_count(String users_count) {
            this.users_count = users_count;
        }

        public String getBiodata_count() {
            return biodata_count;
        }

        public void setBiodata_count(String biodata_count) {
            this.biodata_count = biodata_count;
        }

        public String getEvent_count() {
            return event_count;
        }

        public void setEvent_count(String event_count) {
            this.event_count = event_count;
        }

        public String getRelation_count() {
            return relation_count;
        }

        public void setRelation_count(String relation_count) {
            this.relation_count = relation_count;
        }

        public String getRelation_name() {
            return relation_name;
        }

        public void setRelation_name(String relation_name) {
            this.relation_name = relation_name;
        }

        public ArrayList<UnitRelation> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<UnitRelation> children) {
            this.children = children;
        }


        public String toString() {
            return  name;
        }

    }

}

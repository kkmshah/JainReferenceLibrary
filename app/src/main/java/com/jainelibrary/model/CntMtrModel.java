package com.jainelibrary.model;

import java.io.Serializable;

public class CntMtrModel implements Serializable {

    String id;
    String entity_type;
    String entity_id;
    String link_type;
    String link_entity_type;
    String link_entity_id;

    String type_name;
    String details;
    int image_count;
    int ref_count;
    int cnt_count;
    int mtr_count;

    BiodataMemoryDetailsModel biodata_entity;


    RelationModel relation_entity;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getLink_entity_type() {
        return link_entity_type;
    }

    public void setLink_entity_type(String link_entity_type) {
        this.link_entity_type = link_entity_type;
    }

    public String getLink_entity_id() {
        return link_entity_id;
    }

    public void setLink_entity_id(String link_entity_id) {
        this.link_entity_id = link_entity_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public int getRef_count() {
        return ref_count;
    }

    public void setRef_count(int ref_count) {
        this.ref_count = ref_count;
    }

    public int getCnt_count() {
        return cnt_count;
    }

    public void setCnt_count(int cnt_count) {
        this.cnt_count = cnt_count;
    }

    public int getMtr_count() {
        return mtr_count;
    }

    public void setMtr_count(int mtr_count) {
        this.mtr_count = mtr_count;
    }

    public BiodataMemoryDetailsModel getBiodata_entity() {
        return biodata_entity;
    }

    public void setBiodata_entity(BiodataMemoryDetailsModel biodata_entity) {
        this.biodata_entity = biodata_entity;
    }

    public RelationModel getRelation_entity() {
        return relation_entity;
    }

    public void setRelation_entity(RelationModel relation_entity) {
        this.relation_entity = relation_entity;
    }
}
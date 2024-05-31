package com.jainelibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RelationDetailsModel implements Serializable {
   String id;
   String number;
   String sequence;
   String unit_id;
   String relation_name;
   String relation_id;

   String unit_number;
   String name;
   String type_name;
   String status_name;
   String sect_name;
   String biodata_count;
   String event_count;
   String relation_count;
   String image_count;


    String cnt_count;
    String mtr_count;

    ArrayList reference_book_pages;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getUnit_number() {
        return unit_number;
    }

    public void setUnit_number(String unit_number) {
        this.unit_number = unit_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getSect_name() {
        return sect_name;
    }

    public void setSect_name(String sect_name) {
        this.sect_name = sect_name;
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

    public String getImage_count() {
        return image_count;
    }

    public void setImage_count(String image_count) {
        this.image_count = image_count;
    }

    public String getCnt_count() {
        return cnt_count;
    }

    public void setCnt_count(String cnt_count) {
        this.cnt_count = cnt_count;
    }

    public String getMtr_count() {
        return mtr_count;
    }

    public void setMtr_count(String mtr_count) {
        this.mtr_count = mtr_count;
    }

    public ArrayList getReference_book_pages() {
        return reference_book_pages;
    }

    public void setReference_book_pages(ArrayList reference_book_pages) {
        this.reference_book_pages = reference_book_pages;
    }
}

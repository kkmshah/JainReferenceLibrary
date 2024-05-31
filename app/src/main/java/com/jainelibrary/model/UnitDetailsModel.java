package com.jainelibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

public  class UnitDetailsModel  implements Serializable {
    String id;
    String sequence;
    String number;
    String type_id;
    String name;

    String name_english;
    String status_id;
    String sect_id;
    String type_name;
    String status_name;
    String sect_name;
    String status_ids;
    String sect_ids;
    String biodata_count;
    String event_count;
    String relation_count;

    ArrayList<BiodataMemoryDetailsModel> biodata_list;
    ArrayList<BiodataMemoryDetailsModel> events_list;
    ArrayList<RelationDetailsModel> relation_list;
    ArrayList<ImageFileModel> files;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_english() {
        return name_english;
    }

    public void setName_english(String name_english) {
        this.name_english = name_english;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getSect_id() {
        return sect_id;
    }

    public void setSect_id(String sect_id) {
        this.sect_id = sect_id;
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

    public String getStatus_ids() {
        return status_ids;
    }

    public void setStatus_ids(String status_ids) {
        this.status_ids = status_ids;
    }

    public String getSect_ids() {
        return sect_ids;
    }

    public void setSect_ids(String sect_ids) {
        this.sect_ids = sect_ids;
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

    public ArrayList<BiodataMemoryDetailsModel> getBiodata_list() {
        return biodata_list;
    }

    public void setBiodata_list(ArrayList<BiodataMemoryDetailsModel> biodata_list) {
        this.biodata_list = biodata_list;
    }

    public ArrayList<BiodataMemoryDetailsModel> getEvents_list() {
        return events_list;
    }

    public void setEvents_list(ArrayList<BiodataMemoryDetailsModel> events_list) {
        this.events_list = events_list;
    }

    public ArrayList<RelationDetailsModel> getRelation_list() {
        return relation_list;
    }

    public void setRelation_list(ArrayList<RelationDetailsModel> relation_list) {
        this.relation_list = relation_list;
    }

    public ArrayList<ImageFileModel> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<ImageFileModel> files) {
        this.files = files;
    }

}

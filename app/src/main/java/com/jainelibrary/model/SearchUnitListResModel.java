package com.jainelibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchUnitListResModel {

    boolean status;
    String message;

    UnitListMetaData meta_data;

    ArrayList<Unit> data = new ArrayList<>();

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


    public ArrayList<Unit> getData() {
        return data;
    }

    public void setData(ArrayList<Unit> data) {
        this.data = data;
    }

    public UnitListMetaData getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(UnitListMetaData meta_data) {
        this.meta_data = meta_data;
    }

    public static class UnitListMetaData {
        Integer found_unit_count;
        Integer result_founds;
        Integer found_biodata_ids_count;
        Integer found_event_ids_count;
        Integer found_relation_ids_count;


        Integer total_pages;

        public Integer getFound_unit_count() {
            return found_unit_count;
        }

        public void setFound_unit_count(Integer found_unit_count) {
            this.found_unit_count = found_unit_count;
        }

        public Integer getResult_founds() {
            return result_founds;
        }

        public void setResult_founds(Integer result_founds) {
            this.result_founds = result_founds;
        }

        public Integer getFound_biodata_ids_count() {
            return found_biodata_ids_count;
        }

        public void setFound_biodata_ids_count(Integer found_biodata_ids_count) {
            this.found_biodata_ids_count = found_biodata_ids_count;
        }

        public Integer getFound_event_ids_count() {
            return found_event_ids_count;
        }

        public void setFound_event_ids_count(Integer found_event_ids_count) {
            this.found_event_ids_count = found_event_ids_count;
        }

        public Integer getFound_relation_ids_count() {
            return found_relation_ids_count;
        }

        public void setFound_relation_ids_count(Integer found_relation_ids_count) {
            this.found_relation_ids_count = found_relation_ids_count;
        }

        public Integer getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(Integer total_pages) {
            this.total_pages = total_pages;
        }
    }

    public class Unit implements Serializable {
        String id;
        String sequence;
        String number;
        String type_id;
        String name;
        String name_english;
        String type_name;
        String status_name;
        String sect_name;

        String unit_details;

        String biodata_count;
        String event_count;
        String relation_count;

        String format_relation_count;
        String format_biodata_count;
        String format_event_count;


        ArrayList<String> found_biodata_ids;
        ArrayList<String>  found_event_ids;
        ArrayList<String> found_relation_ids;

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

        public String getUnit_details() {
            return unit_details;
        }

        public void setUnit_details(String unit_details) {
            this.unit_details = unit_details;
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

        public String getFormat_relation_count() {
            return format_relation_count;
        }

        public void setFormat_relation_count(String format_relation_count) {
            this.format_relation_count = format_relation_count;
        }

        public String getFormat_biodata_count() {
            return format_biodata_count;
        }

        public void setFormat_biodata_count(String format_biodata_count) {
            this.format_biodata_count = format_biodata_count;
        }

        public String getFormat_event_count() {
            return format_event_count;
        }

        public void setFormat_event_count(String format_event_count) {
            this.format_event_count = format_event_count;
        }

        public ArrayList<String> getFound_biodata_ids() {
            return found_biodata_ids;
        }

        public void setFound_biodata_ids(ArrayList<String> found_biodata_ids) {
            this.found_biodata_ids = found_biodata_ids;
        }

        public ArrayList<String> getFound_event_ids() {
            return found_event_ids;
        }

        public void setFound_event_ids(ArrayList<String> found_event_ids) {
            this.found_event_ids = found_event_ids;
        }

        public ArrayList<String> getFound_relation_ids() {
            return found_relation_ids;
        }

        public void setFound_relation_ids(ArrayList<String> found_relation_ids) {
            this.found_relation_ids = found_relation_ids;
        }
    }

}

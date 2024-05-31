package com.jainelibrary.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ParamparaFilterDataResModel {

    boolean status;
    String message;
    ArrayList<UnitType> unit_types = new ArrayList<>();

    ArrayList<UnitStatusType> status_types = new ArrayList<>();

    ArrayList<UnitSectCommunityType> sect_community_types = new ArrayList<>();

    ArrayList<SamvatType> samvat_types = new ArrayList<SamvatType>();

    Integer samvat_type;
    ArrayList<Samvat> samvats = new ArrayList<Samvat>();
    ArrayList<SamvatMonth> samvat_months = new ArrayList<SamvatMonth>();

    ArrayList<SamvatTithi> samvat_tithis = new ArrayList<SamvatTithi>();

    ArrayList<RelationType> relation_types = new ArrayList<RelationType>();

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


    public ArrayList<UnitType> getUnit_types() {
        return unit_types;
    }


    public void setUnit_types(ArrayList<UnitType> unit_types) {
        this.unit_types = unit_types;
    }

    public ArrayList<UnitStatusType> getStatus_types() {
        return status_types;
    }


    public void setStatus_types(ArrayList<UnitStatusType> status_types) {
        this.status_types = status_types;
    }


    public ArrayList<UnitSectCommunityType> getSect_community_types() {
        return sect_community_types;
    }


    public void setSect_community_types(ArrayList<UnitSectCommunityType> sect_community_types) {
        this.sect_community_types = sect_community_types;
    }

    public ArrayList<SamvatType> getSamvat_types() {
        return samvat_types;
    }

    public void setSamvat_types(ArrayList<SamvatType> samvat_types) {
        this.samvat_types = samvat_types;
    }

    public ArrayList<RelationType> getRelation_types() {
        return relation_types;
    }

    public void setRelation_types(ArrayList<RelationType> relation_types) {
        this.relation_types = relation_types;
    }

    public Integer getSamvat_type() {
        return samvat_type;
    }

    public void setSamvat_type(Integer samvat_type) {
        this.samvat_type = samvat_type;
    }

    public ArrayList<Samvat> getSamvats() {
        return samvats;
    }

    public void setSamvats(ArrayList<Samvat> samvats) {
        this.samvats = samvats;
    }

    public ArrayList<SamvatMonth> getSamvat_months() {
        return samvat_months;
    }

    public void setSamvat_months(ArrayList<SamvatMonth> samvat_months) {
        this.samvat_months = samvat_months;
    }

    public ArrayList<SamvatTithi> getSamvat_tithis() {
        return samvat_tithis;
    }

    public void setSamvat_tithis(ArrayList<SamvatTithi> samvat_tithis) {
        this.samvat_tithis = samvat_tithis;
    }

    public static class Type  implements Serializable {

        int id;
        String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String toString() {
            return getId() + " | " + getName();
        }
    }

    public static class UnitType extends Type {

    }

    public static class UnitStatusType extends Type {

    }


    public static class UnitSectCommunityType extends Type {

    }

    public static class  SamvatType extends Type {
        String  only_year;
        String code;

        public boolean isOnly_year() {
            return only_year.equals("1");
        }

        public String getOnly_year() {
            return only_year;
        }

        public void setOnly_year(String only_year) {
            this.only_year = only_year;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class  Samvat extends Type {
        String samvat_name;
        String  samvat_type;
        String samvat_type_name;

        public String getSamvat_name() {
            return samvat_name;
        }

        public void setSamvat_name(String samvat_name) {
            this.samvat_name = samvat_name;
        }

        public String getSamvat_type() {
            return samvat_type;
        }

        public void setSamvat_type(String samvat_type) {
            this.samvat_type = samvat_type;
        }

        public String getSamvat_type_name() {
            return samvat_type_name;
        }

        public void setSamvat_type_name(String samvat_type_name) {
            this.samvat_type_name = samvat_type_name;
        }

        public String getSamvatFormatedName() {
            if (this.getSamvat_name() !=null && this.getSamvat_type_name() != null )
                return this.getSamvat_name() + " - " + this.getSamvat_type_name();
            return  this.getName();
        }
    }

    public static class  SamvatMonth extends Type {

    }

    public static class  SamvatTithi extends Type {

    }


    public static class RelationType extends Type {

    }
}

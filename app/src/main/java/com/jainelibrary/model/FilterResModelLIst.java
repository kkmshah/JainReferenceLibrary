package com.jainelibrary.model;

public class FilterResModelLIst {

    String id, name,filtername;

    public FilterResModelLIst(String id,String name) {
        this.id = id;
        this.name = name;
    }

    public FilterResModelLIst() {
    }

    public String getFiltername() {
        return filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }

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
}

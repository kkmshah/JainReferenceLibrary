/*
package com.jainelibrary.CheckedList;

import android.os.Parcel;
import android.os.Parcelable;

public class Mensa implements Parcelable {

    private String name;
    private String geoTag;

    public Mensa(String name, String geoTag) {
        this.name = name;
        this.geoTag = geoTag;
    }

    protected Mensa(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getGeoTag() {
        return geoTag;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Mensa)) return false;

        Mensa mensa = (Mensa) obj;

        return getName() != null ? getName().equals(mensa.getName()) : mensa.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mensa> CREATOR = new Creator<Mensa>() {
        @Override
        public Mensa createFromParcel(Parcel in) {
            return new Mensa(in);
        }

        @Override
public Mensa[] newArray(int size) {
    return new Mensa[size];
}
};
}*/

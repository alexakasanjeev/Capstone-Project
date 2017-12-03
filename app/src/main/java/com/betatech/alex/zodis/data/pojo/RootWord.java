package com.betatech.alex.zodis.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 12/4/2017.
 */

public class RootWord implements Parcelable {

    private long _id;
    private String rootWordName;
    private String rootWordDescription;

    public RootWord(long _id, String rootWordName, String rootWordDescription) {
        this._id = _id;
        this.rootWordName = rootWordName;
        this.rootWordDescription = rootWordDescription;
    }

    protected RootWord(Parcel in) {
        _id = in.readLong();
        rootWordName = in.readString();
        rootWordDescription = in.readString();
    }

    public static final Creator<RootWord> CREATOR = new Creator<RootWord>() {
        @Override
        public RootWord createFromParcel(Parcel in) {
            return new RootWord(in);
        }

        @Override
        public RootWord[] newArray(int size) {
            return new RootWord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(rootWordName);
        dest.writeString(rootWordDescription);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getRootWordName() {
        return rootWordName;
    }

    public void setRootWordName(String rootWordName) {
        this.rootWordName = rootWordName;
    }

    public String getRootWordDescription() {
        return rootWordDescription;
    }

    public void setRootWordDescription(String rootWordDescription) {
        this.rootWordDescription = rootWordDescription;
    }
}

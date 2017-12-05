package com.betatech.alex.zodis.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 12/4/2017.
 */

public class DerivedWord implements Parcelable {

    private String derivedWordName;
    private String derivedWordDescription;

    public DerivedWord(String derivedWordName, String derivedWordDescription) {
        this.derivedWordName = derivedWordName;
        this.derivedWordDescription = derivedWordDescription;
    }

    private void readFromParcel(Parcel in) {
        derivedWordName = in.readString();
        derivedWordDescription = in.readString();
    }

    private DerivedWord(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<DerivedWord> CREATOR = new Creator<DerivedWord>() {
        @Override
        public DerivedWord createFromParcel(Parcel in) {
            return new DerivedWord(in);
        }

        @Override
        public DerivedWord[] newArray(int size) {
            return new DerivedWord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(derivedWordName);
        dest.writeString(derivedWordDescription);
    }


    public String getDerivedWordName() {
        return derivedWordName;
    }


    public String getDerivedWordDescription() {
        return derivedWordDescription;
    }

}

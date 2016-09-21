package com.example.pavolm.pesnicky;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * Created by pavolm on 9/17/16.
 */
public class Song implements Parcelable {
    public String name;
    public @DrawableRes int icon;
    public Bitmap bitmap;
    public String notes;

    public Song(String name, @DrawableRes int icon, String notes) {
        this.name = name;
        this.icon = icon;
        this.notes = notes;
        this.bitmap = null;
    }

    public Song(String name, Bitmap bitmap, String notes) {
        this.name = name;
        this.icon = 0;
        this.bitmap = bitmap;
        this.notes = notes;
    }

    public Song(Parcel in) {
        this.name = in.readString();
        this.icon = in.readInt();
        this.notes = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(icon);
        parcel.writeString(notes);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Song createFromParcel(Parcel in) {
                    return new Song(in);
                }

                public Song[] newArray(int size) {
                    return new Song[size];
                }
            };
}


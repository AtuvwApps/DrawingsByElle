package com.atuvwapps.drawingsbyelle.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Drawing implements Parcelable {
    @SerializedName("Name")
    private String name;
    @SerializedName("ImageSource")
    private String imageSource;
    @SerializedName("Price")
    private int price;

    public Drawing(){}

    public Drawing(String name, String imageSource, int price){
        this.name = name;
        this.imageSource = imageSource;
        this.price = price;
    }

    protected Drawing(Parcel in){
        name = in.readString();
        imageSource = in.readString();
        price = in.readInt();
    }

    public static final Creator<Drawing> CREATOR = new Creator<Drawing>() {
        @Override
        public Drawing createFromParcel(Parcel in) {
            return new Drawing(in);
        }

        @Override
        public Drawing[] newArray(int size) {
            return new Drawing[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageSource);
        dest.writeInt(price);
    }
}

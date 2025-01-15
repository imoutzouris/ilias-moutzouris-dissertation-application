package com.example.iliasdissertation.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ItemModel implements Parcelable {
    private String description;
    private String imageUrl;
    private String name;
    private double price;

    public ItemModel(String description, String imageUrl, String name, double price) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    protected ItemModel(Parcel in) {
        description = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        price = in.readDouble();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeDouble(price);
    }
}


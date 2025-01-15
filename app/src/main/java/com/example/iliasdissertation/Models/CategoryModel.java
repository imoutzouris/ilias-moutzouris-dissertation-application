package com.example.iliasdissertation.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class CategoryModel implements Parcelable {
    String name;
    String imageURL;

    List<ItemModel> items;

    public CategoryModel(String name, String imageURL, List<ItemModel> items) {
        this.name = name;
        this.imageURL = imageURL;
        this.items = items;
    }

    protected CategoryModel(Parcel in) {
        name = in.readString();
        imageURL = in.readString();
        items = in.createTypedArrayList(ItemModel.CREATOR);
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return imageURL;
    }

    public void setImage(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageURL);
        dest.writeTypedList(items);
    }
}

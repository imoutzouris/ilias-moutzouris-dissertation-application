package com.example.iliasdissertation.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CartModel implements Parcelable {
    private String name;
    private String description;
    private String notes;
    private double price;
    private int quantity;
    private double priceTimesQuantity;
    private String imageURL;

    public CartModel(String name, String description, String notes, double price, int quantity, double priceTimesQuantity, String imageURL) {
        this.name = name;
        this.description= description;
        this.notes = notes;
        this.price = price;
        this.quantity = quantity;
        this.priceTimesQuantity = priceTimesQuantity;
        this.imageURL = imageURL;
    }

    protected CartModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        notes = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        priceTimesQuantity = in.readDouble();
        imageURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(notes);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeDouble(priceTimesQuantity);
        dest.writeString(imageURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public String getCartItemName() {
        return name;
    }

    public void setCartItemName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCartItemNotes() {
        return notes;
    }

    public void setCartItemNotes(String notes) {
        this.notes = notes;
    }

    public double getCartItemPrice() {
        return price;
    }

    public void setCartItemPrice(double price) {
        this.price = price;
    }

    public int getCartItemQuantity() {
        return quantity;
    }

    public void setCartItemQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceTimesQuantity() {
        return priceTimesQuantity;
    }

    public void setPriceWithQuantity(double priceWithQuantity) {
        this.priceTimesQuantity = priceWithQuantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

package com.example.iliasdissertation.SQLiteDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "orders", indices = {@Index(value = {"name", "description", "notes", "price"}, unique = true)})
public class OrderEntity {

    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="description")
    public String description;

    @ColumnInfo(name="notes")
    public String notes;

    @ColumnInfo(name="price")
    public double price;

    @ColumnInfo(name="quantity")
    public int quantity;

    @ColumnInfo(name="total")
    public double total;

    @ColumnInfo(name="imageUrl")
    private String imageUrl;

    public OrderEntity(String name, String description, String notes, double price, int quantity, double total, String imageUrl) {
        this.name = name;
        this.description = description;
        this.notes = notes;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.imageUrl = imageUrl;
    }

    public String getOrderName() {
        return name;
    }
    public String getDescription() { return description; }

    public String getOrderNotes() {
        return notes;
    }

    public double getOrderPrice() { return price; }
    public int getOrderQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public String getImageUrl() { return imageUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        boolean equals = Double.compare(that.price, price) == 0 &&
                quantity == that.quantity &&
                Double.compare(that.total, total) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(imageUrl, that.imageUrl);
        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, notes, price, quantity, total, imageUrl);
    }
}

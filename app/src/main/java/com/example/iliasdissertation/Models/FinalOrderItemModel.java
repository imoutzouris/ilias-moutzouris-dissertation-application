package com.example.iliasdissertation.Models;

public class FinalOrderItemModel {
    private String name;
    private String notes;
    private int quantity;

    public FinalOrderItemModel(String name, String notes, int quantity) {
        this.name = name;
        this.notes = notes;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public int getQuantity() {
        return quantity;
    }
}

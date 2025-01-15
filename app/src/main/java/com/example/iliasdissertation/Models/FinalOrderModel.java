package com.example.iliasdissertation.Models;

import java.util.List;

public class FinalOrderModel {
    List<FinalOrderItemModel> finalOrderItems;
    String tableNumber;

    public FinalOrderModel(List<FinalOrderItemModel> finalOrderItems, String tableNumber) {
        this.finalOrderItems = finalOrderItems;
        this.tableNumber = tableNumber;
    }

    public List<FinalOrderItemModel> getFinalOrderItems() {
        return finalOrderItems;
    }

    public String getTableNumber() {
        return tableNumber;
    }
}

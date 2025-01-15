package com.example.iliasdissertation.SQLiteDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDao {
    //Create
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insert(OrderEntity order);

    //Read
    @Query("SELECT * FROM orders")
    public List<OrderEntity> get();

    //Delete
    @Query("DELETE FROM orders")
    public void deleteAll();

    @Query("Delete FROM orders WHERE name = :orderedItemName AND description = :orderedItemDescription " +
            "AND notes = :orderedItemNotes AND price = :orderedItemPrice AND quantity = :orderedItemQuantity " +
            "AND total = :orderedItemTotal")
    public void delete(String orderedItemName, String orderedItemDescription, String orderedItemNotes,
                       double orderedItemPrice, int orderedItemQuantity, double orderedItemTotal);

    @Query("UPDATE orders SET quantity = :updatedItemQuantity, notes = :updatedItemNotes, " +
            "total = :updatedItemTotal WHERE " +
            "name = :orderedItemName AND description = :orderedItemDescription " +
            "AND notes = :orderedItemNotes AND price = :orderedItemPrice AND " +
            "quantity = :orderedItemQuantity " + "AND total = :orderedItemTotal")
    public void update(String orderedItemName, String orderedItemDescription, String orderedItemNotes,
                       double orderedItemPrice, int orderedItemQuantity, double orderedItemTotal, int
                                   updatedItemQuantity, String updatedItemNotes, double updatedItemTotal);
}

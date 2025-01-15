package com.example.iliasdissertation.SQLiteDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {OrderEntity.class}, version = 1)
public abstract class DatabaseInstance extends RoomDatabase {
    public abstract OrderDao orderDao();
    private static DatabaseInstance myDb;

    public static DatabaseInstance getDatabase(Context context) {
        if(myDb == null) {
            synchronized (DatabaseInstance.class){
                if(myDb ==null){
                    myDb = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseInstance.class, "mydb").allowMainThreadQueries().build();
                }
            }
        }
        return myDb;
    }
}

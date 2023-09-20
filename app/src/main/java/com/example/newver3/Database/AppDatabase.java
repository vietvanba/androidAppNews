package com.example.newver3.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.newver3.Daointerface.BaiBaoDAO;

@Database(entities ={BaiBao.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static com.example.newver3.Database.AppDatabase INSTANCE;
    public abstract BaiBaoDAO baiBaoDao();
    public static com.example.newver3.Database.AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), com.example.newver3.Database.AppDatabase.class,"news_Database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}

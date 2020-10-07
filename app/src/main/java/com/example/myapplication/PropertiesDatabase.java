package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.pojo.properties;


@Database(entities = properties.class,version = 1)
public abstract class PropertiesDatabase extends RoomDatabase {
    private static PropertiesDatabase instance;
    public abstract PropertiesDAO propertiesDAO();

    public static synchronized PropertiesDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext()
                    ,PropertiesDatabase.class,"Properties_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

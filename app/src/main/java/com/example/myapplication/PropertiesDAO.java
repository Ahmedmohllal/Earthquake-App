package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.pojo.properties;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface PropertiesDAO  {

    @Insert
    Completable insertData(List<properties> properties);

    @Query("select * from properties_table")
    Observable<List<properties>> getAllData();
}

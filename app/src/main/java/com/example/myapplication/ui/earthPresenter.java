package com.example.myapplication.ui;

import com.example.myapplication.pojo.properties;

import java.util.ArrayList;

public class earthPresenter {
    com.example.myapplication.ui.earthView earthView;

    public earthPresenter(earthView earthView) {
        this.earthView = earthView;
    }

    //return list of data from model
    public ArrayList<properties> getList(ArrayList<properties> arrayList){
        return arrayList;
    }

    //send data to interface class
    public void getAllData(ArrayList<properties> arrayList){
        earthView.onGetEarth(getList(arrayList));
    }
}

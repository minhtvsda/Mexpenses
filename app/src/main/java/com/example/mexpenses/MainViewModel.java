package com.example.mexpenses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpenses.data.TripEntity;
import com.example.mexpenses.sqlite.Database;
import com.example.mexpenses.sqlite.RoomH;


import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel
{


    MutableLiveData<List<TripEntity>> tripList = new MutableLiveData<List<TripEntity>>();

    Database db;

    public MutableLiveData<List<TripEntity>> getData(){
        List<TripEntity> te = db.createTripDao().getAllTrips();
        tripList.setValue(te);
        return tripList;
    }

    public void setDatabase(Database db) {
        this.db = db;
    }










}
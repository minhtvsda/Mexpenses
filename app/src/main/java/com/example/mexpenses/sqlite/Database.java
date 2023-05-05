package com.example.mexpenses.sqlite;

import androidx.room.RoomDatabase;

import com.example.mexpenses.data.ExpenseEntity;
import com.example.mexpenses.data.TripDao;
import com.example.mexpenses.data.TripEntity;

@androidx.room.Database(entities = {TripEntity.class, ExpenseEntity.class} , version = 1)
public abstract class Database extends RoomDatabase {
    public abstract TripDao createTripDao();
}

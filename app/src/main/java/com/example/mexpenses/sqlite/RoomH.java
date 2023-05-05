package com.example.mexpenses.sqlite;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mexpenses.data.ExpenseEntity;
import com.example.mexpenses.data.TripDao;
import com.example.mexpenses.data.TripEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RoomH {

    public static  Database createDatabase(Context context){

        return Room.databaseBuilder(context, Database.class, "alltrip").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        //To prevent queries from blocking the UI, Room does not allow database access on the main thread
    }
}


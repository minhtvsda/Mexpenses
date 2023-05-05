package com.example.mexpenses.data;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {
    @Insert
    void insert(TripEntity... tripEntity);
    @Delete

    void delete(TripEntity tripEntity);

    @Update

    void update(TripEntity tripEntity);

    @Query("SELECT * FROM tripEntity")
    List<TripEntity> getAllTrips();

    @Query("SELECT * FROM tripEntity WHERE id = :id")
    TripEntity getTrip(int id);

    @Query("SELECT * FROM expenseEntity WHERE tripId = :tripid")
    List<ExpenseEntity> getExpense(int tripid);
    @Insert
    void insertExpense(ExpenseEntity... expenseEntity);
    @Delete

    void deleteExpense(ExpenseEntity expenseEntity);

    @Update

    void updateExpense(ExpenseEntity expenseEntity);

    @Query("SELECT * FROM expenseEntity WHERE id = :id")
    ExpenseEntity getExpenseById (int id);

    @Query("DELETE FROM TripEntity")
    void deleteAllTrip();
    @Query("DELETE FROM ExpenseEntity")
    void deleteAllExpense();

}

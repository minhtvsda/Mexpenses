package com.example.mexpenses;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpenses.data.ExpenseEntity;
import com.example.mexpenses.data.TripEntity;
import com.example.mexpenses.sqlite.Database;

public class EditorexViewModel extends ViewModel {

    MutableLiveData<ExpenseEntity> expense = new MutableLiveData<ExpenseEntity>();

    Database db;

    public void updateExpense(ExpenseEntity updateExpense) {
        if(updateExpense.getId() == 0 ){
            db.createTripDao().insertExpense(updateExpense);
        }else {
            db.createTripDao().updateExpense(updateExpense);
        }

    }
    public void deleteExpense() {
        db.createTripDao().deleteExpense(expense.getValue());
    }
    public void setDatabase(Database db) {
        this.db = db;
    }

    public void getExpenseById(int expenseId) {
        if (expenseId == 0){
            //th add new
            expense.setValue(new ExpenseEntity());
            return;
        }
        expense.setValue(db.createTripDao().getExpenseById(expenseId));
    }

}
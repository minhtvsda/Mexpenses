package com.example.mexpenses;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mexpenses.data.ExpenseEntity;
import com.example.mexpenses.data.TripEntity;
import com.example.mexpenses.sqlite.Database;

import java.util.List;

public class ExpenseViewModel extends ViewModel {
    // TODO: Implement the ViewModel
        MutableLiveData<List<ExpenseEntity>> expenseList = new MutableLiveData<List<ExpenseEntity>>();
        Database db;
        NavController navController;

    public MutableLiveData<List<ExpenseEntity>> getData(int tripid){
            List<ExpenseEntity> ee = db.createTripDao().getExpense(tripid);
            expenseList.setValue(ee);
            return expenseList;
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    public void setController(NavController navController) {
        this.navController = navController;
    }

    public void onItemClick(int expenseId, int tripId) {
        Bundle bundle = new Bundle();
        bundle.putInt("expenseId", expenseId);
        bundle.putInt("tripId", tripId);
        navController.navigate(R.id.editorexFragment, bundle);
    }

    public void setView(ConstraintLayout root) {

    }
}
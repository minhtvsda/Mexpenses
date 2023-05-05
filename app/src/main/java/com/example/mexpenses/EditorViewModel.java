package com.example.mexpenses;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpenses.data.TripEntity;
import com.example.mexpenses.sqlite.Database;

public class EditorViewModel extends ViewModel {

    // TODO: Implement the ViewModel
    MutableLiveData<TripEntity> trip = new MutableLiveData<TripEntity>();//Ta sẽ chỉ cần hiển thị 1 trip, nên:

    Database db;

    public void updateTrip(TripEntity updateTrip) {
        if (updateTrip.getId() == 0){
            db.createTripDao().insert(updateTrip);
        } else {
        db.createTripDao().update(updateTrip);}
    }
    public void deleteTrip() {
        db.createTripDao().delete(trip.getValue());
    }
    public void getTripByID(int id){//Do có id rồi nên ta sẽ dùng tạo ra method getTripById() và lấy dữ liệu từ sqlite.
        if (id == 0){
            trip.setValue(new TripEntity());
            return;
        }
        trip.setValue(db.createTripDao().getTrip(id));
    }



    public void setDatabase(Database db) {
        this.db = db;
    }

}
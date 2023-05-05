package com.example.mexpenses.data;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mexpenses.Constants;

@Entity
public class ExpenseEntity {
        @PrimaryKey(autoGenerate = true)
        public int id;
        @ColumnInfo
        public int tripId;
        @ColumnInfo

        public String type;
        @ColumnInfo
        public String amount;
        @ColumnInfo

        public String time;
        @ColumnInfo

        public String comments;
        @ColumnInfo
        public String imageUrl;


    public ExpenseEntity() {
        this(0,0, Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY);
    }
    public ExpenseEntity(int tripId, String type, String amount, String time, String comments, String imageUrl) {
        this.id = 0;
        this.tripId = tripId;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.comments = comments;
        this.imageUrl = imageUrl;
    }

    public ExpenseEntity(int id, int tripId, String type, String amount, String time, String comments, String imageUrl) {
        this.id = id;
        this.tripId = tripId;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.comments = comments;
        this.imageUrl = imageUrl;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripOwnerId(int tripOwnerId) {
        this.tripId = tripOwnerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}


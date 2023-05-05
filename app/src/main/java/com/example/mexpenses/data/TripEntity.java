package com.example.mexpenses.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mexpenses.Constants;
@Entity
public class TripEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String destination;
    @ColumnInfo
    public String dot;
    @ColumnInfo
    public String rriskasses;
    @ColumnInfo
    public String description;
    @ColumnInfo
    public String imageUrl;


    public TripEntity (){
        this(   0,Constants.EMPTY,
                Constants.EMPTY,
                Constants.EMPTY,
                Constants.EMPTY,
                Constants.EMPTY,
                Constants.EMPTY
        );
    }
    public TripEntity(String name,String destination, String dot, String rriskasses, String description,String imageUrl) {
        this(0,name, destination, dot, rriskasses, description, imageUrl);
    }

    public TripEntity (int id,String name,String destination, String dot, String rriskasses, String description, String imageUrl){
        setId(id);
        setName(name);
        setDestination(destination);
        setDot(dot);
        setRrisk(rriskasses);
        setDescription(description);
        setImageUrl(imageUrl);
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

    public String getName(){return name;}

    public void setName(String name) { this.name = name;
    }
    public String getDestination(){return destination;}

    public void setDestination(String destination) { this.destination = destination;
    }
    public String getDot(){return dot;}

    public void setDot(String dot) { this.dot = dot;
    }
    public String getRrisk(){return rriskasses;}

    public void setRrisk(String rriskasses) { this.rriskasses = rriskasses;
    }
    public String getDescription(){return description;}

    public void setDescription(String destination) { this.description = destination;
    }

}


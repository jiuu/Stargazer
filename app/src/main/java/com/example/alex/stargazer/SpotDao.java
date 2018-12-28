package com.example.alex.stargazer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;



@Dao
public interface SpotDao {

    @Query("SELECT * FROM Spot")
    List<Spot> getAllSpots();

    @Insert
    void addSpot(Spot spot);

    @Delete
    void deleteSpot(Spot spot);

    @Update
    void updateSpot(Spot spot);

    @Query("DELETE FROM Spot")
    public void dropTheTable();
}

package com.example.mynotes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DataDao {

    @Insert
    long insert(Data data);

    @Query("SELECT * FROM Data")
    List<Data> getdata();

   /* @Delete
    int delete(Data data);*/

    @Query("DELETE FROM Data WHERE id = :id")
    int delete(long id);

    /*@Update
    int update(Data data);*/

    @Query("UPDATE Data set title = :title,note = :note WHERE id = :id")
    int update(long id, String title, String note);

    @Query("SELECT * FROM Data WHERE id = :id")
    List<Data> getonedata(long id);
}

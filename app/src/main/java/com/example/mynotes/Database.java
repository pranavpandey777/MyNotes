package com.example.mynotes;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@android.arch.persistence.room.Database(entities = {Data.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public static final String DatabaseName = "Notebook";
    public static Database DATABASE;

    // public static volatile Database INSTANCE;
    abstract DataDao dao();

    public static Database getUserDatabaseInstance(Context context) {
        if (DATABASE == null) {
            DATABASE = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class,
                    DatabaseName).build();
        }
        return DATABASE;
    }

}

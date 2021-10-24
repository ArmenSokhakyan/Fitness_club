package com.armensokhakyan.fitnessclub.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Schedule.class}, version = 1, exportSchema = false)
public abstract class ScheduleDatabase extends RoomDatabase {

    private static ScheduleDatabase database;
    private static final String DB_NAME = "schedules.db";
    private static final Object LOCK = new Object();

    public static ScheduleDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, ScheduleDatabase.class, DB_NAME).build();
            }
        }
        return database;
    }

    public abstract ScheduleDao scheduleDao();

}

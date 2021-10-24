package com.armensokhakyan.fitnessclub.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM schedules ORDER BY weekDay")
    LiveData<List<Schedule>> getAllSchedules();

    @Query("SELECT * FROM schedules WHERE appointment_id = :appointmentId")
    Schedule getScheduleByAppointmentId(String appointmentId);

    @Query("DELETE FROM schedules")
    void deleteAllSchedules();

    @Insert
    void insertSchedule(Schedule schedule);

    @Delete
    void deleteSchedule(Schedule schedule);

}

package com.armensokhakyan.fitnessclub.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static ScheduleDatabase database;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = ScheduleDatabase.getInstance(application);
    }

    public static MainViewModel getViewModel(ViewModelStoreOwner viewModelStoreOwner){
        return new ViewModelProvider(viewModelStoreOwner).get(MainViewModel.class);
    }

    public LiveData<List<Schedule>> getAllSchedules() {
        return database.scheduleDao().getAllSchedules();
    }

    //get by AppointmentId
    public Schedule getScheduleByAppointmentId(String appointmentId) {
        try {
            return new GetScheduleTask().execute(appointmentId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class GetScheduleTask extends AsyncTask<String, Void, Schedule> {

        @Override
        protected Schedule doInBackground(String... strings) {
            if (strings != null && strings.length > 0) {
                return database.scheduleDao().getScheduleByAppointmentId(strings[0]);
            }

            return null;
        }
    }

    //add schedule
    public void insertSchedule(Schedule schedule) {
        new InsertScheduleTask().execute(schedule);
    }

    private static class InsertScheduleTask extends AsyncTask<Schedule, Void, Void> {

        @Override
        protected Void doInBackground(Schedule... schedules) {
            if (schedules != null && schedules.length > 0) {
                database.scheduleDao().insertSchedule(schedules[0]);
            }

            return null;
        }
    }

    //delete schedule
    public void deleteSchedule(Schedule schedule) {
        new DeleteScheduleTask().execute(schedule);
    }

    private static class DeleteScheduleTask extends AsyncTask<Schedule, Void, Void> {

        @Override
        protected Void doInBackground(Schedule... schedules) {
            if (schedules != null && schedules.length > 0) {
                database.scheduleDao().deleteSchedule(schedules[0]);
            }

            return null;
        }
    }

    //delete all schedules
    public void deleteAllSchedules() {
        new DeleteAllSchedulesTask().execute();
    }

    private static class DeleteAllSchedulesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... schedules) {
            database.scheduleDao().deleteAllSchedules();
            return null;
        }
    }

}

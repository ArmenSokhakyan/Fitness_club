package com.armensokhakyan.fitnessclub.util;

import com.armensokhakyan.fitnessclub.data.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    private static final String KEY_NAME = "name";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_PLACE = "place";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_WEEK_DAY = "weekDay";
    private static final String KEY_TEACHER = "teacher";
    private static final String KEY_APPOINTMENT_ID = "appointment_id";

    public static ArrayList<Schedule> getSchedulesFromJSON(JSONArray jsonArray) {

        ArrayList<Schedule> schedules = new ArrayList<>();

        if (jsonArray == null) {
            return schedules;
        }

        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString(KEY_NAME);
                String startTime = jsonObject.getString(KEY_START_TIME);
                String endTime = jsonObject.getString(KEY_END_TIME);
                String place = jsonObject.getString(KEY_PLACE);
                String description = jsonObject.getString(KEY_DESCRIPTION);
                int weekDay = jsonObject.getInt(KEY_WEEK_DAY);
                String teacher = jsonObject.getString(KEY_TEACHER);
                String appointmentId = jsonObject.getString(KEY_APPOINTMENT_ID);

                Schedule schedule = new Schedule(appointmentId, name, startTime, endTime, teacher, place, description, weekDay);
                schedules.add(schedule);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return schedules;

    }

}

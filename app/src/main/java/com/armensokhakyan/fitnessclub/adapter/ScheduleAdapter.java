package com.armensokhakyan.fitnessclub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armensokhakyan.fitnessclub.R;
import com.armensokhakyan.fitnessclub.data.Schedule;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleAdapterHolder> {

    private List<Schedule> schedules;

    public ScheduleAdapter() {
        this.schedules = new ArrayList<>();
    }

    @NonNull
    @Override
    public ScheduleAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ScheduleAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapterHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.textViewWeekDay.setText(schedule.getWeekDayAsString());
        holder.textViewName.setText(schedule.getName());
        holder.textViewPlace.setText(schedule.getPlace());
        holder.textViewTime.setText(String.format("%s - %s", schedule.getStartTime(), schedule.getEndTime()));
        holder.textViewTeacher.setText(schedule.getTeacher());

    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    protected class ScheduleAdapterHolder extends RecyclerView.ViewHolder {

        private TextView textViewWeekDay;
        private TextView textViewName;
        private TextView textViewTime;
        private TextView textViewTeacher;
        private TextView textViewPlace;

        public ScheduleAdapterHolder(@NonNull View itemView) {
            super(itemView);
            textViewWeekDay = itemView.findViewById(R.id.textViewWeekDay);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewTeacher = itemView.findViewById(R.id.textViewTeacher);
            textViewPlace = itemView.findViewById(R.id.textViewPlace);
        }
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    public void addSchedules(List<Schedule> schedules){
        this.schedules.addAll(schedules);
        notifyDataSetChanged();
    }
}

package com.armensokhakyan.fitnessclub;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.armensokhakyan.fitnessclub.adapter.ScheduleAdapter;
import com.armensokhakyan.fitnessclub.data.MainViewModel;
import com.armensokhakyan.fitnessclub.data.Schedule;
import com.armensokhakyan.fitnessclub.util.JSONUtils;
import com.armensokhakyan.fitnessclub.util.NetworkUtils;

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //interface
    private RecyclerView recyclerViewSchedules;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRecyclerViewSchedule;

    private ScheduleAdapter scheduleAdapter;
    private LoaderManager loaderManager;
    private MainViewModel viewModel;

    private static final int LOADER_KEY = 1212;
    private static final String WAS_LOADED_KEY = "IS_LOADING_KEY";
    private static boolean wasLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(WAS_LOADED_KEY)) {
            wasLoaded = savedInstanceState.getBoolean(WAS_LOADED_KEY);
        }
        init();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAS_LOADED_KEY, wasLoaded);
    }

    private void init() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        //initializing
        recyclerViewSchedules = findViewById(R.id.recyclerViewSchedules);
        progressBar = findViewById(R.id.progressBar);
        swipeRecyclerViewSchedule = findViewById(R.id.swipeRecyclerViewSchedule);

        scheduleAdapter = new ScheduleAdapter();
        loaderManager = LoaderManager.getInstance(this);
        viewModel = MainViewModel.getViewModel(this);

        //settings
        recyclerViewSchedules.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSchedules.setAdapter(scheduleAdapter);

        viewModel.getAllSchedules().observe(this, schedules -> {
            scheduleAdapter.setSchedules(schedules);
        });

        swipeRecyclerViewSchedule.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadData();
            }
        });

        if (!wasLoaded) {
            downloadData();
        }
    }

    private void downloadData() {
        URL url = NetworkUtils.buildURL();
        Bundle bundle = new Bundle();
        bundle.putString(NetworkUtils.KEY_FOR_BUNDLE_URL, url.toString());
        loaderManager.restartLoader(LOADER_KEY, bundle, new ScheduleLoader());
    }

    private class ScheduleLoader implements LoaderManager.LoaderCallbacks<JSONArray> {

        @NonNull
        @Override
        public Loader<JSONArray> onCreateLoader(int id, @Nullable Bundle bundle) {
            NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(getApplicationContext(), bundle);
            jsonLoader.setOnJsonLoadingListener(new NetworkUtils.JSONLoader.OnJsonLoadingListener() {
                @Override
                public void onStartLoading() {
                    if (!swipeRecyclerViewSchedule.isRefreshing()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            });
            return jsonLoader;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<JSONArray> loader, JSONArray jsonArray) {
            ArrayList<Schedule> schedules = JSONUtils.getSchedulesFromJSON(jsonArray);

            if (!schedules.isEmpty()) {
                viewModel.deleteAllSchedules();
                for (Schedule schedule : schedules) {
                    viewModel.insertSchedule(schedule);
                }
                scheduleAdapter.addSchedules(schedules);
            }

            swipeRecyclerViewSchedule.setRefreshing(false);
            progressBar.setVisibility(View.INVISIBLE);
            wasLoaded = true;
            loaderManager.destroyLoader(LOADER_KEY);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<JSONArray> loader) {

        }
    }
}


package com.armensokhakyan.fitnessclub.util;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String BASE_URL = "https://sample.fitnesskit-admin.ru/schedule/get_group_lessons_v2/1/";

    public static final String KEY_FOR_BUNDLE_URL = "KEY_FOR_BUNDLE_URL";

    public static URL buildURL() {

        URL result = null;

        Uri uri = Uri.parse(BASE_URL).buildUpon().build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;

    }

    public static class JSONLoader extends AsyncTaskLoader<JSONArray> {

        private Bundle bundle;
        private OnJsonLoadingListener onJsonLoadingListener;

        public interface OnJsonLoadingListener{
            void onStartLoading();
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
            if(onJsonLoadingListener != null){
                onJsonLoadingListener.onStartLoading();
            }
        }

        @Nullable
        @Override
        public JSONArray loadInBackground() {

            if (bundle == null) {
                return null;
            }

            String urlAsString = bundle.getString(KEY_FOR_BUNDLE_URL);
            URL url = null;

            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            JSONArray result = null;

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                StringBuilder stringBuilder = new StringBuilder();

                String line = bufferedReader.readLine();

                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }

                return new JSONArray(stringBuilder.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return null;
        }

        public void setOnJsonLoadingListener(OnJsonLoadingListener onJsonLoadingListener) {
            this.onJsonLoadingListener = onJsonLoadingListener;
        }
    }

}

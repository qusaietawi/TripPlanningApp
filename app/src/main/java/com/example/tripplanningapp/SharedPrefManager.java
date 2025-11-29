package com.example.tripplanningapp;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SharedPrefManager {

    private static final String PREFS_NAME = "trip_prefs";
    private static final String KEY_TASKS = "tasks";

    // Save tasks to SharedPreferences as JSON
    public static void saveTasks(Context context, ArrayList<Task> tasks) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (Task task : tasks) {
            jsonArray.put(task.toJSON());
        }
        prefs.edit().putString(KEY_TASKS, jsonArray.toString()).apply();
    }

    // Load tasks from SharedPreferences
    public static ArrayList<Task> loadTasks(Context context) {
        ArrayList<Task> list = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String data = prefs.getString(KEY_TASKS, null);
        if (data == null) return list;

        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Task task = Task.fromJSON(obj);
                if (task != null) list.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

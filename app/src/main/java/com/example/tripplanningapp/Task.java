package com.example.tripplanningapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

    private String id;          // unique id
    private String title;       // task title
    private String description; // task description
    private String date;        // format: yyyy-mm-dd
    private boolean important;  // is important
    private boolean completed;  // is completed
    private String type;        // type: Personal, Work, Other

    // Constructor
    public Task(String id, String title, String description, String date,
                boolean important, boolean completed, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.important = important;
        this.completed = completed;
        this.type = type;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public boolean isImportant() { return important; }
    public boolean isCompleted() { return completed; }
    public String getType() { return type; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setImportant(boolean important) { this.important = important; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setType(String type) { this.type = type; }

    // Convert Task to JSONObject for saving
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("title", title);
            obj.put("description", description);
            obj.put("date", date);
            obj.put("important", important);
            obj.put("completed", completed);
            obj.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    // Convert JSONObject to Task
    public static Task fromJSON(JSONObject obj) {
        try {
            return new Task(
                    obj.optString("id"),
                    obj.optString("title"),
                    obj.optString("description"),
                    obj.optString("date"),
                    obj.optBoolean("important"),
                    obj.optBoolean("completed"),
                    obj.optString("type")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

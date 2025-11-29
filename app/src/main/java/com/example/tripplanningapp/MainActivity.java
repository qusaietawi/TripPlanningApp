package com.example.tripplanningapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
    private ArrayList<Task> filtered;
    private RecyclerView rv;
    private TaskAdapter adapter;
    private Button btnAdd;
    private EditText etSearch;

    public static final int REQ_ADD = 1;
    public static final int REQ_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = SharedPrefManager.loadTasks(this);
        filtered = new ArrayList<>(tasks);

        rv = findViewById(R.id.recycler);
        etSearch = findViewById(R.id.etSearch);
        btnAdd = findViewById(R.id.btnAdd);

        // Restore search query if available (Lifecycle State Management)
        if (savedInstanceState != null) {
            String savedQuery = savedInstanceState.getString("SEARCH_QUERY", "");
            etSearch.setText(savedQuery);
        }

        // Setup RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, filtered, new TaskAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                Task task = filtered.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", task.getId());
                startActivity(intent);
            }

            @Override
            public void onEditClick(int position) {
                Task task = filtered.get(position);
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("mode", "edit");
                intent.putExtra("id", task.getId());
                startActivityForResult(intent, REQ_EDIT);
            }

            @Override
            public void onDeleteClick(int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this task?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Task task = filtered.get(position);
                            for (int i = 0; i < tasks.size(); i++) {
                                if (tasks.get(i).getId().equals(task.getId())) {
                                    tasks.remove(i);
                                    break;
                                }
                            }
                            refreshFiltered(etSearch.getText().toString());
                            SharedPrefManager.saveTasks(MainActivity.this, tasks);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        rv.setAdapter(adapter);

        // Add button click
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra("mode", "add");
            startActivityForResult(intent, REQ_ADD);
        });

        // Search edit text listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshFiltered(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Default sample task if empty
        if (tasks.isEmpty()) {
            Task sampleTask = new Task(
                    UUID.randomUUID().toString(),
                    "Pack travel bag",
                    "Clothes and essentials",
                    "2025-12-01",
                    true,
                    false,
                    "Personal"
            );
            tasks.add(sampleTask);
            SharedPrefManager.saveTasks(this, tasks);
            refreshFiltered(etSearch.getText().toString());
        } else {
            // Refresh with current search query
            refreshFiltered(etSearch.getText().toString());
        }
    }

    private void refreshFiltered(String query) {
        filtered.clear();
        if (query == null || query.trim().isEmpty()) {
            filtered.addAll(tasks);
        } else {
            String low = query.toLowerCase(Locale.getDefault());
            for (Task task : tasks) {
                if (task.getTitle().toLowerCase().contains(low) ||
                        task.getDescription().toLowerCase().contains(low) ||
                        task.getType().toLowerCase().contains(low)) {
                    filtered.add(task);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQ_ADD || requestCode == REQ_EDIT) && resultCode == RESULT_OK) {
            tasks = SharedPrefManager.loadTasks(this);
            refreshFiltered(etSearch.getText().toString());
        }
    }

    // ------------------------------
    // Lifecycle State Management
    // ------------------------------

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current search query
        outState.putString("SEARCH_QUERY", etSearch.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore search query and refresh filtered list
        if (savedInstanceState != null) {
            String savedQuery = savedInstanceState.getString("SEARCH_QUERY", "");
            etSearch.setText(savedQuery);
            refreshFiltered(savedQuery);
        }
    }
}

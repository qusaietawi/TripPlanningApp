package com.example.tripplanningapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDesc, tvDate, tvType, tvFlags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvDTitle);
        tvDesc = findViewById(R.id.tvDDesc);
        tvDate = findViewById(R.id.tvDDate);
        tvType = findViewById(R.id.tvDType);
        tvFlags = findViewById(R.id.tvDFlags);

        // Restore temporary state if available (Lifecycle State Management)
        if (savedInstanceState != null) {
            tvTitle.setText(savedInstanceState.getString("TITLE"));
            tvDesc.setText(savedInstanceState.getString("DESC"));
            tvDate.setText(savedInstanceState.getString("DATE"));
            tvType.setText(savedInstanceState.getString("TYPE"));
            tvFlags.setText(savedInstanceState.getString("FLAGS"));
        } else {
            // Get the task ID from intent
            Intent intent = getIntent();
            String taskId = intent.getStringExtra("id");

            // Load tasks
            ArrayList<Task> tasks = SharedPrefManager.loadTasks(this);

            // Find task by ID and display
            for (Task task : tasks) {
                if (task.getId().equals(taskId)) {
                    tvTitle.setText(task.getTitle());
                    tvDesc.setText(task.getDescription());
                    tvDate.setText(task.getDate());
                    tvType.setText(task.getType());
                    tvFlags.setText(
                            "Important: " + (task.isImportant() ? "Yes" : "No") + "\n" +
                                    "Completed: " + (task.isCompleted() ? "Yes" : "No")
                    );
                    break;
                }
            }
        }
    }

    // Lifecycle State Management

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save temporary state before activity is destroyed (e.g., rotation)
        outState.putString("TITLE", tvTitle.getText().toString());
        outState.putString("DESC", tvDesc.getText().toString());
        outState.putString("DATE", tvDate.getText().toString());
        outState.putString("TYPE", tvType.getText().toString());
        outState.putString("FLAGS", tvFlags.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore temporary state if available
        if (savedInstanceState != null) {
            tvTitle.setText(savedInstanceState.getString("TITLE"));
            tvDesc.setText(savedInstanceState.getString("DESC"));
            tvDate.setText(savedInstanceState.getString("DATE"));
            tvType.setText(savedInstanceState.getString("TYPE"));
            tvFlags.setText(savedInstanceState.getString("FLAGS"));
        }
    }
}

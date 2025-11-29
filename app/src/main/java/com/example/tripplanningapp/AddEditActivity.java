package com.example.tripplanningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddEditActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private TextView tvDate;
    private Button btnPickDate, btnSave;
    private CheckBox cbImportant;
    private Switch swCompleted;
    private RadioGroup rgType;
    private String mode;
    private String editId;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDesc);
        tvDate = findViewById(R.id.tvDate);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnSave = findViewById(R.id.btnSave);
        cbImportant = findViewById(R.id.cbImportant);
        swCompleted = findViewById(R.id.swCompleted);
        rgType = findViewById(R.id.rgType);

        tasks = SharedPrefManager.loadTasks(this);

        mode = getIntent().getStringExtra("mode");
        if ("edit".equals(mode)) {
            editId = getIntent().getStringExtra("id");
            for (Task t : tasks) {
                if (t.getId().equals(editId)) {
                    etTitle.setText(t.getTitle());
                    etDescription.setText(t.getDescription());
                    tvDate.setText(t.getDate());
                    cbImportant.setChecked(t.isImportant());
                    swCompleted.setChecked(t.isCompleted());
                    if ("Personal".equals(t.getType())) rgType.check(R.id.rbPersonal);
                    else if ("Work".equals(t.getType())) rgType.check(R.id.rbWork);
                    else rgType.check(R.id.rbOther);
                    break;
                }
            }
        } else {
            // default date today
            Calendar c = Calendar.getInstance();
            tvDate.setText(String.format("%04d-%02d-%02d",
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH) + 1,
                    c.get(Calendar.DAY_OF_MONTH)));
        }

        // Restore temporary state if available (Lifecycle State Management)
        if (savedInstanceState != null) {
            etTitle.setText(savedInstanceState.getString("TITLE"));
            etDescription.setText(savedInstanceState.getString("DESC"));
            tvDate.setText(savedInstanceState.getString("DATE"));
            cbImportant.setChecked(savedInstanceState.getBoolean("IMPORTANT"));
            swCompleted.setChecked(savedInstanceState.getBoolean("COMPLETED"));
            int checkedId = savedInstanceState.getInt("TYPE", R.id.rbOther);
            rgType.check(checkedId);
        }

        btnPickDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(AddEditActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        tvDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
            dp.show();
        });

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            String date = tvDate.getText().toString();
            boolean important = cbImportant.isChecked();
            boolean completed = swCompleted.isChecked();
            int checked = rgType.getCheckedRadioButtonId();
            String type = "Other";

            if (checked == R.id.rbPersonal) type = "Personal";
            else if (checked == R.id.rbWork) type = "Work";

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("edit".equals(mode)) {
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getId().equals(editId)) {
                        Task t = tasks.get(i);
                        t.setTitle(title);
                        t.setDescription(desc);
                        t.setDate(date);
                        t.setImportant(important);
                        t.setCompleted(completed);
                        t.setType(type);
                        break;
                    }
                }
            } else {
                Task t = new Task(UUID.randomUUID().toString(), title, desc, date, important, completed, type);
                tasks.add(t);
            }

            // Save permanently to SharedPreferences
            SharedPrefManager.saveTasks(AddEditActivity.this, tasks);
            setResult(RESULT_OK);
            finish();
        });
    }

    // ------------------------------
    // Lifecycle State Management
    // ------------------------------

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save temporary state before activity is destroyed (e.g., rotation)
        outState.putString("TITLE", etTitle.getText().toString());
        outState.putString("DESC", etDescription.getText().toString());
        outState.putString("DATE", tvDate.getText().toString());
        outState.putBoolean("IMPORTANT", cbImportant.isChecked());
        outState.putBoolean("COMPLETED", swCompleted.isChecked());
        outState.putInt("TYPE", rgType.getCheckedRadioButtonId());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore temporary state if available
        if (savedInstanceState != null) {
            etTitle.setText(savedInstanceState.getString("TITLE"));
            etDescription.setText(savedInstanceState.getString("DESC"));
            tvDate.setText(savedInstanceState.getString("DATE"));
            cbImportant.setChecked(savedInstanceState.getBoolean("IMPORTANT"));
            swCompleted.setChecked(savedInstanceState.getBoolean("COMPLETED"));
            int checkedId = savedInstanceState.getInt("TYPE", R.id.rbOther);
            rgType.check(checkedId);
        }
    }
}

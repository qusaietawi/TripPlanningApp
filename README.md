📱 Trip Planning App

A simple Android application that allows users to create, view, search, and manage tasks related to trip planning.
The project is built using Java, Android Studio, RecyclerView, and SharedPreferences.

🚀 Features
✅ Core Functionalities

Add new trip tasks

Edit existing tasks

Delete tasks

View details of any task

Search tasks

Store data locally using SharedPreferences

📦 App Structure

The app uses:

RecyclerView to display the list of tasks

TaskAdapter for RecyclerView binding

Task model class for representing each task

SharedPrefManager for saving and loading tasks

Three Activities:

MainActivity — Shows all tasks

AddEditActivity — Add or edit tasks

DetailActivity — Show a single task in detail

📂 Project Structure
TripPlanningApp2/
 └── app/
     └── src/
         └── main/
             ├── java/com/example/tripplanningapp/
             │     ├── MainActivity.java
             │     ├── AddEditActivity.java
             │     ├── DetailActivity.java
             │     ├── Task.java
             │     ├── TaskAdapter.java
             │     └── SharedPrefManager.java
             └── res/
                   ├── layout/
                   ├── drawable/
                   ├── values/
                   └── mipmap/

🛠️ How It Works
🔹 Task Storage

All tasks are saved as a JSON string inside SharedPreferences using:

SharedPreferences sharedPreferences = context.getSharedPreferences("Tasks", Context.MODE_PRIVATE);

🔹 Adding / Editing Tasks

AddEditActivity handles:

Title input

Description input

Date picker

After saving, the data is passed back to MainActivity.

🔹 Displaying Tasks

RecyclerView + TaskAdapter efficiently display all tasks with:

Title

Date

Delete button

On-click to open details

🔹 Viewing Details

DetailActivity shows:

Full task title

Full description

Date

📸 Screens (Conceptual)

Main Screen: List of all trip tasks

Add/Edit Screen: Form to add a task

Detail Screen: Full details of a selected task

▶️ Running the App

Extract the project

Open it in Android Studio

Let Gradle sync

Run on an emulator or physical device

📄 Requirements

Android Studio (Arctic Fox or newer)

Minimum SDK: 21

Java 8+

👨‍💻 Author

Developed as a university-level Android assignment.

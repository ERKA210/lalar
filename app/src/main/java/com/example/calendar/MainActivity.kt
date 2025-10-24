package com.example.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private var currentFilter: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dateTextView: TextView = findViewById(R.id.tvDate)
        val currentDate = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault()).format(Date())
        dateTextView.text = currentDate

        // 🔹 RecyclerView setup
        recyclerView = findViewById(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tasks = TaskStorage.getTasks(this)
        taskAdapter = TaskAdapter(tasks, this)
        recyclerView.adapter = taskAdapter

        setupCategoryFilters()

        val profileContainer = findViewById<android.widget.RelativeLayout>(R.id.profileContainer)
        profileContainer.setOnClickListener {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
        }

        val createTaskButton: ImageButton = findViewById(R.id.create_new_task_button)
        createTaskButton.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    private fun setupCategoryFilters() {
        val allButton: Button = findViewById(R.id.btn_all)
        val workButton: Button = findViewById(R.id.btn_work)
        val lifeButton: Button = findViewById(R.id.btn_life)
        val studyButton: Button = findViewById(R.id.btn_study)
        val eventButton: Button = findViewById(R.id.btn_event)

        allButton.setOnClickListener { filterTasks("All") }
        workButton.setOnClickListener { filterTasks("Work") }
        lifeButton.setOnClickListener { filterTasks("Life") }
        studyButton.setOnClickListener { filterTasks("Study") }
        eventButton.setOnClickListener { filterTasks("Event") }
    }

    private fun filterTasks(category: String) {
        currentFilter = category
        val allTasks = TaskStorage.getTasks(this)

        val filteredTasks = if (category == "All") {
            allTasks
        } else {
            allTasks.filter { it.category == category }
        }

        taskAdapter.updateTasks(filteredTasks)

        updateButtonStates(category)
    }

    private fun updateButtonStates(selectedCategory: String) {
        val buttons = mapOf(
            "All" to findViewById<Button>(R.id.btn_all),
            "Work" to findViewById<Button>(R.id.btn_work),
            "Life" to findViewById<Button>(R.id.btn_life),
            "Study" to findViewById<Button>(R.id.btn_study),
            "Event" to findViewById<Button>(R.id.btn_event)
        )

        buttons.forEach { (category, button) ->
            if (category == selectedCategory) {
                button.setBackgroundColor(getColor(R.color.selected_category))
                button.setTextColor(getColor(R.color.white))
            } else {
                button.setBackgroundResource(R.drawable.category_button_bg)
                button.setTextColor(getColor(R.color.black))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        filterTasks(currentFilter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filterTasks(currentFilter)
        }
    }
}
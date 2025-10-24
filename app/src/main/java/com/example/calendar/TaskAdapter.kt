package com.example.calendar

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(private var tasks: List<Task>, private val activity: MainActivity) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val taskDate: TextView = itemView.findViewById(R.id.tvTaskDate)
        val taskTime: TextView = itemView.findViewById(R.id.tvTaskTime)
        val categoryText: TextView = itemView.findViewById(R.id.tvCategory)
        val categoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.taskTitle.text = task.title
        holder.taskDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(task.date)
        holder.taskTime.text = task.startTime
        holder.categoryText.text = task.category

        holder.itemView.setOnClickListener {
            showTaskDetails(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }


    private fun showTaskDetails(task: Task) {
        val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        val timeFormat = "${task.startTime}${if (task.endTime.isNotEmpty()) " - ${task.endTime}" else ""}"

        AlertDialog.Builder(activity)
            .setTitle(task.title)
            .setMessage(
                "Category: ${task.category}\n" +
                        "Date: ${dateFormat.format(task.date)}\n" +
                        "Time: $timeFormat\n" +
                        "Reminder: ${if (task.reminder) "On" else "Off"}"
            )
            .setPositiveButton("OK", null)
            .show()
    }
}
package com.example.calendar

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TaskStorage {
    private const val PREFS_NAME = "task_storage"
    private const val KEY_TASKS = "tasks"
    fun saveTask(context: Context, task: Task) {
        val tasks = getTasks(context).toMutableList()
        tasks.add(task)
        saveTasks(context, tasks)
    }

    fun getTasks(context: Context): List<Task> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val tasksJson = prefs.getString(KEY_TASKS, "[]")
        val type = object : TypeToken<List<Task>>() {}.type
        return Gson().fromJson(tasksJson, type)
    }

    private fun saveTasks(context: Context, tasks: List<Task>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val tasksJson = Gson().toJson(tasks)
        prefs.edit().putString(KEY_TASKS, tasksJson).apply()
    }

    fun updateTasks(context: Context, tasks: List<Task>) {
        saveTasks(context, tasks)
    }
}

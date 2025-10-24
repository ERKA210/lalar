package com.example.calendar

import java.util.Date

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val date: Date,
    val startTime: String,
    val endTime: String = "",
    val category: String = "",
    val reminder: Boolean = false,
    val completed: Boolean = false
)
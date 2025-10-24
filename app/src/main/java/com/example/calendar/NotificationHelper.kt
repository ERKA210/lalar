package com.example.calendar

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TASKS = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_START_TIME = "start_time"
        private const val COLUMN_END_TIME = "end_time"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_REMINDER = "reminder"
        private const val COLUMN_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_DATE INTEGER NOT NULL,
                $COLUMN_START_TIME TEXT NOT NULL,
                $COLUMN_END_TIME TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_REMINDER INTEGER DEFAULT 0,
                $COLUMN_COMPLETED INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun addTask(task: Task): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DATE, task.date.time)
            put(COLUMN_START_TIME, task.startTime)
            put(COLUMN_END_TIME, task.endTime)
            put(COLUMN_CATEGORY, task.category)
            put(COLUMN_REMINDER, if (task.reminder) 1 else 0)
            put(COLUMN_COMPLETED, if (task.completed) 1 else 0)
        }
        return db.insert(TABLE_TASKS, null, values)
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = readableDatabase
        val cursor = db.query(TABLE_TASKS, null, null, null, null, null, "$COLUMN_DATE ASC")

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val date = Date(getLong(getColumnIndexOrThrow(COLUMN_DATE)))
                val startTime = getString(getColumnIndexOrThrow(COLUMN_START_TIME))
                val endTime = getString(getColumnIndexOrThrow(COLUMN_END_TIME))
                val category = getString(getColumnIndexOrThrow(COLUMN_CATEGORY))
                val reminder = getInt(getColumnIndexOrThrow(COLUMN_REMINDER)) == 1
                val completed = getInt(getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1

                tasks.add(
                    Task(
                        id,
                        title,
                        description,
                        date,
                        startTime,
                        endTime,
                        category,
                        reminder,
                        completed
                    )
                )
            }
        }
        cursor.close()
        return tasks
    }

    fun updateTask(task: Task): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DATE, task.date.time)
            put(COLUMN_START_TIME, task.startTime)
            put(COLUMN_END_TIME, task.endTime)
            put(COLUMN_CATEGORY, task.category)
            put(COLUMN_REMINDER, if (task.reminder) 1 else 0)
            put(COLUMN_COMPLETED, if (task.completed) 1 else 0)
        }
        return db.update(TABLE_TASKS, values, "$COLUMN_ID=?", arrayOf(task.id.toString()))
    }

    fun deleteTask(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_TASKS, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
}
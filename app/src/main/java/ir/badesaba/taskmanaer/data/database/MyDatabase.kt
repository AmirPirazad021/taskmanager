package ir.badesaba.taskmanaer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.badesaba.taskmanaer.data.todo.TodoDao
import ir.badesaba.taskmanaer.data.todo.TasksEntity

@Database(entities = [TasksEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun TodoDao(): TodoDao
}
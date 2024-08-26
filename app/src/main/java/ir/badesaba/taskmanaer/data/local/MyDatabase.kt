package ir.badesaba.taskmanaer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TasksEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun TodoDao(): TodoDao
}
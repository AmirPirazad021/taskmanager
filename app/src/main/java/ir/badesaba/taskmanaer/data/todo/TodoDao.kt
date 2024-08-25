package ir.badesaba.taskmanaer.data.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM tasks")
    fun getTaskList(): Flow<List<TasksEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(vararg todoEntity: TasksEntity)

    @Delete
    suspend fun deleteTask(vararg todoEntity: TasksEntity)

    @Transaction
    suspend fun combineUpsert(
        todoEntity: TasksEntity
    ) {
        upsertTask(todoEntity)
        getTaskList()
    }

    @Transaction
    suspend fun combineDelete(
        todoEntity: TasksEntity
    ) {
        upsertTask(todoEntity)
        getTaskList()
    }

}
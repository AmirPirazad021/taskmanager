package ir.badesaba.taskmanaer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ir.badesaba.taskmanaer.domain.TasksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM tasks")
    fun getTaskList(): Flow<List<TasksEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(vararg todoEntity: TasksEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg todoEntity: TasksEntity)

    @Delete
    suspend fun deleteTask(vararg todoEntity: TasksEntity)

    @Transaction
    suspend fun initTasks(
        currencies: List<TasksEntity>,
    ) {
        insertAll(*currencies.toTypedArray())
    }

}
package ir.badesaba.taskmanaer.domain.tasks

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun taskListFlow(): Flow<List<TasksModel>>
    suspend fun upsertTask(task: TasksModel)
    suspend fun deleteTask(task: TasksModel)
}
package ir.badesaba.taskmanaer.domain.tasks

import ir.badesaba.taskmanaer.data.TasksDto
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun taskListFlow(): Flow<List<TasksDto>>
    suspend fun insertAllTasks()
    suspend fun upsertTask(task: TasksDto)
    suspend fun deleteTask(task: TasksDto)
}
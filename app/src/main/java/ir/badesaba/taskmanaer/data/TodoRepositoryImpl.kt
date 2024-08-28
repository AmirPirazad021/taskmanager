package ir.badesaba.taskmanaer.data

import ir.badesaba.taskmanaer.data.local.TodoDao
import ir.badesaba.taskmanaer.data.remote.ApiService
import ir.badesaba.taskmanaer.domain.tasks.TaskRepository
import ir.badesaba.taskmanaer.mapper.toTaskModel
import ir.badesaba.taskmanaer.mapper.toTasksEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: TodoDao,
) : TaskRepository {

    override suspend fun taskListFlow(): Flow<List<TasksDto>> {
        return dao.getTaskList().map { list ->
            list.map { it.toTaskModel() }
        }
    }

    override suspend fun insertAllTasks() {
        val response = api.getTasks()
        response.map { it.toTasksEntity() }.let { dao.initTasks(it) }
    }

    override suspend fun upsertTask(task: TasksDto) {
        dao.upsertTask(task.toTasksEntity())
    }

    override suspend fun deleteTask(task: TasksDto) {
        dao.deleteTask(task.toTasksEntity())
    }

}

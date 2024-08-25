package ir.badesaba.taskmanaer.data.todo

import ir.badesaba.taskmanaer.domain.tasks.TasksModel
import ir.badesaba.taskmanaer.domain.tasks.TaskRepository
import ir.badesaba.taskmanaer.mapper.toTaskModel
import ir.badesaba.taskmanaer.mapper.toTasksEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao,
) : TaskRepository {

    override suspend fun taskListFlow(): Flow<List<TasksModel>> {
        return dao.getTaskList().map { list ->
            list.map { it.toTaskModel() }
        }
    }

    override suspend fun upsertTask(task: TasksModel) {
        dao.upsertTask(task.toTasksEntity())
    }

    override suspend fun deleteTask(task: TasksModel) {
        dao.deleteTask(task.toTasksEntity())
    }

}

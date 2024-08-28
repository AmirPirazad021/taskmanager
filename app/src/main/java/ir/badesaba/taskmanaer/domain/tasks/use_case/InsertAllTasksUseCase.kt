package ir.badesaba.taskmanaer.domain.tasks.use_case

import ir.badesaba.taskmanaer.domain.tasks.TaskRepository
import javax.inject.Inject


class InsertAllTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke() {
        taskRepository.insertAllTasks()
    }
}
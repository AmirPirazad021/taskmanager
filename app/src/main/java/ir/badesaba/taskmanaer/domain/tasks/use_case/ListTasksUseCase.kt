package ir.badesaba.taskmanaer.domain.tasks.use_case

import ir.badesaba.taskmanaer.domain.tasks.TaskRepository
import javax.inject.Inject


class ListTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke() = taskRepository.taskListFlow()
}
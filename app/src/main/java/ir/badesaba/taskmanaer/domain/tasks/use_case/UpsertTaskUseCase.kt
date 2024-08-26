package ir.badesaba.taskmanaer.domain.tasks.use_case

import ir.badesaba.taskmanaer.domain.tasks.TaskRepository
import ir.badesaba.taskmanaer.domain.tasks.TasksModel
import javax.inject.Inject


class UpsertTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskModel: TasksModel) = taskRepository.upsertTask(taskModel)
}
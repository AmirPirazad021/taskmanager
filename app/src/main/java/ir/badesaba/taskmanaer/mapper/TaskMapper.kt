package ir.badesaba.taskmanaer.mapper

import ir.badesaba.taskmanaer.data.todo.TasksEntity
import ir.badesaba.taskmanaer.domain.tasks.TasksModel

fun TasksModel.toTasksEntity() = TasksEntity(
    id = id, title = title, description = description, deadLine = deadLine
)

fun TasksEntity.toTaskModel() = TasksModel(
    id = id, title = title, description = description, deadLine = deadLine
)
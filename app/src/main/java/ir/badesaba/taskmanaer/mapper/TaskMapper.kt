package ir.badesaba.taskmanaer.mapper

import ir.badesaba.taskmanaer.data.local.TasksEntity
import ir.badesaba.taskmanaer.domain.tasks.TasksModel

fun TasksModel.toTasksEntity() = TasksEntity(
    id = id, title = title, description = description, deadLine = deadLine, updateAt = updateAt
)

fun TasksEntity.toTaskModel() = TasksModel(
    id = id, title = title, description = description, deadLine = deadLine, updateAt = updateAt
)
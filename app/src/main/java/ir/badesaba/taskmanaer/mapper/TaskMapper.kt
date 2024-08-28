package ir.badesaba.taskmanaer.mapper

import ir.badesaba.taskmanaer.domain.TasksEntity
import ir.badesaba.taskmanaer.data.TasksDto

fun TasksDto.toTasksEntity() = TasksEntity(
    id = id, title = title, description = description, deadLine = deadLine, updateAt = updateAt
)

fun TasksEntity.toTaskModel() = TasksDto(
    id = id, title = title, description = description, deadLine = deadLine, updateAt = updateAt
)
package ir.badesaba.taskmanaer.domain.tasks

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TasksModel(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("deadline")
    val deadLine: Long
)
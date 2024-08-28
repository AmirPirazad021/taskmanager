package ir.badesaba.taskmanaer.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TasksDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("deadline")
    val deadLine: Long,
    @SerializedName("update_at")
    val updateAt: Long = System.currentTimeMillis()
) : Parcelable
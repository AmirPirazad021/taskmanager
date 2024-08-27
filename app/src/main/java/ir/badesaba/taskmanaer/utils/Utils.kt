package ir.badesaba.taskmanaer.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

object Utils {

    fun RecyclerView.initRecyclerView(
        context: Context,
        adapterInstance: RecyclerView.Adapter<*>,
        isReversed: Boolean = false
    ) {

        this.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, isReversed)

            setHasFixedSize(false)
            adapter = adapterInstance
        }
    }

    fun convertToDate(time: Long): String {
        val date = java.util.Date(time)
        val format = java.text.SimpleDateFormat("dd/MM/yyyy hh:mm")
        return format.format(date)
    }

    fun convertDateToTimestamp(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute, 0)  // Example date
        return calendar.timeInMillis
    }
}



package ir.badesaba.taskmanaer.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import ir.badesaba.taskmanaer.domain.tasks.TasksModel

const val REMINDER = "REMINDER"

fun setUpAlarm(context: Context, reminder: TasksModel) {
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra(REMINDER, Gson().toJson(reminder))
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, reminder.deadLine.toInt(),
        intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    try {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.deadLine, pendingIntent)
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}

fun cancelAlarm(context: Context, reminder: TasksModel) {
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra(REMINDER, Gson().toJson(reminder))
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, reminder.deadLine.toInt(),
        intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    try {
        alarmManager.cancel(pendingIntent)
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}
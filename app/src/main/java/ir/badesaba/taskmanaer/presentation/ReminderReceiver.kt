package ir.badesaba.taskmanaer.presentation

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ir.badesaba.taskmanaer.CHANNEL
import ir.badesaba.taskmanaer.R;
import ir.badesaba.taskmanaer.data.TasksDto
import ir.badesaba.taskmanaer.domain.tasks.use_case.UpsertTaskUseCase
import ir.badesaba.taskmanaer.utils.MediaPlayerManager
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

const val OK = "OK"

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateUseCase: UpsertTaskUseCase

    private var mediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent) {

        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_music)

        val reminderJson = intent.getStringExtra(REMINDER)
        val reminder = Gson().fromJson(reminderJson, TasksDto::class.java)

        val notificationManager = NotificationManagerCompat.from(context)

        val doneIntent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, reminderJson)
            action = OK
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            context, reminder.deadLine.toInt(), doneIntent, PendingIntent.FLAG_IMMUTABLE
        )

        when (intent.action) {
            OK -> {
                MediaPlayerManager.stopAndReleaseAll()
                // Cancel the notification
                notificationManager.cancel(1)

                // Update the task using the use case
                runBlocking { updateUseCase.invoke(reminder) }

                cancelAlarm(context, reminder)
            }

            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val notification = NotificationCompat.Builder(context, CHANNEL)
                            .setSmallIcon(R.drawable.ic_timer)
                            .setContentTitle("Task Reminder")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentText(reminder.title.plus(" ${reminder.description}"))
                            .addAction(R.drawable.ic_add, "متوجه شدم", donePendingIntent)
                            .build()
                        notificationManager.notify(1, notification)
                    }
                } else {
                    val notification = NotificationCompat.Builder(context, CHANNEL)
                        .setSmallIcon(R.drawable.ic_timer)
                        .setContentTitle("Task Reminder")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentText(reminder.title.plus(" ${reminder.description}"))
                        .addAction(R.drawable.ic_add, "متوجه شدم", donePendingIntent)
                        .build()

                    notificationManager.notify(1, notification)
                }

                mediaPlayer?.setOnCompletionListener {
                    mediaPlayer?.release()
                }
                mediaPlayer?.apply {
                    MediaPlayerManager.addMediaPlayer(this)
                    start()
                }
            }
        }
    }
}
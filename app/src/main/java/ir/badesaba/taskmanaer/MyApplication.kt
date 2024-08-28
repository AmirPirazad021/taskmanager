package ir.badesaba.taskmanaer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference


const val CHANNEL = "channel"
const val NAME = "name"

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var ctx: WeakReference<Context>
    }

    override fun onCreate() {
        super.onCreate()
        ctx = WeakReference<Context>(this)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL, NAME, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
package ir.badesaba.taskmanaer

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var ctx: WeakReference<Context>
    }

    override fun onCreate() {
        super.onCreate()
        ctx = WeakReference<Context>(this)
    }

}
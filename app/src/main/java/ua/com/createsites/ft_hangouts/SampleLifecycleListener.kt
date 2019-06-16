package ua.com.createsites.ft_hangouts

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.Lifecycle
import java.time.temporal.ChronoUnit
import android.content.Context
import java.time.LocalDateTime
import android.widget.Toast
import android.os.Build

class SampleLifecycleListener(context: Context): LifecycleObserver {

    private var backTime: LocalDateTime? = null
    private val myCon: Context = context

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        if (backTime != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val now = LocalDateTime.now()
                val diff = ChronoUnit.SECONDS.between(backTime, now)

                Toast.makeText(myCon, String.format(myCon.getString(R.string.background_message), diff.toString()), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            backTime = LocalDateTime.now()
        }
    }
}
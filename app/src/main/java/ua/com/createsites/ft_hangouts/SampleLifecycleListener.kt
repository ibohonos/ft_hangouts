package ua.com.createsites.ft_hangouts

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class SampleLifecycleListener(context: Context): LifecycleObserver {

    private var backTime: Calendar? = null
    private val myCon = context


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        if (backTime != null) {
            val time1 = backTime.toString()
            val time2 = Calendar.getInstance().toString()

            val format = SimpleDateFormat(DateFormat, Locale.ROOT)
            val date1 = format.parse(time1)
            val date2 = format.parse(time2)
            val difference = date2.getTime() - date1.getTime()
//            val currTime = Calendar.getInstance().time.toString()
//            val currTime = LocalDateTime.now().minus(backTime).toString()
//            Toast.makeText(myCon, String.format(myCon.getString(R.string.background_message), currTime), Toast.LENGTH_LONG).show()
            Log.d("SampleLifecycle", difference.toString())
        }
        Log.d("SampleLifecycle", backTime.toString())
        Log.d("SampleLifecycle", "Returning to foreground…")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        backTime = Calendar.getInstance()
//        backTime = LocalDateTime.now()
        Log.d("SampleLifecycle", "Moving to background…")
    }
}
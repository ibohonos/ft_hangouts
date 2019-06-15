package ua.com.createsites.ft_hangouts

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner

class FtHangoutsApp: Application() {

    private val lifecycleListener: SampleLifecycleListener by lazy {
        SampleLifecycleListener(this)
    }

    override fun onCreate() {
        super.onCreate()
        setupLifecycleListener()
    }

    private fun setupLifecycleListener() {
        ProcessLifecycleOwner.get().lifecycle
                .addObserver(lifecycleListener)
    }
}
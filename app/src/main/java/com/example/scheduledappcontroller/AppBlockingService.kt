package com.example.scheduledappcontroller

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Build

class AppBlockingService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val mode: Mode? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("mode", Mode::class.java)
        } else {
            intent.getParcelableExtra("mode")
        }
        mode?.let {
            blockAppsAndHideNotifications(it)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun blockAppsAndHideNotifications(mode: Mode) {
        // Implement logic to block apps and hide notifications here
    }
}

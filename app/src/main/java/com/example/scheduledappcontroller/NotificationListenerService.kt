package com.example.scheduledappcontroller

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        // Logic for handling the notification when posted
        Log.d("NotificationListener", "Notification posted: ${sbn.packageName}")

        // Here, you can check the package name and block the notification if needed
        if (shouldBlockNotification(sbn.packageName)) {
            cancelNotification(sbn.key)
            Log.d("NotificationListener", "Notification from ${sbn.packageName} blocked.")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        // Logic for handling the notification when removed
        Log.d("NotificationListener", "Notification removed: ${sbn.packageName}")
    }

    private fun shouldBlockNotification(packageName: String): Boolean {
        // Define logic to check whether a notification from a specific package should be blocked
        val blockedApps = getBlockedAppsFromPreferences()
        return blockedApps.contains(packageName)
    }

    private fun getBlockedAppsFromPreferences(): List<String> {
        // Retrieve the list of blocked apps from SharedPreferences or any other storage mechanism
        // For now, let's return a hardcoded list
        return listOf("com.facebook.katana", "com.instagram.android", "com.twitter.android")
    }
}

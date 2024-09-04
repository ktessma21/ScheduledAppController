package com.example.scheduledappcontroller

import java.io.File
import java.io.FileOutputStream
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import kotlinx.coroutines.flow.collect
import java.util.Calendar

@Composable
fun MonitorDisableMode() {
    val context = LocalContext.current
    val disableModeData by getDisableModeData(context).collectAsState(
        initial = DisableModeData("", "Start Time", "End Time", emptySet(), emptySet())
    )

    LaunchedEffect(disableModeData) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentTime = String.format("%02d:%02d", currentHour, currentMinute)

        // Check if current time matches start time and handle it
        if (disableModeData.startTime == currentTime) {
            // Disable the toggled apps here and pass context
            disableApps(disableModeData.toggledApps, context)
        }
    }
}


// Example function to disable the apps (Placeholder)
fun disableApps(apps: Set<String>, context: Context) {
    // Define the file name and create a file object in the app's internal storage directory
    val fileName = "disabled_apps.txt"
    val file = File(context.filesDir, fileName)

    // Prepare the content to be written to the file
    val content = StringBuilder()
    content.append("Disabled Apps:\n")
    apps.forEach { app ->
        content.append("$app\n")
    }

    // Write the content to the file
    try {
        FileOutputStream(file).use { output ->
            output.write(content.toString().toByteArray())
        }
        println("File written successfully: ${file.absolutePath}")
    } catch (e: Exception) {
        println("Error writing file: ${e.message}")
    }
}

package com.example.scheduledappcontroller

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance
val Context.dataStore by preferencesDataStore(name = "disable_mode_preferences")

// Define keys
val SCHEDULE_NAME_KEY = stringPreferencesKey("schedule_name")
val START_TIME_KEY = stringPreferencesKey("start_time")
val END_TIME_KEY = stringPreferencesKey("end_time")
val SELECTED_DAYS_KEY = stringSetPreferencesKey("selected_days")
val TOGGLED_APPS_KEY = stringSetPreferencesKey("toggled_apps")

// Helper function to save data to DataStore
suspend fun saveDisableModeData(
    context: Context,
    scheduleName: String,
    startTime: String,
    endTime: String,
    selectedDays: Set<String>,
    toggledApps: Set<String>
) {
    context.dataStore.edit { preferences ->
        preferences[SCHEDULE_NAME_KEY] = scheduleName
        preferences[START_TIME_KEY] = startTime
        preferences[END_TIME_KEY] = endTime
        preferences[SELECTED_DAYS_KEY] = selectedDays
        preferences[TOGGLED_APPS_KEY] = toggledApps
    }
}

// Helper function to retrieve data from DataStore
fun getDisableModeData(context: Context): Flow<DisableModeData> {
    return context.dataStore.data.map { preferences ->
        val scheduleName = preferences[SCHEDULE_NAME_KEY] ?: ""
        val startTime = preferences[START_TIME_KEY] ?: "Start Time"
        val endTime = preferences[END_TIME_KEY] ?: "End Time"
        val selectedDays = preferences[SELECTED_DAYS_KEY] ?: emptySet()
        val toggledApps = preferences[TOGGLED_APPS_KEY] ?: emptySet()
        DisableModeData(scheduleName, startTime, endTime, selectedDays, toggledApps)
    }
}

// Data class to store all the values retrieved from DataStore
data class DisableModeData(
    val scheduleName: String,
    val startTime: String,
    val endTime: String,
    val selectedDays: Set<String>,
    val toggledApps: Set<String>
)

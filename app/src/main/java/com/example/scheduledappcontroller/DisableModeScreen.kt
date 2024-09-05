package com.example.scheduledappcontroller

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.TimePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisableModeScreen() {
    var scheduleName by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("Start Time") }
    var endTime by remember { mutableStateOf("End Time") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    val installedApps = getInstalledApps(context)

    val filteredApps = installedApps.filter {
        it.name.contains(searchQuery.text, ignoreCase = true)
    }

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val startTimePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            startTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true
    )

    val endTimePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            endTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Disable Mode", fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4459E4),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { handleSave(context, scheduleName, startTime, endTime, filteredApps) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f).padding(4.dp)
                        ) {
                            Text("Save", fontSize = 14.sp)
                        }
                        Button(
                            onClick = { /* Handle Cancel */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF44336),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f).padding(4.dp)
                        ) {
                            Text("Cancel", fontSize = 14.sp)
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                // Schedule Name Input
                TextField(
                    value = scheduleName,
                    onValueChange = { scheduleName = it },
                    placeholder = { Text("Label Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Row for Start and End Time Pickers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { startTimePickerDialog.show() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { startTimePickerDialog.show() }) {
                            Icon(Icons.Filled.Search, contentDescription = "Set Start Time")
                        }
                        Text(text = startTime, fontSize = 14.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { endTimePickerDialog.show() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { endTimePickerDialog.show() }) {
                            Icon(Icons.Filled.Search, contentDescription = "Set End Time")
                        }
                        Text(text = endTime, fontSize = 14.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Repeat Days Selector
                Text("Repeat on:", fontSize = 14.sp)
                // Code for days selection and search bar omitted for brevity...

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredApps) { app ->
                        AppItem(app = app)
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                }
            }
        }
    )
}

@Composable
fun AppItem(app: AppInfo) {
    var isEnabled by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = app.icon.asImageBitmap(),
            contentDescription = app.name,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = app.name,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it }
        )
    }
}

// Helper functions to manage app list and handling save logic
fun handleSave(context: Context, scheduleName: String, startTime: String, endTime: String, apps: List<AppInfo>) {
    val intent = Intent(context, AppBlockingService::class.java)
    // Assuming Mode is a Parcelable data class containing schedule information
    intent.putExtra("mode", Mode(scheduleName, startTime, endTime, apps))
    context.startService(intent)
}

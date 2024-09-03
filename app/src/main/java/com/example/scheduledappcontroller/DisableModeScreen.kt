package com.example.scheduledappcontroller

import android.app.TimePickerDialog
import android.content.pm.ApplicationInfo
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.TimePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.res.painterResource
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

    // Time Picker Dialogs
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
                            onClick = { /* Handle Save */ },
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
                    // Start Time Column
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { startTimePickerDialog.show() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { startTimePickerDialog.show() }) {
                            Icon(painter = painterResource(id = R.drawable.alarm), contentDescription = "Set Start Time")
                        }
                        Text(text = startTime, fontSize = 14.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // End Time Column
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { endTimePickerDialog.show() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { endTimePickerDialog.show() }) {
                            Icon(painter = painterResource(id = R.drawable.alarm), contentDescription = "Set End Time")
                        }
                        Text(text = endTime, fontSize = 14.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Repeat Days Selector
                Text("Repeat on:", fontSize = 14.sp)

                // First Row of Days
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val daysOfWeek1 = listOf("Mon", "Tue", "Wed", "Thu")
                    val selectedDays = remember { mutableStateListOf<String>() }
                    daysOfWeek1.forEach { day ->
                        val isSelected = selectedDays.contains(day)
                        Button(
                            onClick = {
                                if (isSelected) selectedDays.remove(day) else selectedDays.add(day)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFFCCCCCC),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(day, fontSize = 12.sp)
                        }
                    }
                }

                // Second Row of Days
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    val daysOfWeek2 = listOf("Fri", "Sat", "Sun")
                    val selectedDays = remember { mutableStateListOf<String>() }
                    daysOfWeek2.forEach { day ->
                        val isSelected = selectedDays.contains(day)
                        Button(
                            onClick = {
                                if (isSelected) selectedDays.remove(day) else selectedDays.add(day)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFFCCCCCC),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(day, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Scrollable list of installed apps with toggle switches
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
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

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Bitmap
) : Comparable<AppInfo>{
    override fun compareTo(other : AppInfo): Int = this.name.compareTo(other.name)
}

fun getInstalledApps(context: Context): List<AppInfo> {
    val pm: PackageManager = context.packageManager
    val apps = mutableListOf<AppInfo>()
    val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

    for (packageInfo in packages) {
        val name = pm.getApplicationLabel(packageInfo).toString()
        val icon = pm.getApplicationIcon(packageInfo).toBitmap()
        val packageName = packageInfo.packageName

        // Check if it's a relevant app based on known package names
        if (isRelevantApp(packageName)) {
            apps.add(AppInfo(name, packageName, icon))
        }
    }
    apps.sort() // Sort the applications by their names.
    return apps
}


fun isRelevantApp(packageName: String): Boolean {
    // Known package names for social media, messaging, and games
    val relevantPackages = listOf(
       "com.instagram.android", "com.zhiliaoapp.musically", "com.facebook.katana",
    "com.snapchat.android", "com.twitter.android", "com.pinterest", "com.reddit.frontpage",
    "com.linkedin.android", "com.tumblr",

    // Messaging
    "com.whatsapp", "com.facebook.orca", "org.telegram.messenger",
    "com.viber.voip", "com.discord", "us.zoom.videomeetings",

    // Streaming and Video
    "com.google.android.youtube", "com.netflix.mediaclient", "tv.twitch.android.app",
    "com.spotify.music",

    // Games
    "com.king.candycrushsaga", "com.supercell.clashofclans", "com.tencent.ig",
    "com.activision.callofduty.shooter",

    // Shopping
    "com.amazon.mShop.android.shopping", "com.ebay.mobile",

    // Others
    "com.google.android.gm", "com.android.chrome", "com.tinder",
    "com.bumble.app", "com.niksoftware.snapseed"


    )

    return relevantPackages.any { packageName.startsWith(it) }
}


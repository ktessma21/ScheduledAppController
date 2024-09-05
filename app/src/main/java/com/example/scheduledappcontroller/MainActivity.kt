package com.example.scheduledappcontroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scheduledappcontroller.ui.theme.ScheduledAppControllerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScheduledAppControllerTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") { MainScreen(navController) }
        composable("disable_mode_screen") { DisableModeScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ScheduledAppController",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* Handle settings click */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4459E4),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(text = "Choose a Mode", fontSize = 20.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Mode Buttons
                ModeButton(modeName = "Work", navController)
                Spacer(modifier = Modifier.height(16.dp))
                ModeButton(modeName = "School", navController)
                Spacer(modifier = Modifier.height(16.dp))
                ModeButton(modeName = "Gym", navController)

                Spacer(modifier = Modifier.weight(1f))

                // Customize Mode Button
                Button(
                    onClick = { navController.navigate("disable_mode_screen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Customize Mode")
                }
            }
        }
    )
}

@Composable
fun ModeButton(modeName: String, navController: NavHostController) {
    Button(
        onClick = { /* Handle mode activation logic */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFCCCCCC),
            contentColor = Color.Black
        )
    ) {
        Text(text = modeName, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ScheduledAppControllerTheme {
        MainScreen(rememberNavController()) // Pass a NavController for preview
    }
}

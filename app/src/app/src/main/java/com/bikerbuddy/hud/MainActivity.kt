package com.bikerbuddy.hud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.bikerbuddy.hud.location.SpeedManager


class MainActivity : ComponentActivity() {

    lateinit var speedManager: SpeedManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(
            android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        speedManager = SpeedManager(this)

        setContent {
            HudScreen()
        }
    }
}

@Composable
fun HudScreen(speedManager: SpeedManager) {

    val speed by speedManager.speedKmh.collectAsState()

    LaunchedEffect(Unit) {
        speedManager.start()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Speed (top center)
        Text(
            text = "$speed km/h",
            color = Color.White,
            fontSize = 40.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp)
        )

        // Placeholder HUD label
        Text(
            text = "BIKER HUD",
            color = Color.Gray,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}



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
import com.bikerbuddy.hud.call.CallManager
import com.bikerbuddy.hud.media.MediaManager



class MainActivity : ComponentActivity() {

    lateinit var speedManager: SpeedManager
    lateinit var callManager: CallManager
    lateinit var mediaManager: MediaManager




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
fun HudScreen(
    speedManager: SpeedManager,
    callManager: CallManager,
    mediaManager: MediaManager
) {

    val speed by speedManager.speedKmh.collectAsState()
    val callState by callManager.callState.collectAsState()
    val mediaState by mediaManager.mediaState.collectAsState()

    LaunchedEffect(Unit) {
        speedManager.start()
        callManager.start()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // SPEED (top)
        Text(
            text = "$speed km/h",
            color = Color.White,
            fontSize = 40.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp)
        )

        // MUSIC CONTROLS (center)
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "⏮",
                color = Color.White,
                fontSize = 36.sp,
                modifier = Modifier.clickable {
                    mediaManager.previous()
                }
            )

            Text(
                text = if (mediaState.isPlaying) "⏸" else "▶",
                color = Color.White,
                fontSize = 36.sp,
                modifier = Modifier.clickable {
                    mediaManager.playPause()
                }
            )

            Text(
                text = "⏭",
                color = Color.White,
                fontSize = 36.sp,
                modifier = Modifier.clickable {
                    mediaManager.next()
                }
            )
        }

        // CALL ALERT (bottom)
        when (callState) {
            is CallState.Ringing -> {
                Text(
                    text = "INCOMING CALL",
                    color = Color.Green,
                    fontSize = 28.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }

            is CallState.Active -> {
                Text(
                    text = "ON CALL",
                    color = Color.Cyan,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }

            CallState.Idle -> {}
        }
    }
}



callManager = CallManager(this)

setContent {
    HudScreen(
        speedManager = speedManager,
        callManager = callManager
    )
}


mediaManager = MediaManager(this)

setContent {
    HudScreen(
        speedManager = speedManager,
        callManager = callManager,
        mediaManager = mediaManager
    )
}



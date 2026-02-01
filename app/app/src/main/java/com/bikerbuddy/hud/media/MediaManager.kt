package com.bikerbuddy.hud.media

import android.content.Context
import android.media.AudioManager
import android.view.KeyEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MediaState(
    val isPlaying: Boolean = false,
    val title: String = ""
)

class MediaManager(private val context: Context) {

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState: StateFlow<MediaState> = _mediaState

    fun playPause() {
        sendKey(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        _mediaState.value = _mediaState.value.copy(
            isPlaying = !_mediaState.value.isPlaying
        )
    }

    fun next() {
        sendKey(KeyEvent.KEYCODE_MEDIA_NEXT)
    }

    fun previous() {
        sendKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS)
    }

    private fun sendKey(keyCode: Int) {
        val down = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
        val up = KeyEvent(KeyEvent.ACTION_UP, keyCode)
        audioManager.dispatchMediaKeyEvent(down)
        audioManager.dispatchMediaKeyEvent(up)
    }
}

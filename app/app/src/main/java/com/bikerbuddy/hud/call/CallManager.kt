package com.bikerbuddy.hud.call

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class CallState {
    object Idle : CallState()
    data class Ringing(val number: String?) : CallState()
    object Active : CallState()
}

class CallManager(context: Context) {

    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    val callState: StateFlow<CallState> = _callState

    private val listener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, number: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_RINGING ->
                    _callState.value = CallState.Ringing(number)

                TelephonyManager.CALL_STATE_OFFHOOK ->
                    _callState.value = CallState.Active

                TelephonyManager.CALL_STATE_IDLE ->
                    _callState.value = CallState.Idle
            }
        }
    }

    fun start() {
        telephonyManager.listen(
            listener,
            PhoneStateListener.LISTEN_CALL_STATE
        )
    }

    fun stop() {
        telephonyManager.listen(
            listener,
            PhoneStateListener.LISTEN_NONE
        )
    }
}

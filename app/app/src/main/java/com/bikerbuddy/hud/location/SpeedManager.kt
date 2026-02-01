package com.bikerbuddy.hud.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SpeedManager(context: Context) {

    private val fusedClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _speedKmh = MutableStateFlow(0)
    val speedKmh: StateFlow<Int> = _speedKmh

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        1000L // 1 second
    ).build()

    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location: Location = result.lastLocation ?: return
            val speedMps = location.speed   // meters/second
            val kmh = (speedMps * 3.6f).toInt()
            _speedKmh.value = if (kmh < 0) 0 else kmh
        }
    }

    @SuppressLint("MissingPermission")
    fun start() {
        fusedClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )
    }

    fun stop() {
        fusedClient.removeLocationUpdates(callback)
    }
}

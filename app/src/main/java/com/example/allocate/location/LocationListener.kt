package com.example.allocate.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.example.allocate.model.LocationModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationListener (context: Context) : LiveData<LocationModel> () {

    private var fusedLocation = LocationServices.getFusedLocationProviderClient(context)

    companion object {
        val request: LocationRequest = LocationRequest.create().apply {
            interval = 25000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocation(location)
            }
        }
    }

    private fun setLocation(location: Location) {
        value = LocationModel(
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocation.requestLocationUpdates(
            request,
            callback,
            null
        )
    }
    override fun onActive() {
        super.onActive()
        fusedLocation.lastLocation.addOnSuccessListener { location ->
            location?.also {
                setLocation(it)
            }
        }
        startLocationUpdates()
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocation.removeLocationUpdates(callback)
    }
}
package com.example.weatherreport

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

// Class encapsulates location service functions
class LocationHelper(private val context: Context) {

    // FusedLocationProviderClient is a high-level Google Play Services API
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Obtains the user's current location
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onResult: (Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onResult(location)
            }
            .addOnFailureListener { e ->
                onResult(null)
                println(e)
            }
    }

    // Check for permissions
    fun checkLocationPermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    activity,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PERMISSION_GRANTED
    }

    // Send a request to the user to use location services
    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_LOCATION)
    }

    // Permission request ID value, used to identify the permission being requested
    companion object {
        const val PERMISSION_REQUEST_LOCATION = 121
    }


}



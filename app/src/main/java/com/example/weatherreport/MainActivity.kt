package com.example.weatherreport

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherreport.ui.theme.WeatherReportTheme
import androidx.lifecycle.lifecycleScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Declare a Location Helper Class, passing the instance of the activity
        val locationHelper = LocationHelper(this)

        if (locationHelper.checkLocationPermission(this)) {
            // Location permission is on, render main page
            loadMainPage(locationHelper)
        } else {
            // Location permission is off, request for permission
            locationHelper.requestPermissions(this)
        }
    }

    // Receive data needed and renders the main page
    private fun loadMainPage(locationHelper: LocationHelper) {
        getWeather(lifecycleScope, locationHelper) { result ->
            setContent {
                WeatherReportTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) { MainPage(result) }
                }
            }
        }
    }

    // Overrides onRequestPermissionResult function in Activity
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LocationHelper.PERMISSION_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission is granted, proceed to load the main page
                val locationHelper = LocationHelper(this)
                loadMainPage(locationHelper)
            } else {
                // Location permission is denied, do nothing
            }
        }
    }
}

// Takes the pair list of strings and renders the main page contents
@Composable
fun MainPage(texts: List<Pair<String, String>>?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(64.dp)
    ) {
        // Loops through the list of texts for all the string values
        if (texts != null) {
            for (text in texts) {
                TextRow(text.first, text.second)
            }
        }
    }
}

@Composable
fun TextRow(text1: String, text2: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = text1,
            textAlign = TextAlign.Center,
        )
        Text(
            text = text2,
            textAlign = TextAlign.Center,
        )
    }
}




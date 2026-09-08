package com.sih.bahubhashini

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sih.bahubhashini.ui.theme.BahuBhashiniTheme
import com.sih.bahubhashini.ui.theme.DarkBackground
import com.sih.bahubhashini.ui.screens.MainEmergencyScreen
import com.sih.bahubhashini.viewmodel.BahuBhashiniViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BahuBhashiniTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    val viewModel: BahuBhashiniViewModel = viewModel()

                    // Permission launcher for offline voice & mesh discovery
                    val permissionsLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissions ->
                        // Permissions granted/denied callback
                    }

                    LaunchedEffect(Unit) {
                        val permissionsToRequest = mutableListOf(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
                            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
                            permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE)
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionsToRequest.add(Manifest.permission.NEARBY_WIFI_DEVICES)
                        }

                        val ungranted = permissionsToRequest.filter {
                            ContextCompat.checkSelfPermission(this@MainActivity, it) != PackageManager.PERMISSION_GRANTED
                        }

                        if (ungranted.isNotEmpty()) {
                            permissionsLauncher.launch(ungranted.toTypedArray())
                        }
                    }

                    MainEmergencyScreen(viewModel = viewModel)
                }
            }
        }
    }
}


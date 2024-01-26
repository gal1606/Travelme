package com.example.travelme

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.rememberNavController
import com.example.travelme.extension.hasLocationPermission
import com.example.travelme.models.Trip
import com.example.travelme.models.User
import com.example.travelme.navigation.RootNavigationGraph
import com.example.travelme.ui.theme.AppTheme
import com.example.travelme.viewmodels.FirebaseAuthVM
import com.example.travelme.viewmodels.FirebaseDBVM
import com.example.travelme.viewmodels.LocationVM
import com.example.travelme.viewmodels.PermissionEvent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()

        val locationViewModel: LocationVM by viewModels()

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val permissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                LaunchedEffect(!hasLocationPermission()) {
                    permissionState.launchMultiplePermissionRequest()
                }

                when {
                    permissionState.allPermissionsGranted -> {
                        LaunchedEffect(Unit) {
                            locationViewModel.handle(PermissionEvent.Granted)
                        }
                    }

                    permissionState.shouldShowRationale -> {
                        RationaleAlert(onDismiss = { }) {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }

                    !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
                        LaunchedEffect(Unit) {
                            locationViewModel.handle(PermissionEvent.Revoked)
                        }
                    }
                }
                LocationViewModel.locationViewModel = locationViewModel

                RootNavigationGraph(navController = navController)
            }
        }
    }
}

object ShowDialog {
    var showDialog = mutableStateOf(false)
}

object DialogMessage {
    var dialogMessage : String = ""
}

object AuthViewModel {
    var authViewModel: FirebaseAuthVM = FirebaseAuthVM()
}

object StoreViewModel {
    var storeViewModel: FirebaseDBVM = FirebaseDBVM()
}

object LocationViewModel {
    lateinit var locationViewModel: LocationVM
}

object CurrentUser {
    lateinit var currentUser: User
}



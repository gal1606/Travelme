package com.example.travelme.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.example.travelme.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.travelme.ui.components.AdminFooter
import com.example.travelme.ui.theme.AppTheme
import com.example.travelme.ui.theme.spacing
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun AdminHomeScreen(navController: NavHostController) {
   Box(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
    ) {
        val context = LocalContext.current
        //val intentScreen = Intent(context, LocationActivity::class.java)
        val extras = Bundle()
        extras.putString("screen", "admin")
        //intentScreen.putExtras(extras)
        //ContextCompat.startActivity(context, intentScreen, extras)
        //LocationActivity()
   }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AdminHomeScreenPreviewLight() {
    AppTheme {
        AdminHomeScreen(rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AdminHomeScreenPreviewDark() {
    AppTheme {
        AdminHomeScreen(rememberNavController())
    }
}

@Composable
fun adminScreen(currentPosition: LatLng, cameraState: CameraPositionState) {
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)

    val markersArray: ArrayList<LatLng> = ArrayList()
    markersArray.add(LatLng(44.811058, 20.4617586))
    markersArray.add(LatLng(44.811058, 20.4627586))
    markersArray.add(LatLng(44.810058, 20.4627586))
    markersArray.add(LatLng(44.809058, 20.4627586))
    markersArray.add(LatLng(44.809058, 20.4617586))

    val spacing = spacing
    Column(
        modifier = Modifier
            .fillMaxWidth()

            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.welcome_back),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(id = R.string.admin),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        GoogleMap(
            modifier = Modifier.height(400.dp),
            cameraPositionState = cameraState,
            properties = MapProperties(

                mapType = MapType.HYBRID,

                )
        ) {
            Marker(
                state = MarkerState(position = marker),
                title = "MyPosition",
                snippet = "This is a description of this Marker",
                draggable = true
            )
            Marker(
                state = MarkerState(position = LatLng(32.99656592492715, 35.69813236952394)),
                title = "MyPosition",
                snippet = "This is a description of this Marker",
                draggable = true
            )

        }

        AdminFooter()
    }
}
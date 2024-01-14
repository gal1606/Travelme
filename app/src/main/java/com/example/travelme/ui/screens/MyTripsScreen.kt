package com.example.travelme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelme.R
import com.example.travelme.navigation.ProfileScreen
import com.example.travelme.ui.components.TripUserRow
import com.example.travelme.ui.theme.spacing

@Composable
fun MyTripsScreen(navController: NavHostController) {
    val spacing = spacing
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.performed_trips),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(
            modifier = Modifier
                .padding(spacing.medium)
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(200.dp)
                .verticalScroll(rememberScrollState())
        ) {
            repeat(5)
            {
                TripUserRow()
            }
        }

        Text(
            text = stringResource(id = R.string.liked_trips),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(
            modifier = Modifier
                .padding(spacing.medium)
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(200.dp)
                .verticalScroll(rememberScrollState())
        ) {
            repeat(5)
            {
                TripUserRow()
            }
        }

        Button(
            onClick = {
                navController.navigate(ProfileScreen.AddTrip.route)
            },
            modifier = Modifier
                .width(216.dp)
        ) {
            Text(text = stringResource(id = R.string.add_trips))
        }
    }
}


package com.example.travelme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelme.*
import com.example.travelme.R
import com.example.travelme.navigation.Auth
import com.example.travelme.navigation.ProfileScreen
import com.example.travelme.ui.components.TripDetails
import com.example.travelme.ui.theme.spacing
import com.example.travelme.viewmodels.TripVM

@Composable
fun MyTripsScreen(navController: NavHostController) {
    val tripsListLike: MutableState<ArrayList<TripVM>> = remember { mutableStateOf(ArrayList()) }
    val tripsListDone: MutableState<ArrayList<TripVM>> = remember { mutableStateOf(ArrayList()) }

    LaunchedEffect(Unit) {
        StoreViewModel.storeViewModel.getLikedTrips(
            onSuccess = { result ->
                tripsListLike.value = result
            },
            onFailure = {}
        )

        StoreViewModel.storeViewModel.getDoneTrips(
            onSuccess = { result ->
                tripsListDone.value = result
            },
            onFailure = {}
        )
    }

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
            for(i in 0..tripsListDone.value.size - 1)
            {
                TripDetails(
                    tripsListDone.value[i],
                    onLikeClick = {
                        StoreViewModel.storeViewModel.like(tripsListDone.value[i].id, {}, {})
                    },
                    onDoneClick = { StoreViewModel.storeViewModel.done(tripsListDone.value[i].id, {}, {}) }
                )
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
            for(i in 0..tripsListLike.value.size - 1)
            {
                TripDetails(
                    tripsListLike.value[i],
                    onLikeClick = { StoreViewModel.storeViewModel.like(tripsListLike.value[i].id, {}, {}) },
                    onDoneClick = { StoreViewModel.storeViewModel.done(tripsListLike.value[i].id, {}, {}) }
                )
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


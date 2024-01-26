package com.example.travelme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.travelme.*
import com.example.travelme.R
import com.example.travelme.navigation.ProfileScreen
import com.example.travelme.ui.components.TripDetails
import com.example.travelme.ui.theme.spacing
import com.example.travelme.viewmodels.TripVM

@Composable
fun MyTripsScreen(
    navController: NavHostController,
    viewModel: TripVM = hiltViewModel()
) {
    val liked by viewModel.liked.observeAsState(initial = emptyList())
    val done by viewModel.done.observeAsState(initial = emptyList())
    val trips by viewModel.trips.observeAsState(initial = emptyList())
    val context = LocalContext.current

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
                .height(300.dp)
                .verticalScroll(rememberScrollState())
        ) {
            for (i in trips.indices) {
                if (trips[i].tripid in done.map { it.tripid })
                    TripDetails(
                        trips[i],
                        liked,
                        done,
                        onLikeClick = {
                            StoreViewModel.storeViewModel.like(trips[i].tripid, {}, {})
                            viewModel.createLike(trips[i], context)
                        },
                        onDoneClick = {
                            StoreViewModel.storeViewModel.done(trips[i].tripid, {}, {})
                            viewModel.createDone(trips[i], context)
                        }
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
                .height(300.dp)
                .verticalScroll(rememberScrollState())
        ) {
            for (i in trips.indices) {
                if (trips[i].tripid in liked.map { it.tripid })
                    TripDetails(
                        trips[i],
                        liked,
                        done,
                        onLikeClick = {
                            StoreViewModel.storeViewModel.like(trips[i].tripid, {}, {})
                            viewModel.createLike(trips[i], context)
                        },
                        onDoneClick = {
                            StoreViewModel.storeViewModel.done(trips[i].tripid, {}, {})
                            viewModel.createDone(trips[i], context)
                        }
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





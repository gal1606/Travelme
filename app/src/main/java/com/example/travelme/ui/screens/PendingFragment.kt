package com.example.travelme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.travelme.StoreViewModel
import com.example.travelme.ui.components.TripDetailsAdmin
import com.example.travelme.ui.theme.spacing
import com.example.travelme.viewmodels.TripVM

@Composable
fun PendingFragment(
    navController: NavHostController,
    viewModel: TripVM = hiltViewModel(),
) {
    val spacing = spacing
    val context = LocalContext.current
    val trips by viewModel.trips.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .padding(spacing.medium)
                .background(Color.LightGray)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            for(i in trips.indices) {
                TripDetailsAdmin(
                    trips[i],
                    onApplyClick = {
                        StoreViewModel.storeViewModel.applyTrip(trips[i].tripid, {}, {})
                        viewModel.applyTrip(trips[i], context)
                    },
                    onDeclineClick = {
                        StoreViewModel.storeViewModel.declineTrip(trips[i].tripid, {}, {})
                        viewModel.declineTrip(trips[i], context)
                    }
                )
            }
        }
    }
}

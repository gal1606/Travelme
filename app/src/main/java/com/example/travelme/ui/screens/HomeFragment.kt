package com.example.travelme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelme.*
import com.example.travelme.R
import com.example.travelme.models.Trip
import com.example.travelme.navigation.Auth
import com.example.travelme.ui.components.TripDetails
import com.example.travelme.ui.theme.spacing
import com.example.travelme.viewmodels.TripVM

@Composable
fun HomeFragment(navController: NavHostController) {
    val tripsList: MutableState<ArrayList<TripVM>> = remember { mutableStateOf(ArrayList()) }

    LaunchedEffect(Unit) {
        AuthViewModel.authViewModel.isUserLoggedIn(
            onSuccess = { isLogged ->
                if (!isLogged)
                {
                    navController.navigate(route = Auth.Login.route)
                }
            },
            onFailure = { exception ->
                DialogMessage.dialogMessage = exception.message.toString()
                ShowDialog.showDialog.value = true
            }
        )

        StoreViewModel.storeViewModel.getTrips(
            onSuccess = { result ->
                tripsList.value = result
            },
            onFailure = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_back)
        )

        Text(
            text = CurrentUser.currentUser.name
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(spacing.medium)
                .padding(top = spacing.extraLarge)
                .height(400.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for(i in 0..tripsList.value.size - 1)
            {
                TripDetails(
                    tripsList.value[i],
                    onLikeClick = { StoreViewModel.storeViewModel.like(tripsList.value[i].id, {}, {}) },
                    onDoneClick = { StoreViewModel.storeViewModel.done(tripsList.value[i].id, {}, {}) }
                )
            }
        }
    }
}
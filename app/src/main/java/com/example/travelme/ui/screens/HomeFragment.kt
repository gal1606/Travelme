package com.example.travelme.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.travelme.*
import com.example.travelme.R
import com.example.travelme.ui.components.TripDetails
import com.example.travelme.ui.theme.spacing
import com.example.travelme.viewmodels.TripVM
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.travelme.navigation.Graph

@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeFragment(
    navController: NavHostController,
    viewModel: TripVM = hiltViewModel(),
) {
    val trips by viewModel.tripsApplied.observeAsState(initial = emptyList())
    val liked by viewModel.liked.observeAsState(initial = emptyList())
    val done by viewModel.done.observeAsState(initial = emptyList())
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        AuthViewModel.authViewModel.isUserLoggedIn(
            onSuccess = { isLogged ->
                if (!isLogged)
                {
                    navController.navigate(route = Graph.HOME)
                }
            },
            onFailure = { exception ->
                DialogMessage.dialogMessage = exception.message.toString()
                ShowDialog.showDialog.value = true
            }
        )
    }

    if (CurrentUser.currentUser.email == "admin@gmail.com")
        UsersFragment()
    else

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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for(i in trips.indices) {

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
    }
}
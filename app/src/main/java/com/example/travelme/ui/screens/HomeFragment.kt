package com.example.travelme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.travelme.*
import com.example.travelme.R
import com.example.travelme.navigation.Auth
import com.example.travelme.ui.components.TripUserRow
import com.example.travelme.ui.theme.spacing

@Composable
fun HomeFragment(navController: NavHostController) {

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
                .padding(top = spacing.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(5)
            {
                TripUserRow()
            }
        }
    }
}
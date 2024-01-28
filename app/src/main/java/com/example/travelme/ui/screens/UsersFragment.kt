package com.example.travelme.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelme.AuthViewModel
import com.example.travelme.StoreViewModel
import com.example.travelme.models.User
import com.example.travelme.ui.components.UserDetails
import com.example.travelme.ui.theme.spacing

@SuppressLint("MutableCollectionMutableState", "SuspiciousIndentation")
@Composable
fun UsersFragment() {

    var users by remember { mutableStateOf<ArrayList<User>>(arrayListOf()) }
    var showUsers by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        StoreViewModel.storeViewModel.getUsers(
            onSuccess = {
                showUsers = true
                users = it
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

        Column(
            modifier = Modifier
                .padding(spacing.medium)
                .background(Color.LightGray)
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            if (showUsers)
            for ( i in 0..< users.size)
            {
                UserDetails(users[i])
            }
        }
    }
}

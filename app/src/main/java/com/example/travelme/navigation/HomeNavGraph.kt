package com.example.travelme.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.travelme.ui.components.BottomBarScreen
import com.example.travelme.ui.screens.*

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Home.route
    ) {
        authNavGraph(navController = navController)
        composable(route = BottomBarScreen.Home.route) {
            HomeFragment(navController)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileFragment(navController)
        }
        composable(route = BottomBarScreen.Search.route) {
            SearchTripsScreen()
        }
        myTripsNavGraph(navController)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.myTripsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.MYTRIPS,
        startDestination = ProfileScreen.MyTrips.route
    ) {
        composable(route = ProfileScreen.MyTrips.route) {
            MyTripsScreen(navController)
        }
        composable(route = ProfileScreen.AddTrip.route) {
            AddTripScreen()
        }
    }
}

sealed class ProfileScreen(val route: String) {
    object MyTrips : ProfileScreen(route = "MYTRIPS")
    object AddTrip : ProfileScreen(route = "ADDTRIP")
}

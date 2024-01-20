package com.example.travelme.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.travelme.*
import com.example.travelme.ui.auth.AuthScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {

    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Auth.Login.route
    ) {
        composable(route = Auth.Login.route) {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                AuthViewModel.authViewModel.isUserLoggedIn(
                    onSuccess = { isLogged ->
                        if (isLogged)
                        {
                            AuthViewModel.authViewModel.getUser(
                                onSuccess = { user ->
                                    CurrentUser.currentUser = user
                                    navController.navigate(Graph.HOME)
                                },
                                onFailure = { exception ->
                                    DialogMessage.dialogMessage = exception.message.toString()
                                    ShowDialog.showDialog.value = true
                                }
                            )
                        }
                    },
                    onFailure = { exception ->
                        DialogMessage.dialogMessage = exception.message.toString()
                        ShowDialog.showDialog.value = true
                    }
                )
            }

            AuthScreen(
                name = Auth.Login.route,
                onClick = { navController.navigate(Auth.SignUp.route) },
                onAuthClick = {
                    if (AuthViewModel.authViewModel.email.isEmpty() || AuthViewModel.authViewModel.password.isEmpty()) {
                        DialogMessage.dialogMessage = context.getString(R.string.error_empty_fields)
                        ShowDialog.showDialog.value = true
                    } else {
                        AuthViewModel.authViewModel.login(
                            onSuccess = {
                                CurrentUser.currentUser = it
                                navController.navigate(Graph.HOME)
                            },
                            onFailure = { exception ->
                                DialogMessage.dialogMessage = exception.message.toString()
                                ShowDialog.showDialog.value = true
                            })
                    }
                }
            )
        }
        composable(route = Auth.SignUp.route) {
            val context = LocalContext.current

            AuthScreen(
                name = Auth.SignUp.route,
                onClick = {
                    navController.popBackStack(
                        route = Auth.Login.route,
                        inclusive = false
                    )
                },
                onAuthClick = {
                    if (AuthViewModel.authViewModel.email.isEmpty() || AuthViewModel.authViewModel.password.isEmpty() || AuthViewModel.authViewModel.name.isEmpty()) {
                        DialogMessage.dialogMessage = context.getString(R.string.error_empty_fields)
                        ShowDialog.showDialog.value = true
                    } else {
                        AuthViewModel.authViewModel.register(
                            onSuccess = {
                                CurrentUser.currentUser = it
                                StoreViewModel.storeViewModel.addUser(CurrentUser.currentUser, {}, {})
                                navController.navigate(Graph.HOME)
                            },
                            onFailure = { exception ->
                                DialogMessage.dialogMessage = exception.message.toString()
                                ShowDialog.showDialog.value = true
                            }
                        )
                    }
                }
            )
        }
    }
}

sealed class Auth(val route: String) {
    object Login: Auth(route="LOGIN")
    object SignUp: Auth(route="SIGNUP")
}
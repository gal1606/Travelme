package com.example.travelme.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.travelme.AuthViewModel
import com.example.travelme.CurrentUser
import com.example.travelme.R
import com.example.travelme.StoreViewModel
import com.example.travelme.navigation.Auth
import com.example.travelme.navigation.Graph
import com.example.travelme.navigation.ProfileScreen
import com.example.travelme.ui.components.BottomBarScreen
import com.example.travelme.ui.theme.spacing
import com.example.travelme.uriToBitmap

@Composable
fun ProfileFragment(navController: NavHostController) {
    val context = LocalContext.current
    var spinner by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf(CurrentUser.currentUser.name) }
    var imageUri by remember { mutableStateOf<Uri?>(CurrentUser.currentUser.profileImage.toUri()) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )

    if (spinner)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = spacing.medium),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Box(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = CurrentUser.currentUser.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (imageUri.toString() == "null" ) {
            spinner = false
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(128.dp)
                    .clickable { galleryLauncher.launch("image/*") }
            )
        }
        else
            Image (
                painter = rememberAsyncImagePainter(
                    model = imageUri,
                    onSuccess = {
                        spinner = false
                }),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(128.dp)
                    .clickable { galleryLauncher.launch("image/*") }
            )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(spacing.medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text(text = stringResource(id = R.string.name))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text
                    )
                )
            }

            Button(
                onClick = {
                    spinner = true

                    var img: Bitmap? = null
                    if (imageUri.toString() != "null")
                    {
                        val imageBitmap = uriToBitmap(imageUri!!, context)
                        if (imageBitmap != null) {
                            img = imageBitmap
                        }
                    }
                    AuthViewModel.authViewModel.updateProfile(
                        img,
                        name,
                        onSuccess = { user ->
                            CurrentUser.currentUser.profileImage = user.profileImage
                            CurrentUser.currentUser.name = user.name
                            StoreViewModel.storeViewModel.updateUser(
                                user = CurrentUser.currentUser,
                                onSuccess = {
                                    navController.navigate(Graph.HOME)
                                },
                                onFailure = {}
                            )
                        },
                        onFailure = {}
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = spacing.extraLarge)
            ) {
                Text(text = stringResource(id = R.string.save))
            }

            Button(
                onClick = { navController.navigate(ProfileScreen.MyTrips.route) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = spacing.medium)
            ) {
                Text(text = stringResource(id = R.string.my_trips))
            }

            Button(
                onClick = {
                    AuthViewModel.authViewModel.logout(
                        onSuccess = {
                            AuthViewModel.authViewModel.email = ""
                            AuthViewModel.authViewModel.password = ""
                            CurrentUser.currentUser.email = ""

                            navController.navigate(Graph.AUTHENTICATION)
                        },
                        onFailure = { }
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = spacing.medium)
            ) {
                Text(text = stringResource(id = R.string.logout))
            }
        }
    }
}




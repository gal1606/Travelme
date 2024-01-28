package com.example.travelme.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.travelme.R
import com.example.travelme.models.Trip
import com.example.travelme.models.User
import com.example.travelme.models.UserDone
import com.example.travelme.models.UserLike
import com.example.travelme.ui.theme.spacing

@Composable
fun UserDetails(
    user: User
) {
    var spinner by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = spacing.large)
    ) {
        if (user.profileImage == "null" ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                alignment = Alignment.Center
            )
        }
        else {
            if (spinner)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.profileImage,
                    onSuccess = {
                        spinner = false
                    },
                    onError = {
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                alignment = Alignment.Center
            )
        }
        Text(
            text = user.name,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(spacing.medium)
        )

        Text(
            text = user.email,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(spacing.medium)
        )
    }
}





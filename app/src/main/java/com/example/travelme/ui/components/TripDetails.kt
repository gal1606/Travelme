package com.example.travelme.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.travelme.R
import com.example.travelme.models.Trip
import com.example.travelme.models.UserDone
import com.example.travelme.models.UserLike
import com.example.travelme.ui.theme.spacing

@Composable
fun TripDetails(
    trip: Trip,
    liked: List<UserLike>,
    done: List<UserDone>,
    onLikeClick: (String) -> Unit,
    onDoneClick: (String) -> Unit,) {

    var spinner by remember { mutableStateOf(true) }
    var likeEnabled by remember { mutableStateOf(true) }
    var doneEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        likeEnabled = trip.tripid !in liked.map { it.tripid }
        doneEnabled = trip.tripid !in done.map { it.tripid }
    }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = spacing.large)
        ) {

            if (spinner)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                    Image(
                    painter = rememberAsyncImagePainter(
                    model = trip.imageUrl,
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

            Text(
                text = trip.description,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(spacing.medium)
            )

            Row(
                modifier = Modifier
                    .padding(spacing.medium)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.level),
                    color = colorResource(id = R.color.purple_500),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = trip.level,
                    color = colorResource(id = R.color.purple_700),
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = stringResource(id = R.string.length),
                    color = colorResource(id = R.color.purple_500),
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = trip.length.toString() + stringResource(id = R.string.km),
                    color = colorResource(id = R.color.purple_700),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Row(
                modifier = Modifier
                    .padding(spacing.medium)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.time),
                    color = colorResource(id = R.color.purple_500),
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = trip.time.toString() + stringResource(id = R.string.hours),
                    color = colorResource(id = R.color.purple_700),
                    style = MaterialTheme.typography.titleLarge,
                )

                Button(
                    onClick = {
                        likeEnabled = false
                        onLikeClick(trip.tripid)
                    },
                    modifier = Modifier

                        .width(60.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = likeEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Navigation Icon"
                    )
                }

                Button(
                    onClick = {
                        doneEnabled = false
                        onDoneClick(trip.tripid)
                    },
                    modifier = Modifier
                        .width(60.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = doneEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Navigation Icon"
                    )
                }
            }
        }
}





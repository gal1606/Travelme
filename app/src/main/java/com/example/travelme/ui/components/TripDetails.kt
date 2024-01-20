package com.example.travelme.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.travelme.R
import com.example.travelme.StoreViewModel
import com.example.travelme.ui.theme.spacing
import com.example.travelme.viewmodels.TripVM

@Composable
fun TripDetails(
    tripViewModel: TripVM = remember { TripVM() },
    onLikeClick: (String) -> Unit,
    onDoneClick: (String) -> Unit,) {

    var spinner by remember { mutableStateOf(true) }
    var likeEnabled by remember { mutableStateOf(true) }
    var doneEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        StoreViewModel.storeViewModel.isTripLiked(
            tripViewModel.id,
            onSuccess = { result ->
                likeEnabled = !result
            },
            onFailure = {}
        )

        StoreViewModel.storeViewModel.isTripDone(
            tripViewModel.id,
            onSuccess = { result ->
                doneEnabled = !result
            },
            onFailure = {}
        )
    }
    if (tripViewModel.images.size > 0) {
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
                    model = tripViewModel.images[0],
                    onSuccess = {
                        spinner = false
                    },
                    onError = {
                        var d = 5
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),

                alignment = Alignment.Center
            )

            Text(
                text = tripViewModel.description,
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
                    text = tripViewModel.level,
                    color = colorResource(id = R.color.purple_700),
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = stringResource(id = R.string.length),
                    color = colorResource(id = R.color.purple_500),
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = tripViewModel.length.toString() + stringResource(id = R.string.km),
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
                    text = tripViewModel.time.toString() + stringResource(id = R.string.hours),
                    color = colorResource(id = R.color.purple_700),
                    style = MaterialTheme.typography.titleLarge,
                )

                Button(
                    onClick = {
                        likeEnabled = false
                        onLikeClick(tripViewModel.id)
                    },
                    modifier = Modifier

                        .width(60.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = likeEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Navigation Icon"
                    )
                }

                Button(
                    onClick = {
                        doneEnabled = false
                        onDoneClick(tripViewModel.id)
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
}



package com.example.travelme.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.travelme.R
import com.example.travelme.ShowDialog

@Composable
fun ErrorDialog(dialogMessage: String) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { ShowDialog.showDialog.value = false },
        text = { Text(dialogMessage) },
        confirmButton = {
            Button(
                onClick = { ShowDialog.showDialog.value = false }
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    )
}



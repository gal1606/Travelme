package com.example.travelme.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travelme.models.Trip


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelDropDown(trip: MutableState<Trip>) {
    val levels = arrayOf("", "easy", "medium", "hard")
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(192.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier.align(Alignment.CenterEnd),
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = trip.value.level,
                onValueChange = {
                    trip.value.level = it
                },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                levels.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            trip.value.level = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
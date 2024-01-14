package com.example.travelme.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.travelme.ui.components.AdminFooter
import com.example.travelme.ui.components.TripAdminRow
import com.example.travelme.ui.theme.AppTheme
import com.example.travelme.ui.theme.spacing

@Composable
fun AdminPendingScreen(navController: NavHostController) {
    val spacing = spacing
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
                .height(400.dp)
                .verticalScroll(rememberScrollState())
        ) {
            repeat(10)
            {
                TripAdminRow()
            }
        }

        AdminFooter()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AdminPendingScreenPreviewLight() {
    AppTheme {
        AdminPendingScreen(rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AdminPendingScreenPreviewDark() {
    AppTheme {
        AdminPendingScreen(rememberNavController())
    }
}

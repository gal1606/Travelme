package com.example.travelme.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelme.R
import com.example.travelme.ui.theme.AppTheme

@Composable
fun AdminFooter() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            Arrangement.Center
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(108.dp)
            ) {
                Text(text = stringResource(id = R.string.users))
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(108.dp)
            ) {
                Text(text = stringResource(id = R.string.pending))
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(108.dp)
            ) {
                Text(text = stringResource(id = R.string.logout))
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AdminFooterLight() {
    AppTheme {
        AdminFooter()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AdminFooterDark() {
    AppTheme {
        AdminFooter()
    }
}


package com.example.travelme.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelme.R
import com.example.travelme.ui.theme.AppTheme

@Composable
fun TripUserRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = stringResource(id = R.string.empty),
            modifier = Modifier.size(32.dp)
        )

        Text(
            text = stringResource(id = R.string.trip_description),
            color = MaterialTheme.colorScheme.onSurface
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier

                .width(60.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = stringResource(id = R.string.like),
                fontSize = 14.sp,)
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .width(60.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = stringResource(id = R.string.done)
                ,
                fontSize = 14.sp,)
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun TripUserRowLight() {
    AppTheme {
        TripUserRow()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TripUserRowDark() {
    AppTheme {
        TripUserRow()
    }
}


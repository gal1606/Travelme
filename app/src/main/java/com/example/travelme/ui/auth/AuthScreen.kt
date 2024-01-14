package com.example.travelme.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.travelme.AuthViewModel
import com.example.travelme.DialogMessage
import com.example.travelme.R
import com.example.travelme.ShowDialog
import com.example.travelme.navigation.*
import com.example.travelme.ui.components.AuthHeader
import com.example.travelme.ui.components.ErrorDialog
import com.example.travelme.ui.theme.spacing

@Composable
fun AuthScreen(
    name: String,
    onClick: () -> Unit,
    onAuthClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (refHeader, refName, refEmail, refPassword, refButtonLogin, refTextSignup) = createRefs()
        val spacing = spacing

        Box(
            modifier = Modifier
                .constrainAs(refHeader) {
                    top.linkTo(parent.top, spacing.large)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .wrapContentSize()
        ) {
            AuthHeader()
        }

        if (ShowDialog.showDialog.value) {
            ErrorDialog(DialogMessage.dialogMessage)
        }

        TextField(
            value = AuthViewModel.authViewModel.name,
            onValueChange = {
                AuthViewModel.authViewModel.name = it
            },
            label = {
                Text(text = stringResource(id = R.string.name))
            },
            modifier = Modifier
                .alpha(if(name == Auth.SignUp.route) 1f else 0f)
                .constrainAs(refName) {
                    top.linkTo(refHeader.bottom, spacing.large)
                    start.linkTo(parent.start, spacing.large)
                    end.linkTo(parent.end, spacing.large)
                    width = Dimension.fillToConstraints

                },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        TextField(
            value = AuthViewModel.authViewModel.email,
            onValueChange = {
                AuthViewModel.authViewModel.email = it
            },
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            modifier = Modifier.constrainAs(refEmail) {
                top.linkTo(refName.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        TextField(
            value = AuthViewModel.authViewModel.password,
            onValueChange = {
                AuthViewModel.authViewModel.password = it
            },
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            modifier = Modifier.constrainAs(refPassword) {
                top.linkTo(refEmail.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Button(
            modifier = Modifier
                .constrainAs(refButtonLogin) {
                    top.linkTo(refPassword.bottom, spacing.large)
                    start.linkTo(parent.start, spacing.extraLarge)
                    end.linkTo(parent.end, spacing.extraLarge)
                    width = Dimension.fillToConstraints
                },
            onClick = { onAuthClick() }
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            modifier = Modifier
                .constrainAs(refTextSignup) {
                    top.linkTo(refButtonLogin.bottom, spacing.medium)
                    start.linkTo(parent.start, spacing.extraLarge)
                    end.linkTo(parent.end, spacing.extraLarge)
                }
                .clickable { onClick() },
            text = (if (name == Auth.Login.route) stringResource(id = R.string.dont_have_account) else stringResource(id=R.string.already_have_account)),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


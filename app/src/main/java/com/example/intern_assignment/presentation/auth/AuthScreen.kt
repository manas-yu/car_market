package com.example.intern_assignment.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.intern_assignment.data.model.UserState
import com.example.intern_assignment.R
import com.example.intern_assignment.SupabaseAuthViewModel
import com.example.intern_assignment.presentation.common.CustomTextField
import com.example.intern_assignment.utils.Dimens

@Composable
fun AuthScreen(
    viewModel: SupabaseAuthViewModel,
    navigateToHomePage: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var rePassWord by remember { mutableStateOf("") }
    var isBuyer by remember { mutableStateOf(true) }
    var isSignUp by remember {
        mutableStateOf(false)

    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()

    ) {

        Spacer(modifier = Modifier.weight(0.3f))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = if (isBuyer) "Buy Your Dream Car!!" else "Sell Your Car Now!!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
            color = if (isBuyer) MaterialTheme.colorScheme.primary else Color.White
        )
        Spacer(modifier = Modifier.weight(0.5f))
        CustomTextField(value = email, onValueChange = {
            email = it
        }, prefix = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_email_24),
                contentDescription = null
            )
        },
            placeholder = {
                Text(
                    text = if (isSignUp) "New Email" else {
                        if (isBuyer) "User Email"
                        else "Seller Email"
                    }
                )
            }
        )
        CustomTextField(value = password, onValueChange = {
            password = it
        }, prefix = {

        },
            placeholder = {
                Text(text = "Password")
            }
        )
        if (isSignUp) {
            CustomTextField(value = rePassWord, onValueChange = {
                rePassWord = it
            }, prefix = {

            },
                placeholder = {
                    Text(text = "Retype Password")
                }
            )
        }
        Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.ExtraSmallPadding2),
            onClick = {
                if (rePassWord != password && isSignUp) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (isSignUp) {
                    println("Calling signUp function")
                    viewModel.signUp(context, email, password, isBuyer)
                } else {
                    println("Calling login function")
                    viewModel.login(context, email, password, isBuyer)
                }


            },
            shape = RoundedCornerShape(8.dp)
        ) {
            if (viewModel.userState.value is UserState.Loading) {
                CircularProgressIndicator(color = Color.Black)
            } else
                Text(text = if (isSignUp) "SignUp" else "Login")
        }
        OutlinedButton(

            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,

                ),
            onClick = {
                isSignUp = !isSignUp
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (isSignUp) "Login?" else "Signup?", textAlign = TextAlign.Center)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (isBuyer) "Want To Sell?" else "Want To Buy?",
            )
            TextButton(onClick = {
                isBuyer = !isBuyer
            }) {
                Text(text = "Click Here", color = MaterialTheme.colorScheme.primary)
            }
        }
        when (viewModel.userState.value) {
            is UserState.Loading -> {
            }

            is UserState.Error -> {
                Toast.makeText(
                    context,
                    (viewModel.userState.value as UserState.Error).message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                viewModel.changeUserState(UserState.NullState)
            }

            is UserState.Success -> {
                viewModel.changeUserState(UserState.NullState)
                println("NNavigating to homescreen")
                navigateToHomePage(isBuyer)

            }

            else -> {

            }

        }
        Spacer(modifier = Modifier.weight(0.2f))
    }
}
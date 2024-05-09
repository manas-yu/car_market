package com.example.intern_assignment.presentation.buyer

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.intern_assignment.SupabaseAuthViewModel
import com.example.intern_assignment.data.model.UserState

@Composable
fun BuyerScreen(
    viewModel: SupabaseAuthViewModel, onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()

    ) {
        val context = LocalContext.current
        Row {
            Text(text = "Buyer Screen : " + viewModel.currentUser.email)
            IconButton(onClick = {
                viewModel.logout(context)


            }) {
                when (viewModel.userState.value) {
                    is UserState.Loading -> {
                    }

                    is UserState.Error -> {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.changeUserState(UserState.NullState)
                    }

                    is UserState.Success -> {
                        viewModel.changeUserState(UserState.NullState)
                        onLogout()

                    }

                    else -> {

                    }

                }

                Icon(imageVector = Icons.Default.Logout, contentDescription = null)
            }
        }
    }
}
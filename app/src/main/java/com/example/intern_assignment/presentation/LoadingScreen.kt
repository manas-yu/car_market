package com.example.intern_assignment.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.intern_assignment.SupabaseAuthViewModel
import com.example.intern_assignment.data.model.UserState

@Composable
fun LoadingScreen(
    viewModel: SupabaseAuthViewModel, navigateToStart: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it))
        LoadingComponent()
        when (viewModel.userState.value) {
            is UserState.NullState -> {
            }

            is UserState.Loading -> {
            }

            is UserState.Error -> {
                navigateToStart()
                viewModel.changeUserState(UserState.NullState)
            }

            is UserState.Success -> {
                navigateToStart()
                viewModel.changeUserState(UserState.NullState)
            }
        }
    }

}
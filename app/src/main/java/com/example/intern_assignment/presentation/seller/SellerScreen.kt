package com.example.intern_assignment.presentation.seller

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.intern_assignment.SupabaseAuthViewModel
import com.example.intern_assignment.data.model.Car
import com.example.intern_assignment.data.model.UserState
import com.example.intern_assignment.presentation.LoadingComponent
import com.example.intern_assignment.presentation.seller.components.CarCard
import com.example.intern_assignment.utils.Dimens.ExtraSmallPadding
import com.example.intern_assignment.utils.Dimens.ExtraSmallPadding2

@Composable
fun SellerScreen(
    navigateToDetails: (Car, String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sellerViewModel: SellerViewModel,
    viewModel: SupabaseAuthViewModel, onLogout: () -> Unit, navigateToAddCar: () -> Unit
) {
    LaunchedEffect(Unit) {
        sellerViewModel.getContent(viewModel.currentUser.id)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddCar,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(it)

        ) {
            when (sellerViewModel.userState.value) {
                is UserState.Loading -> {
                    LoadingComponent()
                }

                is UserState.Error -> {
                    Toast.makeText(LocalContext.current, "error occurred", Toast.LENGTH_SHORT)
                        .show()
                    Text(text = "error occurred")
                }

                is UserState.Success -> {

                    val context = LocalContext.current
                    Row(
                        Modifier.padding(
                            horizontal = ExtraSmallPadding,
                            vertical = ExtraSmallPadding2
                        )
                    ) {
                        Text(text = "Your Cars ", style = MaterialTheme.typography.displaySmall)
                        Spacer(modifier = Modifier.weight(1f))
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

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null
                            )
                        }
                    }
                    val scrollableState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollableState)
                    ) {
                        sellerViewModel.contentList.forEachIndexed { index, car ->

                            println("content size : " + sellerViewModel.contentList.size)
                            println("image urls " + sellerViewModel.imageUrls.size)
                            CarCard(
                                car = car,
                                navigateToDetails = {
                                    navigateToDetails(car, sellerViewModel.imageUrls[index])
                                },
                                imageUrl = sellerViewModel.imageUrls[index]
                            )
                        }
                    }

                }

                else -> {

                }
            }


        }

    }


}
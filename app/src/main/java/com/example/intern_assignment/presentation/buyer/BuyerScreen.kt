package com.example.intern_assignment.presentation.buyer

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.intern_assignment.SupabaseAuthViewModel
import com.example.intern_assignment.data.model.Car
import com.example.intern_assignment.data.model.UserState
import com.example.intern_assignment.presentation.LoadingComponent
import com.example.intern_assignment.presentation.seller.SellerViewModel
import com.example.intern_assignment.utils.Dimens

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BuyerScreen(
    navigateToDetails: (Car, Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sellerViewModel: BuyerViewModel,
    viewModel: SupabaseAuthViewModel, onLogout: () -> Unit, navigateToAddCar: () -> Unit
) {
    var liked by remember {
        mutableStateOf(false)
    }
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
                            horizontal = Dimens.ExtraSmallPadding,
                            vertical = Dimens.ExtraSmallPadding2
                        )
                    ) {
                        Text(text = "Buy Cars ", style = MaterialTheme.typography.displaySmall)
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
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.DarkGray
                                ),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        navigateToDetails(car, index)
                                    }
                                    .wrapContentHeight()
                            ) {
                                Column {
                                    AsyncImage(
                                        modifier = Modifier
                                            .height(150.dp)
                                            .sharedElement(
                                                state = rememberSharedContentState(key = "imageUrl/${car.imageUrl}/$index"),
                                                animatedVisibilityScope = animatedVisibilityScope,

                                                ),
                                        model = car.imageUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                    Row(
                                        modifier = Modifier.padding(
                                            start = 12.dp,
                                            top = 9.dp,
                                            bottom = 11.dp,
                                            end = Dimens.ExtraSmallPadding2
                                        )
                                    ) {
                                        Column {
                                            Text(
                                                text = car.carBrand,
                                                style = MaterialTheme.typography.titleMedium,
                                                modifier = Modifier.sharedElement(
                                                    state = rememberSharedContentState(key = "carBrand/${car.carBrand}/$index"),
                                                    animatedVisibilityScope = animatedVisibilityScope,

                                                    )
                                            )
                                            Text(
                                                text = car.carModel,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleLarge,
                                                modifier = Modifier.sharedElement(
                                                    state = rememberSharedContentState(key = "carModel/${car.carModel}/$index"),
                                                    animatedVisibilityScope = animatedVisibilityScope,

                                                    )
                                            )

                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        Column {
                                            Text(
                                                text = "₹${car.carCost} Lakhs",
                                                style = MaterialTheme.typography.titleLarge,
                                                modifier = Modifier.sharedElement(
                                                    state = rememberSharedContentState(key = "carCost/${car.carCost}/$index"),
                                                    animatedVisibilityScope = animatedVisibilityScope,

                                                    )
                                            )
                                            Text(
                                                text = "${car.carMileage} L/100Km",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.sharedElement(
                                                    state = rememberSharedContentState(key = "carMileage/${car.carMileage}/$index"),
                                                    animatedVisibilityScope = animatedVisibilityScope,

                                                    )
                                            )
                                        }
                                        IconButton(onClick = {
                                            liked = !liked
                                        }) {
                                            Icon(
                                                imageVector = if (!liked) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                                                contentDescription = null,
                                                tint = if (liked) MaterialTheme.colorScheme.primary else Color.Black
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }

                }

                else -> {

                }
            }


        }

    }


}
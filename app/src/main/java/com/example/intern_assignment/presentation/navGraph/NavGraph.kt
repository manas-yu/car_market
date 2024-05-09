package com.example.intern_assignment.presentation.navGraph

import android.content.Intent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.basic_assignment.data.model.Content
import com.example.intern_assignment.SupabaseAuthViewModel
import com.example.intern_assignment.SupabaseViewModel
import com.example.intern_assignment.data.model.Car
import com.example.intern_assignment.presentation.auth.AuthScreen
import com.example.intern_assignment.presentation.seller.AddCarScreen
import com.example.intern_assignment.presentation.buyer.BuyerScreen
import com.example.intern_assignment.presentation.details.DetailsScreen
import com.example.intern_assignment.presentation.seller.SellerScreen
import com.example.intern_assignment.presentation.seller.SellerViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val sellerViewModel = SellerViewModel()
    val viewModel = SupabaseAuthViewModel()
    val supabaseViewModel = SupabaseViewModel()
    val context = LocalContext.current
    val startDestination = viewModel.startDestination
    LaunchedEffect(Unit) {
//        viewModel.isUserLoggedIn(context)
//        viewModel.changeUserState(UserState.NullState)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        ) {
        val bottomPadding = it.calculateBottomPadding()
        SharedTransitionLayout {
            NavHost(
                navController = navController, startDestination = startDestination,
                modifier = Modifier.padding(bottom = bottomPadding)
            ) {
                composable(
                    route = Routes.DetailsScreen.route + "/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType })
                )
                { backStack ->
                    val index = backStack.arguments?.getInt("index") ?: 0
                    navController.previousBackStackEntry?.savedStateHandle?.get<Car?>("car")
                        ?.let { car ->
                            DetailsScreen(

                                navigateBack = {
                                    navController.popBackStack()
                                },
                                car = car,
                                animatedVisibilityScope = this, index = index
                            )
                        }

                }
                composable(route = Routes.AddCarScreen.route) {
                    AddCarScreen(authViewModel = viewModel, sellerViewModel = sellerViewModel,
                        viewModel = supabaseViewModel,
                        navigateBack = {
                            navController.popBackStack()
                            navController.navigate(Routes.SellerScreen.route)
                        }
                    )
                }
                composable(route = Routes.AuthScreen.route) {
                    AuthScreen(viewModel = viewModel, navigateToHomePage = { isBuyer ->
                        if (isBuyer) {
                            navController.popBackStack()
                            navController.navigate(Routes.BuyerScreen.route)
                        } else {
                            navController.popBackStack()
                            navController.navigate(Routes.SellerScreen.route)
                        }
                    })
                }

                composable(route = Routes.BuyerScreen.route) {
                    BuyerScreen(viewModel, onLogout = {
                        navController.popBackStack()
                        navController.navigate(Routes.AuthScreen.route)
                    })
                }
                composable(route = Routes.SellerScreen.route) {
                    SellerScreen(
                        navigateToDetails = { car, index ->
                            navigateToDetails(navController, car, index)
                        },
                        sellerViewModel = sellerViewModel,
                        animatedVisibilityScope = this,
                        viewModel = viewModel,
                        onLogout = {
                            navController.popBackStack()
                            navController.navigate(Routes.AuthScreen.route)
                        },
                        navigateToAddCar = {
                            navController.navigate(Routes.AddCarScreen.route)
                        }
                    )
                }

            }
        }

    }
}

private fun navigateToDetails(navController: NavController, car: Car, index: Int) {
    navController.currentBackStackEntry?.savedStateHandle?.set(
        "car",
        car
    )
    navController.navigate(Routes.DetailsScreen.route + "/$index")
}
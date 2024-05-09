package com.example.intern_assignment.presentation.navGraph

sealed class Routes(val route: String) {
    data object AuthScreen : Routes("auth_screen")
    data object BuyerScreen : Routes("buyer_screen")
    data object SellerScreen : Routes("seller_screen")
    data object DetailsScreen : Routes("details_screen")
    data object AddCarScreen : Routes("add_car_screen")
    data object LoadingScreen : Routes("loading_screen")
}

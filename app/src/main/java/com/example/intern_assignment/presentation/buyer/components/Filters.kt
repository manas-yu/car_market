package com.example.intern_assignment.presentation.buyer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.intern_assignment.presentation.buyer.BuyerViewModel
import com.example.intern_assignment.presentation.common.CustomTextField
import com.example.intern_assignment.utils.Dimens

@Composable
fun Filters(
    buyerViewModel: BuyerViewModel, closeSheet: () -> Unit,
) {
    var maxCost by remember { mutableStateOf("") }
    var maxCarMileage by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        maxCost = buyerViewModel.maxCost.value
        maxCarMileage = buyerViewModel.maxCarMileage.value
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(Dimens.MediumPadding1)
            .verticalScroll(scrollState)
    ) {

        Text(text = "Car Brand", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding2))
        BrandFilters(buyerViewModel)
        Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
        Text(text = "Car Cost", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding))
        CustomTextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = maxCost,
            onValueChange = {
                maxCost = it
            },
            prefix = { Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = null) },
            placeholder = { Text(text = "Max Car Cost (Lakhs)") })
        Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
        Text(text = "Car Mileage ", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding))
        CustomTextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = maxCarMileage,
            onValueChange = {
                maxCarMileage = it
            },
            prefix = {
                Icon(imageVector = Icons.Default.LocalGasStation, contentDescription = null)
            },
            placeholder = {
                Text(text = "Max Car Mileage L/100Km")
            })
        Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
        Row(horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {
            Button(

                onClick = {
                    buyerViewModel.maxCost.value = maxCost
                    buyerViewModel.maxCarMileage.value = maxCarMileage
                    buyerViewModel.filterCars()
                    closeSheet()
                }) {
                Text(text = "Save & Explore")
            }
            Spacer(modifier = Modifier.width(Dimens.MediumPadding1))
            Button(

                onClick = {
                    buyerViewModel.maxCost.value = ""
                    buyerViewModel.maxCarMileage.value = ""
                    buyerViewModel.selectedBrands.clear()
                    buyerViewModel.resetList()
                    closeSheet()
                }) {
                Text(text = "Reset Filters")
            }
        }


    }
}
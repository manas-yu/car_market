package com.example.intern_assignment.presentation.buyer.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.intern_assignment.presentation.buyer.BuyerViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandFilters(
    buyerViewModel: BuyerViewModel
) {
    val categories = listOf(
        "Mahindra",
        "Toyota",
        "Lexus",
        "Audi",
        "BMW",
        "Mercedes",
        "Range Rover",
        "Ford",
        "Chevrolet",
        "Hyundai",
    )
    val selected = remember { mutableStateListOf<String>() }
    LaunchedEffect(Unit) {
        selected.addAll(buyerViewModel.selectedBrands)
    }
    FlowRow(maxItemsInEachRow = 3, verticalArrangement = Arrangement.SpaceEvenly) {
        categories.onEach { category ->

            OutlinedButton(

                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected.contains(category)) MaterialTheme.colorScheme.primary else Color.Black,
                    contentColor = if (!selected.contains(category)) MaterialTheme.colorScheme.primary else Color.Black
                ),
                onClick = {
                    if (selected.contains(category)) {
                        selected.remove(category)
                        buyerViewModel.selectedBrands.remove(category)
                    } else {
                        selected.add(category)
                        buyerViewModel.selectedBrands.add(category)
                    }


                },
                shape = CircleShape// Set the corner radius of the button
            ) {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = category,
                    color = if (selected.contains(category)) Color.White else MaterialTheme.colorScheme.primary,
                )
            }


        }
    }

}

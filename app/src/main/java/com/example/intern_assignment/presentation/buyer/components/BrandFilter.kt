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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Categories(

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
    FlowRow(maxItemsInEachRow = 3, verticalArrangement = Arrangement.SpaceEvenly) {
        categories.onEach { category ->
            Box(modifier = Modifier.padding(8.dp)) {
                Button(
                    contentPadding = PaddingValues(vertical = 5.dp, horizontal = 7.dp),
                    modifier = Modifier.border(
                        1.dp,
                        if (!selected.contains(category)) MaterialTheme.colorScheme.primary else Color.White,
                        CircleShape
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected.contains(category)) MaterialTheme.colorScheme.primary else Color.White,
                        contentColor = if (!selected.contains(category)) MaterialTheme.colorScheme.primary else Color.White
                    ),
                    onClick = {
                        if (selected.contains(category)) {
                            selected.remove(category)
                        } else {
                            selected.add(category)
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

}

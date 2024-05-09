package com.example.intern_assignment.presentation.seller.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.intern_assignment.utils.Dimens

@Composable
fun CustomDropdownMenu(
    onSelect: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val options = listOf(
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

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(Dimens.ExtraSmallPadding)
        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        .background(Color.Black, RoundedCornerShape(8.dp))
        .clickable { expanded = true }) {
        Row(
            Modifier.padding(Dimens.ExtraSmallPadding2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedIndex?.let { options[it] } ?: "Select an option",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.weight(1f)) // Add this to push the Icon to the end
            Icon(
                imageVector = Icons.Default.ArrowDropDown, // Arrow icon
                contentDescription = "Arrow Down",
                tint = Color.Black
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },

                ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(modifier = Modifier.fillMaxWidth(),
                        text = {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Text(text = option)
                            }

                        }, onClick = {
                            println("Selected index: $index")
                            selectedIndex = index
                            onSelect(options[index])
                            expanded = false
                        }
                    )
                }
            }
        }
    }

}
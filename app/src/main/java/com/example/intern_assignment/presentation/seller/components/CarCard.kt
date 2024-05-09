package com.example.intern_assignment.presentation.seller.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.intern_assignment.data.model.Car
import com.example.intern_assignment.utils.Dimens

@Composable
fun CarCard(
    car: Car, navigateToDetails: () -> Unit
) {
    var liked by remember {
        mutableStateOf(false)
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray
        ),
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                navigateToDetails()
            }
            .wrapContentHeight()
    ) {
        Column {
            AsyncImage(
                modifier = Modifier.height(150.dp),
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
                    Text(text = car.carBrand, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = car.carModel,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )

                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = "₹${car.carCost} Lakhs",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "${car.carMileage} L/100Km",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
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

package com.example.intern_assignment.presentation.details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.intern_assignment.data.model.Car
import com.example.intern_assignment.presentation.details.components.DescriptionCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailsScreen(
    index: Int,
    navigateBack: () -> Unit,
    car: Car,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var intereseted by remember {
        mutableStateOf(false)
    }
    var liked by remember {
        mutableStateOf(false)
    }
    val scrollableState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(scrollableState)
    ) {
        Row {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                liked = !liked
            }) {
                Icon(
                    imageVector = if (!liked) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                    contentDescription = null,
                    tint = if (liked) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
        AsyncImage(
            modifier = Modifier
                .height(250.dp)
                .sharedElement(
                    state = rememberSharedContentState(key = "imageUrl/${car.imageUrl}/$index"),
                    animatedVisibilityScope = animatedVisibilityScope,

                    ),
            model = car.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.Black)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Row {
                    Column {
                        Text(
                            text = car.carBrand, style = MaterialTheme.typography.titleMedium,
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹${car.carCost} Lakhs",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(key = "carCost/${car.carCost}/$index"),
                                animatedVisibilityScope = animatedVisibilityScope,

                                )
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
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
                DescriptionCard()
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (intereseted) MaterialTheme.colorScheme.primary else Color.Transparent

                    ),
                    onClick = {
                        intereseted = !intereseted
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "I'm Interested",
                        color = if (!intereseted) MaterialTheme.colorScheme.primary else Color.Black
                    )
                }
            }


        }
    }


}
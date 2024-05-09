package com.example.intern_assignment.presentation.seller

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.intern_assignment.SupabaseAuthViewModel
import com.example.intern_assignment.SupabaseViewModel
import com.example.intern_assignment.data.model.UserState
import com.example.intern_assignment.presentation.seller.components.CustomDropdownMenu
import com.example.intern_assignment.presentation.common.CustomTextField
import com.example.intern_assignment.utils.Dimens.ExtraSmallPadding
import com.example.intern_assignment.utils.Dimens.ExtraSmallPadding2
import com.example.intern_assignment.utils.uriToByteArray
import java.util.UUID

@Composable
fun AddCarScreen(
    navigateBack: () -> Unit,
    viewModel: SupabaseViewModel,
    authViewModel: SupabaseAuthViewModel,
    sellerViewModel: SellerViewModel
) {
    var fileName = ""
    var carBrand by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var carMilage by remember {
        mutableStateOf("")
    }
    var carModel by remember {
        mutableStateOf("")
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri

        }
    )
    var price by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(ExtraSmallPadding2)
            .verticalScroll(scrollState)
    ) {
        Row {
            IconButton(onClick = navigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(text = "Car Details", style = MaterialTheme.typography.headlineMedium)
        }
        Text(text = "Upload Image", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(ExtraSmallPadding))
        Button(onClick = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text(text = " Upload Image")
        }
        if (selectedImageUri != null) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop,
                model = selectedImageUri, contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
        Text(text = "Car Brand", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(ExtraSmallPadding))
        CustomDropdownMenu() {
            println("Car Brand: $it")
            carBrand = it ?: ""

        }
        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
        Text(text = "Car Model", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(ExtraSmallPadding))
        CustomTextField(
            value = carModel,
            onValueChange = {
                carModel = it
            },
            prefix = {
                Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = null)
            },
            placeholder = {
                Text(text = " Car Model")
            })
        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
        Text(text = "Car Cost", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(ExtraSmallPadding))
        CustomTextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = price,
            onValueChange = {
                price = it
            },
            prefix = {
                Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = null)
            },
            placeholder = {
                Text(text = " Car Cost (Lakhs)")
            })
        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
        Text(text = "Car Mileage ", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(ExtraSmallPadding))
        CustomTextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = carMilage,
            onValueChange = {
                carMilage = it
            },
            prefix = {
                Icon(imageVector = Icons.Default.LocalGasStation, contentDescription = null)
            },
            placeholder = {
                Text(text = " Car Mileage L/100Km")
            })
        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                fileName = UUID.randomUUID().toString()
                val imageByteArray = selectedImageUri?.uriToByteArray(context)
                imageByteArray?.let {
                    viewModel.uploadFile("cars", fileName, it)
                }
                println("Seller ID: ${authViewModel.currentUser.id}")
                println("Car Model: $carModel")
                println("Car Cost: $price")
                println("Car Mileage: $carMilage")
                println("Image File Name: $fileName")
                println("Car Brand: $carBrand")
                viewModel.insertCar(
                    sellerId = authViewModel.currentUser.id,
                    carModel = carModel,
                    carCost = price,
                    carMileage = carMilage,
                    imageFileName = fileName,
                    carBrand = carBrand
                )
            }) {
            when (val userState = viewModel.userState.value) {
                is UserState.Loading -> {
                    Text(text = "Uploading...")
                }

                is UserState.Error -> {
                    Toast.makeText(
                        context,
                        "Could not sell car : " + userState.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.changeUserState(UserState.NullState)
                }

                is UserState.Success -> {
                    Text(text = "Sell Car")
                    sellerViewModel.getContent(authViewModel.currentUser.id)
                    navigateBack()

                    viewModel.changeUserState(UserState.NullState)
                }

                else -> {
                    Text(text = "Sell Car")
                }
            }

        }

    }
}
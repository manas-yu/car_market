package com.example.intern_assignment.presentation.buyer


import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intern_assignment.data.model.Car
import com.example.intern_assignment.data.model.UserState
import com.example.intern_assignment.data.network.SupabaseClient.client
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BuyerViewModel : ViewModel() {
    private val _userState = mutableStateOf<UserState>(UserState.Loading)
    val userState: State<UserState> = _userState
    var contentList = mutableListOf<Car>()
    var filterList = mutableListOf<Car>()
    var selectedBrands = mutableListOf<String>()
    var maxCost = mutableStateOf("")
    var maxCarMileage = mutableStateOf("")
    fun filterCars() {
        filterList = contentList.filter { car ->
            (selectedBrands.isEmpty() || selectedBrands.contains(car.carBrand)) &&
                    (maxCost.value.isEmpty() || car.carCost.toDouble() <= maxCost.value.toDouble()) &&
                    (maxCarMileage.value.isEmpty() || car.carMileage.toDouble() <= maxCarMileage.value.toDouble())
        }.toMutableList()
    }

    fun resetList() {
        maxCost.value = ""
        maxCarMileage.value = ""
        selectedBrands.clear()
        filterList = contentList
    }

    private fun readPublicFile(
        bucketName: String = "cars",
        fileName: String,
        onImageUrlRetrieved: (url: String) -> Unit
    ) {
        viewModelScope.launch {
            try {

                val bucket = client.storage[bucketName]
                val url = bucket.publicUrl("$fileName.jpg")
                onImageUrlRetrieved(url)

            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun getContent(
        sellerId: String
    ) {
        viewModelScope.launch {
            try {
                println("getting content")
                _userState.value = UserState.Loading
                val data = client.postgrest["cars_table"].select()
                    .decodeList<Car>()
                contentList = data.toMutableList()
                filterList = data.toMutableList()
                for (car in contentList) {
                    val url = suspendCoroutine { continuation ->
                        readPublicFile("cars", car.imageFileName) { url ->
                            continuation.resume(url)
                        }
                    }
                    car.imageUrl = url
                }
                println("contentList: $contentList")
                _userState.value = UserState.Success("data fetched successfully")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error occurred")
            }
        }
    }

}
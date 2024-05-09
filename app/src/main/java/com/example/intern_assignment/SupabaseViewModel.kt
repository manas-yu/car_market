package com.example.intern_assignment

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intern_assignment.data.model.Car
import com.example.intern_assignment.data.model.UserState
import com.example.intern_assignment.data.network.SupabaseClient.client
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch

class SupabaseViewModel : ViewModel() {
    private val _userState = mutableStateOf<UserState>(UserState.NullState)
    val userState: State<UserState> = _userState
    fun changeUserState(userState: UserState) {
        _userState.value = userState
    }

    fun insertCar(
        sellerId: String,
        carBrand: String,
        carModel: String,
        carCost: String,
        carMileage: String,
        imageFileName: String
    ) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                if (
                    sellerId.isEmpty() ||
                    carBrand.isEmpty() ||
                    carModel.isEmpty() ||
                    carCost.isEmpty() ||
                    carMileage.isEmpty() ||
                    imageFileName.isEmpty()
                ) {
                    throw Exception("Please fill all the fields!")
                }
                client.postgrest["cars_table"].insert(
                    Car(
                        sellerId = sellerId,
                        carBrand = carBrand,
                        carModel = carModel,
                        carCost = carCost,
                        carMileage = carMileage,
                        imageFileName = imageFileName
                    )
                )
                _userState.value = UserState.Success("Added car successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun uploadFile(bucketName: String, fileName: String, byteArray: ByteArray) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                val bucket = client.storage[bucketName]
                bucket.upload("$fileName.jpg", byteArray, true)

            } catch (e: Exception) {
                println("Error: ${e.message}")
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }
}
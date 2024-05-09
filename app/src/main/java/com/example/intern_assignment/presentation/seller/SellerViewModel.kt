package com.example.intern_assignment.presentation.seller

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

class SellerViewModel : ViewModel() {
    private val _userState = mutableStateOf<UserState>(UserState.Loading)
    val userState: State<UserState> = _userState
    var contentList = mutableListOf<Car>()
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
                    .decodeList<Car>().filter { it.sellerId == sellerId }
                contentList = data.toMutableList()
                val urls = mutableListOf<String>()
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
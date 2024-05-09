package com.example.intern_assignment.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Car(
    val id: Int? = null,
    @SerialName("seller_id")
    val sellerId: String,
    @SerialName("car_brand")
    val carBrand: String,
    @SerialName("car_model")
    val carModel: String,
    @SerialName("car_cost")
    val carCost: String,
    @SerialName("car_mileage")
    val carMileage: String,
    @SerialName("image_filename")
    val imageFileName: String,
    var imageUrl: String? = null

) : Parcelable
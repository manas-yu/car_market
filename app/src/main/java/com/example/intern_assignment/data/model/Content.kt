package com.example.basic_assignment.data.model

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Parcelize
@Serializable
data class Content(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName("video_url")
    val videoUrl: String,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String,
    @SerialName("channel_url")
    val channelUrl: String,
    @SerialName("channel_name")
    val channelName: String,
    val likes: Int
) : Parcelable
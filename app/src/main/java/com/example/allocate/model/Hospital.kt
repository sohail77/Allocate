package com.example.allocate.model

import com.google.gson.annotations.SerializedName

data class Hospital(
    val id: Int,

    val name: String,

    @SerializedName("hlat")
    val latitude: Double,

    @SerializedName("hlong")
    val longitude: Double
)
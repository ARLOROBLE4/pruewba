package com.example.pruewba.Modelo

import com.google.gson.annotations.SerializedName

data class clsServicio(
    @SerializedName("id") val id: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("imagen") val imagen: String
)
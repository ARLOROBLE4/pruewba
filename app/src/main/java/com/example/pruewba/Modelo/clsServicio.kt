package com.example.pruewba.Modelo

import com.google.gson.annotations.SerializedName

data class clsServicio(
    @SerializedName("id") val id: Int,
    @SerializedName(value = "titulo", alternate = ["TITULO", "Titulo"])
    val titulo: String,

    @SerializedName(value = "descripcion", alternate = ["DESCRIPCION", "Descripcion"])
    val descripcion: String,
    @SerializedName("imagen") val imagen: String
)
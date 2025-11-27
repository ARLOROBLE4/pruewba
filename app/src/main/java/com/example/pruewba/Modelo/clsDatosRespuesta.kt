package com.example.pruewba.Modelo

import com.google.gson.annotations.SerializedName

data class clsDatosRespuesta(
    @SerializedName("Estado") val Estado: String,
    @SerializedName("Salida") val Salida: String,
    @SerializedName("user_id") val user_id: Int? = null
)
package com.example.pruewba.Modelo

import com.google.gson.annotations.SerializedName

data class clsDispositivoHistorial(
    @SerializedName("id_registro") val idRegistro: Int,
    @SerializedName("marca") val marca: String,
    @SerializedName("modelo") val modelo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("numero_serie") val numeroSerie: String,
    @SerializedName("fechaIngreso") val fechaIngreso: String,
    @SerializedName("detalles") val detalles: String,
    @SerializedName("diagnostico") val diagnostico: String,
    @SerializedName("costo") val costo: String
)
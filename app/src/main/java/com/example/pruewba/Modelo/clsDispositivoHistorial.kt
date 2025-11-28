package com.example.pruewba.Modelo

import com.google.gson.annotations.SerializedName

data class clsDispositivoHistorial(
    @SerializedName("id_registro") val idRegistro: Int,
    // Eliminado: @SerializedName("tipo_equipo") val tipoEquipo: String,
    @SerializedName("marca") val marca: String,
    @SerializedName("modelo") val modelo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("numero_serie") val numeroSerie: String
)
package com.example.pruewba.Modelo

import com.google.gson.annotations.SerializedName

data class clsDatosInicio(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("video_url") val videoUrl: String
)
package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ifaceApiService {

    @FormUrlEncoded
    @POST("apiAcceso.php")
    fun iniciarSesion(
        @Field("action") action: String = "login",
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<List<clsDatosRespuesta>>

    // NUEVO MÉTODO para obtener los datos de la pantalla de inicio
    @GET("apiInicio.php") // Asumiendo que la nueva API se llama apiInicio.php
    fun obtenerDatosInicio(): Call<List<clsDatosInicio>>
}
package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ifaceApiService {

    @FormUrlEncoded
    @POST("apiAcceso.php")
    fun iniciarSesion(
        @Field("action") action: String = "login",
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<List<clsDatosRespuesta>>

    @GET("apiInicio.php")
    fun obtenerDatosInicio(): Call<List<clsDatosInicio>>

    @GET("apiServicios.php")
    fun obtenerServicios(): Call<List<clsServicio>>

    @GET("apiHistorial.php")
    fun obtenerHistorialCliente(
        @Query("user_id") userId: Int
    ): Call<List<clsDispositivoHistorial>>

    @FormUrlEncoded
    @POST("apiAgenda.php")
    fun agendarCita(
        @Field("nombreCitado") nombreCitado: String,
        @Field("aPaterno") aPaterno: String,
        @Field("aMaterno") aMaterno: String,
        @Field("fechaCita") fechaCita: String,
        @Field("horaCita") horaCita: String
    ): Call<List<clsDatosRespuesta>>

}
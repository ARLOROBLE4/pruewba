package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
interface ifaceApiService {

    @FormUrlEncoded
    @POST("apiAcceso.php")
    fun registrarUsuario(
        @Field("action") action: String,
        @Field("nombreUsuario") nombreusuario: String,
        @Field("apellidoPaterno") apellidoPaterno: String,
        @Field("apellidoMaterno") apellidoMaterno: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<List<clsDatosRespuesta>>

    @FormUrlEncoded
    @POST("apiAcceso.php")
    fun iniciarSesion(
        @Field("action") action: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<List<clsDatosRespuesta>>
}
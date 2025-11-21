package com.example.pruewba.Modelo

// Asegúrate de que clsDatosRespuesta esté en un paquete accesible
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
        // *** CAMPOS AGREGADOS DE TU activity_registro.xml y apiAcceso.php ***
        @Field("apellidoPaterno") apellidoPaterno: String,
        @Field("apellidoMaterno") apellidoMaterno: String,
        // ********************************************************************
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

    // Si tuvieras un API para obtener la lista de clientes o dispositivos, se agregaría aquí:
    // @GET("apiDispositivos.php")
    // fun obtenerDispositivos(): Call<List<clsDispositivo>>
}
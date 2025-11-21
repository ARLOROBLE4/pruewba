package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class accessModel {
    private lateinit var apiService: ifaceApiService

    init {

        val retrofit = Retrofit.Builder()
            //.baseUrl("http://192.168.0.102/appMovil/PCStatus/Api/")
            .baseUrl("http://10.53.204.59/appMovil/PCStatus/Api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ifaceApiService::class.java)
    }
    fun iniciarSesion(email: String, password: String, onResult: (isSuccess: Boolean, message: String) -> Unit) {
        apiService.iniciarSesion("login", email, password)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(call: Call<List<clsDatosRespuesta>>, response: Response<List<clsDatosRespuesta>>) {
                    // ... (Misma lógica de respuesta) ...
                    if (response.isSuccessful) {
                        val datos = response.body()
                        val success = datos?.firstOrNull()?.Estado == "Correcto"
                        val message = datos?.firstOrNull()?.Salida ?: "Respuesta vacía"
                        onResult(success, message)
                    } else {
                        onResult(false, "Error en la respuesta: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    onResult(false, "Error de conexión: ${t.message}")
                }
            })
    }
    fun registrarUsuario(
        nombreUsuario: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        email: String,
        password: String,
        onResult: (isSuccess: Boolean, message: String) -> Unit
    ) {
        apiService.registrarUsuario(
            action = "registrar",
            nombreusuario = nombreUsuario,
            apellidoPaterno = apellidoPaterno,
            apellidoMaterno = apellidoMaterno,
            email = email,
            password = password
        ).enqueue(object : Callback<List<clsDatosRespuesta>> {
            override fun onResponse(call: Call<List<clsDatosRespuesta>>, response: Response<List<clsDatosRespuesta>>) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    val success = datos?.firstOrNull()?.Estado == "true" // El PHP devuelve "true" en minúscula
                    val message = datos?.firstOrNull()?.Salida ?: "Respuesta vacía"
                    onResult(success, message)
                } else {
                    onResult(false, "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                onResult(false, "Error de conexión: ${t.message}")
            }
        })
    }
}
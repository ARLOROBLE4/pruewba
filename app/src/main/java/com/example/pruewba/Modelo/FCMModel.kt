package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FCMModel {
    private val apiService: ifaceApiService
    private val BASE_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ifaceApiService::class.java)
    }

    fun sendTokenToServer(userId: Int, token: String, onResult: (isSuccess: Boolean, message: String) -> Unit) {
        if (userId <= 0) {
            onResult(false, "ID de usuario no válido.")
            return
        }

        apiService.saveFCMToken(userId, token)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(call: Call<List<clsDatosRespuesta>>, response: Response<List<clsDatosRespuesta>>) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()?.firstOrNull()
                        val success = respuesta?.Estado == "Correcto"
                        val message = respuesta?.Salida ?: "Token guardado en servidor."
                        onResult(success, message)
                    } else {
                        onResult(false, "Error al guardar token en el servidor.")
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    onResult(false, "Error de red al enviar token: ${t.message}")
                }
            })
    }
}
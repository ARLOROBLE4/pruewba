package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HistorialModel {
    private val apiService: ifaceApiService
    private val BASE_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ifaceApiService::class.java)
    }

    fun obtenerHistorial(
        userId: Int,
        onResult: (dispositivos: List<clsDispositivoHistorial>?, errorMessage: String?) -> Unit
    ) {
        if (userId == -1) {
            onResult(null, "Error: ID de usuario no válido.")
            return
        }

        apiService.obtenerHistorialCliente(userId).enqueue(object : Callback<List<clsDispositivoHistorial>> {
            override fun onResponse(
                call: Call<List<clsDispositivoHistorial>>,
                response: Response<List<clsDispositivoHistorial>>
            ) {
                if (response.isSuccessful) {
                    val dispositivos = response.body()
                    if (dispositivos != null) {
                        onResult(dispositivos, null)
                    } else {
                        onResult(emptyList(), "No se encontraron dispositivos para este usuario.")
                    }
                } else {
                    onResult(null, "Error al obtener historial: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<clsDispositivoHistorial>>, t: Throwable) {
                onResult(null, "Error de conexión: ${t.message}")
            }
        })
    }
}
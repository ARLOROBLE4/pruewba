package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class agendaModel {
    private val apiService: ifaceApiService
    private val BASE_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ifaceApiService::class.java)
    }

    // NUEVO: Obtener horas
    fun obtenerHorasOcupadas(fecha: String, onResult: (horasOcupadas: List<String>?) -> Unit) {
        apiService.obtenerHorasOcupadas(fecha).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(emptyList())
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                onResult(emptyList())
            }
        })
    }
    fun guardarCita(nombreCitado: String, aPaterno: String, aMaterno: String, fechaCita: String, horaCita: String, detalles: String, onResult: (isSuccess: Boolean, message: String) -> Unit) {
        apiService.agendarCita(nombreCitado, aPaterno, aMaterno, fechaCita, horaCita, detalles).enqueue(object : Callback<List<clsDatosRespuesta>> {
            override fun onResponse(call: Call<List<clsDatosRespuesta>>, response: Response<List<clsDatosRespuesta>>) {
                if (response.isSuccessful) {
                    val respuesta = response.body()?.firstOrNull()
                    val success = respuesta?.Estado == "Correcto"
                    val message = respuesta?.Salida ?: "Respuesta de servidor vacía."
                    onResult(success, message)
                } else {
                    onResult(false, "Error de servidor (${response.code()}).")
                }
            }
            override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                onResult(false, "Error de conexión: ${t.message}")
            }
        })
    }
}
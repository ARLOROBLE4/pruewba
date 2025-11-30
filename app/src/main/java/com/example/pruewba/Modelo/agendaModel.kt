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

    fun guardarCita(
        nombreCitado: String,
        aPaterno: String,
        aMaterno: String,
        fechaCita: String,
        horaCita: String,
        detalles: String,
        onResult: (isSuccess: Boolean, message: String) -> Unit
    ) {
        apiService.agendarCita(
            nombreCitado = nombreCitado,
            aPaterno = aPaterno,
            aMaterno = aMaterno,
            fechaCita = fechaCita,
            horaCita = horaCita,
            detalles = detalles
        ).enqueue(object : Callback<List<clsDatosRespuesta>> {
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
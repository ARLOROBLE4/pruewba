package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class inicioModel {
    private val apiService: ifaceApiService

    // La URL base para la nueva API: https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/
    private val BASE_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ifaceApiService::class.java)
    }

    fun obtenerDatosInicio(onResult: (datos: clsDatosInicio?, errorMessage: String?) -> Unit) {
        apiService.obtenerDatosInicio().enqueue(object : Callback<List<clsDatosInicio>> {
            override fun onResponse(call: Call<List<clsDatosInicio>>, response: Response<List<clsDatosInicio>>) {
                if (response.isSuccessful) {
                    // El PHP devuelve una lista, tomamos el primer elemento (si existe)
                    val datos = response.body()?.firstOrNull()
                    onResult(datos, null)
                } else {
                    onResult(null, "Error al obtener datos: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<clsDatosInicio>>, t: Throwable) {
                onResult(null, "Error de conexión: ${t.message}")
            }
        })
    }
}
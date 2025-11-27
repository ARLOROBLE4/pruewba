package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiciosModel {
    private val apiService: ifaceApiService
    private val BASE_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ifaceApiService::class.java)
    }

    fun obtenerServicios(onResult: (servicios: List<clsServicio>?, errorMessage: String?) -> Unit) {
        apiService.obtenerServicios().enqueue(object : Callback<List<clsServicio>> {
            override fun onResponse(call: Call<List<clsServicio>>, response: Response<List<clsServicio>>) {
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<clsServicio>>, t: Throwable) {
                onResult(null, "Error de conexión: ${t.message}")
            }
        })
    }
}
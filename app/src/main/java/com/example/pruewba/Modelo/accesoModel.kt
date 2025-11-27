package com.example.pruewba.Modelo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class accesoModel {
    private lateinit var apiService: ifaceApiService

    init {
        val BASE_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/"
        //https://srv760-files.hstgr.io/49621e5af04f9c9f/files/public_html/
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ifaceApiService::class.java)
    }

    fun iniciarSesion(email: String, password: String, onResult: (isSuccess: Boolean, message: String) -> Unit) {

        apiService.iniciarSesion(email = email, password = password)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(call: Call<List<clsDatosRespuesta>>, response: Response<List<clsDatosRespuesta>>) {
                    if (response.isSuccessful) {
                        val datos = response.body()
                        val success = datos?.firstOrNull()?.Estado == "Correcto"
                        val message = datos?.firstOrNull()?.Salida ?: "Respuesta vacía"
                        onResult(success, message)
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Error de servidor desconocido"
                        onResult(false, "Error en la respuesta: $errorBody")
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    onResult(false, "Error de conexión: ${t.message}")
                }
            })
    }
}
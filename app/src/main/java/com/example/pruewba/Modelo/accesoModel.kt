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
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ifaceApiService::class.java)
    }

    fun iniciarSesion(
        email: String,
        password: String,
        onResult: (isSuccess: Boolean, message: String, userId: Int?) -> Unit
    ) {

        apiService.iniciarSesion(email = email, password = password)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(call: Call<List<clsDatosRespuesta>>, response: Response<List<clsDatosRespuesta>>) {
                    if (response.isSuccessful) {
                        val datos = response.body()
                        val respuesta = datos?.firstOrNull()
                        val success = respuesta?.Estado == "Correcto"
                        val message = respuesta?.Salida ?: "Respuesta vacía"
                        val userId = respuesta?.user_id

                        onResult(success, message, userId)
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Error de servidor desconocido"
                        onResult(false, "Error en la respuesta: $errorBody", null)
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    onResult(false, "Error de conexión: ${t.message}", null)
                }
            })
    }
}
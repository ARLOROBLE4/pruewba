package com.example.pruewba

import com.example.pruewba.Modelo.ifaceApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ifaceApiService

    // Configuración inicial: Se levanta un servidor web simulado (MockWebServer) para interceptar las peticiones HTTP
    // y devolver respuestas controladas sin depender de la conexión a internet o del backend real.
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ifaceApiService::class.java)
    }

    // Limpieza: Se apaga el servidor simulado al finalizar cada prueba para liberar recursos.
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // Objetivo: Verificar que la interfaz de Retrofit parsea correctamente una respuesta JSON exitosa.
    // Descripción: Se simula un JSON de respuesta de login y se valida que el objeto resultante en Kotlin contenga los datos esperados.
    @Test
    fun `iniciarSesion parsea correctamente la respuesta del servidor`() {
        val mockResponse = """
            [
                {
                    "Estado": "Correcto",
                    "Salida": "Inicio de sesión exitoso",
                    "user_id": 55
                }
            ]
        """
        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        val response = apiService.iniciarSesion(email = "test@prueba.com", password = "123").execute()

        val datos = response.body()?.first()
        assertNotNull("El cuerpo de la respuesta no debe ser nulo", datos)
        assertEquals("Correcto", datos?.Estado)
        assertEquals(55, datos?.user_id)
    }

    // Objetivo: Verificar el comportamiento ante una lista vacía de datos.
    // Descripción: Se simula una respuesta vacía del servidor y se valida que la lista resultante tenga tamaño 0.
    @Test
    fun `obtenerServicios parsea lista vacia correctamente`() {
        mockWebServer.enqueue(MockResponse().setBody("[]").setResponseCode(200))

        val response = apiService.obtenerServicios().execute()

        assertNotNull(response.body())
        assertEquals(0, response.body()?.size)
    }
}
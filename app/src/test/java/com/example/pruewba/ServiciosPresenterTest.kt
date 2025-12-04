package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.ServiciosModel
import com.example.pruewba.Modelo.clsServicio
import com.example.pruewba.Presentador.Contratos.ServiciosContract
import io.mockk.*
import org.junit.Before
import org.junit.Test

/**
 * PRUEBAS UNITARIAS PARA SERVICIOS realizadas por Gabriel Gámez v2.0
 * Pruebas realizadas:
 * 1.- Carga de Servicios ("Happy Path"): Simulamos que el modelo responde correctamente con una lista de servicios. El sistema debe recibir
 * la lista y ordenar a la vista que la muestre.
 * 2.- Manejo de Error al Cargar: Simulamos que el modelo falla (lista nula o error de red). El sistema debe mostrar un mensaje de error adecuado.
 * 3.- Navegación a Agendar: Validamos que al seleccionar un servicio, el sistema navegue a la pantalla de Agenda enviando el objeto del servicio seleccionado.
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////// Prueba realizada: 04/12/2025 12:15 A.M. /////////////////////////////////////////////////////////////////////////////
 * ///////////////// Resultado: CORRECTO - 3/3 tests passed  ////////////////////////////////////////////////////////////////////////////
 * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

class ServiciosPresenterTest {

    private val viewMock = mockk<ServiciosContract.View>(relaxed = true)
    private val modelMock = mockk<ServiciosModel>(relaxed = true)

    private lateinit var presenter: ServiciosPresenter

    @Before
    fun setup() {
        presenter = ServiciosPresenter(modelMock)
        presenter.attachView(viewMock)
        println("SETUP: ServiciosPresenter listo.")
    }

    @Test
    fun `testCargarServiciosExitoso`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Carga de servicios exitosa")

        val listaSimulada = listOf(
            clsServicio(1, "Formateo", "Instalacion de SO", "img1.jpg"),
            clsServicio(2, "Limpieza", "Mantenimiento fisico", "img2.jpg")
        )

        // Simulamos respuesta del modelo
        every { modelMock.obtenerServicios(captureLambda()) } answers {
            println("   -> MOCK: Modelo solicitado. Retornando lista de ${listaSimulada.size} servicios.")
            val callback = lastArg<(List<clsServicio>?, String?) -> Unit>()
            callback.invoke(listaSimulada, null)
        }

        println("PASO 1: Solicitando carga de servicios...")
        presenter.loadServices()

        verify { viewMock.displayServices(listaSimulada) }
        println("   -> CONFIRMADO: La vista mostro la lista correctamente.")
        println("FIN TEST: Carga exitosa.")
    }

    @Test
    fun `testErrorCargarServicios`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Error al cargar servicios")

        val errorMsg = "Fallo de conexion"

        every { modelMock.obtenerServicios(captureLambda()) } answers {
            println("   -> MOCK: Modelo solicitado. Retornando ERROR.")
            val callback = lastArg<(List<clsServicio>?, String?) -> Unit>()
            callback.invoke(null, errorMsg)
        }

        println("PASO 1: Solicitando carga de servicios...")
        presenter.loadServices()

        verify { viewMock.showFetchServicesError(errorMsg) }
        println("   -> CONFIRMADO: La vista mostro el error: $errorMsg")
        println("FIN TEST: Manejo de error verificado.")
    }

    @Test
    fun `testNavegarAAgenda`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Navegacion a Agenda")

        val servicio = clsServicio(1, "Mantenimiento", "Desc", "img.png")

        println("PASO 1: Usuario da click en Agendar servicio ${servicio.titulo}...")
        presenter.handleAgendarClick(servicio)

        verify { viewMock.navigateToAgendaScreen(servicio) }
        println("   -> CONFIRMADO: Navegacion correcta hacia Agenda.")
        println("FIN TEST: Navegacion verificada.")
    }
}
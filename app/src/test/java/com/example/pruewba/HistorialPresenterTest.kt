package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.HistorialModel
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Modelo.clsDispositivoHistorial
import com.example.pruewba.Presentador.Contratos.HistorialContract
import io.mockk.*
import org.junit.Before
import org.junit.Test

/**
 * PRUEBAS UNITARIAS PARA EL HISTORIAL DE EQUIPOS realizadas por Gabriel Gámez v2.0 (debido a que cambió la versión de la aplicación)
 * Pruebas realizadas:
 * 1.- Validación de sesión (Seguridad): En esta prueba simulamos un intento de carga de historial sin un usuario logueado (ID de sesión invalido).
 * El sistema debe detectar la falta de sesión, mostrar un error al usuario, evitando llamar al modelo.
 * 2.- "Happy Path": Simulamos que el mocks (servidor falso) responde correctamente con una lista de equipos. El sistema debe recibir esa lista
 * y ordenarle a la vista que la muestre en pantalla correctamente.
 * 3.- Manejo de errores de red: Simulamos que el servidor falla (ej. Error 500 o sin internet) al intentar obtener la lista.
 * El sistema debe capturar el fallo y mostrar un mensaje legible al usuario sin que la app se cierre.
 * 4.- Navegación a Detalle: Validamos la interacción del usuario al dar clic en "Ver Más" de un equipo específico.
 * El sistema debe identificar el equipo seleccionado y navegar a la pantalla de detalles enviando la información correcta.
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////// Prueba realizada: 04/12/2025 12:05 A.M. /////////////////////////////////////////////////////////////////////////////
 * ///////////////// Resultado: CORRECTO - 4/4 tests passed  ////////////////////////////////////////////////////////////////////////////
 * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

class HistorialPresenterTest {

    private val viewMock = mockk<HistorialContract.View>(relaxed = true)
    private val modelMock = mockk<HistorialModel>(relaxed = true)
    private val sessionMock = mockk<SesionManager>(relaxed = true)

    private lateinit var presenter: HistorialPresenter

    @Before
    fun setup() {
        presenter = HistorialPresenter(modelMock, sessionMock)
        presenter.attachView(viewMock)
        println("SETUP: HistorialPresenter inicializado.")
    }

    // --- PRUEBA 1: USUARIO SIN SESIÓN ---
    @Test
    fun `testCargarHistorialSinSesion`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Intento de carga sin usuario logueado")

        // Simulamos que el SessionManager dice "ID es -1" (No hay usuario)
        every { sessionMock.getUserId() } returns -1
        println("PASO 1: SessionManager retorna ID invalido (-1).")

        // Acción
        presenter.loadUserHistorial()

        // Verificación
        verify { viewMock.showLoadingError("Error de sesión. Por favor, vuelve a ingresar.") }
        println("   -> CONFIRMADO: Se mostro error de sesion.")

        verify(exactly = 0) { modelMock.obtenerHistorial(any(), any()) }
        println("   -> CONFIRMADO: No se consulto al servidor.")
        println("FIN TEST: Validacion de sesion correcta.")
    }

    // --- PRUEBA 2: CARGA EXITOSA (Lista de Equipos) ---
    @Test
    fun `testCargarHistorialExitoso`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Carga de historial exitosa")

        val userId = 10
        // Simulamos que hay un usuario con ID 10
        every { sessionMock.getUserId() } returns userId

        // Preparamos datos falsos (Una lista con 2 equipos)
        val listaEquipos = listOf(
            clsDispositivoHistorial(1, "Dell", "Latitude", "En Revision", "SN1", "01/01", "Falla", "N/A", "0"),
            clsDispositivoHistorial(2, "HP", "Pavilion", "Entregado", "SN2", "02/01", "Limpieza", "OK", "500")
        )

        // Simulamos respuesta del servidor
        every { modelMock.obtenerHistorial(eq(userId), captureLambda()) } answers {
            println("   -> MOCK: Servidor buscando equipos para usuario $userId...")
            val callback = lastArg<(List<clsDispositivoHistorial>?, String?) -> Unit>()
            println("   -> MOCK: Retornando lista con ${listaEquipos.size} equipos.")
            callback.invoke(listaEquipos, null)
        }

        // Acción
        println("PASO 1: Solicitando historial...")
        presenter.loadUserHistorial()

        // Verificación
        verify { viewMock.displayHistorial(listaEquipos) }
        println("   -> CONFIRMADO: La vista recibio y mostro los 2 equipos.")
        println("FIN TEST: Carga exitosa completada.")
    }

    // --- PRUEBA 3: ERROR AL CARGAR (Fallo de Red) ---
    @Test
    fun `testErrorAlCargarHistorial`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Error de red al cargar historial")

        val userId = 10
        every { sessionMock.getUserId() } returns userId
        val mensajeError = "Error 500: Servidor no responde"

        // Simulamos respuesta de error
        every { modelMock.obtenerHistorial(eq(userId), captureLambda()) } answers {
            println("   -> MOCK: Servidor recibio solicitud...")
            val callback = lastArg<(List<clsDispositivoHistorial>?, String?) -> Unit>()
            println("   -> MOCK: Ocurrio un error en el servidor.")
            callback.invoke(null, mensajeError)
        }

        // Acción
        presenter.loadUserHistorial()

        // Verificación
        verify { viewMock.showLoadingError(mensajeError) }
        println("   -> CONFIRMADO: La vista mostro el mensaje: '$mensajeError'.")
        println("FIN TEST: Manejo de error verificado.")
    }

    // --- PRUEBA 4: VER DETALLE (Navegación) ---
    @Test
    fun `testNavegarADetalle`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Click en 'Ver Mas'")

        val dispositivo = clsDispositivoHistorial(1, "Dell", "Latitude", "En Revision", "SN1", "01/01", "Falla", "N/A", "0")

        // Acción
        println("PASO 1: Usuario hace click en el equipo Dell...")
        presenter.handleVerMasClick(dispositivo)

        // Verificación
        verify { viewMock.navigateToDetalleConsulta(dispositivo) }
        println("   -> CONFIRMADO: Navegando a la pantalla de detalles.")
        println("FIN TEST: Navegacion correcta.")
    }
}
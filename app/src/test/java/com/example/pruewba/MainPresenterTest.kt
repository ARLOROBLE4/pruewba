package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Modelo.clsDatosInicio
import com.example.pruewba.Modelo.inicioModel
import com.example.pruewba.Presentador.Contratos.MainContract
import io.mockk.*
import org.junit.Before
import org.junit.Test

/**
 * PRUEBAS UNITARIAS PARA MENU PRINCIPAL (MAIN) realizadas por Gabriel Gámez v2.0
 * Pruebas realizadas:
 * 1.- Navegación a Historial (Con Sesión): Validamos que si el usuario YA inició sesión, al dar clic en consultar el sistema lo lleve directo a su historial.
 * 2.- Navegación a Login (Sin Sesión): Validamos que si el usuario NO ha iniciado sesión, al dar clic en consultar el sistema lo redirija a la pantalla
 * de Login para autenticarse.
 * 3.- Carga de Datos Iniciales: Verificamos que al abrir la app, se descarguen correctamente los textos de bienvenida y se ordene la reproducción
 * del video de presentación.
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////// Prueba realizada: 04/12/2025 12:20 A.M. /////////////////////////////////////////////////////////////////////////////
 * ///////////////// Resultado: CORRECTO - 3/3 tests passed  ////////////////////////////////////////////////////////////////////////////
 * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

class MainPresenterTest {

    private val viewMock = mockk<MainContract.View>(relaxed = true)
    private val modelMock = mockk<inicioModel>(relaxed = true)
    private val sessionMock = mockk<SesionManager>(relaxed = true)

    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        presenter = MainPresenter(modelMock, sessionMock)
        presenter.attachView(viewMock)
        println("SETUP: MainPresenter listo.")
    }

    @Test
    fun `testConsultaEquipo_ConSesion_VaAHistorial`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Consulta de equipo (Usuario Logueado)")

        // Simulamos que SI esta logueado
        every { sessionMock.isLoggedIn() } returns true
        println("PASO 1: SessionManager indica usuario activo.")

        presenter.handleConsultaEquipoClick()

        verify { viewMock.navigateToHistorialScreen() }
        println("   -> CONFIRMADO: Navegacion directa a Historial.")
        println("FIN TEST.")
    }

    @Test
    fun `testConsultaEquipo_SinSesion_VaALogin`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Consulta de equipo (Usuario NO Logueado)")

        // Simulamos que NO esta logueado
        every { sessionMock.isLoggedIn() } returns false
        println("PASO 1: SessionManager indica SIN usuario.")

        presenter.handleConsultaEquipoClick()

        verify { viewMock.navigateToLoginScreen() }
        println("   -> CONFIRMADO: Redireccion a Login para autenticarse.")
        println("FIN TEST.")
    }

    @Test
    fun `testCargarDatosIniciales`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Carga de datos de inicio (Bienvenida y Video)")

        val datos = clsDatosInicio("Bienvenido", "Descripcion App", "video.mp4")

        every { modelMock.obtenerDatosInicio(captureLambda()) } answers {
            println("   -> MOCK: Modelo retornando datos de bienvenida.")
            val callback = lastArg<(clsDatosInicio?, String?) -> Unit>()
            callback.invoke(datos, null)
        }

        println("PASO 1: Cargando datos iniciales...")
        presenter.loadInitialData()

        // Verifica textos
        verify { viewMock.showDatosInicio(datos.titulo, datos.descripcion) }
        println("   -> CONFIRMADO: Textos mostrados.")

        // Verifica video (checa que la URL contenga la base y el nombre)
        verify { viewMock.loadVideo(match { it.contains("videopresentacion.mp4") }) }
        println("   -> CONFIRMADO: Video cargado.")
        println("FIN TEST.")
    }
}
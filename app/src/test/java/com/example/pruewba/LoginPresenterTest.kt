package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Modelo.accesoModel
import com.example.pruewba.Presentador.Contratos.LoginContract
import io.mockk.*
import org.junit.Before
import org.junit.Test

/**
 * PRUEBAS UNITARIAS PARA LOGIN realizadas por Gabriel Gámez v2.0 (debido a que cambió la versión de la aplicación)
 * Pruebas realizadas:
 * -1 Validación: El sistema debe bloquear los intentos de acceso con campos vacíos.
 * -2 "Happy Path": Cuando el servidor (Mockito XD suena a moco) responda "correcto" a la simulación de inicio de sesión del sistema
 * este debe guardar la sesión y permite el acceso a las demás funciones.
 * -3 Control de errores: Cuando el servidor responda con un error (por error de contraseña o email incorrecto) el sistema debe responder
 * con el mensaje adecuado y no debe permitir el acceso al usuario.
 * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////// Prueba realizada: 03/12/2025 11:27 P.M. /////////////////////////////////////////////////////////////////////////////
 * ///////////////// Resultado: CORRECTO - 3/3 tests passed (No se decirlo correctamente en español, "3 pruebas pasaron?") //////////////////
 * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
class LoginPresenterTest {

    private val viewMock = mockk<LoginContract.View>(relaxed = true)
    private val modelMock = mockk<accesoModel>(relaxed = true)
    private val sessionMock = mockk<SesionManager>(relaxed = true)

    private lateinit var presenter: LoginPresenter

    @Before
    fun setup() {
        presenter = LoginPresenter(modelMock, sessionMock)
        presenter.attachView(viewMock)
        println("SETUP: Presentador de Login listo.")
    }

    @Test
    fun `testValidacionCamposVacios`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Validacion de Login (Campos vacios)")

        println("PASO 1: Intentando login con credenciales vacias...")
        presenter.handleLoginButtonClick("", "")

        println("PASO 2: Verificando respuesta...")
        verify { viewMock.showLoginError("Por favor, ingresa correo y contraseña.") }
        println("   -> CONFIRMADO: Mensaje de error mostrado.")

        verify(exactly = 0) { modelMock.iniciarSesion(any(), any(), any()) }
        println("   -> CONFIRMADO: No se llamo al servidor.")
        println("FIN TEST: Validacion correcta.")
    }

    @Test
    fun `testLoginExitoso`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Login Exitoso")

        val email = "prueba@test.com"
        val userIdSimulado = 99

        every { modelMock.iniciarSesion(any(), any(), captureLambda()) } answers {
            println("   -> MOCK: Servidor recibio peticion de login.")
            val callback = lastArg<(Boolean, String, Int?) -> Unit>()
            println("   -> MOCK: Respondiendo EXITO. ID Usuario: $userIdSimulado")
            callback.invoke(true, "Login Correcto", userIdSimulado)
        }

        println("PASO 1: Click en boton Acceder...")
        presenter.handleLoginButtonClick(email, "123")

        verify { sessionMock.createLoginSession(userIdSimulado) }
        println("   -> CONFIRMADO: Sesion guardada.")

        verify { viewMock.showLoginSuccess() }
        verify { viewMock.navigateToConsultaScreen() }
        println("   -> CONFIRMADO: Navegacion a pantalla principal.")
        println("FIN TEST: Login exitoso.")
    }

    @Test
    fun `testLoginFallidoPorServidor`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Login fallido (Credenciales invalidas)")

        val errorMsg = "Credenciales inválidas"

        every { modelMock.iniciarSesion(any(), any(), captureLambda()) } answers {
            println("   -> MOCK: Servidor recibio peticion.")
            val callback = lastArg<(Boolean, String, Int?) -> Unit>()
            println("   -> MOCK: Respondiendo ERROR: $errorMsg")
            callback.invoke(false, errorMsg, null)
        }

        println("PASO 1: Click en boton Acceder...")
        presenter.handleLoginButtonClick("user", "pass")

        verify { viewMock.showLoginError(errorMsg) }
        println("   -> CONFIRMADO: Mensaje de error mostrado en pantalla.")
        println("FIN TEST: Manejo de error correcto.")
    }
}
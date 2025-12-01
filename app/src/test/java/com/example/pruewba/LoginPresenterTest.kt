package com.example.pruewba

import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Modelo.accesoModel
import com.example.pruewba.Presentador.Contratos.LoginContract
import com.example.pruewba.Presentador.LoginPresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginPresenterTest {

    @Mock private lateinit var view: LoginContract.View
    @Mock private lateinit var model: accesoModel
    @Mock private lateinit var sessionManager: SesionManager

    private lateinit var presenter: LoginPresenter

    @Captor
    private lateinit var callbackCaptor: ArgumentCaptor<(Boolean, String, Int?) -> Unit>

    @Before
    fun setUp() {
        presenter = LoginPresenter(model, sessionManager)
        presenter.attachView(view)
    }

    @Test
    fun `handleLoginButtonClick muestra error cuando los campos estan vacios`() {
        println("INICIO PRUEBA: Login con campos vacíos")
        presenter.handleLoginButtonClick("", "")
        verify(view).showLoginError("Por favor, ingresa correo y contraseña.")
        println("RESULTADO: La vista mostró el error esperado.")
        println("--------------------------------------------------")
    }

    @Test
    fun `handleLoginButtonClick navega a consulta cuando el login es exitoso`() {
        println("INICIO PRUEBA: Login Exitoso")

        // GIVEN
        val email = "ingeniero@test.com"
        val password = "securePass"
        val userId = 100
        println("DATOS DE ENTRADA: Email=$email, Pass=****")

        // WHEN
        presenter.handleLoginButtonClick(email, password)

        // Capturamos la llamada
        verify(model).iniciarSesion(eq(email), eq(password), callbackCaptor.capture())
        println("ACCIÓN: Presenter llamó al Modelo.")

        // Simulamos respuesta del servidor
        println("SIMULACIÓN: El servidor responde 'Correcto' con ID=$userId")
        callbackCaptor.value.invoke(true, "Bienvenido", userId)

        // THEN
        verify(sessionManager).createLoginSession(userId)
        println("VERIFICACIÓN: Se guardó la sesión con ID $userId")

        verify(view).showLoginSuccess()
        verify(view).navigateToConsultaScreen()
        println("🚀 VERIFICACIÓN: Navegación a pantalla de Consulta ejecutada.")
        println("PRUEBA FINALIZADA CON ÉXITO")
        println("--------------------------------------------------")
    }

    @Test
    fun `handleLoginButtonClick muestra error cuando el modelo devuelve fallo`() {
        val errorMsg = "Credenciales incorrectas"
        presenter.handleLoginButtonClick("test@test.com", "wrongpass")

        verify(model).iniciarSesion(anyString(), anyString(), callbackCaptor.capture())
        callbackCaptor.value.invoke(false, errorMsg, null)

        verify(view).showLoginError(errorMsg)
        verify(view, never()).navigateToConsultaScreen()
    }
}
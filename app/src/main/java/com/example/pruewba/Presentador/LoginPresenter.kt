package com.example.pruewba.Presentador

import com.example.pruewba.Presentador.Contratos.LoginContract
import com.example.pruewba.Modelo.accesoModel
import com.example.pruewba.Modelo.SesionManager // 🛑 Importar SessionManager

class LoginPresenter(
    private val modelo: accesoModel,
    private val sessionManager: SesionManager // 🛑 NUEVO: Inyectar SessionManager
) : LoginContract.Presentador {
    private var view: LoginContract.View? = null

    override fun attachView(view: LoginContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun handleLoginButtonClick(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            view?.showLoginError("Por favor, ingresa correo y contraseña.")
            return
        }

        // 🛑 El Model ahora devuelve el user_id
        modelo.iniciarSesion(email, password) { isSuccess, message, userId ->
            if (isSuccess) {
                if (userId != null && userId != -1) {
                    sessionManager.createLoginSession(userId) // 🛑 Guardar la sesión
                    view?.showLoginSuccess()
                    view?.navigateToConsultaScreen() // Navega a Historial
                } else {
                    view?.showLoginError("Inicio de sesión fallido: ID de usuario no disponible.")
                }
            } else {
                view?.showLoginError(message)
            }
        }
    }
}
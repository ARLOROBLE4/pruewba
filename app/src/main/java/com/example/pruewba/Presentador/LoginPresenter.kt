package com.example.pruewba.Presentador

import com.example.pruewba.Presentador.Contratos.LoginContract
import com.example.pruewba.Modelo.accesoModel

class LoginPresenter(private val modelo: accesoModel) : LoginContract.Presentador {
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

        modelo.iniciarSesion(email, password) { isSuccess, message ->
            if (isSuccess) {
                view?.showLoginSuccess()
                view?.navigateToConsultaScreen()
            } else {
                view?.showLoginError(message)
            }
        }
    }
}
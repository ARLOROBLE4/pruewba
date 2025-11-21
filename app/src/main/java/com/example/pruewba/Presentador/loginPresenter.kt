package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.accessModel
import com.example.pruewba.Modelo.loginContract

class loginPresenter (private val model: accessModel) : loginContract.Presenter {
    private var view: loginContract.View? = null

    override fun attachView(view: loginContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun handleLoginButtonClick(email: String, password: String) {

        // Validacion básica
        if (email.isBlank() || password.isBlank()) {
            view?.showLoginError("Por favor, completa todos los campos.")
            return
        }

        model.iniciarSesion(email, password) { isSuccess, message ->
            if (isSuccess) {
                view?.showLoginSuccess()
                view?.navigateToMainScreen()
            } else {
                view?.showLoginError(message)
            }
        }
    }

    override fun handleRegisterClick() {
        view?.navigateToRegistration()
    }
}
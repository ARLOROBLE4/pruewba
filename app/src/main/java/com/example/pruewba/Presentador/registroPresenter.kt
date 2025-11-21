package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.accessModel
import com.example.pruewba.Modelo.registroContract

class registroPresenter(private val model: accessModel) : registroContract.Presenter {

    private var view: registroContract.View? = null

    override fun attachView(view: registroContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun handleRegistrationButtonClick(
        nombreUsuario: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        email: String,
        password: String
    ) {// Validación básica
        if (nombreUsuario.isBlank() || apellidoPaterno.isBlank() || apellidoMaterno.isBlank() || email.isBlank() || password.isBlank()) {
            view?.showRegistrationError("Por favor, complete todos los campos para el registro.")
            return
        }

        model.registrarUsuario(
            nombreUsuario,
            apellidoPaterno,
            apellidoMaterno,
            email,
            password
        ) { isSuccess, message ->
            if (isSuccess) {
                view?.showRegistrationSuccess(message)
                view?.closeScreen()
            } else {
                view?.showRegistrationError(message)
            }
        }
    }
}
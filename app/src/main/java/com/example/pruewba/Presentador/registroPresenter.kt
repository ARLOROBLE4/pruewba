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

    /**
     * Maneja el evento de clic en el botón de registro.
     * Recibe todos los campos del formulario de la Vista y los pasa al Modelo.
     */
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

        // Llamada al Modelo para ejecutar la API de registro
        model.registrarUsuario(
            nombreUsuario,
            apellidoPaterno,
            apellidoMaterno,
            email,
            password
        ) { isSuccess, message ->

            // El Presenter maneja la respuesta y notifica a la Vista
            if (isSuccess) {
                view?.showRegistrationSuccess(message)
                view?.closeScreen() // Cierra la Activity de registro al tener éxito
            } else {
                view?.showRegistrationError(message)
            }
        }
    }
}
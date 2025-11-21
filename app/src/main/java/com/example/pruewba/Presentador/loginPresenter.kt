package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.accessModel
import com.example.pruewba.Modelo.loginContract

class loginPresenter (private val model: accessModel) : loginContract.Presenter {
    // Referencia a la vista (Activity), se mantiene como nullable para evitar Memory Leaks
    private var view: loginContract.View? = null

    // ------------------------------------
    // Métodos del Ciclo de Vida de la Vista
    // ------------------------------------

    override fun attachView(view: loginContract.View) {
        this.view = view
    }

    override fun detachView() {
        // Al desvincular la vista (por ejemplo, al destruir la Activity), se libera la referencia
        this.view = null
    }

    override fun handleLoginButtonClick(email: String, password: String) {

        // Validaciones básicas (opcional, pero recomendada)
        if (email.isBlank() || password.isBlank()) {
            view?.showLoginError("Por favor, completa todos los campos.")
            return
        }

        model.iniciarSesion(email, password) { isSuccess, message ->
            // El Presenter recibe la respuesta del Modelo de forma asíncrona
            if (isSuccess) {
                // Si la respuesta es exitosa ("Correcto"), indica a la Vista la navegación
                view?.showLoginSuccess()
                view?.navigateToMainScreen() // Navega a activity_bienvenida
            } else {
                // Si hay un error, indica a la Vista que muestre el mensaje de error
                view?.showLoginError(message)
            }
        }
    }

    override fun handleRegisterClick() {
        view?.navigateToRegistration()
    }
}
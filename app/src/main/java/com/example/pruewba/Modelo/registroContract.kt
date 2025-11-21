package com.example.pruewba.Modelo

interface registroContract {
    interface View {
        fun showRegistrationSuccess(message: String)
        fun showRegistrationError(message: String)
        fun closeScreen()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        // *** SE AGREGAN LOS NUEVOS CAMPOS ***
        fun handleRegistrationButtonClick(
            nombreUsuario: String,
            apellidoPaterno: String,
            apellidoMaterno: String,
            email: String,
            password: String
        )
    }
}
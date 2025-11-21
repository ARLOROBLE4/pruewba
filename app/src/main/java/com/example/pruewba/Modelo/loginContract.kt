package com.example.pruewba.Modelo

interface loginContract {
    interface View {
        fun showLoginSuccess() // Muestra un mensaje de éxito
        fun showLoginError(message: String) // Muestra un mensaje de error
        fun navigateToRegistration() // Navega a la pantalla de registro
        fun navigateToMainScreen() // Navega a la pantalla principal (o Bienvenida) después del login
    }

    interface Presenter {
        fun attachView(view: View) // Enlaza la Vista al Presenter
        fun detachView() // Desvincula la Vista para evitar fugas de memoria
        fun handleLoginButtonClick(email: String, password: String) // Maneja el evento de clic en el botón de login
        fun handleRegisterClick() // Maneja el evento de clic en el texto/botón de registro
    }
}
package com.example.pruewba.Modelo

interface loginContract {
    /**
     * Define los métodos que la Vista (Activity) debe implementar
     * para manejar la interacción con el Presenter.
     */
    interface View {
        fun showLoginSuccess() // Muestra un mensaje de éxito
        fun showLoginError(message: String) // Muestra un mensaje de error
        fun navigateToRegistration() // Navega a la pantalla de registro
        fun navigateToMainScreen() // Navega a la pantalla principal (o Bienvenida) después del login
    }

    /**
     * Define los métodos que el Presenter debe implementar
     * para manejar la lógica de negocio y los eventos de la Vista.
     */
    interface Presenter {
        fun attachView(view: View) // Enlaza la Vista al Presenter
        fun detachView() // Desvincula la Vista para evitar fugas de memoria (Memory Leaks)
        fun handleLoginButtonClick(email: String, password: String) // Maneja el evento de clic en el botón de login
        fun handleRegisterClick() // Maneja el evento de clic en el texto/botón de registro
    }
}
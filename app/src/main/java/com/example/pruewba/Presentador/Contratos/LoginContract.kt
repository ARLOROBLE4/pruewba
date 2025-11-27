package com.example.pruewba.Presentador.Contratos

interface LoginContract {
    interface View {
        fun showLoginSuccess()
        fun showLoginError(message: String)
        fun navigateToConsultaScreen()
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
        fun handleLoginButtonClick(email: String, password: String)
    }
}
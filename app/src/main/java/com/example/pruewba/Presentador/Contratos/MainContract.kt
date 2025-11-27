package com.example.pruewba.Presentador.Contratos

interface MainContract {
    interface View {
        fun navigateToLoginScreen()
        fun navigateToServiciosScreen()
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
        fun handleConsultaEquipoClick()
        fun handleServiciosClick()
    }
}
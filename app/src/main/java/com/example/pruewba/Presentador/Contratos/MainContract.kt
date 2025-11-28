package com.example.pruewba.Presentador.Contratos

interface MainContract {
    interface View {
        fun navigateToLoginScreen()
        fun navigateToServiciosScreen()
        fun navigateToHistorialScreen()
        fun showDatosInicio(titulo: String, descripcion: String)
        fun loadVideo(videoFileName: String)
        fun showDataError(message: String)
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
        fun handleConsultaEquipoClick()
        fun handleServiciosClick()
        fun loadInitialData()
    }
}
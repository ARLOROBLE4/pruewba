package com.example.pruewba.Presentador.Contratos

interface AgendaContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showToast(message: String)
        fun clearForm()
        fun navigateBackToServices()
        fun getDatosAgendamiento(): Map<String, String> // Obtiene los datos del formulario
        fun getServicioTitulo(): String // Obtiene el título del servicio del Intent
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
        fun handleGuardarCitaClick()
    }
}
package com.example.pruewba.Presentador.Contratos

interface AgendaContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showToast(message: String)
        fun clearForm()
        fun navigateBackToServices()
        fun getDatosAgendamiento(): Map<String, String>
        fun getServicioTitulo(): String
        fun showAvailableHours(horas: List<String>)
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
        fun handleGuardarCitaClick()
        fun loadAvailableHours(fecha: String)
    }
}
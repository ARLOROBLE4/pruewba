package com.example.pruewba.Presentador.Contratos

import com.example.pruewba.Modelo.clsDispositivoHistorial

interface HistorialContract {
    interface View {
        fun displayHistorial(dispositivos: List<clsDispositivoHistorial>)
        fun showLoadingError(message: String)
        fun navigateToDetalleConsulta(dispositivo: clsDispositivoHistorial)
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
        fun loadUserHistorial()
        fun handleVerMasClick(dispositivo: clsDispositivoHistorial)
    }
}
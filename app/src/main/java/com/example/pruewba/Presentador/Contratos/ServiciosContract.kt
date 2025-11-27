package com.example.pruewba.Presentador.Contratos

import com.example.pruewba.Modelo.clsServicio

interface ServiciosContract {
    interface View {
        fun displayServices(servicios: List<clsServicio>)
        fun showFetchServicesError(message: String)
        fun navigateToServiceDetail(servicio: clsServicio)
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
        fun loadServices()
        fun handleServiceClick(servicio: clsServicio)
    }
}
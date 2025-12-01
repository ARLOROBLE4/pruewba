package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.ServiciosModel
import com.example.pruewba.Modelo.clsServicio
import com.example.pruewba.Presentador.Contratos.ServiciosContract

class ServiciosPresenter(private val modelo: ServiciosModel) : ServiciosContract.Presentador {
    private var view: ServiciosContract.View? = null

    override fun attachView(view: ServiciosContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadServices() {
        modelo.obtenerServicios { servicios, errorMessage ->
            if (servicios != null) {
                view?.displayServices(servicios)
            } else {
                view?.showFetchServicesError(errorMessage ?: "Error desconocido al obtener servicios")
            }
        }
    }

    override fun handleServiceClick(servicio: clsServicio) {
        view?.navigateToServiceDetail(servicio)
    }

    override fun handleAgendarClick(servicio: clsServicio) {
        view?.navigateToAgendaScreen(servicio)
    }
}
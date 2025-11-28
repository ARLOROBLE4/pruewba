package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.ServiciosModel
import com.example.pruewba.Modelo.clsServicio
import com.example.pruewba.Presentador.Contratos.ServiciosContract

// El Presentador ahora necesita el Modelo de Servicios
class ServiciosPresenter(private val modelo: ServiciosModel) : ServiciosContract.Presentador {
    private var view: ServiciosContract.View? = null

    override fun attachView(view: ServiciosContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadServices() {
        modelo.obtenerServicios { servicios, errorMessage -> // <-- Aquí se inicia la carga
            if (servicios != null) {
                view?.displayServices(servicios)
            } else {
                view?.showFetchServicesError(errorMessage ?: "Error desconocido...")
            }
        }
    }

    override fun handleServiceClick(servicio: clsServicio) {
        view?.navigateToServiceDetail(servicio)
    }
}
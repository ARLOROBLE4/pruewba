package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.inicioModel
import com.example.pruewba.Presentador.Contratos.MainContract

class MainPresenter (private val modeloInicio: inicioModel): MainContract.Presentador {
    private var view: MainContract.View? = null

    override fun attachView(view: MainContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun handleConsultaEquipoClick() {
        view?.navigateToLoginScreen()
    }

    override fun handleServiciosClick() {
        view?.navigateToServiciosScreen()
    }

    override fun loadInitialData() {
        modeloInicio.obtenerDatosInicio { datos, errorMessage ->
            if (datos != null) {
                // 1. Mostrar texto
                view?.showDatosInicio(datos.titulo, datos.descripcion)
                // 2. Cargar video
                view?.loadVideo(datos.videoUrl)
            } else {
                view?.showDataError(errorMessage ?: "Error desconocido al cargar datos iniciales.")
            }
        }
    }
}
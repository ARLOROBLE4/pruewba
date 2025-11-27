package com.example.pruewba.Presentador

import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.Modelo.inicioModel

// El Presentador ahora requiere una instancia de InicioModelo
class MainPresenter(private val modeloInicio: inicioModel) : MainContract.Presentador {
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

    // NUEVA LÓGICA: Cargar datos de la API
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
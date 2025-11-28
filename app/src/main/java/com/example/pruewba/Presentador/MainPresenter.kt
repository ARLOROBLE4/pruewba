package com.example.pruewba.Presentador

import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.Modelo.inicioModel
import com.example.pruewba.Modelo.SesionManager

class MainPresenter(
    private val modeloInicio: inicioModel,
    private val sessionManager: SesionManager
) : MainContract.Presentador {
    private var view: MainContract.View? = null

    override fun attachView(view: MainContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    // Lógica para decidir a dónde ir según la sesión
    override fun handleConsultaEquipoClick() {
        if (sessionManager.isLoggedIn()) {
            // Si la sesión está activa, ir a Historial
            view?.navigateToHistorialScreen()
        } else {
            // Si no está activa, ir a Login
            view?.navigateToLoginScreen()
        }
    }

    override fun handleServiciosClick() {
        view?.navigateToServiciosScreen()
    }

    override fun loadInitialData() {
        modeloInicio.obtenerDatosInicio { datos, errorMessage ->
            if (datos != null) {
                view?.showDatosInicio(datos.titulo, datos.descripcion)
                view?.loadVideo(datos.videoUrl)
            } else {
                view?.showDataError(errorMessage ?: "Error desconocido al cargar datos iniciales.")
            }
        }
    }
}
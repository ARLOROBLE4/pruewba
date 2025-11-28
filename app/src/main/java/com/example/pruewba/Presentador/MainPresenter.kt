package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.Modelo.inicioModel// 🛑 Importar SessionManager

class MainPresenter(
    private val modeloInicio: inicioModel,
    private val sessionManager: SesionManager // 🛑 NUEVO: Inyectar SessionManager
) : MainContract.Presentador {
    private var view: MainContract.View? = null

    override fun attachView(view: MainContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun handleConsultaEquipoClick() {
        if (sessionManager.isLoggedIn()) { // 🛑 Verificar estado de sesión
            view?.navigateToHistorialScreen()
        } else {
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
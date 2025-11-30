package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.HistorialModel
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Modelo.clsDispositivoHistorial
import com.example.pruewba.Presentador.Contratos.HistorialContract

class HistorialPresenter(
    private val model: HistorialModel,
    private val sessionManager: SesionManager
) : HistorialContract.Presentador {
    private var view: HistorialContract.View? = null

    override fun attachView(view: HistorialContract.View) {
        this.view = view
        //loadUserHistorial()
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadUserHistorial() {
        val userId = sessionManager.getUserId()

        if (userId <= 0) {
            view?.showLoadingError("Error: ID de usuario no válido. Por favor, inicie sesión nuevamente.")
            return
        }

        model.obtenerHistorial(userId) { dispositivos, errorMessage ->
            if (dispositivos != null) {
                view?.displayHistorial(dispositivos)
            } else {
                view?.showLoadingError(errorMessage ?: "Error desconocido al cargar el historial.")
            }
        }
    }

    override fun handleVerMasClick(dispositivo: clsDispositivoHistorial) {
        view?.navigateToDetalleConsulta(dispositivo)
    }
}
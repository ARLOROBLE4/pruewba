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
        // No cargamos aquí automáticamente, dejamos que el onResume de la vista lo haga
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadUserHistorial() {
        val userId = sessionManager.getUserId()

        if (userId <= 0) {
            view?.showLoadingError("Error de sesión. Por favor, vuelve a ingresar.")
            return
        }

        // Llamada al modelo (Asumiendo que ya actualizaste el modelo para aceptar timestamp)
        // Si tu modelo aún no lo acepta, usa solo `userId`
        model.obtenerHistorial(userId) { dispositivos, errorMessage ->
            if (view == null) return@obtenerHistorial // Evitar crashes si la vista se cerró

            if (dispositivos != null) {
                view?.displayHistorial(dispositivos)
            } else {
                view?.showLoadingError(errorMessage ?: "Error desconocido.")
            }
        }
    }

    override fun handleVerMasClick(dispositivo: clsDispositivoHistorial) {
        view?.navigateToDetalleConsulta(dispositivo)
    }
}
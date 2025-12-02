package com.example.pruewba.Presentador

import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.Modelo.inicioModel
import com.example.pruewba.Modelo.SesionManager

class MainPresenter(
    private val modeloInicio: inicioModel,
    private val sessionManager: SesionManager
) : MainContract.Presentador {
    private var view: MainContract.View? = null

    // URL base de los videos
    private val BASE_VIDEO_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/videos/"

    override fun attachView(view: MainContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun handleConsultaEquipoClick() {
        if (sessionManager.isLoggedIn()) {
            view?.navigateToHistorialScreen()
        } else {
            view?.navigateToLoginScreen()
        }
    }

    override fun handleServiciosClick() {
        view?.navigateToServiciosScreen()
    }

    override fun loadInitialData() {
        // 1. Cargar textos desde la BD
        modeloInicio.obtenerDatosInicio { datos, errorMessage ->
            if (datos != null) {
                view?.showDatosInicio(datos.titulo, datos.descripcion)
                // Opcional: Si la BD trae el nombre del video, podrías usar datos.videoUrl
            } else {
                view?.showDataError(errorMessage ?: "Error al cargar datos.")
            }
        }

        // 2. Cargar el video específico solicitado
        // Concatenamos la URL base con el nombre del archivo
        val fullVideoUrl = BASE_VIDEO_URL + "videopresentacion.mp4"
        view?.loadVideo(fullVideoUrl)
    }
}
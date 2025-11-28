package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.agendaModel
import com.example.pruewba.Presentador.Contratos.AgendaContract

class AgendaPresenter(
    private val model: agendaModel
) : AgendaContract.Presentador {

    private var view: AgendaContract.View? = null

    override fun attachView(view: AgendaContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun handleGuardarCitaClick() {
        val view = this.view ?: return

        val datos = view.getDatosAgendamiento()

        val nombreCitado = datos["nombreCitado"] ?: ""
        val aPaterno = datos["aPaterno"] ?: ""
        val aMaterno = datos["aMaterno"] ?: ""
        val fechaCita = datos["fechaCita"] ?: ""
        val horaCita = datos["horaCita"] ?: ""

        if (nombreCitado.isBlank() || fechaCita.isBlank() || horaCita.isBlank()) {
            view.showToast("Por favor, complete al menos el nombre, la fecha y la hora.")
            return
        }

        view.showLoading()

        model.guardarCita(
            nombreCitado = nombreCitado,
            aPaterno = aPaterno,
            aMaterno = aMaterno,
            fechaCita = fechaCita,
            horaCita = horaCita
        ) { isSuccess, message ->
            view.hideLoading()
            view.showToast(message)
            if (isSuccess) {
                view.clearForm()
                view.navigateBackToServices()
            }
        }
    }
}
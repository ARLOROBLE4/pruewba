package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.agendaModel
import com.example.pruewba.Presentador.Contratos.AgendaContract

class AgendaPresenter(
    private val model: agendaModel
) : AgendaContract.Presentador {

    private var view: AgendaContract.View? = null

    // Definimos el horario base del negocio: 14:00 a 22:00
    private val horarioBase = listOf(
        "14:00", "15:00", "16:00", "17:00",
        "18:00", "19:00", "20:00", "21:00", "22:00"
    )

    override fun attachView(view: AgendaContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    // NUEVO: Lógica para calcular disponibilidad
    override fun loadAvailableHours(fecha: String) {
        if (fecha.length < 10) return // No cargar si la fecha no está completa

        view?.showLoading()
        model.obtenerHorasOcupadas(fecha) { horasOcupadas ->
            view?.hideLoading()

            // Si hubo error o lista vacía, horasOcupadas es emptyList
            val ocupadas = horasOcupadas ?: emptyList()

            // Filtramos: Dejamos solo las horas base que NO estén en la lista de ocupadas
            val disponibles = horarioBase.filter { !ocupadas.contains(it) }

            if (disponibles.isEmpty()) {
                view?.showToast("No hay horarios disponibles para esta fecha.")
            }

            view?.showAvailableHours(disponibles)
        }
    }

    override fun handleGuardarCitaClick() {
        val view = this.view ?: return

        val datos = view.getDatosAgendamiento()

        val nombreCitado = datos["nombreCitado"] ?: ""
        val aPaterno = datos["aPaterno"] ?: ""
        val aMaterno = datos["aMaterno"] ?: ""
        val fechaCita = datos["fechaCita"] ?: ""
        val horaCita = datos["horaCita"] ?: ""
        val detalles = datos["detalles"] ?: ""

        if (nombreCitado.isBlank() || fechaCita.isBlank()) {
            view.showToast("Faltan datos (Nombre o Fecha).")
            return
        }

        if (horaCita.isBlank()) {
            view.showToast("Por favor selecciona un horario disponible.")
            return
        }

        view.showLoading()

        model.guardarCita(
            nombreCitado = nombreCitado,
            aPaterno = aPaterno,
            aMaterno = aMaterno,
            fechaCita = fechaCita,
            horaCita = horaCita,
            detalles = detalles
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
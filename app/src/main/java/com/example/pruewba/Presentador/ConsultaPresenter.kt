package com.example.pruewba.Presentador

import com.example.pruewba.Presentador.Contratos.ConsultaContract

class ConsultaPresenter: ConsultaContract.Presentador {
    private var view: ConsultaContract.View? = null

    override fun attachView(view: ConsultaContract.View) {
        this.view = view
        view.showInitialMessage("Consulta el estado de tu equipo")
    }

    override fun detachView() {
        this.view = null
    }
}
package com.example.pruewba.Presentador

import com.example.pruewba.Presentador.Contratos.ServiciosContract

class ServiciosPresenter : ServiciosContract.Presentador {
    private var view: ServiciosContract.View? = null

    override fun attachView(view: ServiciosContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }
}
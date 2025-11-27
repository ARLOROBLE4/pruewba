package com.example.pruewba.Presentador

import com.example.pruewba.Presentador.Contratos.MainContract

class MainPresenter : MainContract.Presentador {
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
}
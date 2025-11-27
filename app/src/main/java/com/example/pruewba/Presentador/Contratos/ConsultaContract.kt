package com.example.pruewba.Presentador.Contratos

interface ConsultaContract {
    interface View {
        fun showInitialMessage(message: String)
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
    }
}
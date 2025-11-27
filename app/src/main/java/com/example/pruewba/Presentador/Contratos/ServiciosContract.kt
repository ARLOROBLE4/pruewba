package com.example.pruewba.Presentador.Contratos

interface ServiciosContract {
    interface View {
        // Métodos para mostrar la lista de servicios del RecyclerView
    }

    interface Presentador {
        fun attachView(view: View)
        fun detachView()
    }
}
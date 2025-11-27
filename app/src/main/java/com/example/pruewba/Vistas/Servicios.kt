package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pruewba.Presentador.Contratos.ServiciosContract
import com.example.pruewba.Presentador.ServiciosPresenter
import com.example.pruewba.R

class Servicios : AppCompatActivity(), ServiciosContract.View {
    private lateinit var rcvServicios: RecyclerView
    private lateinit var presenter: ServiciosContract.Presentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_servicios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rcvServicios = findViewById(R.id.rcvServicios)

        presenter = ServiciosPresenter()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}
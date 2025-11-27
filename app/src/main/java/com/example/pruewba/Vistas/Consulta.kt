package com.example.pruewba.Vistas

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Presentador.Contratos.ConsultaContract
import com.example.pruewba.Presentador.ConsultaPresenter
import com.example.pruewba.R

class Consulta : AppCompatActivity(), ConsultaContract.View {
    private lateinit var txtConInfo: TextView
    private lateinit var presenter: ConsultaContract.Presentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consulta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtConInfo = findViewById(R.id.txtConInfo)

        presenter = ConsultaPresenter()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showInitialMessage(message: String) {
        txtConInfo.text = message
    }
}
package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.pruewba.Presentador.MainPresenter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.R


class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var btnBvnServicios: Button
    private lateinit var btnBvnConsulta: Button
    private lateinit var presenter: MainContract.Presentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnBvnServicios = findViewById(R.id.btnBvnServicios)
        btnBvnConsulta = findViewById(R.id.btnBvnConsulta)

        presenter = MainPresenter()
        presenter.attachView(this)

        btnBvnConsulta.setOnClickListener{
            presenter.handleConsultaEquipoClick()
        }

        btnBvnServicios.setOnClickListener {
            presenter.handleServiciosClick()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun navigateToLoginScreen() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    override fun navigateToServiciosScreen() {
        val intent = Intent(this, Servicios::class.java)
        startActivity(intent)
    }
}
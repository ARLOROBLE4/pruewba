package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.R

class ConsultaDetalle : AppCompatActivity() {

    // ... Vistas ...
    private lateinit var txtCltNumSerie: TextView
    private lateinit var txtCltModelo: TextView
    private lateinit var txtCltMarca: TextView
    private lateinit var txtCltFchRegistro: TextView
    private lateinit var txtCltEstado: TextView
    private lateinit var txtCltDetalles: TextView
    private lateinit var txtCltDiagnostico: TextView
    private lateinit var txtCltCosto: TextView
    private lateinit var imgCltEquipo: ImageView

    private lateinit var btnCltInicio: Button
    private lateinit var btnCltConsulta: Button
    private lateinit var btnCltPerfil: Button

    private lateinit var sessionManager: SesionManager


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consulta_detalle)

        // 1. Inicializar SessionManager
        sessionManager = SesionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Mapeo de Vistas
        txtCltNumSerie = findViewById(R.id.txtCltNumSerie)
        txtCltModelo = findViewById(R.id.txtCltModelo)
        txtCltMarca = findViewById(R.id.txtCltMarca)
        txtCltFchRegistro = findViewById(R.id.txtCltFchRegistro)
        txtCltEstado = findViewById(R.id.txtCltEstado)
        txtCltDetalles = findViewById(R.id.txtCltDetalles)
        txtCltDiagnostico = findViewById(R.id.txtCltDiagnostico)
        txtCltCosto = findViewById(R.id.txtCltCosto)
        imgCltEquipo = findViewById(R.id.imgCltEquipo)

        btnCltInicio = findViewById(R.id.btnCltInicio)
        btnCltConsulta = findViewById(R.id.btnCltConsulta)
        btnCltPerfil = findViewById(R.id.btnCltPerfil)

        val intent = intent
        val numSerie = intent.getStringExtra("numero_serie") ?: "N/D"
        val marca = intent.getStringExtra("marca") ?: "N/D"
        val modelo = intent.getStringExtra("modelo") ?: "N/D"
        val estado = intent.getStringExtra("estado") ?: "N/D"
        val fechaRegistro = intent.getStringExtra("fecha_ingreso") ?: "N/D"
        val detalles = intent.getStringExtra("detalles") ?: "Sin detalles registrados."
        val diagnostico = intent.getStringExtra("diagnostico") ?: "Pendiente de diagnóstico."
        val costo = intent.getStringExtra("costo") ?: "0.00"

        txtCltNumSerie.text = numSerie
        txtCltModelo.text = modelo
        txtCltMarca.text = marca
        txtCltEstado.text = estado
        txtCltFchRegistro.text = fechaRegistro
        txtCltDetalles.text = detalles
        txtCltDiagnostico.text = diagnostico
        txtCltCosto.text = "$ $costo"

        // 4. Configurar Listeners de Navegación Global
        btnCltInicio.setOnClickListener { navigateToMainActivity() }
        btnCltConsulta.setOnClickListener { navigateToServiciosActivity() }

        btnCltPerfil.setOnClickListener {
            validateAndNavigateToHistorial()
        }
    }

    private fun validateAndNavigateToHistorial() {
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, Historial::class.java))
        } else {
            startActivity(Intent(this, Login::class.java))
        }
    }

    // Métodos de navegación auxiliar
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        finish()
    }
    private fun navigateToServiciosActivity() {
        startActivity(Intent(this, Servicios::class.java))
    }
    private fun navigateToHistorialActivity() {
        // Redirigido a validateAndNavigateToHistorial()
    }
}
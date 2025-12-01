package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.pruewba.Modelo.SesionManager // 🛑 Importar SesionManager
import com.example.pruewba.R

// Esta Activity solo recibe datos del Intent, no necesita Contrato ni Presenter
class ServicioDetalle : AppCompatActivity() {

    private lateinit var imgSrvServicio: ImageView
    private lateinit var txtSrvTitulo: TextView
    private lateinit var txtSrvDescripcion: TextView
    private lateinit var btnSrvAgendar: Button

    private lateinit var btnSrvInicio: Button
    private lateinit var btnSrvServicios: Button
    private lateinit var btnSrvConsulta: Button

    private lateinit var sessionManager: SesionManager

    private val BASE_IMAGE_URL = "https://pcextreme.grupoctic.com/appWeb/aseets/"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_servicio_detalle)

        // 1. Inicializar SessionManager
        sessionManager = SesionManager(this) // Inicializar Session Manager

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Mapeo de Vistas
        imgSrvServicio = findViewById(R.id.imgSrvServicio)
        txtSrvTitulo = findViewById(R.id.txtSrvTitulo)
        txtSrvDescripcion = findViewById(R.id.txtSrvDescripcion)
        btnSrvAgendar = findViewById(R.id.btnSrvAgendar)

        btnSrvInicio = findViewById(R.id.btnBvnInicio)
        btnSrvServicios = findViewById(R.id.btnBvnServicios)
        btnSrvConsulta = findViewById(R.id.btnBvnConsulta)

        // 3. Obtener datos del Intent
        val servicioTitulo = intent.getStringExtra("titulo")
        val servicioDescripcion = intent.getStringExtra("descripcion")
        val servicioImagen = intent.getStringExtra("imagen")
        val servicioId = intent.getIntExtra("id", -1)

        // 4. Cargar Imagen y Texto
        if (servicioImagen != null) {
            Glide.with(this)
                .load(BASE_IMAGE_URL + servicioImagen)
                .into(imgSrvServicio)
        }

        txtSrvTitulo.text = servicioTitulo
        txtSrvDescripcion.text = servicioDescripcion

        // Botón Agendar
        btnSrvAgendar.setOnClickListener {
            // Redirige a Agenda (similar a Servicios.kt)
            val intent = Intent(this, Agenda::class.java).apply {
                putExtra("servicio_id", servicioId)
                putExtra("servicio_titulo", servicioTitulo)
            }
            startActivity(intent)
        }

        // Navegación Global
        btnSrvInicio.setOnClickListener { navigateToMainActivity() }
        btnSrvServicios.setOnClickListener { navigateToServiciosActivity() }


        btnSrvConsulta.setOnClickListener {
            validateAndNavigateToHistorial()
        }
    }

    //Se valida sesion
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
}
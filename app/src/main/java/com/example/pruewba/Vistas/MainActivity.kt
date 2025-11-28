package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.Presentador.MainPresenter
import com.example.pruewba.Modelo.inicioModel
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.R

// Nota: Se elimina la importación de VideoView

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var btnBvnServicios: Button
    private lateinit var btnBvnConsulta: Button
    // 🛑 ELIMINADO: private lateinit var btnBvnChat: Button
    private lateinit var btnCerrarSesion: Button
    // 🛑 ELIMINADO: private lateinit var vdEmpresa: VideoView
    private lateinit var txtBvnPublicidad3: TextView
    private lateinit var txtBvnInfoPubli3: TextView

    private lateinit var presenter: MainContract.Presentador
    private lateinit var sessionManager: SesionManager

    // URL base para el directorio de videos (se mantiene por si se usa en otro lado)
    private val BASE_VIDEO_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/videos/"


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inicializar Vistas
        btnBvnServicios = findViewById(R.id.btnBvnServicios)
        btnBvnConsulta = findViewById(R.id.btnBvnConsulta)
        // 🛑 ELIMINADO: btnBvnChat = findViewById(R.id.btnBvnChat)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        // 🛑 ELIMINADO: vdEmpresa = findViewById(R.id.vdEmpresa)
        txtBvnPublicidad3 = findViewById(R.id.txtBvnPublicidad3)
        txtBvnInfoPubli3 = findViewById(R.id.txtBvnInfoPubli3)

        // 2. Inicializar Presenter y SessionManager
        sessionManager = SesionManager(this)
        presenter = MainPresenter(inicioModel(), sessionManager)
        presenter.attachView(this)

        // 3. Cargar datos iniciales
        presenter.loadInitialData()

        // 4. Configurar Listeners

        btnBvnConsulta.setOnClickListener {
            presenter.handleConsultaEquipoClick()
        }

        btnBvnServicios.setOnClickListener {
            presenter.handleServiciosClick()
        }

        // 🛑 Listener para Cerrar Sesión
        btnCerrarSesion.setOnClickListener {
            handleLogout()
        }

        // 🛑 ELIMINADO: Listener para Chat

        // 5. Configurar visibilidad inicial de los botones sensibles a la sesión
        setupSessionButtonsVisibility()
    }

    override fun onResume() {
        super.onResume()
        setupSessionButtonsVisibility()
        // 🛑 ELIMINADO: vdEmpresa.start()
    }

    override fun onPause() {
        super.onPause()
        // 🛑 ELIMINADO: vdEmpresa.pause()
    }

    // 🛑 Lógica para controlar la visibilidad (solo queda btnCerrarSesion)
    private fun setupSessionButtonsVisibility() {
        if (sessionManager.isLoggedIn()) {
            // 🛑 btnBvnChat ELIMINADO de aquí
            btnCerrarSesion.visibility = View.VISIBLE
        } else {
            // 🛑 btnBvnChat ELIMINADO de aquí
            btnCerrarSesion.visibility = View.GONE
        }
    }

    // 🛑 Lógica de Cierre de Sesión
    private fun handleLogout() {
        sessionManager.logout()
        Toast.makeText(this, "Sesión cerrada con éxito.", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        // 🛑 ELIMINADO: vdEmpresa.stopPlayback()
        presenter.detachView()
        super.onDestroy()
    }

    // --- Implementación de MainContract.View ---

    override fun navigateToLoginScreen() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    override fun navigateToServiciosScreen() {
        val intent = Intent(this, Servicios::class.java)
        startActivity(intent)
    }

    override fun navigateToHistorialScreen() {
        val intent = Intent(this, Historial::class.java)
        startActivity(intent)
    }

    override fun showDatosInicio(titulo: String, descripcion: String) {
        txtBvnPublicidad3.text = titulo
        txtBvnInfoPubli3.text = descripcion
    }

    override fun showDataError(message: String) {
        Toast.makeText(this, "Error de datos: $message", Toast.LENGTH_LONG).show()
    }

    override fun loadVideo(videoFileName: String) {
        if (!videoFileName.isNullOrEmpty()) {
        }
    }
}
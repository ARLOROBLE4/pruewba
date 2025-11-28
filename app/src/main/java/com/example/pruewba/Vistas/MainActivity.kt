package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.Presentador.MainPresenter
import com.example.pruewba.Modelo.inicioModel
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.R


class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var btnBvnServicios: Button
    private lateinit var btnBvnConsulta: Button
    private lateinit var btnBvnChat: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var vdEmpresa: VideoView
    private lateinit var txtBvnPublicidad3: TextView
    private lateinit var txtBvnInfoPubli3: TextView

    private lateinit var presenter: MainContract.Presentador
    private lateinit var sessionManager: SesionManager

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
        btnBvnChat = findViewById(R.id.btnBvnChat)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        vdEmpresa = findViewById(R.id.vdEmpresa)
        txtBvnPublicidad3 = findViewById(R.id.txtBvnPublicidad3)
        txtBvnInfoPubli3 = findViewById(R.id.txtBvnInfoPubli3)

        // 2. Inicializar Presenter y SessionManager
        sessionManager = SesionManager(this)
        // 🛑 CORRECCIÓN: Pasar ambos argumentos al constructor
        presenter = MainPresenter(inicioModel(), sessionManager)
        presenter.attachView(this)

        // 3. Cargar datos iniciales
        presenter.loadInitialData()

        // 4. Configurar Listeners

        // Botón de Consulta
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

        // Listener para Chat
        btnBvnChat.setOnClickListener {
            val intent = Intent(this, Chat::class.java)
            startActivity(intent)
        }

        // 5. Configurar visibilidad inicial de los botones sensibles a la sesión
        setupSessionButtonsVisibility()
    }

    override fun onResume() {
        super.onResume()
        setupSessionButtonsVisibility()
        vdEmpresa.start()
    }

    override fun onPause() {
        super.onPause()
        vdEmpresa.pause()
    }

    // 🛑 Lógica para controlar la visibilidad de los botones
    private fun setupSessionButtonsVisibility() {
        if (sessionManager.isLoggedIn()) {
            btnBvnChat.visibility = View.VISIBLE
            btnCerrarSesion.visibility = View.VISIBLE
        } else {
            btnBvnChat.visibility = View.GONE
            btnCerrarSesion.visibility = View.GONE
        }
    }

    // 🛑 Lógica de Cierre de Sesión
    private fun handleLogout() {
        sessionManager.logout() // Borra la sesión
        Toast.makeText(this, "Sesión cerrada con éxito.", Toast.LENGTH_SHORT).show()

        // Redirige a Login y limpia el stack de actividades
        val intent = Intent(this, Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        vdEmpresa.stopPlayback()
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
        if (videoFileName.isNullOrEmpty()) {
            showDataError("La URL del video está vacía.")
            return
        }

        val videoPath = Uri.parse(BASE_VIDEO_URL + videoFileName)
        vdEmpresa.setVideoURI(videoPath)

        vdEmpresa.setOnPreparedListener { mp ->
            mp.isLooping = true
            vdEmpresa.start()
        }

        vdEmpresa.setOnErrorListener { mp, what, extra ->
            Toast.makeText(this, "Error al cargar video: $what", Toast.LENGTH_LONG).show()
            true
        }
    }
}
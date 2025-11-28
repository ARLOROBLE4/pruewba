package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.example.pruewba.Modelo.SesionManager // 🛑 Importar SessionManager
import com.example.pruewba.R


class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var btnBvnServicios: Button
    private lateinit var btnBvnConsulta: Button
    private lateinit var vdEmpresa: VideoView
    private lateinit var txtBvnPublicidad3: TextView
    private lateinit var txtBvnInfoPubli3: TextView

    private lateinit var presenter: MainContract.Presentador
    private lateinit var sessionManager: SesionManager // 🛑 NUEVO

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

        // 1. Mapeo de Vistas
        btnBvnServicios = findViewById(R.id.btnBvnServicios)
        btnBvnConsulta = findViewById(R.id.btnBvnConsulta)
        vdEmpresa = findViewById(R.id.vdEmpresa)
        txtBvnPublicidad3 = findViewById(R.id.txtBvnPublicidad3)
        txtBvnInfoPubli3 = findViewById(R.id.txtBvnInfoPubli3)


        // 2. Inicialización del Presenter
        sessionManager = SesionManager(this) // 🛑 Inicializar SessionManager
        presenter = MainPresenter(inicioModel(), sessionManager) // 🛑 Pasar SessionManager
        presenter.attachView(this)

        // 3. Carga de datos de la API al iniciar la Activity
        presenter.loadInitialData()

        // 4. Configuración de Listeners
        btnBvnConsulta.setOnClickListener{
            presenter.handleConsultaEquipoClick()
        }
        btnBvnServicios.setOnClickListener {
            presenter.handleServiciosClick()
        }
    }

    override fun onResume() {
        super.onResume()
        vdEmpresa.start()
    }

    override fun onPause() {
        super.onPause()
        vdEmpresa.pause()
    }

    override fun onDestroy() {
        vdEmpresa.stopPlayback()
        presenter.detachView()
        super.onDestroy()
    }

    // --- Implementación de MainContratos.View ---

    override fun navigateToLoginScreen() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    override fun navigateToServiciosScreen() {
        val intent = Intent(this, Servicios::class.java)
        startActivity(intent)
    }

    // 🛑 NUEVO: Implementación para navegar a Historial
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
            showDataError("Error al reproducir el video (Code: $what)")
            true
        }
    }
}
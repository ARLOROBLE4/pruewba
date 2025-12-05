package com.example.pruewba.Vistas

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Modelo.clsDatosRespuesta
import com.example.pruewba.Modelo.ifaceApiService
import com.example.pruewba.Modelo.inicioModel
import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.Presentador.MainPresenter
import com.example.pruewba.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), MainContract.View {

    // Vistas
    private lateinit var btnBvnServicios: Button
    private lateinit var btnBvnConsulta: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var txtBvnPublicidad3: TextView
    private lateinit var txtBvnInfoPubli3: TextView

    // Video
    private lateinit var playerView: PlayerView
    private var exoPlayer: ExoPlayer? = null

    // Presenter y Managers
    private lateinit var presenter: MainContract.Presentador
    private lateinit var sessionManager: SesionManager

    // --- NUEVO: Lanzador para solicitar permiso de notificaciones ---
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notificaciones habilitadas", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Sin permiso de notificaciones", Toast.LENGTH_SHORT).show()
        }
    }

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

        // --- NUEVO: Verificar permiso al iniciar la vista ---
        checkNotificationPermission()

        // 1. Inicializar Vistas
        btnBvnServicios = findViewById(R.id.btnBvnServicios)
        btnBvnConsulta = findViewById(R.id.btnBvnConsulta)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        txtBvnPublicidad3 = findViewById(R.id.txtBvnPublicidad3)
        txtBvnInfoPubli3 = findViewById(R.id.txtBvnInfoPubli3)

        // Mapeo del PlayerView
        playerView = findViewById(R.id.plyvideo)

        // 2. Inicializar SessionManager y Presenter
        sessionManager = SesionManager(this)
        presenter = MainPresenter(inicioModel(), sessionManager)
        presenter.attachView(this)

        // 3. Cargar datos iniciales
        presenter.loadInitialData()

        // 4. Configurar Listeners
        btnBvnConsulta.setOnClickListener { presenter.handleConsultaEquipoClick() }
        btnBvnServicios.setOnClickListener { presenter.handleServiciosClick() }
        btnCerrarSesion.setOnClickListener { handleLogout() }

        setupSessionButtonsVisibility()
    }

    // --- CICLO DE VIDA DEL VIDEO ---
    override fun onStart() {
        super.onStart()
        presenter.loadInitialData()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            player.release()
        }
        exoPlayer = null
    }

    override fun onResume() {
        super.onResume()
        setupSessionButtonsVisibility()
    }

    private fun setupSessionButtonsVisibility() {
        if (sessionManager.isLoggedIn()) {
            btnCerrarSesion.visibility = View.VISIBLE
        } else {
            btnCerrarSesion.visibility = View.GONE
        }
    }

    private fun handleLogout() {
        val userId = sessionManager.getUserId()
        if (userId > 0) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(ifaceApiService::class.java)

            service.cerrarSesionServidor(userId).enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(call: Call<List<clsDatosRespuesta>>, response: Response<List<clsDatosRespuesta>>) {
                    Log.d("Logout", "Token eliminado")
                }
                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    Log.e("Logout", "Fallo red: ${t.message}")
                }
            })
        }
        sessionManager.logout()
        Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        presenter.detachView()
        releasePlayer()
        super.onDestroy()
    }

    // --- Implementación de MainContract.View ---

    override fun navigateToLoginScreen() {
        startActivity(Intent(this, Login::class.java))
    }

    override fun navigateToServiciosScreen() {
        startActivity(Intent(this, Servicios::class.java))
    }

    override fun navigateToHistorialScreen() {
        startActivity(Intent(this, Historial::class.java))
    }

    override fun showDatosInicio(titulo: String, descripcion: String) {
        txtBvnPublicidad3.text = titulo
        txtBvnInfoPubli3.text = descripcion
    }

    override fun showDataError(message: String) {
        Toast.makeText(this, "Aviso: $message", Toast.LENGTH_LONG).show()
    }

    // --- AQUÍ SE REPRODUCE EL VIDEO ---
    override fun loadVideo(videoUrl: String) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(this).build()
            playerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri(videoUrl)
            exoPlayer?.setMediaItem(mediaItem)

            // 1. ACTIVAR EL BUCLE INFINITO
            exoPlayer?.repeatMode = ExoPlayer.REPEAT_MODE_ONE

            exoPlayer?.prepare()
            exoPlayer?.playWhenReady = true
        }
    }

    // --- NUEVO: Función lógica para verificar permisos ---
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
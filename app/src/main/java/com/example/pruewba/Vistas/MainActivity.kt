package com.example.pruewba.Vistas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem // Importar MediaItem
import androidx.media3.exoplayer.ExoPlayer // Importar ExoPlayer
import androidx.media3.ui.PlayerView // Importar PlayerView
import com.example.pruewba.Presentador.Contratos.MainContract
import com.example.pruewba.Presentador.MainPresenter
import com.example.pruewba.Modelo.inicioModel
import com.example.pruewba.R


class MainActivity : AppCompatActivity(), MainContract.View {

    private val TAG = "VideoDebug"

    private lateinit var btnBvnServicios: Button
    private lateinit var btnBvnConsulta: Button
    // CAMBIO 1: Cambiamos VideoView por PlayerView de Media3
    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer // Controlador del reproductor

    private lateinit var txtBvnPublicidad3: TextView
    private lateinit var txtBvnInfoPubli3: TextView

    private lateinit var presenter: MainContract.Presentador

    private val BASE_VIDEO_URL = "https://pcextreme.grupoctic.com/appMovil/PCStatus/videos/"


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
        // CAMBIO 2: Mapeamos el ID vdEmpresa al PlayerView
        playerView = findViewById(R.id.vdEmpresa)
        txtBvnPublicidad3 = findViewById(R.id.txtBvnPublicidad3)
        txtBvnInfoPubli3 = findViewById(R.id.txtBvnInfoPubli3)

        // 3. Inicialización de ExoPlayer (se crea aquí)
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer
        // Ocultar los controles de reproducción si solo queremos un fondo
        playerView.useController = false


        presenter = MainPresenter(inicioModel())
        presenter.attachView(this)
        presenter.loadInitialData()

        btnBvnConsulta.setOnClickListener{
            presenter.handleConsultaEquipoClick()
        }
        btnBvnServicios.setOnClickListener {
            presenter.handleServiciosClick()
        }
    }

    override fun onResume() {
        super.onResume()
        // Reanudar la reproducción al volver a la Activity
        exoPlayer.play()
    }

    override fun onPause() {
        super.onPause()
        // Pausar el reproductor en onPause para liberar recursos
        exoPlayer.pause()
    }

    override fun onDestroy() {
        // Liberar el reproductor en onDestroy
        exoPlayer.release()
        presenter.detachView()
        super.onDestroy()
    }

    // --- FUNCIÓN loadVideo MODIFICADA PARA EXOPLAYER ---

    override fun loadVideo(videoFileName: String) {
        if (videoFileName.isNullOrEmpty()) {
            showDataError("La URL del video está vacía.")
            return
        }

        val videoUriString = BASE_VIDEO_URL + videoFileName
        Log.d(TAG, "URI Final construida: $videoUriString")

        val mediaItem = MediaItem.fromUri(Uri.parse(videoUriString))

        // Asignar el MediaItem al reproductor
        exoPlayer.setMediaItem(mediaItem)

        // Habilitar la reproducción en bucle (looping)
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE

        // Agregar un listener básico para registrar errores (opcional)
        exoPlayer.addListener(object : androidx.media3.common.Player.Listener {
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                Log.e(TAG, "ExoPlayer Error: ${error.message}")
                showDataError("Error de reproducción (ExoPlayer): ${error.errorCodeName}")
            }
        })

        // Preparar y empezar la reproducción
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    // --- Otras implementaciones de MainContratos.View (sin cambios) ---
    override fun navigateToLoginScreen() { /* ... */ }
    override fun navigateToServiciosScreen() { /* ... */ }
    override fun showDatosInicio(titulo: String, descripcion: String) {
        txtBvnPublicidad3.text = titulo
        txtBvnInfoPubli3.text = descripcion
    }
    override fun showDataError(message: String) {
        Log.e(TAG, "Error: $message")
        Toast.makeText(this, "Error de datos: $message", Toast.LENGTH_LONG).show()
    }
}
package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pruewba.Modelo.HistorialModel
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Modelo.clsDispositivoHistorial
import com.example.pruewba.Presentador.Contratos.HistorialContract
import com.example.pruewba.Presentador.HistorialPresenter
import com.example.pruewba.R

class Historial : AppCompatActivity(), HistorialContract.View {

    private lateinit var rcvHistorial: RecyclerView
    private lateinit var presenter: HistorialContract.Presentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Mapeo y Configuración del RecyclerView
        rcvHistorial = findViewById(R.id.rcvHistorial)
        rcvHistorial.layoutManager = LinearLayoutManager(this)

        // 2. Inicializar Modelos y Presenter
        val historialModel = HistorialModel()
        val sessionManager = SesionManager(this)
        presenter = HistorialPresenter(historialModel, sessionManager)

        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    // --- Implementación de HistorialContract.View ---

    override fun displayHistorial(dispositivos: List<clsDispositivoHistorial>) {
        if (dispositivos.isEmpty()) {
            showLoadingError("No tienes equipos registrados.")
            return
        }
        val adapter = HistorialAdapter(dispositivos) { dispositivo ->
            presenter.handleVerMasClick(dispositivo)
        }
        rcvHistorial.adapter = adapter
    }

    override fun showLoadingError(message: String) {
        Toast.makeText(this, "Error al cargar historial: $message", Toast.LENGTH_LONG).show()
    }

    // 🛑 Modificado: Pasa TODOS los campos al Intent para ConsultaDetalle.kt
    override fun navigateToDetalleConsulta(dispositivo: clsDispositivoHistorial) {
        val intent = Intent(this, ConsultaDetalle::class.java).apply {
            putExtra("id_registro", dispositivo.idRegistro)
            putExtra("numero_serie", dispositivo.numeroSerie)
            putExtra("marca", dispositivo.marca)
            putExtra("modelo", dispositivo.modelo)
            putExtra("estado", dispositivo.estado)
            // 🛑 Nuevos campos de detalle
            putExtra("fecha_ingreso", dispositivo.fechaIngreso)
            putExtra("detalles", dispositivo.detalles)
            putExtra("diagnostico", dispositivo.diagnostico)
            putExtra("costo", dispositivo.costo)
        }
        startActivity(intent)
    }
}
package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
    private lateinit var sessionManager: SesionManager

    private lateinit var btnConInicio3: Button
    private lateinit var btnConPerfil3: Button
    private lateinit var btnConConsulta3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Mapeo del RecyclerView y Botones
        rcvHistorial = findViewById(R.id.rcvHistorial)
        rcvHistorial.layoutManager = LinearLayoutManager(this)

        btnConInicio3 = findViewById(R.id.btnConInicio3)
        btnConPerfil3 = findViewById(R.id.btnConPerfil3)
        btnConConsulta3 = findViewById(R.id.btnConConsulta3)

        // 2. Inicializar Presenter y SessionManager
        sessionManager = SesionManager(this)
        val historialModel = HistorialModel()
        presenter = HistorialPresenter(historialModel, sessionManager)

        presenter.attachView(this)

        // 3. Listeners de Navegación Global
        btnConInicio3.setOnClickListener { navigateToMainActivity() }
        btnConPerfil3.setOnClickListener { navigateToServiciosActivity() }

        btnConConsulta3.setOnClickListener {
            validateAndNavigateToHistorial() // Va a Historial o Login
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    private fun validateAndNavigateToHistorial() {
        if (sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Ya estás viendo el historial.", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this, Login::class.java))
        }
    }

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

    override fun navigateToDetalleConsulta(dispositivo: clsDispositivoHistorial) {
        val intent = Intent(this, ConsultaDetalle::class.java).apply {
            putExtra("id_registro", dispositivo.idRegistro)
            putExtra("numero_serie", dispositivo.numeroSerie)
            putExtra("marca", dispositivo.marca)
            putExtra("modelo", dispositivo.modelo)
            putExtra("estado", dispositivo.estado)
            putExtra("fecha_ingreso", dispositivo.fechaIngreso)
            putExtra("detalles", dispositivo.detalles)
            putExtra("diagnostico", dispositivo.diagnostico)
            putExtra("costo", dispositivo.costo)
        }
        startActivity(intent)
    }

    // Métodos de navegación auxiliar
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        finish()
    }
    private fun navigateToServiciosActivity() {
        startActivity(Intent(this, Servicios::class.java))
    }
    private fun navigateToConsultaActivity() {
        // Redirigido a validateAndNavigateToHistorial()
    }
}
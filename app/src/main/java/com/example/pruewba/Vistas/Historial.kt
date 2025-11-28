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

    // Botones de navegación (IDs de activity_historial.xml)
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

        // 2. Inicializar Presenter
        val historialModel = HistorialModel()
        val sessionManager = SesionManager(this)
        presenter = HistorialPresenter(historialModel, sessionManager)

        presenter.attachView(this)

        // 3. Listeners de Navegación
        btnConInicio3.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        }

        btnConPerfil3.setOnClickListener {
            startActivity(Intent(this, Servicios::class.java))
        }

        btnConConsulta3.setOnClickListener {
            // Navega a la pantalla de consulta genérica
            startActivity(Intent(this, Consulta::class.java))
        }
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
        // El adaptador maneja el clic y llama a handleVerMasClick
        val adapter = HistorialAdapter(dispositivos) { dispositivo ->
            presenter.handleVerMasClick(dispositivo)
        }
        rcvHistorial.adapter = adapter
    }

    override fun showLoadingError(message: String) {
        Toast.makeText(this, "Error al cargar historial: $message", Toast.LENGTH_LONG).show()
    }

    // 🛑 Función clave: Pasa TODOS los 8 campos de detalle
    override fun navigateToDetalleConsulta(dispositivo: clsDispositivoHistorial) {
        val intent = Intent(this, ConsultaDetalle::class.java).apply {
            // 8 CAMPOS DE DATOS:
            putExtra("id_registro", dispositivo.idRegistro)
            putExtra("numero_serie", dispositivo.numeroSerie) // 1. Número de Serie
            putExtra("marca", dispositivo.marca) // 2. Marca
            putExtra("modelo", dispositivo.modelo) // 3. Modelo
            putExtra("estado", dispositivo.estado) // 4. Estado
            putExtra("fecha_ingreso", dispositivo.fechaIngreso) // 5. Fecha de Ingreso
            putExtra("detalles", dispositivo.detalles) // 6. Detalles
            putExtra("diagnostico", dispositivo.diagnostico) // 7. Diagnóstico
            putExtra("costo", dispositivo.costo) // 8. Costo
        }
        startActivity(intent)
    }
}
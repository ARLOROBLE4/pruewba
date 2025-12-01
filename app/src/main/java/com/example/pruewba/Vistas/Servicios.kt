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
import com.example.pruewba.Modelo.ServiciosModel
import com.example.pruewba.Modelo.clsServicio
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Presentador.Contratos.ServiciosContract
import com.example.pruewba.R
import com.example.pruewba.Presentador.ServiciosPresenter

class Servicios : AppCompatActivity(), ServiciosContract.View {
    private lateinit var rcvServicios: RecyclerView
    private lateinit var presenter: ServiciosContract.Presentador
    private lateinit var sessionManager: SesionManager

    private lateinit var btnBvnInicio2: Button
    private lateinit var btnBvnServicios2: Button
    private lateinit var btnBvnConsulta2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_servicios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Mapeo de vistas
        rcvServicios = findViewById(R.id.rcvServicios)
        btnBvnInicio2 = findViewById(R.id.btnBvnInicio)
        btnBvnServicios2 = findViewById(R.id.btnBvnServicios)
        btnBvnConsulta2 = findViewById(R.id.btnBvnConsulta)

        // 2. Inicializar Presenter y SessionManager
        sessionManager = SesionManager(this)
        presenter = ServiciosPresenter(ServiciosModel())
        presenter.attachView(this)

        // Configurar RecyclerView
        rcvServicios.layoutManager = LinearLayoutManager(this)

        // Iniciar la carga de datos
        presenter.loadServices()

        btnBvnInicio2.setOnClickListener { navigateToMainActivity() }
        btnBvnServicios2.setOnClickListener { /* No hacer nada, ya está en Servicios */ }

        btnBvnConsulta2.setOnClickListener {
            validateAndNavigateToHistorial()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    // Se valida la sesion
    private fun validateAndNavigateToHistorial() {
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, Historial::class.java))
        } else {
            startActivity(Intent(this, Login::class.java))
        }
    }

    override fun displayServices(servicios: List<clsServicio>) {
        // CORRECCIÓN AQUÍ: Se eliminó el parámetro onServiceClickListener
        val adapter = ServiciosAdapter(
            context = this,
            listaServicios = servicios,
            onAgendarClickListener = { servicio ->
                presenter.handleAgendarClick(servicio)
            }
        )
        rcvServicios.adapter = adapter
    }

    override fun showFetchServicesError(message: String) {
        Toast.makeText(this, "Error al cargar servicios: $message", Toast.LENGTH_LONG).show()
    }

    // Este método ya no se invocará desde la lista, pero se deja por si el Presenter lo requiere en el futuro
    // o puedes borrarlo si limpias el contrato.
    override fun navigateToServiceDetail(servicio: clsServicio) {
        // Código desactivado visualmente desde el adaptador
        val intent = Intent(this, ServicioDetalle::class.java).apply {
            putExtra("id", servicio.id)
            putExtra("titulo", servicio.titulo)
            putExtra("descripcion", servicio.descripcion)
            putExtra("imagen", servicio.imagen)
        }
        startActivity(intent)
    }

    override fun navigateToAgendaScreen(servicio: clsServicio) {
        val intent = Intent(this, Agenda::class.java).apply {
            putExtra("servicio_id", servicio.id)
            putExtra("servicio_titulo", servicio.titulo)
        }
        startActivity(intent)
    }

    // Métodos de navegación auxiliar
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        finish()
    }
}
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
import com.example.pruewba.Modelo.ServiciosModel // Usando la clase corregida
import com.example.pruewba.Modelo.clsServicio
import com.example.pruewba.Presentador.Contratos.ServiciosContract
import com.example.pruewba.R
import com.example.pruewba.Presentador.ServiciosPresenter

class Servicios : AppCompatActivity(), ServiciosContract.View {
    private lateinit var rcvServicios: RecyclerView
    private lateinit var presenter: ServiciosContract.Presentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_servicios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rcvServicios = findViewById(R.id.rcvServicios)

        // Inicializar Presenter con el Modelo
        presenter = ServiciosPresenter(ServiciosModel()) // Usando ServiciosModelo
        presenter.attachView(this)

        // Configurar RecyclerView
        rcvServicios.layoutManager = LinearLayoutManager(this)

        // Iniciar la carga de datos
        presenter.loadServices()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    // --- Implementación de ServiciosContract.View ---

    override fun displayServices(servicios: List<clsServicio>) {
        // Inicialización del adaptador con DOS listeners
        val adapter = ServiciosAdapter(
            context = this,
            listaServicios = servicios,
            onServiceClickListener = { servicio ->
                presenter.handleServiceClick(servicio) // Ir a Detalle
            },
            onAgendarClickListener = { servicio ->
                presenter.handleAgendarClick(servicio) // Ir a Agenda
            }
        )
        rcvServicios.adapter = adapter
    }

    override fun showFetchServicesError(message: String) {
        Toast.makeText(this, "Error al cargar servicios: $message", Toast.LENGTH_LONG).show()
    }

    override fun navigateToServiceDetail(servicio: clsServicio) {
        // Navegar a ServicioDetalle.kt
        val intent = Intent(this, ServicioDetalle::class.java).apply {
            putExtra("id", servicio.id)
            putExtra("titulo", servicio.titulo)
            putExtra("descripcion", servicio.descripcion)
            putExtra("imagen", servicio.imagen)
        }
        startActivity(intent)
    }

    override fun navigateToAgendaScreen(servicio: clsServicio) {
        // Navegar a Agenda.kt
        val intent = Intent(this, Agenda::class.java).apply {
            putExtra("servicio_id", servicio.id)
            putExtra("servicio_titulo", servicio.titulo)
        }
        startActivity(intent)
    }

}
package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    // Presenter y Managers
    private lateinit var presenter: MainContract.Presentador
    private lateinit var sessionManager: SesionManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajuste para bordes (EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inicializar Vistas
        btnBvnServicios = findViewById(R.id.btnBvnServicios)
        btnBvnConsulta = findViewById(R.id.btnBvnConsulta)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        txtBvnPublicidad3 = findViewById(R.id.txtBvnPublicidad3)
        txtBvnInfoPubli3 = findViewById(R.id.txtBvnInfoPubli3)

        // 2. Inicializar SessionManager y Presenter
        sessionManager = SesionManager(this)
        presenter = MainPresenter(inicioModel(), sessionManager)
        presenter.attachView(this)

        // 3. Cargar datos iniciales (Video/Texto bienvenida)
        presenter.loadInitialData()

        // 4. Configurar Listeners
        btnBvnConsulta.setOnClickListener {
            presenter.handleConsultaEquipoClick()
        }

        btnBvnServicios.setOnClickListener {
            presenter.handleServiciosClick()
        }

        btnCerrarSesion.setOnClickListener {
            handleLogout()
        }

        // 5. Configurar visibilidad inicial de botones según sesión
        setupSessionButtonsVisibility()
    }

    override fun onResume() {
        super.onResume()
        // Verificar sesión cada vez que se vuelve a esta pantalla
        setupSessionButtonsVisibility()
    }

    // Muestra u oculta el botón "Cerrar Sesión" dependiendo del estado
    private fun setupSessionButtonsVisibility() {
        if (sessionManager.isLoggedIn()) {
            btnCerrarSesion.visibility = View.VISIBLE
        } else {
            btnCerrarSesion.visibility = View.GONE
        }
    }

    // --- LÓGICA DE CIERRE DE SESIÓN SEGURO ---
    private fun handleLogout() {
        val userId = sessionManager.getUserId()

        // 1. Avisar al servidor para borrar el token FCM (Fire & Forget)
        if (userId > 0) {
            // Creamos una instancia temporal de Retrofit para esta llamada
            val retrofit = Retrofit.Builder()
                .baseUrl("https://pcextreme.grupoctic.com/appMovil/PCStatus/Api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(ifaceApiService::class.java)

            service.cerrarSesionServidor(userId).enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(call: Call<List<clsDatosRespuesta>>, response: Response<List<clsDatosRespuesta>>) {
                    Log.d("Logout", "Token eliminado del servidor correctamente")
                }
                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    Log.e("Logout", "Fallo al avisar al servidor (posible falta de internet): ${t.message}")
                }
            })
        }

        // 2. Borrar datos locales y redirigir
        sessionManager.logout()
        Toast.makeText(this, "Sesión cerrada con éxito.", Toast.LENGTH_SHORT).show()

        // Reiniciar la actividad para limpiar pilas de navegación
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        presenter.detachView()
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

    override fun loadVideo(videoFileName: String) {
        // Implementación pendiente o eliminada según requerimientos
    }
}
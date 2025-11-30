package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Modelo.FCMModel
import com.example.pruewba.Presentador.Contratos.LoginContract
import com.example.pruewba.Modelo.accesoModel
import com.example.pruewba.Presentador.LoginPresenter
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.R
import com.google.firebase.messaging.FirebaseMessaging

class Login : AppCompatActivity(), LoginContract.View {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnAcceder: Button
    private lateinit var ckbPassword: CheckBox
    private lateinit var btnRegresar: Button
    private lateinit var presenter: LoginContract.Presentador
    private lateinit var sessionManager: SesionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Mapeo de Vistas
        etEmail = findViewById(R.id.edtLoginEmail)
        etPassword = findViewById(R.id.edtLoginPassword)
        btnAcceder = findViewById(R.id.btnLoguear)
        ckbPassword = findViewById(R.id.ckbPassword)
        btnRegresar = findViewById(R.id.btnRegresar)

        sessionManager = SesionManager(this)

        // 2. Lógica del Checkbox (Mostrar/Ocultar Contraseña)
        ckbPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etPassword.setSelection(etPassword.text.length)
        }

        // 3. Inicializar Presenter
        presenter = LoginPresenter(accesoModel(), sessionManager)
        presenter.attachView(this)

        // 4. Listeners

        // Listener para Iniciar Sesión
        btnAcceder.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            presenter.handleLoginButtonClick(email, password)
        }

        // Listener para Regresar
        btnRegresar.setOnClickListener {
            navigateToMainActivity()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
    override fun showLoginSuccess() {
        Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun navigateToConsultaScreen() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                val userId = sessionManager.getUserId()

                if (userId > 0 && token != null) {
                    FCMModel().sendTokenToServer(userId, token) { isSuccess, message ->
                        Log.d("LoginFCM", if (isSuccess) message else "Fallo al enviar token de FCM: $message")
                    }
                }
            } else {
                Log.e("LoginFCM", "Fallo al obtener el token de FCM", task.exception)
            }
        }
        val intent = Intent(this, Historial::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cierra la actividad de Login
    }
}
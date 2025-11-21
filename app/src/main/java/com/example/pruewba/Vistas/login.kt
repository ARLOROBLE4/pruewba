package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruewba.Modelo.accessModel
import com.example.pruewba.Modelo.loginContract
import com.example.pruewba.Presentador.loginPresenter
import com.example.pruewba.R

class login : AppCompatActivity(), loginContract.View {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnAcceder: Button // Corregido el nombre si usas R.id.btnLoguear
    private lateinit var presenter: loginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Usando tu layout activity_login.xml

        // 1. Enlazar Views
        etEmail = findViewById(R.id.edtLoginEmail)
        etPassword = findViewById(R.id.edtLoginPassword)
        btnAcceder = findViewById(R.id.btnLoguear) // Usando el ID de activity_login.xml

        // 2. Inicializar Presenter
        presenter = loginPresenter(accessModel())
        presenter.attachView(this)
        // 3. Manejar Click del botón Acceder
        btnAcceder.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            presenter.handleLoginButtonClick(email, password)
        }
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun navigateToRegistration() {
        //no se ocupa
    }

    override fun navigateToMainScreen() {
        // Navegar a la pantalla de bienvenida (activity_bienvenida) como solicitaste
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finish() // Cierra la pantalla de login

    }
}
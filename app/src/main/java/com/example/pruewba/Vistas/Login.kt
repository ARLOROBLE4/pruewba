package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Presentador.Contratos.LoginContract
import com.example.pruewba.Modelo.accesoModel
import com.example.pruewba.Presentador.LoginPresenter
import com.example.pruewba.R

class Login : AppCompatActivity(), LoginContract.View {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnAcceder: Button
    private lateinit var presenter: LoginContract.Presentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etEmail = findViewById(R.id.edtLoginEmail)
        etPassword = findViewById(R.id.edtLoginPassword)
        btnAcceder = findViewById(R.id.btnLoguear)

        presenter = LoginPresenter(accesoModel())
        presenter.attachView(this)

        btnAcceder.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            presenter.handleLoginButtonClick(email, password)
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
        val intent = Intent(this, Consulta::class.java)
        startActivity(intent)
        finish()
    }
}
package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Enlazar botones
        val btnIrLogin = findViewById<Button>(R.id.btnIrLogin)
        val btnIrRegistro = findViewById<Button>(R.id.btnirRegistro)

        // 2. Click para ir a Login
        btnIrLogin.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        // 3. Click para ir a Registro
        btnIrRegistro.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
    }
}
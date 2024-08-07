package com.example.basurin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Agrega el click listener en el onCreate
        val btnRutas = findViewById<ImageView>(R.id.btnRutas)
        btnRutas.setOnClickListener {
            abrirRutas()
        }

        val btnEduca = findViewById<ImageView>(R.id.btnEduca)
        btnEduca.setOnClickListener{
            abrirEduca()
        }

        val btnReportes = findViewById<ImageView>(R.id.btnReportes)
        btnReportes.setOnClickListener{
            abrirReportes()
        }

        val btnAvisos = findViewById<ImageView>(R.id.btnAvisos)
        btnAvisos.setOnClickListener{
            abrirAvisos()
        }
    }

    // Abre la actividad Rutas
    private fun abrirRutas() {
        val intent = Intent(this, Rutas::class.java)
        startActivity(intent)
    }

    private fun abrirEduca(){

        val intent = Intent(this,Educa::class.java)
        startActivity(intent)

    }

    private fun abrirReportes(){

        val intent = Intent(this,Reportes::class.java)
        startActivity(intent)

    }

    private fun abrirAvisos(){

        val intent = Intent(this,Avisos::class.java)
        startActivity(intent)

    }
}

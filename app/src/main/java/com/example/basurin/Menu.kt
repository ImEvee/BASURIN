package com.example.basurin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val btnRutas = findViewById<Button>(R.id.btnRutas)
        val btnAvisos = findViewById<Button>(R.id.btnAvisos)
        val btnEduca = findViewById<Button>(R.id.btnEduca)
        val btnReportes = findViewById<Button>(R.id.btnEnviarReporte)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRutas.setOnClickListener{
            val intent = Intent(this, Rutas::class.java)
            startActivity(intent);
        }

        btnAvisos.setOnClickListener{
            val intent = Intent(this, Avisos::class.java)
            startActivity(intent);
        }

        btnEduca.setOnClickListener{
            val intent = Intent(this, Educa::class.java)
            startActivity(intent);
        }

        btnReportes.setOnClickListener{
            val intent = Intent(this, Reportes::class.java)
            startActivity(intent);
        }



    }
}
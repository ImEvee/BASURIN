package com.example.basurin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Reportes : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reportes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar la referencia a Firebase
        database = FirebaseDatabase.getInstance().reference

        val icon1: ImageButton = findViewById(R.id.btnRutas)
        val icon2: ImageButton = findViewById(R.id.btnNotificacion)
        val icon3: ImageButton = findViewById(R.id.btnInicio)
        val icon4: ImageButton = findViewById(R.id.btnEduca)
        val icon5: ImageButton = findViewById(R.id.btnReportes)
        val btnEnviarReporte: Button = findViewById(R.id.btnEnviarReporte)

        icon1.setOnClickListener {
            startActivity(Intent(this, Rutas::class.java))
        }

        icon2.setOnClickListener {
            startActivity(Intent(this, Avisos::class.java))
        }

        icon3.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        icon4.setOnClickListener {
            startActivity(Intent(this, Educa::class.java))
        }

        icon5.setOnClickListener {
            startActivity(Intent(this, Reportes::class.java))
        }

        // Acción al presionar el botón "Enviar Reporte"
        btnEnviarReporte.setOnClickListener {
            // Obtener el texto del TextView y enviarlo a Firebase
            val txtReportes: TextView = findViewById(R.id.txtReporte)
            val reportesTexto = txtReportes.text.toString()

            // Crear un ID único para cada reporte
            val reporteId = database.child("reportes").push().key

            // Enviar el texto a Firebase en el nodo "reportes" con un ID único
            if (reporteId != null) {
                database.child("reportes").child(reporteId).setValue(reportesTexto)
                    .addOnSuccessListener {
                        // Acción en caso de éxito
                        Toast.makeText(this, "Reporte enviado a Firebase", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // Acción en caso de error
                        Toast.makeText(this, "Error al enviar reporte", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}





package com.example.basurin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("DEPRECATION")
class Educa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_educa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar AutoCompleteTextView y manejar selecciones
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val options = arrayOf("Reutiliza", "Reduce", "Recicla")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, options)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.threshold = 1 // Mostrar opciones después de ingresar 1 carácter

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedOption = parent.getItemAtPosition(position) as String

            // Redirigir a la actividad correspondiente según la opción seleccionada
            when (selectedOption) {
                "Reutiliza" -> startActivity(Intent(this, EducaReutiliza::class.java))
                "Reduce" -> startActivity(Intent(this, EducaReduce::class.java))
                "Recicla" -> startActivity(Intent(this, EducaRecicla::class.java))
                else -> {/* Manejar opción por defecto si es necesario */}
            }
        }
        // Configurar AutoCompleteTextView y manejar selecciones
        val autoCompleteTextView2 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2   )
        val options2 = arrayOf("Organico", "Inorganico")
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, options2)
        autoCompleteTextView2.setAdapter(adapter2)
        autoCompleteTextView2.threshold = 1 // Mostrar opciones después de ingresar 1 carácter

        autoCompleteTextView2.setOnItemClickListener { parent, _, position, _ ->
            val selectedOption = parent.getItemAtPosition(position) as String

            // Redirigir a la actividad correspondiente según la opción seleccionada
            when (selectedOption) {
                "Organico" -> startActivity(Intent(this, EducaOrganico::class.java))
                "Inorganico" -> startActivity(Intent(this, EducaInorganico::class.java))
                else -> {/* Manejar opción por defecto si es necesario */}
            }
        }
    }

    private fun enableEdgeToEdge() {
        // Habilitar el modo Edge-to-Edge para pantalla completa
        this.window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                    android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}

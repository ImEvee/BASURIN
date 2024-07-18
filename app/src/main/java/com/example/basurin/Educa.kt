package com.example.basurin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
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
        val icon1: ImageButton = findViewById(R.id.icon1)
        val icon2: ImageButton = findViewById(R.id.icon2)
        val icon3: ImageButton = findViewById(R.id.icon3)
        val icon4: ImageButton = findViewById(R.id.icon4)
        val icon5: ImageButton = findViewById(R.id.icon5)

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
        val btnProhibido = findViewById<Button>(R.id.btnProhibido)


        btnProhibido.setOnClickListener{
            val intent = Intent(this, EducaProhibido::class.java)
            startActivity(intent);
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

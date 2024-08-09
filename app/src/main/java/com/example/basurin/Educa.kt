package com.example.basurin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("DEPRECATION")
class Educa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val icon1: ImageButton = findViewById(R.id.btnRutas)
        val icon2: ImageButton = findViewById(R.id.btnNotificacion)
        val icon3: ImageButton = findViewById(R.id.btnInicio)
        val icon4: ImageButton = findViewById(R.id.btnEduca)
        val icon5: ImageButton = findViewById(R.id.btnReportes)

        icon1.setOnClickListener {
            startActivity(Intent(this, Rutas::class.java))
        }

        icon2.setOnClickListener {
            startActivity(Intent(this, Avisos::class.java))
        }

        icon3.setOnClickListener {
            startActivity(Intent(this, Menu::class.java))
        }

        icon4.setOnClickListener {
            startActivity(Intent(this, Educa::class.java))
        }

        icon5.setOnClickListener {
            startActivity(Intent(this, Reportes::class.java))
        }


        // Configurar cada CardView
        val cardOrganico = findViewById<CardView>(R.id.cardOrganico)
        val cardInorganico = findViewById<CardView>(R.id.cardInorganico)
        val cardReduce = findViewById<CardView>(R.id.cardReduce)
        val cardReutiliza = findViewById<CardView>(R.id.cardReutiliza)
        val cardRecicla = findViewById<CardView>(R.id.cardRecicla)
        val cardProhibido = findViewById<CardView>(R.id.cardProhibido)

        // Configurar los OnClickListener para cada CardView
        cardOrganico.setOnClickListener {
            val intent = Intent(this, EducaOrganico::class.java)
            startActivity(intent)
        }

        cardInorganico.setOnClickListener {
            val intent = Intent(this, EducaInorganico::class.java)
            startActivity(intent)
        }

        cardReduce.setOnClickListener {
            val intent = Intent(this, EducaReduce::class.java)
            startActivity(intent)
        }

        cardReutiliza.setOnClickListener {
            val intent = Intent(this, EducaReutiliza::class.java)
            startActivity(intent)
        }

        cardRecicla.setOnClickListener {
            val intent = Intent(this, EducaRecicla::class.java)
            startActivity(intent)
        }

        cardProhibido.setOnClickListener {
            val intent = Intent(this, EducaProhibido::class.java)
            startActivity(intent)
        }


    }
}

package com.example.siyakhula

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Donations : AppCompatActivity() {

    private lateinit var stewardshipButton: Button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donations)

        stewardshipButton = findViewById(R.id.stewardshipButton)
        backButton = findViewById(R.id.backButton)

        stewardshipButton.setOnClickListener{
            val url = "https://account.stewardship.org.uk/gift/start/20327069?donationType=OneOff"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        backButton.setOnClickListener{
            startActivity(Intent(this , Dashboard::class.java))
        }
    }
}
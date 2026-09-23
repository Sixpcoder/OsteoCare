package eightBugs.osteocare.screening.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import eightBugs.osteocare.R
import eightBugs.osteocare.patient.newPatient

class records : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_records)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        findViewById<android.view.View>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // Add new screening button
        findViewById<android.view.View>(R.id.fab_add_screening).setOnClickListener {
            startActivity(Intent(this, newPatient::class.java))
        }
    }
}
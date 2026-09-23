package eightBugs.osteocare.home

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import eightBugs.osteocare.R
import eightBugs.osteocare.patient.newPatient
import eightBugs.osteocare.screening.ui.records
import kotlin.jvm.java

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // Add new patient
        findViewById<android.view.View>(R.id.card_add_patient).setOnClickListener {
            startActivity(Intent(this, newPatient::class.java))
        }

        // Check previous records
        findViewById<android.view.View>(R.id.card_check_records).setOnClickListener {
            startActivity(Intent(this, records::class.java))
        }

        val btnEnglish = findViewById<TextView>(R.id.btnEnglish)
        val btnAssamese = findViewById<TextView>(R.id.btnAssamese)

        btnEnglish.setOnClickListener {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags("en")
            )
        }

        btnAssamese.setOnClickListener {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags("as")
            )
        }




    }
}
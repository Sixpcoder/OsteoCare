package eightBugs.osteocare.patient

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView
import eightBugs.osteocare.R
import eightBugs.osteocare.screening.ui.questionaire

class newPatient : AppCompatActivity() {

    // Store selected sex
    private var selectedSex: String? = null

    // Store consent status
    private var consentGiven = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_patient)

        // --------------------------------------------------
        // Back button
        // --------------------------------------------------

        findViewById<android.view.View>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // --------------------------------------------------
        // Find views
        // --------------------------------------------------

        val fullName =
            findViewById<EditText>(R.id.et_full_name)

        val age =
            findViewById<EditText>(R.id.et_age)

        val phone =
            findViewById<EditText>(R.id.et_phone)

        val femaleButton =
            findViewById<TextView>(R.id.btn_sex_female)

        val maleButton =
            findViewById<TextView>(R.id.btn_sex_male)

        val village =
            findViewById<EditText>(R.id.et_village)

        val block =
            findViewById<EditText>(R.id.et_block)

        val district =
            findViewById<EditText>(R.id.et_district)

        val occupation =
            findViewById<EditText>(R.id.et_occupation)

        val consentButton =
            findViewById<android.view.View>(R.id.btn_record_consent)

        // --------------------------------------------------
        // Sex selection
        // --------------------------------------------------

        // Start with no selection
        selectedSex = null

        femaleButton.setBackgroundResource(
            R.drawable.bg_toggle_unselected
        )
        femaleButton.setTextColor(
            getColor(R.color.primary_blue)
        )

        maleButton.setBackgroundResource(
            R.drawable.bg_toggle_unselected
        )
        maleButton.setTextColor(
            getColor(R.color.primary_blue)
        )

        femaleButton.setOnClickListener {

            selectedSex = "Female"

            femaleButton.setBackgroundResource(
                R.drawable.bg_toggle_selected
            )
            femaleButton.setTextColor(
                getColor(R.color.white)
            )

            maleButton.setBackgroundResource(
                R.drawable.bg_toggle_unselected
            )
            maleButton.setTextColor(
                getColor(R.color.primary_blue)
            )
        }

        maleButton.setOnClickListener {

            selectedSex = "Male"

            maleButton.setBackgroundResource(
                R.drawable.bg_toggle_selected
            )
            maleButton.setTextColor(
                getColor(R.color.white)
            )

            femaleButton.setBackgroundResource(
                R.drawable.bg_toggle_unselected
            )
            femaleButton.setTextColor(
                getColor(R.color.primary_blue)
            )
        }

        // --------------------------------------------------
        // Record consent
        // --------------------------------------------------

        consentButton.setOnClickListener {

            consentGiven = !consentGiven

            if (consentGiven) {

                consentButton.setBackgroundResource(
                    R.drawable.bg_toggle_selected
                )

            } else {

                consentButton.setBackgroundResource(
                    R.drawable.bg_toggle_unselected
                )
            }
        }

        // --------------------------------------------------
        // Save & Continue
        // --------------------------------------------------

        findViewById<MaterialTextView>(R.id.btn_save_continue)
            .setOnClickListener {

                // Clear previous errors
                fullName.error = null
                age.error = null
                phone.error = null
                village.error = null
                block.error = null
                district.error = null
                occupation.error = null

                // ------------------------------------------
                // Full name validation
                // ------------------------------------------

                val nameValue =
                    fullName.text.toString().trim()

                if (nameValue.isEmpty()) {

                    fullName.error =
                        "Please enter patient's name"

                    fullName.requestFocus()
                    return@setOnClickListener
                }

                if (nameValue.length < 2) {

                    fullName.error =
                        "Please enter a valid name"

                    fullName.requestFocus()
                    return@setOnClickListener
                }

                // ------------------------------------------
                // Age validation
                // ------------------------------------------

                val ageValue =
                    age.text.toString().trim()

                if (ageValue.isEmpty()) {

                    age.error =
                        "Please enter patient's age"

                    age.requestFocus()
                    return@setOnClickListener
                }

                val ageNumber =
                    ageValue.toIntOrNull()

                if (ageNumber == null) {

                    age.error =
                        "Please enter a valid age"

                    age.requestFocus()
                    return@setOnClickListener
                }

                if (ageNumber !in 1..120) {

                    age.error =
                        "Age must be between 1 and 120"

                    age.requestFocus()
                    return@setOnClickListener
                }

                // ------------------------------------------
                // Phone validation
                // ------------------------------------------

                val phoneValue =
                    phone.text.toString().trim()

                if (phoneValue.isEmpty()) {

                    phone.error =
                        "Please enter phone number"

                    phone.requestFocus()
                    return@setOnClickListener
                }

                if (!phoneValue.matches(Regex("^[0-9]{10}$"))) {

                    phone.error =
                        "Please enter a valid 10-digit phone number"

                    phone.requestFocus()
                    return@setOnClickListener
                }

                // ------------------------------------------
                // Sex validation
                // ------------------------------------------

                if (selectedSex == null) {

                    android.widget.Toast.makeText(
                        this,
                        "Please select patient's sex",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                // ------------------------------------------
                // Village validation
                // ------------------------------------------

                val villageValue =
                    village.text.toString().trim()

                if (villageValue.isEmpty()) {

                    village.error =
                        "Please enter village"

                    village.requestFocus()
                    return@setOnClickListener
                }

                // ------------------------------------------
                // Block validation
                // ------------------------------------------

                val blockValue =
                    block.text.toString().trim()

                if (blockValue.isEmpty()) {

                    block.error =
                        "Please enter block"

                    block.requestFocus()
                    return@setOnClickListener
                }

                // ------------------------------------------
                // District validation
                // ------------------------------------------

                val districtValue =
                    district.text.toString().trim()

                if (districtValue.isEmpty()) {

                    district.error =
                        "Please enter district"

                    district.requestFocus()
                    return@setOnClickListener
                }

                // ------------------------------------------
                // Occupation validation
                // ------------------------------------------

                val occupationValue =
                    occupation.text.toString().trim()

                if (occupationValue.isEmpty()) {

                    occupation.error =
                        "Please enter occupation"

                    occupation.requestFocus()
                    return@setOnClickListener
                }

                // ------------------------------------------
                // Consent validation
                // ------------------------------------------

                if (!consentGiven) {

                    android.widget.Toast.makeText(
                        this,
                        "Please record patient consent",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                // ------------------------------------------
                // Everything is valid
                // ------------------------------------------

                val intent =
                    Intent(this, questionaire::class.java)

                startActivity(intent)
            }
    }
}
package eightBugs.osteocare.screening.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import eightBugs.osteocare.R

class questionaire : AppCompatActivity() {

    private var bmiValue: Float? = null
    private var diabetes = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_questionaire)

        // --------------------------------------------------
// BMI (auto-calculated from height/weight)
// --------------------------------------------------
        val heightInput = findViewById<android.widget.EditText>(R.id.et_height_cm)
        val weightInput = findViewById<android.widget.EditText>(R.id.et_weight_kg)
        val bmiText = findViewById<TextView>(R.id.tv_bmi_value)

        val bmiWatcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val heightCm = heightInput.text.toString().toFloatOrNull()
                val weightKg = weightInput.text.toString().toFloatOrNull()

                if (heightCm != null && heightCm > 0 && weightKg != null && weightKg > 0) {
                    val heightM = heightCm / 100f
                    val bmi = weightKg / (heightM * heightM)
                    bmiValue = bmi
                    bmiText.text = String.format("%.1f", bmi)
                } else {
                    bmiValue = null
                    bmiText.text = "--"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        heightInput.addTextChangedListener(bmiWatcher)
        weightInput.addTextChangedListener(bmiWatcher)

        // Back button
        findViewById<android.view.View>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // Pain score
        val painSeekBar = findViewById<SeekBar>(R.id.seekbar_pain_score)
        val painScoreText =
            findViewById<android.widget.TextView>(R.id.tv_pain_score)

        painSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    painScoreText.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )

        // Morning stiffness
        val stiffnessUnder30 =
            findViewById<android.widget.Button>(R.id.btn_stiffness_under_30)

        val stiffness30OrMore =
            findViewById<android.widget.Button>(R.id.btn_stiffness_30_or_more)

        stiffnessUnder30.setOnClickListener {
            stiffnessUnder30.setBackgroundResource(R.drawable.bg_toggle_selected)
            stiffnessUnder30.setTextColor(getColor(R.color.white))

            stiffness30OrMore.setBackgroundResource(R.drawable.bg_toggle_unselected)
            stiffness30OrMore.setTextColor(getColor(R.color.primary_blue))
        }

        stiffness30OrMore.setOnClickListener {
            stiffness30OrMore.setBackgroundResource(R.drawable.bg_toggle_selected)
            stiffness30OrMore.setTextColor(getColor(R.color.white))

            stiffnessUnder30.setBackgroundResource(R.drawable.bg_toggle_unselected)
            stiffnessUnder30.setTextColor(getColor(R.color.primary_blue))
        }
        // Swelling
        val swellingNo =
            findViewById<android.widget.Button>(R.id.btn_swelling_no)

        val swellingYes =
            findViewById<android.widget.Button>(R.id.btn_swelling_yes)

        swellingNo.setOnClickListener {
            swellingNo.setBackgroundResource(R.drawable.bg_toggle_selected)
            swellingNo.setTextColor(getColor(R.color.white))

            swellingYes.setBackgroundResource(R.drawable.bg_toggle_unselected)
            swellingYes.setTextColor(getColor(R.color.primary_blue))
        }

        swellingYes.setOnClickListener {
            swellingYes.setBackgroundResource(R.drawable.bg_toggle_selected)
            swellingYes.setTextColor(getColor(R.color.white))

            swellingNo.setBackgroundResource(R.drawable.bg_toggle_unselected)
            swellingNo.setTextColor(getColor(R.color.primary_blue))
        }
        // Physical activity
        val activityLight =
            findViewById<android.widget.Button>(R.id.btn_activity_light)

        val activityModerate =
            findViewById<android.widget.Button>(R.id.btn_activity_moderate)

        val activityHeavy =
            findViewById<android.widget.Button>(R.id.btn_activity_heavy)

        activityLight.setOnClickListener {
            activityLight.setBackgroundResource(R.drawable.bg_toggle_selected)
            activityLight.setTextColor(getColor(R.color.white))

            activityModerate.setBackgroundResource(R.drawable.bg_toggle_unselected)
            activityModerate.setTextColor(getColor(R.color.primary_blue))

            activityHeavy.setBackgroundResource(R.drawable.bg_toggle_unselected)
            activityHeavy.setTextColor(getColor(R.color.primary_blue))
        }

        activityModerate.setOnClickListener {
            activityModerate.setBackgroundResource(R.drawable.bg_toggle_selected)
            activityModerate.setTextColor(getColor(R.color.white))

            activityLight.setBackgroundResource(R.drawable.bg_toggle_unselected)
            activityLight.setTextColor(getColor(R.color.primary_blue))

            activityHeavy.setBackgroundResource(R.drawable.bg_toggle_unselected)
            activityHeavy.setTextColor(getColor(R.color.primary_blue))
        }

        activityHeavy.setOnClickListener {
            activityHeavy.setBackgroundResource(R.drawable.bg_toggle_selected)
            activityHeavy.setTextColor(getColor(R.color.white))

            activityLight.setBackgroundResource(R.drawable.bg_toggle_unselected)
            activityLight.setTextColor(getColor(R.color.primary_blue))

            activityModerate.setBackgroundResource(R.drawable.bg_toggle_unselected)
            activityModerate.setTextColor(getColor(R.color.primary_blue))
        }
        // Walking difficulty
        val walkingSeekBar =
            findViewById<SeekBar>(R.id.seekbar_walking_difficulty)

        val walkingText =
            findViewById<android.widget.TextView>(R.id.tv_walking_difficulty)

        walkingSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    walkingText.text = progress.toString()
                    updateMobilityScore()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

// Stair difficulty
        val stairSeekBar =
            findViewById<SeekBar>(R.id.seekbar_stair_difficulty)

        val stairText =
            findViewById<android.widget.TextView>(R.id.tv_stair_difficulty)

        stairSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    stairText.text = progress.toString()
                    updateMobilityScore()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

// Squatting difficulty
        val squattingSeekBar =
            findViewById<SeekBar>(R.id.seekbar_squatting_difficulty)

        val squattingText =
            findViewById<android.widget.TextView>(R.id.tv_squatting_difficulty)

        squattingSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    squattingText.text = progress.toString()
                    updateMobilityScore()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )
        // Previous joint injury
        val previousInjuryYes =
            findViewById<android.widget.Button>(R.id.btn_previous_injury_yes)

        val previousInjuryNo =
            findViewById<android.widget.Button>(R.id.btn_previous_injury_no)

        previousInjuryYes.setOnClickListener {
            previousInjuryYes.setBackgroundResource(R.drawable.bg_toggle_selected)
            previousInjuryYes.setTextColor(getColor(R.color.white))

            previousInjuryNo.setBackgroundResource(R.drawable.bg_toggle_unselected)
            previousInjuryNo.setTextColor(getColor(R.color.primary_blue))
        }

        previousInjuryNo.setOnClickListener {
            previousInjuryNo.setBackgroundResource(R.drawable.bg_toggle_selected)
            previousInjuryNo.setTextColor(getColor(R.color.white))

            previousInjuryYes.setBackgroundResource(R.drawable.bg_toggle_unselected)
            previousInjuryYes.setTextColor(getColor(R.color.primary_blue))
        }

        // Family history of joint problems
        val familyHistoryYes =
            findViewById<android.widget.Button>(R.id.btn_family_history_yes)

        val familyHistoryNo =
            findViewById<android.widget.Button>(R.id.btn_family_history_no)

        familyHistoryYes.setOnClickListener {
            familyHistoryYes.setBackgroundResource(R.drawable.bg_toggle_selected)
            familyHistoryYes.setTextColor(getColor(R.color.white))

            familyHistoryNo.setBackgroundResource(R.drawable.bg_toggle_unselected)
            familyHistoryNo.setTextColor(getColor(R.color.primary_blue))
        }

        familyHistoryNo.setOnClickListener {
            familyHistoryNo.setBackgroundResource(R.drawable.bg_toggle_selected)
            familyHistoryNo.setTextColor(getColor(R.color.white))

            familyHistoryYes.setBackgroundResource(R.drawable.bg_toggle_unselected)
            familyHistoryYes.setTextColor(getColor(R.color.primary_blue))
        }
        // --------------------------------------------------
// Diabetes
// --------------------------------------------------
        val diabetesYes = findViewById<Button>(R.id.btn_diabetes_yes)
        val diabetesNo = findViewById<Button>(R.id.btn_diabetes_no)

        diabetesYes.setOnClickListener {
            diabetes = true
            diabetesYes.setBackgroundResource(R.drawable.bg_toggle_selected)
            diabetesYes.setTextColor(getColor(R.color.white))
            diabetesNo.setBackgroundResource(R.drawable.bg_toggle_unselected)
            diabetesNo.setTextColor(getColor(R.color.primary_blue))
        }

        diabetesNo.setOnClickListener {
            diabetes = false
            diabetesNo.setBackgroundResource(R.drawable.bg_toggle_selected)
            diabetesNo.setTextColor(getColor(R.color.white))
            diabetesYes.setBackgroundResource(R.drawable.bg_toggle_unselected)
            diabetesYes.setTextColor(getColor(R.color.primary_blue))
        }

        // Analyse and view result
        findViewById<android.view.View>(R.id.btn_analyse_result)
            .setOnClickListener {
                startActivity(Intent(this, result::class.java))
            }
    }
    private fun updateMobilityScore() {

        val walking =
            findViewById<SeekBar>(R.id.seekbar_walking_difficulty).progress

        val stair =
            findViewById<SeekBar>(R.id.seekbar_stair_difficulty).progress

        val squatting =
            findViewById<SeekBar>(R.id.seekbar_squatting_difficulty).progress

        val mobilityScore =
            (walking + stair + squatting) / 3

        findViewById<android.widget.TextView>(R.id.tv_mobility_score)
            .text = "$mobilityScore/10"
    }
}
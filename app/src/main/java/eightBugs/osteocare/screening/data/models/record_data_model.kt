package eightBugs.osteocare.screening.data.models

import eightBugs.osteocare.R

data class PatientRecord(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val village: String,
    val lastScreeningDate: String,
    val riskLevel: RiskLevel,
    val isHighlighted: Boolean = false
)

enum class RiskLevel(
    val label: String,
    val bgDrawableRes: Int,
    val textColorRes: Int,
    val dotDrawableRes: Int
) {
    MODERATE(
        label = "Moderate risk",
        bgDrawableRes = R.drawable.bg_risk_pill_moderate,
        textColorRes = R.color.risk_moderate_text,
        dotDrawableRes = R.drawable.circle_dot_moderate
    ),
    HIGH(
        label = "High risk",
        bgDrawableRes = R.drawable.bg_risk_pill_high,
        textColorRes = R.color.risk_high_text,
        dotDrawableRes = R.drawable.circle_dot_high
    ),
    LOW(
        label = "Low risk",
        bgDrawableRes = R.drawable.bg_risk_pill_low,
        textColorRes = R.color.risk_low_text,
        dotDrawableRes = R.drawable.circle_dot_low
    ),
    URGENT(
        label = "Urgent risk",
        bgDrawableRes = R.drawable.bg_risk_pill_urgent,
        textColorRes = R.color.risk_urgent_text,
        dotDrawableRes = R.drawable.circle_dot_urgent
    )
}
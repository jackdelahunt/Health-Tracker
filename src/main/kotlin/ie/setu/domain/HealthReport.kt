package ie.setu.domain

data class HealthReport (
    var id: Int,
    val weight: Int,
    val height: Double,
    val BMI: Int,
    val userId: Int
    )
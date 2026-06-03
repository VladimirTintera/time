package eu.tintera.time.format

internal data class Measurable(
    val unit: MeasureUnit,
    val value: Int
)

internal enum class MeasureUnit {
    YEARS, MONTHS, DAYS, HOURS, MINUTES, SECONDS, FRACTIONAL_SECONDS
}
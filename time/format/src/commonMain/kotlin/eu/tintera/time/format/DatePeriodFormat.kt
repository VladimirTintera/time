package eu.tintera.time.format

/**
 * Configuration for localized date period formatting.
 *
 * This class encapsulates a formatting DSL block used to define how [kotlinx.datetime.DatePeriod]
 * values are represented as strings.
 *
 * Example:
 * ```kotlin
 * val format = DatePeriodFormat {
 *     years = UnitVisibility.Required
 *     months = UnitVisibility.Auto
 * }
 * ```
 */
class DatePeriodFormat(
    val block: DatePeriodFormatScope.() -> Unit = DatePeriodFormatScope.defaultConfig
) {
    companion object {
        /**
         * Creates a [DatePeriodFormat] instance with the specified configuration block.
         *
         * @param block The configuration DSL block.
         * @return A new [DatePeriodFormat] instance.
         */
        operator fun invoke(
            block: DatePeriodFormatScope.() -> Unit = DatePeriodFormatScope.defaultConfig
        ): DatePeriodFormat = DatePeriodFormat(block)
    }
}

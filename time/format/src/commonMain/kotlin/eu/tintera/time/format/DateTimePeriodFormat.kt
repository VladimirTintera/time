package eu.tintera.time.format

/**
 * Configuration for localized date-time period formatting.
 *
 * This class encapsulates a formatting DSL block used to define how [kotlinx.datetime.DateTimePeriod]
 * values are represented as strings.
 *
 * Example:
 * ```kotlin
 * val format = DateTimePeriodFormat {
 *     style = FormatStyle.Full
 *     calendar {
 *         years = UnitVisibility.Required
 *         months = UnitVisibility.Auto
 *     }
 *     clock {
 *         hours = UnitVisibility.Required
 *     }
 * }
 * ```
 */
class DateTimePeriodFormat(
    val block: DateTimePeriodFormatScope.() -> Unit = DateTimePeriodFormatScope.defaultConfig
) {
   companion object {
       /**
        * Creates a [DateTimePeriodFormat] instance with the specified configuration block.
        *
        * @param block The configuration DSL block.
        * @return A new [DateTimePeriodFormat] instance.
        */
       operator fun invoke(
           block: DateTimePeriodFormatScope.() -> Unit = DateTimePeriodFormatScope.defaultConfig
       ) = DateTimePeriodFormat(block)
   }
}
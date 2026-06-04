package eu.tintera.time.format

import kotlinx.datetime.LocalDate

/**
 * Encapsulates a formatting configuration for date intervals.
 *
 * Example:
 * ```kotlin
 * val format = DateIntervalFormat {
 *     month = MonthFormat.Name.Short
 *     day = DayFormat.Numeric
 * }
 * ```
 *
 * @param block The configuration block applied to the [DateFormatScope] of [OpenEndRange] of [LocalDate].
 */
class DateIntervalFormat internal constructor(
    block: DateFormatScope<OpenEndRange<LocalDate>>.() -> Unit = DateFormatScope.defaultConfig()
) : BaseDateFormat<OpenEndRange<LocalDate>>(block) {
    companion object {
        /**
         * Creates a [DateIntervalFormat] using the specified configuration block.
         *
         * Example:
         * ```kotlin
         * val format = DateIntervalFormat {
         *     month = MonthFormat.Name.Full
         * }
         * ```
         *
         * @param block The configuration block applied to the [DateFormatScope] of [OpenEndRange] of [LocalDate].
         * @return A new [DateIntervalFormat] instance.
         */
        operator fun invoke(
            block: DateFormatScope<OpenEndRange<LocalDate>>.() -> Unit = DateFormatScope.defaultConfig()
        ) : DateIntervalFormat = DateIntervalFormat(block)
    }
}
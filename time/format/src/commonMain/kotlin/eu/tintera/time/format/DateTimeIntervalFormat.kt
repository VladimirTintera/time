package eu.tintera.time.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime


/**
 * Encapsulates a formatting configuration for date-time intervals.
 *
 * Example:
 * ```kotlin
 * val format = DateTimeIntervalFormat {
 *     date {
 *         month = MonthFormat.Name.Short
 *         day = DayFormat.Numeric
 *     }
 *     time {
 *         short()
 *     }
 * }
 * ```
 *
 * @param block The configuration block applied to the [DateTimeFormatScope] of [OpenEndRange] of [LocalDateTime].
 */
class DateTimeIntervalFormat internal constructor(
    block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit
) : BaseDateTimeFormat<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>(block) {

    companion object {
        /**
         * Creates a [DateTimeIntervalFormat] using the specified configuration block.
         *
         * Example:
         * ```kotlin
         * val format = DateTimeIntervalFormat {
         *     date {
         *         month = MonthFormat.Name.Full
         *     }
         * }
         * ```
         *
         * @param block The configuration block applied to the [DateTimeFormatScope] of [OpenEndRange] of [LocalDateTime].
         * @return A new [DateTimeIntervalFormat] instance.
         */
        operator fun invoke(
            block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = DateTimeFormatScope.defaultConfig()
        ): DateTimeIntervalFormat = DateTimeIntervalFormat(block)
    }
}

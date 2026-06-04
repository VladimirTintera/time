package eu.tintera.time.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/**
 * Configuration for localized date-time formatting.
 *
 * This class encapsulates a formatting DSL block used to define how date-time values
 * are represented as strings.
 *
 * Example:
 * ```kotlin
 * val format = DateTimeFormat {
 *     date { short() }
 *     time { short() }
 * }
 * ```
 */
class DateTimeFormat internal constructor(
    block: DateTimeFormatScope<LocalDateTime, LocalDate, LocalTime>.() -> Unit
) : BaseDateTimeFormat<LocalDateTime, LocalDate, LocalTime>(block) {
    companion object {
        /**
         * Creates a [DateTimeFormat] instance with the specified configuration block.
         *
         * @param block The configuration DSL block.
         * @return A new [DateTimeFormat] instance.
         */
        operator fun invoke(
            block: DateTimeFormatScope<LocalDateTime, LocalDate, LocalTime>.() -> Unit = DateTimeFormatScope.defaultConfig()
        ): DateTimeFormat = DateTimeFormat(block)
    }
}

/**
 * Base configuration class for localized date-time formatting.
 *
 * @param T The type of date-time representation being formatted.
 * @param TDate The type of date representation used within the scope.
 * @param TTime The type of time representation used within the scope.
 * @property block The configuration DSL block.
 */
abstract class BaseDateTimeFormat<T : Any, TDate : Any, TTime : Any> internal constructor(
    val block: DateTimeFormatScope<T, TDate, TTime>.() -> Unit,
)
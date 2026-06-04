package eu.tintera.time.format

import kotlinx.datetime.LocalDate

/**
 * Configuration for localized date formatting.
 *
 * This class encapsulates a formatting DSL block used to define how date values
 * are represented as strings.
 *
 * Example:
 * ```kotlin
 * val format = DateFormat {
 *     day = DayFormat.Padded
 *     month = MonthFormat.Name.Full
 *     year = YearFormat.FourDigits
 * }
 * ```
 */
class DateFormat internal constructor(
    block: DateFormatScope<LocalDate>.() -> Unit
) : BaseDateFormat<LocalDate>(block) {
    companion object {
        /**
         * Creates a [DateFormat] instance with the specified configuration block.
         *
         * @param block The configuration DSL block.
         * @return A new [BaseDateFormat] instance.
         */
        operator fun invoke(
            block: DateFormatScope<LocalDate>.() -> Unit = DateFormatScope.defaultConfig()
        ): BaseDateFormat<LocalDate> = DateFormat(block)
    }
}

/**
 * Base configuration class for localized date formatting.
 *
 * @param T The type of date representation being formatted.
 * @property block The configuration DSL block.
 */
abstract class BaseDateFormat<T : Any> internal constructor(
    val block: DateFormatScope<T>.() -> Unit
)

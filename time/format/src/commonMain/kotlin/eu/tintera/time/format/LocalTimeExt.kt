package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/**
 * Formats this [LocalTime] into a string representation using the specified format.
 *
 * This function internally converts the [LocalTime] to a [LocalDateTime] (using a dummy date)
 * to apply the [TimeFormat].
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(14, 30)
 * val format = TimeFormat {
 *     hour = HourFormat.Digital24h.Padded
 *     minute = MinuteFormat.Padded
 * }
 * val formatted = time.format(format)
 * // formatted will be "14:30" (depending on locale)
 * ```
 *
 * @param format The [TimeFormat] configuration to apply.
 * @param locale An optional [AppLocale] to use for formatting.
 * @return The formatted time string.
 */
fun LocalTime.format(
    format: TimeFormat,
    locale: AppLocale
): String = platformDateTimeFormat(
    date = LocalDateTime(
        year = 1970,
        month = 1,
        day = 1,
        hour = hour,
        minute = minute,
        second = second,
        nanosecond = nanosecond
    ),
    locale = locale,
    timeFormat = format,
    dateFormat = null
)

/**
 * Formats this [LocalTime] into a string representation using a DSL-configured format.
 *
 * This function provides a convenient way to define a [TimeFormat] on-the-fly
 * using a DSL and apply it to the [LocalTime].
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(14, 30)
 * val formatted = time.format {
 *     short()
 * }
 * // formatted will be "2:30 PM" or "14:30" depending on locale
 * ```
 *
 * @param locale An optional [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [TimeFormat].
 * @return The formatted time string.
 */
fun LocalTime.format(
    locale: AppLocale,
    block: TimeFormatBuilder.() -> Unit
) = format(
    locale = locale,
    format = TimeFormat(block = block)
)

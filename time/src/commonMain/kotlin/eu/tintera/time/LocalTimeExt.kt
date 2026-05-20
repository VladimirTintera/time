package eu.tintera.time

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/**
 * Formats this [LocalTime] into a string representation using the specified format.
 *
 * This function internally converts the [LocalTime] to a [LocalDateTime] (using a dummy date)
 * to apply the [TimeFormat].
 *
 * @param format The [TimeFormat] configuration to apply.
 * @return The formatted time string.
 */
fun LocalTime.format(format: TimeFormat): String = formatDateTime(
    date = LocalDateTime(
        year = 1970,
        month = 1,
        day = 1,
        hour = hour,
        minute = minute,
        second = second,
        nanosecond = nanosecond
    ),
    format = DateTimeFormatImpl(dateFormat = null, timeFormat = format)
)

/**
 * Formats this [LocalTime] into a string representation using a DSL-configured format.
 *
 * This function provides a convenient way to define a [TimeFormat] on-the-fly
 * using a DSL and apply it to the [LocalTime].
 *
 * @param block The DSL block for configuring the [TimeFormat].
 * @return The formatted time string.
 */
fun LocalTime.format(
    block: TimeFormatBuilder.() -> Unit
) = format(
    format = time(base = null, block = block)
)

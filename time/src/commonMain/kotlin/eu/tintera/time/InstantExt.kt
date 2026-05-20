package eu.tintera.time

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Formats this [Instant] into a string representation using the specified format and time zone.
 *
 * This extension function simplifies the process of converting an [Instant] to a human-readable
 * string by handling the conversion to a [LocalDateTime] based on the provided time zone.
 *
 * @param timeZone The time zone to use for formatting. Defaults to the system's current default time zone.
 * @param format The [DateTimeFormat] configuration to apply.
 * @return The formatted date-time string.
 */
fun Instant.format(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    format: DateTimeFormat
) = formatDateTime(
    date = toLocalDateTime(timeZone),
    format = format
)

/**
 * Formats this [Instant] into a string representation using a DSL-configured format and the specified time zone.
 *
 * This extension function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the [Instant].
 *
 * @param timeZone The time zone to use for formatting. Defaults to the system's current default time zone.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted date-time string.
 */
fun Instant.format(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    block: DateTimeFormatBuilder.() -> Unit
) = formatDateTime(
    date = toLocalDateTime(timeZone),
    format = dateTimeFormat(base = null, block = block)
)

/**
 * Formats this [Instant] as a relative time string from the current moment.
 *
 * This function provides a human-readable representation of the time difference
 * between this [Instant] and a reference point (by default, the current system time).
 *
 * @param now The reference point for calculating the relative time. Defaults to the current system time.
 * @param style The desired style for the relative time units (e.g., "full" or "short").
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun Instant.formatRelative(
    now: Instant = Clock.System.now(),
    style: RelativeUnitStyle = RelativeUnitStyle.Full
): String = formatRelativeTime(this, now, style)

/**
 * Formats the interval between this [Instant] and another [Instant] as a string.
 *
 * This function is useful for displaying a time range, such as the start and end of an event.
 *
 * @param to The end of the time interval.
 * @param format The [DateTimeFormat] to apply to both the start and end of the interval.
 * @param timeZone The time zone to use for formatting. Defaults to the system's current default time zone.
 * @return The formatted interval string.
 */
fun Instant.formatInterval(
    to: Instant,
    format: DateTimeFormat,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = formatInterval(
    from = toLocalDateTime(timeZone),
    to = to.toLocalDateTime(timeZone),
    format = format
)

/**
 * Formats the interval between this [Instant] and another [Instant] using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the time interval.
 *
 * @param to The end of the time interval.
 * @param timeZone The time zone to use for formatting. Defaults to the system's current default time zone.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted interval string.
 */
fun Instant.formatInterval(
    to: Instant,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    block: DateTimeFormatBuilder.() -> Unit
) = formatInterval(
    from = toLocalDateTime(timeZone),
    to = to.toLocalDateTime(timeZone),
    format = dateTimeFormat(base = null, block = block)
)

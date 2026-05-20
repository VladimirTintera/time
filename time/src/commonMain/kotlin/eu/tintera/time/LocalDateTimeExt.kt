package eu.tintera.time

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

/**
 * Formats this [LocalDateTime] into a string representation using the specified format.
 *
 * @param format The [DateTimeFormat] configuration to apply.
 * @return The formatted date-time string.
 */
fun LocalDateTime.format(format: DateTimeFormat): String {
    return formatDateTime(
        date = this,
        format = format
    )
}

/**
 * Formats this [LocalDateTime] into a string representation using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the [LocalDateTime].
 *
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted date-time string.
 */
fun LocalDateTime.format(block: DateTimeFormatBuilder.() -> Unit) = formatDateTime(
    date = this,
    format = dateTimeFormat(base = null, block = block)
)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime].
 *
 * This function provides a human-readable representation of the time difference
 * between this [LocalDateTime] and a reference point.
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for the conversion to [Instant]. Defaults to the system's current default time zone.
 * @param style The desired style for the relative time units (e.g., "full" or "short").
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    style: RelativeUnitStyle = RelativeUnitStyle.Full
): String = formatRelativeTime(
    target = this.toInstant(timeZone),
    now = now.toInstant(timeZone),
    style = style
)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] as a string.
 *
 * This function is useful for displaying a time range, such as the start and end of an event.
 *
 * @param to The end of the time interval.
 * @param format The [DateTimeFormat] to apply to both the start and end of the interval.
 * @return The formatted interval string.
 */
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    format: DateTimeFormat
) = formatInterval(
    from = this,
    to = to,
    format = format
)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the time interval.
 *
 * @param to The end of the time interval.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted interval string.
 */
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    block: DateTimeFormatBuilder.() -> Unit
) = formatInterval(
    from = this,
    to = to,
    format = dateTimeFormat(base = null, block = block)
)

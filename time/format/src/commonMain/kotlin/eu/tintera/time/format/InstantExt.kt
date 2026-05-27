package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDateTime
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
 * Example:
 * ```kotlin
 * val instant = Instant.parse("2024-01-01T12:00:00Z")
 * val format = DateTimeFormat {
 *     date { long() }
 *     time { short() }
 * }
 * val formatted = instant.format(format, TimeZone.of("America/New_York"))
 * // formatted will be "January 1, 2024 at 7:00 AM" (depending on locale)
 * ```
 *
 * @param timeZone The time zone to use for formatting.
 * @param locale An optional [AppLocale] to use for formatting.
 * * @param format The [DateTimeFormat] configuration to apply.
 * @return The formatted date-time string.
 */
fun Instant.format(
    format: DateTimeFormat,
    timeZone: TimeZone,
    locale: AppLocale,
) = platformDateTimeFormat(
    date = toLocalDateTime(timeZone),
    locale = locale,
    dateFormat = format,
    timeFormat = format
)

/**
 * Formats this [Instant] into a string representation using a DSL-configured format and the specified time zone.
 *
 * This extension function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the [Instant].
 *
 * Example:
 * ```kotlin
 * val instant = Instant.parse("2024-01-01T12:00:00Z")
 * val formatted = instant.format(TimeZone.of("Europe/Prague")) {
 *     date { full() }
 * }
 * // formatted will be "Monday, January 1, 2024" (depending on locale)
 * ```
 *
 * @param timeZone The time zone to use for formatting.
 * @param locale An optional [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted date-time string.
 */
fun Instant.format(
    timeZone: TimeZone,
    locale: AppLocale,
    block: DateTimeFormatBuilder.() -> Unit
) = format(
    locale = locale,
    timeZone = timeZone,
    format = DateTimeFormat(block),
)

/**
 * Formats this [Instant] as a relative time string from the current moment.
 *
 * This function provides a human-readable representation of the time difference
 * between this [Instant] and a reference point (by default, the current system time).
 *
 * Example:
 * ```kotlin
 * val fiveMinutesAgo = Clock.System.now() - 5.minutes
 * val formatted = fiveMinutesAgo.formatRelative(
 *     format = RelativeDateTimeFormat { minutes() }
 * )
 * // formatted will be "5 minutes ago"
 * ```
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for relative calculations.
 * @param format The [RelativeDateTimeFormat] configuration specifying style and thresholds.
 * @param locale An optional [AppLocale] to use for formatting.
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun Instant.formatRelative(
    now: Instant,
    timeZone: TimeZone,
    locale: AppLocale,
    format: RelativeDateTimeFormat,
): String = platformRelativeTimeFormat(
    target = this,
    now = now,
    format = format,
    locale = locale,
    timeZone = timeZone
)

/**
 * Formats this [Instant] as a relative time string from the current moment using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * val fiveMinutesAgo = Clock.System.now() - 5.minutes
 * val formatted = fiveMinutesAgo.formatRelative {
 *     minutes()
 * }
 * // formatted will be "5 minutes ago"
 * ```
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for relative calculations.
 * @param locale An optional [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [RelativeDateTimeFormat].
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun Instant.formatRelative(
    now: Instant,
    timeZone: TimeZone,
    locale: AppLocale,
    block: RealRelativeDateTimeFormatBuilder.() -> Unit
) = formatRelative(
    now = now,
    timeZone = timeZone,
    format = RelativeDateTimeFormat(block),
    locale = locale,
)

/**
 * Formats the interval between this [Instant] and another [Instant] as a string.
 *
 * This function is useful for displaying a time range, such as the start and end of an event.
 *
 * Example:
 * ```kotlin
 * val start = Instant.parse("2024-01-01T10:00:00Z")
 * val end = Instant.parse("2024-01-01T12:30:00Z")
 * val format = DateTimeFormat { time { short() } }
 * val formatted = start.formatInterval(end, format)
 * // formatted will be "10:00 AM – 12:30 PM" (depending on locale)
 * ```
 *
 * @param to The end of the time interval.
 * @param format The [DateTimeFormat] to apply to both the start and end of the interval.
 * @param timeZone The time zone to use for formatting.
 * @param locale An optional [AppLocale] to use for formatting.
 * @return The formatted interval string.
 */
fun Instant.formatInterval(
    to: Instant,
    format: DateTimeFormat,
    timeZone: TimeZone,
    locale: AppLocale
) = formatInterval(
    from = this,
    to = to,
    format = format,
    locale = locale,
    timeZone = timeZone
)

/**
 * Formats the interval between this [Instant] and another [Instant] using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the time interval.
 *
 * Example:
 * ```kotlin
 * val start = Instant.parse("2024-05-20T10:00:00Z")
 * val end = Instant.parse("2024-05-21T12:30:00Z")
 * val formatted = start.formatInterval(end) {
 *     date {
 *         month = MonthFormat.Name.Short
 *         day = DayFormat.Numeric
 *     }
 * }
 * // formatted will be "May 20 – 21" (depending on locale)
 * ```
 *
 * @param to The end of the time interval.
 * @param timeZone The time zone to use for formatting.
 * @param locale An optional [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted interval string.
 */
fun Instant.formatInterval(
    to: Instant,
    timeZone: TimeZone,
    locale: AppLocale,
    block: DateTimeFormatBuilder.() -> Unit
) = formatInterval(
    from = this,
    to = to,
    format = DateTimeFormat(block),
    locale = locale,
    timeZone = timeZone
)
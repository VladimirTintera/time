package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Instant

/**
 * Formats this [LocalDateTime] into a string representation using the specified format.
 *
 * Example:
 * ```kotlin
 * val dateTime = LocalDateTime(2024, 1, 1, 12, 30)
 * val format = DateTimeFormat {
 *     date { short() }
 *     time { short() }
 * }
 * val formatted = dateTime.format(format)
 * // formatted will be "1/1/24, 12:30 PM" (depending on locale)
 * ```
 *
 * @param format The [DateTimeFormat] configuration to apply.
 * @param locale An optional [AppLocale] to use for formatting.
 * @return The formatted date-time string.
 */
fun LocalDateTime.format(
    format: DateTimeFormat,
    locale: AppLocale
): String = platformDateTimeFormat(
    date = this,
    dateFormat = format,
    timeFormat = format,
    locale = locale,
)


/**
 * Formats this [LocalDateTime] into a string representation using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the [LocalDateTime].
 *
 * Example:
 * ```kotlin
 * val dateTime = LocalDateTime(2024, 1, 1, 12, 30)
 * val formatted = dateTime.format {
 *     date { full() }
 *     time { short() }
 * }
 * // formatted will be "Monday, January 1, 2024, 12:30 PM" (depending on locale)
 * ```
 *
 * @param locale An optional [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted date-time string.
 */
fun LocalDateTime.format(
    locale: AppLocale,
    block: DateTimeFormatBuilder.() -> Unit
) = format(
    format = DateTimeFormat(block),
    locale = locale
)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime].
 *
 * This function provides a human-readable representation of the time difference
 * between this [LocalDateTime] and a reference point.
 *
 * Example:
 * ```kotlin
 * val now = LocalDateTime(2024, 1, 1, 12, 0)
 * val past = LocalDateTime(2024, 1, 1, 11, 0)
 * val formatted = past.formatRelative(
 *     now = now,
 *     format = RelativeDateTimeFormat { hours() }
 * )
 * // formatted will be "1 hour ago"
 * ```
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for the conversion to [Instant].
 * @param format The [RelativeDateTimeFormat] configuration specifying style and thresholds.
 * @param locale An optional [AppLocale] to use for formatting.
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    timeZone: TimeZone,
    format: RelativeDateTimeFormat,
    locale: AppLocale
): String = platformRelativeTimeFormat(
    target = this.toInstant(timeZone),
    now = now.toInstant(timeZone),
    timeZone = timeZone,
    format = format,
    locale = locale
)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime] using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * val now = LocalDateTime(2024, 1, 1, 12, 0)
 * val past = LocalDateTime(2024, 1, 1, 11, 0)
 * val formatted = past.formatRelative(now = now) {
 *     hours()
 * }
 * // formatted will be "1 hour ago"
 * ```
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for the conversion to [Instant].
 * @param locale An optional [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [RelativeDateTimeFormat].
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    timeZone: TimeZone,
    locale: AppLocale,
    block: RealRelativeDateTimeFormatBuilder.() -> Unit
): String = platformRelativeTimeFormat(
    target = this.toInstant(timeZone),
    now = now.toInstant(timeZone),
    timeZone = timeZone,
    format = RelativeDateTimeFormat(block),
    locale = locale
)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] as a string.
 *
 * This function is useful for displaying a time range, such as the start and end of an event.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2024, 1, 1, 10, 0)
 * val end = LocalDateTime(2024, 1, 1, 12, 0)
 * val format = DateTimeFormat { time { short() } }
 * val formatted = start.formatInterval(end, format)
 * // formatted will be "10:00 AM – 12:00 PM" (depending on locale)
 * ```
 *
 * @param to The end of the time interval.
 * @param format The [DateTimeFormat] to apply to both the start and end of the interval.
 * @param locale An optional [AppLocale] to use for formatting.
 * @param timeZone The time zone to use.
 * @param onSameDate Custom combiner for events on the same day.
 * @param onSameMonth Custom combiner for events in the same month.
 * @param onSameYear Custom combiner for events in the same year.
 * @param onDifferentDate Custom combiner for multi-day events.
 * @return The formatted interval string.
 */
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    format: DateTimeFormat,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner()
) = formatInterval(
    from = this.toInstant(timeZone),
    to = to.toInstant(timeZone),
    format = format,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the time interval.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2024, 1, 1, 10, 0)
 * val end = LocalDateTime(2024, 1, 1, 12, 0)
 * val formatted = start.formatInterval(end) {
 *     time { short() }
 * }
 * // formatted will be "10:00 AM – 12:00 PM" (depending on locale)
 * ```
 *
 * @param to The end of the time interval.
 * @param locale An optional [AppLocale] to use for formatting.
 * @param timeZone The time zone to use.
 * @param onSameDate Custom combiner for events on the same day.
 * @param onSameMonth Custom combiner for events in the same month.
 * @param onSameYear Custom combiner for events in the same year.
 * @param onDifferentDate Custom combiner for multi-day events.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted interval string.
 */
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
    block: DateTimeFormatBuilder.() -> Unit
) = formatInterval(
    from = this.toInstant(timeZone),
    to = to.toInstant(timeZone),
    format = DateTimeFormat(block),
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
)

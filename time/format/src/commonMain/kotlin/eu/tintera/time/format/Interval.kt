package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.*
import kotlin.time.Instant

/**
 * Represents a specific time interval with context required for formatting.
 *
 * Encapsulates the start instant, end instant, timezone, and locale.
 */
class Interval internal constructor(
    /** The start instant of the interval. */
    val from: Instant,
    /** The end instant of the interval. */
    val to: Instant,
    private val dateFormat: DateFormat,
    private val timeFormat: TimeFormat?,
    /** The time zone in which the interval is formatted. */
    val timeZone: TimeZone,
    /** The locale used for localized text/formatting. */
    val locale: AppLocale
) {
    /**
     * Formats the interval using the default platform skeleton-based interval formatter.
     *
     * @return The formatted interval string.
     */
    fun format() = platformIntervalFormat(
        from = from,
        to = to,
        dateFormat = dateFormat,
        timeFormat = timeFormat,
        locale = locale,
        timeZone = timeZone
    )
}

/**
 * Functional interface for combining the start and end of an interval that occurs on the same day.
 */
fun interface SameDayCombiner {
    /**
     * Combines the start and end times into a single formatted interval string.
     *
     * @param input The raw interval context.
     * @param date The date of the event.
     * @param startTime The start time.
     * @param endTime The end time.
     * @return The combined localized interval string.
     */
    fun combine(input: Interval, date: LocalDate, startTime: LocalTime, endTime: LocalTime): String
}

/**
 * Functional interface for combining the start and end dates of an interval that spans different dates (but maybe same month/year).
 */
fun interface DifferentDateCombiner {
    /**
     * Combines the start and end dates into a single formatted interval string.
     *
     * @param input The raw interval context.
     * @param start The start date.
     * @param end The end date.
     * @return The combined localized interval string.
     */
    fun combine(input: Interval, start: LocalDate, end: LocalDate): String
}

/**
 * Functional interface for combining the start and end date-times of an interval spanning different days with specific times.
 */
fun interface DifferentDateTimeCombiner {
    /**
     * Combines the start and end date-times into a single formatted interval string.
     *
     * @param input The raw interval context.
     * @param start The start date-time.
     * @param end The end date-time.
     * @return The combined localized interval string.
     */
    fun combine(input: Interval, start: LocalDateTime, end: LocalDateTime): String
}

/**
 * Returns the default [SameDayCombiner] that delegates formatting directly to the platform interval formatter.
 */
fun defaultSameDayCombiner() = SameDayCombiner { interval, _, _, _ ->
    interval.format()
}

/**
 * Returns the default [DifferentDateCombiner] that delegates formatting directly to the platform interval formatter.
 */
fun defaultDifferentDateCombiner() = DifferentDateCombiner { interval, _, _ ->
    interval.format()
}

/**
 * Returns the default [DifferentDateTimeCombiner] that delegates formatting directly to the platform interval formatter.
 */
fun defaultDifferentDateTimeCombiner() = DifferentDateTimeCombiner { interval, _, _ ->
    interval.format()
}

internal fun formatInterval(
    from: Instant,
    to: Instant,
    dateFormat: DateFormat,
    timeFormat: TimeFormat?,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner,
    onSameMonth: DifferentDateCombiner,
    onSameYear: DifferentDateCombiner,
    onDifferentDate: DifferentDateTimeCombiner,
): String {

    val startInstant = if (from <= to) from else to
    val endInstant = if (from <= to) to else from

    val input = Interval(
        from = startInstant,
        to = endInstant,
        locale = locale,
        timeZone = timeZone,
        dateFormat = dateFormat,
        timeFormat = timeFormat
    )

    val start = startInstant.toLocalDateTime(timeZone)
    val end = endInstant.toLocalDateTime(timeZone)

    if (start == end) return platformDateTimeFormat(
        date = start,
        dateFormat = dateFormat,
        timeFormat = timeFormat,
        locale = locale,
    )

    val emptyTime = LocalTime(0, 0)

    val isAllDay = (start.time == emptyTime && end.time == emptyTime) || (timeFormat == null || timeFormat.isEmpty())

    return when {
        // Scénář A: Úplně stejný den. Tady čas nevadí, naopak – combiner dostává startTime a endTime,
        // takže si s tím poradí, i když to není celodenní (např. 12.5. v 10:00 - 12:00)
        start.date == end.date -> {
            onSameDate.combine(input, start.date, start.time, end.time)
        }

        !isAllDay -> {
            onDifferentDate.combine(input, start, end)
        }

        // Scénář B: Stejný měsíc a rok, ale POZOR – pouze pokud je to celodenní!
        // Pokud se časy liší od půlnoci, padá to do 'else' (plnotučné datumočasy)
        start.year == end.year && start.month == end.month -> {
            onSameMonth.combine(input, start.date, end.date)
        }

        // Scénář C: Stejný rok, ale opět pouze pro celodenní události
        start.year == end.year -> {
            onSameYear.combine(input, start.date, end.date)
        }

        // Scénář D: Různé roky, NEBO události přes více dní, které v sobě mají konkrétní časy
        else -> {
            onDifferentDate.combine(input, start, end)
        }
    }
}

/**
 * Formats a time interval between two instants into a localized string using combiners for different date/time scenarios.
 *
 * Example:
 * ```kotlin
 * val start = Instant.parse("2024-01-01T10:00:00Z")
 * val end = Instant.parse("2024-01-01T12:00:00Z")
 * val formatted = formatInterval(start, end, DateTimeFormat { time { short() } })
 * // e.g. "10:00 AM – 12:00 PM"
 * ```
 *
 * @param from The start instant of the interval.
 * @param to The end instant of the interval.
 * @param format The [DateTimeFormat] configuration.
 * @param locale The [AppLocale] to use.
 * @param timeZone The time zone to use.
 * @param onSameDate Custom combiner for events on the same day.
 * @param onSameMonth Custom combiner for events in the same month (all-day only).
 * @param onSameYear Custom combiner for events in the same year (all-day only).
 * @param onDifferentDate Custom combiner for multi-day events or when times are specified.
 * @return The formatted interval string.
 */
fun formatInterval(
    from: Instant,
    to: Instant,
    format: DateTimeFormat,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
): String = formatInterval(
    from = from,
    to = to,
    dateFormat = format,
    timeFormat = format,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate
)

internal fun platformIntervalFormat(
    from: Instant,
    to: Instant,
    dateFormat: DateFormat,
    timeFormat: TimeFormat?,
    locale: AppLocale,
    timeZone: TimeZone
): String {

    return nativeIntervalFormat(
        from = from,
        to = to,
        dateFormat = dateFormat,
        timeFormat = timeFormat,
        locale = locale,
        timeZone = timeZone,
        skeleton = cldrSkeleton(dateFormat, timeFormat)
    )
}

internal expect fun nativeIntervalFormat(
    from: Instant,
    to: Instant,
    dateFormat: DateFormat,
    timeFormat: TimeFormat?,
    skeleton: String,
    locale: AppLocale,
    timeZone: TimeZone
): String

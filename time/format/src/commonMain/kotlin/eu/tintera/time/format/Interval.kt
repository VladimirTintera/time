package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.locale.languageTag
import kotlinx.datetime.*
import kotlin.time.Instant

/**
 * Represents a specific time interval with context required for formatting.
 *
 * Encapsulates the start instant, end instant, timezone, and locale.
 *
 * Example:
 * ```kotlin
 * // Assuming we have an Interval instance (e.g. inside a combiner):
 * val startInstant = interval.from
 * val endInstant = interval.to
 * val zone = interval.timeZone
 * ```
 */
class Interval internal constructor(
    /** The start instant of the interval. */
    val from: Instant,
    /** The end instant of the interval. */
    val to: Instant,
    private val format: DateTimeIntervalFormat,
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
        format = format,
        locale = locale,
        timeZone = timeZone
    )
}

/**
 * Functional interface for combining the start and end of an interval that occurs on the same day.
 *
 * Example:
 * ```kotlin
 * val combiner = SameDayCombiner { interval, date, startTime, endTime ->
 *     "On $date from $startTime to $endTime"
 * }
 * ```
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
 *
 * Example:
 * ```kotlin
 * val combiner = DifferentDateCombiner { interval, start, end ->
 *     "From $start to $end"
 * }
 * ```
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
 *
 * Example:
 * ```kotlin
 * val combiner = DifferentDateTimeCombiner { interval, start, end ->
 *     "From $start to $end"
 * }
 * ```
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
 *
 * Example:
 * ```kotlin
 * val combiner = defaultSameDayCombiner()
 * ```
 */
fun defaultSameDayCombiner() = SameDayCombiner { interval, _, _, _ ->
    interval.format()
}

/**
 * Returns the default [DifferentDateCombiner] that delegates formatting directly to the platform interval formatter.
 *
 * Example:
 * ```kotlin
 * val combiner = defaultDifferentDateCombiner()
 * ```
 */
fun defaultDifferentDateCombiner() = DifferentDateCombiner { interval, _, _ ->
    interval.format()
}

/**
 * Returns the default [DifferentDateTimeCombiner] that delegates formatting directly to the platform interval formatter.
 *
 * Example:
 * ```kotlin
 * val combiner = defaultDifferentDateTimeCombiner()
 * ```
 */
fun defaultDifferentDateTimeCombiner() = DifferentDateTimeCombiner { interval, _, _ ->
    interval.format()
}

fun formatInterval(
    from: Instant,
    to: Instant,
    format: DateTimeIntervalFormat,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
): String {

    val startInstant = if (from <= to) from else to
    val endInstant = if (from <= to) to else from

    val input = Interval(
        from = startInstant,
        to = endInstant,
        locale = locale,
        timeZone = timeZone,
        format = format
    )

    val start = startInstant.toLocalDateTime(timeZone)
    val end = endInstant.toLocalDateTime(timeZone)

    /*if (start == end) return platformDateTimeFormat(
        date = start,
        format = format,
        locale = locale,
        dateRequired = false,
        timeRequired = false,
    )*/

    val emptyTime = LocalTime(0, 0)

    val isAllDay =
        (start.time == emptyTime && end.time == emptyTime) //|| (timeFormat == null /*|| timeFormat.isEmpty()*/)

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

internal data class IntervalCacheKey(
    val skeleton: String,
    val languageTag: String,
    val timeZoneId: String
)

internal expect val intervalFormatterCache: Cache<IntervalCacheKey, IntervalFormatter>

internal fun platformIntervalFormat(
    from: Instant,
    to: Instant,
    format: DateTimeIntervalFormat,
    locale: AppLocale,
    timeZone: TimeZone
): String {

    val start = (if (from < to) from else to).toLocalDateTime(timeZone)
    val end = (if (to > from) to else from).toLocalDateTime(timeZone)

    val scope = DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>(
        value = start..<end,
        date = start.date..<end.date,
        time = start.time..<end.time,
        locale = locale,
    )

    format.block(scope)

    val skeleton = scope.cldrSkeleton()

    val formatter = intervalFormatterCache.getOrPut(
        IntervalCacheKey(
            skeleton = skeleton,
            languageTag = locale.languageTag,
            timeZoneId = timeZone.id
        )
    ) {
        createIntervalFormatterFactory(
            locale = locale,
            timeZone = timeZone
        ).formatter(skeleton, scope.dateFormatScope, scope.timeFormatScope)
    }

    return formatter.format(from, to)
}
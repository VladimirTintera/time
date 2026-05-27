package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.*
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

/**
 * Formats a time interval between two instants into a localized string using combiners for different date/time scenarios.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val start = Instant.fromEpochMilliseconds(1704067200000) // 12:00
 * val end = Instant.fromEpochMilliseconds(1704074400000) // 14:00
 * val format = DateTimeFormat { time { short() } }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     formatInterval(from = start, to = end, format = format)
 * }
 * ```
 *
 * @param from The start instant of the interval.
 * @param to The end instant of the interval.
 * @param format The format to use for formatting individual date-times.
 * @param onSameDate The combiner logic when both instants fall on the same date.
 * @param onSameMonth The combiner logic when both instants fall in the same month of the same year.
 * @param onSameYear The combiner logic when both instants fall in the same year.
 * @param onDifferentDate The combiner logic when instants fall on different dates.
 * @return The formatted localized interval string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun formatInterval(
    from: Instant,
    to: Instant,
    format: DateTimeFormat,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
): String = formatInterval(
    from = from,
    to = to,
    format = format,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate
)


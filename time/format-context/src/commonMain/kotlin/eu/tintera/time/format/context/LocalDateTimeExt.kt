package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

/**
 * Formats this [LocalDateTime] into a string representation using the specified format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val dateTime = LocalDateTime(2024, 1, 1, 12, 30)
 * val format = DateTimeFormat {
 *     date { short() }
 *     time { short() }
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     dateTime.format(format)
 * }
 * ```
 *
 * @param format The format to use for string conversion.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun LocalDateTime.format(
    format: DateTimeFormat
): String = format(format, locale)

/**
 * Formats this [LocalDateTime] into a string representation using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val dateTime = LocalDateTime(2024, 1, 1, 12, 30)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     dateTime.format {
 *         date { short() }
 *         time { short() }
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the date-time format.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun LocalDateTime.format(
    block: DateTimeFormatBuilder.() -> Unit
): String = format(locale, block)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime].
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val now = LocalDateTime(2024, 1, 1, 12, 0)
 * val past = LocalDateTime(2024, 1, 1, 11, 0)
 * val format = RelativeDateTimeFormat { hours() }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     past.formatRelative(now, format)
 * }
 * ```
 *
 * @param now The reference point in time (the "now" date-time).
 * @param format The format configuration for relative formatting.
 * @return The formatted relative time string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    format: RelativeDateTimeFormat
): String = formatRelative(now, timeZone, format, locale)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime] using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val now = LocalDateTime(2024, 1, 1, 12, 0)
 * val past = LocalDateTime(2024, 1, 1, 11, 0)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     past.formatRelative(now) { hours() }
 * }
 * ```
 *
 * @param now The reference point in time (the "now" date-time).
 * @param block The builder block to configure the relative format.
 * @return The formatted relative time string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    block: RealRelativeDateTimeFormatBuilder.() -> Unit
): String = formatRelative(now, timeZone, locale, block)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] as a string.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2024, 1, 1, 10, 0)
 * val end = LocalDateTime(2024, 1, 1, 12, 0)
 * val format = DateTimeFormat { time { short() } }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     start.formatInterval(end, format)
 * }
 * ```
 *
 * @param to The end point of the interval.
 * @param format The format to use for formatting the start and end date-times.
 * @param onSameDate The combiner logic when both date-times fall on the same date.
 * @param onSameMonth The combiner logic when both date-times fall in the same month of the same year.
 * @param onSameYear The combiner logic when both date-times fall in the same year.
 * @param onDifferentDate The combiner logic when date-times fall on different dates.
 * @return The formatted localized interval string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    format: DateTimeFormat,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner()
): String = formatInterval(
    to = to,
    format = format,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate
)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2024, 1, 1, 10, 0)
 * val end = LocalDateTime(2024, 1, 1, 12, 0)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     start.formatInterval(end) {
 *         time { short() }
 *     }
 * }
 * ```
 *
 * @param to The end point of the interval.
 * @param onSameDate The combiner logic when both date-times fall on the same date.
 * @param onSameMonth The combiner logic when both date-times fall in the same month of the same year.
 * @param onSameYear The combiner logic when both date-times fall in the same year.
 * @param onDifferentDate The combiner logic when date-times fall on different dates.
 * @param block The builder block to configure the date-time format.
 * @return The formatted localized interval string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
    block: DateTimeFormatBuilder.() -> Unit
): String = formatInterval(
    to = to,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
    block = block
)


package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

/**
 * Formats this [Instant] into a string representation using the specified format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val instant = Instant.fromEpochMilliseconds(1704067200000) // 2024-01-01T00:00:00Z
 * val format = DateTimeFormat {
 *     date { short() }
 *     time { short() }
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     instant.format(format)
 * }
 * ```
 *
 * @param format The format to use for string conversion.
 * @return The formatted localized string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun Instant.format(
    format: DateTimeFormat
): String = format(format, timeZone, locale)

/**
 * Formats this [Instant] into a string representation using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val instant = Instant.fromEpochMilliseconds(1704067200000)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     instant.format {
 *         date { short() }
 *         time { short() }
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the date-time format.
 * @return The formatted localized string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun Instant.format(
    block: DateTimeFormatScope<LocalDateTime, LocalDate, LocalTime>.() -> Unit = DateTimeFormatScope.defaultConfig()
): String = format(timeZone, locale, block)

/**
 * Formats this [Instant] as a relative time string from another reference [Instant].
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val target = Instant.fromEpochMilliseconds(1704067200000) // 12:00
 * val now = Instant.fromEpochMilliseconds(1704063600000) // 11:00
 * val format = RelativeDateTimeFormat { hours() }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     target.formatRelative(now, format)
 * }
 * ```
 *
 * @param now The reference point in time (the "now" instant).
 * @param format The format configuration for relative formatting.
 * @return The formatted relative time string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun Instant.formatRelative(
    now: Instant,
    format: RelativeDateTimeFormat
): String = formatRelative(now, timeZone, locale, format)

/**
 * Formats this [Instant] as a relative time string from another reference [Instant] using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val target = Instant.fromEpochMilliseconds(1704067200000)
 * val now = Instant.fromEpochMilliseconds(1704063600000)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     target.formatRelative(now) { hours() }
 * }
 * ```
 *
 * @param now The reference point in time (the "now" instant).
 * @param block The builder block to configure the relative format.
 * @return The formatted relative time string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun Instant.formatRelative(
    now: Instant,
    block: RelativeDateTimeFormatScope.() -> Unit = RelativeDateTimeFormatScope.defaultConfig
): String = formatRelative(now, timeZone, locale, block)

/**
 * Formats the interval between this [Instant] and another [Instant] as a string.
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
 *     start.formatInterval(end, format)
 * }
 * ```
 *
 * @param to The end point of the interval.
 * @param format The format to use for formatting the start and end date-times.
 * @return The formatted interval string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun Instant.formatInterval(
    to: Instant,
    format: DateTimeIntervalFormat
): String = formatInterval(to, format, timeZone, locale)

/**
 * Formats the interval between this [Instant] and another [Instant] using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val start = Instant.fromEpochMilliseconds(1704067200000)
 * val end = Instant.fromEpochMilliseconds(1704074400000)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     start.formatInterval(end) { time { short() } }
 * }
 * ```
 *
 * @param to The end point of the interval.
 * @param block The builder block to configure the interval format.
 * @return The formatted interval string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun Instant.formatInterval(
    to: Instant,
    block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = DateTimeFormatScope.defaultConfig()
): String = formatInterval(to, timeZone, locale, block)

context(locale: AppLocale, timeZone: TimeZone)
fun OpenEndRange<Instant>.format(
    format: DateTimeIntervalFormat,
) = formatInterval(
    from = this.start,
    to = this.endExclusive,
    format = format,
    timeZone = timeZone,
    locale = locale,
)

context(locale: AppLocale, timeZone: TimeZone)
fun OpenEndRange<Instant>.format(
    block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = DateTimeFormatScope.defaultConfig()
) = formatInterval(
    from = this.start,
    to = this.endExclusive,
    format = DateTimeIntervalFormat(block),
    timeZone = timeZone,
    locale = locale,
)


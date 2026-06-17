package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.TimeFormat
import eu.tintera.time.format.TimeFormatScope
import eu.tintera.time.format.format
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

/**
 * Formats this [LocalTime] into a string representation using the specified format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(14, 30)
 * val format = TimeFormat {
 *     hour = HourFormat.Digital24h.Padded
 *     minute = MinuteFormat.Padded
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     time.format(format)
 * }
 * ```
 *
 * @param format The format to use for string conversion.
 * @return The formatted localized string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun LocalTime.format(
    format: TimeFormat
): String = format(format, locale, timeZone)

/**
 * Formats this [LocalTime] into a string representation using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(14, 30)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     time.format {
 *         short()
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the time format.
 * @return The formatted localized string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun LocalTime.format(
    block: context(AppLocale, TimeZone) TimeFormatScope<LocalTime>.() -> Unit = { TimeFormatScope.defaultConfig<LocalTime>().invoke(this) }
): String = format(locale, timeZone) {
    block()
}


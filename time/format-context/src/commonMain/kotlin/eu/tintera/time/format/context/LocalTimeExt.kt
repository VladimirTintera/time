package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.TimeFormat
import eu.tintera.time.format.TimeFormatBuilder
import eu.tintera.time.format.format
import kotlinx.datetime.LocalTime

/**
 * Formats this [LocalTime] into a string representation using the specified format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(14, 30)
 * val format = TimeFormat {
 *     hour = HourFormat.Digital24h.Padded
 *     minute = MinuteFormat.Padded
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     time.format(format)
 * }
 * ```
 *
 * @param format The format to use for string conversion.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun LocalTime.format(
    format: TimeFormat
): String = format(format, locale)

/**
 * Formats this [LocalTime] into a string representation using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(14, 30)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     time.format {
 *         short()
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the time format.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun LocalTime.format(
    block: TimeFormatBuilder.() -> Unit
): String = format(locale, block)


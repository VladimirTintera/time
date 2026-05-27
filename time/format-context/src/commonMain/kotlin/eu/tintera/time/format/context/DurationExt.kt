package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.*
import kotlin.time.Duration

/**
 * Formats this [Duration] into a localized text representation.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 * import kotlin.time.Duration.Companion.minutes
 *
 * val duration = 2.hours + 30.minutes
 * val format = DurationFormat {
 *     hours = UnitVisibility.Required
 *     minutes = UnitVisibility.Required
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     duration.format(format)
 * }
 * ```
 *
 * @param format The format to use for string conversion.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun Duration.format(
    format: DurationFormat
): String = format(format, locale)

/**
 * Formats this [Duration] into a localized text representation using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 *
 * val duration = 2.hours
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     duration.format {
 *         hours = UnitVisibility.Required
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the duration format.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun Duration.format(
    block: DurationFormatBuilder.() -> Unit
): String = format(locale, block)

/**
 * Formats this [Duration] into a digital clock-style string representation.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 * import kotlin.time.Duration.Companion.minutes
 *
 * val duration = 1.hours + 30.minutes
 * val format = DurationDigitalFormat {
 *     stopwatch()
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     duration.formatDigital(format)
 * }
 * ```
 *
 * @param format The digital clock format to use.
 * @return The formatted digital clock-style string.
 */
context(locale: AppLocale)
fun Duration.formatDigital(
    format: DurationDigitalFormat
): String = formatDigital(format, locale)

/**
 * Formats this [Duration] into a digital clock-style string representation using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 * import kotlin.time.Duration.Companion.minutes
 *
 * val duration = 1.hours + 30.minutes
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     duration.formatDigital {
 *         stopwatch()
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the digital clock format.
 * @return The formatted digital clock-style string.
 */
context(locale: AppLocale)
fun Duration.formatDigital(
    block: DurationDigitalFormatBuilder.() -> Unit
): String = formatDigital(locale, block)


package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.DateTimePeriodFormat
import eu.tintera.time.format.DateTimePeriodFormatScope
import eu.tintera.time.format.format
import kotlinx.datetime.DateTimePeriod

/**
 * Formats this [DateTimePeriod] into a localized string representation.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(years = 1, hours = 4)
 * val format = DateTimePeriodFormat {
 *     calendar { years = UnitVisibility.Always }
 *     clock { hours = UnitVisibility.Always }
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     period.format(format)
 * }
 * ```
 *
 * @param format The format to use for string conversion.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun DateTimePeriod.format(
    format: DateTimePeriodFormat
): String = format(format, locale)

/**
 * Formats this [DateTimePeriod] into a localized string using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(years = 1, hours = 4)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     period.format {
 *         calendar { years = UnitVisibility.Always }
 *         clock { hours = UnitVisibility.Always }
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the date-time period format.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun DateTimePeriod.format(
    block: context(AppLocale) DateTimePeriodFormatScope.() -> Unit = { DateTimePeriodFormatScope.defaultConfig.invoke(this) }
): String = format(locale) {
    block()
}


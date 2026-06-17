package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.DatePeriodFormat
import eu.tintera.time.format.DatePeriodFormatScope
import eu.tintera.time.format.format
import eu.tintera.time.format.formatCalendar
import kotlinx.datetime.DatePeriod

/**
 * Formats this [DatePeriod] into a localized string representation.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val period = DatePeriod(years = 1)
 * val format = DatePeriodFormat {
 *     years = UnitVisibility.Always
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
fun DatePeriod.format(
    format: DatePeriodFormat
): String = format(format, locale)

/**
 * Formats this [DatePeriod] into a localized string using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val period = DatePeriod(years = 1)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     period.formatCalendar {
 *         years = UnitVisibility.Always
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the date period format.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun DatePeriod.formatCalendar(
    block: context(AppLocale) DatePeriodFormatScope.() -> Unit = { DatePeriodFormatScope.defaultConfig.invoke(this) }
): String = formatCalendar(locale) {
    block()
}

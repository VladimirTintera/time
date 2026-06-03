package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.MonthFormat
import eu.tintera.time.format.WeekDayFormat
import eu.tintera.time.format.formatName
import eu.tintera.time.format.getDecimalSeparator
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

/**
 * Formats the name of this [Month] into a localized string.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val month = Month.JANUARY
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     month.formatName()
 * }
 * ```
 *
 * @param format The style of month name to format. Defaults to [MonthFormat.Name.Full].
 * @return The localized month name string.
 */
context(locale: AppLocale)
fun Month.formatName(
    format: MonthFormat.Name = MonthFormat.Name.Full
): String = formatName(locale, format)

/**
 * Formats the name of this [DayOfWeek] into a localized string.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val day = DayOfWeek.MONDAY
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     day.formatName()
 * }
 * ```
 *
 * @param format The style of weekday name to format. Defaults to [WeekDayFormat.FullName].
 * @return The localized weekday name string.
 */
context(locale: AppLocale)
fun DayOfWeek.formatName(
    format: WeekDayFormat = WeekDayFormat.FullName
): String = formatName(locale, format)

/**
 * Returns the localized decimal separator symbol for the active locale.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve the decimal separator.
 *
 * Example:
 * ```kotlin
 * val myLocale = localeForLanguageTag("en-US")
 * val separator = with(myLocale) {
 *     getDecimalSeparator()
 * }
 * ```
 *
 * @return The decimal separator string (e.g., "." or ",").
 */
context(locale: AppLocale)
fun getDecimalSeparator(): String = getDecimalSeparator(locale)


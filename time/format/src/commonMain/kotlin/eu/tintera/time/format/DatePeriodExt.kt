package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod

/**
 * Formats this [DatePeriod] into a localized string representation.
 *
 * Example:
 * ```kotlin
 * val period = DatePeriod(years = 1, months = 2, days = 3)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = period.format(
 *     format = DatePeriodFormat {
 *         years = UnitVisibility.Auto
 *         months = UnitVisibility.Required
 *     },
 *     locale = myLocale
 * )
 * // e.g. "1 year, 2 months"
 * ```
 *
 * @param format The [DatePeriodFormat] configuration specifying style and unit visibility.
 * @param locale The [AppLocale] to use.
 * @return The formatted period string.
 */
fun DatePeriod.format(
    format: DatePeriodFormat,
    locale: AppLocale
): String = platformPeriodFormat(
    period = DateTimePeriod(
        years = this.years,
        months = this.months,
        days = this.days,
        hours = 0,
        minutes = 0,
        seconds = 0,
        nanoseconds = 0
    ),
    calendar = format,
    clock = null,
    style = format.style,
    locale = locale,
    maxUnitsCount = format.maxUnitsCount
)

/**
 * Formats this [DatePeriod] into a localized string using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * val period = DatePeriod(years = 1, months = 0, days = 5)
 * val czLocale = localeForLanguageTag("cs-CZ")
 * val formatted = period.formatCalendar(locale = czLocale) {
 *     years = UnitVisibility.Auto
 *     days = UnitVisibility.Required
 * }
 * // e.g., "1 year, 5 days"
 * ```
 *
 * @param locale The [AppLocale] to use.
 * @param block The configuration block applied to the [DatePeriodFormatBuilder].
 * @return The formatted period string.
 */
fun DatePeriod.formatCalendar(
    locale: AppLocale,
    block: DatePeriodFormatBuilder.() -> Unit
) = format(
    locale = locale,
    format = DatePeriodFormat(block)
)
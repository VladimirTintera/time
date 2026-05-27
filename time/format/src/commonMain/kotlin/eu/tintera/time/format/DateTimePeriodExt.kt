package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.DateTimePeriod

/**
 * Formats this [DateTimePeriod] into a localized string representation.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(years = 1, months = 2, hours = 4)
 * val formatted = period.format(DateTimePeriodFormat {
 *     calendar { years = UnitVisibility.Auto }
 *     clock { hours = UnitVisibility.Required }
 * })
 * // e.g. "1 year, 4 hours"
 * ```
 *
 * @param format The [DateTimePeriodFormat] configuration specifying style and component visibility.
 * @param locale The [AppLocale] to use.
 * @return The formatted period string.
 */
fun DateTimePeriod.format(
    format: DateTimePeriodFormat,
    locale: AppLocale
) = platformPeriodFormat(
    period = this,
    clock = format,
    calendar = format,
    locale = locale,
    style = format.style,
    maxUnitsCount = format.maxUnitsCount
)

/**
 * Formats this [DateTimePeriod] into a localized string using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(months = 2, hours = 4)
 * val formatted = period.format {
 *     calendar { months = UnitVisibility.Auto }
 *     clock { hours = UnitVisibility.Required }
 * }
 * // e.g., "2 months, 4 hours"
 * ```
 *
 * @param locale The [AppLocale] to use.
 * @param block The configuration block applied to the [DateTimePeriodFormatBuilder].
 * @return The formatted period string.
 */
fun DateTimePeriod.format(
    locale: AppLocale,
    block: DateTimePeriodFormatBuilder.() -> Unit
) = format(
    format = DateTimePeriodFormat(block),
    locale = locale,
)
package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.format.DatePeriodFormat
import eu.tintera.time.format.DatePeriodFormatBuilder
import eu.tintera.time.format.format
import eu.tintera.time.format.formatCalendar
import kotlinx.datetime.DatePeriod

/**
 * Formats this [DatePeriod] into a localized string representation,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun DatePeriod.format(
    format: DatePeriodFormat
): String = format(format, locale.locale)

/**
 * Formats this [DatePeriod] into a localized string using a DSL-configured format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun DatePeriod.formatCalendar(
    block: DatePeriodFormatBuilder.() -> Unit
): String = formatCalendar(locale.locale, block)

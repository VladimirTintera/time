package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.format.DateTimePeriodFormat
import eu.tintera.time.format.DateTimePeriodFormatBuilder
import eu.tintera.time.format.format
import kotlinx.datetime.DateTimePeriod

/**
 * Formats this [DateTimePeriod] into a localized string representation,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun DateTimePeriod.format(
    format: DateTimePeriodFormat
): String = format(format, locale.locale)

/**
 * Formats this [DateTimePeriod] into a localized string using a DSL-configured format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun DateTimePeriod.format(
    block: DateTimePeriodFormatBuilder.() -> Unit
): String = format(locale.locale, block)

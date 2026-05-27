package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.format.TimeFormat
import eu.tintera.time.format.TimeFormatBuilder
import eu.tintera.time.format.format
import kotlinx.datetime.LocalTime

/**
 * Formats this [LocalTime] into a string representation using the specified format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun LocalTime.format(
    format: TimeFormat
): String = format(format, locale.locale)

/**
 * Formats this [LocalTime] into a string representation using a DSL-configured format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun LocalTime.format(
    block: TimeFormatBuilder.() -> Unit
): String = format(locale.locale, block)

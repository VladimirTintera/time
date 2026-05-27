package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.format.*
import kotlin.time.Duration

/**
 * Formats this [Duration] into a localized text representation,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun Duration.format(
    format: DurationFormat
): String = format(format, locale.locale)

/**
 * Formats this [Duration] into a localized text representation using a DSL-configured format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun Duration.format(
    block: DurationFormatBuilder.() -> Unit
): String = format(locale.locale, block)

/**
 * Formats this [Duration] into a digital clock-style string representation,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun Duration.formatDigital(
    format: DurationDigitalFormat
): String = formatDigital(format, locale.locale)

/**
 * Formats this [Duration] into a digital clock-style string representation using a DSL-configured format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun Duration.formatDigital(
    block: DurationDigitalFormatBuilder.() -> Unit
): String = formatDigital(locale.locale, block)

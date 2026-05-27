package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.core.context.TimeZoneContext
import eu.tintera.time.format.*
import kotlinx.datetime.Instant

/**
 * Formats this [Instant] into a string representation using the specified format,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun Instant.format(
    format: DateTimeFormat
): String = format(format, zone.timeZone, locale.locale)

/**
 * Formats this [Instant] into a string representation using a DSL-configured format,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun Instant.format(
    block: DateTimeFormatBuilder.() -> Unit
): String = format(zone.timeZone, locale.locale, block)

/**
 * Formats this [Instant] as a relative time string from another [Instant],
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun Instant.formatRelative(
    now: Instant,
    format: RelativeDateTimeFormat
): String = formatRelative(now, zone.timeZone, locale.locale, format)

/**
 * Formats this [Instant] as a relative time string from another [Instant] using a DSL-configured format,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun Instant.formatRelative(
    now: Instant,
    block: RealRelativeDateTimeFormatBuilder.() -> Unit
): String = formatRelative(now, zone.timeZone, locale.locale, block)

/**
 * Formats the interval between this [Instant] and another [Instant] as a string,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun Instant.formatInterval(
    to: Instant,
    format: DateTimeFormat
): String = formatInterval(to, format, zone.timeZone, locale.locale)

/**
 * Formats the interval between this [Instant] and another [Instant] using a DSL-configured format,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun Instant.formatInterval(
    to: Instant,
    block: DateTimeFormatBuilder.() -> Unit
): String = formatInterval(to, zone.timeZone, locale.locale, block)

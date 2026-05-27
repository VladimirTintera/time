package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.core.context.TimeZoneContext
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDateTime

/**
 * Formats this [LocalDateTime] into a string representation using the specified format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun LocalDateTime.format(
    format: DateTimeFormat
): String = format(format, locale.locale)

/**
 * Formats this [LocalDateTime] into a string representation using a DSL-configured format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun LocalDateTime.format(
    block: DateTimeFormatBuilder.() -> Unit
): String = format(locale.locale, block)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime],
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    format: RelativeDateTimeFormat
): String = formatRelative(now, zone.timeZone, format, locale.locale)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime] using a DSL-configured format,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    block: RealRelativeDateTimeFormatBuilder.() -> Unit
): String = formatRelative(now, zone.timeZone, locale.locale, block)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] as a string,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    format: DateTimeFormat,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner()
): String = formatInterval(
    to = to,
    format = format,
    locale = locale.locale,
    timeZone = zone.timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate
)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] using a DSL-configured format,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
    block: DateTimeFormatBuilder.() -> Unit
): String = formatInterval(
    to = to,
    locale = locale.locale,
    timeZone = zone.timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
    block = block
)

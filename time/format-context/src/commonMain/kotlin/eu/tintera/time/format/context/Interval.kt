package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.core.context.TimeZoneContext
import eu.tintera.time.format.*
import kotlinx.datetime.Instant

/**
 * Formats a time interval between two instants into a localized string using combiners for different date/time scenarios,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun formatInterval(
    from: Instant,
    to: Instant,
    format: DateTimeFormat,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
): String = formatInterval(
    from = from,
    to = to,
    format = format,
    locale = locale.locale,
    timeZone = zone.timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate
)

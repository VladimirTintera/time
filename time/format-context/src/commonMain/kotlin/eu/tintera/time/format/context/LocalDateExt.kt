package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.core.context.TimeZoneContext
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDate

/**
 * Formats this [LocalDate] into a string representation using the specified format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun LocalDate.format(
    format: DateFormat
): String = format(format, locale.locale)

/**
 * Formats this [LocalDate] into a string representation using a DSL-configured format,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun LocalDate.format(
    block: DateFormatBuilder.() -> Unit
): String = format(locale.locale, block)

/**
 * Formats this [LocalDate] to return just the localized name of its month,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun LocalDate.formatMonthName(
    format: MonthFormat.Name
): String = formatMonthName(format, locale.locale)

/**
 * Formats this [LocalDate] to return just the localized name of its day of the week,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun LocalDate.formatWeekDayName(
    format: WeekDayFormat = WeekDayFormat.FullName
): String = formatWeekDayName(format, locale.locale)

/**
 * Formats the interval between this [LocalDate] and another [LocalDate] as a string,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun LocalDate.formatInterval(
    to: LocalDate,
    format: DateFormat,
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
 * Formats the interval between this [LocalDate] and another [LocalDate] using a DSL-configured format,
 * resolved using the [LocaleContext] and [TimeZoneContext] from the context.
 */
context(locale: LocaleContext, zone: TimeZoneContext)
fun LocalDate.formatInterval(
    to: LocalDate,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
    block: DateFormatBuilder.() -> Unit
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

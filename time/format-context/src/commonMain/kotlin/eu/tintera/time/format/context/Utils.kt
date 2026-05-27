package eu.tintera.time.format.context

import eu.tintera.locale.context.LocaleContext
import eu.tintera.time.format.MonthFormat
import eu.tintera.time.format.WeekDayFormat
import eu.tintera.time.format.formatName
import eu.tintera.time.format.getDecimalSeparator
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

/**
 * Formats the name of this [Month] into a localized string,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun Month.formatName(
    format: MonthFormat.Name = MonthFormat.Name.Full
): String = formatName(locale.locale, format)

/**
 * Formats the name of this [DayOfWeek] into a localized string,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun DayOfWeek.formatName(
    format: WeekDayFormat = WeekDayFormat.FullName
): String = formatName(locale.locale, format)

/**
 * Returns the localized decimal separator symbol for the active locale,
 * resolved using the [LocaleContext] from the context.
 */
context(locale: LocaleContext)
fun getDecimalSeparator(): String = getDecimalSeparator(locale.locale)

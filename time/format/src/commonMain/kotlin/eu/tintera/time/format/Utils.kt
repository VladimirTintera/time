package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber

/**
 * Returns the first day of the week based on the current locale.
 *
 * This function provides a platform-specific implementation to determine whether
 * the week starts on Sunday, Monday, or another day.
 *
 * Example:
 * ```kotlin
 * val firstDay = getFirstDayOfWeek()
 * // firstDay will be DayOfWeek.SUNDAY in the US, DayOfWeek.MONDAY in the UK
 * ```
 *
 * @return The [DayOfWeek] representing the first day of the week.
 */
expect fun getFirstDayOfWeek(locale: AppLocale): DayOfWeek

/**
 * Formats the name of this [kotlinx.datetime.Month] into a localized string.
 *
 * This extension function leverages the formatting capabilities of [LocalDate] to
 * provide a human-readable representation of the month.
 *
 * Example:
 * ```kotlin
 * val month = Month.JANUARY
 * val myLocale = localeForLanguageTag("en-US")
 * val fullName = month.formatName(myLocale)
 * val shortName = month.formatName(locale = myLocale, format = MonthFormat.Name.Short)
 * ```
 * @param locale The [AppLocale] to use for formatting.
 * @param format The desired [MonthFormat.Name] style. Defaults to [MonthFormat.Name.Full].
 * @return The formatted month name.
 */
fun Month.formatName(
    locale: AppLocale,
    format: MonthFormat.Name = MonthFormat.Name.Full,
): String {
    val dummyDate = LocalDate(year = 2026, month = this, day = 1)
    return dummyDate.formatMonthName(format = format, locale = locale)
}

/**
 * Formats the name of this [DayOfWeek] into a localized string.
 *
 * This extension function calculates a dummy date that falls on the specified day of the week
 * and then uses the formatting capabilities of [LocalDate] to get its name.
 *
 * Example:
 * ```kotlin
 * val day = DayOfWeek.MONDAY
 * val myLocale = localeForLanguageTag("en-US")
 * val fullName = day.formatName(myLocale)
 * val shortName = day.formatName(locale = myLocale, format = WeekDayFormat.ShortName)
 * ```
 * @param locale The [AppLocale] to use for formatting.
 * @param format The desired [WeekDayFormat] style. Defaults to [WeekDayFormat.FullName].
 * @return The formatted day of the week name.
 */
fun DayOfWeek.formatName(
    locale: AppLocale,
    format: WeekDayFormat = WeekDayFormat.FullName,
): String {
    return sampleDate().formatWeekDayName(
        format = format,
        locale = locale
    )
}

/**
 * Returns a date that falls on this [DayOfWeek], used as a formatting sample by [formatName].
 */
internal fun DayOfWeek.sampleDate(): LocalDate {
    // January 5, 2026, is a Monday (DayOfWeek.MONDAY = 1).
    // Adding the offset from Monday lands on the target day within the week of January 5–11.
    return LocalDate(year = 2026, month = 1, day = 5 + (this.isoDayNumber - 1))
}

/**
 * Returns the localized decimal separator symbol for the specified locale.
 *
 * Example:
 * ```kotlin
 * val myLocale = localeForLanguageTag("en-US")
 * val separator = getDecimalSeparator(myLocale)
 * ```
 *
 * @param locale The [AppLocale] to use.
 * @return The decimal separator character/string (e.g., "." or ",").
 */
expect fun getDecimalSeparator(locale: AppLocale): String
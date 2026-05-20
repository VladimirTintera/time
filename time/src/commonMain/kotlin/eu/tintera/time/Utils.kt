package eu.tintera.time

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

/**
 * Returns the first day of the week based on the current locale.
 *
 * This function provides a platform-specific implementation to determine whether
 * the week starts on Sunday, Monday, or another day.
 *
 * @return The [DayOfWeek] representing the first day of the week.
 */
expect fun getFirstDayOfWeek(): DayOfWeek

/**
 * Formats the name of this [kotlinx.datetime.Month] into a localized string.
 *
 * This extension function leverages the formatting capabilities of [LocalDate] to
 * provide a human-readable representation of the month.
 *
 * @param abbrev Whether to return the abbreviated month name (e.g., "Jan") or the full name (e.g., "January").
 * @return The formatted month name.
 */
fun kotlinx.datetime.Month.formatName(abbrev: Boolean = false): String {
    val dummyDate = kotlinx.datetime.LocalDate(year = 2026, month = this, day = 1)
    return dummyDate.formatMonthName(abbrev)
}

/**
 * Formats the name of this [DayOfWeek] into a localized string.
 *
 * This extension function calculates a dummy date that falls on the specified day of the week
 * and then uses the formatting capabilities of [LocalDate] to get its name.
 *
 * @param abbrev Whether to return the abbreviated day name (e.g., "Mon") or the full name (e.g., "Monday").
 * @return The formatted day of the week name.
 */
fun DayOfWeek.formatName(abbrev: Boolean = false): String {
    // January 1, 2026, was a Thursday (DayOfWeek.THURSDAY = 4).
    // We calculate an offset from Thursday to the target day to hit the correct day of the week.
    val safeDay = 10 + (this.isoDayNumber - 4)

    val dummyDate = LocalDate(year = 2026, monthNumber = 1, dayOfMonth = safeDay)
    return dummyDate.formatWeekDayName(abbrev)
}

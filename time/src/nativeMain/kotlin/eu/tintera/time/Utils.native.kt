package eu.tintera.time

import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSCalendar

actual fun getFirstDayOfWeek(): DayOfWeek {
    val firstWeekday = NSCalendar.currentCalendar.firstWeekday.toInt()

    val isoDayNumber = if (firstWeekday == 1) 7 else firstWeekday - 1

    return DayOfWeek(isoDayNumber)
}
package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.UnsafeNumber
import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSCalendar
import platform.Foundation.NSLocaleDecimalSeparator

@OptIn(UnsafeNumber::class)
actual fun getFirstDayOfWeek(): DayOfWeek {
    val firstWeekday = NSCalendar.currentCalendar.firstWeekday.toInt()

    val isoDayNumber = if (firstWeekday == 1) 7 else firstWeekday - 1

    return DayOfWeek(isoDayNumber)
}

actual fun getDecimalSeparator(locale: AppLocale): String {
    return locale.objectForKey(NSLocaleDecimalSeparator) as? String ?: "."
}
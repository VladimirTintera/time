package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.UnsafeNumber
import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSCalendar
import platform.Foundation.NSLocaleDecimalSeparator

@OptIn(UnsafeNumber::class)
actual fun getFirstDayOfWeek(locale: AppLocale): DayOfWeek {

    val firstWeekday = (NSCalendar.currentCalendar.copy() as NSCalendar).apply {
        this.locale = locale
    }.firstWeekday.toInt()

    val isoDayNumber = if (firstWeekday == 1) 7 else firstWeekday - 1

    return DayOfWeek(isoDayNumber)
}

actual fun getDecimalSeparator(locale: AppLocale): String {
    return locale.objectForKey(NSLocaleDecimalSeparator) as? String ?: "."
}
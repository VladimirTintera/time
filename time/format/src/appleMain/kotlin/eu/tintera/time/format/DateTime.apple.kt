package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.convert
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
internal actual fun nativeDateTimeFormat(
    date: LocalDateTime,
    locale: AppLocale,
    dateFormat: DateFormat?,
    timeFormat: TimeFormat?,
    skeleton: String
): String {

    // 1. Vytvoříme surové komponenty
    val components = NSDateComponents().apply {
        year = date.year.convert()
        month = date.month.number.convert()
        day = date.day.convert()
        hour = date.hour.convert()
        minute = date.minute.convert()
        second = date.second.convert()
        nanosecond = date.nanosecond.convert()
    }

    val utcZone = NSTimeZone.timeZoneWithName("UTC")
    val utcCalendar = NSCalendar.calendarWithIdentifier(NSCalendarIdentifierGregorian)?.apply {
        utcZone?.also { timeZone = it }
    }
    val nsDate = utcCalendar?.dateFromComponents(components) ?: NSDate()

    val nsTemplate = skeleton.toDateTimeTemplate(locale)

    val formatter = NSDateFormatter().apply {
        this.locale = locale
        utcZone?.also {
            timeZone = it
        }
        nsTemplate?.also {
            setLocalizedDateFormatFromTemplate(nsTemplate)
        }
    }

    return formatter.stringFromDate(nsDate)
}
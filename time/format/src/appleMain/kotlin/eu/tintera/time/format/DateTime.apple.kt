package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
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
        year = date.year.toLong()
        month = date.month.number.toLong()
        day = date.day.toLong()
        hour = date.hour.toLong()
        minute = date.minute.toLong()
        second = date.second.toLong()
        nanosecond = date.nanosecond.toLong()
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
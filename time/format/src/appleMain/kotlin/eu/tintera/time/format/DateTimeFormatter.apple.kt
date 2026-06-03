package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.convert
import kotlinx.datetime.number
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
internal actual fun createDateTimeFormatterFactory(
    locale: AppLocale
): DateTimeFormatterFactory = DateTimeFormatterFactory { skeleton, _, _ ->

    val template = skeleton.toDateTimeTemplate(locale)
    val utcZone = NSTimeZone.timeZoneWithName("UTC")
    val utcCalendar = NSCalendar.calendarWithIdentifier(NSCalendarIdentifierGregorian)?.apply {
        utcZone?.also { timeZone = it }
    }

    val formatter = NSDateFormatter().apply {
        this.locale = locale
        utcZone?.also { timeZone = it }
        template?.also {
            setLocalizedDateFormatFromTemplate(it)
            val iosPattern = this.dateFormat
            this.dateFormat = iosPattern.localizedPatternFix(skeleton)
        }
    }

    DateTimeFormatter { date ->

        val components = NSDateComponents().apply {
            year = date.year.convert()
            month = date.month.number.convert()
            day = date.day.convert()
            hour = date.hour.convert()
            minute = date.minute.convert()
            second = date.second.convert()
            nanosecond = date.nanosecond.convert()
        }

        val nsDate = utcCalendar?.dateFromComponents(components) ?: NSDate()
        (formatter.copy() as NSDateFormatter).stringFromDate(nsDate)
    }
}
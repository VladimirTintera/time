package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.UnsafeNumber
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toNSDate
import kotlinx.datetime.toNSTimeZone
import platform.Foundation.NSDateInterval
import platform.Foundation.NSDateIntervalFormatter
import kotlin.time.Instant

@OptIn(UnsafeNumber::class)
internal actual fun nativeIntervalFormat(
    from: Instant,
    to: Instant,
    dateFormat: DateFormat,
    timeFormat: TimeFormat?,
    skeleton: String,
    locale: AppLocale,
    timeZone: TimeZone
): String {

    val formatter = NSDateIntervalFormatter().apply {
        this.locale = locale
        dateTemplate = skeleton
        this.timeZone = timeZone.toNSTimeZone()
    }

    return formatter.stringFromDateInterval(NSDateInterval(from.toNSDate(), to.toNSDate())) ?: ""
}
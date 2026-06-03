package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toNSDate
import kotlinx.datetime.toNSTimeZone
import platform.Foundation.NSDateInterval
import platform.Foundation.NSDateIntervalFormatter

internal actual fun createIntervalFormatterFactory(
    locale: AppLocale,
    timeZone: TimeZone
): IntervalFormatterFactory = IntervalFormatterFactory { skeleton, _, _ ->

    val formatter = NSDateIntervalFormatter().apply {
        this.locale = locale
        dateTemplate = skeleton
        this.timeZone = timeZone.toNSTimeZone()
    }

    IntervalFormatter { from, to ->
        (formatter.copy() as NSDateIntervalFormatter).stringFromDateInterval(
            NSDateInterval(
                from.toNSDate(),
                to.toNSDate()
            )
        ) ?: ""
    }
}
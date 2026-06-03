package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.date.Date
import kotlinx.datetime.TimeZone

internal actual fun createIntervalFormatterFactory(
    locale: AppLocale,
    timeZone: TimeZone
): IntervalFormatterFactory = IntervalFormatterFactory { _, dateFormat, timeFormat ->

    val formatter = js.intl.DateTimeFormat(
        locales = locale,
        options = dateTimeFormatOptions(
            timeFormat = timeFormat,
            dateFormat = dateFormat,
            timeZone = timeZone.toJsTimeZoneString(locale)
        )
    )

    IntervalFormatter { from, to ->
        formatter.formatRange(
            startDate = Date(from.toEpochMilliseconds().toDouble()),
            endDate = Date(to.toEpochMilliseconds().toDouble())
        )
    }
}
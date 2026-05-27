package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.date.Date
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

internal actual fun nativeIntervalFormat(
    from: Instant,
    to: Instant,
    dateFormat: DateFormat,
    timeFormat: TimeFormat?,
    skeleton: String,
    locale: AppLocale,
    timeZone: TimeZone
): String = js.intl.DateTimeFormat(
    locales = locale,
    options = dateTimeFormatOptions(
        timeFormat = timeFormat,
        dateFormat = dateFormat,
        timeZone = timeZone.toJsTimeZoneString(locale)
    )
).formatRange(
    startDate = Date(from.toEpochMilliseconds().toDouble()),
    endDate = Date(to.toEpochMilliseconds().toDouble())
)

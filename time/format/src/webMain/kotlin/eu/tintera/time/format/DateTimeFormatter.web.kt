package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.date.Date
import kotlinx.datetime.number
import kotlin.js.toJsString

internal actual fun createDateTimeFormatterFactory(
    locale: AppLocale
): DateTimeFormatterFactory = DateTimeFormatterFactory { _, dateFormat, timeFormat ->
    val options = dateTimeFormatOptions(
        dateFormat = dateFormat,
        timeFormat = timeFormat,
        timeZone = "UTC"
    )

    DateTimeFormatter { date ->
        Date(
            Date.UTC(
                year = date.year,
                monthIndex = date.month.number - 1,
                date = date.day,
                hours = date.hour,
                minutes = date.minute,
                seconds = date.second,
                milliseconds = date.nanosecond / 1_000_000
            )
        ).toLocaleString(
            locales = locale.toJsString(),
            options = options
        )
    }

}
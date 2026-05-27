package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.date.Date
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.toJsString

@OptIn(ExperimentalWasmJsInterop::class)
internal actual fun nativeDateTimeFormat(
    date: LocalDateTime,
    locale: AppLocale,
    dateFormat: DateFormat?,
    timeFormat: TimeFormat?,
    skeleton: String
): String = Date(
    Date.UTC(
        date.year,
        date.month.number - 1,
        date.day,
        date.hour,
        date.minute,
        date.second,
        date.nanosecond / 1_000_000
    )
).toLocaleString(
    locale.toJsString(),
    dateTimeFormatOptions(
        dateFormat = dateFormat,
        timeFormat = timeFormat,
        timeZone = "UTC"
    )
)


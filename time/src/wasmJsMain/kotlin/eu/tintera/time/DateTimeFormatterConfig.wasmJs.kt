package eu.tintera.time

import js.date.Date
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@OptIn(ExperimentalWasmJsInterop::class)
internal actual fun formatDateTime(
    date: kotlinx.datetime.LocalDateTime,
    format: DateTimeFormat
): String {
    val epoch = date.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds().toDouble()
    val jsDate = Date(epoch)

    val options = format.toOptions()

    return jsDate.toLocaleString(locales = "default".toJsString(), options = options)
}
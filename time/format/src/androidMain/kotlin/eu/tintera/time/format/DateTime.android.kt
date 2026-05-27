package eu.tintera.time.format

import android.text.format.DateFormat
import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter


internal actual fun nativeDateTimeFormat(
    date: LocalDateTime,
    locale: AppLocale,
    dateFormat: eu.tintera.time.format.DateFormat?,
    timeFormat: TimeFormat?,
    skeleton: String
): String {
    val currentLocale = locale.toLocale()
    val pattern = DateFormat.getBestDateTimePattern(currentLocale, skeleton)
    val formatter = DateTimeFormatter.ofPattern(pattern, currentLocale)
    return date.toJavaLocalDateTime().format(formatter)
}
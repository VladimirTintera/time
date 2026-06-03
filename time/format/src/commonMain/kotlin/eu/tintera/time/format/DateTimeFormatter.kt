package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

internal fun interface DateTimeFormatter {
    fun format(date: LocalDateTime): String
}

internal fun interface DateTimeFormatterFactory {
    fun formatter(skeleton: String, dateFormat: DateFormatScope<LocalDate>?, timeFormat: TimeFormatScope<LocalTime>?): DateTimeFormatter
}

internal expect fun createDateTimeFormatterFactory(
    locale: AppLocale
): DateTimeFormatterFactory

internal fun String.localizedPatternFix(skeleton: String): String {
    var result = this

    if (skeleton.contains("HH") && result.contains("H") && !result.contains("HH")) {
        result = result.replace("H", "HH")
    }

    if (skeleton.contains("hh") && result.contains("h") && !result.contains("hh")) {
        result = result.replace("h", "hh")
    }

    if (skeleton.contains("jj")) {
        if (result.contains("H") && !result.contains("HH")) result = result.replace("H", "HH")
        if (result.contains("h") && !result.contains("hh")) result = result.replace("h", "hh")
    }

    return result
}
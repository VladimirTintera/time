package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

internal fun interface IntervalFormatter {
    fun format(from: Instant, to: Instant) : String
}

internal fun interface IntervalFormatterFactory {
    fun formatter(skeleton: String, dateFormat: DateFormatScope<OpenEndRange<LocalDate>>?, timeFormat: TimeFormatScope<OpenEndRange<LocalTime>>?) : IntervalFormatter
}

internal expect fun createIntervalFormatterFactory(
    locale: AppLocale,
    timeZone: TimeZone
) : IntervalFormatterFactory
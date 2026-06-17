package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.TimeZone

interface TimeZonedFormatScope<T: Any> : FormatScope<T> {
    val timeZone: TimeZone
}

interface FormatScope<T: Any> {
    val value: T
    val locale: AppLocale
}
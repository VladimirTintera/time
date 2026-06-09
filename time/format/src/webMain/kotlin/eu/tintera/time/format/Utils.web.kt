package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.intl.Locale
import js.intl.NumberFormat
import kotlinx.datetime.DayOfWeek
import web.navigator.navigator
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.toJsString

@OptIn(ExperimentalWasmJsInterop::class)
actual fun getFirstDayOfWeek(locale: AppLocale): DayOfWeek {
    return try {
        val locale = Locale(locale.toJsString())
        val firstDay = locale.getWeekInfo().firstDay
        DayOfWeek(firstDay)
    } catch (e: Throwable) {
        e.printStackTrace()
        val lang = navigator.language
        if (lang.contains("US") || lang.contains("CA")) {
            DayOfWeek.SUNDAY
        } else {
            DayOfWeek.MONDAY
        }
    }
}

actual fun getDecimalSeparator(locale: AppLocale): String {
    return NumberFormat(locale).format(0.1).substring(1, 2)
}
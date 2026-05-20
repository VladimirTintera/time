package eu.tintera.time

import js.intl.Locale
import kotlinx.datetime.DayOfWeek
import web.navigator.navigator
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.toJsString

@OptIn(ExperimentalWasmJsInterop::class)
actual fun getFirstDayOfWeek(): DayOfWeek {
    return try {
        val locale = Locale(navigator.language.toJsString())
        println("Locale: $locale, language = ${locale.language}")
        // JS getWeekInfo() vrací ISO formát (1=Pondělí, 7=Neděle)
        val firstDay = locale.getWeekInfo().firstDay
        DayOfWeek(firstDay)
    } catch (e: Throwable) {
        e.printStackTrace()
        // Fallback pro starší prohlížeče - pro US a Kanadu Neděle, jinak Pondělí
        val lang = navigator.language
        if (lang.contains("US") || lang.contains("CA")) {
            DayOfWeek.SUNDAY
        } else {
            DayOfWeek.MONDAY
        }
    }
}

//internal expect fun localeOptions(): LocaleOptions
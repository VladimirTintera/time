package eu.tintera.time

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.js.Date

internal actual fun formatDateTime(
    date: kotlinx.datetime.LocalDateTime,
    format: DateTimeFormat
): String {
    // Převod Kotlin data na JS Date
    val jsDate = Date(date.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())

    val options = dateLocaleOptions {
        format.dateFormat?.also {
            when (it.year) {
                YearFormat.FourDigits -> year = "numeric"
                YearFormat.TwoDigits -> year = "2-digit"
                null -> {}
            }

            when (it.month) {
                MonthFormat.FullName -> month = "long"
                MonthFormat.ShortName -> month = "short"
                MonthFormat.PaddedNumber -> month = "2-digit"
                MonthFormat.Number -> month = "numeric"
                null -> {}
            }

            when (it.day) {
                DayFormat.Normal -> day = "numeric"
                DayFormat.Padded -> day = "2-digit"
                null -> {}
            }

            when (it.weekDay) {
                WeekDayFormat.FullName -> weekday = "long"
                WeekDayFormat.ShortName -> weekday = "short"
                null -> {}
            }
        }

        format.timeFormat?.also {
            when (it.hour) {
                HourFormat.AutoPadded -> hour = "2-digit"
                HourFormat.Auto -> hour = "numeric"
                // Pro vynucení 12h/24h režimu lze přidat i vlastnost hour12 = true/false
                else -> if (it.hour != null) hour = "numeric"
            }

            when (it.minute) {
                MinuteFormat.Padded -> minute = "2-digit"
                MinuteFormat.Normal -> minute = "numeric"
                null -> {}
            }

            if (it.includeSeconds)
                second = "2-digit"

            if (it.includeMilliseconds) {
                // Aby milisekundy fungovaly, JavaScript vyžaduje,
                // aby byly zapnuté i sekundy (jinak by to nedávalo smysl)
                second = "2-digit"

                // Zde obejdeme chybějící deklaraci v Kotlinu a nastavíme
                // JavaScriptu, že chceme 3 desetinná místa (tisíciny = ms)
                this.asDynamic().fractionalSecondDigits = 3
            }
        }
    }

    return jsDate.toLocaleString(
        locales = arrayOf(),
        options = options
    )
}
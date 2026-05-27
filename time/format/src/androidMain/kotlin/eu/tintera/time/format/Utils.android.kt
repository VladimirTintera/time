package eu.tintera.time.format

import android.icu.text.DecimalFormatSymbols
import android.icu.util.ULocale
import eu.tintera.locale.AppLocale
import kotlinx.datetime.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*

actual fun getFirstDayOfWeek(): DayOfWeek {
    val javaDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    return DayOfWeek(javaDayOfWeek.value)
}

actual fun getDecimalSeparator(locale: AppLocale): String {
    val symbols = DecimalFormatSymbols.getInstance(locale)
    return symbols.decimalSeparator.toString()
}
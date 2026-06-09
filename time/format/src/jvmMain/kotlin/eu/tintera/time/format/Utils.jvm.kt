package eu.tintera.time.format

import com.ibm.icu.text.DecimalFormatSymbols
import eu.tintera.locale.AppLocale
import kotlinx.datetime.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*

actual fun getFirstDayOfWeek(locale: AppLocale): DayOfWeek {
    val javaDayOfWeek = WeekFields.of(locale).firstDayOfWeek
    return DayOfWeek(javaDayOfWeek.value)
}

actual fun getDecimalSeparator(locale: AppLocale): String {
    val symbols = DecimalFormatSymbols.getInstance(locale)
    return symbols.decimalSeparator.toString()
}
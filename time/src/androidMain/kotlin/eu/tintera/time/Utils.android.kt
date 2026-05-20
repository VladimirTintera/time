package eu.tintera.time

import kotlinx.datetime.DayOfWeek
import java.time.temporal.WeekFields
import java.util.Locale

actual fun getFirstDayOfWeek(): DayOfWeek {
    val javaDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    return DayOfWeek(javaDayOfWeek.value)
}
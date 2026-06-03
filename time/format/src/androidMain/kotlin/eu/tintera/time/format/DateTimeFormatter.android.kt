package eu.tintera.time.format

import android.text.format.DateFormat
import eu.tintera.locale.AppLocale
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.*

internal actual fun createDateTimeFormatterFactory(
    locale: AppLocale
): DateTimeFormatterFactory = DateTimeFormatterFactory { skeleton, _, _ ->

    val locale = locale.toLocale()
    val pattern = DateFormat.getBestDateTimePattern(locale, skeleton).localizedPatternFix(skeleton)
    val formatter = android.icu.text.SimpleDateFormat(pattern, locale).apply {
        timeZone = android.icu.util.TimeZone.getTimeZone("UTC")
    }

    DateTimeFormatter {
        val epochMillis = it.toInstant(TimeZone.UTC).toEpochMilliseconds()
        val localFormatter = formatter.clone() as android.icu.text.SimpleDateFormat
        localFormatter.format(Date(epochMillis))
    }
}
package eu.tintera.time.format

import android.icu.util.DateInterval
import eu.tintera.locale.AppLocale
import kotlinx.datetime.TimeZone
import java.text.FieldPosition
import kotlin.time.Instant

internal actual fun nativeIntervalFormat(
    from: Instant,
    to: Instant,
    dateFormat: DateFormat,
    timeFormat: TimeFormat?,
    skeleton: String,
    locale: AppLocale,
    timeZone: TimeZone
): String {
    val formatter = android.icu.text.DateIntervalFormat.getInstance(skeleton, locale)
    formatter.timeZone = android.icu.util.TimeZone.getTimeZone(timeZone.id)
    return formatter.format(
        DateInterval(from.toEpochMilliseconds(), to.toEpochMilliseconds()),
        StringBuffer(),
        FieldPosition(0)
    ).toString()
}
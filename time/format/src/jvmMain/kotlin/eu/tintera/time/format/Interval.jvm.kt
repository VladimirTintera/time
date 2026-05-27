package eu.tintera.time.format

import com.ibm.icu.text.DateIntervalFormat
import com.ibm.icu.util.DateInterval
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
    val formatter = DateIntervalFormat.getInstance(skeleton, locale)
    formatter.timeZone = com.ibm.icu.util.TimeZone.getTimeZone(timeZone.id)
    return formatter.format(
        DateInterval(from.toEpochMilliseconds(), to.toEpochMilliseconds()),
        StringBuffer(),
        FieldPosition(0)
    ).toString()
}

package eu.tintera.time.format

import android.icu.util.DateInterval
import eu.tintera.locale.AppLocale
import kotlinx.datetime.TimeZone
import java.text.FieldPosition

internal actual fun createIntervalFormatterFactory(
    locale: AppLocale,
    timeZone: TimeZone
): IntervalFormatterFactory = IntervalFormatterFactory { skeleton, _, _ ->

    val formatter = android.icu.text.DateIntervalFormat.getInstance(skeleton, locale).apply {
        this.timeZone = android.icu.util.TimeZone.getTimeZone(timeZone.id)
    }

    IntervalFormatter { from, to ->
        formatter.format(
            DateInterval(from.toEpochMilliseconds(), to.toEpochMilliseconds()),
            StringBuffer(),
            FieldPosition(0)
        ).toString()
    }
}
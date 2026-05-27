package eu.tintera.time.format

import com.ibm.icu.text.MeasureFormat
import com.ibm.icu.util.Measure
import com.ibm.icu.util.MeasureUnit
import eu.tintera.locale.AppLocale
import kotlin.time.Duration

internal actual fun nativeDurationFormat(
    duration: Duration,
    format: DurationFormat,
    locale: AppLocale
): String {

    val measures = buildList {

        duration.toComponents { days, hours, minutes, seconds, millis ->
            format.days.ifAvailable({ days > 0 }) {
                add(Measure(days, MeasureUnit.DAY))
            }
            format.hours.ifAvailable({ hours > 0 }) { add(Measure(hours, MeasureUnit.HOUR)) }
            format.minutes.ifAvailable({ minutes > 0 }) { add(Measure(minutes, MeasureUnit.MINUTE)) }
            format.seconds.ifAvailable({ seconds > 0 }) { add(Measure(seconds, MeasureUnit.SECOND)) }
            format.fractionalSeconds.ifAvailable({ millis > 0 }) { add(Measure(millis, MeasureUnit.MILLISECOND)) }
        }
    }

    val androidWidth = when (format.style) {
        FormatStyle.Full -> MeasureFormat.FormatWidth.WIDE
        FormatStyle.Short -> MeasureFormat.FormatWidth.SHORT
        FormatStyle.Narrow -> MeasureFormat.FormatWidth.NARROW
    }

    val formatter = MeasureFormat.getInstance(locale, androidWidth)
    return formatter.formatMeasures(*measures.toTypedArray())
}
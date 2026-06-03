package eu.tintera.time.format

import com.ibm.icu.text.MeasureFormat
import com.ibm.icu.util.Measure
import com.ibm.icu.util.MeasureUnit
import eu.tintera.locale.AppLocale
import kotlin.time.Duration

internal actual fun nativeDurationFormat(
    measurables: List<Measurable>,
    style: FormatStyle,
    locale: AppLocale
): List<String> {

    val androidWidth = when (style) {
        FormatStyle.Full -> MeasureFormat.FormatWidth.WIDE
        FormatStyle.Short ->MeasureFormat.FormatWidth.SHORT
        FormatStyle.Narrow -> MeasureFormat.FormatWidth.NARROW
    }

    val formatter = MeasureFormat.getInstance(locale, androidWidth)

    return measurables.map {
        formatter.formatMeasures(it.toMeasure())
    }
}
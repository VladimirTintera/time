package eu.tintera.time.format

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import eu.tintera.locale.AppLocale

internal actual fun nativePeriodFormat(
    style: FormatStyle,
    items: List<Measurable>,
    locale: AppLocale
): String {

    val androidWidth = when (style) {
        FormatStyle.Full -> MeasureFormat.FormatWidth.WIDE
        FormatStyle.Short -> MeasureFormat.FormatWidth.SHORT
        FormatStyle.Narrow -> MeasureFormat.FormatWidth.NARROW
    }

    val formatter = MeasureFormat.getInstance(locale, androidWidth)

    return formatter.formatMeasures(*items.map {
        it.toMeasure()
    }.toTypedArray())
}
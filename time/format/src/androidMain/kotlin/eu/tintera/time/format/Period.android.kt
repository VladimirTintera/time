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
        Measure(
            it.value, when (it.unit) {
                eu.tintera.time.format.MeasureUnit.YEARS -> MeasureUnit.YEAR
                eu.tintera.time.format.MeasureUnit.MONTHS -> MeasureUnit.MONTH
                eu.tintera.time.format.MeasureUnit.DAYS -> MeasureUnit.DAY
                eu.tintera.time.format.MeasureUnit.HOURS -> MeasureUnit.HOUR
                eu.tintera.time.format.MeasureUnit.MINUTES -> MeasureUnit.MINUTE
                eu.tintera.time.format.MeasureUnit.SECOND -> MeasureUnit.SECOND
            }
        )
    }.toTypedArray())
}
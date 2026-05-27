package eu.tintera.time.format

import com.ibm.icu.text.MeasureFormat
import com.ibm.icu.util.Measure
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
                MeasureUnit.YEARS -> com.ibm.icu.util.MeasureUnit.YEAR
                MeasureUnit.MONTHS -> com.ibm.icu.util.MeasureUnit.MONTH
                MeasureUnit.DAYS -> com.ibm.icu.util.MeasureUnit.DAY
                MeasureUnit.HOURS -> com.ibm.icu.util.MeasureUnit.HOUR
                MeasureUnit.MINUTES -> com.ibm.icu.util.MeasureUnit.MINUTE
                MeasureUnit.SECOND -> com.ibm.icu.util.MeasureUnit.SECOND
            }
        )
    }.toTypedArray())
}
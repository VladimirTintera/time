package eu.tintera.time.format

import android.icu.util.Measure
import android.icu.util.MeasureUnit

internal fun Measurable.toMeasure() = Measure(
    value, when (unit) {
        eu.tintera.time.format.MeasureUnit.YEARS -> MeasureUnit.YEAR
        eu.tintera.time.format.MeasureUnit.MONTHS -> MeasureUnit.MONTH
        eu.tintera.time.format.MeasureUnit.DAYS -> MeasureUnit.DAY
        eu.tintera.time.format.MeasureUnit.HOURS -> MeasureUnit.HOUR
        eu.tintera.time.format.MeasureUnit.MINUTES -> MeasureUnit.MINUTE
        eu.tintera.time.format.MeasureUnit.SECONDS -> MeasureUnit.SECOND
        eu.tintera.time.format.MeasureUnit.FRACTIONAL_SECONDS -> MeasureUnit.MILLISECOND
    }
)
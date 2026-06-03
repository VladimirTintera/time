package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.intl.*
import js.intl.DurationFormat
import js.numbers.JsNumbers.toJsInt
import js.objects.buildReadonlyRecord
import kotlin.js.toJsString

internal actual fun nativePeriodFormat(
    style: FormatStyle,
    items: List<Measurable>,
    locale: AppLocale
): String = DurationFormat(
    locales = locale.toJsString(),
    options = jsObject {
        this.style = when (style) {
            FormatStyle.Full -> DurationFormatStyle.long
            FormatStyle.Short -> DurationFormatStyle.short
            FormatStyle.Narrow -> DurationFormatStyle.narrow
        }
    }
).format(
    duration = buildReadonlyRecord {
        items.forEach {
            set(
                key = when (it.unit) {
                    MeasureUnit.YEARS -> DurationFormatUnit.years
                    MeasureUnit.MONTHS -> DurationFormatUnit.months
                    MeasureUnit.DAYS -> DurationFormatUnit.days
                    MeasureUnit.HOURS -> DurationFormatUnit.hours
                    MeasureUnit.MINUTES -> DurationFormatUnit.minutes
                    MeasureUnit.SECONDS -> DurationFormatUnit.seconds
                    MeasureUnit.FRACTIONAL_SECONDS -> DurationFormatUnit.milliseconds
                },
                value = it.value.toJsInt()
            )
        }
    }
)

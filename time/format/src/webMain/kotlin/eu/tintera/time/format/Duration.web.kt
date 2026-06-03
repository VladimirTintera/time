package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.intl.*
import js.intl.DurationFormat
import js.numbers.JsNumbers.toJsInt
import js.objects.buildReadonlyRecord
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.toJsString
import eu.tintera.time.format.DurationFormat as LibDurationFormat

@OptIn(ExperimentalWasmJsInterop::class)
internal actual fun nativeDurationFormat(
    measurables: List<Measurable>,
    style: FormatStyle,
    locale: AppLocale
): List<String> {
    val formatter = DurationFormat(
        locales = locale.toJsString(),
        options = jsObject {
            this.style = when (style) {
                FormatStyle.Full -> DurationFormatStyle.long
                FormatStyle.Short -> DurationFormatStyle.short
                FormatStyle.Narrow -> DurationFormatStyle.narrow
            }
        }
    )
    return measurables.map {
        formatter.format(
            buildReadonlyRecord {
                val unit = when (it.unit) {
                    MeasureUnit.YEARS -> throw UnsupportedOperationException("Unsupported unit: ${it.unit}")
                    MeasureUnit.MONTHS -> throw UnsupportedOperationException("Unsupported unit: ${it.unit}")
                    MeasureUnit.DAYS -> DurationFormatUnit.days
                    MeasureUnit.HOURS -> DurationFormatUnit.hours
                    MeasureUnit.MINUTES -> DurationFormatUnit.minutes
                    MeasureUnit.SECONDS -> DurationFormatUnit.seconds
                    MeasureUnit.FRACTIONAL_SECONDS -> DurationFormatUnit.milliseconds
                }

                set(unit, it.value.toJsInt())
            }
        )
    }
}
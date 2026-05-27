package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.intl.*
import js.intl.DurationFormat
import js.numbers.JsNumbers.toJsInt
import js.objects.buildReadonlyRecord
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.toJsString
import kotlin.time.Duration
import eu.tintera.time.format.DurationFormat as LibDurationFormat

@OptIn(ExperimentalWasmJsInterop::class)
internal actual fun nativeDurationFormat(
    duration: Duration,
    format: LibDurationFormat,
    locale: AppLocale
): String {

    val formatter = DurationFormat(
        locales = locale.toJsString(),
        options = jsObject {
            style = when (format.style) {
                FormatStyle.Full -> DurationFormatStyle.long
                FormatStyle.Short -> DurationFormatStyle.short
                FormatStyle.Narrow -> DurationFormatStyle.narrow
            }
        }
    )

    return duration.toComponents { days, hours, minutes, seconds, nanoseconds ->
        val millis = nanoseconds / 1_000_000

        val record = buildReadonlyRecord {
            format.days.ifAvailable({ days > 0 }) { set(DurationFormatUnit.days, days.toInt().toJsInt()) }
            format.hours.ifAvailable({ hours > 0 }) { set(DurationFormatUnit.hours, hours.toJsInt()) }
            format.minutes.ifAvailable({ minutes > 0 }) { set(DurationFormatUnit.minutes, minutes.toJsInt()) }
            format.seconds.ifAvailable({ seconds > 0 }) { set(DurationFormatUnit.seconds, seconds.toJsInt()) }
            format.fractionalSeconds.ifAvailable({ millis > 0 }) {
                set(DurationFormatUnit.milliseconds, millis.toJsInt())
            }
        }

        formatter.format(record)
    }
}
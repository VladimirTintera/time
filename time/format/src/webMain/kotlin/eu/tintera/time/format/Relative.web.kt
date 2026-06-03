package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import js.intl.*
import kotlin.js.toJsString

internal actual fun nativeRelativeTimeFormat(
    measurable: Measurable?,
    style: FormatStyle,
    display: RelativeDisplay,
    locale: AppLocale
): String = RelativeTimeFormat(
    locales = locale.toJsString(),
    options = jsObject {
        numeric = when(display) {
            RelativeDisplay.Idiomatic -> RelativeTimeFormatNumeric.auto
            RelativeDisplay.Numeric -> RelativeTimeFormatNumeric.always
        }
        this.style = when (style) {
            FormatStyle.Full -> RelativeTimeFormatStyle.long
            FormatStyle.Short -> RelativeTimeFormatStyle.short
            FormatStyle.Narrow -> RelativeTimeFormatStyle.narrow
        }
    }
).format(
    value = measurable?.value?.toDouble() ?: 0.0,
    unit = when (measurable?.unit) {
        MeasureUnit.YEARS -> RelativeTimeFormatUnit.years
        MeasureUnit.MONTHS -> RelativeTimeFormatUnit.months
        MeasureUnit.DAYS -> RelativeTimeFormatUnit.days
        MeasureUnit.HOURS -> RelativeTimeFormatUnit.hour
        MeasureUnit.MINUTES -> RelativeTimeFormatUnit.minute
        MeasureUnit.SECONDS -> RelativeTimeFormatUnit.seconds
        null -> RelativeTimeFormatUnit.seconds
        MeasureUnit.FRACTIONAL_SECONDS -> throw UnsupportedOperationException("Unsupported unit: ${measurable.unit}")
    }
)
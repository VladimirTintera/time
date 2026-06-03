package eu.tintera.time.format


import eu.tintera.locale.AppLocale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.convert
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
internal actual fun nativeRelativeTimeFormat(
    measurable: Measurable?,
    style: FormatStyle,
    display: RelativeDisplay,
    locale: AppLocale
): String {

    val components = NSDateComponents()

    if (measurable != null) {
        when (measurable.unit) {
            MeasureUnit.YEARS -> components.year = measurable.value.convert()
            MeasureUnit.MONTHS -> components.month = measurable.value.convert()
            MeasureUnit.DAYS -> components.day = measurable.value.convert()
            MeasureUnit.HOURS -> components.hour = measurable.value.convert()
            MeasureUnit.MINUTES -> components.minute = measurable.value.convert()
            MeasureUnit.SECONDS -> components.second = measurable.value.convert()
            MeasureUnit.FRACTIONAL_SECONDS -> components.nanosecond = (measurable.value * 1_000_000).convert()
        }
    } else components.second = 0.convert()

    val formatter = NSRelativeDateTimeFormatter().apply {
        this.locale = locale
        unitsStyle = when (style) {
            FormatStyle.Full -> NSRelativeDateTimeFormatterUnitsStyleFull
            FormatStyle.Short -> NSRelativeDateTimeFormatterUnitsStyleShort
            FormatStyle.Narrow -> NSRelativeDateTimeFormatterUnitsStyleAbbreviated
        }

        dateTimeStyle = when (display) {
            RelativeDisplay.Idiomatic -> NSRelativeDateTimeFormatterStyleNamed
            RelativeDisplay.Numeric -> NSRelativeDateTimeFormatterStyleNumeric
        }

    }

    return formatter.localizedStringFromDateComponents(dateComponents = components)
}
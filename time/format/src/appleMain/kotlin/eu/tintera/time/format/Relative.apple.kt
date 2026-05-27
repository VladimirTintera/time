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
            MeasureUnit.SECOND -> components.second = measurable.value.convert()
        }
    } else components.second = 0.convert()

    val formatter = NSRelativeDateTimeFormatter().apply {
        this.locale = locale
        unitsStyle = when (style) {
            FormatStyle.Full -> NSRelativeDateTimeFormatterUnitsStyleFull
            FormatStyle.Short -> NSRelativeDateTimeFormatterUnitsStyleShort
            FormatStyle.Narrow -> NSRelativeDateTimeFormatterUnitsStyleAbbreviated
        }

        dateTimeStyle = NSRelativeDateTimeFormatterStyleNamed

    }

    return formatter.localizedStringFromDateComponents(dateComponents = components)
}
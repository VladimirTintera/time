package eu.tintera.time.format


import eu.tintera.locale.AppLocale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
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
            MeasureUnit.YEARS -> components.year = measurable.value.toLong()
            MeasureUnit.MONTHS -> components.month = measurable.value.toLong()
            MeasureUnit.DAYS -> components.day = measurable.value.toLong()
            MeasureUnit.HOURS -> components.hour = measurable.value.toLong()
            MeasureUnit.MINUTES -> components.minute = measurable.value.toLong()
            MeasureUnit.SECOND -> components.second = measurable.value.toLong()
        }
    }

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
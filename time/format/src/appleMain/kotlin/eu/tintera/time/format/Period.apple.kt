package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.convert
import platform.Foundation.*

@OptIn(UnsafeNumber::class, ExperimentalForeignApi::class)
internal actual fun nativePeriodFormat(
    style: FormatStyle,
    items: List<Measurable>,
    locale: AppLocale
): String {
    val components = NSDateComponents()
    var allowedUnitsMask: NSCalendarUnit = 0u.convert()

    items.forEach {
        when (it.unit) {
            MeasureUnit.YEARS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitYear
                components.year = it.value.convert()
            }

            MeasureUnit.MONTHS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitMonth
                components.month = it.value.convert()
            }

            MeasureUnit.DAYS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitDay
                components.day = it.value.convert()
            }

            MeasureUnit.HOURS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitHour
                components.hour = it.value.convert()
            }

            MeasureUnit.MINUTES -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitMinute
                components.minute = it.value.convert()
            }

            MeasureUnit.SECONDS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitSecond
                components.second = it.value.convert()
            }

            MeasureUnit.FRACTIONAL_SECONDS -> throw UnsupportedOperationException("Unsupported unit: ${it.unit}")
        }
    }

    val formatter = NSDateComponentsFormatter().apply {
        unitsStyle = when (style) {
            FormatStyle.Full -> NSDateComponentsFormatterUnitsStyleFull
            FormatStyle.Short -> NSDateComponentsFormatterUnitsStyleShort
            FormatStyle.Narrow -> NSDateComponentsFormatterUnitsStyleAbbreviated // "h" "m" "s"
        }
        allowedUnits = allowedUnitsMask

        calendar = (NSCalendar.currentCalendar.copy() as NSCalendar).apply {
            this.locale = locale
        }
        zeroFormattingBehavior = NSDateComponentsFormatterZeroFormattingBehaviorNone
    }

    return formatter.stringFromDateComponents(components) ?: ""
}
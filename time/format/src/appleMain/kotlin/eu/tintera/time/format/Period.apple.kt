package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.*

@OptIn(UnsafeNumber::class)
internal actual fun nativePeriodFormat(
    style: FormatStyle,
    items: List<Measurable>,
    locale: AppLocale
): String {
    val components = NSDateComponents()
    var allowedUnitsMask = 0u

    items.forEach {
        when (it.unit) {
            MeasureUnit.YEARS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitYear.toUInt()
                components.year = it.value.toLong()
            }

            MeasureUnit.MONTHS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitMonth.toUInt()
                components.month = it.value.toLong()
            }

            MeasureUnit.DAYS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitDay.toUInt()
                components.day = it.value.toLong()
            }

            MeasureUnit.HOURS -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitHour.toUInt()
                components.hour = it.value.toLong()
            }

            MeasureUnit.MINUTES -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitMinute.toUInt()
                components.minute = it.value.toLong()
            }

            MeasureUnit.SECOND -> {
                allowedUnitsMask = allowedUnitsMask or NSCalendarUnitSecond.toUInt()
                components.second = it.value.toLong()
            }
        }
    }

    val formatter = NSDateComponentsFormatter().apply {
        unitsStyle = when (style) {
            FormatStyle.Full -> NSDateComponentsFormatterUnitsStyleFull
            FormatStyle.Short -> NSDateComponentsFormatterUnitsStyleShort
            FormatStyle.Narrow -> NSDateComponentsFormatterUnitsStyleAbbreviated // "h" "m" "s"
        }
        allowedUnits = allowedUnitsMask.toULong()

        calendar = (NSCalendar.currentCalendar.copy() as NSCalendar).apply {
            this.locale = locale
        }
        zeroFormattingBehavior = NSDateComponentsFormatterZeroFormattingBehaviorNone
    }

    return formatter.stringFromDateComponents(components) ?: ""
}
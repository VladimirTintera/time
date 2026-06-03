package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.*
import kotlin.time.Duration

@OptIn(UnsafeNumber::class)
internal actual fun nativeDurationFormat(
    measurables: List<Measurable>,
    style: FormatStyle,
    locale: AppLocale
): List<String> {

    val formatter = NSMeasurementFormatter().apply {
        this.locale = locale
        unitStyle = when (style) {
            FormatStyle.Full -> NSFormattingUnitStyleLong
            FormatStyle.Short -> NSFormattingUnitStyleMedium
            FormatStyle.Narrow -> NSFormattingUnitStyleShort
        }
        // Vypneme automatické převody (nechceme, aby z 60 minut udělal 1 hodinu, pokud hodiny nechceme)
        unitOptions = NSMeasurementFormatterUnitOptionsProvidedUnit
    }

    val parts = measurables.mapNotNull {
        when(it.unit) {
            MeasureUnit.YEARS -> throw UnsupportedOperationException("Unsupported unit: ${it.unit}")
            MeasureUnit.MONTHS -> throw UnsupportedOperationException("Unsupported unit: ${it.unit}")
            MeasureUnit.DAYS -> {
                val dayFormatter = NSDateComponentsFormatter().apply {
                    unitsStyle = when (style) {
                        FormatStyle.Full -> NSDateComponentsFormatterUnitsStyleFull
                        FormatStyle.Short -> NSDateComponentsFormatterUnitsStyleShort
                        FormatStyle.Narrow -> NSDateComponentsFormatterUnitsStyleAbbreviated
                    }
                    allowedUnits = NSCalendarUnitDay
                    calendar = (NSCalendar.currentCalendar.copy() as? NSCalendar)?.apply {
                        this.locale = locale
                    }
                }
                // Vyrobí např. "1 den", "3 dny", "5 dní" nebo "1 d." podle zvoleného stylu a locale
                dayFormatter.stringFromTimeInterval(it.value * 86400.0)
            }
            MeasureUnit.HOURS -> {
                val measurement = NSMeasurement(it.value.toDouble(), NSUnitDuration.hours())
                formatter.stringFromMeasurement(measurement)
            }
            MeasureUnit.MINUTES -> {
                val measurement = NSMeasurement(it.value.toDouble(), NSUnitDuration.minutes())
                formatter.stringFromMeasurement(measurement)
            }
            MeasureUnit.SECONDS -> {
                val measurement = NSMeasurement(it.value.toDouble(), NSUnitDuration.seconds())
                formatter.stringFromMeasurement(measurement)
            }
            MeasureUnit.FRACTIONAL_SECONDS -> {
                val measurement = NSMeasurement(it.value.toDouble(), NSUnitDuration.milliseconds())
                formatter.stringFromMeasurement(measurement)
            }
        }
    }


    return parts
}
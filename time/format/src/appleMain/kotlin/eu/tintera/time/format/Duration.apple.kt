package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.*
import kotlin.time.Duration

@OptIn(UnsafeNumber::class)
internal actual fun nativeDurationFormat(
    duration: Duration,
    format: DurationFormat,
    locale: AppLocale
): String {

    val formatter = NSMeasurementFormatter().apply {
        this.locale = locale
        unitStyle = when (format.style) {
            FormatStyle.Full -> NSFormattingUnitStyleLong
            FormatStyle.Short -> NSFormattingUnitStyleMedium
            FormatStyle.Narrow -> NSFormattingUnitStyleShort
        }
        // Vypneme automatické převody (nechceme, aby z 60 minut udělal 1 hodinu, pokud hodiny nechceme)
        unitOptions = NSMeasurementFormatterUnitOptionsProvidedUnit
    }

    val parts = buildList {
        duration.toComponents { days, hours, minutes, seconds, nanoseconds ->
            val millis = nanoseconds / 1_000_000

            format.days.ifAvailable({ days > 0 }) {
                val dayFormatter = NSDateComponentsFormatter().apply {
                    unitsStyle = when (format.style) {
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
                dayFormatter.stringFromTimeInterval(days * 86400.0)?.also {
                    add(it)
                }
            }
            format.hours.ifAvailable({ hours > 0 }) {
                val measurement = NSMeasurement(hours.toDouble(), NSUnitDuration.hours())
                add(formatter.stringFromMeasurement(measurement))
            }
            format.minutes.ifAvailable({ minutes > 0 }) {
                val measurement = NSMeasurement(minutes.toDouble(), NSUnitDuration.minutes())
                add(formatter.stringFromMeasurement(measurement))
            }
            format.seconds.ifAvailable({ seconds > 0 }) {
                val measurement = NSMeasurement(seconds.toDouble(), NSUnitDuration.seconds())
                add(formatter.stringFromMeasurement(measurement))
            }
            format.fractionalSeconds.ifAvailable({ millis > 0 }) {
                val measurement = NSMeasurement(millis.toDouble(), NSUnitDuration.milliseconds())
                add(formatter.stringFromMeasurement(measurement))
            }
        }
    }

    return NSListFormatter().apply {
        this.locale = locale
    }.stringFromItems(parts) ?: ""
}
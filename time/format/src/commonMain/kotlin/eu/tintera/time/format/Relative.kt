package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.yearsUntil
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.time.DurationUnit
import kotlin.time.Instant


internal fun platformRelativeTimeFormat(
    target: Instant,
    now: Instant,
    timeZone: TimeZone,
    format: RelativeDateTimeFormat,
    locale: AppLocale,
): String {

    if (format.years == null && format.months == null && format.days == null && format.hours == null && format.minutes == null && format.seconds == null) {
        throw EmptyFormatConfigurationException("RelativeDateTimeFormat cannot be empty.")
    }

    val measurable = computeRelativeUnit(
        target = target,
        now = now,
        zone = timeZone,
        format = format,
    )

    return nativeRelativeTimeFormat(
        measurable = measurable,
        style = format.style,
        locale = locale
    )
}

internal expect fun nativeRelativeTimeFormat(
    measurable: Measurable?,
    style: FormatStyle,
    locale: AppLocale
): String

internal fun computeRelativeUnit(
    target: Instant,
    now: Instant,
    zone: TimeZone,
    format: RelativeDateTimeFormat
): Measurable? {

    format.years?.let { threshold ->
        val years = now.yearsUntil(target, zone)
        if (years.absoluteValue >= threshold.min) return Measurable(MeasureUnit.YEARS, years)
    }

    format.months?.let { threshold ->
        val months = now.monthsUntil(target, zone)
        if (months.absoluteValue >= threshold.min) return Measurable(MeasureUnit.MONTHS, months)
    }

    format.days?.let { threshold ->
        val days = now.daysUntil(target, zone)
        if (days.absoluteValue >= threshold.min) return Measurable(MeasureUnit.DAYS, days)
    }

    val duration = target - now

    format.hours?.let { threshold ->
        val hours = duration.toDouble(DurationUnit.HOURS).roundToInt()
        if (hours.absoluteValue >= threshold.min) return Measurable(MeasureUnit.HOURS, hours)
    }

    format.minutes?.let { threshold ->
        val minutes = duration.toDouble(DurationUnit.MINUTES).roundToInt()
        if (minutes.absoluteValue >= threshold.min) return Measurable(MeasureUnit.MINUTES, minutes)
    }

    format.seconds?.let { threshold ->
        val seconds = duration.toDouble(DurationUnit.SECONDS).roundToInt()
        if (seconds.absoluteValue >= threshold.min) return Measurable(MeasureUnit.SECOND, seconds)
    }

    return null
}

package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.time.DurationUnit
import kotlin.time.Instant

data class RelativeValues(
    val target: LocalDateTime,
    val now: LocalDateTime
) {
    val isPast: Boolean get() = target < now
    val isFuture: Boolean get() = target > now
}

internal fun platformRelativeTimeFormat(
    target: Instant,
    now: Instant,
    timeZone: TimeZone,
    format: RelativeDateTimeFormat,
    locale: AppLocale,
): String {

    val scope = RelativeDateTimeFormatScope(
        value = RelativeValues(
            target = target.toLocalDateTime(timeZone),
            now = now.toLocalDateTime(timeZone),
        ),
        locale = locale,
    )

    format.block(scope)

    if (scope.years == null && scope.months == null && scope.days == null && scope.hours == null && scope.minutes == null && scope.seconds == null) {
        throw EmptyFormatConfigurationException("RelativeDateTimeFormat cannot be empty.")
    }

    val measurable = computeRelativeUnit(
        target = target,
        now = now,
        zone = timeZone,
        scope = scope,
    )

    return nativeRelativeTimeFormat(
        measurable = measurable,
        style = scope.style,
        locale = locale,
        display = scope.display,
    )
}

internal expect fun nativeRelativeTimeFormat(
    measurable: Measurable?,
    style: FormatStyle,
    display: RelativeDisplay,
    locale: AppLocale
): String

internal fun computeRelativeUnit(
    target: Instant,
    now: Instant,
    zone: TimeZone,
    scope: RelativeDateTimeFormatScope
): Measurable? {

    scope.years?.let { threshold ->
        val years = now.yearsUntil(target, zone)
        if (years.absoluteValue >= threshold.min) return Measurable(MeasureUnit.YEARS, years)
    }

    scope.months?.let { threshold ->
        val months = now.monthsUntil(target, zone)
        if (months.absoluteValue >= threshold.min) return Measurable(MeasureUnit.MONTHS, months)
    }

    scope.days?.let { threshold ->
        val days = now.daysUntil(target, zone)
        if (days.absoluteValue >= threshold.min) return Measurable(MeasureUnit.DAYS, days)
    }

    val duration = target - now

    scope.hours?.let { threshold ->
        val hours = duration.toDouble(DurationUnit.HOURS).roundToInt()
        if (hours.absoluteValue >= threshold.min) return Measurable(MeasureUnit.HOURS, hours)
    }

    scope.minutes?.let { threshold ->
        val minutes = duration.toDouble(DurationUnit.MINUTES).roundToInt()
        if (minutes.absoluteValue >= threshold.min) return Measurable(MeasureUnit.MINUTES, minutes)
    }

    scope.seconds?.let { threshold ->
        val seconds = duration.toDouble(DurationUnit.SECONDS).roundToInt()
        if (seconds.absoluteValue >= threshold.min) return Measurable(MeasureUnit.SECONDS, seconds)
    }

    return null
}

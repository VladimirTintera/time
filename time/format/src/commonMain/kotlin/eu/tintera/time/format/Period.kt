package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone

internal fun platformPeriodFormat(
    period: DateTimePeriod,
    format: DateTimePeriodFormat,
    locale: AppLocale
): String {

    val scope = DateTimePeriodFormatScope(period, locale)
    format.block(scope)

    val calendar = scope.calendar
    val clock = scope.clock

    if (calendar.isEmpty() && clock.isEmpty())
        throw EmptyFormatConfigurationException("Period format can't be empty.")

    val list = buildList {
        calendar.years?.ifAvailable({ period.years != 0 }) { add(Measurable(MeasureUnit.YEARS, period.years)) }
        calendar.months?.ifAvailable({ period.months != 0 }) { add(Measurable(MeasureUnit.MONTHS, period.months)) }
        calendar.days?.ifAvailable({ period.days != 0 }) { add(Measurable(MeasureUnit.DAYS, period.days)) }
        clock.hours?.ifAvailable({ period.hours != 0 }) { add(Measurable(MeasureUnit.HOURS, period.hours)) }
        clock.minutes?.ifAvailable({ period.minutes != 0 }) { add(Measurable(MeasureUnit.MINUTES, period.minutes)) }
        clock.seconds?.ifAvailable({ period.seconds != 0 }) { add(Measurable(MeasureUnit.SECONDS, period.seconds)) }
    }

    return nativePeriodFormat(
        style = scope.style,
        items = scope.maxUnitsCount?.let {
            list.take(it)
        } ?: list,
        locale = locale
    )
}

internal expect fun nativePeriodFormat(
    style: FormatStyle,
    items: List<Measurable>,
    locale: AppLocale
): String
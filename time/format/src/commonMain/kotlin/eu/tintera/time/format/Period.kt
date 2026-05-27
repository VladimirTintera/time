package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.DateTimePeriod

internal fun platformPeriodFormat(
    period: DateTimePeriod,
    style: FormatStyle,
    maxUnitsCount: Int?,
    calendar: CalendarComponents?,
    clock: ClockComponents?,
    locale: AppLocale
): String {

    if (calendar?.years == null && calendar?.months == null && calendar?.days == null && clock?.hours == null && clock?.minutes == null && clock?.seconds == null)
        throw EmptyFormatConfigurationException("Period format can't be empty.")

    val list = buildList {
        calendar?.years?.ifAvailable({ period.years > 0 }) { add(Measurable(MeasureUnit.YEARS, period.years)) }
        calendar?.months?.ifAvailable({ period.months > 0 }) { add(Measurable(MeasureUnit.MONTHS, period.months)) }
        calendar?.days?.ifAvailable({ period.days > 0 }) { add(Measurable(MeasureUnit.DAYS, period.days)) }
        clock?.hours?.ifAvailable({ period.hours > 0 }) { add(Measurable(MeasureUnit.HOURS, period.hours)) }
        clock?.minutes?.ifAvailable({ period.minutes > 0 }) { add(Measurable(MeasureUnit.MINUTES, period.minutes)) }
        clock?.seconds?.ifAvailable({ period.seconds > 0 }) { add(Measurable(MeasureUnit.SECOND, period.seconds)) }
    }

    return nativePeriodFormat(
        style = style,
        items = maxUnitsCount?.let {
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
package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDateTime

internal fun platformDateTimeFormat(
    date: LocalDateTime,
    locale: AppLocale,
    dateFormat: DateFormat?,
    timeFormat: TimeFormat?
): String {
    when {
        dateFormat == null && timeFormat == null -> throw EmptyFormatConfigurationException("Date and time format cannot be empty.")
        dateFormat != null && timeFormat != null -> {
            if (dateFormat.isEmpty() && timeFormat.isEmpty())
                throw EmptyFormatConfigurationException("Time format cannot be empty. At least one time component must be configured.")
        }

        dateFormat != null -> if (dateFormat.isEmpty()) throw EmptyFormatConfigurationException("Date format cannot be empty. At least one date component must be configured.")
        timeFormat != null -> if (timeFormat.isEmpty()) throw EmptyFormatConfigurationException("Time format cannot be empty. At least one time component must be configured.")
    }

    return nativeDateTimeFormat(
        date = date,
        locale = locale,
        skeleton = cldrSkeleton(
            dateFormat = dateFormat,
            timeFormat = timeFormat
        ),
        timeFormat = timeFormat,
        dateFormat = dateFormat,
    )
}


private fun DateFormat.isEmpty() = weekDay == null && day == null && month == null && year == null

internal expect fun nativeDateTimeFormat(
    date: LocalDateTime,
    locale: AppLocale,
    dateFormat: DateFormat?,
    timeFormat: TimeFormat?,
    skeleton: String
): String

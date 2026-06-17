package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.locale.languageTag
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

internal data class FormatterKey(
    val skeleton: String,
    val localeKey: String
)

internal expect val formatterCache: Cache<FormatterKey, DateTimeFormatter>

internal fun platformDateTimeFormat(
    date: LocalDateTime,
    locale: AppLocale,
    timeZone: TimeZone,
    format: DateTimeFormat,
    dateRequired: Boolean,
    timeRequired: Boolean,
): String {

    val dateTimeFormatScope = DateTimeFormatScope(
        value = date,
        date = date.date,
        time = date.time,
        locale = locale,
        timeZone = timeZone,
    )

    format.block(dateTimeFormatScope)

    when {
        dateTimeFormatScope.dateFormatScope.isEmpty() && dateTimeFormatScope.timeFormatScope.isEmpty() -> throw EmptyFormatConfigurationException(
            "Date and time format cannot be empty."
        )

        timeRequired && dateTimeFormatScope.timeFormatScope.isEmpty() -> {
            throw EmptyFormatConfigurationException("Time format cannot be empty. At least one time component must be configured.")
        }

        dateRequired && dateTimeFormatScope.dateFormatScope.isEmpty() -> {
            throw EmptyFormatConfigurationException("Date format cannot be empty. At least one date component must be configured.")
        }
    }

    val skeleton = dateTimeFormatScope.cldrSkeleton()

    val formatter = formatterCache.getOrPut(
        FormatterKey(
            skeleton = skeleton,
            localeKey = locale.languageTag
        )
    ) {
        createDateTimeFormatterFactory(
            locale = locale
        ).formatter(skeleton, dateTimeFormatScope.dateFormatScope, dateTimeFormatScope.timeFormatScope)
    }

    return formatter.format(date)
}

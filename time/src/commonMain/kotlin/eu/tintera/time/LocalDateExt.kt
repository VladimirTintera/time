package eu.tintera.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

/**
 * Formats this [LocalDate] into a string representation using the specified format.
 *
 * This function internally converts the [LocalDate] to a [LocalDateTime] at midnight
 * to apply the [DateFormat].
 *
 * @param format The [DateFormat] configuration to apply.
 * @return The formatted date string.
 */
fun LocalDate.format(format: DateFormat): String = formatDateTime(
    LocalDateTime(
        year = year,
        month = month.number,
        day = day,
        hour = 0, minute = 0, second = 0, nanosecond = 0
    ),
    format = DateTimeFormatImpl(dateFormat = format, timeFormat = null)
)

/**
 * Formats this [LocalDate] into a string representation using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateFormat] on-the-fly
 * using a DSL and apply it to the [LocalDate].
 *
 * @param block The DSL block for configuring the [DateFormat].
 * @return The formatted date string.
 */
fun LocalDate.format(block: DateFormatBuilder.() -> Unit) = format(
    format = date(
        base = null,
        block = block
    )
)

/**
 * Formats this [LocalDate] to return just the localized name of its month.
 *
 * By default, it returns the full month name (e.g., "April", "duben").
 * If `abbrev` is true, it returns the abbreviated name (e.g., "Apr", "dub").
 *
 * @param abbrev Whether to return the abbreviated month name. Defaults to false.
 * @return The formatted month name string.
 */
fun LocalDate.formatMonthName(abbrev: Boolean = false): String = format(
    date {
        day = null
        year = null
        weekDay = null
        month = if (abbrev) MonthFormat.ShortName else MonthFormat.FullName
    }
)

/**
 * Formats this [LocalDate] to return just the localized name of its day of the week.
 *
 * By default, it returns the full weekday name (e.g., "Wednesday", "středa").
 * If `abbrev` is true, it returns the abbreviated name (e.g., "Wed", "st").
 *
 * @param abbrev Whether to return the abbreviated weekday name. Defaults to false.
 * @return The formatted weekday name string.
 */
fun LocalDate.formatWeekDayName(abbrev: Boolean = false): String = format(
    date {
        day = null
        year = null
        month = null
        weekDay = if (abbrev) WeekDayFormat.ShortName else WeekDayFormat.FullName
    }
)

/**
 * Formats the interval between this [LocalDate] and another [LocalDate] as a string.
 *
 * This function is useful for displaying a date range, such as the start and end of an event.
 *
 * @param to The end of the date interval.
 * @param format The [DateFormat] to apply to both the start and end of the interval.
 * @return The formatted interval string.
 */
fun LocalDate.formatInterval(
    to: LocalDate,
    format: DateFormat
) = formatInterval(
    from = LocalDateTime(
        year = year,
        month = month.number,
        day = day,
        hour = 0, minute = 0, second = 0, nanosecond = 0
    ),
    to = LocalDateTime(
        year = to.year,
        month = to.month.number,
        day = to.day,
        hour = 0, minute = 0, second = 0, nanosecond = 0
    ),
    format = dateTimeFormat {
        date {
            from(format)
        }
    }
)

/**
 * Formats the interval between this [LocalDate] and another [LocalDate] using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateFormat] on-the-fly
 * using a DSL and apply it to the date interval.
 *
 * @param to The end of the date interval.
 * @param block The DSL block for configuring the [DateFormat].
 * @return The formatted interval string.
 */
fun LocalDate.formatInterval(
    to: LocalDate,
    block: DateFormatBuilder.() -> Unit
) = formatInterval(
    to = to,
    format = date(
        base = null,
        block = block
    )
)

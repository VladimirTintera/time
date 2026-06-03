package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.*

/**
 * Formats this [LocalDate] into a string representation using the specified format.
 *
 * This function internally converts the [LocalDate] to a [LocalDateTime] at midnight
 * to apply the [DateFormat].
 *
 * Example:
 * ```kotlin
 * val date = LocalDate(2024, 1, 1)
 * val format = DateFormat {
 *     year = YearFormat.FourDigits
 *     month = MonthFormat.Name.Full
 *     day = DayFormat.Numeric
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = date.format(format, myLocale)
 * // formatted will be "January 1, 2024" (depending on locale)
 * ```
 *
 * @param format The [DateFormat] configuration to apply.
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted date string.
 */
fun LocalDate.format(
    format: DateFormat,
    locale: AppLocale
): String = platformDateTimeFormat(
    LocalDateTime(
        year = year,
        month = month.number,
        day = day,
        hour = 0, minute = 0, second = 0, nanosecond = 0
    ),
    locale = locale,
    format = DateTimeFormat {
        date { from(format) }
    },
    timeRequired = false,
    dateRequired = true
)

/**
 * Formats this [LocalDate] into a string representation using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateFormat] on-the-fly
 * using a DSL and apply it to the [LocalDate].
 *
 * Example:
 * ```kotlin
 * val date = LocalDate(2024, 1, 1)
 * val czLocale = localeForLanguageTag("cs-CZ")
 * val formatted = date.format(czLocale) {
 *     year = YearFormat.TwoDigits
 *     month = MonthFormat.Digital.Padded
 *     day = DayFormat.Padded
 * }
 * // formatted will be "01/01/24" (depending on locale)
 * ```
 *
 * @param locale The [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [DateFormat].
 * @return The formatted date string.
 */
fun LocalDate.format(
    locale: AppLocale,
    block: DateFormatScope<LocalDate>.() -> Unit = DateFormatScope.defaultConfig()
) = format(
    format = DateFormat(block),
    locale = locale
)

/**
 * Formats this [LocalDate] to return just the localized name of its month.
 *
 * Example:
 * ```kotlin
 * val date = LocalDate(2024, 4, 1)
 * val myLocale = localeForLanguageTag("en-US")
 * val monthName = date.formatMonthName(MonthFormat.Name.Full, myLocale)
 * // monthName will be "April"
 * val monthNameAbbr = date.formatMonthName(MonthFormat.Name.Short, myLocale)
 * // monthNameAbbr will be "Apr"
 * ```
 *
 * @param format The desired [MonthFormat.Name] style.
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted month name string.
 */
fun LocalDate.formatMonthName(
    format: MonthFormat.Name,
    locale: AppLocale
): String = format(locale) {
    day = null
    year = null
    weekDay = null
    month = format
}

/**
 * Formats this [LocalDate] to return just the localized name of its day of the week.
 *
 * Example:
 * ```kotlin
 * val date = LocalDate(2024, 1, 1) // A Monday
 * val myLocale = localeForLanguageTag("en-US")
 * val dayName = date.formatWeekDayName(locale = myLocale)
 * // dayName will be "Monday"
 * val dayNameAbbr = date.formatWeekDayName(WeekDayFormat.ShortName, myLocale)
 * // dayNameAbbr will be "Mon"
 * ```
 *
 * @param format The desired [WeekDayFormat] style. Defaults to [WeekDayFormat.FullName].
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted weekday name string.
 */
fun LocalDate.formatWeekDayName(
    format: WeekDayFormat = WeekDayFormat.FullName,
    locale: AppLocale
): String = format(locale = locale) {
    day = null
    year = null
    month = null
    weekDay = format
}

/**
 * Formats the interval between this [LocalDate] and another [LocalDate] as a string.
 *
 * This function is useful for displaying a date range, such as the start and end of an event.
 *
 * Example:
 * ```kotlin
 * val start = LocalDate(2024, 1, 10)
 * val end = LocalDate(2024, 1, 20)
 * val format = DateFormat {
 *     month = MonthFormat.Name.Short
 *     day = DayFormat.Numeric
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = start.formatInterval(
 *     to = end,
 *     format = format,
 *     locale = myLocale,
 *     timeZone = TimeZone.UTC
 * )
 * // formatted will be "Jan 10 – 20" (depending on locale)
 * ```
 *
 * @param to The end of the date interval.
 * @param format The [DateFormat] to apply to both the start and end of the interval.
 * @param locale The [AppLocale] to use for formatting.
 * @param timeZone The time zone to use.
 * @param onSameDate Custom combiner for events on the same day.
 * @param onSameMonth Custom combiner for events in the same month.
 * @param onSameYear Custom combiner for events in the same year.
 * @param onDifferentDate Custom combiner for multi-day events.
 * @return The formatted interval string.
 */
fun LocalDate.formatInterval(
    to: LocalDate,
    format: DateIntervalFormat,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner()
) = formatInterval(
    from = LocalDateTime(
        year = year,
        month = month.number,
        day = day,
        hour = 0, minute = 0, second = 0, nanosecond = 0
    ).toInstant(timeZone),
    to = LocalDateTime(
        year = to.year,
        month = to.month.number,
        day = to.day,
        hour = 0, minute = 0, second = 0, nanosecond = 0
    ).toInstant(timeZone),
    format = DateTimeIntervalFormat {
        date { from(format) }
    },
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
)

/**
 * Formats the interval between this [LocalDate] and another [LocalDate] using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the date interval.
 *
 * Example:
 * ```kotlin
 * val start = LocalDate(2024, 1, 10)
 * val end = LocalDate(2024, 1, 20)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = start.formatInterval(
 *     to = end,
 *     locale = myLocale,
 *     timeZone = TimeZone.UTC
 * ) {
 *     month = MonthFormat.Name.Short
 *     day = DayFormat.Numeric
 * }
 * // formatted will be "Jan 10 – 20" (depending on locale)
 * ```
 *
 * @param to The end of the date interval.
 * @param locale The [AppLocale] to use for formatting.
 * @param timeZone The time zone to use.
 * @param onSameDate Custom combiner for events on the same day.
 * @param onSameMonth Custom combiner for events in the same month.
 * @param onSameYear Custom combiner for events in the same year.
 * @param onDifferentDate Custom combiner for multi-day events.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted interval string.
 */
fun LocalDate.formatInterval(
    to: LocalDate,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
    block: DateFormatScope<OpenEndRange<LocalDate>>.() -> Unit = DateFormatScope.defaultConfig()
) = formatInterval(
    to = to,
    format = DateIntervalFormat(block),
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
)

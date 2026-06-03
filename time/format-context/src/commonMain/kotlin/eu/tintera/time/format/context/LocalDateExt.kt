package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

/**
 * Formats this [LocalDate] into a string representation using the specified format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
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
 * val formatted = with(myLocale) {
 *     date.format(format)
 * }
 * ```
 *
 * @param format The format to use for string conversion.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun LocalDate.format(
    format: DateFormat
): String = format(format, locale)

/**
 * Formats this [LocalDate] into a string representation using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val date = LocalDate(2024, 1, 1)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = with(myLocale) {
 *     date.format {
 *         year = YearFormat.FourDigits
 *         month = MonthFormat.Name.Full
 *         day = DayFormat.Numeric
 *     }
 * }
 * ```
 *
 * @param block The builder block to configure the date format.
 * @return The formatted localized string.
 */
context(locale: AppLocale)
fun LocalDate.format(
    block: DateFormatScope<LocalDate>.() -> Unit = DateFormatScope.defaultConfig()
): String = format(locale, block)

/**
 * Formats this [LocalDate] to return just the localized name of its month.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val date = LocalDate(2024, 4, 1)
 * val myLocale = localeForLanguageTag("en-US")
 * val monthName = with(myLocale) {
 *     date.formatMonthName(MonthFormat.Name.Full)
 * }
 * ```
 *
 * @param format The format style for the month name.
 * @return The localized month name.
 */
context(locale: AppLocale)
fun LocalDate.formatMonthName(
    format: MonthFormat.Name
): String = formatMonthName(format, locale)

/**
 * Formats this [LocalDate] to return just the localized name of its day of the week.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] context
 * to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val date = LocalDate(2024, 1, 1)
 * val myLocale = localeForLanguageTag("en-US")
 * val dayName = with(myLocale) {
 *     date.formatWeekDayName()
 * }
 * ```
 *
 * @param format The format style for the weekday name. Defaults to [WeekDayFormat.FullName].
 * @return The localized weekday name.
 */
context(locale: AppLocale)
fun LocalDate.formatWeekDayName(
    format: WeekDayFormat = WeekDayFormat.FullName
): String = formatWeekDayName(format, locale)

/**
 * Formats the interval between this [LocalDate] and another [LocalDate] as a string.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
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
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     start.formatInterval(end, format)
 * }
 * ```
 *
 * @param to The end point of the interval.
 * @param format The format to use for formatting the start and end dates.
 * @param onSameDate The combiner logic when both dates are the same.
 * @param onSameMonth The combiner logic when dates fall in the same month of the same year.
 * @param onSameYear The combiner logic when dates fall in the same year.
 * @param onDifferentDate The combiner logic when dates fall in different years.
 * @return The formatted localized interval string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun LocalDate.formatInterval(
    to: LocalDate,
    format: DateIntervalFormat,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner()
): String = formatInterval(
    to = to,
    format = format,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate
)

/**
 * Formats the interval between this [LocalDate] and another [LocalDate] using a DSL-configured format.
 *
 * This function is context-aware and automatically uses the implicit [AppLocale] and
 * [TimeZone] contexts to resolve formatting.
 *
 * Example:
 * ```kotlin
 * val start = LocalDate(2024, 1, 10)
 * val end = LocalDate(2024, 1, 20)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     start.formatInterval(end) {
 *         month = MonthFormat.Name.Short
 *         day = DayFormat.Numeric
 *     }
 * }
 * ```
 *
 * @param to The end point of the interval.
 * @param onSameDate The combiner logic when both dates are the same.
 * @param onSameMonth The combiner logic when dates fall in the same month of the same year.
 * @param onSameYear The combiner logic when dates fall in the same year.
 * @param onDifferentDate The combiner logic when dates fall in different years.
 * @param block The builder block to configure the date format.
 * @return The formatted localized interval string.
 */
context(locale: AppLocale, timeZone: TimeZone)
fun LocalDate.formatInterval(
    to: LocalDate,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
    block: DateFormatScope<OpenEndRange<LocalDate>>.() -> Unit = DateFormatScope.defaultConfig()
): String = formatInterval(
    to = to,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
    block = block
)


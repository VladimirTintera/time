package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Instant

/**
 * Formats this [LocalDateTime] into a string representation using the specified format.
 *
 * Example:
 * ```kotlin
 * val dateTime = LocalDateTime(2024, 1, 1, 12, 30)
 * val format = DateTimeFormat {
 *     date { short() }
 *     time { short() }
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = dateTime.format(format, myLocale)
 * // formatted will be "1/1/24, 12:30 PM" (depending on locale)
 * ```
 *
 * @param format The [DateTimeFormat] configuration to apply.
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted date-time string.
 */
@Deprecated(
    message = "Use the overload that explicitly requires a TimeZone, which is needed for contextual evaluation inside the format scope.",
    replaceWith = ReplaceWith("this.format(format, locale, TimeZone.currentSystemDefault())", "import kotlinx.datetime.TimeZone")
)
fun LocalDateTime.format(
    format: DateTimeFormat,
    locale: AppLocale
): String = format(
    format = format,
    locale = locale,
    timeZone = TimeZone.currentSystemDefault()
)

fun LocalDateTime.format(
    format: DateTimeFormat,
    locale: AppLocale,
    timeZone: TimeZone
): String = platformDateTimeFormat(
    date = this,
    format = format,
    locale = locale,
    dateRequired = false,
    timeRequired = false,
    timeZone = timeZone
)



/**
 * Formats this [LocalDateTime] into a string representation using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the [LocalDateTime].
 *
 * Example:
 * ```kotlin
 * val dateTime = LocalDateTime(2024, 1, 1, 12, 30)
 * val czLocale = localeForLanguageTag("cs-CZ")
 * val formatted = dateTime.format(czLocale) {
 *     date { full() }
 *     time { short() }
 * }
 * // formatted will be "Monday, January 1, 2024, 12:30 PM" (depending on locale)
 * ```
 *
 * @param locale The [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted date-time string.
 */
@Deprecated(
    message = "Use the overload that explicitly requires a TimeZone, which is needed for contextual evaluation inside the format scope.",
    replaceWith = ReplaceWith("this.format(locale, TimeZone.currentSystemDefault(), block)", "import kotlinx.datetime.TimeZone")
)
fun LocalDateTime.format(
    locale: AppLocale,
    block: DateTimeFormatScope<LocalDateTime, LocalDate, LocalTime>.() -> Unit = DateTimeFormatScope.defaultConfig()
) = format(
    format = DateTimeFormat(block),
    locale = locale,
    timeZone = TimeZone.currentSystemDefault()
)

fun LocalDateTime.format(
    locale: AppLocale,
    timeZone: TimeZone,
    block: DateTimeFormatScope<LocalDateTime, LocalDate, LocalTime>.() -> Unit = DateTimeFormatScope.defaultConfig()
) = format(
    format = DateTimeFormat(block),
    locale = locale,
    timeZone = timeZone
)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime].
 *
 * This function provides a human-readable representation of the time difference
 * between this [LocalDateTime] and a reference point.
 *
 * Example:
 * ```kotlin
 * val now = LocalDateTime(2024, 1, 1, 12, 0)
 * val past = LocalDateTime(2024, 1, 1, 11, 0)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = past.formatRelative(
 *     now = now,
 *     timeZone = TimeZone.UTC,
 *     format = RelativeDateTimeFormat { hours() },
 *     locale = myLocale
 * )
 * // formatted will be "1 hour ago"
 * ```
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for the conversion to [Instant].
 * @param format The [RelativeDateTimeFormat] configuration specifying style and thresholds.
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    timeZone: TimeZone,
    format: RelativeDateTimeFormat,
    locale: AppLocale
): String = platformRelativeTimeFormat(
    target = this.toInstant(timeZone),
    now = now.toInstant(timeZone),
    timeZone = timeZone,
    format = format,
    locale = locale
)

/**
 * Formats this [LocalDateTime] as a relative time string from another [LocalDateTime] using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * val now = LocalDateTime(2024, 1, 1, 12, 0)
 * val past = LocalDateTime(2024, 1, 1, 11, 0)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = past.formatRelative(
 *     now = now,
 *     timeZone = TimeZone.UTC,
 *     locale = myLocale
 * ) {
 *     hours()
 * }
 * // formatted will be "1 hour ago"
 * ```
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for the conversion to [Instant].
 * @param locale The [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [RelativeDateTimeFormat].
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun LocalDateTime.formatRelative(
    now: LocalDateTime,
    timeZone: TimeZone,
    locale: AppLocale,
    block: RelativeDateTimeFormatScope.() -> Unit = RelativeDateTimeFormatScope.defaultConfig
): String = platformRelativeTimeFormat(
    target = this.toInstant(timeZone),
    now = now.toInstant(timeZone),
    timeZone = timeZone,
    format = RelativeDateTimeFormat(block),
    locale = locale
)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] as a string.
 *
 * This function is useful for displaying a time range, such as the start and end of an event.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2024, 1, 1, 10, 0)
 * val end = LocalDateTime(2024, 1, 1, 12, 0)
 * val format = DateTimeFormat { time { short() } }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = start.formatInterval(
 *     to = end,
 *     format = format,
 *     locale = myLocale,
 *     timeZone = TimeZone.UTC
 * )
 * // formatted will be "10:00 AM – 12:00 PM" (depending on locale)
 * ```
 *
 * @param to The end of the time interval.
 * @param format The [DateTimeFormat] to apply to both the start and end of the interval.
 * @param locale The [AppLocale] to use for formatting.
 * @param timeZone The time zone to use.
 * @param onSameDate Custom combiner for events on the same day.
 * @param onSameMonth Custom combiner for events in the same month.
 * @param onSameYear Custom combiner for events in the same year.
 * @param onDifferentDate Custom combiner for multi-day events.
 * @return The formatted interval string.
 */
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    format: DateTimeIntervalFormat,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner()
) = formatInterval(
    from = this.toInstant(timeZone),
    to = to.toInstant(timeZone),
    format = format,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
)

/**
 * Formats the interval between this [LocalDateTime] and another [LocalDateTime] using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the time interval.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2024, 1, 1, 10, 0)
 * val end = LocalDateTime(2024, 1, 1, 12, 0)
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = start.formatInterval(
 *     to = end,
 *     locale = myLocale,
 *     timeZone = TimeZone.UTC
 * ) {
 *     time { short() }
 * }
 * // formatted will be "10:00 AM – 12:00 PM" (depending on locale)
 * ```
 *
 * @param to The end of the time interval.
 * @param locale The [AppLocale] to use for formatting.
 * @param timeZone The time zone to use.
 * @param onSameDate Custom combiner for events on the same day.
 * @param onSameMonth Custom combiner for events in the same month.
 * @param onSameYear Custom combiner for events in the same year.
 * @param onDifferentDate Custom combiner for multi-day events.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted interval string.
 */
fun LocalDateTime.formatInterval(
    to: LocalDateTime,
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
    block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = DateTimeFormatScope.defaultConfig()
) = formatInterval(
    from = this.toInstant(timeZone),
    to = to.toInstant(timeZone),
    format = DateTimeIntervalFormat(block),
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate,
)

/**
 * Formats this range of [LocalDateTime] into a localized interval string.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2025, 4, 15, 10, 0)
 * val end = LocalDateTime(2025, 4, 15, 12, 0)
 * val range = start..<end
 * val format = DateTimeFormat { time { short() } }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = range.format(
 *     locale = myLocale,
 *     timeZone = TimeZone.UTC,
 *     format = format
 * )
 * // formatted will be "10:00 AM – 12:00 PM" (depending on locale)
 * ```
 *
 * @param locale The [AppLocale] to use for formatting.
 * @param timeZone The time zone to use.
 * @param format The format to use for formatting individual date-times.
 * @param onSameDate The combiner logic when both date-times fall on the same date.
 * @param onSameMonth The combiner logic when both date-times fall in the same month of the same year.
 * @param onSameYear The combiner logic when both date-times fall in the same year.
 * @param onDifferentDate The combiner logic when date-times fall on different dates.
 * @return The formatted localized interval string.
 */
fun OpenEndRange<LocalDateTime>.format(
    locale: AppLocale,
    timeZone: TimeZone,
    format: DateTimeIntervalFormat,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner()
) = start.formatInterval(
    to = endExclusive,
    format = format,
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate
)

/**
 * Formats this range of [LocalDateTime] into a localized interval string using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2025, 4, 15, 10, 0)
 * val end = LocalDateTime(2025, 4, 15, 12, 0)
 * val range = start..<end
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = range.format(
 *     locale = myLocale,
 *     timeZone = TimeZone.UTC
 * ) {
 *     time { short() }
 * }
 * // formatted will be "10:00 AM – 12:00 PM" (depending on locale)
 * ```
 *
 * @param locale The [AppLocale] to use for formatting.
 * @param timeZone The time zone to use.
 * @param onSameDate The combiner logic when both date-times fall on the same date.
 * @param onSameMonth The combiner logic when both date-times fall in the same month of the same year.
 * @param onSameYear The combiner logic when both date-times fall in the same year.
 * @param onDifferentDate The combiner logic when date-times fall on different dates.
 * @param block The builder block to configure the date-time format.
 * @return The formatted localized interval string.
 */
fun OpenEndRange<LocalDateTime>.format(
    locale: AppLocale,
    timeZone: TimeZone,
    onSameDate: SameDayCombiner = defaultSameDayCombiner(),
    onSameMonth: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onSameYear: DifferentDateCombiner = defaultDifferentDateCombiner(),
    onDifferentDate: DifferentDateTimeCombiner = defaultDifferentDateTimeCombiner(),
    block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = DateTimeFormatScope.defaultConfig()
) = start.formatInterval(
    to = endExclusive,
    format = DateTimeIntervalFormat(block),
    locale = locale,
    timeZone = timeZone,
    onSameDate = onSameDate,
    onSameMonth = onSameMonth,
    onSameYear = onSameYear,
    onDifferentDate = onDifferentDate
)

package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.*
import kotlin.time.Instant

/**
 * Formats this [Instant] into a string representation using the specified format and time zone.
 *
 * This extension function simplifies the process of converting an [Instant] to a human-readable
 * string by handling the conversion to a [LocalDateTime] based on the provided time zone.
 *
 * Example:
 * ```kotlin
 * val instant = Instant.parse("2024-01-01T12:00:00Z")
 * val format = DateTimeFormat {
 *     date { long() }
 *     time { short() }
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = instant.format(format, TimeZone.of("America/New_York"), myLocale)
 * // formatted will be "January 1, 2024 at 7:00 AM" (depending on locale)
 * ```
 *
 * @param format The [DateTimeFormat] configuration to apply.
 * @param timeZone The time zone to use for formatting.
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted date-time string.
 */
fun Instant.format(
    format: DateTimeFormat,
    timeZone: TimeZone,
    locale: AppLocale,
) = platformDateTimeFormat(
    date = toLocalDateTime(timeZone),
    locale = locale,
    format = format,
    dateRequired = false,
    timeRequired = false
)

/**
 * Formats this [Instant] into a string representation using a DSL-configured format and the specified time zone.
 *
 * This extension function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the [Instant].
 *
 * Example:
 * ```kotlin
 * val instant = Instant.parse("2024-01-01T12:00:00Z")
 * val czLocale = localeForLanguageTag("cs-CZ")
 * val formatted = instant.format(TimeZone.of("Europe/Prague"), czLocale) {
 *     date { full() }
 * }
 * // formatted will be "Monday, January 1, 2024" (depending on locale)
 * ```
 *
 * @param timeZone The time zone to use for formatting.
 * @param locale The [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted date-time string.
 */
fun Instant.format(
    timeZone: TimeZone,
    locale: AppLocale,
    block: DateTimeFormatScope<LocalDateTime, LocalDate, LocalTime>.() -> Unit = DateTimeFormatScope.defaultConfig()
) = format(
    locale = locale,
    timeZone = timeZone,
    format = DateTimeFormat(block),
)

/**
 * Formats this [Instant] as a relative time string from the current moment.
 *
 * This function provides a human-readable representation of the time difference
 * between this [Instant] and a reference point (by default, the current system time).
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.minutes
 * import kotlinx.datetime.Clock
 * import kotlinx.datetime.TimeZone
 *
 * val now = Clock.System.now()
 * val fiveMinutesAgo = now - 5.minutes
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = fiveMinutesAgo.formatRelative(
 *     now = now,
 *     timeZone = TimeZone.UTC,
 *     locale = myLocale,
 *     format = RelativeDateTimeFormat { minutes() }
 * )
 * // formatted will be "5 minutes ago"
 * ```
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for relative calculations.
 * @param format The [RelativeDateTimeFormat] configuration specifying style and thresholds.
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun Instant.formatRelative(
    now: Instant,
    timeZone: TimeZone,
    locale: AppLocale,
    format: RelativeDateTimeFormat,
): String = platformRelativeTimeFormat(
    target = this,
    now = now,
    format = format,
    locale = locale,
    timeZone = timeZone
)

/**
 * Formats this [Instant] as a relative time string from the current moment using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.minutes
 * import kotlinx.datetime.Clock
 * import kotlinx.datetime.TimeZone
 *
 * val now = Clock.System.now()
 * val fiveMinutesAgo = now - 5.minutes
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = fiveMinutesAgo.formatRelative(
 *     now = now,
 *     timeZone = TimeZone.UTC,
 *     locale = myLocale
 * ) {
 *     minutes()
 * }
 * // formatted will be "5 minutes ago"
 * ```
 *
 * @param now The reference point for calculating the relative time.
 * @param timeZone The time zone to use for relative calculations.
 * @param locale The [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [RelativeDateTimeFormat].
 * @return The formatted relative time string (e.g., "in 5 minutes", "2 hours ago").
 */
fun Instant.formatRelative(
    now: Instant,
    timeZone: TimeZone,
    locale: AppLocale,
    block: RelativeDateTimeFormatScope.() -> Unit
) = formatRelative(
    now = now,
    timeZone = timeZone,
    format = RelativeDateTimeFormat(block),
    locale = locale,
)

/**
 * Formats the interval between this [Instant] and another [Instant] as a string.
 *
 * This function is useful for displaying a time range, such as the start and end of an event.
 *
 * Example:
 * ```kotlin
 * val start = Instant.parse("2024-01-01T10:00:00Z")
 * val end = Instant.parse("2024-01-01T12:30:00Z")
 * val format = DateTimeFormat { time { short() } }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = start.formatInterval(
 *     to = end,
 *     format = format,
 *     timeZone = TimeZone.UTC,
 *     locale = myLocale
 * )
 * // formatted will be "10:00 AM – 12:30 PM" (depending on locale)
 * ```
 *
 * @param to The end of the time interval.
 * @param format The [DateTimeFormat] to apply to both the start and end of the interval.
 * @param timeZone The time zone to use for formatting.
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted interval string.
 */
fun Instant.formatInterval(
    to: Instant,
    format: DateTimeIntervalFormat,
    timeZone: TimeZone,
    locale: AppLocale
) = formatInterval(
    from = this,
    to = to,
    format = format,
    locale = locale,
    timeZone = timeZone
)

/**
 * Formats the interval between this [Instant] and another [Instant] using a DSL-configured format.
 *
 * This function provides a convenient way to define a [DateTimeFormat] on-the-fly
 * using a DSL and apply it to the time interval.
 *
 * Example:
 * ```kotlin
 * val start = Instant.parse("2024-05-20T10:00:00Z")
 * val end = Instant.parse("2024-05-21T12:30:00Z")
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = start.formatInterval(
 *     to = end,
 *     timeZone = TimeZone.UTC,
 *     locale = myLocale
 * ) {
 *     date {
 *         month = MonthFormat.Name.Short
 *         day = DayFormat.Numeric
 *     }
 * }
 * // formatted will be "May 20 – 21" (depending on locale)
 * ```
 *
 * @param to The end of the time interval.
 * @param timeZone The time zone to use for formatting.
 * @param locale The [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [DateTimeFormat].
 * @return The formatted interval string.
 */
fun Instant.formatInterval(
    to: Instant,
    timeZone: TimeZone,
    locale: AppLocale,
    block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = DateTimeFormatScope.defaultConfig()
) = formatInterval(
    from = this,
    to = to,
    format = DateTimeIntervalFormat(block),
    locale = locale,
    timeZone = timeZone
)

/**
 * Formats this range of [Instant] into a localized interval string.
 *
 * Example:
 * ```kotlin
 * val start = Instant.parse("2024-01-01T10:00:00Z")
 * val end = Instant.parse("2024-01-01T12:00:00Z")
 * val range = start..<end
 * val format = DateTimeIntervalFormat { time { short() } }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = range.format(format, TimeZone.UTC, myLocale)
 * // formatted will be "10:00 AM – 12:00 PM"
 * ```
 *
 * @param format The [DateTimeIntervalFormat] configuration to apply.
 * @param timeZone The time zone to use for formatting.
 * @param locale The [AppLocale] to use.
 * @return The formatted interval string.
 */
fun OpenEndRange<Instant>.format(
    format: DateTimeIntervalFormat,
    timeZone: TimeZone,
    locale: AppLocale
) = formatInterval(
    from = this.start,
    to = this.endExclusive,
    format = format,
    timeZone = timeZone,
    locale = locale,
)

/**
 * Formats this range of [Instant] into a localized interval string using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * val start = Instant.parse("2024-01-01T10:00:00Z")
 * val end = Instant.parse("2024-01-01T12:00:00Z")
 * val range = start..<end
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = range.format(TimeZone.UTC, myLocale) {
 *     time { short() }
 * }
 * // formatted will be "10:00 AM – 12:00 PM"
 * ```
 *
 * @param timeZone The time zone to use for formatting.
 * @param locale The [AppLocale] to use.
 * @param block The DSL block for configuring the [DateTimeIntervalFormat].
 * @return The formatted interval string.
 */
fun OpenEndRange<Instant>.format(
    timeZone: TimeZone,
    locale: AppLocale,
    block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = DateTimeFormatScope.defaultConfig()
) = formatInterval(
    from = this.start,
    to = this.endExclusive,
    format = DateTimeIntervalFormat(block),
    timeZone = timeZone,
    locale = locale,
)
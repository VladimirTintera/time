package eu.tintera.time.core

import kotlinx.datetime.*
import kotlin.time.Duration

/**
 * Builder class for modifying [LocalDateTime] values using a domain-specific language (DSL).
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val modified = ldt.modify(TimeZone.UTC) {
 *     plusYears(1)
 *     plusMonths(2)
 *     plusDays(3)
 *     withTime(14, 30)
 * }
 * ```
 */
@TimeDslMarker
class LocalDateTimeModifierBuilder internal constructor(
    initial: LocalDateTime,
    timeZone: TimeZone
) {
    private var currentTimeZone = timeZone
    private var currentDate: LocalDate = initial.date
    private var currentTime: LocalTime = initial.time

    /**
     * Sets the time zone to be used for subsequent date-time operations that require zone-aware calculations.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     timeZone(TimeZone.of("Europe/Prague"))
     * }
     * ```
     *
     * @param timeZone The time zone to use.
     */
    fun timeZone(timeZone: TimeZone) {
        currentTimeZone = timeZone
    }

    /**
     * Adds the specified number of years to the current date.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     plusYears(5)
     * }
     * ```
     *
     * @param value The number of years to add.
     */
    fun plusYears(value: Int) {
        currentDate = currentDate.plus(value, DateTimeUnit.YEAR)
    }

    /**
     * Subtracts the specified number of years from the current date.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     minusYears(5)
     * }
     * ```
     *
     * @param value The number of years to subtract.
     */
    fun minusYears(value: Int) = plusYears(-value)

    /**
     * Adds the specified number of months to the current date.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     plusMonths(3)
     * }
     * ```
     *
     * @param value The number of months to add.
     */
    fun plusMonths(value: Int) {
        currentDate = currentDate.plus(value, DateTimeUnit.MONTH)
    }

    /**
     * Subtracts the specified number of months from the current date.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     minusMonths(3)
     * }
     * ```
     *
     * @param value The number of months to subtract.
     */
    fun minusMonths(value: Int) = plusMonths(-value)

    /**
     * Adds the specified number of days to the current date.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     plusDays(10)
     * }
     * ```
     *
     * @param value The number of days to add.
     */
    fun plusDays(value: Int) {
        currentDate = currentDate.plus(value, DateTimeUnit.DAY)
    }

    /**
     * Subtracts the specified number of days from the current date.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     minusDays(10)
     * }
     * ```
     *
     * @param value The number of days to subtract.
     */
    fun minusDays(value: Int) = plusDays(-value)

    /**
     * Adds the time duration of the specified [LocalTime] to the current date-time.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     plusTime(LocalTime(1, 30))
     * }
     * ```
     *
     * @param time The local time duration to add.
     */
    fun plusTime(time: LocalTime) {
        val nextLdt = build().plus(time, currentTimeZone)
        currentDate = nextLdt.date
        currentTime = nextLdt.time
    }

    /**
     * Adds the specified hours, minutes, seconds, and nanoseconds to the current date-time.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     plusTime(hours = 2, minutes = 30)
     * }
     * ```
     *
     * @param hours The number of hours to add.
     * @param minutes The number of minutes to add.
     * @param seconds The number of seconds to add.
     * @param nanoseconds The number of nanoseconds to add.
     */
    fun plusTime(hours: Int = 0, minutes: Int = 0, seconds: Int = 0, nanoseconds: Int = 0) = plus(
        DateTimePeriod(hours = hours, minutes = minutes, seconds = seconds, nanoseconds = nanoseconds.toLong())
    )

    /**
     * Adds the specified Kotlin [Duration] to the current date-time.
     *
     * Example:
     * ```kotlin
     * import kotlin.time.Duration.Companion.hours
     *
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     plusDuration(2.hours)
     * }
     * ```
     *
     * @param duration The duration to add.
     */
    fun plusDuration(duration: Duration) {
        val nextLdt = build().plus(duration, currentTimeZone)
        currentDate = nextLdt.date
        currentTime = nextLdt.time
    }

    /**
     * Sets the time component to the specified hour, minute, second, and nanosecond.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     withTime(hour = 14, minute = 30)
     * }
     * ```
     *
     * @param hour The hour of the day (0-23).
     * @param minute The minute of the hour (0-59).
     * @param second The second of the minute (0-59).
     * @param nanosecond The nanosecond of the second (0-999_999_999).
     */
    fun withTime(hour: Int = 0, minute: Int = 0, second: Int = 0, nanosecond: Int = 0) {
        currentTime = LocalTime(hour, minute, second, nanosecond)
    }

    /**
     * Sets the date component to the specified year, month, and day.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     withDate(2026, 5, 20)
     * }
     * ```
     *
     * @param year The year.
     * @param month The month of the year (1-12).
     * @param day The day of the month (1-31).
     */
    fun withDate(year: Int, month: Int, day: Int) {
        currentDate = LocalDate(year, month, day)
    }

    /**
     * Adds the specified [DateTimePeriod] to the current date-time.
     *
     * Example:
     * ```kotlin
     * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
     * val period = DateTimePeriod(months = 2, days = 5)
     * val modified = ldt.modify(TimeZone.UTC) {
     *     plus(period)
     * }
     * ```
     *
     * @param period The period to add.
     */
    fun plus(period: DateTimePeriod) {
        val nextLdt = build().plus(period, currentTimeZone)
        currentDate = nextLdt.date
        currentTime = nextLdt.time
    }

    internal fun build(): LocalDateTime {
        return LocalDateTime(currentDate, currentTime)
    }
}

/**
 * Modifies this [LocalDateTime] in the specified [TimeZone] using a builder DSL.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val modified = ldt.modify(TimeZone.UTC) {
 *     plusDays(5)
 *     withTime(10, 0)
 * }
 * ```
 *
 * @param timeZone The time zone in which zone-aware operations are performed.
 * @param block The builder block.
 * @return The modified [LocalDateTime].
 */
fun LocalDateTime.modify(
    timeZone: TimeZone,
    block: LocalDateTimeModifierBuilder.() -> Unit
): LocalDateTime = LocalDateTimeModifierBuilder(
    initial = this,
    timeZone = timeZone
).apply(block).build()

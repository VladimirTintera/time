package eu.tintera.time.core

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Extracts the date portion of this [DateTimePeriod] as a [DatePeriod].
 *
 * This includes the years, months, and days from this period.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(years = 1, months = 2, days = 3, hours = 4)
 * val datePart = period.datePeriod
 * ```
 */
val DateTimePeriod.datePeriod: DatePeriod
    get() = DatePeriod(
        years = years,
        months = months,
        days = days
    )

/**
 * Extracts the time portion of this [DateTimePeriod] as a Kotlin [Duration].
 *
 * This calculates the duration represented by the hours, minutes, seconds,
 * and nanoseconds components of this period.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(hours = 2, minutes = 30)
 * val durationPart = period.timeDuration
 * ```
 */
val DateTimePeriod.timeDuration: Duration
    get() = hours.hours + minutes.minutes + seconds.seconds + nanoseconds.nanoseconds

/**
 * Creates a [DateTimePeriod] representing the specified number of years.
 *
 * Example:
 * ```kotlin
 * val period = 3.periodYears
 * ```
 */
val Int.periodYears: DateTimePeriod get() = DateTimePeriod(years = this)

/**
 * Creates a [DateTimePeriod] representing the specified number of months.
 *
 * Example:
 * ```kotlin
 * val period = 6.periodMonths
 * ```
 */
val Int.periodMonths: DateTimePeriod get() = DateTimePeriod(months = this)

/**
 * Creates a [DateTimePeriod] representing the specified number of days.
 *
 * Example:
 * ```kotlin
 * val period = 10.periodDays
 * ```
 */
val Int.periodDays: DateTimePeriod get() = DateTimePeriod(days = this)

/**
 * Creates a [DateTimePeriod] representing the specified number of hours.
 *
 * Example:
 * ```kotlin
 * val period = 4.periodHours
 * ```
 */
val Int.periodHours: DateTimePeriod get() = DateTimePeriod(hours = this)

/**
 * Creates a [DateTimePeriod] representing the specified number of minutes.
 *
 * Example:
 * ```kotlin
 * val period = 30.periodMinutes
 * ```
 */
val Int.periodMinutes: DateTimePeriod get() = DateTimePeriod(minutes = this)

/**
 * Creates a [DateTimePeriod] representing the specified number of seconds.
 *
 * Example:
 * ```kotlin
 * val period = 45.periodSeconds
 * ```
 */
val Int.periodSeconds: DateTimePeriod get() = DateTimePeriod(seconds = this)

/**
 * Creates a [DateTimePeriod] representing the specified number of nanoseconds.
 *
 * Example:
 * ```kotlin
 * val period = 500L.periodNanoseconds
 * ```
 */
val Long.periodNanoseconds: DateTimePeriod get() = DateTimePeriod(nanoseconds = this)

/**
 * Returns a new [DateTimePeriod] with all components negated.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(days = 5)
 * val negativePeriod = -period
 * ```
 *
 * @return The negated [DateTimePeriod].
 */
operator fun DateTimePeriod.unaryMinus(): DateTimePeriod = DateTimePeriod(
    years = -years,
    months = -months,
    days = -days,
    hours = -hours,
    minutes = -minutes,
    seconds = -seconds,
    nanoseconds = -nanoseconds.toLong() // 👑 Tady je 'nanoseconds' typu Int, ale Kotlin to sám přetypuje na Long pro konstruktor
)


